package com.example.newfeatures.stream;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 2 * @Author: miaodongbiao
 * 3 * @Date: 2018/7/21 17:21
 * 4   创建流的几种方式
 */
public class CreateStreamTest {


    /**
     * 使用Stream的静态方法创建流
     * of方法：有两个overload方法，一个接受变长参数，一个接受单一值
     *
     */
    @Test
    public void createStreamByStream(){
        Stream stream = Stream.of("a","b","c");

        String [] arr = {"a","b","c","d"};
        String [] arr2 = new String [5];

        Stream stream2 = Stream.of(arr2);

        Stream stream1 = Stream.of("a");

        System.out.println(stream.count());
        System.out.println(stream2.count());
        System.out.println(stream1.count());

        new Thread(() -> System.out.println("haha"));

    }

    /**
     * 通过数组工具类创建流
     * 查看java.util.Arrays的源码可看到，有多个重载的stream方法
     */
    @Test
    public void createStreamByArray(){
        String [] arr = {"a","b","c","d"};
        Stream stream = Arrays.stream(arr);
        System.out.println(stream.count());
    }


    /**
     * 使用Collection创建流
     * Collection是集合的一个根接口，在Collection下有两个生成Stream方法,所以其所有子类都都可以获取对应的Stream对象 .
     * 有两个方法
     *      1，stream()
     *      2, parallelStream()
     */
    @Test
    public void createStreamByCollection(){

        List<String> list = new ArrayList();
        list.add("zhangsan");
        list.add("lisi");
        list.add("wangwu");

        Stream stream = list.stream();
        System.out.println(stream.count());

    }

    /**
     *  parallelstream    parallel的翻译是平行得，并行的
     *  所以parallelstream可以称作并行流，并行流提高性能
     *
     *  并发和并行的区别：
     *      并发是指一个处理器同时处理多个任务。
     *      并行是指多个处理器或者是多核的处理器同时处理多个不同的任务。
     *      并发是逻辑上的同时发生（simultaneous），而并行是物理上的同时发生。
     *      来个比喻：并发是一个人同时吃三个馒头，而并行是三个人同时吃三个馒头。
     */
    @Test
    public void createStreamByCollection2(){
        List<String> list = new ArrayList();
        list.add("zhangsan");
        list.add("lisi");
        list.add("wangwu");
        Stream stream = list.parallelStream();
        // TODO: 2018/7/21  parallelstream理解
    }

    /**
     * 其他创建流的方式
     * - BufferedReader   lines()
     * - Random.ints()
     * - BitSet.stream()
     * - Pattern.splitAsStream(java.lang.CharSequence)
     * - JarFile.stream()
     */
}
