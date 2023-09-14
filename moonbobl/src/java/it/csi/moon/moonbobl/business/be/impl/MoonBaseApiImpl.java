/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.JwtIdentitaUtil;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * 
 * @author Laurent Pissard
 *
 * @since 1.0.0 - 20/03/2020
 */
public class MoonBaseApiImpl {

	private final static String CLASS_NAME = "MoonBaseApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	public static final String QP_SIMULATE_PORTALE = "simulatePortale";
	
    public static final Pattern PATTERN_NUMERIC_SIMPLE_NON_ZERO = Pattern.compile("^([1-9]+[0-9]* | [1-9])$");

    protected boolean devmode = false;
	
//  @Autowired
//  JwtTokenUtil jwtTokenUtil;
    @Autowired
    JwtIdentitaUtil jwtIdentitaUtil;
    @Autowired
    BackendService backendService;
  
	public MoonBaseApiImpl() {
		super();
		devmode = "true".equals(EnvProperties.readFromFile(EnvProperties.DEV_MODE))?true:false;
	}
	
	protected UserInfo retrieveUserInfo(HttpServletRequest httpRequest) throws UnauthorizedBusinessException, BusinessException {
		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String marker = httpRequestUtils.getMoonToken(httpRequest, devmode);
			if (!jwtIdentitaUtil.isTokenValid(marker)) {
				log.error("[" + CLASS_NAME + "::retrieveUserInfo] UnauthorizedBusinessException jwtIdentitaUtil.isTokenValid() of marker: " + marker);
				throw new UnauthorizedBusinessException();
			}
			UserInfo userInfo = jwtIdentitaUtil.retreiveUserInfoFromToken(marker);
			return userInfo;
		} catch (UnauthorizedBusinessException ube) {
			throw ube;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	protected UserInfo retrieveUserInfoWithRoles(HttpServletRequest httpRequest) throws UnauthorizedBusinessException, BusinessException {
		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String marker = httpRequestUtils.getMoonToken(httpRequest, devmode);
			if (!jwtIdentitaUtil.isTokenValid(marker)) {
				log.error("[" + CLASS_NAME + "::retrieveUserInfoWithRoles] UnauthorizedBusinessException jwtIdentitaUtil.isTokenValid() of marker: " + marker);
				throw new UnauthorizedBusinessException();
			}
			UserInfo userInfo = jwtIdentitaUtil.retreiveUserInfoFromToken(marker);
			List<EnteAreeRuoli> entiAreeRuoli = backendService.getEntiAreeRuoliAttivi(userInfo.getIdentificativoUtente(), jwtIdentitaUtil.getIdEnteFromToken(marker));
			userInfo.setEntiAreeRuoli(entiAreeRuoli);
			return userInfo;
		} catch (UnauthorizedBusinessException ube) {
			throw ube;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	/*
	protected UserInfo retrieveUserInfo(HttpServletRequest httpRequest) throws BusinessException {
		try {
			// MODULISTICA
//			UserInfo userInfo = jwtTokenUtil.retreiveUserInfoFromToken((String) httpRequest.getHeader(Constants.HEADER_MOON_ID_JWT));
//			log.debug("[" + CLASS_NAME + "::saveDatiIstanza] userInfoFromJWT: "+user);
			// MOONFOBL
			UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(Constants.SESSION_USERINFO);
			return userInfo;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	*/
	protected String getPortalName(HttpServletRequest httpRequest) {
		String portalName = httpRequest.getServerName();
		if (devmode && ("localhost".equals(portalName) || "127.0.0.1".equals(portalName)) ) {
			String simulatePortaleDev = getParamDevMode(httpRequest, QP_SIMULATE_PORTALE);
			log.debug("[" + CLASS_NAME + "::getPortalName] simulatePortaleDev=" + simulatePortaleDev);
			portalName = simulatePortaleDev!=null?simulatePortaleDev:portalName;
		} else {
			portalName = removeTstPortaleName(portalName);
		}
	    if (log.isDebugEnabled()) {
	    	log.debug("[" + CLASS_NAME + "::getPortalName] getPortalName()=" + portalName);
	    }
	    return portalName;
	}
	protected String getParamDevMode(HttpServletRequest httpRequest, String paramName) {
		String marker = (String) httpRequest.getParameter(paramName);
		return marker;
	}
	private String removeTstPortaleName(String serverName) {
		String portalName = serverName;
		Pattern p = Pattern.compile("tst-");
      Matcher m = p.matcher(serverName);
      if(m.find()) {
      	portalName = serverName.substring(4);
      }
      Pattern p2 = Pattern.compile("ts-");
      Matcher m2 = p2.matcher(serverName);
      if(m2.find()) {
      	portalName = serverName.substring(3);
      }
      return portalName;
	}
	
	//
	//
	//
  boolean isNumericSimpleNonZero(CharSequence input) {
      return input != null && PATTERN_NUMERIC_SIMPLE_NON_ZERO.matcher(input).matches();
  }
  
    
    //
    // String
    //
	protected String validaStringRequired(String input) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		return input;
	}
	// String CODE (trim+upper)
	protected String validaStringCode(String input) {
		if (input==null) {
			return null;
		}
		return input.trim().toUpperCase();
	}
	protected String validaStringCodeRequired(String input) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		return input.trim().toUpperCase();
	}
	protected void required(String input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("String required for "+fieldName);
		}
	}
	
    //
    // Long
    //
    protected Long validaLong(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
			return Long.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Long non corretto (expected: Long ; actual:"+input+" )");
		}
	}
    protected Long validaLong(String input, Long defaultValue) throws UnprocessableEntityException {
		Long result = validaLong(input);
		return result!=null?result:defaultValue;
	}
	protected Long validaLongRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Long required.");
			}
			return Long.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Long required non corretto (expected: Long ; actual:"+input+" )");
		}
	}
	protected void required(Long input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Long required for "+fieldName);
		}
	}
	
	//
	// Integer
	//
    protected Integer validaInteger(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
			return Integer.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Integer non corretto (expected: Integer ; actual:"+input+" )");
		}
	}
    protected Integer validaInteger(String input, Integer defaultValue) throws UnprocessableEntityException {
		Integer result = validaInteger(input);
		return result!=null?result:defaultValue;
	}
    protected Integer validaInteger0Based(String input) throws UnprocessableEntityException {
    	Integer result = validaInteger(input);
    	if (result!=null && result<0) {
    		throw new UnprocessableEntityException("Integer 0 based.");
    	}
    	return result;
	}
    protected Integer validaInteger1Based(String input) throws UnprocessableEntityException {
    	Integer result = validaInteger(input);
    	if (result!=null && result<1) {
    		throw new UnprocessableEntityException("Integer 1 based.");
    	}
    	return result;
	}
	protected Integer validaIntegerRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Integer required.");
			}
			return Integer.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Integer required non corretto (expected: Integer ; actual:"+input+" )");
		}
	}
    protected Integer validaInteger0BasedRequired(String input) throws UnprocessableEntityException {
    	Integer result = validaIntegerRequired(input);
    	if (result!=null && result<0) {
    		throw new UnprocessableEntityException("Integer 0 based required.");
    	}
    	return result;
	}
    protected Integer validaInteger1BasedRequired(String input) throws UnprocessableEntityException {
    	Integer result = validaIntegerRequired(input);
    	if (result!=null && result<1) {
    		throw new UnprocessableEntityException("Integer 1 based required");
    	}
    	return result;
	}
	protected void required(Integer input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Integer required for "+fieldName);
		}
	}
	
	//
	// Boolean
	//
    protected Boolean validaBoolean(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
			return Boolean.valueOf(input);
		} catch(Exception e) {
	         throw new UnprocessableEntityException("Boolean non corretto (expected: Boolean ; actual:"+input+" )");
		}
	}
    protected Boolean validaBoolean(String input, Boolean defaultValue) throws UnprocessableEntityException {
    	Boolean result = validaBoolean(input);
		return result!=null?result:defaultValue;
	}
	protected Boolean validaBooleanRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Boolean required.");
			}
			return Boolean.valueOf(input);
		} catch(Exception e) {
	         throw new UnprocessableEntityException("Boolean required non corretto (expected: Boolean ; actual:"+input+" )");
		}
	}
	protected void required(Boolean input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Boolean required for "+fieldName);
		}
	}

	//
	// MOON GENERICO
	protected String validaFields(String input) throws UnprocessableEntityException {
		// TODO Auto-generated method stub
		return input;
	}
	
	//
	// MOON MODULI
	protected String validaCodiceModulo(String input) throws UnprocessableEntityException {
		// TODO Auto-generated method stub
		return input;
	}
	
	//
	//
	protected Date validaDateTime(String input) {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        Date date = df.parse(input);
	        return date;
	    } catch (ParseException e) {
	    	throw new UnprocessableEntityException("Data non corretta (expected: 'yyyy-MM-dd'T'HH:mm:ss' ; actual:"+input+" )");
	    }
	}
	//
	//
	protected String validaStringPortaleName(String serverName) {
		if (StringUtils.isEmpty(serverName)) {
			throw new UnprocessableEntityException("String required.");
		}
		String portalName = serverName;
		Pattern p = Pattern.compile("tst-");
        Matcher m = p.matcher(serverName);
        if(m.find()) {
        	portalName = serverName.substring(4);
        }
        Pattern p2 = Pattern.compile("ts-");
        Matcher m2 = p2.matcher(serverName);
        if(m2.find()) {
        	portalName = serverName.substring(3);
        }
        return portalName;
	}
	
	protected String validaStringPortaleName(String serverName, String simulatePortale) {
		if (/*devmode &&*/ "localhost".equals(serverName) || "127.0.0.1".equals(serverName)) {
			serverName = simulatePortale!=null?simulatePortale:serverName;
		}
		return validaStringPortaleName(serverName);
	}
	protected String retrievePortalName(HttpServletRequest httpRequest) throws BusinessException {
		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String portalName = httpRequestUtils.getPortalName(httpRequest, devmode);
//			portalName = removeTstPortaleName(portalName);
		    if (log.isDebugEnabled()) {
		    	log.debug("[" + CLASS_NAME + "::retrievePortalName] getPortalName()=" + portalName);
		    }
		    return portalName;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	
	//
	//
	protected String getIpAddress(HttpServletRequest request) {

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();            
        } else {
        	ipAddress = "";
        }
        return ipAddress;
	}
}
