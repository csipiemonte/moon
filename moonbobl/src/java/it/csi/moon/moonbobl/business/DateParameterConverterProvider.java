/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import it.csi.moon.moonbobl.business.rs.annotations.DateFormat;
import it.csi.moon.moonbobl.business.rs.annotations.DateTimeFormat;
import it.csi.moon.moonbobl.business.rs.paramconverter.DateParameterConverter;

/**
 * 
 * FROM  https://zenidas.wordpress.com/recipes/configurable-date-format-in-jax-rs-as-queryparam/
 * 
 * USING:
 * 
 * @QueryParam("myDate") @DateFormat final Date myDate
 * @QueryParam("myDate") @DateFormat("yyyy/MM/dd") final Date myDate
 * 
 * @QueryParam("myDate") @DateTimeFormat final Date myDate
 * @QueryParam("myDate") @DateTimeFormat("yyyy/MM/dd HH:mm") final Date myDate
 * 
 * 
 * 
 *  @QueryParam("myDate") @DateTimeFormat("dd-MM-yyyy HH:mm")
 *
 *    
 * @author laurent
 *
 */
@Provider
public class DateParameterConverterProvider implements ParamConverterProvider {
 
  @SuppressWarnings("unchecked")
  @Override
  public <T> ParamConverter<T> getConverter(final Class<T> rawType,
    final Type genericType, final Annotation[] annotations) {
    if (Date.class.equals(rawType)) {
      final DateParameterConverter dateParameterConverter =
        new DateParameterConverter();
 
      for (Annotation annotation : annotations) {
        if (DateTimeFormat.class.equals(
          annotation.annotationType())) {
      dateParameterConverter.
            setCustomDateTimeFormat((DateTimeFormat) annotation);
        } else if (DateFormat.class.equals(
          annotation.annotationType())) {
          dateParameterConverter.
            setCustomDateFormat((DateFormat) annotation);
        }
      }
      return (ParamConverter<T>) dateParameterConverter;
    }
    return null;
  }
}
