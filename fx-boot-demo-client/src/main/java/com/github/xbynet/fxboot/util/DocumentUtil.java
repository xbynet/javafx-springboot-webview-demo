package com.github.xbynet.fxboot.util;

import com.github.xbynet.fxboot.annotation.Tokenize;
import org.apache.lucene.document.*;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anzhou.tjw
 * @date 2018/6/15 下午3:57
 */
public class DocumentUtil {

    ConcurrentHashMap<String,Set<Field>> fieldSetMap=new ConcurrentHashMap<>();

    public <T> Document createDoc(T t){
        Set<Field> fieldSet=fieldSetMap.get(t.getClass().getName());
        if(fieldSet==null){
            fieldSet=ReflectionUtils.getAllFields(t.getClass());
            fieldSetMap.put(t.getClass().getName(),fieldSet);
        }

        Document document=new Document();
        fieldSet.stream().forEach(f->{
            String name=f.getName();
            Class clazz=f.getType();
            f.setAccessible(true);
            Object value=null;
            try {
                value=f.get(t);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            //if(clazz.isPrimitive())
            if(clazz==int.class || clazz==Integer.class){
                IntPoint tmp=new IntPoint(name,(int)value);
                document.add(tmp);
                document.add(new StoredField(name,(int)value));
            }else if(clazz==char.class){
                document.add(new StringField(name,String.valueOf((char)value), org.apache.lucene.document.Field.Store.YES));

            }else if(clazz==long.class || clazz==Long.class){
                LongPoint longPoint=new LongPoint(name,(long)value);
                document.add(longPoint);
                document.add(new StoredField(name,(long)value));
            }else if(clazz==double.class || clazz==Double.class){
                DoublePoint doublePoint=new DoublePoint(name,(double)value);
                document.add(doublePoint);
                document.add(new StoredField(name,(double)value));
            }else{

                //需要分词
                if(f.isAnnotationPresent(Tokenize.class)){
                    //Tokenize tokenize=f.getAnnotation(Tokenize.class);
                    TextField textField=new TextField(name,value.toString(), org.apache.lucene.document.Field.Store.YES);
                    document.add(textField);
                }else{
                    //无需分词
                    StringField stringField=new StringField(name,value.toString(),
                        org.apache.lucene.document.Field.Store.YES);
                    document.add(stringField);
                }

            }
        });
        return document;
    }
}
