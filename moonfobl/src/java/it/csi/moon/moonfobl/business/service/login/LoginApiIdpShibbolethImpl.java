/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.login;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.DatiAggiuntiviHeaders;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.entity.AmbitoEntity;
import it.csi.moon.commons.entity.FruitoreEntity;
import it.csi.moon.commons.util.HeadersUtils;
import it.csi.moon.moonfobl.business.service.AuditService;
import it.csi.moon.moonfobl.business.service.helper.JwtClientProfileUtils;
import it.csi.moon.moonfobl.business.service.impl.dao.AmbitoDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.FruitoreDAO;
import it.csi.moon.moonfobl.dto.moonfobl.LoginApiRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonfobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonfobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonfobl.util.IrideIdentitaDigitale;
import it.csi.moon.moonfobl.util.LoggerAccessor;

/**
 * Login con IDP Shiboleth
 * 
 * @author laurent
 *
 */
@Component
public class LoginApiIdpShibbolethImpl extends BaseLoginImpl implements Login {

	private static final String CLASS_NAME = "LoginApiIdpShibbolethImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	JwtClientProfileUtils jwtClientProfileUtils;
	@Autowired
	FruitoreDAO fruitoreDAO;
	@Autowired
	AuditService auditService;
//	@Autowired
//	UtenteDAO utenteDAO;
//	@Autowired
//	RuoloDAO ruoloDAO;
	@Autowired
	AmbitoDAO ambitoDAO;
	
	@Override
	public <T extends LoginRequest> LoginResponse login(T loginRequest, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		try {
			LOG.info("[" + CLASS_NAME + "::login] BEGIN");
			LOG.info("[" + CLASS_NAME + "::login] LoginRequest " + loginRequest);
			validaLoginApiRequest((LoginApiRequest)loginRequest);
			FruitoreEntity fruitore = validaFruitore(httpRequestUtils.getClientProfile(httpRequest));
			
			// login
			LoginResponse result = _login((it.csi.moon.moonfobl.dto.moonfobl.LoginApiRequest) loginRequest, httpRequest);
			result.setDatiAggiuntivi(makeDatiAggiuntivi(HeadersUtils.readFromHeaders(httpHeaders)));
			result.setIdFruitore(fruitore.getIdFruitore());
			
			// IdMoonToken
			final String idMoonToken = jwtTokenUtil.generateToken(loginRequest, result);
			logDebugJwtToken(idMoonToken);
			result.setIdMoonToken(idMoonToken);
			result.setDatiAggiuntivi(null);
			result.setIdFruitore(null);
			result.getEnte().setIdEnte(null);
			result.getEnte().setIdTipoEnte(null);
			result.getEnte().setIdEntePadre(null);
			result.getEnte().setDataUpd(null);
			result.getEnte().setAttoreUpd(null);
			result.getEnte().setLogo(null);
			result.getEnte().setFlAttivo(null);
			result.setApi(null);
			result.setOperatore(null);
			result.setMultiEntePortale(null);
			return result;
		} catch (DAOException daoe) {
			LOG.warn("[" + CLASS_NAME + "::login] DAOException " + daoe.getMessage());
			throw new BusinessException(daoe);
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::login] BusinessException " + be.getMessage());
			throw be;
		}
	}

	
	private DatiAggiuntivi makeDatiAggiuntivi(DatiAggiuntiviHeaders daHeaders) {
		DatiAggiuntivi datiAggiuntivi = new DatiAggiuntivi();
		datiAggiuntivi.setDataOraLogin(new Date());
		datiAggiuntivi.setProvider(daHeaders.getShibIdentitaProvider());
		datiAggiuntivi.setHeaders(daHeaders);
		return datiAggiuntivi;
	}


	private LoginResponse _login(LoginApiRequest loginRequest,  HttpServletRequest httpRequest) throws BusinessException, BusinessException, UnauthorizedException {
		LoginResponse result = null;
//		COTO_EXTRACOM_10 moon-extracom.patrim.csi.it
		
//	    checkLogin(nomePortale, LogonModeEnum.IDP_SHIBBOLETH.name(), loginRequest);	
						
		result = new LoginResponse(_login_GIA_AUTH(loginRequest,  httpRequest));
		result.setApi(true);
//		result.setProvider(loginRequest.getProvider());
//		String urlLogoutKey = EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider() ;
//		LOG.debug("[" + CLASS_NAME + "::urlLogoutKey ] " + urlLogoutKey);
		
		//result.setUrlLogout(EnvProperties.readFromFile(EnvProperties.LOGOUT_URL_PREFIX+loginRequest.getNomePortale()+"."+loginRequest.getProvider()));
//		result.setUrlLogout(EnvProperties.readFromFile(urlLogoutKey));
//		result.setIdIride(httpRequestUtils.getShibToken(httpRequest, devmode));
		result.setIdIride(loginRequest.getIdIride());
//		if (DecodificaPortale.COTO_EXTRACOM_10.hasNomePortale(nomePortale)||DecodificaPortale.TOFACILE_BO_3.hasNomePortale(nomePortale)
//				|| DecodificaPortale.TOFACILE_BO_TEST_9.hasNomePortale(nomePortale)) {
//			// COTO          TO_SOLIDALE
//			result = new LoginResponse(_login_COTO(loginRequest, nomePortale, httpServletRequest));
//			if (!Constants.CODICE_MODULO_TO_SOLIDALE.equals(loginRequest.getCodiceModulo())) {
//				LOG.error("[" + CLASS_NAME + "::_login] BusinessException : Modulo non pubblicato sul Portale COTO_EXTRACOM_10 || TOFACILE_BO_3 || TOFACILE_BO_TEST_9: "+loginRequest.getCodiceModulo());
//				throw new BusinessException("Modulo non pubblicato sul Portale");
//			}
//		}
		

		result = completaLoginResponseModulo(result, loginRequest); // QUI con contoTerzi
		LOG.info("[" + CLASS_NAME + "::_login] LoginOK result="+result);
		return result;
	}

	
	private UserInfo _login_GIA_AUTH(LoginApiRequest loginRequest, HttpServletRequest httpRequest) throws BusinessException, UnauthorizedException {
		try {
			String marker = loginRequest.getIdIride();
			IrideIdentitaDigitale identita = null;
			UserInfo userInfo = null;
			if (marker == null) {
				// Login con identificativo_utente, cognome, nome
				identita = new IrideIdentitaDigitale();
				identita.setCodFiscale(loginRequest.getCodiceFiscale());
				identita.setCognome(loginRequest.getCognome());
				identita.setNome(loginRequest.getNome());
				LOG.debug("[" + CLASS_NAME + "::_login_GIA_AUTH] Fake identita con identificativo_utente, cognome, nome:" + identita);
			} else {
				// Login con id_iride
				identita = new IrideIdentitaDigitale(marker);
			}
			LOG.debug("[" + CLASS_NAME + "::_login_GIA_AUTH] buildUserInfo con IRIDE:" + identita);
			userInfo = buildUserInfo(identita, loginRequest.getNomePortale(), httpRequest, loginRequest.getEmail());
			userInfo = completaUserInfoOfIsOperatore(userInfo, loginRequest, httpRequest);

			return userInfo;
		} catch (Exception e) { // MalformedIdTokenException e) {
			LOG.error("[" + CLASS_NAME + "::_login_GIA_AUTH] " + e.toString(), e);
			throw new BusinessException();
		}
	}


	private UserInfo completaUserInfoOfIsOperatore(UserInfo userInfo, LoginApiRequest loginRequest,
			HttpServletRequest httpRequest) {
//		String gruppoOperatoreReqParam = httpRequestUtils.getGruppoOperatoreRequestParam(httpRequest);
		String gruppoOperatoreReqParam = loginRequest.getGruppoOperatore();
		LOG.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] gruppoOperatoreReqParam:" + gruppoOperatoreReqParam);
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
		LOG.debug("[" + CLASS_NAME + "::completaUserInfoOfIsOperatore] OUT userInfo:" + userInfo);
		return userInfo;
	}


	private UserInfo buildUserInfo(IrideIdentitaDigitale identita, String portalName, HttpServletRequest httpRequest, String mail) throws DAOException, BusinessException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN identita:" + identita);
			LOG.debug("[" + CLASS_NAME + "::buildUserInfo] IN portalName:" + portalName);
		}
		LOG.debug("[" + CLASS_NAME + "::buildUserInfo] userInfo from identita..." );
		UserInfo userInfo = new UserInfo();
		userInfo.setIdentificativoUtente(identita.getCodFiscale());
		String shibCognome = httpRequestUtils.retrieveCognome(httpRequest);
		String cognome = shibCognome!=null?shibCognome.toUpperCase():identita.getCognome();
		String shibNome = httpRequestUtils.retrieveNome(httpRequest);
		String nome = shibNome!=null?shibNome.toUpperCase():identita.getNome();
		userInfo.setNome(nome);
		userInfo.setCognome(cognome);
		userInfo.setCodFiscDichIstanza(identita.getCodFiscale());
		userInfo.setMail(mail);
		// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
		Ente ente = null;
		try {
			ente = retrieveEnte(portalName, httpRequestUtils.getCodiceEnteRequestParam(httpRequest)); // Ente per il modulo
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::buildUserInfo] MOONFOBL-10902 - Errore invalid parameters - Identificazione dell'ente impossibile per il portale");
			throw new BusinessException("Identificazione dell'ente impossibile per il portale", "MOONFOBL-10902", "Errore invalid parameters");
		}
		if (ente == null) {
			LOG.error("[" + CLASS_NAME + "::buildUserInfo] MOONFOBL-10903 - Errore invalid parameters - Identificazione dell'ente impossibile per il portale (String codice_ente required)");
			throw new BusinessException("Identificazione dell'ente impossibile per il portale (String codice_ente required)", "MOONFOBL-10903", "Errore invalid parameters");
		}
		userInfo.setEnte(ente);
		userInfo.setMultiEntePortale(isPortaleMultiEnte(portalName));
		userInfo.setPortalName(portalName);
		String codiceAmbiente = httpRequestUtils.getCodiceAmbitoRequestParam(httpRequest);
		if (StringUtils.isNotBlank(codiceAmbiente)) {
			codiceAmbiente = codiceAmbiente.trim().toUpperCase();
			AmbitoEntity ambitoE = ambitoDAO.findByCodice(codiceAmbiente);
			userInfo.setIdAmbito(ambitoE.getIdAmbito());
			userInfo.setDescrizioneAmbito(ambitoE.getNomeAmbito());
		}
		userInfo.setIdIride(identita.toString());
		//
		String jwt = httpRequestUtils.retrieveJWT(httpRequest, devmode);
		userInfo.setJwt(jwt);

		return userInfo;
	}

	private void validaLoginApiRequest(LoginApiRequest loginRequest) throws BusinessException {
		if (StringUtils.isEmpty(loginRequest.getIdIride()) && StringUtils.isEmpty(loginRequest.getCodiceFiscale())) {
			LOG.error("[" + CLASS_NAME + "::validaLoginApiRequest] MOONFOBL-10901 - Errore invalid parameters - String required for id_iride or identificativo_utente");
			throw new BusinessException("String required for id_iride or identificativo_utente", "MOONFOBL-10901", "Errore invalid parameters");
		}
	}
	
	private FruitoreEntity validaFruitore(String clientProfile) throws UnauthorizedBusinessException, BusinessException {
		if (!jwtClientProfileUtils.isTokenValid(clientProfile)) {
			LOG.error("[" + CLASS_NAME + "::validaFruitore] UnauthorizedBusinessException jwtClientProfileUtils.isTokenValid() of clientProfile: " + clientProfile);
			throw new UnauthorizedBusinessException();
		}
		
		// Fruitore :  
		String codiceFruitore = jwtClientProfileUtils.getCodiceFruitoreFromToken(clientProfile);
		FruitoreEntity fruitore = null;
		try {
			fruitore = fruitoreDAO.getFruitoreByCodice(codiceFruitore);
		} catch (ItemNotFoundDAOException nfe) {
			LOG.error("[" + CLASS_NAME + "::validaFruitore] UnauthorizedBusinessException Fruitore " + codiceFruitore + " non configurato.");
			throw new UnauthorizedBusinessException("Fruitore sconosciuto", "MOONFOBL-10904", "ERRORE");
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::validaFruitore] idFruitore by codiceFruitore : " + fruitore.getIdFruitore());
		}
		return fruitore;
	}

}
