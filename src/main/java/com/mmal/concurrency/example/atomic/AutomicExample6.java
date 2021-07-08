package com.mmal.concurrency.example.atomic;

import com.mmal.concurrency.annotations.ThreadSafe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@ThreadSafe
@Slf4j
public class AutomicExample6 {
    private static AtomicBoolean isHappened = new AtomicBoolean(false);
    public static int clientTotal =5000;
    //执行并发的线程数
    public static  int thread_Total =200;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(thread_Total);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal); //
        for(int i= 0; i<clientTotal; i++){
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    test();
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
       // log.info("count:{}", count);
    }

    private static void test(){
        if(isHappened.compareAndSet(false,true)){
            log.info("executed, isHappened:{}", isHappened.get());
        }
    }


}
