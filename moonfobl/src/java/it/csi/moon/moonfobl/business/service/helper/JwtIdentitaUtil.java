/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * https://github.com/dario-frongillo/spring-jwt-auth-italiancoders
 */
package it.csi.moon.moonfobl.business.service.helper;

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
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import it.csi.moon.commons.dto.DatiAggiuntivi;
import it.csi.moon.commons.dto.Ente;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.moonfobl.dto.moonfobl.LoginRequest;
import it.csi.moon.moonfobl.dto.moonfobl.LoginResponse;
import it.csi.moon.moonfobl.dto.moonfobl.UlterioreDatiMoonToken;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.util.Constants;
import it.csi.moon.moonfobl.util.LoggerAccessor;

@Component
public class JwtIdentitaUtil implements Serializable {
	
	private static final String CLASS_NAME = "JwtIdentitaUtil";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
    private static final long serialVersionUID = -3301605591108950415L;
    
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

    private static final String SECRET = "CSI-MOOn-Monfobl!2020";
	private static final String ISSUER = "it.csi.moon."+Constants.COMPONENT_NAME;
	private static final SignatureAlgorithm ALG = SignatureAlgorithm.HS256;

//    private static final Long EXPIRATION = 60*30L; // 30min in seconds // SVIL
    private static final Long EXPIRATION = 3600*12L; // 12ore in seconds  // PROD

    
    public String getIdentificativoUtenteFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_ID_UTENTE);
    }

    public String getCodiceFiscaleFromToken(String token) {
        String codiceFiscale;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            codiceFiscale = (claims==null)?null:claims.getSubject();
        } catch (Exception e) {
            codiceFiscale = null;
        }
        return codiceFiscale;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            created = (claims==null)?null:new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
    
    public String getCodFiscFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_CF);
    }
    
    public String getLogonModeFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_LOGON_MODE);
    }
    
    public Integer getTipoDocumentoFromToken(String token) {
    	return getIntegerClaimFromToken(token, CLAIM_KEY_TIPO_DOCUMENTO);
    }
    
    public String getNumeroDocumentoFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_NUMERO_DOCUMENTO);
    }
    
    public String getDataRilascioDocumentoFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_DATA_RILASCIO_DOCUMENTO);
    }
    
	public String getCognomeFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_COGNOME);
    }
	
    public Integer getIdComuneFromToken(String token) {
    	return getIntegerClaimFromToken(token, CLAIM_KEY_ID_COMUNE);
    }
    
	public Long getIdModuloFromToken(String token) {
    	return getLongClaimFromToken(token, CLAIM_KEY_ID_MODULO);
    }
    
    public Long getIdEnteFromToken(String token) {
    	return getLongClaimFromToken(token, CLAIM_KEY_ID_ENTE);
    }
    
    public Long getIdAmbitoFromToken(String token) {
    	return getLongClaimFromToken(token, CLAIM_KEY_ID_AMBITO);
    }
    
    public String getPortaleNameFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_PORTAL_NAME);
    }
    
    public Integer getIdFruitoreFromToken(String token) {
    	return getIntegerClaimFromToken(token, CLAIM_KEY_ID_FRUITORE);
    }
    
    public String getIdFamigliaConvivenzaANPRFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR);
    } 
    
    public String getChiaveUnivocitaFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_CHIAVE_UNIVOCA);
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
    
	
    public UserInfo retreiveUserInfoFromToken(String token) throws BusinessException {
    	UserInfo result = new UserInfo();
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result.setIdFruitore(claims.get(CLAIM_KEY_ID_FRUITORE) != null ? ((Integer)claims.get(CLAIM_KEY_ID_FRUITORE)) : null);
            result.setIdentificativoUtente((String) claims.get(CLAIM_KEY_ID_UTENTE));
            result.setCodFiscDichIstanza((String) claims.get(CLAIM_KEY_CF));
            result.setCognome(decodeValue((String) claims.get(CLAIM_KEY_COGNOME)));
            result.setNome(decodeValue((String) claims.get(CLAIM_KEY_NOME)));          
            result.setIdMoonToken(token);
            result.setIdIride(decodeValue((String) claims.get(CLAIM_KEY_SHIB_IRIDE_IDENTITA)));
            result.setOperatore((boolean) claims.get(CLAIM_KEY_IS_OPERATORE));
            result.setGruppoOperatoreFo((String) claims.get(CLAIM_KEY_GRUPPO_OPERATORE_FO));
            result.setMultiEntePortale((boolean) claims.get(CLAIM_KEY_IS_MULTI_ENTE_PORTALE));            
            Ente ente = new Ente(); // mantenere il cast integer 
            ente.setIdEnte(claims.get(CLAIM_KEY_ID_ENTE) != null ? ((Integer)claims.get(CLAIM_KEY_ID_ENTE)).longValue(): null);
            result.setEnte(ente);
            result.setIdAmbito(claims.get(CLAIM_KEY_ID_AMBITO) != null ? ((Integer) claims.get(CLAIM_KEY_ID_AMBITO)): null);
            result.setDatiAggiuntivi(getDatiAggiuntiviFromClaims(claims));
            result.setPortalName((String) claims.get(CLAIM_KEY_PORTAL_NAME));
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::retreiveUserInfoFromToken] OUT result = " + result);
			}
	        return result;
        } catch (BusinessException be) {
        	LOG.warn("[" + CLASS_NAME + "::retreiveUserInfoFromToken] BusinessException "+be.getMessage());
        	throw be;
        } catch (Exception e) {
        	LOG.debug("[" + CLASS_NAME + "::retreiveUserInfoFromToken] Exception ", e);
        	throw new BusinessException("retreiveUserInfoFromToken");
        }
    }
    
    public LoginResponse retreiveLoginResponseFromToken(String token) throws BusinessException {
    	LoginResponse result = new LoginResponse(retreiveUserInfoFromToken(token));
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            Long idModulo = (Long) claims.get(CLAIM_KEY_ID_MODULO);
            if (idModulo!=null) {
            	Modulo modulo = new Modulo();
            	modulo.setIdModulo(idModulo);
            	result.setModulo(modulo);
            }
            if (LOG.isDebugEnabled()) {
    			LOG.fatal("[" + CLASS_NAME + "::retreiveLoginResponseFromToken] OUT result = " + result);
            }
            return result;
        } catch (BusinessException be) {
        	LOG.warn("[" + CLASS_NAME + "::retreiveLoginResponseFromToken] BusinessException "+be.getMessage());
        	throw be;
        } catch (Exception e) {
        	LOG.debug("[" + CLASS_NAME + "::retreiveLoginResponseFromToken] Exception ", e);
        	throw new BusinessException("retreiveLoginResponseFromToken"); 
        }
    }
    
    
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            expiration = (claims==null)?null:claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    public String getAudienceFromToken(String token) {
    	return getStringClaimFromToken(token, CLAIM_KEY_AUDIENCE);
    }

    
    private Claims getBobyClaimsFromToken(String token) throws BusinessException {
        Claims claims;
        try {
            Jws<Claims> allClaims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .requireIssuer(ISSUER)
                    .parseClaimsJws(token);
            if (!ALG.name().equals(allClaims.getHeader().getAlgorithm())) {
                LOG.fatal("[" + CLASS_NAME + "::getClaimsFromToken] allClaims.Header().getAlgorithm() = " + allClaims.getHeader().getAlgorithm());
            	throw new BusinessException("JWT without correct Algorithm.");
            }
            claims = allClaims.getBody();
            return claims;
        } catch (BusinessException be) {
        	LOG.warn("[" + CLASS_NAME + "::getClaimsFromToken] BusinessException " + be.getMessage());
        	throw be;
        } catch (Exception e) {
        	LOG.error("[" + CLASS_NAME + "::getClaimsFromToken] Exception " + e);
        	throw new BusinessException("JWT parser");
        }
    }

    
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRATION * 1000);
    }

    public Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate==null || expirationDate.before(new Date());
    }

    
    public String generateToken(LoginResponse userInfo/*, Device device*/) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, userInfo.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_UTENTE, userInfo.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_FRUITORE, userInfo.getIdFruitore());
        
        claims.put(CLAIM_KEY_CF, userInfo.getCodFiscDichIstanza());
        claims.put(CLAIM_KEY_COGNOME, userInfo.getCognome());
        claims.put(CLAIM_KEY_ID_MODULO, userInfo.getModulo().getIdModulo());
        claims.put(CLAIM_KEY_ID_ENTE, userInfo.getEnte().getIdEnte());
        claims.put(CLAIM_KEY_PORTAL_NAME, userInfo.getPortalName());
        claims.put(CLAIM_KEY_SHIB_IRIDE_IDENTITA, userInfo.getIdIride());
        claims.put(CLAIM_KEY_ID_AMBITO, userInfo.getIdAmbito());
        
        claims.put(CLAIM_KEY_IS_OPERATORE, userInfo.isOperatore());
        claims.put(CLAIM_KEY_GRUPPO_OPERATORE_FO, userInfo.getGruppoOperatoreFo());
        claims.put(CLAIM_KEY_IS_MULTI_ENTE_PORTALE, userInfo.isMultiEntePortale());

        return generateToken(claims);
    }

    
    public String generateToken(LoginRequest loginRequest, LoginResponse loginResponse/*, Device device*/) {
        return generateToken(initClaims(loginRequest, loginResponse));
    }
    public String generateToken(LoginRequest loginRequest, LoginResponse loginResponse, UlterioreDatiMoonToken ulterioreDatiMoonToken/*, Device device*/) throws JsonProcessingException {
        Map<String, Object> claims = initClaims(loginRequest, loginResponse);
        claims.put(CLAIM_KEY_ID_FAMIGLIA_CONVIVENZA_ANPR, ulterioreDatiMoonToken!=null?ulterioreDatiMoonToken.getIdFamigliaConvivenzaANPR():null);
        claims.put(CLAIM_KEY_CHIAVE_UNIVOCA, ulterioreDatiMoonToken!=null?ulterioreDatiMoonToken.getChiaveUnivocita():null);
        return generateToken(claims);
    }

	private Map<String, Object> initClaims(LoginRequest loginRequest, LoginResponse loginResponse/*, Device device*/) {
	  try {
		Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, loginResponse.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_UTENTE, loginResponse.getIdentificativoUtente());
        claims.put(CLAIM_KEY_ID_FRUITORE, loginResponse.getIdFruitore());
        
        claims.put(CLAIM_KEY_CF, loginResponse.getCodFiscDichIstanza());
        claims.put(CLAIM_KEY_COGNOME, encodeValue(loginResponse.getCognome()));
        claims.put(CLAIM_KEY_NOME, encodeValue(loginResponse.getNome()));
        claims.put(CLAIM_KEY_ID_MODULO, loginResponse.getModulo()==null?null:loginResponse.getModulo().getIdModulo());
        claims.put(CLAIM_KEY_ID_ENTE, loginResponse.getEnte()==null?null:loginResponse.getEnte().getIdEnte());
        claims.put(CLAIM_KEY_PORTAL_NAME, loginResponse.getPortalName());
        claims.put(CLAIM_KEY_SHIB_IRIDE_IDENTITA, encodeValue(loginResponse.getIdIride()));
        claims.put(CLAIM_KEY_ID_AMBITO, loginResponse.getIdAmbito()==null?null:loginResponse.getIdAmbito());
        
        claims.put(CLAIM_KEY_IS_OPERATORE, loginResponse.isOperatore());
        claims.put(CLAIM_KEY_GRUPPO_OPERATORE_FO, loginResponse.getGruppoOperatoreFo());
        claims.put(CLAIM_KEY_IS_MULTI_ENTE_PORTALE, loginResponse.isMultiEntePortale());
        
        claims.put(CLAIM_KEY_LOGON_MODE, loginRequest.getLogonMode());
        claims.put(CLAIM_KEY_TIPO_DOCUMENTO, loginRequest.getTipoDocumento());
        claims.put(CLAIM_KEY_NUMERO_DOCUMENTO, loginRequest.getNumeroDocumento());
        claims.put(CLAIM_KEY_DATA_RILASCIO_DOCUMENTO, loginRequest.getDataRilascioDocumento());
        
        DatiAggiuntivi da = null;
        try {
        	da = loginResponse.getDatiAggiuntivi();
			claims.put(CLAIM_KEY_DATI_AGGIUNTIVI, getMapper().writeValueAsString(da));
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::initClaims] datiAggiuntivi = " + da, e);
		}
        
		return claims;
	  } catch (UnsupportedEncodingException e) {
		LOG.error("[" + CLASS_NAME + "::initClaims] UnsupportedEncodingException loginResponse = " + loginResponse);
		throw new BusinessException();
	  }
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
	    return value!= null ? URLEncoder.encode(value, StandardCharsets.UTF_8.toString()):null;
	}
	private String decodeValue(String value) throws DecoderException {
	    return new URLCodec().decode(value);
	}
    
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setIssuer(ISSUER)
            .setExpiration(generateExpirationDate())
            .signWith(ALG, SECRET)
            .compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return  (!isTokenExpired(token) /*|| ignoreTokenExpiration(token)*/);
    }

    public String refreshToken(String token) {
        String refreshedToken = null;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            if (claims!=null) {
            	claims.put(CLAIM_KEY_CREATED, new Date());
            	refreshedToken = generateToken(claims);
            }
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public String writeIdEnte(String token, Long idEnte) {
        String refreshedToken = null;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            if (claims!=null) {
	            claims.put(CLAIM_KEY_ID_ENTE, idEnte);
	            claims.put(CLAIM_KEY_CREATED, new Date());
	            refreshedToken = generateToken(claims);
            }
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
			mapper = new ObjectMapper()
				.setSerializationInclusion(Include.NON_EMPTY)
				.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		}
		return mapper;
	}

	//
	private String getStringClaimFromToken(String token, String key) {
		String result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (String) claims.get(key);
        } catch (Exception e) {
        	result = null;
        }
        return result;
	}
	private Integer getIntegerClaimFromToken(String token, String key) {
		Integer result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = (Integer) claims.get(key);
        } catch (Exception e) {
        	result = null;
        }
        return result;
	}
	private Long getLongClaimFromToken(String token, String key) {
		Long result;
        try {
            final Claims claims = getBobyClaimsFromToken(token);
            result = Long.valueOf((Integer) claims.get(key));
        } catch (Exception e) {
        	result = null;
        }
        return result;
	}
}
