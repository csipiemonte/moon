/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.business.service.helper.JwtIdentitaUtil;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonfobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * 
 * @author Laurent Pissard
 *
 * @since 1.0.0 - 20/03/2020
 */
public class MoonBaseApiImpl {

	private static final String CLASS_NAME = "MoonBaseApiImpl";
	private static Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private static final int MAX_COOKIE_LENGTH = 4096;
	private static final int MAX_COOKIE_AGE_SECONDS = 10;
	
//	public static final String QP_SIMULATE_PORTALE = "Simulate-Portale";
	
//    public static final Pattern PATTERN_NUMERIC_SIMPLE_NON_ZERO = Pattern.compile("^([1-9]+[0-9]* | [1-9])$");

	protected boolean devmode = false;
	
    @Autowired
    JwtIdentitaUtil jwtIdentitaUtil;
    
	public MoonBaseApiImpl() {
		super();
		devmode = "true".equals(EnvProperties.readFromFile(EnvProperties.DEV_MODE))?true:false;
	}
	
	protected UserInfo retrieveUserInfo(HttpServletRequest httpRequest) throws UnauthorizedBusinessException, BusinessException {
		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String marker = httpRequestUtils.getMoonToken(httpRequest, devmode);
			if (!jwtIdentitaUtil.isTokenValid(marker)) {
				LOG.error("[" + CLASS_NAME + "::retrieveUserInfo] UnauthorizedBusinessException jwtIdentitaUtil.isTokenValid() of marker: " + marker);
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
	protected String retrievePortalName(HttpServletRequest httpRequest) throws BusinessException {
		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String portalName = httpRequestUtils.getPortalName(httpRequest, devmode);
//			portalName = removeTstPortaleName(portalName);
		    LOG.info("[" + CLASS_NAME + "::retrievePortalName] retrievePortalName()=" + portalName);
		    return portalName;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}
	
	//
	//
	//
//    boolean isNumericSimpleNonZero(CharSequence input) {
//        return input != null && PATTERN_NUMERIC_SIMPLE_NON_ZERO.matcher(input).matches();
//    }
    
    //
    // String
    //
	protected String validaStringRequired(String input) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		return input;
	}
	protected String validaStringRequired(String input, String fieldName) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required for "+fieldName);
		}
		return input.trim();
	}
	protected String validaStringRequired(String input, String fieldName, String errCode, String errTitle) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required for "+fieldName, errCode, errTitle);
		}
		return input.trim();
	}
	protected String validaStringRequired(String input, int exactLength) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		if (input.length()!=exactLength) {
			throw new UnprocessableEntityException("String required, exactLength must be of "+exactLength+" char.");
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
	protected String validaStringCodeRequired(String input, int len) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		String result = input.trim().toUpperCase();
		if (result.length()!=len) {
			throw new UnprocessableEntityException("String deve essere lungo "+len+" char.");
		}
		return result;
	}
	
	// Particolare String
	protected String validaStringRequiredLPadZero(String input, int exactLength) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException("String required.");
		}
		if (input.length()>exactLength) {
			throw new UnprocessableEntityException("String required, maxLength must be of "+exactLength+" char.");
		}
		try {
			NumberFormat nf = NumberFormat.getInstance();
		    nf.setMaximumIntegerDigits(exactLength);
		    nf.setMinimumIntegerDigits(exactLength);
		    nf.setGroupingUsed(false);
		    return nf.format(Integer.parseInt(input));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaStringRequiredLPadZero] Exception", e);
			throw new UnprocessableEntityException("String required, Cannot format LPadZero in "+exactLength+" char.");
		}
	}
	protected String validaIstatRegione(String istatRegione) throws UnprocessableEntityException {
		return validaStringRequiredLPadZero(istatRegione, 2);
	}
	protected String validaIstatProvincia(String istatProvincia) throws UnprocessableEntityException {
		return validaStringRequiredLPadZero(istatProvincia, 3);
	}
	protected String validaIstatComune(String istatComune) throws UnprocessableEntityException {
		return validaStringRequiredLPadZero(istatComune, 6);
	}
	protected String validaStringPortaleName(String serverName) {
		return new HttpRequestUtils().removeTstPortaleName(validaStringRequired(serverName));
	}
	protected String validaStringPortaleName(String serverName, String simulatePortale) {
		if (/*devmode &&*/ "localhost".equals(serverName) || "127.0.0.1".equals(serverName) ) {
			serverName = simulatePortale!=null?simulatePortale:serverName;
		}
		return validaStringPortaleName(serverName);
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
    	Boolean result = null;
		try {
			if (StringUtils.isEmpty(input)) {
				return result;
			}
			result = Boolean.valueOf(input);
			return result;
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
	
		
	protected List<String> normalizzaCodiciIstat(List<String> codiciIstat) {			
		 List<String> codiciNormalizzati = new ArrayList<>();	
		 for (String codice : codiciIstat) {
			 codiciNormalizzati.add(StringUtils.leftPad(codice, 6, "0"));
		 }			 
		 return codiciNormalizzati;
	}
	
	
	protected String validaCodiceIstatComuneRequired(String input) throws UnprocessableEntityException {
		
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("String required.");
			}			
						
			return normalizzaCodiceIstat(input);			
		} catch(Exception e) {
	         throw new UnprocessableEntityException("List String:"+input+" )");
		}

	}
	
	protected String validaCodiceIstatProvinciaRequired(String input) throws UnprocessableEntityException {
		
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("String required.");
			}			
						
			return normalizzaCodiceIstatProvincia(input);			
		} catch(Exception e) {
	         throw new UnprocessableEntityException("List String:"+input+" )");
		}

	}

	protected String normalizzaCodiceIstat(String codice) {
		return StringUtils.leftPad(codice, 6, "0");
	}
	
	protected String normalizzaCodiceIstatProvincia(String codice) {
		return StringUtils.leftPad(codice, 3, "0");
	}

	protected List<String> validaCodiciIstatComuneRequired(String input) throws UnprocessableEntityException {
		
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("List String required.");
			}			
			List<String> codici = Arrays.asList(StringUtils.split(input,","));			
			if (codici.isEmpty()) {
				throw new UnprocessableEntityException("List String required.");
			}					
			return normalizzaCodiciIstat(codici);			
		} catch(Exception e) {
	         throw new UnprocessableEntityException("List String:"+input+" )");
		}

	}
	


	//
	//
	protected Cookie buildCookie(String name, String value) throws Throwable {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setMaxAge(MAX_COOKIE_AGE_SECONDS);
		cookie.setHttpOnly(false); // false : this cookies are for Angular client side
		LOG.debug("[" + CLASS_NAME + "::buildCookie] Valore cookie: " + cookie.getValue());
		return cookie;
	}
	protected String pruneAndEncodeLoginResponse(LoginResponse loginResponse) throws Throwable {
		LoginResponse loginRespForCookie = new LoginResponse(loginResponse);
		// Eliminazione dei dati inutile nel cookie (per non superare la limit di dimenzione)
		loginRespForCookie.setIdIride(null);
		loginRespForCookie.setDatiAggiuntivi(null);
		return encodeCookieBase64(loginRespForCookie);
	}
	private String encodeCookieBase64(LoginResponse loginResponse) throws Throwable {
		String result = encodedBase64(loginResponse);
		if (result.length() > MAX_COOKIE_LENGTH) {
			throw new Throwable("Lunghezza massima cookie superata");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::encodedBase64] Encode loginResponse=" + result);
		}
		return result;
	}
	private String encodedBase64(LoginResponse loginResponse) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String strJSON = mapper.writeValueAsString(loginResponse);
		String result = Base64.getUrlEncoder().encodeToString(strJSON.getBytes(Charset.forName("UTF-8")));
		LOG.debug("[" + CLASS_NAME + "::encodedBase64] result = " + result);
		return result;
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
