/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.AreaEntity;
import it.csi.moon.commons.entity.AreaModuloEntity;
import it.csi.moon.commons.entity.EnteEntity;
import it.csi.moon.commons.entity.UtenteEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.AreaModuloDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.EnteDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonsrv.util.LoggerAccessor;

public class StrReplaceHelper {

	private static final String CLASS_NAME = "StrReplaceHelper";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdf_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private DatiIstanzaHelper datiIstanzaHelper;
	
	@Autowired
	EnteDAO enteDAO;
	@Autowired
	AreaDAO areaDAO;
	@Autowired
	AreaModuloDAO areaModuloDAO;
	@Autowired
	UtenteDAO utenteDAO;
	
	public StrReplaceHelper(Istanza istanza) {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		datiIstanzaHelper = new DatiIstanzaHelper();
		datiIstanzaHelper.initDataNode(istanza);
	}

	public String replaceDinamici(String textValue, Istanza istanza) {
		return replaceDinamici(textValue, istanza, Optional.empty());
	}
	
	/**
	 * Replace dinamici usato per  oggetto, title, description
	 *
	 * @param textValue
	 *
	 * @return textValue con replace effettuati
	 */
	public String replaceDinamici(String textValue, Istanza istanza, Optional<AllegatoEntity> allegato) {
		if (textValue==null)
			return null;
    	LOG.debug("[" + CLASS_NAME + "::replaceDinamici] IN" + textValue);
		String result = textValue;
		result = replaceIstanzaAttributes(istanza, allegato, result);
		result = replaceIstanzaJsonKeys(result);
    	LOG.debug("[" + CLASS_NAME + "::replaceDinamici] END " + result);
		return result;
	}

	protected String replaceIstanzaAttributes(Istanza istanza, Optional<AllegatoEntity> allegato, String result) {
		if (result.contains("@@")) {
			// User
			result = replaceIstanzaAttributesGeneralita(istanza, result);
			result = replaceIstanzaAttributesGeneralitaAltre(istanza, result);
			//
			result = result.replace("@@ATTORE_INS@@", istanza.getAttoreIns()!=null?istanza.getAttoreIns().toUpperCase():"");
			result = replaceIstanzaAttributesContoTerzi(istanza, result);
			result = replaceIstanzaAttributesEnteAreaModulo(istanza, result);
			// Istanza
			result = result.replace("@@ID_ISTANZA@@", istanza.getIdIstanza().toString());
			result = result.replace("@@CODICE_ISTANZA@@", istanza.getCodiceIstanza());
			result = result.replace("@@CREATED@@|YYYY-MM-DD", istanza.getCreated()==null?"":sdf_YYYY_MM_DD.format(istanza.getCreated()));
			result = result.replace("@@CREATED@@", istanza.getCreated()==null?"":sdf.format(istanza.getCreated()));
			result = result.replace("@@MODIFIED@@", istanza.getModified()==null?"":sdf.format(istanza.getModified()));
			result = replaceIstanzaAttributesAllegati(allegato, result);
		}
		return result;
	}

	protected String replaceIstanzaAttributesGeneralita(Istanza istanza, String result) {
		result = result.replace("@@CODICE_FISCALE@@", istanza.getCodiceFiscaleDichiarante()!=null?istanza.getCodiceFiscaleDichiarante():"");
		result = result.replace("@@Cognome@@", istanza.getCognomeDichiarante()!=null?WordUtils.capitalize(istanza.getCognomeDichiarante()):"");
		result = result.replace("@@Nome@@", istanza.getNomeDichiarante()!=null?WordUtils.capitalize(istanza.getNomeDichiarante()):"");
		result = result.replace("@@COGNOME@@", istanza.getCognomeDichiarante()!=null?istanza.getCognomeDichiarante().toUpperCase():"");
		result = result.replace("@@NOME@@", istanza.getNomeDichiarante()!=null?istanza.getNomeDichiarante().toUpperCase():"");
		return result;
	}

	protected String replaceIstanzaAttributesGeneralitaAltre(Istanza istanza, String result) {
		result = result.replace("@@NOME_COGNOME@@", String.join(" ", 
				istanza.getNomeDichiarante()!=null?istanza.getNomeDichiarante().toUpperCase():"",
				istanza.getCognomeDichiarante()!=null?istanza.getCognomeDichiarante().toUpperCase():""));
		result = result.replace("@@NOME-COGNOME@@", String.join("-", 
				istanza.getNomeDichiarante()!=null?istanza.getNomeDichiarante().toUpperCase():"",
				istanza.getCognomeDichiarante()!=null?istanza.getCognomeDichiarante().toUpperCase():""));
		result = result.replace("@@COGNOME_NOME@@", String.join(" ", 
				istanza.getCognomeDichiarante()!=null?istanza.getCognomeDichiarante().toUpperCase():"",
				istanza.getNomeDichiarante()!=null?istanza.getNomeDichiarante().toUpperCase():""));
		result = result.replace("@@COGNOME-NOME@@", String.join("-", 
				istanza.getCognomeDichiarante()!=null?istanza.getCognomeDichiarante().toUpperCase():"",
				istanza.getNomeDichiarante()!=null?istanza.getNomeDichiarante().toUpperCase():""));
		return result;
	}

	protected String replaceIstanzaAttributesContoTerzi(Istanza istanza, String result) {
		if (result.contains("_CONTO_TERZI@@")) {
			UtenteEntity operatore = null;
			if (istanza.getAttoreIns()!=null && !istanza.getAttoreIns().equals(istanza.getCodiceFiscaleDichiarante())) {
				operatore = utenteDAO.findByIdentificativoUtente(istanza.getAttoreIns());
			}
			result = _replaceIstanzaAttributesContoTerziOperatore(result, operatore);
		}
		return result;
	}

	private String _replaceIstanzaAttributesContoTerziOperatore(String result, UtenteEntity operatore) {
		if (operatore!=null) {
			result = result.replace("@@ATTORE_INS_CONTO_TERZI@@", operatore.getIdentificativoUtente()!=null?operatore.getIdentificativoUtente().toUpperCase():"");
			result = result.replace("@@Cognome_CONTO_TERZI@@", operatore.getCognome()!=null?WordUtils.capitalize(operatore.getCognome()):"");
			result = result.replace("@@Nome_CONTO_TERZI@@", operatore.getNome()!=null?WordUtils.capitalize(operatore.getNome()):"");
			result = result.replace("@@COGNOME_CONTO_TERZI@@", operatore.getCognome()!=null?operatore.getCognome().toUpperCase():"");
			result = result.replace("@@NOME_CONTO_TERZI@@", operatore.getNome()!=null?operatore.getNome().toUpperCase():"");
		}
		return result;
	}

	protected String replaceIstanzaAttributesEnteAreaModulo(Istanza istanza, String result) {
		// Ente
		result = result.replace("@@ID_ENTE@@", istanza.getIdEnte().toString());
		if (result.contains("_ENTE@@")) {
			EnteEntity ente = enteDAO.findById(istanza.getIdEnte());
			result = result.replace("@@CODICE_ENTE@@", ente.getCodiceEnte());
			result = result.replace("@@NOME_ENTE@@", ente.getNomeEnte());
			result = result.replace("@@DESCRIZIONE_ENTE@@", ente.getDescrizioneEnte());
			result = result.replace("@@LOGO_ENTE@@", ente.getLogo());
			result = result.replace("@@CODICE_IPA_ENTE@@", ente.getCodiceIpa());
		}
		// Area
		if (result.contains("_AREA@@")) {
			AreaModuloEntity areaModulo = areaModuloDAO.findByIdModuloEnte(istanza.getModulo().getIdModulo(), istanza.getIdEnte());
			AreaEntity area = areaDAO.findById(areaModulo.getIdArea());
			result = result.replace("@@CODICE_AREA@@", area.getCodiceArea());
			result = result.replace("@@NOME_AREA@@", area.getNomeArea());
		}
		// Modulo
		result = result.replace("@@ID_MODULO@@", istanza.getModulo().getIdModulo().toString());
		result = result.replace("@@CODICE_MODULO@@", istanza.getModulo().getCodiceModulo());
		result = result.replace("@@ID_MODULO_VERSIONE@@", istanza.getModulo().getIdVersioneModulo().toString());
		result = result.replace("@@VERSIONE_MODULO@@", istanza.getModulo().getVersioneModulo());
		result = result.replace("@@OGGETTO_MODULO@@", istanza.getModulo().getOggettoModulo());
		result = result.replace("@@DESCRIZIONE_MODULO@@", istanza.getModulo().getDescrizioneModulo());
		return result;
	}

	protected String replaceIstanzaAttributesAllegati(Optional<AllegatoEntity> allegato, String result) {
		// Allegato
		if (allegato.isPresent() && result.contains("_ALLEGATO@@")) {
			AllegatoEntity a = allegato.get();
			result = result.replace("@@ID_ALLEGATO@@", a.getIdAllegato().toString());
			result = result.replace("@@CODICE_FILE_ALLEGATO@@", a.getCodiceFile());
			result = result.replace("@@NOME_FILE_ALLEGATO@@", a.getNomeFile());
			result = result.replace("@@FORMIO_NAME_FILE_ALLEGATO@@", a.getFormioNameFile());
		}
		return result;
	}

	protected String replaceIstanzaJsonKeys(String result) {
		if (result.contains("##")) {
			List<String> keysFormated = extractByHashSign(result);
			
	        for(String keyFormated: keysFormated) {
	        	String value = "";
				String formioType = "Text";
	        	String key = keyFormated;
	        	if (keyFormated.contains("!")) {
	        		key = keyFormated.split("!")[0];
	        		formioType = keyFormated.split("!")[1];
	        	}
	        	value = datiIstanzaHelper.extractedTextValueFromDataNodeByKey(key, formioType);
	        	result = result.replace("##"+keyFormated+"##", value != null ? value : "");
	        }
		}
		return result;
	}
	
	private List<String> extractByHashSign(String str)
    {
		String regex = "\\##(.*?)\\##";
        Pattern p = Pattern.compile(regex); 
        Matcher m = p.matcher(str);
 
        List<String> arrlist = new ArrayList<>();
        while (m.find()) {
       	
        	arrlist.add(m.group(1));
        }
        
        return arrlist;        
    }
	
//	private DatiIstanzaHelper getDatiIstanzaHelper() {
//		if (datiIstanzaHelper==null) {
//			datiIstanzaHelper = new DatiIstanzaHelper();
//			datiIstanzaHelper.initDataNode(istanza);
//		}
//		return datiIstanzaHelper;
//	}

}
