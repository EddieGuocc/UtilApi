package com.gcy.bootwithutils;

import com.gcy.bootwithutils.service.bean.BeanService;
import com.gcy.bootwithutils.service.date.DateService;
import com.gcy.bootwithutils.service.json.JsonService;
import com.gcy.bootwithutils.service.number.NumberService;
import com.gcy.bootwithutils.service.string.StringService;
import com.gcy.bootwithutils.vo.House;
import com.gcy.bootwithutils.vo.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BootwithutilsApplicationTests {

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



}
