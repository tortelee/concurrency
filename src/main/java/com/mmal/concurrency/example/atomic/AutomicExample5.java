package com.mmal.concurrency.example.atomic;

import com.mmal.concurrency.annotations.ThreadSafe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
@Slf4j
public class AutomicExample5 {
    private static AtomicIntegerFieldUpdater<AutomicExample5> updater = AtomicIntegerFieldUpdater.newUpdater(AutomicExample5.class,"count");

    @Getter
    public volatile int count = 100 ;

    private static AutomicExample5 example5 = new AutomicExample5();
    public static void main(String[] args) {
        if(updater.compareAndSet(example5,100,120)){
            log.info("update success count:{}", example5.getCount());
        }

        if(updater.compareAndSet(example5,100,120)){
            log.info("update success count:{}", example5.getCount());
        }else{
            log.info("second update failed");
        }

       // log.info("count:{}", count.get());
    }

}
