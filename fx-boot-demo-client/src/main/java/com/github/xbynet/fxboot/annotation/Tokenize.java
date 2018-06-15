package com.github.xbynet.fxboot.annotation;

import java.lang.annotation.*;

/**
 * 是否需要分词
 * @author anzhou.tjw
 * @date 2018/6/15 下午4:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tokenize {
}
