/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.login;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.business.service.mapper.TipoUtenteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.AreaRuolo;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.IrideIdentitaDigitale;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;





@Component
public class LoginIdpShibbolethImpl extends BaseLoginImpl implements Login {

	private final static String CLASS_NAME = "LoginIdpShibbolethImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	private static final Set<String> ALLOWED_ROLES = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("ADMIN", "RESP", "OP_ADM", "OP_ADV", "OP_SIMP", "OP_CON", "OP_BLDR", "OP_COMP", "OP_SIMPMOD")));

	@Autowired
	AuditService auditService;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	RuoloDAO ruoloDAO;
	//@Autowired
	//AmbitoDAO ambitoDAO;
	@Autowired
	BackendService backendService;
	
	@Override
	public LoginResponse login(LoginRequest loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		try {
			log.info("[" + CLASS_NAME + "::login] BEGIN");
			log.info("[" + CLASS_NAME + "::login] LoginRequest " + loginRequest);
			validaLoginRequest(loginRequest);
			
			// login
			LoginResponse result = _login(loginRequest, httpRequest);
			result.setDatiAggiuntivi(makeDatiAggiuntivi(httpHeaders));
			
			// IdMoonToken
			final String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
	
			return result;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::login] BusinessException ", be);
			throw be;
	//		throw new BusinessException("NON RISULTI RESIDENTE A TORINO, SE DI RECENTE HAI FATTO UNA PRATICA DI IMMIGRAZIONE RIVOLGITI AL CALL CENTER");
		} catch (JsonProcessingException e) {
			log.error("[" + CLASS_NAME + "::login] JsonProcessingException - Errore generazione JWT - ", e);
			throw new BusinessException("Errore accesso");
		}
	}
	
	private void validaLoginRequest(LoginRequest loginRequest) throws BusinessException {
		Set<String> validationErrors = new HashSet<String>();
		if (StringUtils.isEmpty(loginRequest.getNomePortale())) {
			validationErrors.add("nomePortale");
		}
		if (StringUtils.isEmpty(loginRequest.getProvider())) {
			validationErrors.add("provider");
		}
		if (!validationErrors.isEmpty()) {
			log.error("[" + CLASS_NAME + "::validaLoginRequest] Elementi obbligatori mancanti nell'oggetto LoginRequest: " + String.join(", ", validationErrors));
			throw new BusinessException("Invalid LoginRequest");
		}
	}
	
	private LoginResponse _login(LoginRequest loginRequest,  HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		LoginResponse result = null;
//		COTO_EXTRACOM_10 moon-extracom.patrim.csi.it
			
//	    checkLogin(nomePortale, LogonModeEnum.IDP_SHIBBOLETH.name(), loginRequest);	
						
		result = new LoginResponse(_login_GIA_AUTH(loginRequest,  httpRequest));
//		result.setProvider(loginRequest.getProvider());
		String urlLogoutKey = EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider() ;
		log.debug("[" + CLASS_NAME + "::urlLogoutKey ] " + urlLogoutKey);
		
		result.setUrlLogout(EnvProperties.readFromFile(urlLogoutKey));
		result.setIdIride(httpRequestUtils.getShibToken(httpRequest, devmode));
//		if (DecodificaPortale.COTO_EXTRACOM_10.hasNomePortale(nomePortale)||DecodificaPortale.TOFACILE_BO_3.hasNomePortale(nomePortale)
//				|| DecodificaPortale.TOFACILE_BO_TEST_9.hasNomePortale(nomePortale)) {
//			// COTO          TO_SOLIDALE
//			result = new LoginResponse(_login_COTO(loginRequest, nomePortale, httpServletRequest));
//			if (!Constants.CODICE_MODULO_TO_SOLIDALE.equals(loginRequest.getCodiceModulo())) {
//				log.error("[" + CLASS_NAME + "::_login] BusinessException : Modulo non pubblicato sul Portale COTO_EXTRACOM_10 || TOFACILE_BO_3 || TOFACILE_BO_TEST_9: "+loginRequest.getCodiceModulo());
//				throw new BusinessException("Modulo non pubblicato sul Portale");
//			}
//		}

		result = completaLoginResponseModulo(result, loginRequest); // QUI con contoTerzi
		log.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}
	
	private UserInfo _login_GIA_AUTH(LoginRequest loginRequest, HttpServletRequest httpRequest) throws BusinessException, UnauthorizedException {
		try {
			String marker = httpRequestUtils.getShibToken(httpRequest, devmode);
			if (marker == null) {
				// il marcatore deve sempre essere presente altrimenti e' una 
				// condizione di errore (escluse le pagine home e di servizio)
				log.warn(
					"[" + CLASS_NAME + "::_login_GIA_AUTH] MOONBOBL - Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza from " + httpRequest.getRemoteAddr() + " for " + httpRequest.getRequestURI());
	            throw new UnauthorizedException (
					"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza", "MOONBOBL");
			}
			
			IrideIdentitaDigitale identita = new IrideIdentitaDigitale(marker);
			log.debug("[" + CLASS_NAME + "::_login_GIA_AUTH] Inserito in sessione marcatore IRIDE:" + identita);
			
			UserInfo userInfo = buildUserInfo(identita, loginRequest.getNomePortale(), httpRequest, httpRequestUtils.retrieveMail(httpRequest));
//			userInfo = completaUserInfoOfIsOperatore(userInfo, loginRequest, httpRequest);
//			validaPresenzaJwtIfRequired(userInfo);

			return userInfo;
		} catch (Exception e) { // MalformedIdTokenException e) {
			log.error("[" + CLASS_NAME + "::_login_GIA_AUTH] " + e.toString(), e);
			throw new BusinessException();
		}
	}

	private UserInfo buildUserInfo(IrideIdentitaDigitale identita, String portalName, HttpServletRequest httpRequest, String mail) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::buildUserInfo] IN identita:" + identita);
			log.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + portalName);
		}
		// Remap User DEMO con User reali per Tests su casi reali
		log.debug("[" + CLASS_NAME + "::buildUserInfo] userInfo from identita..." );
		
		UtenteEntity utente = null;
		try {
			utente = utenteDAO.findByIdentificativoUtente(identita.getCodFiscale());
		} catch (ItemNotFoundDAOException e) {
			log.error("[" + CLASS_NAME + "::buildUserInfo] IN ItemNotFoundDAOException utente non trovato identita.getCodFisc()=" + identita.getCodFiscale());
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "UnknownUser");
			throw new UnauthorizedException("UnknownUser");
		}
		
		UserInfo userInfo = new UserInfo();
		userInfo.setIdentificativoUtente(identita.getCodFiscale());
		userInfo.setNome(identita.getNome());
		userInfo.setCognome(identita.getCognome());
//		userInfo.setCodFiscDichIstanza(identita.getCodFiscale()); NotUserInfoBO
		userInfo.setMail(mail);
		//
		List<EnteAreeRuoli> entiAreeRuoli = backendService.getEntiAreeRuoliAttivi(utente.getIdUtente(), portalName);
		if ((entiAreeRuoli==null || entiAreeRuoli.isEmpty()) && !DecodificaTipoUtente.ADM.isCorrectType(utente)) {
			log.error("[" + CLASS_NAME + "::buildUserInfo] EntiAreaRuoliAttivi vuoto per identitaJwt.getCodFisc()=" + utente.getIdentificativoUtente() + "  idUtente="+utente.getIdUtente());
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "NoRolesForUserNonADM");
			throw new UnauthorizedException("NoRolesForUserNonADM");
		}
		userInfo.setEntiAreeRuoli(entiAreeRuoli);
		// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
		Ente ente = retrieveEnte(portalName, httpRequestUtils.getCodiceEnteRequestParam(httpRequest)); // Enti del portale
		if(ente == null) {
			ente = retrieveUnicoEnte(entiAreeRuoli);
		}
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(portalName));
		userInfo.setPortalName(portalName);
		userInfo.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(utente.getIdTipoUtente()));
		userInfo.setIdIride(identita.toString());
	
		// Valida accesso al BO
		if (!(DecodificaTipoUtente.ADM.isCorrectType(utente) || mantainEnteIfContainsRuoloRESPorOPERATORE(userInfo))) {
			log.error("[" + CLASS_NAME + "::buildUserInfo] LoginException for "+userInfo);
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "LoginException");
			throw new UnauthorizedException("LoginException");
		}
		
		//
		String jwt = httpRequestUtils.retrieveJWT(httpRequest, devmode);
		userInfo.setJwt(jwt);
		return userInfo;
	}
	
	private Ente retrieveUnicoEnte(List<EnteAreeRuoli> entiAreeRuoli) {
		Ente result = null;
		List<Long> idEnti= entiAreeRuoli.stream()
			.mapToLong(ear -> ear.getIdEnte())
			.distinct()
			.boxed()
			.collect(Collectors.toList());
		if(idEnti.size() == 1) {
			result = EnteMapper.buildFromEntity(enteDAO.findById(idEnti.get(0)));
		}
		return result;
	}
	
	/**
	 * Ritorna true se la lista contiene un ruolo di tipo OP_* or RESP
	 * Eliminando gli ente dove non c'è 1 ruolo OP_* or RESP
	 * @param result
	 * @return
	 */
	private boolean mantainEnteIfContainsRuoloRESPorOPERATORE(UserInfo result) {
		log.debug(result);
		if (result==null || result.getEntiAreeRuoli()==null || result.getEntiAreeRuoli().isEmpty()) return false;
		Iterator<EnteAreeRuoli> i = result.getEntiAreeRuoli().iterator();
		while (i.hasNext()) {
			EnteAreeRuoli e = i.next(); // must be called before you can call i.remove()
			boolean enteContainAllowedRole = false;
			for (AreaRuolo ar : e.getAreeRuoli()) {
				if (ALLOWED_ROLES.contains(ar.getCodiceRuolo())) {
					enteContainAllowedRole = true;
				}
			}
			if (!enteContainAllowedRole) {
				i.remove();
			}
		}
		return !result.getEntiAreeRuoli().isEmpty();
	}
	
	/*
	private UserInfo completaUserInfoOfIsOperatore(UserInfo userInfo, LoginRequest loginRequest,
			HttpServletRequest httpRequest) {
		String gruppoOperatoreReqParam = httpRequestUtils.getGruppoOperatoreRequestParam(httpRequest);
		log.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] gruppoOperatoreReqParam:" + gruppoOperatoreReqParam);
		if (gruppoOperatoreReqParam != null) {
			String gruppoOperatoreFo = getGruppoOperatoreFoByCodiceArea(userInfo, gruppoOperatoreReqParam);
			if (gruppoOperatoreFo != null) {
				userInfo.setOperatore(true);
				userInfo.setGruppoOperatoreFo(gruppoOperatoreFo);
//				userInfo.setCodFiscDichIstanza("e:"+flat.getIdEnte()+";a:"+flat.getIdArea()); // FORSE MEGLIO un HASH (magari dei codici Ente e Area) ?
//				userInfo.setCognomeDich(flat.getNomeEnte());
//				userInfo.setNomeDich(""); // String vuota per azzerare il nome dell'utente sull'utanza
			}
		}
		log.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] OUT userInfo:" + userInfo);
		return userInfo;
	}
	
	private String getGruppoOperatoreFoByCodiceArea(UserInfo userInfo, String codiceArea) {
		String result = null;
		UtenteEntity utente = null;
		try {
			utente = utenteDAO.findByIdentificativoUtente(userInfo.getIdentificativoUtente());
			List<EnteAreaRuoloFlatDTO> entiAreeRuoliFlat = ruoloDAO.findEntiAreaRuoliAttiviByIdUtente(utente.getIdUtente());
			log.debug("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] entiAreeRuoliFlat.size() = "+entiAreeRuoliFlat!=null?entiAreeRuoliFlat.size():null);
			if (entiAreeRuoliFlat!=null && !entiAreeRuoliFlat.isEmpty()) {
				EnteAreeRuoli curEAR = null;
				for (EnteAreaRuoloFlatDTO flat : entiAreeRuoliFlat) {
					// Attualmente su DecodificaPortale.COTO_EXTRACOM_10 filtro per Ente CAF
//						if (DecodificaTipoEnte.CAF.isCorrectType(flat) && DecodificaRuolo.FO_SIMP.isCorrectRuolo(flat)) {
					if (codiceArea.equalsIgnoreCase(flat.getCodiceArea()) && DecodificaRuolo.FO_SIMP.isCorrectRuolo(flat)) {
						return "e:"+flat.getIdEnte()+",a:"+flat.getIdArea();
//							userInfo.setOperatore(true);
//							userInfo.setGruppoOperatoreFo("e:"+flat.getIdEnte()+";a:"+flat.getIdArea());
////							userInfo.setCodFiscDichIstanza("e:"+flat.getIdEnte()+";a:"+flat.getIdArea()); // FORSE MEGLIO un HASH (magari dei codici Ente e Area) ?
////							userInfo.setCognomeDich(flat.getNomeEnte());
//							userInfo.setNomeDich(""); // String vuota per azzerare il nome dell'utente sull'utanza
					}
				}
			} else {
				log.warn("["+CLASS_NAME+"::getGruppoOperatoreFoByCodiceArea] Nessun EnteAreaRuolo trovato per CF:"+utente.getIdentificativoUtente()+" (dal Portale COTO_EXTRACOM).");
			}
		} catch (ItemNotFoundDAOException e) {
			log.warn("[IrideIdAdapterFilter::getGruppoOperatoreFoByCodiceArea] ItemNotFoundDAOException utente non trovato codiceFiscale=" + userInfo.getIdentificativoUtente());
		} catch (DAOException e) {
			log.warn("[IrideIdAdapterFilter::getGruppoOperatoreFoByCodiceArea] DAOException findByIdentificativoUtente codiceFiscale=" + userInfo.getIdentificativoUtente());
		}
		return result;
	}
	*/
}
