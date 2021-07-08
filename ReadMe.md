### chapter2
#### 环境搭建
### 并发模拟工具
- postman
- AB apache bench
- Jmeter
- 代码

postman

##### 代码
CountDownLatch  线程执行完，进行其他处理
- 是一个线程等待其他线程类执行完之后执行。
- 是通过一个计数器来实现的，计数器的初始值是线程的数量，每当一个
线程执行完毕，计算器的数值就减去1，当计算器为0时，表示所有的线程执行完毕。
  然后闭锁上等待的线程就可以恢复工作了。
  
Semaphore: 阻塞进程，控制线程数量

### 3-1 线程安全性，原子性 atomic
定义： 当多个线程访问一个类时，不管运行时环境采用<font color='red'>何种调度方式</font>或者这些进行将如何交替执行，
并且子主调代码中<font color='red'>不需要额外的同步和协同</font>，这个类都表现出<font colot='red'>正确行为</font>，
那么就称这个类是线程安全带的.

主要体现在三个方面
- 原子性： 提供互斥，同一时刻，只能一个线程对它进行操作
- 可见性： 对主内存修改，被其他线程观察到
- 有序性： 一个线程观察其他线程中的指令执行顺序，由于指令重排的存在
该观察结果一般是杂乱无序的
  
example
#### Atomic包

unsafe类，
```java
public final int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {
            v = getIntVolatile(o, offset);// 底层的值
        } while (!weakCompareAndSetInt(o, offset, v, v + delta));
        return v;
    }
```
继续查找，可以看到一个native方法，即不是java实现的方法
```java
public final native boolean compareAndSetInt(Object o, long offset,
                                                 int expected,
                                                 int x);
```

1, 当前o取出的offset值，是否跟底层取出的值,v一样，如果一样，那么
返回v+d, d就是你想要加的值
2，当前取出的值offset跟底层取出的不一样，就是说，中间被别的线程
更改了，此时就不可以更新底层数据，要重新再取数。

##### AutomicLong LongAdder
为什么单独研究这个类？
jdk8 流行， jdk8新增一个类，跟automic long 很像。LongAdder

优点：之前cas ,死循环，影响性能。
longaddr，性能更好，在大数据时，误差有点。

##### 包的其他
- 方法除了`compareint`之外，还有`compareAndSet`, 用于
atomicBoolean. {希望某件事情只执行一次，执行之后就变为false。当
  使用atomicBoolean,时，可以控制。甚至只有一个线程会执行此代码
  }
  
##### AtomicReference, AtomicReferenceFieldUpdater
atomicReferenceFileUpdater  修改更新某个类的某个字段的值
要求这个字段必须时voilate修饰，不能是static字段才可以。

##### AtomicStampReference: CAS 的ABA 问题。
aba: 其他线程将a改为B,又改回a，此时测试线程发现a没变，于是
进行更新。与设计思想不符合。

解决设计思想：每次更改数值，需要将版本号加1.

多了一个stamp比较

##### atomicLongArray
`getAndSet(int i ,long newValue)`
i is the index of the array.

###3-3 线程安全性，原子性，synchronized
除了原子包提供线程安全之外，还有一种，提供原子性，就是锁。

jdk 提供锁有两种
- synchronized: 依赖JVM
- Lock: 代码层次的锁，依赖特殊cpu指令，代码实现，ReentrantLock

#### synchronized
语法的使用
- 修饰代码块：大括号括起来的代码，作用于<font color="red">调用的对象</font>
- 修饰方法，（被修饰的方法称为同步方法），作用于<font color="red">调用对象</font>
- 修饰静态方法： 整个静态方法，作用于这个类的<font color="red">所有对象</font>
- 修饰类： 括号括起来的部分，作用于这个类的所有对象

Note
- 一个函数内部的方法块都是synchronized 跟这个函数synchronized,
一致。
  
- 父类有synchronized，子类没有synchronize。原因是synchronize
不属于方法声明的一部分。
#### Lock

#### 对比
- synchronized: 不可中断的锁，适合竞争不激烈，可读性好
- Lock: 可中断的锁，多样化同步，竞争激烈时能维持常态。
- Atomic: 竞争激烈时能维持常态，比Lock性能好；只能同步一个值



### Reference
#### long 读写
64位 拆成两个 32位的读写。