/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service;

import java.util.List;

import it.csi.moon.moonbobl.dto.moonfobl.LogPraticaCosmo;
import it.csi.moon.moonbobl.dto.moonfobl.LogServizioCosmo;

public interface CosmoService {

	public List<LogPraticaCosmo> getElencoLogPraticaByIdIstanza(Long idIstanza);

	public List<LogServizioCosmo> getElencoLogServizioByIdIstanza(Long idIstanza);
	
}
