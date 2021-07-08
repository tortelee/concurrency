package com.mmal.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SynchronizedExample2 {

    // 修饰一个类代码块
    public void test1(int j){
        synchronized (SynchronizedExample2.class){  //synchronized 类
            for(int i=0;i<10;i++){
                log.info("test1- {} - {}", i, j );
            }
        }
    }

     // 修饰一个静态方法

    public static synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2 - {} - {}", i , j);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample2 example1 = new SynchronizedExample2();
        SynchronizedExample2 example2 = new SynchronizedExample2();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
           // example1.test1();
            example1.test1(1); // 乱序，两个对象
           // example1.test2(1);

        });

        //线程池，不等上面的结束，就继续执行下面的代码块
        executorService.execute(()->{
            // example1.test1();
            example2.test1(2);  // 乱序，两个对象
        //    example2.test2(2);

        });
    }
}
