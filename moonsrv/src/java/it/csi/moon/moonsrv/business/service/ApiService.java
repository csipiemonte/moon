/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.StreamingOutput;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.dto.api.FruitoreIstanza;
import it.csi.moon.commons.dto.api.FruitoreIstanzaDettagliata;
import it.csi.moon.commons.dto.api.FruitoreModuloVersione;
import it.csi.moon.commons.dto.api.StatoAcquisizioneRequest;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonsrv.exceptions.business.UnauthorizedBusinessException;

public interface ApiService {

	List<String> getIstanze(String codiceModulo, String stato,  String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, boolean test, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException;
	List<String> getIstanzePaginate(String codiceModulo, String stato,  String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, boolean test, Integer offset, Integer limit,String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException;
	List<FruitoreIstanza> getIstanze(String codiceModulo, String stato,  String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa, Date dataA, String ordinamento, boolean test, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException;
	Integer getCountIstanze(String codiceModulo, String stato, String versioneModulo, String codiceEnte, String codiceAmbito, String identificativoUtente, Date dataDa,Date dataA, boolean test, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;	
	FruitoreIstanzaDettagliata getIstanza(String codiceIstanza, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;		

	String updateStatoAcquisizione(String codiceistanza, String codiceAzione, StatoAcquisizioneRequest fruitoreAcquisizione, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;		
	
	String getNotifica(String codiceistanza, StatoAcquisizioneRequest fruitoreAcquisizione, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	Allegato getAllegato(String codiceIstanza,String codiceFile, String formIoNameFile, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	byte[] getIstanzaPdf(String codiceIstanza, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	
	List<FruitoreModuloVersione> getElencoModuli(ModuliFilter filter, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, BusinessException;
	FruitoreModuloVersione getModuloVersione(String codiceModulo, String versioneModulo, String fields,	String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	
	byte[] getReport(String codiceModulo, String codice, Date dataDa, Date dataA, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	StreamingOutput getStreamReport(String codiceModulo, String codice, Date dataDa, Date dataA, String clientProfile, String xRequestId) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	
}
