/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private ObjectMapper objectMapper;

    public JacksonConfig() throws Exception {
        this.objectMapper = new ObjectMapper();
        
        this.objectMapper.setSerializationInclusion(Include.NON_NULL);

        // converte ogni DateTime in timestamp in formato ISO-8601
		this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        
        // permette di serializzare automaticamente in snake_case le property che sono
        // in camelCase, senza dover specificare le annotation sulle singole property     
//        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        
        // permette di escludere dal JSON le proprieta' con valore nullo
		
        this.objectMapper.setSerializationInclusion(Include.NON_EMPTY);
        
        // 2020-07-22 Non mi piace come solution ma ho un problema su /civici 
        // javax.ws.rs.ProcessingException: com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "iBisTer"
//        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public ObjectMapper getContext(Class<?> objectType) {
        return objectMapper;
    }
}
