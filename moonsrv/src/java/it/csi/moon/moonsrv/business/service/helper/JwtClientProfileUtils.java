/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * https://github.com/dario-frongillo/spring-jwt-auth-italiancoders
 */
package it.csi.moon.moonsrv.business.service.helper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import it.csi.moon.moonsrv.util.Constants;

/**
 * Classe di utility per la verifica del token jwt
 * Il jwt del fruitore va generato per verificare 
 * @author Danilo Mosca
 *
 * 
 */

@Component
public class JwtClientProfileUtils implements Serializable {
	
	private static final String CLASS_NAME = "JwtClientProfileUtils";
//	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
    private static final long serialVersionUID = -3301605591108950415L;

	static final String CLAIM_KEY_CODICE_FRUITORE = "cdf"; // codiceFruitore
	
    static final String CLAIM_KEY_CREATED = "iat";
    static final String CLAIM_KEY_SUBJECT = "sub";
    static final String CLAIM_KEY_AUDIENCE = "aud";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";
	private static final String ISSUER = "it.csi.moon." + Constants.COMPONENT_NAME;
    
    private String secret = "CSI-MOOn-SRV!2020";

//    private Long expiration = 60*30L;   // 30min in seconds
//    private Long expiration = 3600*10L; // 10ore in seconds
//    private Long expiration = 86400*30L; // 1 mese in seconds
//    private Long expiration = 365*24*3600L; // 1 anno in seconds
    
//    private Long expiration = 100*365*24*3600L; // 100 anni in seconds
    
    private Long EXP_100_ANNI = 100*365*24*3600L; // 100 anni in seconds
    
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims==null?null:new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }
   
    public String getCodiceFruitoreFromToken(String token) {
    	String codiceFruitore;
        try {
            final Claims claims = getClaimsFromToken(token);
            codiceFruitore = claims==null?null:(String) claims.get(CLAIM_KEY_CODICE_FRUITORE);
        } catch (Exception e) {
        	codiceFruitore = null;
        }
        return codiceFruitore;
    }
    
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims==null?null:(Date) claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = claims==null?null:(String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    
//    private Date generateExpirationDate() {
////        return null;  // FOR NEVER_EXPIRE
//        return new Date(System.currentTimeMillis() + expiration * 1000); // WITH EXPIRATION DATE
//    }
    
    private Date generateExpirationDate(Long expiration) {
//      return null;  // FOR NEVER_EXPIRE
    	
    if (expiration == null) return null;
    return new Date(System.currentTimeMillis() + expiration * 1000); // WITH EXPIRATION DATE
  }

    public boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
//        LOG.debug("[" + CLASS_NAME + "::isTokenExpired] expirationDate: "+expirationDate);
//        Boolean result = expirationDate==null || expirationDate.before(new Date()); // FOR EXPIRATION OBBLIGATORIA
        boolean result = expirationDate!=null && expirationDate.before(new Date()); // FOR EXPIRATION NON OBBLIGATORIA // FOR NEVER_EXPIRE
//        LOG.debug("[" + CLASS_NAME + "::isTokenExpired] result: "+result);
        return result;
    }

    public String generateToken(String codiceFruitore/*, Device device*/) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CODICE_FRUITORE, codiceFruitore);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, codiceFruitore); // codiceFruitore
        return generateToken(claims);
    }
    
    public String generateToken(String codiceFruitore, Long expiration) throws JsonProcessingException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CODICE_FRUITORE, codiceFruitore);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        claims.put(CLAIM_KEY_SUBJECT, codiceFruitore); // codiceFruitore
        return generateToken(claims,expiration);
    }

    private String generateToken(Map<String, Object> claims) {
//        ObjectMapper mapper = new ObjectMapper();

        String result = Jwts.builder()
                .setClaims(claims)
                .setIssuer(ISSUER)
                .setExpiration(generateExpirationDate(EXP_100_ANNI))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return result;
    }
    
    private String generateToken(Map<String, Object> claims,Long expiration) {
//        ObjectMapper mapper = new ObjectMapper();

        String result = Jwts.builder()
                .setClaims(claims)
                .setIssuer(ISSUER)
                .setExpiration(generateExpirationDate(expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return result;
    }


    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return  (!isTokenExpired(token) /*|| ignoreTokenExpiration(token)*/);
    }

    public String refreshToken(String token) {
        String refreshedToken = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims!=null) {
	            claims.put(CLAIM_KEY_CREATED, new Date());
	            refreshedToken = generateToken(claims);
            }
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, String codiceFruitore) {
    	String codiceFruitoreToken = getCodiceFruitoreFromToken(token);
        return (codiceFruitoreToken.equals(codiceFruitore) && !isTokenExpired(token));
    }


/* 
 * new methods
 */
    
    
    private Claims getDataClaimsFromToken(String token)  {
        Claims claims;
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token)
                    .getBody();
        return claims;
    }
    
    
    public boolean isTokenValid(String token) {
    	try {
			Claims claims = this.getDataClaimsFromToken(token);
			return !isTokenExpired(token);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException jwtEx ) {
				return false;
		} catch (SignatureException e) {
			return false;
		} 
        
    }
    
    
    public String toString(String token) {
    	if (token==null || token.isEmpty() || token.isBlank()) {
    		return "token null or empty";
    	}
    	final Claims claims = getClaimsFromToken(token);
	    StringBuilder sb = new StringBuilder();
    	if (claims!=null) {
		    sb.append("    codiceFruitore: ").append(claims.get(CLAIM_KEY_CODICE_FRUITORE)).append("\n");
		    sb.append("    created: ").append(new Date((Long) claims.get(CLAIM_KEY_CREATED))).append("\n");
		    sb.append("    expiration: ").append(claims.getExpiration()).append("\n");
		    sb.append("    subject: ").append(claims.getSubject()).append("\n");
		    sb.append("    issuer: ").append(claims.getIssuer()).append("\n");
		    sb.append("    issuedAt: ").append(claims.getIssuedAt()).append("\n");
		    sb.append("    audience: ").append(claims.getAudience()).append("\n");
		    sb.append("    id: ").append(claims.getId()).append("\n");
		    sb.append("    notBefore: ").append(claims.getNotBefore()).append("\n");
    	} else {
    		sb.append("token claims NULL !!!");
    	}
	    return sb.toString();
    }
    
}





