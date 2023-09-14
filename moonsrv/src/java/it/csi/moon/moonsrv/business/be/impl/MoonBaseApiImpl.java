/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * 
 * @author Laurent Pissard
 *
 * @since 1.0.0 - 20/03/2020
 */
public class MoonBaseApiImpl {

	private static final String CLASS_NAME = "MoonBaseApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String STRING_PARAMETER_REQUIRED = "String parameter required.";
	private static final String CHAR = " char.";

    //
    // String
    //
	protected String validaStringRequired(String input) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException(STRING_PARAMETER_REQUIRED);
		}
		return input;
	}
	protected String validaStringRequired(String input, int exactLength) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException(STRING_PARAMETER_REQUIRED);
		}
		if (input.length()!=exactLength) {
			throw new UnprocessableEntityException(STRING_PARAMETER_REQUIRED + ", exactLength must be of " + exactLength + CHAR);
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
			throw new UnprocessableEntityException(STRING_PARAMETER_REQUIRED);
		}
		return input.trim().toUpperCase();
	}
	protected void required(String input, String fieldName) {
		if (input == null) {
			throw new UnprocessableEntityException("String parameter required for " + fieldName);
		}
	}
	
	// Particolare String
	protected String validaStringRequiredLPadZero(String input, int exactLength) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			throw new UnprocessableEntityException(STRING_PARAMETER_REQUIRED);
		}
		if (input.length()>exactLength) {
			throw new UnprocessableEntityException("String parameter required, maxLength must be of " + exactLength + CHAR);
		}
		try {
			NumberFormat nf = NumberFormat.getInstance();
		    nf.setMaximumIntegerDigits(exactLength);
		    nf.setMinimumIntegerDigits(exactLength);
		    nf.setGroupingUsed(false);
		    return nf.format(Integer.parseInt(input));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::validaStringRequiredLPadZero] Exception", e);
			throw new UnprocessableEntityException("String parameter required, Cannot format LPadZero in " + exactLength + CHAR);
		}
	}
	protected String validaIstatRegione(String istatRegione) {
		return validaStringRequiredLPadZero(istatRegione, 2);
	}
	protected String validaIstatProvincia(String istatProvincia) {
		return validaStringRequiredLPadZero(istatProvincia, 3);
	}
	protected String validaIstatComune(String istatComune) {
		return validaStringRequiredLPadZero(istatComune, 6);
	}
	
    //
    // Long
    //
    protected Long validaLong(String input) {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
			return Long.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Long parameter incorrect (expected: Long ; actual:"+input+" )");
		}
	}
    protected Long validaLong(String input, Long defaultValue) {
    	Long result = validaLong(input);
		return result!=null?result:defaultValue;
	}
	protected Long validaLongRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Long parameter required.");
			}
			return Long.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Long parameter required non corretto (expected: Long ; actual:"+input+" )");
		}
	}
	protected void required(Long input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Long parameter required for "+fieldName);
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
	         throw new UnprocessableEntityException("Integer parameter incorrect (expected: Integer ; actual:"+input+" )");
		}
	}
    protected Integer validaInteger(String input, Integer defaultValue) throws UnprocessableEntityException {
		Integer result = validaInteger(input);
		return result!=null?result:defaultValue;
	}
    protected Integer validaInteger0Based(String input) throws UnprocessableEntityException {
    	Integer result = validaInteger(input);
    	if (result!=null && result<0) {
    		throw new UnprocessableEntityException("Integer parameter 0 based.");
    	}
    	return result;
	}
    protected Integer validaInteger1Based(String input) throws UnprocessableEntityException {
    	Integer result = validaInteger(input);
    	if (result!=null && result<1) {
    		throw new UnprocessableEntityException("Integer parameter 1 based.");
    	}
    	return result;
	}
	protected Integer validaIntegerRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Integer parameter required.");
			}
			return Integer.valueOf(input);
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Integer parameter required incorrect (expected: Integer ; actual:"+input+" )");
		}
	}
    protected Integer validaInteger0BasedRequired(String input) throws UnprocessableEntityException {
    	Integer result = validaIntegerRequired(input);
    	if (result!=null && result<0) {
    		throw new UnprocessableEntityException("Integer parameter 0 based required.");
    	}
    	return result;
	}
    protected Integer validaInteger1BasedRequired(String input) throws UnprocessableEntityException {
    	Integer result = validaIntegerRequired(input);
    	if (result!=null && result<1) {
    		throw new UnprocessableEntityException("Integer parameter 1 based required");
    	}
    	return result;
	}
	protected void required(Integer input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Integer parameter required for "+fieldName);
		}
	}
	
	protected List<Integer> validaListInteger(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
			List<Integer> result = Stream.of(input.split(","))
					.map(String::trim)
					.filter(Objects::nonNull)
					.filter(Predicate.not(String::isEmpty))
					.map(Integer::parseInt)
					.collect(Collectors.toList());
			return result;
		} catch(NumberFormatException nfe) {
	         throw new UnprocessableEntityException("Integer parameter required incorrect (expected: Integer (comma separate) ; actual:"+input+" )");
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
	         throw new UnprocessableEntityException("Boolean parameter incorrect (expected: Boolean ; actual:"+input+" )");
		}
	}
    protected Boolean validaBoolean(String input, Boolean defaultValue) throws UnprocessableEntityException {
    	Boolean result = validaBoolean(input);
		return result!=null?result:defaultValue;
	}
	protected Boolean validaBooleanRequired(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				throw new UnprocessableEntityException("Boolean parameter required.");
			}
			return Boolean.valueOf(input);
		} catch(Exception e) {
	         throw new UnprocessableEntityException("Boolean parameter required incorrect (expected: Boolean ; actual:"+input+" )");
		}
	}
	protected void required(Boolean input, String fieldName) throws UnprocessableEntityException {
		if (input == null) {
			throw new UnprocessableEntityException("Boolean parameter required for "+fieldName);
		}
	}

	//
	// MOON GENERICO
	protected String validaFields(String input) {
		return input;
	}
	
	//
	// MOON MODULI
	protected String validaCodiceModulo(String input) {
		return input;
	}
	protected DecodificaStatoModulo validaDecodificaStatoModulo(String input) throws UnprocessableEntityException {
		if (StringUtils.isEmpty(input)) {
			return null;
		}
		DecodificaStatoModulo result = DecodificaStatoModulo.byCodice(input);
		if(result==null) {
	         throw new UnprocessableEntityException("stato modulo incorrect (expected: [INIT,TST,MOD,PUB,SOSP,ELI,RIT] ; actual:"+input+" )");
		}
		return result;
	}
	
	//
	//
	protected Date validaDateTime(String input) throws UnprocessableEntityException {
		try {
			if (StringUtils.isEmpty(input)) {
				return null;
			}
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        Date date = df.parse(input);
	        return date;
	    } catch (ParseException e) {
	    	throw new UnprocessableEntityException("Date invalid format (expected: 'yyyy-MM-dd'T'HH:mm:ss' ; actual:"+input+" )");
	    }
	}
}
