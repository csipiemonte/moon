/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.util.StrUtils;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
//import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.Message;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;


@Component
public class MoonsrvDAOImpl extends MoonsrvTemplateImpl implements MoonsrvDAO {
	
	protected static final String CLASS_NAME = "MoonsrvDAOImpl";
	
	@Override
	public String pingMoonsrv() throws DAOException {
		return getMoonsrv("/cmd/ping");
	}

	@Override
	public List<Modulo> getModuli(ModuliFilter filter) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::getModuli] BEGIN");
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			Optional<Long> longOpt = Optional.empty();
			Optional<String> strOpt = Optional.empty();
			Optional<Integer> intOpt = Optional.empty();
			longOpt = filter.getIdModulo();
			if(longOpt.isPresent()) {
				queryParams.add("idModulo", longOpt.get());
			}
			strOpt = filter.getCodiceModulo();
			if(strOpt.isPresent()) {
				queryParams.add("codiceModulo", strOpt.get());
			}
			strOpt = filter.getVersioneModulo();
			if(strOpt.isPresent()) {
				queryParams.add("versioneModulo", strOpt.get());
			}
			strOpt = filter.getOggettoModulo();
			if(strOpt.isPresent()) {
				queryParams.add("oggettoModulo", strOpt.get());
			}
			strOpt = filter.getDescrizioneModulo();
			if(strOpt.isPresent()) {
				queryParams.add("descrizioneModulo", strOpt.get());
			}
			strOpt = filter.getConPresenzaIstanzeUser();
			if(strOpt.isPresent()) {
				queryParams.add("conPresenzaIstanzeUser", strOpt.get()); // filterUser delle istanze presente
			} else {
				queryParams.add("stato", DecodificaStatoModulo.PUBBLICATO.getCodice()); // Tutti moduli Pubblicati
			}
			strOpt = filter.getNomePortale();
			if(strOpt.isPresent()) {
				queryParams.add("nomePortale", strOpt.get());
			}
			longOpt = filter.getIdEnte();
			if(longOpt.isPresent()) {
				queryParams.add("idEnte", longOpt.get());
			}
			intOpt = filter.getIdAmbito();
			if(intOpt.isPresent()) {
				queryParams.add("idAmbito", intOpt.get());
			}
			intOpt = filter.getIdVisibilitaAmbito();
			if(intOpt.isPresent()) {
				queryParams.add("idVisibilitaAmbito", intOpt.get());
			}
			return List.of(getMoonsrvJson("/moduli", Modulo[].class, queryParams));
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getModuli] END in " + sec + "milliseconds.");
			}
		}
	}

//	@Override
//	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws DAOException {
//		long start = System.currentTimeMillis();
//		try {
//			LOG.debug("[" + CLASS_NAME + "::getModuloById] BEGIN");
//			return getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo, Modulo.class);
//		} finally {
//			if (LOG.isDebugEnabled()) {
//				long end = System.currentTimeMillis();
//				float sec = (end - start); 
//				LOG.debug("[" + CLASS_NAME + "::getModuloById] END in " + sec + "milliseconds.");
//			}
//		}
//	}
	
	@Override
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fields) throws DAOException {	
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::getModuloById] BEGIN");
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			if(!StrUtils.isEmpty(fields)) {
				queryParams.add("fields", fields);
			}
			return getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo, Modulo.class, queryParams);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getModuloById] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getModuloByCodice] BEGIN codiceModulo: " + codiceModulo + " versione: " + versione);
			return getMoonsrvJson("/moduli/codice/"+codiceModulo+"/versione/"+versione, Modulo.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getModuloByCodice] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] BEGIN codiceModulo: " + codiceModulo);
			return getMoonsrvJson("/moduli/codice/"+codiceModulo+"/pubblicato", Modulo.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Istanza initIstanza(String ipAddress, UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params) throws ItemNotFoundDAOException, DAOException {
		assert user!=null : "User non presente.";
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::initIstanza] BEGIN");
			IstanzaInitParams initParams = new IstanzaInitParams();
			initParams.setIpAdress(ipAddress);
			initParams.setIdModulo(idModulo);
			initParams.setIdVersioneModulo(idVersioneModulo);
			initParams.setCodiceFiscale(user.getCodFiscDichIstanza());
			initParams.setCognome(user.getCognome());
			initParams.setNome(user.getNome());
			initParams.setUserInfo(user);
			initParams.setBlParams(params);
			//I servizi del Notificatore necessitano dell'identità iride in inizializzazione
			initParams.setShibIrideIdentitaDigitale(user.getIdIride());
			LOG.debug("[" + CLASS_NAME + "::initIstanza] initParams = " + initParams);
			return postMoonsrvJson("/istanze/init", Istanza.class, initParams);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::initIstanza] END in " + sec + "milliseconds.");
			}
		}
	}
		
	@Override
	public String generaPdf(UserInfo user, Long idIstanza) throws DAOException {
		assert user!=null : "User non presente.";
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::generaPdf] BEGIN");

			return postMoonsrvJson("/istanze/"+ idIstanza + "/generaPdf", String.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::generaPdf] END in " + sec + "milliseconds.");
			}
		}
	}
	@Override
	public byte[] getPdfByIdIstanza(UserInfo user, Long idIstanza) throws DAOException {
		assert user!=null : "User non presente.";
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::getPdfByIdIstanza] BEGIN");
			return getMoonsrvBytes("/istanze/"+ idIstanza + "/pdf");
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getPdfByIdIstanza] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public byte[] getNotificaByIdIstanza(UserInfo user, Long idIstanza) throws DAOException {
		assert user!=null : "User non presente.";
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getNotificaByIdIstanza] BEGIN idIstanza: " + idIstanza);
			return getMoonsrvBytes("/istanze/"+ idIstanza + "/notifica");
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getNotificaByIdIstanza] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public byte[] getDocumentoByFormioNameFile(UserInfo user, String formioNameFile) throws DAOException {
		assert user!=null : "User non presente.";
		assert formioNameFile!=null : "formioNameFile non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] BEGIN formioNameFile: " + formioNameFile);			
			return getMoonsrvBytes("/istanze/documenti/by-name/"+ formioNameFile);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getDocumentoByFormioNameFile] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public byte[] getDocumentoByIdFile(UserInfo user, Long idFile) throws DAOException {
		assert user!=null : "User non presente.";
		assert idFile!=null : "idFile non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getDocumentoByIdFile] BEGIN idFile: " + idFile);
			return getMoonsrvBytes("/istanze/documenti/by-id/"+ idFile);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getDocumentoByIdFile] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Documento getDocumentoNotificaByIdIstanza(UserInfo user, Long idIstanza) throws DAOException {
		assert user!=null : "User non presente.";
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getDocumentoNotificaByIdIstanza] BEGIN idIstanza: " + idIstanza);
			return getMoonsrvJson("/istanze/"+ idIstanza + "/documento-notifica",Documento.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getDocumentoNotificaByIdIstanza] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String sendEmail(EmailRequest request) throws DAOException {
		assert request!=null : "EmailRequest non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.debug("[" + CLASS_NAME + "::sendEmail] BEGIN");
			return postMoonsrvJson("/email/text", String.class, request);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::sendEmail] END in " + sec + "milliseconds.");
			}
		}
	}


	@Override
	public String protocolla(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::protocolla] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/protocolla/"+idIstanza, String.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::protocolla] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public String protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		assert idStoricoWorkflow!=null : "idStoricoWorkflow non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::protocollaIntegrazione] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/"+idIstanza+"/protocolla-integrazione/"+idStoricoWorkflow, String.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::protocollaIntegrazione] END in " + sec + "milliseconds.");
			}
		}
	}


	@Override
	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/creaPraticaEdAvviaProcesso/"+idIstanza, String.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String inviaRispostaIntegrazioneCosmo(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/"+idIstanza+"/integrazione-cosmo", String.class);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws DAOException {
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getCampiModulo] BEGIN idModulo: " + idModulo);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			if(!StrUtils.isEmpty(type)) {
				queryParams.add("type", type);
			}
			return List.of(getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo+"/campi", CampoModulo[].class, queryParams));
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getCampiModulo] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public List<CampoModulo> getCampiDatiAzione(Long idDatiAzione, String type) throws DAOException {
		assert idDatiAzione!=null : "idDatiAzione non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getCampiDatiAzione] BEGIN idDatiAzione: " + idDatiAzione + " type: " + type);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			if(!StrUtils.isEmpty(type)) {
				queryParams.add("type", type);
			}
			return List.of(getMoonsrvJson("/moduli/dati-azione/"+idDatiAzione+"/campi", CampoModulo[].class, queryParams));
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getCampiDatiAzione] END in " + sec + "milliseconds.");
			}
		}
	}
	/*
	@Override
	public String inviaMessaggioNotifica(Message message) throws DAOException {
		assert message!=null : "message non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::inviaMessaggioNotifica] BEGIN");
			return postMoonsrvJson("/extra/notificatore/topics/messages", String.class, message);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::inviaMessaggioNotifica] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public ContactPreference getContacts(String identitaDigitale, String codiceFiscale) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getContacts] BEGIN codiceFiscale: " + codiceFiscale);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			queryParams.add("identita_digitale", identitaDigitale);
			queryParams.add("codice_fiscale", codiceFiscale);
			return getMoonsrvJson("/extra/notificatore/preferences/contacts", ContactPreference.class, queryParams);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getContacts] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public List<Service> getServices(String identitaDigitale) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getServices] BEGIN identitaDigitale: " + identitaDigitale);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			queryParams.add("identita_digitale", identitaDigitale);
			return List.of(getMoonsrvJson("/extra/notificatore/preferences/services", Service[].class,queryParams));
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getServices] END in " + sec + "milliseconds.");
			}
		}
	}
	
	
	@Override
	public Status getStatus(String idMessaggio) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getStatus] BEGIN idMessaggio: " + idMessaggio);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			queryParams.add("id_messaggio", idMessaggio);
			return getMoonsrvJson("/extra/notificatore/messages/status", Status.class,queryParams);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getStatus] END in " + sec + "milliseconds.");
			}
		}
	}
	*/	
	@Override
	public String creaTicketCrm(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::creaTicketCrm] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/crea-ticket-crm/"+idIstanza, String.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::creaTicketCrm] END in " + sec + "milliseconds.");
			}
		}
	}
	@Override
	public CreaIuvResponse creaIUV(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::creaIUV] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/" + idIstanza + "/crea-iuv", CreaIuvResponse.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::creaIUV] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public CreaIuvResponse pagoPA(Long idIstanza, EPayPagoPAParams pagoPAParams) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::pagoPA] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/istanze/" + idIstanza + "/pago-pa", CreaIuvResponse.class, pagoPAParams);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::pagoPA] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public CreaIuvResponse annullaIUV(Long idIstanza, String iuv) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::annullaIUV] BEGIN idIstanza:" + idIstanza + " iuv:" + iuv);
			return postMoonsrvJson("/istanze/" + idIstanza + "/annulla-iuv/" + iuv, CreaIuvResponse.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::annullaIUV] END in " + sec + "milliseconds.");
			}
		}
	}

	
	
	@Override
	public byte[] getAllegatoFruitore(String codice) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getAllegatoFruitore] BEGIN codice: " + codice);			
			return getMoonsrvBytes("/extra/wf/cosmo/"+codice+"/contenuto");			
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getAllegatoFruitore] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public byte[] getContenutoIndexByUid(String uuidIndex) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getContenutoIndexByUid] BEGIN uuidIndex: " + uuidIndex);			
			return getMoonsrvBytes("/index/"+uuidIndex);			
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::getContenutoIndexByUid] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public List<ComponenteFamiglia> getFamigliaANPR(String codiceFiscale, String userJwt, String clientProfileKey, String ipAdress, String utente) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::getFamigliaANPR] BEGIN codiceFiscale: " + codiceFiscale);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN codiceFiscale="+codiceFiscale);
				LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN clientProfileKey="+clientProfileKey);
				LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN ipAdress="+ipAdress);
				LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN utente="+utente);
			}
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
//			queryParams.add("userJwt", "eyJhbGciOiJSUzI1NiJ9.eyJTaGliLUlkZW50aXRhLUNvZGljZUZpc2NhbGUiOiJDUlpOR0w3MVQ0MkY5NzlYIiwiZXhwIjoxNTkwNjk5NzQ2LCJzdWIiOiJHQVNQQ09UTyIsImVtYWlsIjoiYW5nZWxhLmNhcnplZGRhQGdtYWlsLmNvbSIsIm5hbWUiOiJBbmdlbGEiLCJmYW1pbHlOYW1lIjoiQ2FyemVkZGEiLCJhdWQiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJpc3MiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJqdGkiOiJfOWNjN2VlYzUtYjc1MC00ZTM4LWJmMWUtNGUzZDIwZWMyNmIyIn0.oaPoq4rf2ElmgMFshKD0WElHXssVmDDVY8CatgJWNwWYKikII3WuhjLmD6p4-tNo-6orE6ubVuCTsj3-uLfENpe6MLnIUPnjyxq1GBeHCka8KCNlta_LKHgzzuLM293KYjprdot4LwziZJmIdnT1hMwk4mpxrk7acKJHe7mT0NrHQkNaCa4jFsFcd23tKllkNFuVUwQfaIPUFxEG5UjS8Qk9LZIKRV3bKBiwkydN3AOO3u77T0bDOIyvn2ym80csA2QuXAUMLQ6ddYSqpGsqu1hH78KRpl1V4HjujTVU2GzwGv3HbQ9VY_jsKii4z0LLDkA6asvWEDaZKV1Bn9ixug");
//			if (!StringUtils.isEmpty(consumerPrefix)) {
//				queryParams.add("consumerPrefix", consumerPrefix);
//			}
			if (!StringUtils.isEmpty(userJwt)) {
				queryParams.add("userJwt", userJwt);
			}
			if (!StringUtils.isEmpty(clientProfileKey)) {
				queryParams.add("clientProfileKey", clientProfileKey);
			}
			if (!StringUtils.isEmpty(ipAdress)) {
				queryParams.add("ipAdress", ipAdress);
			}
			if (!StringUtils.isEmpty(utente)) {
				queryParams.add("utente", utente);
			}
			ComponenteFamiglia[] componenti = getMoonsrvJson("/extra/demografia/anpr/componentiFamigliaByCF/"+codiceFiscale, ComponenteFamiglia[].class, queryParams);
			return List.of(componenti);
		} finally {
			 long end = System.currentTimeMillis();
			 float sec = (end - start); 
			 LOG.info("[" + CLASS_NAME + "::getFamigliaANPR] in " + sec + " milliseconds.");
			 LOG.debug("[" + CLASS_NAME + "::getFamigliaANPR] END");
		}
	}

	@Override
	public ReportVerificaFirma verificaFirmaByContenuto(byte[] bytes) throws DAOException {
		long start = System.currentTimeMillis();
		try(InputStream is = new ByteArrayInputStream(bytes)) {
			LOG.debug("[" + CLASS_NAME + "::verificaFirmaByContenuto] BEGIN");
			MultipartFormDataOutput bodyMultipart = new MultipartFormDataOutput();
			bodyMultipart.addFormData("attachment", is, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			return postJsonMultipartForm("/firma/verifica-firma", ReportVerificaFirma.class, bodyMultipart);			
		} catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::verificaFirmaByContenuto] ERROR ByteArrayInputStream()");
			throw new DAOException();
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::verificaFirmaByContenuto] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public String pubblicaMyDocs(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::pubblicaMyDocs] BEGIN idIstanza: " + idIstanza);
//			return postMoonsrvJson("/istanze/pubblica-mydocs/"+idIstanza, String.class, null);
			return postMoonsrvJson("/extra/doc/mydocs/pubblica-istanza/"+idIstanza, String.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::pubblicaMyDocs] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public String richiestaNotifyByIdIstanza(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			LOG.info("[" + CLASS_NAME + "::richiestaNotifyByIdIstanza] BEGIN idIstanza: " + idIstanza);
			return postMoonsrvJson("/extra/notificatore/"+idIstanza, String.class, null);
		} finally {
			if (LOG.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				LOG.debug("[" + CLASS_NAME + "::richiestaNotifyByIdIstanza] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public List<PraticaEdilizia> getElencoPratiche(String anno, String numRegistro, String progressivo)
			throws DAOException {
		try {
			LOG.info("[" + CLASS_NAME + "::getElencoPratiche] IN anno: " + anno);			
			LOG.info("[" + CLASS_NAME + "::getElencoPratiche] IN numRegistro: " + numRegistro);
			LOG.info("[" + CLASS_NAME + "::getElencoPratiche] IN progressivo: " + progressivo);
			
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();

			if (!StringUtils.isEmpty(anno)) {
				queryParams.add("anno", anno);
			}
			if (!StringUtils.isEmpty(numRegistro)) {
				queryParams.add("registro", numRegistro);
			}
			if (!StringUtils.isEmpty(progressivo)) {
				queryParams.add("progressivo", progressivo);
			}		
			
			PraticaEdilizia[] pratiche = getMoonsrvJson("/extra/edilizia/pratiche", PraticaEdilizia[].class, queryParams);
			
			return List.of(pratiche);
		} finally {
			 LOG.debug("[" + CLASS_NAME + "::getElencoPratiche] END");
		}
	}
	
}
