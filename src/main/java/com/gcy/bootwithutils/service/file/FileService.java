package com.gcy.bootwithutils.service.file;


import com.gcy.bootwithutils.service.date.DateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class FileService {

    public static void generateErrorLog(String exceptionType, String exceptionCause, String time){
        String fileName = "/User/eddieguo/log/ErrorLog/"+ time + "ErrorLog.txt";
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

    public void exportFile(String fileName, String data, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment;file=" + fileName + ".txt");

        try (OutputStream outStr = response.getOutputStream();
             BufferedOutputStream buff = new BufferedOutputStream(outStr)){
            buff.write(data.getBytes(StandardCharsets.UTF_8));
            buff.flush();
            outStr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
