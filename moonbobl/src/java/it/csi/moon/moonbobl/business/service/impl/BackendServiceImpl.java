/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.impl.dao.DummyDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteAreaRuoloFlatDTO;
import it.csi.moon.moonbobl.business.service.impl.dto.EnteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.AreaRuolo;
import it.csi.moon.moonbobl.dto.moonfobl.BuildInfo;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.UserChangeRequest;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.JwtIdentitaUtil;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;



@Component
@Qualifier("provaprovaprova")
public class BackendServiceImpl implements BackendService {
	
	private final static String CLASS_NAME = "BackendServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	DummyDAO dummyDAO;
	@Autowired
	RuoloDAO ruoloDAO;
	@Autowired
	AuditService auditService;
	@Autowired
	EnteDAO enteDAO;
	@Autowired
    JwtIdentitaUtil jwtTokenUtil;
	@Autowired
	UtenteDAO utenteDAO;
	
	public String getMessage(){return dummyDAO.pingDB();}

	public it.csi.moon.moonbobl.dto.moonfobl.UserInfo getCurrentUser(HttpServletRequest req) {
		log.debug("[" + CLASS_NAME + "::getCurrentUser] IN "+req);
		return (it.csi.moon.moonbobl.dto.moonfobl.UserInfo)req.getSession().getAttribute(Constants.SESSION_USERINFO);
	}
	
	@Override
	/*public UserInfo aggiornaCurrentUser(HttpServletRequest request, UserChangeRequest userChangeRequest) {
		if (log.isDebugEnabled() ) {
			log.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nrequest=" + request + "\nuserChangeRequest=" + userChangeRequest);
		}
		EnteEntity enteE = enteDAO.findById(userChangeRequest.getEnte().getIdEnte());
		UserInfo result = getCurrentUser(request);
		result.setEnte(EnteMapper.buildFromEntity(enteE));
		request.getSession().setAttribute(Constants.SESSION_USERINFO, result);
		if (log.isDebugEnabled() ) {
			log.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nresult=" + result);
		}
		log.info("[" + CLASS_NAME + "::aggiornaCurrentUser] utente aggiornato : "+ result);
		return result;
	}*/
	public UserInfo aggiornaCurrentUser(HttpServletRequest httpRequest, UserChangeRequest userChangeRequest) {
		if (log.isDebugEnabled() ) {
			log.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nhttpRequest=" + httpRequest + "\nuserChangeRequest=" + userChangeRequest);
		}

		try {
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String oldIdMoonToken = httpRequestUtils.getMoonToken(httpRequest); //, devmode);
			String portalName = httpRequestUtils.getPortalName(httpRequest, false);
			UserInfo userInfo = jwtTokenUtil.retreiveUserInfoFromToken(oldIdMoonToken);
			UtenteEntity utenteEntity = utenteDAO.findByIdentificativoUtente(userInfo.getIdentificativoUtente());
			// Codice simile a quello presente alla login in IrideIdAdapterFilter
			EnteEntity enteE = enteDAO.findById(userChangeRequest.getEnte().getIdEnte());
			userInfo.setEnte(EnteMapper.buildFromEntity(enteE));
			List<EnteAreeRuoli> entiAreeRuoli = getEntiAreeRuoliAttivi(utenteEntity.getIdUtente(), portalName);
			if ((entiAreeRuoli==null || entiAreeRuoli.isEmpty()) && !DecodificaTipoUtente.ADM.isCorrectType(utenteEntity)) {
				log.error("[" + CLASS_NAME + "::buildUserInfo] EntiAreaRuoliAttivi vuoto per identitaJwt.getCodFisc()=" + utenteEntity.getIdentificativoUtente() + "  idUtente="+utenteEntity.getIdUtente());
				//response.sendError(HttpServletResponse.SC_FORBIDDEN, "NoRolesForUserNonADM");
				throw new UnauthorizedException("NoRolesForUserNonADM");
			}
			userInfo.setEntiAreeRuoli(entiAreeRuoli);
			String newIdMoonToken = jwtTokenUtil.writeIdEnte(oldIdMoonToken,userChangeRequest.getEnte().getIdEnte());
			userInfo.setIdMoonToken(newIdMoonToken);
			if (log.isDebugEnabled() ) {
				log.debug("[" + CLASS_NAME + "::aggiornaCurrentUser] \nresult=" + userInfo);
			}
			return userInfo;
		} catch (Exception e) {
			throw new BusinessException();
		}
	}

	@Override
	public String localLogout(HttpServletRequest request) {
		UserInfo user = getCurrentUser(request);
		String portalName = (String)request.getSession().getAttribute(Constants.SESSION_PORTALNAME);
		//
		request.getSession().invalidate();
		log.debug("[" + CLASS_NAME + "::localLogout] Sessione invalidata !!!");
		// Ricerca della response : URL di logout del portale
		String result = EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+portalName);
		
		// audit login
		auditService.insertAuditLogout(request.getRemoteAddr(), user, result);
		
		return result;
	}
	
	@Override
	public List<EnteAreeRuoli> getEntiAreeRuoliAttivi(Long idUtente, String portalName) throws BusinessException {
		try {
			log.info("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] IN idUtente = " + idUtente);
			log.info("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] IN portalName = " + portalName);
			List<EnteAreeRuoli> result = new ArrayList<EnteAreeRuoli>();
			List<EnteAreaRuoloFlatDTO> entiAreeRuoliFlat = ruoloDAO.findEntiAreaRuoliAttiviByIdUtentePortale(idUtente, portalName);
			log.debug("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] entiAreeRuoliFlat.size() = " + ((entiAreeRuoliFlat!=null)?entiAreeRuoliFlat.size():null));
			if (entiAreeRuoliFlat!=null && !entiAreeRuoliFlat.isEmpty()) {
				EnteAreeRuoli curEAR = null;
				for (EnteAreaRuoloFlatDTO flat : entiAreeRuoliFlat) {
					if (primoEnteOrSwitchEnte(curEAR, flat)) {
						if (curEAR!=null) result.add(curEAR);
						curEAR = new EnteAreeRuoli();
						curEAR.setIdEnte(flat.getIdEnte());
						curEAR.setCodiceEnte(flat.getCodiceEnte());
						curEAR.setNomeEnte(flat.getNomeEnte());
						curEAR.setDescrizioneEnte(flat.getDescrizioneEnte());
						curEAR.setIdTipoEnte(flat.getIdTipoEnte());
						curEAR.setAreeRuoli(new ArrayList<AreaRuolo>());
					}
					//
					AreaRuolo ar = new AreaRuolo();
					ar.setIdArea(flat.getIdArea());
					ar.setCodiceArea(flat.getCodiceArea());
					ar.setNomeArea(flat.getNomeArea());
					//
					ar.setIdRuolo(flat.getIdRuolo());
					ar.setCodiceRuolo(flat.getCodiceRuolo());
					ar.setNomeRuolo(flat.getNomeRuolo());
					curEAR.getAreeRuoli().add(ar);
				}
				if (curEAR!=null) result.add(curEAR);
			}
			log.info("[" + CLASS_NAME + "::getEntiAreeRuoliAttivi] OUT result = " + result);
			return result;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getEntiAreeRuoliAttivi]  Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca EntiAreeRuoli");
		}
	}
	
	@Override
	public List<EnteAreeRuoli> getEntiAreeRuoliAttivi(String identificativoUtente, Long idEnte) throws BusinessException {
		try {
			log.info("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] IN identificativoUtente = " + identificativoUtente);
			log.info("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] IN idEnte = " + idEnte);
			List<EnteAreeRuoli> result = new ArrayList<EnteAreeRuoli>();
			List<EnteAreaRuoloFlatDTO> entiAreeRuoliFlat = ruoloDAO.findEntiAreaRuoliAttiviByUtenteIdEnte(identificativoUtente, idEnte);
			log.debug("["+CLASS_NAME+"::getEntiAreeRuoliAttivi] entiAreeRuoliFlat.size() = " + ((entiAreeRuoliFlat!=null)?entiAreeRuoliFlat.size():null));
			if (entiAreeRuoliFlat!=null && !entiAreeRuoliFlat.isEmpty()) {
				EnteAreeRuoli curEAR = null;
				for (EnteAreaRuoloFlatDTO flat : entiAreeRuoliFlat) {
					if (primoEnteOrSwitchEnte(curEAR, flat)) {
						if (curEAR!=null) result.add(curEAR);
						curEAR = new EnteAreeRuoli();
						curEAR.setIdEnte(flat.getIdEnte());
						curEAR.setCodiceEnte(flat.getCodiceEnte());
						curEAR.setNomeEnte(flat.getNomeEnte());
						curEAR.setDescrizioneEnte(flat.getDescrizioneEnte());
						curEAR.setIdTipoEnte(flat.getIdTipoEnte());
						curEAR.setAreeRuoli(new ArrayList<AreaRuolo>());
					}
					//
					AreaRuolo ar = new AreaRuolo();
					ar.setIdArea(flat.getIdArea());
					ar.setCodiceArea(flat.getCodiceArea());
					ar.setNomeArea(flat.getNomeArea());
					//
					ar.setIdRuolo(flat.getIdRuolo());
					ar.setCodiceRuolo(flat.getCodiceRuolo());
					ar.setNomeRuolo(flat.getNomeRuolo());
					curEAR.getAreeRuoli().add(ar);
				}
				if (curEAR!=null) result.add(curEAR);
			}
			log.info("[" + CLASS_NAME + "::getEntiAreeRuoliAttivi] OUT result = " + result);
			return result;
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getEntiAreeRuoliAttivi]  Errore invocazione DAO - ", e);
			throw new BusinessException("Errore ricerca EntiAreeRuoli");
		}
	}
	
	private boolean primoEnteOrSwitchEnte(EnteAreeRuoli curEAR, EnteAreaRuoloFlatDTO flat) {
		return curEAR==null || !curEAR.getIdEnte().equals(flat.getIdEnte());
	}

	@Override
	public String getVersion() throws BusinessException {
		return EnvProperties.readFromFile(EnvProperties.VERSION);
	}
	@Override
	public BuildInfo getBuildInfo() throws BusinessException {
		return new BuildInfo(EnvProperties.readFromFile(EnvProperties.VERSION),EnvProperties.readFromFile(EnvProperties.BUILD_TIME));
	}
}
