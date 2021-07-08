package com.mmal.concurrency.example.count;

import com.mmal.concurrency.annotations.NotThreadSafe;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@NotThreadSafe
@Slf4j
public class CountExample4 {
    // 请求总数
    public static int clientTotal =5000;
    //执行并发的线程数
    public static  int thread_Total =200;

    public static int count =0;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(thread_Total);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal); //
        for(int i= 0; i<clientTotal; i++){
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception", e);
                    e.printStackTrace();
                }

                countDownLatch.countDown(); //每次线程执行完，就减少一个
            });
        }
        try {
            countDownLatch.await(); // 等待所有的countDownlatch结束，那么必然最后减为0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        log.info("count:{}", count);
    }
  // 修饰类的方法
    private static void add(){
        synchronized (CountExample4.class){
            count++;    //本质是这里不安全
        }

    }


}
