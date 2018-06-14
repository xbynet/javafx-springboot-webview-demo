package com.github.xbynet.fxboot.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anzhou.tjw
 * @date 2018/6/14 上午11:12
 */
public class StringUtil {

    public static String subStr(String str,String regex){
        Pattern pattern=Pattern.compile("("+regex+")");
        Matcher matcher=pattern.matcher(str);
        if(matcher.find()){
            return matcher.group(1);
        }
        return null;
    }

    public static void main(String[] args) {
        String str="sss(6集)";
        System.out.println(subStr(str,"\\d"));

        ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap<>();
        concurrentHashMap.put("aa","11");
        String res=concurrentHashMap.putIfAbsent("aa","22");
        String res2=concurrentHashMap.putIfAbsent("aaa","22");
        concurrentHashMap.compute("aa",(v,c)->{
            System.out.println("v:"+v+",c:"+c);
            return v+":"+c;
        });
        System.out.println();

        System.out.println(subStr("冰抗 +2833&nbsp;(子女+110)","\\d+"));

    }
}
