package ServerProgram;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
      /*Log class provides static function that are used to write log messages
    on terminal and Log.txt file that can be used in future to resolve any error
    format for Log message is (date and time) [type("Info","Debug", etc)] log_message
       */
    public static void info(String message){
        //Getting Date and Time of ServerProgram.Log
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String log = "("+dtf.format(currentDateTime)+") [Info] "+message+"\n";

        //Writing on CLI
        System.out.print(log);

        //Opening and Writing into File
        try {
            FileWriter logFile = new FileWriter("Log.txt", true);
            logFile.write(log);
            logFile.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void error(String message){
        //Getting Date and Time of ServerProgram.Log
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String log = "("+dtf.format(currentDateTime)+") [Error] "+message+"\n";

        //Writing on CLI
        System.out.print(log);

        //Opening and Writing into File
        try {
            FileWriter logFile = new FileWriter("Log.txt", true);
            logFile.write(log);
            logFile.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void debug(String message){
        //Getting Date and Time of ServerProgram.Log
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String log = "("+dtf.format(currentDateTime)+") [Debug] "+message+"\n";

        //Writing on CLI
        System.out.print(log);

        //Opening and Writing into File
        try {
            FileWriter logFile = new FileWriter("Log.txt", true);
            logFile.write(log);
            logFile.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
