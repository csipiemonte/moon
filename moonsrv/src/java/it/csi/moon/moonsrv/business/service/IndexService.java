/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

import java.util.Date;

import it.csi.moon.commons.dto.Modulo;

public interface IndexService {

	public String salvaIstanzaIndexById(Long idIstanza, Long idRichiesta, Modulo modulo);
	
	public String salvaIstanzaIndexById(Long idIstanza, Long idRichiesta);
	
	public byte[] getContentByUid(String uid);

	public String archiviaIndexByModulo(String codiceOrIdModulo, Date data);

	public String deleteContentByModulo(String codiceOrIdModulo, Date data);
	
	public String deleteContentIstanzaById(Long idIstanza);

}
