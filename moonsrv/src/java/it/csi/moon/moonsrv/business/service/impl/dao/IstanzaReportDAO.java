/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.commons.entity.IstanzaApiEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso a reportistica istanze
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface IstanzaReportDAO {
		
	public int findForApiReport(String codiceEstrazione, Long idModulo, Date dateDa, Date dateA, Consumer<IstanzaReport> consumer);	
																  
}
