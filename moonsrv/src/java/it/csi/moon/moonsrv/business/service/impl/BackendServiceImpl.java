/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.BuildInfo;
import it.csi.moon.commons.dto.SysInfo;
import it.csi.moon.moonsrv.business.service.BackendService;
import it.csi.moon.moonsrv.business.service.impl.dao.MoondbDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business generico
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class BackendServiceImpl  implements BackendService {
	
	private static final String CLASS_NAME = "BackendServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private static final Date LAST_STARTUP = new Date();

	@Autowired
	private MoondbDAO dummyDAO;
	
	public String getMessage(){
		return dummyDAO.pingDB();
	}

	@Override
	public String getVersion() throws BusinessException {
		return EnvProperties.readFromFile(EnvProperties.VERSION);
	}
	
	@Override
	public BuildInfo getBuildInfo() throws BusinessException {
		return new BuildInfo(EnvProperties.readFromFile(EnvProperties.VERSION),
				EnvProperties.readFromFile(EnvProperties.BUILD_TIME),
				EnvProperties.readFromFile(EnvProperties.TARGET_LINE));
	}
	
	@Override
	public AttributeList getAttributeList() throws BusinessException {
		try {
		    ObjectName mBeanName = new ObjectName("jboss.as:deployment=wildfly-helloworld-rs.war,subsystem=undertow");
	
		    String host = "localhost";
		    int port = 9990;  // management-native port
	
		    String urlString = System.getProperty("jmx.service.url", "service:jmx:http-remoting-jmx://" + host + ":" + port);
		    JMXServiceURL serviceURL = new JMXServiceURL(urlString);
		    JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
		    //
		    MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
		    AttributeList values = connection.getAttributes(new ObjectName("jboss.system:type=Server"), new String[]{"StartDate"});
	//	    Date jbossStartupTime = new Date(System.currentTimeMillis() - startDate.getTime());
		    return values;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getAttributeList] Exception ", e);
			throw new BusinessException();
		}
	}

	@Override
	public SysInfo getSysInfo(String fields) throws BusinessException {
		try {
			InetAddress iAddress = InetAddress.getLocalHost();
	        String ipAdress = "" + iAddress;
	        String hostName = iAddress.getHostName();
	        String canonicalHostName = iAddress.getCanonicalHostName();
	        SysInfo result = new SysInfo(ipAdress,
	        		hostName,
	        		canonicalHostName);
	        result = completeRequestFields(fields,result);
	        return result;
		} catch (UnknownHostException uhe) {
			throw new BusinessException("UnknownHostException");
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	
	private SysInfo completeRequestFields(String fields, SysInfo result) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));
		if (listFields.contains("prop")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] prop ...");
			result.setProp(getProp());
		}
		if (listFields.contains("env")) {
			LOG.debug("[" + CLASS_NAME + "::completeRequestFields] env ...");
			result.setEnv(getEnv());
		}
		return result;
	}
	
	@Override
	public Map<String,String> getProp() throws BusinessException {
		try {
			Properties props = System.getProperties();
	        props.forEach((k, v) -> {
	            System.out.println(k + ":" + v);
	        });
	        return streamConvert(props);
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	
	public HashMap<String, String> streamConvert(Properties prop) {
	    return prop.entrySet().stream().collect(
	    	Collectors.toMap(
	    		e -> String.valueOf(e.getKey()),
	    		e -> String.valueOf(e.getValue()),
	    		(prev, next) -> next, HashMap::new
	    ));
	}
	
	@Override
	public Map<String,String> getEnv() throws BusinessException {
		try {
	        Map<String, String> env = System.getenv();
	        env.forEach((k, v) -> {
	            System.out.println(k + ":" + v);
	        });
			return env;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}

}
