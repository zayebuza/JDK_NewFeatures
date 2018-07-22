package com.example.newfeatures.stream;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * 2 * @Author: miaodongbiao
 * 3 * @Date: 2018/7/21 18:33
 * 4   流的操作  创建流之后的转换
 *      转换Stream其实就是把一个Stream通过某些行为转换成一个新的Stream
 */
public class TransformStreamTest {
    String [] arr = {"a","b","c","d","a","d"};
    Stream stream = Arrays.stream(arr);
    //@Before
    public void initStream(){

        arr =new String[] {"a","b","c","d"};
    }

    /**
     *  distinct: 对于Stream中包含的元素进行去重操作（去重逻辑依赖元素的equals方法），新生成的Stream中没有重复的元素；
     *
     */
    @Test
    public void distinctTest(){
        System.out.println("下面一行是distinct之后的集合元素：");
        stream.distinct().forEach(System.out::print);

    }

    /**
     * filter: 对于Stream中包含的元素使用给定的过滤函数进行过滤操作，新生成的Stream只包含符合条件的元素；
     */
    @Test
    public void filterTest(){
        System.out.println("下面一行是filter之后的结果,去掉等于a的元素");
        stream.filter(p -> !p.equals("a")).forEach(System.out::print);
    }

    /**
     * map: 对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。
     * 这个方法有三个对于原始类型的变种方法，分别是：mapToInt，mapToLong和mapToDouble。
     * 这三个方法也比较好理解，比如mapToInt就是把原始Stream转换成一个新的Stream，这个新生成的Stream中的元素都是int类型。
     * 之所以会有这样三个变种方法，可以免除自动装箱/拆箱的额外消耗；
     */
    @Test
    public void mapTest(){
        System.out.println("下面一行是map之后的结果，所有元素+a：");
        stream.map(p -> p + "a").forEach(System.out::print);

    }

    /**
     *  flatMap：和map类似，不同的是其每个元素转换得到的是Stream对象，会把子Stream中的元素压缩到父集合中；
     */
    @Test
    public void flatMapTest(){

    }


    /**
     *  peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；
     */
    @Test
    public void peekTest(){

    }


    /**
     *  limit: 对一个Stream进行截断操作，获取其前N个元素，如果原Stream中包含的元素个数小于N，那就获取其所有的元素；
     */
    @Test
    public void limitTest(){
        System.out.println("我是limit之后的结果，取出两个元素");
        stream.limit(2).forEach(System.out::print);
    }


    /**
     * skip: 返回一个丢弃原Stream的前N个元素后剩下元素组成的新Stream，如果原Stream中包含的元素个数小于N，那么返回空Stream；
     */
    @Test
    public void skipTest(){

    }

    /**
     * 快速生成10个1-100之间的正整数并排序
     */
    @Test
    public void initNums(){
        new Random().ints(1,100).limit(10).sorted().forEach(System.out::println);
    }
}
