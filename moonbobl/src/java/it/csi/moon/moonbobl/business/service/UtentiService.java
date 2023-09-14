/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.extra.istat.CodiceNome;
import it.csi.moon.moonbobl.dto.moonfobl.Funzione;
import it.csi.moon.moonbobl.dto.moonfobl.Gruppo;
import it.csi.moon.moonbobl.dto.moonfobl.Ruolo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.Utente;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteEnteAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;


/**
 * @author Laurent
 * Metodi di business relativi ai istanze
 */
public interface UtentiService {
	List<Utente> getElencoUtenti() throws BusinessException;
	List<UtenteEnteAbilitato> getUtentiEnteAbilitato(UserInfo user) throws BusinessException;
	List<UtenteEnteAbilitato> getUtentiEnteNoAbilitato(UserInfo user) throws BusinessException;
	Utente getUtenteById(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	Utente getUtenteByIdentificativo(String identificativoUtente) throws ItemNotFoundBusinessException, BusinessException;
	Utente createUtente(UserInfo user, Utente body) throws BusinessException;
	Utente updateUtente(UserInfo user, Long idUtente, Utente body) throws BusinessException;
	Utente patchUtente(UserInfo user, Long idUtente, Utente partialUtente) throws BusinessException;
	
	//
	List<Ruolo> getRuoliByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	UtenteEnteAbilitato addEnteAreaRuolo(UserInfo user, Long idUtente, Long idEnte, Long idArea, Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException;
	UtenteEnteAbilitato deleteEnteAreaRuolo(UserInfo user, Long idUtente, Long idEnte, Long idArea, Integer idRuolo) throws ItemNotFoundBusinessException, BusinessException;
	List<Funzione> getFunzioniByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	List<Gruppo> getGruppiByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;

	//
	public List<CodiceNome> getComuniAbilitati(UserInfo user) throws BusinessException;
	public List<CodiceNome> getEntiAbilitati(UserInfo user) throws BusinessException;
	public Boolean isUtenteAbilitato(UserInfo user,Long idModulo, String filtroRicercaDati) throws BusinessException;
	
	//
	public void addUtenteModulo(UserInfo user, Long idUtente, Long idModulo) throws BusinessException;
	public void deleteUtenteModulo(UserInfo user, Long idUtente, Long idModulo) throws BusinessException;

}
