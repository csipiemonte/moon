/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.mapper.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaReportDAO;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class ReportMapperDefault extends BaseReportMapper implements ReportMapper {
	
	private final static String CLASS_NAME = "ReportMapperDefault";
	private final String DEFAULT_VALUE = "";
	
	@Autowired
	IstanzaReportDAO istanzaReportDAO;

	public ReportMapperDefault() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String remapIstanza(IstanzaReport istanza) throws Exception {
	
		StringBuilder row = new StringBuilder();
		row.append(istanza.getCodiceIstanza()).append(CSV_SEPARATOR);
		row.append(istanza.getNumeroProtocollo()).append(CSV_SEPARATOR);
		row.append(fDataOra.format(istanza.getCreated())).append(CSV_SEPARATOR);
		
		//setting jsonPathUtil
		setJsonPathUtil(istanza.getData().toString());		
		setRoot(".data");
		
		String cognome = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".richiedente.cognome"));
		row.append(cognome).append(CSV_SEPARATOR);
		
		String nome = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".richiedente.nome"));
		row.append(nome).append(CSV_SEPARATOR);
		
		String email = jsonPathUtil.getStringValue(jsonPathUtil.getValue("$"+root+".richiedente.email"));
		row.append(email).append(CSV_SEPARATOR);

		return row.toString();
	}


	@Override
	public String getHeader() {	
		
		StringBuilder sb = new StringBuilder();
		sb.append("N. Istanza Moon").append(CSV_SEPARATOR);
		sb.append("N. Protocollo").append(CSV_SEPARATOR);
		sb.append("Data Richiesta").append(CSV_SEPARATOR);
		sb.append("Cognome").append(CSV_SEPARATOR);
		sb.append("Nome").append(CSV_SEPARATOR);
		sb.append("Indirizzo Email").append(CSV_SEPARATOR);
		
		return sb.toString();
	}


	@Override
	public StreamingOutput getStreamingOutput(Long idModulo,Date dataDa, Date dataA) {
		IstanzeFilter filter = new IstanzeFilter();			
		return new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(os))) {						
					writer.write(getHeader());
					writer.write('\n');										
					int n = istanzaReportDAO.findForApiReport(null, idModulo, dataDa, dataA, istanza -> {	
						try {
							writer.write(remapIstanza(istanza).replaceAll("[\\n\\r]", " "));
							writer.write('\n');														
						} catch (Exception e) {
							LOG.error("[ReportMapperDefault::getStreamingOutput()] Errore findForApiReport id_istanza = " + istanza.getIdIstanza() + e.getMessage(),e);			
						}
					});						
				}
			}
		};
	}


}
