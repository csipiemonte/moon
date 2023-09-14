/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ReportApi;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ReportService;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapper;
import it.csi.moon.moonbobl.business.service.mapper.report.ReportIstanzaMapperFactory;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.util.LoggerAccessor;

@Component
public class ReportApiImpl extends MoonBaseApiImpl implements ReportApi {
	
	private final static String CLASS_NAME = "ReportApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();	
	
	private final static String CODICE_MOD_RILEVAZ_SER_INFANZIA = "RP_RSI";
	private static final char CSV_SEPARATOR = ';';
	
	@Autowired
	ReportService reportService;
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModuloDAO moduloDAO;
	
	public Response getNumeroModuliInviati(String fields ,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getNumeroModuliInviati(user,modulo.getIdModulo());
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getNumeroModuliInviati] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNumeroModuliInviati] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getNumeroComuniCompilanti(String filtro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getNumeroComuniCompilanti(user,modulo.getIdModulo());
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getNumeroComuniCompilanti] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNumeroComuniCompilanti] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getNumServizi02(String filtro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getCount(user,modulo.getIdModulo(), "servizi02");
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getNumServizi02] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNumServizi02] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getTotFreq02(String filtro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getCount(user,modulo.getIdModulo(), "numero02");
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getTotFreq02] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getTotFreq02] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getNumServizi36(String filtro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getCount(user,modulo.getIdModulo(), "servizi36");
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getNumServizi36] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getNumServizi36] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getTotFreq36(String filtro, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName(); 
		try {
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findByCodice(CODICE_MOD_RILEVAZ_SER_INFANZIA);
			Integer count = reportService.getCount(user,modulo.getIdModulo(), "numero36");
			return Response.ok(count).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getTotFreq36] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getTotFreq36] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}

	@Override
	public Response getCSVReport(String filtro, Long idModulo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
				   
		try {
			UserInfo user = retrieveUserInfo(httpRequest);			
			ModuloEntity modulo = moduloDAO.findById(idModulo);

			if(!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
				throw new BusinessException("Utente non abilitato ad operare sul modulo indicato");
			}
			
			IstanzeFilter filter = new IstanzeFilter();
			if (modulo.getIdModulo() != null) {
				filter.setIdModuli(Arrays.asList(modulo.getIdModulo()));
			}
			
			String sort = "i.data_creazione";
			filter.setSort(sort);
			
			filter.setUsePagination(true);
			filter.setLimit(100);
			
			List<String> rowsList = reportService.getRowsCSV(filter, modulo.getCodiceModulo());
			StringBuilder sb = new StringBuilder();
			for (String  row : rowsList) {
				sb.append(row);
				sb.append("\n");
			}
			String allRows = sb.toString();
			
			//InputStream is;
			//byte[] bytes = IOUtils.toByteArray(is);
			byte[] bytes = allRows.getBytes("UTF-8");
			
			Calendar dataOdierna = Calendar.getInstance();
			DateFormat dF = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String timestampForFileName = dF.format(dataOdierna.getTime());
			String nomeFile = modulo.getCodiceModulo() + "_" +timestampForFileName+".csv";
			return Response.ok(bytes)
        		.header("Cache-Control", "no-cache, no-store, must-revalidate")
        		.header("Pragma", "no-cache")
        		.header("Expires", "0")
        		.header(HttpHeaders.CONTENT_TYPE, "text/csv")
        		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;"+nomeFile)
        		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
        		.build();
			
			// .header(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel")
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCSVReport] Errore servizio ",e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCSVReport] Errore generico servizio ",ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}  
	}


	
	
	
	@Override
	public Response getLargeCSVReport(String filtro, String idModuloPP, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String nameofCurrMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
		
		long started = System.currentTimeMillis();

		try {
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			ModuloEntity modulo = moduloDAO.findById(idModulo);

			if (!moduliService.verificaAbilitazioneModulo(idModulo, user)) {
				throw new BusinessException("Utente non abilitato ad operare sul modulo indicato");
			}
			ReportIstanzaMapper mapper = new ReportIstanzaMapperFactory()
				.getReportIstanzaMapper(modulo.getCodiceModulo());
			StreamingOutput stream = mapper.getStreamingOutput(user, filtro, idModulo);

			Calendar dataOdierna = Calendar.getInstance();
			DateFormat dF = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String timestampForFileName = dF.format(dataOdierna.getTime());
			String nomeFile = modulo.getCodiceModulo() + "_" + timestampForFileName + ".csv";

			return Response.ok(stream).header("Cache-Control", "no-cache, no-store, must-revalidate")
					.header("Pragma", "no-cache").header("Expires", "0").header(HttpHeaders.CONTENT_TYPE, "text/csv")
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;" + nomeFile)
					//.header(HttpHeaders.CONTENT_LENGTH, byteArray.length)
					.build();

			// .header(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel")
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCSVReport] Errore servizio ", e);
			throw new ServiceException("Errore servizio " + nameofCurrMethod);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCSVReport] Errore generico servizio ", ex);
			throw new ServiceException("Errore generico servizio " + nameofCurrMethod);
		}
	}
	
	
	

	
}
