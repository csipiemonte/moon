/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonsrv.business.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Date;

import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.ReportService;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapper;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapperFactory;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

/**
 * Metodi di business relativi alla reportistica moduli
 *
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
@Configurable
public class ReportServiceImpl  implements ReportService {

	private static final String CLASS_NAME = "ReportServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	IstanzeService istanzeService;
	
	@Autowired
	ModuliService moduliService;

	
	/**
	 * Servizio generazione report di default in formato csv
	 */	
	@Override
	public byte[] getReportByCodiceModulo(String codiceModulo) throws BusinessException {
		return getReportTestByCSVWriter();

	}

	/**
	 * Servizio generazione report con tipo di estrazione definito dall'attributo codice
	 */	
	@Override
	public byte[] getReportByCodiceModuloCodiceReport(String codiceModulo, String codice) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	
	private byte[] getReportTestByCSVWriter() throws BusinessException {
		var output = new ByteArrayOutputStream();
		try (var printer = new CSVPrinter(new OutputStreamWriter(output, StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            printer.printRecord("ID", "USERNAME", "FIRSTNAME", "LASTNAME", "BIRTHDAY");
            printer.printRecord(1, "john73", "John", "Doe", LocalDate.of(1973, 9, 15));
            printer.println();
            printer.printRecord(2, "mary", "Mary", "Meyer", LocalDate.of(1985, 3, 29));
            return output.toByteArray();
		} catch (IOException ioex) {
			LOG.error("[" + CLASS_NAME + "::getReportTestByCSVWriter] IOException ", ioex);
			throw new BusinessException();
        }
	}

	@Override
	public byte[] getReportByCodiceModulo(String codiceModulo, Date dataDa, Date dataA) throws BusinessException {
		return getReportTestByCSVWriter();
	}

	@Override
	public StreamingOutput getStreamReportByCodiceModulo(String codiceModulo, Date dataDa, Date dataA)
			throws BusinessException {		
		ReportMapper mapper = new ReportMapperFactory().getReportMapper(codiceModulo,"");
		Long idModulo = moduliService.getIdModuloByCodice(codiceModulo);
		StreamingOutput stream = mapper.getStreamingOutput(idModulo, dataDa, dataA);
		return stream;
	}

	@Override
	public StreamingOutput getStreamReportByCodiceModuloCodiceReport(String codiceModulo, String codiceEstrazione, Date dataDa, Date dataA)
			throws BusinessException {
		ReportMapper mapper = new ReportMapperFactory().getReportMapper(codiceModulo,codiceEstrazione);
		Long idModulo = moduliService.getIdModuloByCodice(codiceModulo);
		StreamingOutput stream = mapper.getStreamingOutput(idModulo, dataDa, dataA);
		return stream;
	}


}
