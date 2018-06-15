package com.github.xbynet.fxboot.util;

/**
 * @author anzhou.tjw
 * @date 2018/6/15 下午4:01
 */
public class ClassUtil {

    public static Class primitiveToClass(String primitive) {
        switch (primitive) {
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean":
                return boolean.class;
            case "char":
                return char.class;
            default:
                break;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(int.class.getName());//int
        System.out.println(int.class.isPrimitive());
    }
}
