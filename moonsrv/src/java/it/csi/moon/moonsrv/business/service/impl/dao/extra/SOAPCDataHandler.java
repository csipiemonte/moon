/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import it.csi.moon.moonsrv.util.LoggerAccessor;

public class SOAPCDataHandler implements SOAPHandler<SOAPMessageContext> {

	private static final String CLASS_NAME = "SOAPCDataHandler";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();

	private static final String CDATA_TAG_NAME = "ProtocolloInStr";

	@Override
	public void close(MessageContext context) {
        // do nothing
	}

	@Override
	public boolean handleMessage(SOAPMessageContext soapMessage) {
	    try {
	      log(soapMessage, "PRE ");
	      SOAPMessage message = soapMessage.getMessage();
//	      boolean isOutboundMessage = (Boolean) soapMessage
//	        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

	      // is a request?
//	      if (isOutboundMessage) {
	        // build a CDATA NODE with the text in the root tag
	        Node cddata = (Node) message.getSOAPPart().createCDATASection(
	        	renameNodeProtoIn(message.getSOAPBody().getElementsByTagName(CDATA_TAG_NAME).item(0).getTextContent()));
//	            message.getSOAPBody().getFirstChild().getTextContent());

	        // add the CDATA's node at soap message
	        message.getSOAPBody().getElementsByTagName(CDATA_TAG_NAME).item(0).appendChild(cddata);

	        // remove the text tag with the raw text that will be escaped
	        message.getSOAPBody().getElementsByTagName(CDATA_TAG_NAME).item(0)
	            .removeChild(message.getSOAPBody().getElementsByTagName(CDATA_TAG_NAME).item(0).getFirstChild());
//	      }

	        log(soapMessage, "POST ");
	    } catch (Exception ex) {
	      // fail
	    }
	    return true;
	  }

	  private String renameNodeProtoIn(String protocolloInStr) {
		  String result = new String(protocolloInStr);
//			result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
//			result = result.replace("<protocolloIn xmlns:ns2=\"http://tempuri.org/\">", "<tem:ProtocolloInStr><![CDATA[<ProtoIn>");
//			result = result.replace("</protocolloIn>", "</ProtoIn>]]></tem:ProtocolloInStr>");
			result = result.replace("<protocolloIn xmlns:ns2=\"http://tempuri.org/\">", "<ProtoIn>");
			result = result.replace("</protocolloIn>", "</ProtoIn>");
			result = result.replace("ns2:", "");
		return result;
	}

	@Override
	  public boolean handleFault(SOAPMessageContext soapMessage) {
		// allow any other handler to execute
	    return true;
	  }

	  @Override
	  public Set<QName> getHeaders() {
	    return Collections.EMPTY_SET;
	  }
	  
		protected void log(SOAPMessageContext smc, String prefisso) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				smc.getMessage().writeTo(out);
				LOG.info("[" + CLASS_NAME + "::log] " + prefisso + out.toString());
			} catch (IOException ioe) {
				LOG.error("[" + CLASS_NAME + "::log] IOException ", ioe);
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::log] Exception ", e);
			}
		}
}
