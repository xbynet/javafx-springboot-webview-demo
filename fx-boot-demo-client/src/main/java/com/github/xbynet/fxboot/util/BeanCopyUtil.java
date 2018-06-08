
package com.github.xbynet.fxboot.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtil {
	private static Logger log = LoggerFactory.getLogger(BeanCopyUtil.class);

	public static <S, T> T copyProperties(S srcObject, Class<T> clazz) {
		if (srcObject == null) {
			return null;
		}

		T t = null;
		try {
			t = (T) clazz.newInstance();
		} catch (Exception e) {
			log.error("bean copy erorr! ", e);
		}

		BeanUtils.copyProperties(srcObject, t);
		return t;
	}

	public static <S, T> List<T> copyProperties(List<S> srcObjects, Class<T> clazz) {
		if (srcObjects != null) {
			return srcObjects.stream().map(it -> copyProperties(it, clazz)).collect(Collectors.toList());
		}
		return null;
	}
	

}
