package com.mmal.concurrency.example.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SynchronizedExample1 {

    // 修饰代码块
    public void test1(int j){
        synchronized (this){
            for(int i=0;i<10;i++){
                log.info("test1- {} - {}", i, j );
            }
        }
    }

     // 修饰一个方法

    public synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2 - {} - {}", i , j);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 example1 = new SynchronizedExample1();
        SynchronizedExample1 example2 = new SynchronizedExample1();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(()->{
           // example1.test1();
          //  example1.test1(1); // 乱序，两个对象
            example1.test2(1);

        });

        //线程池，不等上面的结束，就继续执行下面的代码块
        executorService.execute(()->{
            // example1.test1();
          //  example2.test1(2);  // 乱序，两个对象
            example2.test2(2);

        });
    }
}
