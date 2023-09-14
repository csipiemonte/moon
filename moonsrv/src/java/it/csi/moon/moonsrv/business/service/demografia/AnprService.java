/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.demografia;

import java.util.List;

import it.csi.apimint.demografia.v1.dto.Soggetto;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Layer di logica servizi che richiama i DAO ANPR 
 * Implementazioni :
 *  - WS :: via API Manager WebService
 *  - RS :: via API Manager Outer REST
 * 
 * @author Laurent
 * 
 * @since 1.0.0
*/
public interface AnprService {
	
	public List<ComponenteFamiglia> getFamigliaANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException;
//	@Deprecated
//	public List<ComponenteFamiglia> getFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String consumerPrefix, String ipAdress, String utente) throws BusinessException;
	public List<ComponenteFamiglia> getFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException;
	
	public List<Soggetto> getSoggettiFamigliaANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException;
//	@Deprecated
//	public List<Soggetto> getSoggettiFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String consumerPrefix, String ipAdress, String utente) throws BusinessException;
	public List<Soggetto> getSoggettiFamigliaANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException;
//	@Deprecated
//	public List<Soggetto> getSoggettiFamigliaANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey, String consumerPrefix) throws BusinessException;
	public List<Soggetto> getSoggettiFamigliaANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey) throws BusinessException;

	public Soggetto getSoggettoANPR(String cfRicercato, String userJwt, String ipAdress, String utente) throws BusinessException;
//	@Deprecated
//	public Soggetto getSoggettoANPR(String cfRicercato, String userJwt, String clientProfileKey, String consumerPrefix, String ipAdress, String utente) throws BusinessException;
	public Soggetto getSoggettoANPR(String cfRicercato, String userJwt, String clientProfileKey, String ipAdress, String utente) throws BusinessException;
//	@Deprecated
//	public Soggetto getSoggettoANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey, String consumerPrefix) throws BusinessException;
	public Soggetto getSoggettoANPRForInit(String cfRicercato, IstanzaInitParams initParams, String clientProfileKey) throws BusinessException;
}
