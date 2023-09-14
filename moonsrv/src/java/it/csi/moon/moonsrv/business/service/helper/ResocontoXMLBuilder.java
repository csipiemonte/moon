/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import it.csi.moon.commons.dto.ResocontoAllegato;

public class ResocontoXMLBuilder {

	private DocumentBuilderFactory documentFactory;
	private DocumentBuilder documentBuilder;
	private Document document;
	private Element root;
	
	// campi XML
	private String codiceIstanza;
	private String cognomeUtenteCompilante;
	private String nomeUtenteCompilante;
	private String cfUtenteCompilante;
	private String dataLogin;
	private String provider;
	private String nomePdfIstanza;
	private String hashPdfIstanza;
	private ArrayList<ResocontoAllegato> allegati;
	private String dataInvio;
	
	private enum ResocontoNodeNames {
		ROOT_NODE("RESOCONTO"),
		UTENTE_COMPILANTE_COGNOME_NODE("UTENTE-COMPILANTE-COGNOME"),
		UTENTE_COMPILANTE_NOME_NODE("UTENTE-COMPILANTE-NOME"),
		UTENTE_COMPILANTE_CF_NODE("UTENTE-COMPILANTE-CODICE-FISCALE"),
		DATA_LOGIN_NODE("DATA-LOGIN"),
		PROVIDER_NODE("PROVIDER"),
		NOME_PDF_ISTANZA_NODE("NOME-PDF-ISTANZA"),
		HASH_PDF_ISTANZA_NODE("HASH-PDF-ISTANZA"),
		ALLEGATI_NODE("ALLEGATI"),
		ALLEGATO_PDF_NODE("ALLEGATO"),
		NOME_ALLEGATO_PDF_NODE("NOME"),
		HASH_ALLEGATO_PDF_NODE("HASH"),
		DATA_INVIO_NODE("DATA-INVIO"),
		CODICE_ISTANZA_NODE("CODICE-ISTANZA");
		
		 private String name;
		 
		ResocontoNodeNames(String name) {
	        this.name = name;
	    }
	 
	    public String getName() {
	        return name;
	    }
	}
	
	public ResocontoXMLBuilder() throws Exception {
		  documentFactory = DocumentBuilderFactory.newInstance();
          documentBuilder = documentFactory.newDocumentBuilder();
          document = documentBuilder.newDocument();
          allegati = new ArrayList<>();
	}
	
	private Element createRootElement(String rootElementName) {      
		root = document.createElement(rootElementName);
		document.appendChild(root);
		return root;
	}
	
	private Element createElement(Element father, String elementName, String elementValue) {
		Element el = document.createElement(elementName);
		el.appendChild(document.createTextNode(elementValue));
		father.appendChild(el);
		return el;
	}
	
	
	public String buildXmlIndented() throws TransformerException  {
		buildXml();
		StringWriter sw = new StringWriter();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Compliant
		// ACCESS_EXTERNAL_SCHEMA not supported in several TransformerFactory implementations
        Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT,"yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(sw);
		transformer.transform(domSource, streamResult);
		return sw.toString();
	}
	
	
	private void buildXml() {

		root = createRootElement(ResocontoNodeNames.ROOT_NODE.getName());
        createElement(root, ResocontoNodeNames.CODICE_ISTANZA_NODE.getName(), "" + this.codiceIstanza);
        createElement(root, ResocontoNodeNames.UTENTE_COMPILANTE_COGNOME_NODE.getName(),"" +  this.cognomeUtenteCompilante);
        createElement(root, ResocontoNodeNames.UTENTE_COMPILANTE_NOME_NODE.getName(),"" +  this.nomeUtenteCompilante);
        createElement(root, ResocontoNodeNames.UTENTE_COMPILANTE_CF_NODE.getName(),"" +  this.cfUtenteCompilante);
        createElement(root, ResocontoNodeNames.PROVIDER_NODE.getName(), "" + this.provider);
        createElement(root, ResocontoNodeNames.DATA_LOGIN_NODE.getName(),"" +  this.dataLogin);
        createElement(root, ResocontoNodeNames.DATA_INVIO_NODE.getName(),"" +  this.dataInvio);
        createElement(root, ResocontoNodeNames.NOME_PDF_ISTANZA_NODE.getName(), "" + this.nomePdfIstanza);
        createElement(root, ResocontoNodeNames.HASH_PDF_ISTANZA_NODE.getName(), "" + this.hashPdfIstanza);
        // Verifico se presenti  allegati
        if (!allegati.isEmpty()) {
        	Element nodoAllegato = createElement(root, ResocontoNodeNames.ALLEGATI_NODE.getName(), "");
        	for (ResocontoAllegato resocontoAllegato : allegati) {
        		Element allegato = createElement(nodoAllegato, ResocontoNodeNames.ALLEGATO_PDF_NODE.getName(), "");
        		createElement(allegato, ResocontoNodeNames.NOME_ALLEGATO_PDF_NODE.getName(), "" + resocontoAllegato.getNomeAllegato());
        		createElement(allegato, ResocontoNodeNames.HASH_ALLEGATO_PDF_NODE.getName(), "" + resocontoAllegato.getHashAllegato());
			}
        }
		// Imposto nome nodi e valore
	}

	public String getCodiceIstanza() {
		return codiceIstanza;
	}

	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}

	

	public String getDataLogin() {
		return dataLogin;
	}

	public void setDataLogin(String dataLogin) {
		this.dataLogin = dataLogin;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getNomePdfIstanza() {
		return nomePdfIstanza;
	}

	public void setNomePdfIstanza(String nomePdfIstanza) {
		this.nomePdfIstanza = nomePdfIstanza;
	}

	public String getHashPdfIstanza() {
		return hashPdfIstanza;
	}

	public void setHashPdfIstanza(String hashPdfIstanza) {
		this.hashPdfIstanza = hashPdfIstanza;
	}


	public String getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(String dataInvio) {
		this.dataInvio = dataInvio;
	}

	public ArrayList<ResocontoAllegato> getAllegati() {
		return allegati;
	}

	public void setAllegati(ArrayList<ResocontoAllegato> allegati) {
		this.allegati = allegati;
	}
	
	public void addResocontoAllegato(ResocontoAllegato res) {
	 this.allegati.add(res);
	}

	public String getCognomeUtenteCompilante() {
		return cognomeUtenteCompilante;
	}

	public void setCognomeUtenteCompilante(String cognomeUtenteCompilante) {
		this.cognomeUtenteCompilante = cognomeUtenteCompilante;
	}

	public String getNomeUtenteCompilante() {
		return nomeUtenteCompilante;
	}

	public void setNomeUtenteCompilante(String nomeUtenteCompilante) {
		this.nomeUtenteCompilante = nomeUtenteCompilante;
	}

	public String getCfUtenteCompilante() {
		return cfUtenteCompilante;
	}

	public void setCfUtenteCompilante(String cfUtenteCompilante) {
		this.cfUtenteCompilante = cfUtenteCompilante;
	}
	
}
