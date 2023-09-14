/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * https://github.com/dario-frongillo/spring-jwt-auth-italiancoders
 */
package it.csi.moon.moonbobl.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import it.csi.moon.moonbobl.business.service.mapper.TipoUtenteMapper;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntivi;
import it.csi.moon.moonbobl.dto.moonfobl.Ente;
import it.csi.moon.moonbobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonbobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;


@Component
public class JwtIdentitaUtil implements Serializable {
	
	private final static String CLASS_NAME = "JwtIdentitaUtil";
	private Logger log = LoggerAccessor.getLoggerBusiness();
    private static final long serialVersionUID = -3301605591108950419L;
    
	static ObjectMapper mapper;
	
    static final String CLAIM_KEY_CREATED = "iat";
    static final String CLAIM_KEY_SUBJECT = "sub";
    static final String CLAIM_KEY_AUDIENCE = "aud";
    
    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    static final String CLAIM_KEY_LOGON_MODE = "lm"; // logon_mode
    static final String CLAIM_KEY_TIPO_DOCUMENTO = "tdoc"; // tipo_documento
    static final String CLAIM_KEY_NUMERO_DOCUMENTO = "ndoc"; // numero_documento
    static final String CLAIM_KEY_DATA_RILASCIO_DOCUMENTO = "dtrdoc"; // data_rilascio_documento

    static final String CLAIM_KEY_ID_COMUNE = "idc"; // idComune
    static final String CLAIM_KEY_ID_FRUITORE = "idf"; // idFruitore
	
    static final String CLAIM_KEY_ID_UTENTE = "idu"; // identificativoUtente
    static final String CLAIM_KEY_CF = "cf"; // codiceFiscale
    static final String CLAIM_KEY_COGNOME = "cognome"; // cognome
    static final String CLAIM_KEY_NOME = "nome"; // nome
    static final String CLAIM_KEY_ID_MODULO = "idm"; // idModulo
    static final String CLAIM_KEY_ID_ENTE = "ide"; // idEnte
    static final String CLAIM_KEY_ID_AMBITO = "ida"; // idAmbito
    
    static final String CLAIM_KEY_IS_OPERATORE = "ope"; // isOperatore
    static final String CLAIM_KEY_GRUPPO_OPERATORE_FO = "gope"; // gruppoOperatore
    static final String CLAIM_KEY_IS_MULTI_ENTE_PORTALE = "mep"; // isMultiEntePortale
    
    static final String CLAIM_KEY_SHIB_IRIDE_IDENTITA = "shibIrideId"; // idIride - Shib-Iride-IdentitaDigitale
    static final String CLAIM_KEY_PORTAL_NAME = "pn"; // portalName
    
    static final String CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR = "ifc"; // idFamigliaConvivenzaANPR
    static final String CLAIM_KEY_CHIAVE_UNIVOCA = "ak"; // chiaveUnivocita
    
    static final String CLAIM_KEY_DATI_AGGIUNTIVI = "da";

    // Specifico BO
    static final String CLAIM_KEY_CODICE_TIPO_UTENTE = "ctu"; // codiceTipoUtente
    
    private static final String SECRET = "CSI-MOOn-Monbobl!2022";
   	private static final String ISSUER = "it.csi.moon."+Constants.COMPONENT_NAME;
   	private static final SignatureAlgorithm ALG = SignatureAlgorithm.HS256;
   	
//  private static final Long EXPIRATION = 60*30L; // 30min in seconds // SVIL
    private static final Long EXPIRATION = 3600*12L; // 12ore in seconds  // PROD

    public String getIdentificativoUtenteFromToken(String token) {
    	String idUtente;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idUtente = (String) claims.get(CLAIM_KEY_ID_UTENTE);
        } catch (Exception e) {
        	idUtente = null;
        }
        return idUtente;
    }
    
    public String getCodiceFiscaleFromToken(String token) {
        String codiceFiscale;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            codiceFiscale = claims.getSubject();
        } catch (Exception e) {
            codiceFiscale = null;
        }
        return codiceFiscale;
    }
    
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
    
    public String getCodFiscFromToken(String token) {
    	String codFisc;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            codFisc = (String) claims.get(CLAIM_KEY_CF);
        } catch (Exception e) {
        	codFisc = null;
        }
        return codFisc;
    }
    
    public String getLogonModeFromToken(String token) {
    	String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(CLAIM_KEY_LOGON_MODE);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }
    
    public Integer getTipoDocumentoFromToken(String token) {
    	Integer result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (Integer) claims.get(CLAIM_KEY_TIPO_DOCUMENTO);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }
    
    public String getNumeroDocumentoFromToken(String token) {
    	String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(CLAIM_KEY_NUMERO_DOCUMENTO);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }
    
    public String getDataRilascioDocumentoFromToken(String token) {
    	String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(CLAIM_KEY_DATA_RILASCIO_DOCUMENTO);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }
    
	public String getCognomeFromToken(String token) {
    	String cognome;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            cognome = (String) claims.get(CLAIM_KEY_COGNOME);
        } catch (Exception e) {
        	cognome = null;
        }
        return cognome;
    }
	
    public Integer getIdComuneFromToken(String token) {
    	Integer idComune;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idComune = (Integer) claims.get(CLAIM_KEY_ID_COMUNE);
        } catch (Exception e) {
        	idComune = null;
        }
        return idComune;
    }
    
	public Long getIdModuloFromToken(String token) {
    	Long idModulo;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idModulo = Long.valueOf((Integer)claims.get(CLAIM_KEY_ID_MODULO));
        } catch (Exception e) {
        	idModulo = null;
        }
        return idModulo;
    }
    
    public Long getIdEnteFromToken(String token) {
    	Long idEnte;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idEnte = Long.valueOf((Integer)claims.get(CLAIM_KEY_ID_ENTE));
        } catch (Exception e) {
        	idEnte = null;
        }
        return idEnte;
    }
    
    public Long getIdAmbitoFromToken(String token) {
    	Long idEnte;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idEnte = Long.valueOf((Integer)claims.get(CLAIM_KEY_ID_AMBITO));
        } catch (Exception e) {
        	idEnte = null;
        }
        return idEnte;
    }
    
    public String getPortaleNameFromToken(String token) {
    	String portaleName;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            portaleName = (String) claims.get(CLAIM_KEY_PORTAL_NAME);
        } catch (Exception e) {
        	portaleName = null;
        }
        return portaleName;
    }
    
    public Integer getIdFruitoreFromToken(String token) {
    	Integer idFruitore;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            idFruitore = (Integer) claims.get(CLAIM_KEY_ID_FRUITORE);
        } catch (Exception e) {
        	idFruitore = null;
        }
        return idFruitore;
    }
    
    public String getIdFamigliaConvivenzaANPRFromToken(String token) {
    	String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    } 
    
    public String getChiaveUnivocitaFromToken(String token) {
    	String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(CLAIM_KEY_CHIAVE_UNIVOCA);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }

    public DatiAggiuntivi getDatiAggiuntiviFromToken(String token) {
    	DatiAggiuntivi result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = getDatiAggiuntiviFromClaims(claims);
        } catch (Exception e) {
        	result = null;
        }
        return result;
    }

	private DatiAggiuntivi getDatiAggiuntiviFromClaims(final Claims claims) {
		DatiAggiuntivi result;
		try {
			String strResult = (String) claims.get(CLAIM_KEY_DATI_AGGIUNTIVI);
			result = getMapper().readValue(strResult, DatiAggiuntivi.class);
	    } catch (Exception e) {
	    	result = null;
	    }
	    return result;
	}
	
	
    public UserInfo retreiveUserInfoFromToken(String token) throws JsonProcessingException {
    	UserInfo result = new UserInfo();
        try {
            final Claims claims = getBobyClaimsFromToken(token);
//            result.setIdFruitore(claims.get(CLAIM_KEY_ID_FRUITORE) != null ? ((Integer)claims.get(CLAIM_KEY_ID_FRUITORE)) : null);
            result.setIdentificativoUtente((String) claims.get(CLAIM_KEY_ID_UTENTE));
//            result.setCodFiscDichIstanza((String) claims.get(CLAIM_KEY_CF));
            result.setCognome(decodeValue((String) claims.get(CLAIM_KEY_COGNOME)));
            result.setNome(decodeValue((String) claims.get(CLAIM_KEY_NOME)));          
            result.setIdMoonToken(token);
            result.setIdIride(decodeValue((String) claims.get(CLAIM_KEY_SHIB_IRIDE_IDENTITA)));
//            result.setOperatore((boolean) claims.get(CLAIM_KEY_IS_OPERATORE));
//            result.setGruppoOperatoreFo((String) claims.get(CLAIM_KEY_GRUPPO_OPERATORE_FO));
            result.setMultiEntePortale((boolean) claims.get(CLAIM_KEY_IS_MULTI_ENTE_PORTALE));            
            Ente ente = new Ente(); // mantenere il cast integer 
            ente.setIdEnte(claims.get(CLAIM_KEY_ID_ENTE) != null ? ((Integer)claims.get(CLAIM_KEY_ID_ENTE)).longValue(): null);
            result.setEnte(ente);
//            result.setIdAmbito(claims.get(CLAIM_KEY_ID_AMBITO) != null ? ((Integer) claims.get(CLAIM_KEY_ID_AMBITO)): null);
            result.setDatiAggiuntivi(getDatiAggiuntiviFromClaims(claims));
            result.setPortalName((String) claims.get(CLAIM_KEY_PORTAL_NAME));
            // Specifico BO
            result.setTipoUtente(TipoUtenteMapper.buildFromCodiceTipoUtente((String) claims.get(CLAIM_KEY_CODICE_TIPO_UTENTE)));
            if (log.isDebugEnabled()) {
    			log.debug("[" + CLASS_NAME + "::retreiveUserInfoFromToken] OUT result = " + result);
            }
        } catch (Exception e) {
        	log.debug("[" + CLASS_NAME + "::retreiveUserInfoFromToken] Exception "+e.getMessage());
        }
        return result;
    }
	
    public LoginResponse retreiveLoginResponseFromToken(String token) throws JsonProcessingException {
    	LoginResponse result = new LoginResponse(retreiveUserInfoFromToken(token));
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            Long idModulo = (Long) claims.get(CLAIM_KEY_ID_MODULO);
            if (idModulo!=null) {
            	Modulo modulo = new Modulo();
            	modulo.setIdModulo(idModulo);
            	result.setModulo(modulo);
            }
            if (log.isDebugEnabled()) {
    			log.fatal("[" + CLASS_NAME + "::retreiveLoginResponseFromToken] OUT result = " + result);
            }
        } catch (Exception e) {
        	log.debug("[" + CLASS_NAME + "::retreiveLoginResponseFromToken] Exception "+e.getMessage());
        }
        return result;
    }
	
    
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }
	
	
    private Claims getBobyClaimsFromToken(String token) {
        Claims claims;
        try {
            Jws<Claims> allClaims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .requireIssuer(ISSUER)
                    .parseClaimsJws(token);
            if (!ALG.name().equals(allClaims.getHeader().getAlgorithm())) {
                log.fatal("[" + CLASS_NAME + "::getClaimsFromToken] allClaims.Header().getAlgorithm() = " + allClaims.getHeader().getAlgorithm());
            	throw new BusinessException("JWT without correct Algorithm.");
            }
            claims = allClaims.getBody();
        } catch (Exception e) {
        	log.error("[" + CLASS_NAME + "::getClaimsFromToken] Exception " + e);
            claims = null;
        }
        return claims;
    }

    
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION * 1000);
    }
    
    public Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        log.debug("[" + CLASS_NAME + "::isTokenExpired] expirationDate: "+expirationDate);
        Boolean result = expirationDate==null || expirationDate.before(new Date());
        log.debug("[" + CLASS_NAME + "::isTokenExpired] result: "+result);
        return result;
    }
    
    
    public String generateToken(LoginResponse userInfo/*, Device device*/) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, userInfo.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_UTENTE, userInfo.getIdentificativoUtente());
//        claims.put(CLAIM_KEY_ID_FRUITORE, userInfo.getIdFruitore());
        
//        claims.put(CLAIM_KEY_CF, userInfo.getCodFiscDichIstanza());
        claims.put(CLAIM_KEY_COGNOME, userInfo.getCognome());
        claims.put(CLAIM_KEY_ID_MODULO, userInfo.getModulo().getIdModulo());
        claims.put(CLAIM_KEY_ID_ENTE, userInfo.getEnte().getIdEnte());
        claims.put(CLAIM_KEY_PORTAL_NAME, userInfo.getPortalName());
        claims.put(CLAIM_KEY_SHIB_IRIDE_IDENTITA, userInfo.getIdIride());
//        claims.put(CLAIM_KEY_ID_AMBITO, userInfo.getIdAmbito());
        
//        claims.put(CLAIM_KEY_IS_OPERATORE, userInfo.isOperatore());
//        claims.put(CLAIM_KEY_GRUPPO_OPERATORE_FO, userInfo.getGruppoOperatoreFo());
        claims.put(CLAIM_KEY_IS_MULTI_ENTE_PORTALE, userInfo.isMultiEntePortale());
        
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);

        return generateToken(claims);
    }
    
	public String generateToken(LoginRequest loginRequest, LoginResponse loginResponse/*, Device device*/) throws JsonProcessingException {
        return generateToken(initClaims(loginRequest, loginResponse));
    }
//    public String generateToken(LoginRequest loginRequest, LoginResponse loginResponse, UlterioreDatiMoonToken ulterioreDatiMoonToken/*, Device device*/) throws JsonProcessingException {
//        Map<String, Object> claims = initClaims(loginRequest, loginResponse);
//        claims.put(CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR, ulterioreDatiMoonToken!=null?ulterioreDatiMoonToken.getIdFamigliaConvivenzaANPR():null);
//        claims.put(CLAIM_KEY_CHIAVE_UNIVOCA, ulterioreDatiMoonToken!=null?ulterioreDatiMoonToken.getChiaveUnivocita():null);
//        return generateToken(claims);
//    }

	private Map<String, Object> initClaims(LoginRequest loginRequest, LoginResponse loginResponse/*, Device device*/) {
	  try {
		Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, loginResponse.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_UTENTE, loginResponse.getIdentificativoUtente());
//        claims.put(CLAIM_KEY_ID_FRUITORE, loginResponse.getIdFruitore());
        
//        claims.put(CLAIM_KEY_CF, loginResponse.getCodFiscDichIstanza());
        claims.put(CLAIM_KEY_COGNOME, encodeValue(loginResponse.getCognome()));
        claims.put(CLAIM_KEY_NOME, encodeValue(loginResponse.getNome()));
        claims.put(CLAIM_KEY_ID_MODULO, loginResponse.getModulo()==null?null:loginResponse.getModulo().getIdModulo());
        claims.put(CLAIM_KEY_ID_ENTE, loginResponse.getEnte()==null?null:loginResponse.getEnte().getIdEnte());
        claims.put(CLAIM_KEY_PORTAL_NAME, loginResponse.getPortalName());
        claims.put(CLAIM_KEY_SHIB_IRIDE_IDENTITA, encodeValue(loginResponse.getIdIride()));
//        claims.put(CLAIM_KEY_ID_AMBITO, loginResponse.getIdAmbito()==null?null:loginResponse.getIdAmbito());
        
//        claims.put(CLAIM_KEY_IS_OPERATORE, loginResponse.isOperatore());
//        claims.put(CLAIM_KEY_GRUPPO_OPERATORE_FO, loginResponse.getGruppoOperatoreFo());
        claims.put(CLAIM_KEY_IS_MULTI_ENTE_PORTALE, loginResponse.isMultiEntePortale());
        
        claims.put(CLAIM_KEY_LOGON_MODE, loginRequest.getLogonMode());
        claims.put(CLAIM_KEY_TIPO_DOCUMENTO, loginRequest.getTipoDocumento());
        claims.put(CLAIM_KEY_NUMERO_DOCUMENTO, loginRequest.getNumeroDocumento());
        claims.put(CLAIM_KEY_DATA_RILASCIO_DOCUMENTO, loginRequest.getDataRilascioDocumento());
        claims.put(CLAIM_KEY_CODICE_TIPO_UTENTE, loginResponse.getTipoUtente().getCodice());
        
        DatiAggiuntivi da = null;
        try {
        	da = loginResponse.getDatiAggiuntivi();
			claims.put(CLAIM_KEY_DATI_AGGIUNTIVI, getMapper().writeValueAsString(da));
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::initClaims] datiAggiuntivi = " + da, e);
		}
        
		return claims;
	  } catch (UnsupportedEncodingException e) {
		log.error("[" + CLASS_NAME + "::initClaims] UnsupportedEncodingException loginResponse = " + loginResponse);
		throw new BusinessException();
	  }
	}
	
    private String decodeValue(String value) throws DecoderException {
	    return new URLCodec().decode(value);
	}
    private String encodeValue(String value) throws UnsupportedEncodingException {
	    return value!= null ? URLEncoder.encode(value, StandardCharsets.UTF_8.toString()):null;
	}
    
    private String generateToken(Map<String, Object> claims) {
        String result = Jwts.builder()
            .setClaims(claims)
            .setIssuer(ISSUER)
            .setExpiration(generateExpirationDate())
            .signWith(ALG, SECRET)
            .compact();
        return result;
    }

    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return  (!isTokenExpired(token) /*|| ignoreTokenExpiration(token)*/);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String writeIdEnte(String token, Long idEnte) {
        String refreshedToken;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            claims.put(CLAIM_KEY_ID_ENTE, idEnte);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
    
    public Boolean validateToken(String token, LoginResponse userInfo) {
        final String idUtente = getIdentificativoUtenteFromToken(token);
        return (idUtente.equals(userInfo.getIdentificativoUtente()) && !isTokenExpired(token));
    }


    /* 
     * new methods    
     */
    private Claims getDataClaimsFromToken(String token)  {
        Claims claims;
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                    .parseClaimsJws(token)
                    .getBody();
        return claims;
    }
    
    public Boolean isTokenValid(String token) {
    	try {
			Claims claims = this.getDataClaimsFromToken(token);	
			return Boolean.TRUE && !isTokenExpired(token);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException jwtEx ) {
				return Boolean.FALSE;	
		} 
    }
    
    public String toString(String token) {
    	if (token==null || token.isEmpty() || token.isBlank()) {
    		return "token null or empty";
    	}
    	final Claims claims = getBobyClaimsFromToken(token);
	    StringBuilder sb = new StringBuilder();	
    	if (claims!=null) {
//		    sb.append("    idComune: ").append((Integer) claims.get(CLAIM_KEY_ID_COMUNE)).append("\n");
//		    sb.append("    idFarmacia: ").append((Integer) claims.get(CLAIM_KEY_ID_FARMACIA)).append("\n");
//		    sb.append("    codiceFarmacia: ").append(claims.get(CLAIM_KEY_CODICE_FARMACIA)).append("\n");

    		appendOneClaims(sb, claims, CLAIM_KEY_ID_UTENTE);
    		appendOneClaims(sb, claims, CLAIM_KEY_ID_FRUITORE);
		    appendOneClaims(sb, claims, CLAIM_KEY_CF);
		    appendOneClaims(sb, claims, CLAIM_KEY_COGNOME);
		    appendOneClaims(sb, claims, CLAIM_KEY_NOME);
		    appendOneClaims(sb, claims, CLAIM_KEY_ID_MODULO);
		    appendOneClaims(sb, claims, CLAIM_KEY_ID_ENTE);
		    appendOneClaims(sb, claims, CLAIM_KEY_ID_AMBITO);
		    
		    appendOneClaims(sb, claims, CLAIM_KEY_IS_OPERATORE);
		    appendOneClaims(sb, claims, CLAIM_KEY_GRUPPO_OPERATORE_FO);
		    appendOneClaims(sb, claims, CLAIM_KEY_IS_MULTI_ENTE_PORTALE);
		    appendOneClaims(sb, claims, CLAIM_KEY_SHIB_IRIDE_IDENTITA);
		    appendOneClaims(sb, claims, CLAIM_KEY_PORTAL_NAME);
		    
		    sb.append("    created: ").append(new Date((Long) claims.get(CLAIM_KEY_CREATED))).append("\n");
		    sb.append("    expiration: ").append(claims.getExpiration()).append("\n");
		    sb.append("    subject: ").append(claims.getSubject()).append("\n");
		    sb.append("    issuer: ").append(claims.getIssuer()).append("\n");
		    sb.append("    issuedAt: ").append(claims.getIssuedAt()).append("\n");
		    sb.append("    audience: ").append(claims.getAudience()).append("\n");
		    sb.append("    id: ").append(claims.getId()).append("\n");
		    sb.append("    notBefore: ").append(claims.getNotBefore()).append("\n");

		    appendOneClaims(sb, claims, CLAIM_KEY_LOGON_MODE);
		    appendOneClaims(sb, claims, CLAIM_KEY_TIPO_DOCUMENTO);
		    appendOneClaims(sb, claims, CLAIM_KEY_NUMERO_DOCUMENTO);
		    appendOneClaims(sb, claims, CLAIM_KEY_DATA_RILASCIO_DOCUMENTO);

		    appendOneClaims(sb, claims, CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR);
		    appendOneClaims(sb, claims, CLAIM_KEY_CHIAVE_UNIVOCA);
		    appendOneClaims(sb, claims, CLAIM_KEY_DATI_AGGIUNTIVI);

    	} else {
    		sb.append("token claims NULL !!!");
    	}
	    return sb.toString();
    }
    
    private void appendOneClaims(StringBuilder sb, final Claims claims, final String claimsKey) {
		sb.append("    ").append(claimsKey).append(": ").append(claims.get(claimsKey)).append("\n");
	}

	public static ObjectMapper getMapper() {
		if(mapper == null) {
			mapper = new ObjectMapper();
			SerializationConfig config = mapper
				.getSerializationConfig()
				.withSerializationInclusion(Inclusion.NON_EMPTY)
				.without(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
			mapper.setSerializationConfig(config);
		}
		return mapper;
	}
}
