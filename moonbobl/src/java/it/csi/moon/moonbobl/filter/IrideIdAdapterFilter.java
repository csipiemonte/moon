/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.BackendService;
import it.csi.moon.moonbobl.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RuoloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.mapper.EnteMapper;
import it.csi.moon.moonbobl.business.service.mapper.TipoUtenteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.AreaRuolo;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.EnteAreeRuoli;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.EnvProperties;
import it.csi.moon.moonbobl.util.IrideIdentitaDigitale;
import it.csi.moon.moonbobl.util.decodifica.DecodificaPortale;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;

/**
 * Inserisce in sessione:
 * <ul> 
 *  <li>l'identit&agrave; digitale relativa all'utente autenticato.
 *  <li>l'oggetto <code>currentUser</code>
 * </ul>
 * Funge da adapter tra il filter del metodo di autenticaizone previsto e la
 * logica applicativa.
 *
 * @author CSIPiemonte
 */
public class IrideIdAdapterFilter implements Filter {

	public static final String IRIDE_ID_SESSIONATTR = "iride2_id";
	public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";
	public static final String JWT_ID_MARKER = "Shib-Identita-JWT";
	public static final String SIMULATE_PORTALE = "Simulate-Portale";

    private static final Set<String> ALLOWED_EXACT_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("", "/localLogout" ,"/restfacade/be/file/notifica"
            	)));   
    private static final Set<String> ALLOWED_INIT_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
                "/restfacade/be/extra/istat",
            	"/restfacade/be/extra/regioni",
            	"/restfacade/be/extra/demografia",
            	"/restfacade/be/extra/demografia/nazioni",
            	"/restfacade/be/extra/territorio/toponomastica/vie")));
    
    private static final Set<String> ALLOWED_ROLES = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("ADMIN", "RESP", "OP_ADM", "OP_ADV", "OP_SIMP", "OP_CON", "OP_BLDR", "OP_COMP", "OP_SIMPMOD")));
    
    public static final String target = EnvProperties.readFromFile(EnvProperties.TARGET_LINE);
    public static final boolean test = target.startsWith("tst-");
    
	/**  */
	protected static final Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME + ".security");

	@Autowired
	EnteDAO enteDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	RuoloDAO ruoloDAO;
	@Autowired
	BackendService backendService;
	@Autowired
	AuditService auditService;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fchain)
			throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", ""); 

        boolean loggedIn = (session != null && session.getAttribute(IRIDE_ID_SESSIONATTR) != null);
        boolean allowedPath = ALLOWED_EXACT_PATHS.contains(path) || (!path.isEmpty() && !path.startsWith("/restfacade") || path.contains("-unsecure"));
        if (!allowedPath) {
        	Iterator<String> allowedInitPathItr = ALLOWED_INIT_PATHS.iterator();
        	while(!allowedPath && allowedInitPathItr.hasNext()) { 
				if (!allowedPath && path.startsWith(allowedInitPathItr.next())) {
					allowedPath = true;
				}
        	}
        }
        
		if (loggedIn || allowedPath) {
			fchain.doFilter(req, res);
		} else {
			String marker = getToken(request);
			if (marker != null) {
				try {
					String portalName = getPortalName(request);
					IrideIdentitaDigitale identita = new IrideIdentitaDigitale(normalizeToken(marker));
					LOG.debug("[IrideIdAdapterFilter::doFilter] Inserito in sessione marcatore IRIDE:" + identita);
					String jwt = getJWT(request);
					UserInfo userInfo = _login(identita, jwt, portalName, response);
					request.getSession().setAttribute(Constants.SESSION_PORTALNAME, portalName);
					request.getSession().setAttribute(IRIDE_ID_SESSIONATTR, identita);
					request.getSession().setAttribute(Constants.SESSION_USERINFO, userInfo);
					auditService.insertAuditLogin(req.getRemoteAddr(), userInfo);
					fchain.doFilter(req, res);
				} catch (UnauthorizedException e) {
					LOG.error("[IrideIdAdapterFilter::doFilter] UnauthorizedException " + e.toString());
					response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
				} catch (Exception e) { // MalformedIdTokenException
					LOG.error("[IrideIdAdapterFilter::doFilter] " + e.toString(), e);
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "MalformedJWT");
				}
			} else {
				// il marcatore deve sempre essere presente altrimenti e' una 
				// condizione di errore (escluse le pagine home e di servizio)
				if (mustCheckPage(request.getRequestURI())) {
					LOG.error(
							"[IrideIdAdapterFilter::doFilter] Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
					response.sendError(HttpServletResponse.SC_FORBIDDEN,
							"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
					throw new ServletException(
							"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
				}
			}
		}



	}

	private UserInfo _login(IrideIdentitaDigitale identita, String jwt, String portalName, HttpServletResponse response) throws IOException, UnauthorizedException {
		UserInfo result = null;
		try {
			// remap utente per test
			UserInfo identitaJwt = transform(identita, jwt);
			
			//
			UtenteEntity utente = null;
			try {
				utente = utenteDAO.findByIdentificativoUtente(identitaJwt.getIdentificativoUtente()); // identita.getCodFiscale());
			} catch (ItemNotFoundDAOException e) {
				LOG.error("[IrideIdAdapterFilter::_login] ItemNotFoundDAOException utente non trovato identitaJwt.getCodFisc()=" + identitaJwt.getIdentificativoUtente());
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "UnknownUser");
				throw new UnauthorizedException("UnknownUser");
			}
			result = new UserInfo();
			result.setNome(identitaJwt.getNome());
			result.setCognome(identitaJwt.getCognome());
			List<EnteAreeRuoli> entiAreeRuoli = backendService.getEntiAreeRuoliAttivi(utente.getIdUtente(), portalName);
			if ((entiAreeRuoli==null || entiAreeRuoli.isEmpty()) && !DecodificaTipoUtente.ADM.isCorrectType(utente)) {
				LOG.error("[IrideIdAdapterFilter::_login] EntiAreaRuoliAttivi vuoto per identitaJwt.getCodFisc()=" + identitaJwt.getIdentificativoUtente() + "  idUtente="+utente.getIdUtente());
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "NoRolesForUserNonADM");
				throw new UnauthorizedException("NoRolesForUserNonADM");
			}
			result.setEntiAreeRuoli(entiAreeRuoli);
			LOG.debug("[IrideIdAdapterFilter::_login] result.getEntiAreeRuoli.size() = "+result.getEntiAreeRuoli()!=null?result.getEntiAreeRuoli().size():null);
			LOG.debug("[IrideIdAdapterFilter::_login] result.getEntiAreeRuoli = "+result.getEntiAreeRuoli()!=null?result.getEntiAreeRuoli():"");
			result.setTipoUtente(TipoUtenteMapper.buildFromIdTipoUtente(utente.getIdTipoUtente()));
			result.setIdentificativoUtente(identitaJwt.getIdentificativoUtente());
			result.setIdIride(identitaJwt.getIdIride());
			
			// Valida accesso al BO
			if (!(DecodificaTipoUtente.ADM.isCorrectType(utente) || mantainEnteIfContainsRuoloRESPorOPERATORE(result))) {
				LOG.error("[IrideIdAdapterFilter::_login] LoginException for "+result);
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "LoginException");
				throw new UnauthorizedException("LoginException");
			}
//			if (!result.getEntiAreeRuoli().isEmpty()) {
//				// init con il primo ente
//				EnteEntity enteE = enteDAO.findById(result.getEntiAreeRuoli().get(0).getIdEnte());
//				result.setEnte(EnteMapper.buildFromEntity(enteE));
//				// ma per ADM ??
//			}
			// ENTE - Nel caso di installazione MONO_ENTE deve essere valorizzato id_ente nei file di properties (buidtime)
			Ente ente = retrieveEnte(portalName); // Ente per il modulo
			if(ente == null) {
				ente = retrieveUnicoEnte(entiAreeRuoli);
			}
			result.setEnte(ente);
			result.setMultiEntePortale(ente==null);
		} catch (DAOException e) {
			LOG.error("[IrideIdAdapterFilter::_login] DAOException ", e);
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "DAOException");
			throw new UnauthorizedException("_LoginDAOException");
		} catch (BusinessException e) {
			LOG.error("[IrideIdAdapterFilter::_login] BusinessException ", e);
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "BusinessException");
			throw new UnauthorizedException("_LoginBusinessException");
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
		LOG.debug(result);
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


	//
	// NON SO SE SERVE PER IL DEV&TST DEL BO  !?
	//
	private it.csi.moon.moonbobl.dto.moonfobl.UserInfo transform(IrideIdentitaDigitale identita, String jwt) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[IrideIdAdapterFilter::transform] IN identita:" + identita);
			LOG.debug("[IrideIdAdapterFilter::transform] IN jwt:" + jwt);
		}
		LOG.debug("[IrideIdAdapterFilter::transform] userInfo from identita..." );
		it.csi.moon.moonbobl.dto.moonfobl.UserInfo userInfo = new it.csi.moon.moonbobl.dto.moonfobl.UserInfo();
		userInfo.setNome(identita.getNome());
		userInfo.setCognome(identita.getCognome());
		userInfo.setEnte(null);
//		userInfo.setRuolo("--");
		userInfo.setIdentificativoUtente(identita.getCodFiscale());
		userInfo.setIdIride(identita.toString());
		//
		userInfo.setJwt(jwt);

		return userInfo;
	}

	
	private boolean mustCheckPage(String requestURI) {

		return true;
	}

	public void destroy() {
		// NOP
	}

	/**
	 * Recupera l'ente corrente, che verra usato nel salvataggio dell'istanza (non corrisponde all'ente dell'utente)
	 * Attualmente i portali sono mono ente, la relazione e codificata in DecodificaPortale
	 * @param portalName
	 * @return
	 */
	private Ente retrieveEnte(String portalName) {
		Ente result = null; //new Ente();
		LOG.debug("[IrideIdAdapterFilter::retrieveEnte] IN portalName " + portalName );
		DecodificaPortale portale = DecodificaPortale.byNomePortale((portalName.equals("localhost")) ? "*" : portalName);
		LOG.debug("[IrideIdAdapterFilter::retrieveEnte] portale " + portale );
		Long idEnte = portale.getIdEnte();
		if (idEnte>0) {
			try {
				result = EnteMapper.buildFromEntity(enteDAO.findById(idEnte));
			} catch (DAOException e) {
				LOG.error("[IrideIdAdapterFilter::retrieveEnte] DAOException enteDAO.findById " + idEnte, e );
				throw e;
			}
		}
		return result;
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
	
	private static final String DEVMODE_INIT_PARAM = "devmode";
	private static final String TSTMODE_REMAPIDENTITA_INIT_PARAM = "tstmode.remapidentita";

	private boolean devmode = false;
	private boolean tstmodeRemapIdentita = false;

	public void init(FilterConfig fc) throws ServletException {
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, fc.getServletContext());
        //
		String sDevmode = fc.getInitParameter(DEVMODE_INIT_PARAM);
		if ("true".equals(sDevmode)) {
			devmode = true;
		} else {
			devmode = false;
		}
		
		String sTstmodeRemapIdentita = fc.getInitParameter(TSTMODE_REMAPIDENTITA_INIT_PARAM);
		if ("true".equals(sTstmodeRemapIdentita)) {
			tstmodeRemapIdentita = true;
		} else {
			tstmodeRemapIdentita = false;
		}
	}

	public String getToken(HttpServletRequest httpreq) {
		String marker = (String) httpreq.getHeader(AUTH_ID_MARKER);
		if (marker == null && devmode) {
			return getParamDevMode(httpreq, AUTH_ID_MARKER);
		} else {
			try {
				// gestione dell'encoding
				String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
				if (test) {
					LOG.debug("[IrideIdAdapterFilter::getToken] decodedMarker: " + decodedMarker);
				}
				return decodedMarker;
			} catch (java.io.UnsupportedEncodingException e) {
				// se la decodifica non funziona comunque sempre meglio restituire 
				// il marker originale non decodificato
				return marker;
			}
		}
	}

	public String getJWT(HttpServletRequest httpreq) {
		String marker = (String) httpreq.getHeader(JWT_ID_MARKER);
		
		if (marker == null) {
			if (devmode)
				return getParamDevMode(httpreq, JWT_ID_MARKER);
			else
				return "";
		} else {
			try {
				// gestione dell'encoding
				String decodedMarker = new String(marker.getBytes("ISO-8859-1"), "UTF-8");
				return decodedMarker;
			} catch (java.io.UnsupportedEncodingException e) {
				// se la decodifica non funziona comunque sempre meglio restituire 
				// il marker originale non decodificato
				return marker;
			}
		}
	}
	
	private String getParamDevMode(HttpServletRequest httpreq, String paramName) {
		String marker = (String) httpreq.getParameter(paramName);
		return marker;
	}

	private String normalizeToken(String token) {
		return token;
	}
	
	private String getPortalName(HttpServletRequest httpreq) {
		String portalName = httpreq.getServerName();
		if (devmode && "localhost".equals(portalName)) {
			String simulatePortaleDev = getParamDevMode(httpreq, SIMULATE_PORTALE);
			LOG.debug("[IrideIdAdapterFilter::getPortalName] simulatePortaleDev=" + simulatePortaleDev);
			portalName = simulatePortaleDev!=null?simulatePortaleDev:portalName;
		} else {
			portalName = removeTstPortaleName(portalName);
		}
	    if (LOG.isDebugEnabled()) {
	    	LOG.debug("[IrideIdAdapterFilter::getPortalName] getPortalName()=" + portalName);
	    }
	    return portalName;
	}
	private String removeTstPortaleName(String serverName) {
		String portalName = serverName;
		Pattern p = Pattern.compile("tst-");
        Matcher m = p.matcher(serverName);
        if(m.find()) {
        	portalName = serverName.substring(4);
        }
        Pattern p2 = Pattern.compile("ts-");
        Matcher m2 = p2.matcher(serverName);
        if(m2.find()) {
        	portalName = serverName.substring(3);
        }
        return portalName;
	}

}
