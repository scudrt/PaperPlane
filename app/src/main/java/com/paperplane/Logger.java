package com.paperplane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Logger{
    static final int THRESHOLD = 1000; //size of each log file
    private Logger(){
        this.logs = new ArrayList<String>();
    }
    
    public static boolean log(String str){
        //make new record with time stamp
        str = new Date().toString() + " :\n" + str + '\n';
        System.out.println(str);
        instance.logs.add(str);

        //if logs need to be saved
        if (instance.logs.size() >= THRESHOLD){
            try{
                //check and create directory
                File folder = new File("./logs");
                if (!folder.exists() && !folder.isDirectory()){
                    folder.mkdir();
                }

                //write logs into new file
                FileWriter fw = new FileWriter("./logs/" + new Date().getTime() + ".txt");
                for (int i=0;i<THRESHOLD;++i){
                    fw.write(instance.logs.get(i) + '\n');
                }
                fw.close();
                instance.logs.clear();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
        }
        return true;
    }

    private static Logger instance = new Logger();
    private ArrayList<String> logs;
}