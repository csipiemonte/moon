/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.mapper.report;

import java.util.List;

import javax.ws.rs.core.StreamingOutput;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;

public interface ReportIstanzaMapper {

	public List<String> remap(List<Istanza> elenco) throws Exception;
	
	public String remapIstanza(IstanzaEstratta istanza) throws Exception;
	
	public String getHeader();

	public StreamingOutput getStreamingOutput(UserInfo user, String filtro, Long idModulo);
	
}
