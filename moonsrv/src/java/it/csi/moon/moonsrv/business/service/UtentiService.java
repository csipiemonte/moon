/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.List;

import it.csi.moon.commons.dto.Funzione;
import it.csi.moon.commons.dto.Gruppo;
import it.csi.moon.commons.dto.Ruolo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.Utente;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundBusinessException;

/**
 * Metodi di business relativi alle utenti
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface UtentiService {
	List<Utente> getElencoUtenti() throws BusinessException;
	Utente getUtenteById(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	Utente getUtenteByIdentificativo(String identificativoUtente) throws ItemNotFoundBusinessException, BusinessException;
	Utente createUtente(/*UserInfo user, */Utente utente) throws BusinessException;
	Utente updateUtente(Long idUtente, Utente body) throws BusinessException;
	Utente patchUtente(Long idUtente, Utente partialUtente) throws BusinessException;
	
	//
	List<Ruolo> getRuoliByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	List<Funzione> getFunzioniByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	List<Gruppo> getGruppiByIdUtente(Long idUtente) throws ItemNotFoundBusinessException, BusinessException;
	
	void addUtenteModulo(UserInfo user, Long idUtente, Long idModulo);
	void deleteUtenteModulo(UserInfo user, Long idUtente, Long idModulo);
}
