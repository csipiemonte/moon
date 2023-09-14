/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer.utils;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

import it.csi.moon.moonprint.exceptions.business.BusinessException;
import it.csi.moon.moonprint.util.LoggerAccessor;

public class ExtensionLoader<C> {

	private final static String CLASS_NAME = "ExtensionLoader";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
    public C LoadClass(String directory, String classpath, Class<C> parentClass) throws ClassNotFoundException {
        //File pluginsDir = new File(System.getProperty("user.dir") + directory);
        File pluginsDir = new File(directory);
        for (File jar : pluginsDir.listFiles()) {
            try {
                ClassLoader loader = URLClassLoader.newInstance(
//                        new URL[]{jar.toURL()},
                        new URL[]{jar.toURI().toURL()},
                        getClass().getClassLoader()
                );
                Class<?> clazz = Class.forName(classpath, true, loader);
                Class<? extends C> newClass = clazz.asSubclass(parentClass);
                // Apparently its bad to use Class.newInstance, so we use
                // newClass.getConstructor() instead
                Constructor<? extends C> constructor = newClass.getConstructor();
                return constructor.newInstance();

            } catch (ClassNotFoundException e) {
                // There might be multiple JARs in the directory,
                // so keep looking
                continue;
            } catch (MalformedURLException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] MalformedURLException", e);
    			throw new BusinessException("MalformedURLException");
            } catch (NoSuchMethodException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] NoSuchMethodException", e);
    			throw new BusinessException("NoSuchMethodException");
            } catch (InvocationTargetException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] InvocationTargetException", e);
    			throw new BusinessException("InvocationTargetException");
            } catch (IllegalAccessException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] IllegalAccessException", e);
    			throw new BusinessException("IllegalAccessException");
            } catch (InstantiationException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] InstantiationException", e);
    			throw new BusinessException("InstantiationException");
            }
        }
        throw new ClassNotFoundException("Class " + classpath
                + " wasn't found in directory: " + directory);
    }

    public Class LoadClass2(String directory, String classpath) throws ClassNotFoundException {
        //File pluginsDir = new File(System.getProperty("user.dir") + directory);
        File pluginsDir = new File(directory);
        for (File jar : pluginsDir.listFiles()) {
            try {
                ClassLoader loader = URLClassLoader.newInstance(
                        new URL[]{jar.toURI().toURL()},
                        getClass().getClassLoader()
                );
                Class<?> clazz = Class.forName(classpath, true, loader);
                return clazz;

            } catch (ClassNotFoundException e) {
                // There might be multiple JARs in the directory,
                // so keep looking
                continue;
            } catch (MalformedURLException e) {
    			LOG.error("[" + CLASS_NAME + "::LoadClass] MalformedURLException", e);
    			throw new BusinessException("MalformedURLException");
            }
        }
        throw new ClassNotFoundException("Class " + classpath
                + " wasn't found in directory " + System.getProperty("user.dir") + directory);
    }

}