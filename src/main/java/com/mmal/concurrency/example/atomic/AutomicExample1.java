package com.mmal.concurrency.example.atomic;

import com.mmal.concurrency.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Slf4j
public class AutomicExample1 {
    // 请求总数
    public static int clientTotal =5000;
    //执行并发的线程数
    public static  int thread_Total =200;

    public static AtomicInteger count = new AtomicInteger(0);

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

    private static void add(){
        count.incrementAndGet();    //本质是这里不安全
    //    count.getAndIncrement();    // 返回值会不一样
    }


}
