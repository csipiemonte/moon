/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaEstrattoreDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.util.LoggerAccessor;


/**
 * Contruttore di oggetto JSON per MOOnPrint
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ReportIstanzaMapperDefault extends BaseReportIstanzaMapper implements ReportIstanzaMapper {
	
	private final static String CLASS_NAME = "ReportIstanzaMapperDefault";
	private final static Logger log = LoggerAccessor.getLoggerBusiness();
	private static final int FIRST_LEVEL = 1;
	
	@Autowired
	IstanzaEstrattoreDAO istanzaEstrattoreDAO;

	/**
     * Remap l'istanza con il suo modulo  in una stringa
     * 
     * @param istanza
     * @param strutturaEntity
     * 
     * @return String stringa che contiene tutte le righe del csv
     * 
     * @throws Exception 
     */
	public List<String> remap(List<Istanza> elenco) throws Exception {

		ArrayList<String> rows = new ArrayList<String>();
		
		return rows;
	}


	@Override
	public String remapIstanza(IstanzaEstratta istanza) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public StreamingOutput getStreamingOutput(UserInfo user, String filtro, Long idModulo) {
		IstanzeFilter filter = new IstanzeFilter();		
		filter.setIdModuli(Arrays.asList(idModulo));
		String sort = "i.data_creazione";
		filter.setSort(sort);
		
		return new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(os))) {						
					writer.write(getHeader());
					writer.write('\n');										
					int n = istanzaEstrattoreDAO.estraiIstanze(filter, istanza -> {	
						try {
							writer.write(remapIstanza(istanza));
							writer.write('\n');														
						}catch (Exception e) {
							log.error("[ReportIstanzaMapperDefault::getStreamingOutput()] Errore estraiIstanze id_istanza = " + istanza.getIdIstanza() + e.getMessage(),e);
//							throw new RuntimeException(e);
						}
					});						
				}
			}
		};
	}

}
