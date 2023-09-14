/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.UnivocitaIstanzaBusinessException;

/**
 * Delegate delle operazioni di IstanzeServiceImpl che possono essere specializzate per Modulo
 * Attualmente 2 metodi :
 * - getInitIstanzaByIdModulo : generalmente per bloccare l'accesso al modulo per esaurimento delle risorsa
 * - saveIstanza : generalmente per salvataggio ulteriore in altre tabelle
 * 
 * saveIstanza rimane transazionale da IstanzeServiceImpl, quindi qualsiasi Exception annulla l'inserimento dell'istanza
 * 
 * @author laurent
 *
 */
public interface IstanzaServiceDelegate {

	public Istanza getInitIstanza(UserInfo user, ModuloVersionatoEntity moduloEntity, MapModuloAttributi attributi, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException;
	public IstanzaSaveResponse saveIstanza(UserInfo user, Istanza istanza) throws BusinessException;
	public IstanzaSaveResponse invia(UserInfo user, IstanzaEntity istanzaE, ModuloVersionatoEntity moduloE, String ipAddress) throws BusinessException;
	public IstanzaSaveResponse gestisciPagamento(UserInfo user, Istanza istanza) throws BusinessException;
	public IstanzaSaveResponse pagaOnline(UserInfo user, Istanza istanza) throws BusinessException;
	public IstanzaSaveResponse pagaSportello(UserInfo user, Istanza istanza) throws BusinessException;
	public IstanzaSaveResponse annulla(UserInfo user, Istanza istanza) throws BusinessException;
	
}
