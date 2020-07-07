package com.gcy.bootwithutils;

import com.gcy.bootwithutils.vo.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BootwithutilsApplicationTests {

    /*
     * 两种常用的阻塞队列 基本用法类似 但是底层数据结构
     * ArrayBlockingQueue 是数组 只有一个锁 本质上不是并发进行入队出队操作
     * LinkedBlockingQueue 是链表 两个所 一个控制入队 一个控制出队 并发进行操作
     * 此队列声明时要指定容量，否则队列容量就是Integer.MAX_VALUE 在入队速率大于出队速率时容易耗尽内存
     * 出/入并发高 使用ArrayBlockingQueue
     * 均并发高 使用LinkedBlockingQueue
     */
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
    private static BlockingQueue<Integer> linked_queue = new LinkedBlockingQueue<>(10);

    @Test
    public void BubbleSort(){
        int[] arr = {3,5,6,9,13,1};
        for(int i = 0; i < arr.length; i++){
            for(int j = arr.length - 1; j > i; j--){
                int max;
               if(arr[j-1] > arr[j]){
                   max = arr[j];
                   arr[j] = arr[i];
                   arr[i] = max;
               }
            }
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void SelectSort(){
        int[] arr = {3,5,6,9,13,1};
        for(int i = 0; i < arr.length; i++){
            int min = arr[i];
            for(int j = i + 1; j < arr.length; j++){
                if(arr[i] > arr[j]){
                    min = arr[j];
                    arr[j] = arr[i];
                    arr[i] = min;
                }
            }
            System.out.println(Arrays.toString(arr));
        }

    }

    @Test
    public void InsertSort(){
        int[] arr = {13,11,6,7,1};
        for(int i = 1; i < arr.length; i++){
            int item = arr[i];
            int insertPoint = i - 1;
            //两两比较交换
            while(insertPoint >= 0 && arr[insertPoint] > item){
                arr[insertPoint + 1] = arr[insertPoint];
                insertPoint--;
            }
            arr[insertPoint + 1] = item;
            System.out.println(Arrays.toString(arr));
        }
    }


    /*
     * @Author gcy
     * @Description 强引用，最常用到，所有的new对象都是强引用，
     * 即使是断开引用关系，内存溢出，也不会轻易回收，需要显式地弱化为null，即可被回收
     * @Date 16:45 2020/6/19
     * @Param []
     * @return void
     **/
    @Test
    public void StrongReference(){
        Object o1 = new Object();
        Object o2 = o1;
        o1 = null;
        System.gc();
        System.out.println("StrongReference After GC:" +o2);
    }

    /*
     * @Author gcy
     * @Description 软引用，使用 java.lang.ref.SoftReference 包来声明，内存空间足够时，不会被回收，
     * 只有当内存空间不足时才会被回收，常用来做缓存功能。
     * @Date 16:49 2020/6/19
     * @Param []
     * @return void
     **/
    @Test
    public void SoftReference(){
        String str = new String("SoftReferenceStringValue");
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        //定义软引用队列，当软引用对象被回收，就会被加入到队列中
        SoftReference<String> softReference = new SoftReference<>(str,referenceQueue);
        str = null;
        System.gc();
        System.out.println("SoftReference After GC:" +softReference.get());

    }

    /*
     * @Author gcy
     * @Description 弱引用，使用 java.lang.ref.WeakReference 包来声明，不论内存空间是否足够，都进行回收
     * @Date 16:50 2020/6/19
     * @Param []
     * @return void
     **/
    @Test
    public void WeakReference(){
        Person person = new Person("eddie",'M',20, "owner");
        ReferenceQueue<Person> referenceQueue = new ReferenceQueue<>();
        WeakReference<Person> weakReference = new WeakReference<>(person, referenceQueue);
        System.gc();
        //弱引用被清理后会立刻储存到关联的弱引用队列中
        System.out.println("WeakReference After GC:" +weakReference.get());
        //转为强引用继续使用
        Person newPerson = weakReference.get();
        System.out.println("Exchange WeakReference to StrongReference " + newPerson);
    }

    /*
     * @Author gcy
     * @Description 虚引用 随时可能被回收 必须跟随引用队列一起使用 主要用于跟踪对象被垃圾回收的活动
     * @Date 13:21 2020/6/21
     * @Param []
     * @return void
     **/
    @Test
    public void PhantomReference(){
        ReferenceQueue queue = new ReferenceQueue();
        String str = new String("123");
        PhantomReference phantomReference = new PhantomReference(str,queue);
        System.out.println("PhantomReference Before GC:" + phantomReference.get());
        System.gc();
        //回收后 虚引用对象加入队列中
        System.out.println("PhantomReference After GC:" + phantomReference);
    }

    class ItemThread implements Runnable{
        private Random random = new Random();
        ConcurrentHashMap<String, List<Integer>> map;

        public ItemThread(ConcurrentHashMap<String, List<Integer>> map) {
            this.map = map;
        }

        @Override
        public void run() {
            for(int i = 0; i < 5; i++){
                execute();
            }
        }

        public void execute(){
            try {
                String thread = Thread.currentThread().getName();
                List<Integer> list = new ArrayList<>();
                //按照线程名 寻找map的key
                if (map.get(thread) == null || map.get(thread).isEmpty()){
                    list.add(random.nextInt(100));
                    map.put(thread, list);
                }else {
                    list = map.get(thread);
                    list.add(random.nextInt(100));
                }

                System.out.println("Thread: "+ thread +" added "+ list.get(list.size()-1));
                Thread.sleep(1000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ThreadPool_FixedThreadPool(){
        ConcurrentHashMap<String, List<Integer>> map = new ConcurrentHashMap<>();
        //线程池大小
        Integer poolSize = 3;
        //线程个数
        Integer threadNum = 3;
        //创建一个固定的线程池，并规定最大并发数，超过的线程会等待
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        System.out.println("Starting ...");
        for (int i = 0; i < threadNum; i++) {
            //按照线程个数 提交入线程池
            executorService.submit(new ItemThread(map));
        }
        //等待各个子线程关闭后关闭线程池
        executorService.shutdown();
        //executorService.shutdownNow(); 不等待 直接关闭
        try {
            //一直等待 直到线程池状态为Termination或者达到指定时间 此处是1天
            executorService.awaitTermination(1, TimeUnit.DAYS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        for(Map.Entry<String,List<Integer>> entry : map.entrySet()){
            //输出
            System.out.println("\nThread "+entry.getKey());
            System.out.println("Value ");
            for(Integer integer : entry.getValue()){
                System.out.print(integer+"\t");
            }
        }

        /* 【RESULT】
          Thread pool-2-thread-2
          Value
          23	52	76	19	34
          Thread pool-2-thread-1
          Value
          97	98	7	87	28
          Thread pool-2-thread-3
          Value
          38	9	73	67	18
         **/
    }

    /*
     * @Author gcy
     * @Description 并发阻塞队列 BlockingQueue 生产者（入队列操作）
     * @Date 19:00 2020/7/3
     * @Param []
     * @return void
     **/
    private static void producer() throws InterruptedException {
        Random random = new Random();
        Thread.sleep(5000);
        while (true) {
            int temp = random.nextInt(100);
            //使用offer方法不会阻塞当前线程，所以依旧能看到线程isAlive
            Thread t = Thread.currentThread();
            System.out.println(t.isAlive());
            if(queue.offer(temp,1000,TimeUnit.MILLISECONDS)){
                System.out.println("producer add "+ temp);
            }else{
                System.out.println("queue is full waiting for consumer");
            }

            //使用put方法会阻塞当前线程 只有添入队列的时候线程isAlive
            queue.put(temp);
            System.out.println("producer add "+ temp);

        }
    }

    /*
     * @Author gcy
     * @Description 并发阻塞队列 BlockingQueue 消费者（出队列操作）
     * @Date 19:03 2020/7/3
     * @Param []
     * @return void
     **/
    private static void consumer() throws InterruptedException {
        Random random = new Random();
        List<Integer> res = new ArrayList<Integer>();
        while (true) {
            Thread.sleep(1000);
            //使用take方法取出 如果队列中没有元素 则阻塞线程
            Integer value_take = queue.take();
            //使用poll方法取出 没有元素 超时返回null
            Integer value_poll = queue.poll(1000,TimeUnit.MILLISECONDS);
            //批量取出
            queue.drainTo(res);
            System.out.println("Taken value: " + value_take + "; Queue size is: " + queue.size());
            res.clear();
        }
    }

    @Test
    public void BlockingQueue_Example() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    producer();
                } catch (InterruptedException ignored) {}
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException ignored) {}
            }
        });
        t1.start();
        t2.start();
        //无限循环 60秒后自动终止
        Thread.sleep(60000);
        System.exit(0);
    }

    //synchronized (this) 指获取此对象的锁 进入同步代码块 其他访问需要等到锁释放
    public void objectLock() throws InterruptedException {
        synchronized (this) {
            for(int i = 0; i <= 4 ; i++){
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
        }
    }

    //synchronized (class) 指获取此类的锁 由于jvm只有一个类 所以类锁只有一个
    public void classLock() throws InterruptedException {
        synchronized (BootwithutilsApplicationTests.class) {
            for(int i = 0; i <= 4 ; i++){
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
        }
    }

    /*
     * 两个对象锁 ———— 先启动的线程获得执行权 执行完毕后另外线程获得锁再执行
     * 两个类锁   ———— 同上
     * 一个对象锁 ———— 一个类锁 互不影响 两个线程执行结果交替出现
     * 使用【synchronized】修饰的方法获取的锁也是对象锁
     */
    @Test
    public void DifferentLocks(){
        final BootwithutilsApplicationTests tests = new BootwithutilsApplicationTests();
        Thread thread_1 = new Thread(new Runnable() {
            public void run() {
                try {
                    tests.classLock();
                    //tests.objectLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");

        Thread thread_2 = new Thread(new Runnable() {
            public void run() {
                try {
                    tests.classLock();
                    //tests.objectLock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2");

        thread_1.start();
        thread_2.start();
    }



}


