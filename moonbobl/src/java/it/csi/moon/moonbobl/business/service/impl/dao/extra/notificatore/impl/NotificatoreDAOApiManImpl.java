/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.notificatore.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.component.ApiManagerInternetTemplateImpl;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.ApimintResponse;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.Message;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.Token;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.notificatore.NotificatoreDAO;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;

/**
*
* @author 
* clase per accedere servizio demografia attraverso APIMINT
*/
@Component
@Qualifier("orchanprApiManagerInternetRS")
public class NotificatoreDAOApiManImpl extends ApiManagerInternetTemplateImpl implements NotificatoreDAO {
	
	private final static String CLASS_NAME = "NotificatoreDAOApiManImpl";
    /*
     * Parametri che servono
     */
    private String consumerKey;
    private String consumerSecret;
    private String userToken;
    private String clientProfile;
    
    
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	@Override
	public String inviaNotifica(Message message) throws DAOException {
		
        long start = System.currentTimeMillis();

        try {
        	log.debug("[" + CLASS_NAME + "::inviaNotifica] BEGIN");
        	
        	String endpoint = getEndpoint() + "/notify-mb/api/v1/topics/messages";        	
        	String consumerKey = "VAK_2oXUAYJSxl3C2jkxRMFH5qga";
   	     	String consumerSecret = "dl6RbKtSxQlxPdeU1VmLvgeIOgQa";
   	        String userToken = "eyJhbGciOiJSUzI1NiJ9.eyJTaGliLUlkZW50aXRhLUNvZGljZUZpc2NhbGUiOiJDUlpOR0w3MVQ0MkY5NzlYIiwiZXhwIjoxNTkwNjk5NzQ2LCJzdWIiOiJHQVNQQ09UTyIsImVtYWlsIjoiYW5nZWxhLmNhcnplZGRhQGdtYWlsLmNvbSIsIm5hbWUiOiJBbmdlbGEiLCJmYW1pbHlOYW1lIjoiQ2FyemVkZGEiLCJhdWQiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJpc3MiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJqdGkiOiJfOWNjN2VlYzUtYjc1MC00ZTM4LWJmMWUtNGUzZDIwZWMyNmIyIn0.oaPoq4rf2ElmgMFshKD0WElHXssVmDDVY8CatgJWNwWYKikII3WuhjLmD6p4-tNo-6orE6ubVuCTsj3-uLfENpe6MLnIUPnjyxq1GBeHCka8KCNlta_LKHgzzuLM293KYjprdot4LwziZJmIdnT1hMwk4mpxrk7acKJHe7mT0NrHQkNaCa4jFsFcd23tKllkNFuVUwQfaIPUFxEG5UjS8Qk9LZIKRV3bKBiwkydN3AOO3u77T0bDOIyvn2ym80csA2QuXAUMLQ6ddYSqpGsqu1hH78KRpl1V4HjujTVU2GzwGv3HbQ9VY_jsKii4z0LLDkA6asvWEDaZKV1Bn9ixug";
            String clientProfileCI = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJjb2RfYXBwbGljYXRpdm8iOiJNT09OIiwiZW50ZSI6IkNPVE8iLCJpc3MiOiJDU0kuT1JDSEFOUFIiLCJwcm9maWxvIjoiTU9PTl9DIn0.fJYJ_mcYFmjDbNHuKR3sYjOknUo_5hngf8dLackIOj6FTu6au6LKWAz9JQw0kvV_g_FTThoyDZjlvgQzBBFwBw";
         
        	Token token = getTokenAuthorization(consumerKey, consumerSecret);
        	
        	ApimintResponse rispostaNotificatore = invokeApiMintService(endpoint, token.getAccess_token(), userToken, clientProfileCI, message);

    	    return rispostaNotificatore.getRispostaRequest();
    	        
        } catch(Exception e) {
	    	e.printStackTrace();
			log.error("[" + CLASS_NAME + "::inviaNotifica] Errore generico servizio getFamigliaANPR", e);
			throw new ServiceException("Errore generico servizio getFamigliaANPR");
	    } finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            log.info("[" + CLASS_NAME + "::inviaNotifica] END of ("+message.getUuid()+") in " + sec + " milliseconds.");
        }
    }

	
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getClientProfile() {
		return clientProfile;
	}
	public void setClientProfile(String clientProfile) {
		this.clientProfile = clientProfile;
	}
	
	
	
}
