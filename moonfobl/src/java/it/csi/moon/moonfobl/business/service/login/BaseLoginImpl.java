/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.EnteAreeRuoli;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.EnteAreaRuoloFlatDTO;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.EntiFilter;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.commons.mapper.EnteMapper;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaPortale;
import it.csi.moon.commons.util.decodifica.DecodificaRuolo;
import it.csi.moon.moonfobl.business.service.ModuliService;
import it.csi.moon.moonfobl.business.service.helper.JwtIdentitaUtil;
import it.csi.moon.moonfobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.LogonModeDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonfobl.business.service.impl.dto.LogonModeEntity;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.EnvProperties;
import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public abstract class BaseLoginImpl {

	private static final String CLASS_NAME = "BaseLoginImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	protected HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
	
    @Autowired
    JwtIdentitaUtil jwtTokenUtil;
    @Autowired
    EnteDAO enteDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	RuoloDAO ruoloDAO;
    @Autowired
    ModuliService moduliService;
    @Autowired
    MoonsrvDAO moonsrvDAO;
    @Autowired
    LogonModeDAO logonModeDAO;

	protected boolean devmode = false;
	protected boolean tstmodeRemapIdentita = false;

	public BaseLoginImpl() {
		super();
		//must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        devmode = "true".equals(EnvProperties.readFromFile(EnvProperties.DEV_MODE))?true:false;
//        tstmodeRemapIdentita = "true".equals(EnvProperties.readFromFile(EnvProperties.TSTMODE_REMAPIDENTITA))?true:false;
	}

	protected void logDebugJwtToken(String idMoonToken) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::login] ========================================== ");
			LOG.debug("[" + CLASS_NAME + "::login] getCodFiscFromToken        = " + jwtTokenUtil.getCodFiscFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getCognomeFromToken        = " + jwtTokenUtil.getCognomeFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getIdModuloFromToken       = " + jwtTokenUtil.getIdModuloFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getIdEnteFromToken         = " + jwtTokenUtil.getIdEnteFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getCodiceFiscaleFromToken  = " + jwtTokenUtil.getCodiceFiscaleFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getCreatedDateFromToken    = " + jwtTokenUtil.getCreatedDateFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getExpirationDateFromToken = " + jwtTokenUtil.getExpirationDateFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] ------------------------------------------ ");
			LOG.debug("[" + CLASS_NAME + "::login] getLogonModeFromToken             = " + jwtTokenUtil.getLogonModeFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getTipoDocumentoFromToken         = " + jwtTokenUtil.getTipoDocumentoFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getNumeroDocumentoFromToken       = " + jwtTokenUtil.getNumeroDocumentoFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getDataRilascioDocumentoFromToken = " + jwtTokenUtil.getDataRilascioDocumentoFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] getIdFamigliaConvivenzaANPRFromToken = " + jwtTokenUtil.getIdFamigliaConvivenzaANPRFromToken(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] ========================================== ");
			LOG.debug("[" + CLASS_NAME + "::login] idMoonToken:\n"+jwtTokenUtil.toString(idMoonToken));
			LOG.debug("[" + CLASS_NAME + "::login] ========================================== ");
		}
	}
	
	protected LoginResponse completaLoginResponseModulo(LoginResponse result, LoginRequest loginRequest) throws BusinessException {
		if (result==null) return null;
		result.setModulo(StrUtils.isEmpty(loginRequest.getCodiceModulo())?null:moduliService.getModuloPubblicatoByCodice(loginRequest.getCodiceModulo()));
		return result;
	}
	protected LoginResponse completaLoginResponseModuloNoContoTerziIfRequested(LoginResponse result, LoginRequest loginRequest) throws BusinessException {
		if (result==null) return null;
		try {
			if (StringUtils.isNotBlank(loginRequest.getCodiceModulo())) {
				Modulo modulo = moduliService.getModuloPubblicatoByCodice(loginRequest.getCodiceModulo());
				LOG.debug("[" + CLASS_NAME + "::completaLoginResponseModuloNoContoTerziIfRequested] PRIMA :: " + modulo.getAttributi());
				modulo.setAttributi(modulo.getAttributi()==null?null:modulo.getAttributi().stream().filter(a -> !"CONTO_TERZI".equalsIgnoreCase(a.getNome())).collect(Collectors.toList()) );
				LOG.debug("[" + CLASS_NAME + "::completaLoginResponseModuloNoContoTerziIfRequested] DOPO :: " + modulo.getAttributi());
				result.setModulo(modulo);
			}
			return result;
		} catch (Exception e) {
			LOG.debug("[" + CLASS_NAME + "::completaLoginResponseModuloNoContoTerziIfRequested] Exception for " + loginRequest.getCodiceModulo());
			throw new BusinessException();
		}
	}

	// controllo la modalità di login per modulo/portale
	protected void checkLogin(String nomePortale, String logonType, LoginRequest loginRequest) throws BusinessException {

		LogonModeEntity logonMode;
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::checkLogin] IN " + nomePortale + " ; " + logonType + " ; " + loginRequest);
			}
			logonMode = logonModeDAO.findLogonMode(loginRequest.getCodiceModulo(), nomePortale);

		} catch (DAOException e) {
			throw e;
		}

		if (!logonMode.getLogonMode().equals(logonType)) {
			throw new BusinessException("Modulo non pubblicato su Portale" + nomePortale);
		}

	}
			
	
	protected boolean isPortaleMultiEnte(String portalName) {
		Ente result = null; //new Ente();
		LOG.debug("[" + CLASS_NAME + "::isPortaleMultiEnte] IN portalName " + portalName);
		DecodificaPortale portale = DecodificaPortale.byNomePortale((portalName.equals("localhost")) ? "*" : portalName);
		return portale.getIdEnte()<0?true:false;
	}
	
	/**
	 * Recupera l'ente corrente, che verra usato nel salvataggio dell'istanza (non corrisponde all'ente dell'utente)
	 * Attualmente i portali sono mono ente, la relazione e codificata in DecodificaPortale
	 * @param portalName
	 * @return
	 */
	protected Ente retrieveEnte(String portalName, String codiceEnteDiretto) throws BusinessException {
		Ente result = null; //new Ente();
		LOG.debug("[" + CLASS_NAME + "::retrieveEnte] IN portalName " + portalName + " codiceEnteDiretto=" + codiceEnteDiretto );
		DecodificaPortale portale = DecodificaPortale.byNomePortale((portalName.equals("localhost")) ? "*" : portalName);
		if (portale == null) {
			LOG.error("[" + CLASS_NAME + "::retrieveEnte] portalName sconosciuto : " + portalName);
			throw new BusinessException("portalName sconosciuto");
		}
		LOG.debug("[" + CLASS_NAME + "::retrieveEnte] portale " + portale );
		Long idEnte = portale.getIdEnte();
		if (idEnte>0) {
			try {
				result = EnteMapper.buildFromEntity(enteDAO.findById(idEnte));
			} catch (DAOException e) {
				LOG.error("[" + CLASS_NAME + "::retrieveEnte] DAOException enteDAO.findById " + idEnte, e );
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
						LOG.error("[" + CLASS_NAME + "::retrieveEnte] EnteAccessoDiretto " + codiceEnteDiretto + " non trovato attivo per il portale " + portalName);
						return null;
					}
					result = EnteMapper.buildFromEntity(enti.get(0));
				} catch (DAOException e) {
					LOG.error("[" + CLASS_NAME + "::retrieveEnte] DAOException enteDAO.findById " + idEnte, e );
//					throw e;
				}
			}
		}
		return result;
	}

	
	protected UserInfo completaUserInfoOfIsOperatore(UserInfo userInfo, LoginRequest loginRequest,
			HttpServletRequest httpRequest) {
		String gruppoOperatoreReqParam = httpRequestUtils.getGruppoOperatoreRequestParam(httpRequest);
		LOG.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] gruppoOperatoreReqParam:" + gruppoOperatoreReqParam);
		if (gruppoOperatoreReqParam != null) {
			String gruppoOperatoreFo = getGruppoOperatoreFoByCodiceArea(userInfo, gruppoOperatoreReqParam);
			if (gruppoOperatoreFo == null) {
				LOG.error("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] MOONFOBL-00015 - Tentativo di accesso ad un area non abilitato from " + 
					httpRequest.getRemoteAddr() + " for " + httpRequest.getQueryString() + " gop:" + gruppoOperatoreReqParam);
				throw new UnauthorizedException ("Tentativo di accesso ad un area non abilitato", "MOONFOBL-00015");
			}
			userInfo.setOperatore(true);
			userInfo.setGruppoOperatoreFo(gruppoOperatoreFo);
//			userInfo.setCodFiscDichIstanza("e:"+flat.getIdEnte()+";a:"+flat.getIdArea()); // FORSE MEGLIO un HASH (magari dei codici Ente e Area) ?
//			userInfo.setCognomeDich(flat.getNomeEnte());
//			userInfo.setNomeDich(""); // String vuota per azzerare il nome dell'utente sull'utanza
		}
		LOG.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] OUT userInfo:" + userInfo);
		return userInfo;
	}


	protected String getGruppoOperatoreFoByCodiceArea(UserInfo userInfo, String codiceArea) {
		String result = null;
		UtenteEntity utente = null;
		try {
			utente = utenteDAO.findByIdentificativoUtente(userInfo.getIdentificativoUtente());
			List<EnteAreaRuoloFlatDTO> entiAreeRuoliFlat = ruoloDAO.findEntiAreaRuoliAttiviByIdUtente(utente.getIdUtente());
			LOG.debug("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] entiAreeRuoliFlat.size() = " + entiAreeRuoliFlat.size());
			if (entiAreeRuoliFlat!=null && !entiAreeRuoliFlat.isEmpty()) {
				EnteAreeRuoli curEAR = null;
				for (EnteAreaRuoloFlatDTO flat : entiAreeRuoliFlat) {
					// Attualmente su DecodificaPortale.COTO_EXTRACOM_10 filtro per Ente CAF
//						if (DecodificaTipoEnte.CAF.isCorrectType(flat) && DecodificaRuolo.FO_SIMP.isCorrectRuolo(flat)) {
					if (codiceArea.equalsIgnoreCase(flat.getCodiceArea()) && DecodificaRuolo.FO_SIMP.isCorrectRuoloByCodice(flat)) {
						return "e:"+flat.getIdEnte()+",a:"+flat.getIdArea();
//							userInfo.setOperatore(true);
//							userInfo.setGruppoOperatoreFo("e:"+flat.getIdEnte()+";a:"+flat.getIdArea());
////							userInfo.setCodFiscDichIstanza("e:"+flat.getIdEnte()+";a:"+flat.getIdArea()); // FORSE MEGLIO un HASH (magari dei codici Ente e Area) ?
////							userInfo.setCognomeDich(flat.getNomeEnte());
//							userInfo.setNomeDich(""); // String vuota per azzerare il nome dell'utente sull'utanza
					}
				}
				LOG.warn("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] Nessun EnteAreaRuolo FO_SIMP su area: " + codiceArea + " per CF:"+utente.getIdentificativoUtente()+ "[" + utente.getIdUtente() + "].");
			} else {
				LOG.warn("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] Nessun EnteAreaRuolo trovato per CF:"+utente.getIdentificativoUtente()+" (dal Portale COTO_EXTRACOM).");
			}
		} catch (ItemNotFoundDAOException e) {
			LOG.warn("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] ItemNotFoundDAOException utente non trovato codiceFiscale=" + userInfo.getIdentificativoUtente());
		} catch (DAOException e) {
			LOG.warn("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] DAOException findByIdentificativoUtente codiceFiscale=" + userInfo.getIdentificativoUtente());
		}
		return result;
	}

}
