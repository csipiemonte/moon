/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service;

import java.util.Date;

import javax.ws.rs.core.StreamingOutput;

import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business relativi alla reportistica moduli
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public interface ReportService {

	public byte[] getReportByCodiceModulo(String codiceModulo) throws BusinessException;
	public byte[] getReportByCodiceModulo(String codiceModulo, Date dataDa, Date dataA) throws BusinessException;
	public byte[] getReportByCodiceModuloCodiceReport(String codiceModulo,String codice) throws BusinessException;
	public StreamingOutput getStreamReportByCodiceModulo(String codiceModulo, Date dataDa, Date dataA) throws BusinessException;
	public StreamingOutput getStreamReportByCodiceModuloCodiceReport(String codiceModulo,String codice,Date dataDa, Date dataA) throws BusinessException;	
}
