/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service;

import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.business.UnauthorizedBusinessException;

public interface ApiService {

	List<Istanza> getIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter, UserInfo user) throws BusinessException;
	ResponsePaginated<Istanza> getIstanzePaginate(IstanzeFilter filter, Optional<IstanzeSorter> sorter, UserInfo user) throws BusinessException;
//	List<String> getIstanze(String codiceModulo, String stato,  String versioneModulo, String codiceEnte,Date dataDa, Date dataA, boolean test, UserInfo user) throws UnauthorizedBusinessException, BusinessException;
//	List<String> getIstanzePaginate(String codiceModulo, String stato,  String versioneModulo, String codiceEnte,Date dataDa, Date dataA, boolean test, Integer offset, Integer limit, UserInfo user) throws UnauthorizedBusinessException, BusinessException;
//	Integer getCountIstanze(String codiceModulo, String stato, String versioneModulo, String codiceEnte, Date dataDa,Date dataA, boolean test, UserInfo user) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;	

	Istanza getIstanza(String codiceIstanza, UserInfo user) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;		
//	String updateStatoAcquisizione(String codiceistanza, String codiceAzione, StatoAcquisizioneRequest fruitoreAcquisizione, UserInfo user) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;		
//	String getNotifica(String codiceistanza, StatoAcquisizioneRequest fruitoreAcquisizione, UserInfo user) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
//	Allegato getAllegato(String codiceIstanza,String codiceFile, String formIoNameFile, UserInfo user) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	byte[] getIstanzaPdf(String codiceIstanza, UserInfo user, String ipAddress) throws UnauthorizedBusinessException, ItemNotFoundBusinessException, BusinessException;
	
	public List<Modulo> getElencoModuli(ModuliFilter filter) throws BusinessException;
}
