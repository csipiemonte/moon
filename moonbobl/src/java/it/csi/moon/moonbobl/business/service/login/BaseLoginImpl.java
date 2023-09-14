/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.login;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EntiFilter;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntivi;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.HeadersUtils;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.JwtIdentitaUtil;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.StrUtils;
import it.csi.moon.moonbobl.util.decodifica.DecodificaPortale;





public abstract class BaseLoginImpl {
	
	private final static String CLASS_NAME = "BaseLoginImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	protected HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
	
	 @Autowired
	 ModuliService moduliService;
	 @Autowired
	 JwtIdentitaUtil jwtTokenUtil;
	 @Autowired
	 EnteDAO enteDAO;
	 
	 protected boolean devmode = false;
	 protected boolean tstmodeRemapIdentita = false;

	 public BaseLoginImpl() {
			super();
			//must provide autowiring support to inject SpringBean
	        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

	        devmode = "true".equals(EnvProperties.readFromFile(EnvProperties.DEV_MODE))?true:false;
//	        tstmodeRemapIdentita = "true".equals(EnvProperties.readFromFile(EnvProperties.TSTMODE_REMAPIDENTITA))?true:false;
		}

	protected LoginResponse completaLoginResponseModulo(LoginResponse result, LoginRequest loginRequest) throws BusinessException {
		if (result==null) return null;
		result.setModulo(StrUtils.isEmpty(loginRequest.getCodiceModulo())?null:moduliService.getModuloPubblicatoByCodice(loginRequest.getCodiceModulo()));
		return result;
	}
	
	protected void logDebugJwtToken(String idMoonToken) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::login] ========================================== ");
			log.debug("[" + CLASS_NAME + "::login] getCodFiscFromToken        = " + jwtTokenUtil.getCodFiscFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getCognomeFromToken        = " + jwtTokenUtil.getCognomeFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getIdModuloFromToken       = " + jwtTokenUtil.getIdModuloFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getIdEnteFromToken         = " + jwtTokenUtil.getIdEnteFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getCodiceFiscaleFromToken  = " + jwtTokenUtil.getCodiceFiscaleFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getCreatedDateFromToken    = " + jwtTokenUtil.getCreatedDateFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getExpirationDateFromToken = " + jwtTokenUtil.getExpirationDateFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] ------------------------------------------ ");
			log.debug("[" + CLASS_NAME + "::login] getLogonModeFromToken             = " + jwtTokenUtil.getLogonModeFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getTipoDocumentoFromToken         = " + jwtTokenUtil.getTipoDocumentoFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getNumeroDocumentoFromToken       = " + jwtTokenUtil.getNumeroDocumentoFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getDataRilascioDocumentoFromToken = " + jwtTokenUtil.getDataRilascioDocumentoFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] getIdFamigliaConvivenzaANPRFromToken = " + jwtTokenUtil.getIdFamigliaConvivenzaANPRFromToken(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] ========================================== ");
			log.debug("[" + CLASS_NAME + "::login] idMoonToken:\n"+jwtTokenUtil.toString(idMoonToken));
			log.debug("[" + CLASS_NAME + "::login] ========================================== ");
		}
	}
	
	/**
	 * Recupera l'ente corrente, che verra usato nel salvataggio dell'istanza (non corrisponde all'ente dell'utente)
	 * Attualmente i portali sono mono ente, la relazione e codificata in DecodificaPortale
	 * @param portalName
	 * @return
	 */
	protected Ente retrieveEnte(String portalName, String codiceEnteDiretto) {
		Ente result = null; //new Ente();
		log.debug("[" + CLASS_NAME + "::retrieveEnte] IN portalName " + portalName + " codiceEnteDiretto=" + codiceEnteDiretto );
		DecodificaPortale portale = DecodificaPortale.byNomePortale((portalName.equals("localhost")) ? "*" : portalName);
		if (portale == null) {
			log.error("[" + CLASS_NAME + "::retrieveEnte] portalName sconosciuto : " + portalName);
			throw new BusinessException("portalName sconosciuto");
		}
		log.debug("[" + CLASS_NAME + "::retrieveEnte] portale " + portale );
		Long idEnte = portale.getIdEnte();
		if (idEnte>0) {
			try {
				result = EnteMapper.buildFromEntity(enteDAO.findById(idEnte));
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::retrieveEnte] DAOException enteDAO.findById " + idEnte, e );
				throw e;
			}
		} else {
			if (StringUtils.isNotEmpty(codiceEnteDiretto)) {
				try {
					EntiFilter filter = new EntiFilter();
					filter.setCodiceEnte(codiceEnteDiretto);
					filter.setFlAttivo("S");
					filter.setNomePortale(portalName);
					List<EnteEntity> enti = enteDAO.find(filter);
					if (enti==null || enti.size()==0) {
						log.error("[" + CLASS_NAME + "::retrieveEnte] EnteAccessoDiretto " + codiceEnteDiretto + " non trovato attivo per il portale " + portalName);
						return null;
					}
					result = EnteMapper.buildFromEntity(enti.get(0));
				} catch (DAOException e) {
					log.error("[" + CLASS_NAME + "::retrieveEnte] DAOException enteDAO.findById " + idEnte, e );
//					throw e;
				}
			}
		}
		return result;
	}
	
	protected boolean isPortaleMultiEnte(String portalName) {
		Ente result = null; //new Ente();
		log.debug("[" + CLASS_NAME + "::isPortaleMultiEnte] IN portalName " + portalName);
		DecodificaPortale portale = DecodificaPortale.byNomePortale((portalName.equals("localhost")) ? "*" : portalName);
		return portale.getIdEnte()<0?true:false;
	}
	
	protected DatiAggiuntivi makeDatiAggiuntivi(HttpHeaders httpHeaders) {
		return makeDatiAggiuntivi(HeadersUtils.readFromHeaders(httpHeaders));
	}
	private DatiAggiuntivi makeDatiAggiuntivi(DatiAggiuntiviHeaders daHeaders) {
		DatiAggiuntivi datiAggiuntivi = new DatiAggiuntivi();
		datiAggiuntivi.setDataOraLogin(new Date());
		datiAggiuntivi.setProvider(daHeaders.getShibIdentitaProvider());
		datiAggiuntivi.setHeaders(daHeaders);
		return datiAggiuntivi;
	}
	
}
