/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.istanza;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.IstanzaSaveResponse;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.DocumentoRiconoscimento;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.ext.DomandaBuoniDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.ext.DomandaBuoniEntity;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.util.Constants;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * IstanzaDelegate_BUONO_SPESA - Specializzazione.
 *  - saveUlteriori richiamato da saveIstanza per salvataggio ulteriore in moon_ext_domanda_buoni via domandaBuoniDAO
 *  - il blocco alla ri-compilazione del modulo era gestito via LoginBusinessException.ErrType.DOMANDA_GIA_PRESENTATA 
 *    in quanto il modulo era stato pubblicato solo via moonDirect ed era l'unico modulo accessibile con il mecanismo custom du auth
 * 
 * Quindi questo codice non ha mai girato in moonfobl
 * 
 * @author laurent
 *
 */
public class IstanzaDelegate_BUONO_SPESA extends IstanzaDefaultDelegate implements IstanzaServiceDelegate {

	private static final String CLASS_NAME = "IstanzaDelegate_BUONO_SPESA";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	DomandaBuoniDAO domandaBuoniDAO;
	
	public IstanzaDelegate_BUONO_SPESA() {
		super();
		LOG.debug("[" + CLASS_NAME + "::" + CLASS_NAME + "]");
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	//
	// SAVE ISTANZA
	//
	@Override
	protected void saveUlteriori(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response, boolean insertMode, boolean completaMode, Long idProcesso) throws BusinessException {
		if (completaMode) {
			insertExtDomandaBuoni(user, docRiconoscimento, response);
		}
	}
	
	private void insertExtDomandaBuoni(UserInfo user, DocumentoRiconoscimento docRiconoscimento, IstanzaSaveResponse response) throws DAOException, BusinessException {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readValue((String)response.getIstanza().getData(), JsonNode.class);
			JsonNode dataNode = jsonNode.get("data");
		
			DomandaBuoniEntity domandaBuoni = new DomandaBuoniEntity();

		
			if (!dataNode.get("numeroCartaIdentita").asText().equals("")) {
				domandaBuoni.setTipoDocumento("carta identita");
				domandaBuoni.setNumeroDocumento(dataNode.get("numeroCartaIdentita").asText());
				//Date date = formatter.parse(domandaBuono.getDataRilascioCartaIdentita());
				domandaBuoni.setDataEmissioneDocumento(null);
			}
			else {
				if (!dataNode.get("numeroDocumentoSoggiorno").asText().equals("")) {
					domandaBuoni.setTipoDocumento("permesso soggiorno");
					domandaBuoni.setNumeroDocumento(dataNode.get("numeroDocumentoSoggiorno").asText());
					//Date date = formatter.parse(domandaBuono.getDataRilascioDocumentoSoggiorno());
					domandaBuoni.setDataEmissioneDocumento(null);
				}
			}
		
			// Dati da estrarre dall'istanza
			domandaBuoni.setNome(dataNode.get("nome")!=null?dataNode.get("nome").asText():user.getNome());
			domandaBuoni.setCognome(dataNode.get("cognome")!=null?dataNode.get("cognome").asText():user.getCognome());
			domandaBuoni.setNumComponenti(dataNode.get("numComponentiFamiglia")!=null?dataNode.get("numComponentiFamiglia").asInt():null);
			domandaBuoni.setTelefono(dataNode.get("telefonoFisso1")!=null?dataNode.get("telefonoFisso1").asText():null);
			domandaBuoni.setCellulare(dataNode.get("cellulare")!=null?dataNode.get("cellulare").asText():null);
			domandaBuoni.setEmail(dataNode.get("email")!=null?dataNode.get("email").asText():null);

		
			domandaBuoni.setChkRedditoCittadinanza(dataNode.get("nessunComponenteTitolareReddito").asBoolean()?"N":"S");
			domandaBuoni.setChkAltriSostegni(dataNode.get("nucleoBeneficiaSostegni").asBoolean()?"S":"N");
			domandaBuoni.setFlagVerificatoAnpr((dataNode.get("dataNascita")==null||StringUtils.isEmpty(dataNode.get("dataNascita").asText()))?"N":"S");
//			domandaBuoni.setFlagMailInviata(flagMailInviata);
//			domandaBuoni.setFlagControlliEseguiti(flagControlliEseguiti);
//			domandaBuoni.setEsitoControlli(esitoControlli);
//			"diEssereResidenteATorino": true,
//			"componenteTitolareReddito": false,
//			"nucleoInStatoDiNecessita": true,
			
			domandaBuoni.setIdIstanza(response.getIstanza().getIdIstanza());
			domandaBuoni.setCodiceIstanza(response.getIstanza().getCodiceIstanza());
			domandaBuoni.setCodiceFiscale(response.getIstanza().getAttoreIns());
			domandaBuoni.setDataInsDomanda(new Date());
			domandaBuoni.setPeriodo("P1");
		
			Integer idExtDomanda = domandaBuoniDAO.insert(domandaBuoni);
			domandaBuoni.setIdExtDomanda(idExtDomanda);
			
		} catch (JsonParseException e) {
			LOG.error("[" + CLASS_NAME + "::insertExtDomandaBuoni] Errore objectMapper.readValue - ",e );
			throw new BusinessException("JsonParseException");
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::insertExtDomandaBuoni] Errore objectMapper.readValue - ",e );
			throw new BusinessException("IOException");
		}
	}
	
	
	private void controlliPreSave(ModuloEntity moduloE, HttpServletRequest req) throws BusinessException, DAOException {
		switch (moduloE.getCodiceModulo()) {
			case Constants.CODICE_MODULO_BUONO_SPESA_COVID19:
				controllaNessunIstanzePresente(req);
				break;
			default:
				break;
		}
	}

	/**
	 * ControlliUlteriori per CODICE_MODULO_BUONO_SPESA_COVID19
	 * 
	 * @param moduloE
	 * @param componentiFamiglia
	 * @throws BusinessException
	 * @throws DAOException
	 */
	private void controllaNessunIstanzePresente(HttpServletRequest req) throws BusinessException, DAOException {
//		String joinedCodiceFiscali = (String) req.getSession().getAttribute(Constants.SESSION_JOINED_CODICE_FISCALI);
//		Integer cnt = domandaBuoniDAO.countByCodiceFiscali(joinedCodiceFiscali);
//		if (cnt > 0) {
//			throw new BusinessException(LoginBusinessException.ErrType.DOMANDA_GIA_PRESENTATA);
//		}
	}

	
}
