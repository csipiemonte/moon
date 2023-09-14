/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

 package it.csi.moon.moonbobl.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import it.csi.moon.moonbobl.util.LoggerAccessor;

public class SpringApplicationContextHelper implements ApplicationContextAware {

	private static ApplicationContext appContext;

	private static Map<String, Object> beanCache = new HashMap<>();

	private static Map<String, Object> restEasyServiceCache = new HashMap<>();
	
	// Private constructor prevents instantiation from other classes
    private SpringApplicationContextHelper() {}

    private final static String CLASS_NAME = "SpringApplicationContextHelper";
    private static Logger log = LoggerAccessor.getLoggerBusiness();
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	

	public static Object getBean(String beanName, boolean cacheable) {
		log.info("[" + CLASS_NAME + "::getBean] BEGIN beanName:" + beanName + "  cacheable:"+cacheable);
		if (cacheable && beanCache.containsKey(beanName)) {
			return beanCache.get(beanName);
		}
		
		Object bean = null;
		
		if (appContext.containsBean(beanName)) {
			bean = appContext.getBean(beanName);
		} else {
			bean = appContext.getBean(beanName.substring(0, 1).toLowerCase() + beanName.substring(1));
		}
		
		if (cacheable) {
			beanCache.put(beanName, bean);
		}
		
		return bean;
	}

	public static Object getBean(String beanName) {
		return getBean(beanName, true);
	}

}
