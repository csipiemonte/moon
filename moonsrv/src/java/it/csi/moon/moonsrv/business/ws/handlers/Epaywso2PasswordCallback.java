/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.ws.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.log4j.Logger;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import it.csi.moon.moonsrv.util.LoggerAccessor;

public class Epaywso2PasswordCallback implements CallbackHandler {

	private static final String CLASS_NAME = "Epaywso2PasswordCallback";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
    private Map<String, String> passwords = new HashMap<>();

    public Epaywso2PasswordCallback() {
        super();
        // ...
    }
    
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
//        	if (callbacks[i] instanceof WSPasswordCallback) {
	            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];
	
	            String pass = passwords.get(pc.getIdentifier());
	            LOG.info("[" + CLASS_NAME + "::handle] setPassword for identifier: " + pc.getIdentifier());
	            if (pass != null) {
	                pc.setPassword(pass);
	                return;
	            }
//        	} else {
//        		throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
//    	    }
        }
	}

    /**
     * Add an alias/password pair to the callback mechanism.
     */
    public void setAliasPassword(String alias, String password) {
        passwords.put(alias, password);
    }
    
}
