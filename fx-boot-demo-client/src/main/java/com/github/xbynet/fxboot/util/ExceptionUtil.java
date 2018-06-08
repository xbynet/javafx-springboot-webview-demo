package com.github.xbynet.fxboot.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

public class ExceptionUtil {
    public static String getStackTrace(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    public static String getGoodMessageInfo(Throwable t){
        return MessageFormat.format("异常信息：{0}；异常堆栈：{1}", t.getMessage(), getStackTrace(t));
    }

}