/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.impl;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.maggioli.prt.cxfclient.Protocollo;
import com.maggioli.prt.cxfclient.ProtocolloIn;
import com.maggioli.prt.cxfclient.ProtocolloOut;
import com.maggioli.prt.cxfclient.ProtocolloSoap;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.SOAPCDataHandler;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.SOAPLoggingHandler;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.MaggioliProtocolloSoapDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.protocollo.dto.maggioli.soap.InserisciDocumentoRequest;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class MaggioliProtocolloSoapDAOImpl implements MaggioliProtocolloSoapDAO {

	private static final String CLASS_NAME = "MaggioliProtocolloSoapDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	
	private ProtocolloSoap delegate; //getDelegate();

	private String getUrlEndPoint() {
		return EnvProperties.readFromFile(EnvProperties.MAGGIOLI_PRT_SOAP_ENDPOINT);
	}

	private ProtocolloSoap getDelegate() {
		try {
			 if(delegate != null) return delegate;
			 URL wsdl = new URL(getUrlEndPoint());
			 Protocollo streamService = new Protocollo(wsdl);
			 delegate = streamService.getProtocolloSoap(); // = streamService.getPort(new QName("http://tempuri.org/", "ProtocolloSoap"), ProtocolloSoap.class);

//			 delegate.setHandlerResolver(HandlerFactory.build(new SOAPLoggingHandler(), new CDataHandler()));
			 
//			 SOAPLoggingHandler.addSOAPLogger((BindingProvider)delegate);
			 Binding binding = ((BindingProvider)delegate).getBinding();
			 List<Handler> handlerChain = binding.getHandlerChain();
			 handlerChain.add(new SOAPLoggingHandler());
			 handlerChain.add(new SOAPCDataHandler());
			 binding.setHandlerChain(handlerChain);
			 			 
//			 org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(delegate);
			 
//			 org.apache.cxf.jaxws.JaxWsClientProxy client = ((org.apache.cxf.jaxws.JaxWsClientProxy)delegate).getClient();
//			 client.getOutInterceptors().add(new CustomSoapCxfInterceptor());

//			((BindingProvider)delegate).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, MAGGIOLI_PRT_SOAP_BASIC_AUTH_USERNAME_PROPERTY);
//			((BindingProvider)delegate).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, MAGGIOLI_PRT_SOAP_BASIC_AUTH_PASSWORD_PROPERTY);
			 return delegate;
		} catch (MalformedURLException e) {
			LOG.error("[" + CLASS_NAME + "::getDelegate] Errore URL: " + getUrlEndPoint());
			throw new DAOException();
		}
	}

	@Deprecated
	@Override
	public String inserisciDocumentoEAnagraficheString(InserisciDocumentoRequest req) {
        long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::inserisciDocumentoEAnagraficheString] BEGIN");
//			String result = getDelegate().inserisciDocumentoEAnagraficheString(convertCDATA(req.protocolloIn), req.codiceAmministrazione, req.codiceAOO);
			String result = getDelegate().inserisciDocumentoEAnagraficheString(convertString(req.protocolloIn), req.codiceAmministrazione, req.codiceAOO);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::inserisciDocumentoEAnagraficheString] Exception: ", e);
			throw new DAOException(e);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("inserisciDocumentoEAnagraficheString() in " + sec + " milliseconds.");
        }
	}
	
	@Override
	public ProtocolloOut inserisciProtocolloEAnagraficheString(InserisciDocumentoRequest req) {
        long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::inserisciProtocolloEAnagraficheString] BEGIN");
//			String result = getDelegate().inserisciProtocolloEAnagraficheString(convertCDATA(req.protocolloIn), req.codiceAmministrazione, req.codiceAOO);
			String xmlResponse = getDelegate().inserisciProtocolloEAnagraficheString(convertString(req.protocolloIn), req.codiceAmministrazione, req.codiceAOO);
			ProtocolloOut result = readResponseString(xmlResponse);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::inserisciProtocolloEAnagraficheString] Exception: ", e);
			throw new DAOException(e);
		} finally {
            long end = System.currentTimeMillis();
            float sec = (end - start); 
            LOG.info("inserisciProtocolloEAnagraficheString() in " + sec + " milliseconds.");
        }
	}
	

	public static String convertString(ProtocolloIn protocolloIn) {
		StringWriter stringWriter = new StringWriter();
        JAXB.marshal(protocolloIn, stringWriter);
        String result = stringWriter.toString();
		LOG.info("[" + CLASS_NAME + "::convertString] result: " + result);
		return result;
	}
	private ProtocolloOut readResponseString(String xmlResponse) throws BusinessException {
//		try {
			ProtocolloOut result = null;
			if (xmlResponse!=null) {
				LOG.info("[" + CLASS_NAME + "::readResponseString] xmlResponse=" + xmlResponse);
				// TODO
//				JAXBContext jaxbContext = JAXBContext.newInstance( ProtocolloOut.class );
//				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//				result = (ProtocolloOut) jaxbUnmarshaller.unmarshal(new StringReader(xmlResponse));
				result = new ProtocolloOut();
				LOG.info("[" + CLASS_NAME + "::readResponseString] result: " + result);
			} else {
				LOG.warn("[" + CLASS_NAME + "::readResponseString] xmlResponse NULL from maggioliSoapProtocolloDAO !");
			}
			return result;
//		} catch (JAXBException jaxbe) {
//			LOG.error("[" + CLASS_NAME + "::readResponseString] JAXBException ", jaxbe);
//			throw new BusinessException("ERRORE LETTURE RESPONSE MAGGIOLI");
//		}
	}
}
