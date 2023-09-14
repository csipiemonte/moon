/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.demografia;

import java.util.List;

import it.csi.moon.moonbobl.dto.extra.demografia.Nazione;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;

/**
 * Layer di logica servizi che estrai gli StatiEsteri ANPR in oggetto Nazione a secondo dell'utilizzo
 * 
 * @author Laurent
 * 
 * @since 1.0.0
*/
public interface AnprNazioneService {
	
	public List<Nazione> listaNazioniResidenza() throws BusinessException;
	public List<Nazione> listaNazioniCittadinanza() throws BusinessException;
	public List<Nazione> listaNazioniNascita() throws BusinessException;
	
	public Nazione getNazioneById(Integer codiceIstat) throws BusinessException;
	
}
