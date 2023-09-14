/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonbobl.dto.moonfobl.Processo;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import net.sourceforge.plantuml.SourceStringReader;

public class Processo2ImageHelper {
	
	private final static String CLASS_NAME = "Processo2ImageHelper";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	private static final String HEADER = "@startuml\r\n"
			+ "scale 800 width\r\n"
			+ "!theme cerulean\r\n"
			+ "\r\n"
			+ "[*] -> BOZZA\r\n";
	private static final String FOOTER = "@enduml";
	private static final String BODY_EMPTY = "[*] --> [*]\r\n";
	private static final String CRLF = "\r\n";
	private static final String STARTUML_SAMPLE = "@startuml\r\n"
			+ "scale 600 width\r\n"
			+ "!theme cerulean\r\n"
			+ "\r\n"
			+ "[*] -> BOZZA\r\n"
			+ "BOZZA--> DA_INVIARE : COMPLETA\r\n"
			+ "DA_INVIARE --> INVIATA : INVIA\r\n"
			+ "DA_INVIARE --> BOZZA : RIPORTA_IN_BOZZA\r\n"
			+ "INVIATA --> DEPOSITATA : INTEGRAZIONE_FRUITORE_OK\r\n"
			+ "INVIATA --> ACQUISITA_CON_ERRORE : INTEGRAZIONE_FRUITORE_KO\r\n"
			+ "DEPOSITATA --> REGISTRATA_DA_PA : PROTOCOLLA\r\n"
			+ "REGISTRATA_DA_PA --> [*]\r\n"
			+ "ACQUISITA_CON_ERRORE --> INVIATA : TORNA_IN_INVIATA\r\n"
			+ "ACQUISITA_CON_ERRORE --> BOZZA : RIPORTA_IN_BOZZA\r\n"
			+ "@enduml";
	
	@Autowired
	StatoDAO statoDAO;
	
	public Processo2ImageHelper() {
        //must provide autowiring support to inject SpringBean
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	public byte[] makeStateDiagram(Processo processo) throws BusinessException {
		String sourceStringUml = null;
		try {
			sourceStringUml = buildUmlString(processo);
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::makeStateDiagram] sourceStringUml =\r\n" + sourceStringUml);
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			SourceStringReader reader = new SourceStringReader(sourceStringUml);
	        // Write the first image to "png"
	        String desc = reader.generateImage(bos);
	        
			return bos.toByteArray();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::makeStateDiagram] ERROR generateImage with sourceStringUml =\r\n" + sourceStringUml, e);
			throw new BusinessException();
		}
	}

	private String buildUmlString(Processo processo) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::buildUmlString] processo = " + processo);
		}
		String body = BODY_EMPTY;
		String finalStates = null;
		if (processo != null || processo.getWorkflows() != null || !processo.getWorkflows().isEmpty()) {
			body = processo.getWorkflows().stream()
				.map(wf -> String.format("%s --> %s : %s"
					, (wf.getStatoPartenza()!=null && wf.getStatoPartenza().getCodice()!=null) ? wf.getStatoPartenza().getCodice() : wf.getIdStatoWfPartenza()
					, (wf.getStatoArrivo()!=null && wf.getStatoArrivo().getCodice()!=null) ? wf.getStatoArrivo().getCodice() : wf.getIdStatoWfArrivo()
					, wf.getCodiceAzione()) )
				.collect(Collectors.joining(CRLF));
			//
			List<Integer> listIdStatoArrivo = retrieveElencoIdStatoArrivoFinali(processo);
			finalStates = listIdStatoArrivo.stream()
				.map(this::findCodiceStato)
				.map(codiceFinalState -> String.format("%s --> [*]", codiceFinalState) )
				.collect(Collectors.joining(CRLF));
			log.debug("[" + CLASS_NAME + "::buildUmlString] finalStates = " + finalStates);
		}

		return new StringBuilder()
			.append(HEADER)
			.append(body)
			.append(CRLF)
			.append(finalStates)
			.append(CRLF)
			.append(FOOTER)
			.toString();
	}

	private List<Integer> retrieveElencoIdStatoArrivoFinali(Processo processo) {
		List<Integer> listIdStatoPartenza = processo.getWorkflows().stream()
			.map(Workflow::getIdStatoWfPartenza)
			.distinct()
			.collect(Collectors.toList());
		log.debug("[" + CLASS_NAME + "::retrieveElencoIdStatoArrivoFinali] listIdStatoPartenza = " + listIdStatoPartenza);
		Predicate<? super Integer> notPresenteLikeIdStatoPartenza = idStato -> listIdStatoPartenza.stream().noneMatch(idStato::equals);
		List<Integer> listIdStatoArrivo = processo.getWorkflows().stream()
			.map(Workflow::getIdStatoWfArrivo)
			.filter(notPresenteLikeIdStatoPartenza)
			.collect(Collectors.toList());
		log.debug("[" + CLASS_NAME + "::retrieveElencoIdStatoArrivoFinali] listIdStatoArrivo = " + listIdStatoArrivo);
		return listIdStatoArrivo;
	}
	private String findCodiceStato(Integer idStato) {
		return statoDAO.findById(idStato).getCodiceStatoWf();
	}
}
