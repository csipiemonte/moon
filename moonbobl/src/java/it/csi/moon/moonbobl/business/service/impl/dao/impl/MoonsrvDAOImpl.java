/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.component.MoonsrvTemplateImpl;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaInitParams;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.MyDocsRichiestaEntity;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.Categoria;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaMoonsrv;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloParametro;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.ValutazioneModuloSintesi;
import it.csi.moon.moonbobl.dto.moonfobl.VerificaPagamento;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;


@Component
public class MoonsrvDAOImpl extends MoonsrvTemplateImpl implements MoonsrvDAO {
	
	protected final static String CLASS_NAME = "MoonsrvDAOImpl";
	
	@Override
	public String pingMoonsrv() throws DAOException {
		return "MOONSRV: " + getMoonsrv("/cmd/ping");
	}

	@Override
	public List<Modulo> getModuli(ModuliFilter filter) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			log.info("[" + CLASS_NAME + "::getModuli] filter="+filter);
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			
			if(filter.getCodiceModulo().isPresent()) {
				queryParams.add("codiceModulo", filter.getCodiceModulo().get());
			}
//			if(filter.getDataEntroIntDiPubblicazione().isPresent()) {
//				queryParams.add("dataEntroIntDiPub", filter.getDataEntroIntDiPubblicazione().get());
//			}
			if(filter.getConPresenzaIstanzeUser().isPresent()) {
				queryParams.add("conPresenzaIstanzeUser", filter.getConPresenzaIstanzeUser().get()); // filterUser delle istanze presente
			} else {
				queryParams.add("stato", DecodificaStatoModulo.PUBBLICATO.getCodice()); // Tutti moduli Pubblicati
			}
			if(filter.getNomePortale().isPresent()) {
				queryParams.add("nomePortale", filter.getNomePortale().get());
			}
			return List.of(getMoonsrvJson("/moduli", Modulo[].class, queryParams));
		} finally {
//			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.info("[" + CLASS_NAME + "::getModuli] END in " + sec + "milliseconds.");
//			}
		}
	}

	@Override
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getModuloById] BEGIN");
			return getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo, Modulo.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getModuloById] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getModuloByCodice] BEGIN");
			return getMoonsrvJson("/moduli/codice/"+codiceModulo+"/versione/"+versione, Modulo.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getModuloByCodice] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Istanza initIstanza(UserInfo user, Long idModulo) throws DAOException {
		assert user!=null : "User non presente.";
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::initIstanza] BEGIN");
			IstanzaInitParams initParams = new IstanzaInitParams();
			initParams.setIdModulo(idModulo);
			initParams.setCodiceFiscale(user.getIdentificativoUtente());
			initParams.setCognome(user.getCognome());
			initParams.setNome(user.getNome());
			initParams.setJwt(user.getJwt());
			return postMoonsrvJson("/istanze/init", Istanza.class, initParams);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::initIstanza] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Istanza initIstanza(String ipAdress, UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params) throws ItemNotFoundBusinessException, DAOException {
		assert user!=null : "User non presente.";
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::initIstanza] BEGIN");
			IstanzaInitParams initParams = new IstanzaInitParams();
			initParams.setIpAdress(ipAdress);
			initParams.setIdModulo(idModulo);
			initParams.setIdVersioneModulo(idVersioneModulo);
			initParams.setCodiceFiscale(user.getIdentificativoUtente());
			initParams.setCognome(user.getCognome());
			initParams.setNome(user.getNome());
			initParams.setJwt(user.getJwt());
			initParams.setBlParams(params);
			initParams.setFlagCompilaBo(true);
			
			//I servizi del Notificatore necessitano dell'identità iride in inizializzazione
			//initParams.setShibIrideIdentitaDigitale(user.getIdIride());
			
			return postMoonsrvJson("/istanze/init", Istanza.class, initParams);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::initIstanza] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public byte[] getPdfByIdIstanza(UserInfo user, Long idIstanza) throws DAOException {
		assert user!=null : "User non presente.";
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getPdfByIdIstanza] BEGIN");

			return getMoonsrvBytes("/istanze/"+ idIstanza + "/pdf");
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getPdfByIdIstanza] END in " + sec + "milliseconds.");
			}
		}
	}
	
//	@Override
//	public String generaSalvaPdf(Long idIstanza) throws DAOException {		
//		assert idIstanza!=null : "idIstanza non presente.";
//		long start = System.currentTimeMillis();
//		try {
//			log.debug("[" + CLASS_NAME + "::generaSalvaPdf] BEGIN");
//
//			return postMoonsrvJson("/istanze/"+ idIstanza + "/generaSalvaPdf",String.class, null);
//		} finally {
//			if (log.isDebugEnabled()) {
//				long end = System.currentTimeMillis();
//				float sec = (end - start); 
//				log.debug("[" + CLASS_NAME + "::generaSalvaPdf] END in " + sec + "milliseconds.");
//			}
//		}
//	}
	
//	@Override
//	public String rigeneraSalvaPdf(Long idIstanza) throws DAOException {		
//		assert idIstanza!=null : "idIstanza non presente.";
//		long start = System.currentTimeMillis();
//		try {
//			log.debug("[" + CLASS_NAME + "::generaSalvaPdf] BEGIN");
//
//			return postMoonsrvJson("/istanze/"+ idIstanza + "/rigeneraSalvaPdf",String.class, null);
//		} finally {
//			if (log.isDebugEnabled()) {
//				long end = System.currentTimeMillis();
//				float sec = (end - start); 
//				log.debug("[" + CLASS_NAME + "::generaSalvaPdf] END in " + sec + "milliseconds.");
//			}
//		}
//	}
	
	@Override
	public  byte[] generaPdf(IstanzaMoonsrv istanza) throws DAOException {		

		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::generaPdf] BEGIN");			
			return postMoonsrvBytes("/istanze/generaPdf",istanza);					
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::generaPdf] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public  byte[] generaPdf(Long idIstanza) throws DAOException {		
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::generaPdf] BEGIN");								
			return getMoonsrvBytes("/istanze/"+ idIstanza + "/generaPdf");	
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::generaPdf] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String sendEmail(EmailRequest request) throws DAOException {
		assert request!=null : "EmailRequest non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::sendEmail] BEGIN");
			return postMoonsrvJson("/email/text", String.class, request);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::sendEmail] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String sendEmailWithAttachment(EmailRequest request) throws DAOException {
		assert request!=null : "EmailRequest non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::sendEmailWithAttachment] BEGIN");
			return postMoonsrvJson("/email/text-attach", String.class, request);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::sendEmailWithAttachment] END in " + sec + "milliseconds.");
			}
		}
	}



	@Override
	public String protocolla(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::protocolla] BEGIN");
			return postMoonsrvJson("/istanze/protocolla/"+idIstanza, String.class, null);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::protocolla] END in " + sec + "milliseconds.");
			}
		}
	}
	

	
	@Override
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type, String onlyFirstLevel) throws DAOException {
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getCampiModulo] BEGIN");
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			if(!StringUtils.isEmpty(type)) {
				queryParams.add("type", type);
			}
			if(!StringUtils.isEmpty(onlyFirstLevel)) {
				queryParams.add("onlyFirstLevel", onlyFirstLevel);
			}
			
			
			List<CampoModulo> campi = List.of(getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo+"/campi", CampoModulo[].class, queryParams));
			return campi;
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getCampiModulo] END in " + sec + "milliseconds.");
			}
		}
	}	
	
	@Override
	public List<ProtocolloParametro> getProtocolloParametri(Long idModulo, Long idVersioneModulo) throws DAOException {
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getProtocolloParametri] BEGIN");
			List<ProtocolloParametro> result = List.of(getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo+"/protocollo-parametri", ProtocolloParametro[].class));
			return result;
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getProtocolloParametri] END in " + sec + "milliseconds.");
			}
		}
	}
	
//	
	@Override
	public List<Categoria> getCategorie() throws DAOException {
	
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getCategorie] BEGIN");
			
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
					
			return getMoonsrvJson("/categorie/",  List.class, queryParams);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getCategorie] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String protocollaFile(Long idFile) throws DAOException {
		assert idFile!=null : "idFile non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::protocollaFile] BEGIN");
			return postMoonsrvJson("/repository/file/protocolla/"+idFile, String.class, null);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::protocollaFile] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public String protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		assert idStoricoWorkflow!=null : "idStoricoWorkflow non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::protocollaIntegrazione] BEGIN");
			return postMoonsrvJson("/istanze/"+idIstanza+"/protocolla-integrazione/"+idStoricoWorkflow, String.class, null);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::protocollaIntegrazione] END in " + sec + "milliseconds.");
			}
		}
	}


	@Override
	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] BEGIN");
			return postMoonsrvJson("/istanze/creaPraticaEdAvviaProcesso/"+idIstanza, String.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::creaPraticaEdAvviaProcesso] END in " + sec + "milliseconds.");
			}
		}
	}
	
	
	@Override
	public String inviaRispostaIntegrazione(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazione] BEGIN");
			return postMoonsrvJson("/istanze/"+idIstanza+"/integrazione-cosmo", String.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazione] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws DAOException {
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getCampiModulo] BEGIN");
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
			if(!StringUtils.isEmpty(type)) {
				queryParams.add("type", type);
			}
			return List.of(getMoonsrvJson("/moduli/"+idModulo+"/v/"+idVersioneModulo+"/campi", CampoModulo[].class, queryParams));
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getCampiModulo] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String creaTicketCrm(Long idIstanza) throws DAOException {
		assert idIstanza!=null : "idIstanza non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::creaTicketCrm] BEGIN");
			return postMoonsrvJson("/istanze/crea-ticket-crm/"+idIstanza, String.class, null);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::creaTicketCrm] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public byte[] getAllegatoFruitore(String codice) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getAllegatoFruitore] BEGIN");			
			return getMoonsrvBytes("/extra/wf/cosmo/"+codice+"/contenuto");			
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getAllegatoFruitore] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public ModuloClass uploadModuloClass(Long idModulo, int idTipologia, byte[] bytes) {
		String url = "/moduli/"+idModulo+"/modulo-class-for-tipologia/"+idTipologia;
		ModuloClass ris = postMoonsrvFileClass(url,bytes) ;
		return ris;
	}

	@Override
	public void deleteModuloClass(Long idModulo, int idTipologia) {
		String url = "/moduli/"+idModulo+"/modulo-class-for-tipologia/"+idTipologia;
		deleteMoonsrv(url);
	}
	
	@Override
	public byte[] getContenutoIndexByUid(String uuidIndex) {
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getContenutoIndexByUid] BEGIN");			
			return getMoonsrvBytes("/index/"+uuidIndex);			
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getContenutoIndexByUid] END in " + sec + "milliseconds.");
			}
		}
	}

	
	@Override
	public String getPrintMapperName(String codiceModulo) throws DAOException {
		assert codiceModulo!=null : "codiceModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getPrintMapperName] BEGIN");
			return getMoonsrv("/moduli/codice/"+codiceModulo+"/print-mapper-name");
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getPrintMapperName] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String getProtocolloManagerName(String codiceModulo) throws DAOException {
		assert codiceModulo!=null : "codiceModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getProtocolloManagerName] BEGIN");
			return getMoonsrv("/moduli/codice/"+codiceModulo+"/protocollo-manager-name");
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getProtocolloManagerName] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public String getEpayManagerName(String codiceModulo) throws DAOException {
		assert codiceModulo!=null : "codiceModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getEpayManagerName] BEGIN");
			return getMoonsrv("/moduli/codice/"+codiceModulo+"/epay-manager-name");
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getEpayManagerName] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] BEGIN");
			return getMoonsrvJson("/moduli/codice/"+codiceModulo+"/pubblicato", Modulo.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] END in " + sec + "milliseconds.");
			}
		}
	}
	
	@Override
	public VerificaPagamento getVerificaPagamento(String idEpay) throws DAOException {
		assert idEpay!=null : "idEpay non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getVerificaPagamento] BEGIN");
			return getMoonsrvJson("/epay/verifica-pagamento/"+idEpay, VerificaPagamento.class);
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getVerificaPagamento] END in " + sec + "milliseconds.");
			}
		}
	}

	@Override
	public List<ValutazioneModuloSintesi> getValutazioneModuloSintesi(Long idModulo) throws DAOException {
		assert idModulo!=null : "idModulo non presente.";
		long start = System.currentTimeMillis();
		try {
			log.debug("[" + CLASS_NAME + "::getValutazioneModuloSintesi] BEGIN");
			return List.of(getMoonsrvJson("/valutazioni/sintesi?idModulo="+idModulo, ValutazioneModuloSintesi[].class));
		} finally {
			if (log.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				float sec = (end - start); 
				log.debug("[" + CLASS_NAME + "::getValutazioneModuloSintesi] END in " + sec + "milliseconds.");
			}
		}
	}
	

	@Override
	public List<ComponenteFamiglia> getFamigliaANPR(String codiceFiscale, String clientProfileKey, String ipAdress, String utente) throws DAOException {
		long start = System.currentTimeMillis();
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getFamigliaANPR] BEGIN");
				log.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN codiceFiscale="+codiceFiscale);
				log.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN clientProfileKey="+clientProfileKey);
				log.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN ipAdress="+ipAdress);
				log.debug("[" + CLASS_NAME + "::getFamigliaANPR] IN utente="+utente);
			}
			MultivaluedMap<String, Object> queryParams = new MultivaluedMapImpl<>();
//			queryParams.add("userJwt", "eyJhbGciOiJSUzI1NiJ9.eyJTaGliLUlkZW50aXRhLUNvZGljZUZpc2NhbGUiOiJDUlpOR0w3MVQ0MkY5NzlYIiwiZXhwIjoxNTkwNjk5NzQ2LCJzdWIiOiJHQVNQQ09UTyIsImVtYWlsIjoiYW5nZWxhLmNhcnplZGRhQGdtYWlsLmNvbSIsIm5hbWUiOiJBbmdlbGEiLCJmYW1pbHlOYW1lIjoiQ2FyemVkZGEiLCJhdWQiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJpc3MiOiJodHRwczpcL1wvd3d3LnNwaWQucGllbW9udGUuaXQiLCJqdGkiOiJfOWNjN2VlYzUtYjc1MC00ZTM4LWJmMWUtNGUzZDIwZWMyNmIyIn0.oaPoq4rf2ElmgMFshKD0WElHXssVmDDVY8CatgJWNwWYKikII3WuhjLmD6p4-tNo-6orE6ubVuCTsj3-uLfENpe6MLnIUPnjyxq1GBeHCka8KCNlta_LKHgzzuLM293KYjprdot4LwziZJmIdnT1hMwk4mpxrk7acKJHe7mT0NrHQkNaCa4jFsFcd23tKllkNFuVUwQfaIPUFxEG5UjS8Qk9LZIKRV3bKBiwkydN3AOO3u77T0bDOIyvn2ym80csA2QuXAUMLQ6ddYSqpGsqu1hH78KRpl1V4HjujTVU2GzwGv3HbQ9VY_jsKii4z0LLDkA6asvWEDaZKV1Bn9ixug");
//			if (!StringUtils.isEmpty(consumerPrefix)) {
//				queryParams.add("consumerPrefix", consumerPrefix);
//			}
//			queryParams.add("userJwt", userJwt);
			queryParams.add("clientProfileKey", clientProfileKey);
			queryParams.add("ipAdress", ipAdress);
			queryParams.add("utente", utente);
			ComponenteFamiglia[] componenti = getMoonsrvJson("/extra/demografia/anpr/componentiFamigliaByCF/"+codiceFiscale, ComponenteFamiglia[].class, queryParams);
			return List.of(componenti);
		} finally {
			 long end = System.currentTimeMillis();
			 float sec = (end - start); 
			 log.info("[" + CLASS_NAME + "::getFamigliaANPR] in " + sec + " milliseconds.");
			 log.debug("[" + CLASS_NAME + "::getFamigliaANPR] END");
		}
	}
	
	   @Override
		public String pubblicaFileMyDocs(Long idFile) throws DAOException {
			assert idFile!=null : "idFile non presente.";
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idFile: " + idFile);
				return postMoonsrvJson("/extra/doc/mydocs/pubblica-file/"+idFile, String.class, null);
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] END in " + sec + "milliseconds.");
				}
			}
		} 
	   
//	   @Override
//		public String pubblicaFileMyDocs(Long idFile, Long idIstanza, Long idStoricoWorkflow) throws DAOException {
//			assert idFile!=null : "idFile non presente.";
//			long start = System.currentTimeMillis();
//			try {
//				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idFile: " + idFile);
//				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idIstanza: " + idIstanza);
//				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idStoricoWorkflow: " + idStoricoWorkflow);
//				
//				return postMoonsrvJson("/extra/doc/mydocs/pubblica-file/"+idFile+"/istanza/"+idIstanza+"/storico/"+idStoricoWorkflow, String.class, null);
//			} finally {
//				if (log.isDebugEnabled()) {
//					long end = System.currentTimeMillis();
//					float sec = (end - start); 
//					log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] END in " + sec + "milliseconds.");
//				}
//			}
//		} 	   
	   
	   @Override
//	   @Transactional(propagation = Propagation.REQUIRES_NEW)
		public String pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow) throws DAOException {
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::pubblicaMyDocs] BEGIN idIstanza: " + idIstanza);
				log.info("[" + CLASS_NAME + "::pubblicaMyDocs] BEGIN idStoricoWorkflow: " + idStoricoWorkflow);
				
				return postMoonsrvJson("/extra/doc/mydocs/pubblica-mydocs/istanza/"+idIstanza+"/storico/"+idStoricoWorkflow, String.class, null);
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] END in " + sec + "milliseconds.");
				}
			}
		} 	
	   
	   
	   @Override
		public String pubblicaIstanzaMyDocs(Long idIstanza, Long idRichiesta) throws DAOException {
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::pubblicaIstanzaMyDocs] BEGIN idIstanza: " + idIstanza);
				log.info("[" + CLASS_NAME + "::pubblicaIstanzaMyDocs] BEGIN idRichiesta: " + idRichiesta);
				
				return postMoonsrvJson("/extra/doc/mydocs/pubblica-istanza/"+idIstanza+"/log-richiesta/"+idRichiesta, String.class, null);
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::pubblicaIstanzaMyDocs] END in " + sec + "milliseconds.");
				}
			}
		}
	   
	   
	   @Override
		public String pubblicaFileMyDocs(Long idFile, Long idRichiesta) throws DAOException {
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idFile: " + idFile);
				log.info("[" + CLASS_NAME + "::pubblicaFileMyDocs] BEGIN idRichiesta: " + idRichiesta);
				
				return postMoonsrvJson("/extra/doc/mydocs/pubblica-file/"+idFile+"/log-richiesta/"+idRichiesta, String.class, null);
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::pubblicaFileMyDocs] END in " + sec + "milliseconds.");
				}
			}
		}	   
	   
	   
	   @Override
		public List<MyDocsRichiestaEntity> getLogRichiesteMyDocs(Long idIstanza) throws DAOException {
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::getLogRichiesteMyDocs] BEGIN idIstanza: " + idIstanza);					
				return List.of(getMoonsrvJson("/extra/doc/mydocs/richieste/istanza/"+idIstanza, MyDocsRichiestaEntity[].class));
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::getLogRichiesteMyDocs] END in " + sec + "milliseconds.");
				}
			}
		} 		  	   

		
		@Override
		public byte[] getReport(String codiceModulo, String codiceEstrazione, Date createdStart,
				Date createdEnd) {
			long start = System.currentTimeMillis();
			try {
				log.debug("[" + CLASS_NAME + "::getReport] BEGIN");			
				String url = "/reports/modulo/"+codiceModulo+"?codice_estrazione="+codiceEstrazione;
				if(createdStart != null) {
					SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
					url += "&data_da="+sdf_yyyyMMdd.format(createdStart);
				}
				if(createdEnd != null) {
					SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
					url += "&data_a="+sdf_yyyyMMdd.format(createdEnd);
				}
				return getMoonsrvBytes(url);			
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::getReport] END in " + sec + "milliseconds.");
				}
			}
		}

		@Override
		public String richiestaNotifyByIdIstanza(Long idIstanza) throws DAOException {
			assert idIstanza!=null : "idIstanza non presente.";
			long start = System.currentTimeMillis();
			try {
				log.info("[" + CLASS_NAME + "::richiestaNotifyByIdIstanza] BEGIN idIstanza: " + idIstanza);
				return postMoonsrvJson("/extra/notificatore/"+idIstanza, String.class, null);
			} finally {
				if (log.isDebugEnabled()) {
					long end = System.currentTimeMillis();
					float sec = (end - start); 
					log.debug("[" + CLASS_NAME + "::richiestaNotifyByIdIstanza] END in " + sec + "milliseconds.");
				}
			}
		}
}
