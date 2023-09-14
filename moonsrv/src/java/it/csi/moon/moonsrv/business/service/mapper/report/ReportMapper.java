/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report;

import java.util.Date;

import javax.ws.rs.core.StreamingOutput;

import it.csi.moon.commons.dto.api.IstanzaReport;

public interface ReportMapper {
	
	public String remapIstanza(IstanzaReport istanza) throws Exception;
	
	public String getHeader();

	public StreamingOutput getStreamingOutput(Long idModulo,Date dataDa, Date dataA);

}
