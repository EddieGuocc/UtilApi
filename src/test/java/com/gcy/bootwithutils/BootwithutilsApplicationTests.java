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



}
