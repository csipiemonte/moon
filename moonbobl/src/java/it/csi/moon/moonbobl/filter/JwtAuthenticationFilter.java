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
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.HttpRequestUtils;
import it.csi.moon.moonbobl.util.JwtIdentitaUtil;

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
public class JwtAuthenticationFilter implements Filter {

//	public static final String MOON_ID_JWT = "Moon-Identita-JWT"; // Marker in HttpServletRequest.Header

    @Autowired
    private JwtIdentitaUtil jwtTokenUtil;
    
    private static final Set<String> ALLOWED_EXACT_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("", 
            	"/restfacade/be/api/v1/moon-identita", 
            	//"/restfacade/be/file", 
            	"/localLogout" 
            	)));
	/*
	 * Consente di accedere ai servizi pubblici pubblicati sotto il path extra
	 * toponomastica
	 * istat
	 * nazioni
	 *  */
    private static final Set<String> ALLOWED_INIT_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList(
            	"/restfacade/be/extra")));
    

	protected static final Logger LOG = Logger.getLogger(Constants.COMPONENT_NAME + ".security");

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fchain)
			throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", ""); 

        boolean allowedPath = ALLOWED_EXACT_PATHS.contains(path) || (!path.isEmpty() && !path.startsWith("/restfacade") );
        if (!allowedPath) {
        	Iterator<String> allowedInitPathItr = ALLOWED_INIT_PATHS.iterator();
        	while(!allowedPath && allowedInitPathItr.hasNext()) { 
				if (!allowedPath && path.startsWith(allowedInitPathItr.next())) {
					allowedPath = true;
				}
        	}
        }


		if (allowedPath) {
			LOG.debug("[JwtAuthenticationFilter::doFilter] Already logged.");
			fchain.doFilter(req, res);
		} else {
			LOG.debug("[JwtAuthenticationFilter::doFilter] sessionAttribute not found. NEW Login ...");
//			String marker = getMoonToken(request);
			HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
			String marker = httpRequestUtils.getMoonToken(request, devmode);
			LOG.debug("[JwtAuthenticationFilter::doFilter] marker :\n"+jwtTokenUtil.toString(marker));
			if (marker != null) {
				try {
					if (!jwtTokenUtil.isTokenExpired(marker)) {
						LOG.debug("[JwtAuthenticationFilter::doFilter] not expired.");
						marker = jwtTokenUtil.refreshToken(marker);
						response.setHeader(HttpRequestUtils.HEADER_MOON_ID_JWT, marker);
					
						//
						fchain.doFilter(req, res);
					} else {
						// Expired
						LOG.error("[JwtAuthenticationFilter::doFilter] ExpiredJWT " + marker);
						response.sendError(HttpServletResponse.SC_FORBIDDEN, "ExpiredJWT");
//						throw new ServletException(
//								"Token di sicurezza espirato.");
					}
				} catch (Exception e) {
					LOG.error("[JwtAuthenticationFilter::doFilter] MalformedJWT " + marker + "\n" + e.toString(), e);
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "MalformedJWT");
				}
			} else {
				// il marcatore deve sempre essere presente altrimenti e' una 
				// condizione di errore (escluse le pagine home e di servizio)
				if (mustCheckPage(request.getRequestURI())) {
					LOG.warn(
							"[JwtAuthenticationFilter::doFilter] Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
					response.sendError(HttpServletResponse.SC_FORBIDDEN,
							"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
					throw new ServletException(
							"Tentativo di accesso a pagina non home e non di servizio senza token di sicurezza");
				}
			}
		}

	}
	
	private boolean mustCheckPage(String requestURI) {

		return true;
	}

	public void destroy() {
		// NOP
	}

	private static final String DEVMODE_INIT_PARAM = "devmode";
	private static final String TSTMODE_REMAPIDENTITA_INIT_PARAM = "tstmode.remapidentita";

	private boolean devmode = false;
	private boolean tstmodeRemapIdentita = false;

	public void init(FilterConfig fc) throws ServletException {
        //must provide autowiring support to inject SpringBean
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, fc.getServletContext()); 

        devmode = "true".equalsIgnoreCase(fc.getInitParameter(DEVMODE_INIT_PARAM))?true:false;
//        tstmodeRemapIdentita = "true".equalsIgnoreCase(fc.getInitParameter(TSTMODE_REMAPIDENTITA_INIT_PARAM))?true:false;
	}

}
