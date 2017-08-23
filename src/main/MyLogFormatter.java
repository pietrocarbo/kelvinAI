package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;


public class MyLogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuffer log = new StringBuffer();
        log.append("[" + record.getLevel() + "] ");
        log.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.getMillis())));
        log.append(" ");
        log.append("[").append(record.getSourceClassName());
        log.append(".");
        log.append(record.getSourceMethodName() + "()]");
        log.append(" ");
        log.append(record.getMessage());
        log.append(System.getProperty("line.separator"));
        return log.toString();
    }
}