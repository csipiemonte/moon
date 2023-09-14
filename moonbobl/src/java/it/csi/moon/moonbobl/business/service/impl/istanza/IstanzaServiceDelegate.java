/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.istanza;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.util.MapModuloAttributi;

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
	public IstanzaSaveResponse invia(UserInfo user, IstanzaEntity istanzaE, ModuloVersionatoEntity moduloE, DatiAggiuntiviHeaders daHeaders, String ipAddress) throws BusinessException;
	
}
