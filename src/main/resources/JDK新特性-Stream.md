# 		JDK新特性-Stream

## 1，Stream

### 	1.1 Stream的定义

```
	A sequence of elements supporting sequential and parallel aggregate
```

​	通过查看Stream源码（1.8），我们可以看到这样的定义。通俗的理解就是：

​		支持并行和并行聚合的元素序列                      - - 来自百度翻译  可忽略。



Stream（流）是一个来自数据源的元素队列并支持聚合操作      - - 参考 菜鸟教程

- 元素是特定类型的对象，形成一个队列。 Java中的Stream并不会存储元素，而是按需计算。
- **数据源** 流的来源。 可以是集合，数组，I/O channel， 产生器generator 等。
- **聚合操作** 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。

和以前的Collection操作不同， Stream操作还有两个基础的特征：

- **Pipelining**: 中间操作都会返回流对象本身。 这样多个操作可以串联成一个管道， 如同流式风格（fluent style）。 这样做可以对操作进行优化， 比如延迟执行(laziness)和短路( short-circuiting)。

- **内部迭代**： 以前对集合遍历都是通过Iterator或者For-Each的方式, 显式的在集合外部进行迭代， 这叫做外部迭代。 Stream提供了内部迭代的方式， 通过访问者模式(Visitor)实现。	

  

  ### 1.2 Stream的用法

  简单说，对 Stream 的使用就是实现一个 filter-map-reduce 过程，产生一个最终结果，或者导致一个副作用（side effect）。 

  当我们使用一个流的时候，通常包括三个基本步骤： 

获取一个数据源（source）→ 数据转换→执行操作获取想要的结果，每次转换原有 Stream 对象不改变，返回一个新的 Stream 对象（可以有多次转换），这就允许对其操作可以像链条一样排列，变成一个管道，如下图所示。 

```java
List<Integer> nums = Lists.newArrayList(1,null,3,4,null,6);
nums.stream().filter(num -> num != null).count();

```

上面这段代码是获取一个List中，元素不为null的个数。这段代码虽然很简短，但是却是一个很好的入门级别的例子来体现如何使用Stream，正所谓“麻雀虽小五脏俱全”。我们现在开始深入解刨这个例子，完成以后你可能可以基本掌握Stream的用法！ 

![](https://github.com/zayebuza/static-resources/blob/master/JDK%E6%96%B0%E7%89%B9%E6%80%A7/Stream%E9%80%9A%E7%94%A8%E8%AF%AD%E6%B3%95.jpg)

图片就是对于Stream例子的一个解析，可以很清楚的看见：原本一条语句被三种颜色的框分割成了三个部分。红色框中的语句是一个Stream的生命开始的地方，负责创建一个Stream实例；绿色框中的语句是赋予Stream灵魂的地方，把一个Stream转换成另外一个Stream，红框的语句生成的是一个包含所有nums变量的Stream，进过绿框的filter方法以后，重新生成了一个过滤掉原nums列表所有null以后的Stream；蓝色框中的语句是丰收的地方，把Stream的里面包含的内容按照某种算法来汇聚成一个值，例子中是获取Stream中包含的元素个数。如果这样解析以后，还不理解，那就只能动用“核武器”–图形化，一图抵千言！ 

![](https://github.com/zayebuza/static-resources/blob/master/JDK%E6%96%B0%E7%89%B9%E6%80%A7/T2BbhlXFBXXXXXXXXX_!!90219132.jpg)

在此我们总结一下使用Stream的基本步骤：

1. 创建Stream；

2. 转换Stream，每次转换原有Stream对象不改变，返回一个新的Stream对象（**可以有多次转换**）；

3. 对Stream进行聚合（Reduce）操作，获取想要的结果；

   ### 1.3 创建Stream的方式

   

   1，使用Stream的静态方法

   ​	of方法：有两个overload方法，一个接受变长参数，一个接受单一值 

   ```java
   
       /**
        * Returns a sequential {@code Stream} containing a single element.
        *
        * @param t the single element
        * @param <T> the type of stream elements
        * @return a singleton sequential stream
        */
       public static<T> Stream<T> of(T t) {
           return StreamSupport.stream(new Streams.StreamBuilderImpl<>(t), false);
       }
   ```

   ```java
    /**
        * Returns a sequential ordered stream whose elements are the specified values.
        *
        * @param <T> the type of stream elements
        * @param values the elements of the new stream
        * @return the new stream
        */
       @SafeVarargs
       @SuppressWarnings("varargs") // Creating a stream from an array is safe
       public static<T> Stream<T> of(T... values) {
           return Arrays.stream(values);
       }
   ```

   

   2，使用集合Collection

   ​	Collection是集合的一个根接口，在Collection下有两个生成Stream方法,所以其所有子类都都可以获取对应的Stream对象 .

   ```java
   
       /**
        * Returns a sequential {@code Stream} with this collection as its source.
        *
        * <p>This method should be overridden when the {@link #spliterator()}
        * method cannot return a spliterator that is {@code IMMUTABLE},
        * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
        * for details.)
        *
        * @implSpec
        * The default implementation creates a sequential {@code Stream} from the
        * collection's {@code Spliterator}.
        *
        * @return a sequential {@code Stream} over the elements in this collection
        * @since 1.8
        */
       default Stream<E> stream() {
           return StreamSupport.stream(spliterator(), false);
       }
   ```

   ```java
    /**
        * Returns a possibly parallel {@code Stream} with this collection as its
        * source.  It is allowable for this method to return a sequential stream.
        *
        * <p>This method should be overridden when the {@link #spliterator()}
        * method cannot return a spliterator that is {@code IMMUTABLE},
        * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
        * for details.)
        *
        * @implSpec
        * The default implementation creates a parallel {@code Stream} from the
        * collection's {@code Spliterator}.
        *
        * @return a possibly parallel {@code Stream} over the elements in this
        * collection
        * @since 1.8
        */
       default Stream<E> parallelStream() {
           return StreamSupport.stream(spliterator(), true);
       }
   ```

   3，使用数组（查看java.util.Arrays的源码可看到，有多个重载的方法，这里只复制出来一个）

   ```java
    /**
        * Returns a sequential {@link Stream} with the specified array as its
        * source.
        *
        * @param <T> The type of the array elements
        * @param array The array, assumed to be unmodified during use
        * @return a {@code Stream} for the array
        * @since 1.8
        */
       public static <T> Stream<T> stream(T[] array) {
           return stream(array, 0, array.length);
       }
   ```

   4,使用 BufferedReader 

   ```java
       /**
        * Returns a {@code Stream}, the elements of which are lines read from
        * this {@code BufferedReader}.  The {@link Stream} is lazily populated,
        * i.e., read only occurs during the
        * <a href="../util/stream/package-summary.html#StreamOps">terminal
        * stream operation</a>.
        *
        * <p> The reader must not be operated on during the execution of the
        * terminal stream operation. Otherwise, the result of the terminal stream
        * operation is undefined.
        *
        * <p> After execution of the terminal stream operation there are no
        * guarantees that the reader will be at a specific position from which to
        * read the next character or line.
        *
        * <p> If an {@link IOException} is thrown when accessing the underlying
        * {@code BufferedReader}, it is wrapped in an {@link
        * UncheckedIOException} which will be thrown from the {@code Stream}
        * method that caused the read to take place. This method will return a
        * Stream if invoked on a BufferedReader that is closed. Any operation on
        * that stream that requires reading from the BufferedReader after it is
        * closed, will cause an UncheckedIOException to be thrown.
        *
        * @return a {@code Stream<String>} providing the lines of text
        *         described by this {@code BufferedReader}
        *
        * @since 1.8
        */
       public Stream<String> lines() {
           Iterator<String> iter = new Iterator<String>() {
               String nextLine = null;
   
               @Override
               public boolean hasNext() {
                   if (nextLine != null) {
                       return true;
                   } else {
                       try {
                           nextLine = readLine();
                           return (nextLine != null);
                       } catch (IOException e) {
                           throw new UncheckedIOException(e);
                       }
                   }
               }
   
               @Override
               public String next() {
                   if (nextLine != null || hasNext()) {
                       String line = nextLine;
                       nextLine = null;
                       return line;
                   } else {
                       throw new NoSuchElementException();
                   }
               }
           };
           return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                   iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
       }
   ```

   5，其他

   - Random.ints()
   - BitSet.stream()
   - Pattern.splitAsStream(java.lang.CharSequence)
   - JarFile.stream()

### Stream的操作

流的操作类型分为两种：

- **Intermediate**：一个流可以后面跟随零个或多个 intermediate 操作。其目的主要是打开流，做出某种程度的数据映射/过滤，然后返回一个新的流，交给下一个操作使用。这类操作都是惰性化的（lazy），就是说，仅仅调用到这类方法，并没有真正开始流的遍历。
- **Terminal**：一个流只能有一个 terminal 操作，当这个操作执行后，流就被使用“光”了，无法再被操作。所以这必定是流的最后一个操作。Terminal 操作的执行，才会真正开始流的遍历，并且会生成一个结果，或者一个 side effect。

在对于一个 Stream 进行多次转换操作 (Intermediate 操作)，每次都对 Stream 的每个元素进行转换，而且是执行多次，这样时间复杂度就是 N（转换次数）个 for 循环里把所有操作都做掉的总和吗？其实不是这样的，转换操作都是 lazy 的，多个转换操作只会在 Terminal 操作的时候融合起来，一次循环完成。我们可以这样简单的理解，Stream 里有个操作函数的集合，每次转换操作就是把转换函数放入这个集合中，在 Terminal 操作的时候循环 Stream 对应的集合，然后对每个元素执行所有的函数。

还有一种操作被称为 **short-circuiting**。用以指：

- 对于一个 intermediate 操作，如果它接受的是一个无限大（infinite/unbounded）的 Stream，但返回一个有限的新 Stream。
- 对于一个 terminal 操作，如果它接受的是一个无限大的 Stream，但能在有限的时间计算出结果。

当操作一个无限大的 Stream，而又希望在有限时间内完成操作，则在管道内拥有一个 short-circuiting 操作是必要非充分条件。

接下来，当把一个数据结构包装成 Stream 后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。

- Intermediate：

map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered

- Terminal：

forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator

- Short-circuiting：

anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit





























参考：

http://ifeve.com/stream/

http://www.runoob.com/java/java8-streams.html

https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/