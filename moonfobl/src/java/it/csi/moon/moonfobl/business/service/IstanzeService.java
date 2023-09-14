/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.ResponsePaginated;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;

/**
 * Metodi di business relativi ai istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface IstanzeService {
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter) throws BusinessException;	
	public Istanza getIstanzaById(UserInfo user, Long idIstanza) throws BusinessException;
	public Istanza getIstanzaByCd(UserInfo user, String codiceIstanza) throws BusinessException;
	public Long getIdIstanzaByCd(UserInfo user, String codiceIstanza) throws BusinessException;
	public Istanza getInitIstanza(UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException;	
	public IstanzaSaveResponse saveIstanza(UserInfo user, Istanza istanza) throws BusinessException;
	public IstanzaSaveResponse saveIstanza(UserInfo user, Long idIstanza, Istanza body) throws BusinessException;
	public Istanza deleteIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public Istanza patchIstanza(UserInfo user, Long idIstanza, Istanza partialIstanza) throws BusinessException;
	public Istanza riportaInBozza(UserInfo user, Long idIstanza) throws BusinessException;
	public IstanzaSaveResponse invia(UserInfo user, Long idIstanza, String ipAddress) throws BusinessException;
	public IstanzaSaveResponse compieAzione(UserInfo user, Long idIstanza, Long idAzione, String ipAddress) throws BusinessException;
	public ResponsePaginated<Istanza> getElencoIstanzePaginated(IstanzeFilter filter, String filtroRicerca, Optional<IstanzeSorter> optSorter) throws BusinessException;
	public byte[] getPdfIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public byte[] getNotificaIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public byte[] getDocumentoByFormioNameFile(UserInfo user, String formioNameFile) throws BusinessException;
	public byte[] getDocumentoByIdFile(UserInfo user, Long idFile) throws BusinessException;
	public Documento getDocumentoNotificaIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public String inviaRispostaIntegrazioneCosmo(Long idIstanza) throws BusinessException;
	public IstanzaSaveResponse creaIUV(UserInfo user, Long idIstanza) throws BusinessException;
	public Istanza deleteIstanzaForApi(UserInfo user, String codiceIstanza) throws BusinessException;
	public IstanzaSaveResponse duplica(UserInfo user, Long idIstanza, Boolean duplicatiAllegati, String ipAddress) throws BusinessException;
	public void validateUserAccess(UserInfo user, IstanzaEntity entity) throws BusinessException;
	public Istanza duplicaForApi(UserInfo user, String codiceIstanza, Boolean duplicaAllegati) throws BusinessException;

}
