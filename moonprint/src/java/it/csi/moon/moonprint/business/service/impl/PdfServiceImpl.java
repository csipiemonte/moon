/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.business.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonprint.business.service.PdfService;
import it.csi.moon.moonprint.exceptions.service.ServiceException;
import it.csi.moon.moonprint.renderer.PdfRenderer;
import it.csi.moon.moonprint.util.LoggerAccessor;

@Component
public class PdfServiceImpl  implements PdfService{
	
	private static final String TEMPLATES_PATH_PROP_NAME = "TemplatesPath";
	private static final String CLASS_NAME = "PdfServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	private String templatesPathCached;
	
	@Autowired
	private PdfRenderer pdfRenderer;
	
	public PdfServiceImpl() {
	}

	@Override
	public byte[] getPdfMock() throws ServiceException {
		try {
			LOG.debug("[" + CLASS_NAME + "::" + "getPdfMock] BEGIN");
			
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("response.pdf");
			byte[] result = is.readAllBytes();
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + "getPdfMock] result = "+result);
			}
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + "getPdfMock] Errore - ", e);
			throw new ServiceException("Errore recupero PdfMock");
		}
	}

	@Override
	public byte[] getPdf(String body) throws ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] IN body="+body);
			}
			
			String template = "";
			String json = "";			

			int positionPipe = body.indexOf("|");
			if (positionPipe <= 0 && positionPipe < 80) {
				template = "default";
			}else {
				template = body.substring(0,positionPipe);
			}
			json = body.substring(positionPipe+1,body.length());

			// JAR INTERNO
			LOG.debug("[" + CLASS_NAME + "::" + "getPdf] new PdfRenderer() ...");
			PdfRenderer pdfRenderer = new PdfRenderer(getTemplatesPathCached());

			byte[] result = pdfRenderer.toPdf(json,template);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] Pdf generated : result.length = "+(result!=null?result.length:"null"));
			}
			return result;

		} catch (Exception e) {
			// e.printStackTrace(System.out);
			LOG.error("[" + CLASS_NAME + "::" + "getPdf] Errore - ", e);
			throw new ServiceException("Errore recupero getPdf");
		} finally {
			try {
//				child.close();
//			    child = null;
			    // System.out.println("[" + CLASS_NAME + "::" + "] URLClassLoader CLOSE");
			    // Avviamo il garbage collector, vedere se mantenere
			    Runtime.getRuntime().gc(); 
			} catch (Exception e) {
				// e.printStackTrace(System.out);
			}
		}
	}

	private String retrieveEnvTemplatesPath() {
		// Retrieve template path
		// IMPORTANTE: il path e' configurato nel file nella directory di wildfly: \standalone\configuration\pdfRenderer.properties
		//     e deve essere inserito nel formato: TemplatesPath=PATH_COMPLETO
		String fileName = "";
		try {
			fileName = System.getProperty("jboss.server.config.dir")+File.separator+"pdfRenderer.properties";
			File propFile = new File(fileName);
			java.util.Properties props = new java.util.Properties();
			if (!propFile.exists()) {
				throw new ServiceException("Errore recupero getPdf: Il file con la definzione del path ai template non trovato");
			}
			try (FileInputStream fis = new FileInputStream(propFile)) {
				props.load(fis);
			}
	
			if (!props.containsKey(TEMPLATES_PATH_PROP_NAME)) {
				throw new ServiceException("Errore recupero getPdf: Errora durante la lettura del path ai template --> Verificare il file pdfRenderer.properties");
			}
			if (!(new File(props.getProperty(TEMPLATES_PATH_PROP_NAME)).exists())) {
				throw new ServiceException("Errore recupero getPdf: Path con i template non valido --> Verificare il file pdfRenderer.properties; verificare che il percorso specificato sia presente e i relativi permessi di accesso");
			}
	//			System.out.println("Props => "+props.getProperty("TemplatesPath"));
			//mettere il percorso assoluto della cartella dove ci sono il jar e il template 
			//private String destinazione = "D:\\W3Lab\\2020\\CSI\\_src\\MOOnPrintRenderer\\target";
			String destinazione = props.getProperty(TEMPLATES_PATH_PROP_NAME);
			return destinazione;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::" + "getPdf] Errore recupero file di properties : " + fileName);
			throw new ServiceException("Errore recupero file di properties");
		}
	}

	public String getTemplatesPathCached() {
		if (templatesPathCached==null) {
			templatesPathCached = retrieveEnvTemplatesPath();
		}
		return templatesPathCached;
	}
	
	@Override
	public byte[] getPdf(String template, String body) throws ServiceException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] BEGIN");
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] IN template="+template);
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] IN body="+body);
			}
			template = StringUtils.isBlank(template)?"default":template;
			
			// JAR INTERNO
			LOG.debug("[" + CLASS_NAME + "::" + "getPdf] new PdfRenderer() ...");
			// PdfRenderer pdfRenderer = new PdfRenderer(getTemplatesPathCached());
			byte[] result = pdfRenderer.toPdf(body,template);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::" + "getPdf] Pdf generated : result.length = "+(result!=null?result.length:"null"));
			}
			return result;

		} catch (Exception e) {
			// e.printStackTrace(System.out);
			LOG.error("[" + CLASS_NAME + "::" + "getPdf] Errore - ", e);
			throw new ServiceException("Errore recupero getPdf");
		} finally {
			try {
//				child.close();
//			    child = null;
			    // System.out.println("[" + CLASS_NAME + "::" + "] URLClassLoader CLOSE");
			    // Avviamo il garbage collector, vedere se mantenere
			    Runtime.getRuntime().gc(); 
			} catch (Exception e) {
				// e.printStackTrace(System.out);
			}
		}
	}

}
