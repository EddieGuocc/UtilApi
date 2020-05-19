package com.gcy.bootwithutils.service.file;


import com.gcy.bootwithutils.service.date.DateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class FileService {

    public static void generateErrorLog(String exceptionType, String exceptionCause, String time){
        String fileName = "./ErrorLog/"+ time + "ErrorLog.txt";
        try{

            File file = new File(fileName);
            if(file.exists()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
                writer.write("["+ DateService.currentFormattedMilliseconds("yyyy-MM-dd-HH:mm:ss")+"]");
                writer.write(exceptionType + "\n\t");
                writer.write(exceptionCause+"\n");
                writer.flush();
                writer.close();
            }else{
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
                writer.write("["+ DateService.currentFormattedMilliseconds("yyyy-MM-dd-HH:mm:ss")+"]");
                writer.write(exceptionType + "\n\t");
                writer.write(exceptionCause+"\n");
                writer.flush();
                writer.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
