/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonbobl.business.service.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ProcessiService;
import it.csi.moon.moonbobl.business.service.WorkflowService;
import it.csi.moon.moonbobl.business.service.impl.dao.CategoriaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloStrutturaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.PortaleDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.PortaleModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ProcessoModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.ProtocolloParametroDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.UtenteModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.CategoriaModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloAttributoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloProgressivoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloStrutturaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersioneEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.PortaleModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.UtenteModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.WorkflowFilter;
import it.csi.moon.moonbobl.business.service.mapper.CategoriaMapper;
import it.csi.moon.moonbobl.business.service.mapper.ModuloAttributoMapper;
import it.csi.moon.moonbobl.business.service.mapper.ModuloMapper;
import it.csi.moon.moonbobl.business.service.mapper.PortaleMapper;
import it.csi.moon.moonbobl.business.service.mapper.StatoModuloMapper;
import it.csi.moon.moonbobl.business.service.mapper.UtenteModuloAbilitatoMapper;
import it.csi.moon.moonbobl.dto.moonfobl.InitModulo;
import it.csi.moon.moonbobl.dto.moonfobl.InitModuloCambiaStato;
import it.csi.moon.moonbobl.dto.moonfobl.InitModuloVersione;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloAttributo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonbobl.dto.moonfobl.NuovaVersioneModuloRequest;
import it.csi.moon.moonbobl.dto.moonfobl.StatoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteModuloAbilitato;
import it.csi.moon.moonbobl.dto.moonfobl.Workflow;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.exceptions.business.TooManyItemFoundBusinessException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.ModuloAttributoKeys;
import it.csi.moon.moonbobl.util.StrUtils;
import it.csi.moon.moonbobl.util.decodifica.DecodificaAzione;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoUtente;



/**
 * @author fran
 * Layer di logica servizi che richiama i DAO
 */
@Component
public class ModuliServiceImpl implements ModuliService {

	private final static String CLASS_NAME = "ModuliServiceImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
    public static final Set<String> ATTRIBUTI_EMAIL = Collections.unmodifiableSet(new HashSet<>(
    	Arrays.asList("PSIT_EMAIL", "PSIT_EMAIL_CONF")));
    public static final Set<String> ATTRIBUTI_GENERALI = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("CONTO_TERZI", "COMPILA_BO", "HASH_UNIVOCITA", "HASH_UNIVOCITA_FIELDS", "MSG_UNIVOCITA", 
       		"INIT_NOME_CLASS", "INIT_OBBLIGATORIA", "ISTANZA_AUTO_SAVE", "STAMPA_PDF", "TIPO_FILTER_BO",
       		"STAMPA_DINAMICA", "MODIFICA_ISTANZA_INVIATA","FORMIO_BREADCRUMB_CLICKABLE")));
    public static final Set<String> ATTRIBUTI_NOTIFY = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("PSIT_NOTIFY", "PSIT_NOTIFY_CONF")));
    public static final Set<String> ATTRIBUTI_PROTOCOLLO = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("PSIT_PROTOCOLLO","PSIT_PROTOCOLLO_INTEGRAZIONE", "PRT_METADATI", "PCPT_IN_EMAIL", "PCPT_IN_EMAIL_CONF", "PROTOCOLLA_BO","PROTOCOLLA_INTEGRAZIONE_BO")));
    public static final Set<String> ATTRIBUTI_COSMO = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("PSIT_COSMO", "PSIT_COSMO_CONF")));
    public static final Set<String> ATTRIBUTI_AZIONE = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("PSIT_AZIONE")));
    public static final Set<String> ATTRIBUTI_ESTRAI_DICH = Collections.unmodifiableSet(new HashSet<>(
    	Arrays.asList("PSIT_ESTRAI_DICHIARANTE","PSIT_ESTRAI_DICHIARANTE_CONF")));
    public static final Set<String> ATTRIBUTI_CRM = Collections.unmodifiableSet(new HashSet<>(
       	Arrays.asList("PSIT_CRM","PSIT_CRM_SYSTEM","PSIT_CRM_CONF","PSIT_CRM_CONF_NEXTCRM","PSIT_CRM_CONF_R2U","PSIT_CRM_CONF_OTRS")));
    public static final Set<String> ATTRIBUTI_EPAY = Collections.unmodifiableSet(new HashSet<>(
           	Arrays.asList("PSIT_EPAY","PSIT_EPAY_CONF")));
    public static final Set<String> ATTRIBUTI_API = Collections.unmodifiableSet(new HashSet<>(
           	Arrays.asList("APA_EMAIL_CONF")));
    
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	ModuloStrutturaDAO strutturaDAO;
	@Autowired
	ModuloProgressivoDAO moduloProgressivoDAO;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	@Autowired
	CategoriaDAO categoriaDAO;
	@Autowired
	UtenteDAO utenteDAO;
	@Autowired
	UtenteModuloDAO utenteModuloDAO;
	@Autowired
	PortaleDAO portaleDAO;
	@Autowired
	PortaleModuloDAO portaleModuloDAO;
	@Autowired
	ProcessoModuloDAO processoModuloDAO;
	@Autowired
	ProcessiService processiService;
	@Autowired
	ProtocolloParametroDAO protocolloParametroDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	WorkflowService workflowService;
	
	@Override
	public Modulo getModuloById(UserInfo user, Long idModulo, Long idVersioneModulo, String fields) throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getModuloById] BEGIN");
			log.debug("[" + CLASS_NAME + "::getModuloById] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  fields="+fields);
		}
		try {
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			Modulo modulo = ModuloMapper.buildFromModuloVersionatoEntity(entity);
			modulo = completeRequestFields(fields, modulo);
			return modulo;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getModuloById] Risorsa nont trovata idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getModuloById] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo, e);
			throw new BusinessException("Errore ricerca modulo per id");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getModuloById] Exception idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo, e);
			throw new BusinessException("Errore generica in ricerca modulo per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getModuloById] END");
			}
		}
	}
	private Modulo completeRequestFields(String fields, Modulo result) {
		if (StringUtils.isEmpty(fields)) {
			return result;
		}
		List<String> listFields = Arrays.asList(fields.split(","));

		if ( listFields.contains("strutturaBO")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] strutturaBO");
				result = completaModuloStrutturaBO(result);
		}
		else {
			// TODO da rendere optional : per il momento lodiamo sempre per retrocompatibilita
//			if ( listFields.contains("struttura")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] struttura");
				result = completaModuloStruttura(result);
//			}
			
		}		
		if ( listFields.contains("portali")) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] portali");
			result = completaModuloPortali(result);
		}
		if ( listFields.contains("processo")) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] processo");
			result = completaModuloProcesso(result);
		}
		if ( listFields.contains("objAttributi")) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] objAttributi");
			result = completaModuloObjAttributiBO(result);
		}
		if ( listFields.contains("attributi")) {
			log.debug("[" + CLASS_NAME + "::completeRequestFields] attributi");
			result = completaModuloAttributi(result);
		} else {
			if ( listFields.contains("attributiGenerali")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiGenerali");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_GENERALI);
			}	
			if ( listFields.contains("attributiEmail")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiEmail");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_EMAIL);
			}	
			if ( listFields.contains("attributiNotify")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiNotify");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_NOTIFY);
			}	
			if ( listFields.contains("attributiProtocollo")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiProtocollo");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_PROTOCOLLO);
			}	
			if ( listFields.contains("attributiCosmo")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiCosmo");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_COSMO);
			}
			if ( listFields.contains("attributiAzione")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiAzione");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_AZIONE);
			}
			if ( listFields.contains("attributiEstraiDich")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiEstraiDich");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_ESTRAI_DICH);
			}
			if ( listFields.contains("attributiCrm")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiCrm");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_CRM);
			}
			if ( listFields.contains("attributiEpay")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiEpay");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_EPAY);
			}
			if ( listFields.contains("attributiApi")) {
				log.debug("[" + CLASS_NAME + "::completeRequestFields] attributiApi");
				result = completaModuloAttributiFilter(result, ATTRIBUTI_API);
			}
		}
		// 2 possibilite: "versioni","versioniCurrentStato"
		Optional<String> fieldVersioni = listFields.stream().filter(s -> s.startsWith("versioni")).findFirst();
		if (fieldVersioni.isPresent()) { 
			log.debug("[" + CLASS_NAME + "::completeRequestFields] versioni");
			result = completaModuloVersioni(result, fieldVersioni.get());
		}
		return result;
	}
	
	

	@Override
	public List<Modulo> getElencoModuliPubblicati(UserInfo user) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::getElencoModuliPubblicati] BEGIN");
			// Qui siamo nel backoffice, quindi visualizziamo solo quelli abilitati all'utente connesso
			ModuliFilter filter = new ModuliFilter();
			if(!DecodificaTipoUtente.ADM.isCorrectType(user)) {
				filter.setNomePortale(user.getPortalName());
			}
			filter.setStatoModulo(DecodificaStatoModulo.PUBBLICATO);
			filter.setUtenteAbilitato(!DecodificaTipoUtente.ADM.isCorrectType(user)?user.getIdentificativoUtente():null);
			return moduloDAO.find(filter).stream()
				.map(ModuloMapper::buildFromModuloVersionatoEntity)
				.collect(Collectors.toList());
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoModuliPubblicati] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli pubblicati");
 		}
	}
	
	
	@Override
	public List<Modulo> getElencoModuliAbilitatiCurrentUserEnte(UserInfo user) throws BusinessException {
		String onlyLastVersione = "false";
		return getElencoModuliAbilitatiCurrentUserEnte(user, onlyLastVersione);
	}
	@Override
	public List<Modulo> getElencoModuliAbilitatiCurrentUserEnte(UserInfo user, String onlyLastVersione) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::getElencoModuliAbilitatiCurrentUserEnte] BEGIN onlyLastVersione=" + onlyLastVersione);
			// Ricerca moduli abilitati per l'utente connesso
			ModuliFilter filter = new ModuliFilter();
//			if(!DecodificaTipoUtente.ADM.isCorrectType(user)) {
//				filter.setNomePortale(user.getPortalName());
//			}
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			filter.setUtenteAbilitato(!DecodificaTipoUtente.ADM.isCorrectType(user)?user.getIdentificativoUtente():null);
			if ("S".equals(onlyLastVersione) || "true".equals(onlyLastVersione)) {
				filter.setOnlyLastVersione(Boolean.TRUE);
			}
			return moduloDAO.find(filter).stream()
				.map(ModuloMapper::buildFromModuloVersionatoEntity)
//TODO da eliminare
				.map(m -> completaModuloObjAttributiBO(m)) // rallento molto, da verificare quando serve realemnte, caso mai dal WCL richiamare  /moduli/{idModulo}/attributi
				.collect(Collectors.toList());
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoModuliAbilitatiCurrentUserEnte] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli abilitati CurrentUserEnte");
 		}
	}
	

	@Override
	public List<Modulo> getElencoModuliAbilitati(UserInfo user) throws BusinessException {
		String onlyLastVersione = "false";
		return getElencoModuliAbilitati(user, null, onlyLastVersione, null);
	}
	@Override
	public List<Modulo> getElencoModuliAbilitati(UserInfo user, Long idModulo, String onlyLastVersione, String pubblicatoBO) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::getElencoModuliAbilitati] BEGIN idModulo=" + idModulo + " onlyLastVersione=" + onlyLastVersione + " pubblicatoBO=" + pubblicatoBO);
			// Ricerca moduli abilitati per l'utente connesso
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
//			if(!DecodificaTipoUtente.ADM.isCorrectType(user)) {
//				filter.setNomePortale(user.getPortalName());
//			}
			filter.setIdEnte(user.isMultiEntePortale()? (user.getEnte() != null ? user.getEnte().getIdEnte():null):null);
			
			filter.setUtenteAbilitato(!DecodificaTipoUtente.ADM.isCorrectType(user)?user.getIdentificativoUtente():null);
			if ("S".equals(onlyLastVersione) || "true".equals(onlyLastVersione)) {
				filter.setOnlyLastVersione(Boolean.TRUE);
			}
			if ("S".equals(pubblicatoBO) || "true".equals(pubblicatoBO)) {
				filter.setStatoModulo(DecodificaStatoModulo.PUBBLICATO);
				filter.setAttributoPresente(ModuloAttributoKeys.COMPILA_BO.getKey());
			}
			return moduloDAO.find(filter).stream()
				.map(ModuloMapper::buildFromModuloVersionatoEntity)
//TODO da eliminare
				.map(m -> completaModuloObjAttributiBO(m)) // rallento molto, da verificare quando serve realemnte, caso mai dal WCL richiamare  /moduli/{idModulo}/attributi
				.collect(Collectors.toList());
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoModuliAbilitati] Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli abilitati");
 		}
	}
	@Override
	public List<Modulo> getElencoModuliAbilitati(UserInfo user, Long idModulo, String onlyLastVersione, String otherIdentificativoUtente, String pubblicatoBO) throws BusinessException {
		try {
			log.info("[" + CLASS_NAME + "::getElencoModuliAbilitati] BEGIN idModulo=" + idModulo + " onlyLastVersione=" + onlyLastVersione + " otherIdentificativoUtente=" + otherIdentificativoUtente + " pubblicatoBO=" + pubblicatoBO);
			if (otherIdentificativoUtente==null) {
				return getElencoModuliAbilitati(user, idModulo, onlyLastVersione, pubblicatoBO);
			}
			// Ricerca moduli abilitati per l'utente connesso
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
//			if(!DecodificaTipoUtente.ADM.isCorrectType(user)) {
//				filter.setNomePortale(user.getPortalName());
//			}
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			filter.setUtenteAbilitato(otherIdentificativoUtente);
			if ("S".equals(onlyLastVersione) || "true".equals(onlyLastVersione)) {
				filter.setOnlyLastVersione(Boolean.TRUE);
			}
			
			if ("S".equals(pubblicatoBO) || "true".equals(pubblicatoBO)) {
				filter.setStatoModulo(DecodificaStatoModulo.PUBBLICATO);
				filter.setAttributoPresente(ModuloAttributoKeys.COMPILA_BO.getKey());
			}
			return moduloDAO.find(filter).stream()
				.map(ModuloMapper::buildFromModuloVersionatoEntity)
				.collect(Collectors.toList());
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoModuliAbilitati] "+otherIdentificativoUtente+" Errore invocazione DAO", e);
			throw new BusinessException("Errore recupero elenco moduli abilitati");
 		}
	}

	/**
	 * Completa l'oggetto Modulo della struttura che servire per gli applicativi client WCL
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloStruttura(Modulo modulo) throws DAOException {
		ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(modulo.getIdVersioneModulo());
		modulo.setStruttura(struttura.getStruttura());
		modulo.setTipoStruttura(struttura.getTipoStruttura());
		modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo della struttura che servire per gli applicativi client WCL con replace url su BO
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloStrutturaBO(Modulo modulo) {
		
		try {
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(modulo.getIdVersioneModulo());
			modulo.setStruttura(modificaContesto(struttura.getStruttura(), modulo.getIdModulo()));
			modulo.setTipoStruttura(struttura.getTipoStruttura());
			modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
		}
		catch(Exception e) {			
			log.error("[" + CLASS_NAME + "::completaModuloStrutturaBO] ", e);
			throw new BusinessException("Errore completaModuloStrutturaBO");
		}
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo dei attributi che possono servire per gli applicativi client WCL
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloObjAttributiBO(Modulo modulo) throws DAOException {
		modulo.setObjAttributi(toJson(moduloAttributiDAO.findByIdModulo(modulo.getIdModulo())));
		return modulo;
	}
	/**
	 * Completa l'oggetto Modulo della lista degli attributi
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloAttributi(Modulo modulo) {
		modulo.setAttributi(
				moduloAttributiDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.map(ModuloAttributoMapper::remap)
				.collect(Collectors.toList()));
		return modulo;
	}
	private Modulo completaModuloAttributiFilter(Modulo modulo, Set<String> FILTER_SET) {
		modulo.setAttributi(
				moduloAttributiDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.filter(ma -> FILTER_SET.contains(ma.getNomeAttributo()))
				.map(ModuloAttributoMapper::remap)
				.collect(Collectors.toList()));
		return modulo;
	}
	/**
	 * Completa l'oggetto Modulo della categoria
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloCategoria(Modulo modulo) throws DAOException {
		modulo.setCategoria(
			categoriaDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.findFirst()
				.map(CategoriaMapper::buildFromCategoriaEntity)
				.orElse(null));
		return modulo;
	}

	/**
	 * Completa l'oggetto Modulo del suo processo
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloProcesso(Modulo modulo) throws DAOException {
		modulo.setProcesso(processiService.getProcessoByIdModulo(modulo.getIdModulo()));
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo dei portali di pubblicazione
	 * @param modulo
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloPortali(Modulo modulo) {
		modulo.setPortali(
			portaleDAO.findByIdModulo(modulo.getIdModulo()).stream()
				.map(PortaleMapper::buildFromEntity)
				.collect(Collectors.toList()));
		return modulo;
	}
	
	/**
	 * Completa l'oggetto Modulo delle versioni
	 * fields 2 possibilite: "versioni","versioniCurrentStato"
	 * @param modulo
	 * @param fields "currentStato" se deve essere limitato ad una riga per versione con il suo stato corrente. "" se devono essere estratti tutti stati della cronologia stato.
	 * @return
	 * @throws DAOException
	 */
	private Modulo completaModuloVersioni(Modulo modulo, String fields) {
		modulo.setVersioni(
				moduloDAO.findVersioniModuloById(modulo.getIdModulo(),fields ));
		return modulo;
	}
	
	@Override
	public String getStrutturaByIdStruttura(UserInfo user, Long idStruttura) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] BEGIN");
			log.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] IN idStruttura:"+idStruttura);
		}
		try {
			ModuloStrutturaEntity struttura = strutturaDAO.findById(idStruttura);
			String strutturaJson = struttura.getStruttura();
			/*
			 * Modifico il contesto per consentire di visualizzare dati recuperati dai servizi
			 */
			try {
				strutturaJson = modificaContesto(strutturaJson, struttura.getIdModulo());
			} catch (Exception e) {
				log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore modificaContesto" );
			}
			return strutturaJson;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Risorsa nont trovata idStruttura="+idStruttura);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] DAOException idStruttura="+idStruttura, e);
			throw new BusinessException("Errore ricerca struttura per id");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] END");
			}
		}
	}
	
	@Override
	@Transactional
	public Modulo insertModulo(UserInfo user, Modulo modulo) throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::insertModulo] BEGIN");
		}
		try {
			Date now = new Date();
			String attoreIns = user.getIdentificativoUtente();
	
			// MODULO
			ModuloEntity moduloEntity = new ModuloEntity();
			moduloEntity.setCodiceModulo(modulo.getCodiceModulo() );
			moduloEntity.setAttoreUpd(attoreIns);
			moduloEntity.setDataIns(now);
			moduloEntity.setDataUpd(now); // NOT NULL !
			moduloEntity.setDescrizioneModulo(modulo.getDescrizioneModulo() );
			moduloEntity.setFlagIsRiservato("N");
			moduloEntity.setFlagProtocolloIntegrato("N");
			moduloEntity.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza());
			moduloEntity.setOggettoModulo(modulo.getOggettoModulo());
			Long idModulo;
			try {
				idModulo = moduloDAO.insert(moduloEntity);
			} catch (DAOException daoe) {
				log.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO moduloDAO", daoe);
				throw new BusinessException(daoe);
			}
			modulo.setIdModulo(idModulo);
			
			// VERSIONE MODULO
			ModuloVersioneEntity versioneEntity = new ModuloVersioneEntity();
			versioneEntity.setIdModulo(idModulo);
			versioneEntity.setVersioneModulo(modulo.getVersioneModulo());
			versioneEntity.setDataUpd(now);
			versioneEntity.setAttoreUpd(attoreIns);
			Long idVersioneModulo;
			try {
				idVersioneModulo = moduloDAO.insertVersione(versioneEntity);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::insertVersione] Errore invocazione DAO moduloDAO", e);
				throw new BusinessException("Errore inserimento Versione");
			}
			modulo.setIdVersioneModulo(idVersioneModulo);
			
			// CRONOLOGIA
			ModuloCronologiaStatiEntity cron = new ModuloCronologiaStatiEntity();
			cron.setIdVersioneModulo(idVersioneModulo);
			cron.setIdStato(DecodificaStatoModulo.IN_COSTRUZIONE.getId());
			cron.setDataInizioValidita(now);
			cron.setDataFineValidita(null);
			try {
				Long idCron = moduloDAO.insertCronologia(cron);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO cronologiaDAO", e);
				throw new BusinessException("Errore inserimento modulo-cronologia");
			}
			
			// Progressivo
			ModuloProgressivoEntity progressivo = new ModuloProgressivoEntity();
			progressivo.setIdModulo(idModulo);
			progressivo.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza());
			progressivo.setProgressivo(0L);
			progressivo.setAnnoRiferimento(Calendar.getInstance().get(Calendar.YEAR));
			progressivo.setLunghezza(7);
			try {
				moduloProgressivoDAO.insert(progressivo);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO progressivoDAO", e);
				throw new BusinessException("Errore inserimento progressivo modulo");
			}

			// STRUTTURA
			ModuloStrutturaEntity struttura = new ModuloStrutturaEntity();
			struttura.setIdVersioneModulo(idVersioneModulo);
			struttura.setIdModulo(idModulo);
			struttura.setTipoStruttura(modulo.getTipoStruttura());
			struttura.setStruttura(StringUtils.isEmpty(modulo.getStruttura())?getDefaultStructure(modulo):modulo.getStruttura());
			try {
				Long idStruttura = strutturaDAO.insert(struttura);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO strutturaDAO", e);
				throw new BusinessException("Errore inserimento modulo-struttura");
			}
			
			// PROCESSO
			processoModuloDAO.insert(new ProcessoModuloEntity(idModulo, 
				(modulo.getProcesso()!=null?(modulo.getProcesso().getIdProcesso()!=null?modulo.getProcesso().getIdProcesso():1L):1L)));
			
			insertUtenteModuloIfNecessary(user, modulo);

			// CATEGORIA
			return completaModuloCategoriaAggiornaIfNecessary(user, modulo);

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::insertModulo] Errore invocazione DAO ", e);
			throw new BusinessException("Errore aggiornamento struttura");
		}
	}

	
	private String getDefaultStructure(Modulo modulo) {
		return "{}";
		// TOVERIFY magari creare un oggetto Struttura usando il codiceModulo e altri attributi
	}
	
	private void insertUtenteModuloIfNecessary(UserInfo user, Modulo modulo) {
		if (DecodificaTipoUtente.ADM.isCorrectType(user)) {
			return ; // non serve l'inserimento di auth per ADM
		}
		UtenteEntity utente = utenteDAO.findByIdentificativoUtente(user.getIdentificativoUtente());
		if (utente != null) {
			UtenteModuloEntity utenteModulo;
			try {
				utenteModulo = utenteModuloDAO.findByIdUtenteModulo(utente.getIdUtente(), modulo.getIdModulo());
			} catch (ItemNotFoundDAOException e) {
				log.debug("[" + CLASS_NAME + "::insertUtenteModuloIfNecessary] associazione utente-modulo non trovata", e );
				utenteModulo = new UtenteModuloEntity();			
				utenteModulo.setIdModulo(modulo.getIdModulo());
				utenteModulo.setIdUtente(utente.getIdUtente());
				utenteModulo.setDataUpd(new Date());
				utenteModulo.setAttoreUpd(user.getIdentificativoUtente());
				utenteModuloDAO.insert(utenteModulo);
			} 
		}
	}

	@Override
	@Transactional
	public Modulo updateModulo(UserInfo user, Modulo modulo) throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::updateModulo] BEGIN");
		}
		try {
			Date now = new Date();
			String attoreIns = user.getIdentificativoUtente();

			// MODULO
			ModuloEntity moduloEntity = moduloDAO.findById(modulo.getIdModulo());
			moduloEntity.setCodiceModulo(modulo.getCodiceModulo() );
			moduloEntity.setAttoreUpd(attoreIns);
			moduloEntity.setDataUpd(now); // NOT NULL !
			moduloEntity.setDescrizioneModulo(modulo.getDescrizioneModulo() );
			moduloEntity.setFlagIsRiservato(Boolean.TRUE.equals(modulo.isFlagIsRiservato()) ? "S" : "N");
			moduloEntity.setFlagProtocolloIntegrato(Boolean.TRUE.equals(modulo.isFlagProtocolloIntegrato()) ? "S" : "N");
			moduloEntity.setIdTipoCodiceIstanza(modulo.getIdTipoCodiceIstanza()); //  FlagUsaProgressivo(modulo.isFlagUsaProgressivo() ? "S" : "N");
			moduloEntity.setOggettoModulo(modulo.getOggettoModulo());
			try {
				moduloDAO.update(moduloEntity);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO moduloDAO", e);
				throw new BusinessException("Errore aggiornamento modulo");
			}
			
			// VERSIONE MODULO
			ModuloVersioneEntity versioneEntity = new ModuloVersioneEntity();
			versioneEntity.setIdVersioneModulo(modulo.getIdVersioneModulo());
			versioneEntity.setIdModulo(modulo.getIdModulo());
			versioneEntity.setVersioneModulo(modulo.getVersioneModulo());
			versioneEntity.setDataUpd(now);
			versioneEntity.setAttoreUpd(attoreIns);
			try {
				moduloDAO.updateVersione(versioneEntity);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO moduloDAO", e);
				throw new BusinessException("Errore aggiornamento versione");
			}
			
			// STRUTTURA
			ModuloStrutturaEntity struttura = new ModuloStrutturaEntity();
			struttura.setIdModuloStruttura(modulo.getIdModuloStruttura());
			struttura.setIdVersioneModulo(modulo.getIdVersioneModulo());
			struttura.setIdModulo(modulo.getIdModulo());
			struttura.setTipoStruttura(modulo.getTipoStruttura());
			struttura.setStruttura(modulo.getStruttura());
			try {
				strutturaDAO.update(struttura);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO strutturaDAO", e);
				throw new BusinessException("Errore aggiornamento struttura");
			}
			
			// PROCESSO
			if (modulo.getProcesso()!=null && modulo.getProcesso().getIdProcesso()!=null) {
				ProcessoModuloEntity processoModuloE = new ProcessoModuloEntity(modulo.getIdModulo(),modulo.getProcesso().getIdProcesso());
				int numRecord = processoModuloDAO.updateProcesso(processoModuloE);
				if (numRecord==0) {
					numRecord = processoModuloDAO.insert(processoModuloE);
				}
			}
			
			// CATEGORIA
			return completaModuloCategoriaAggiornaIfNecessary(user, modulo);
			
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::updateModulo] Errore invocazione DAO ", e);
			throw new BusinessException("Errore aggiornamento struttura");
		}
	}

	private Modulo completaModuloCategoriaAggiornaIfNecessary(UserInfo user, Modulo modulo) throws DAOException {
		if (modulo.getCategoria()==null || modulo.getCategoria().getIdCategoria()==null) return modulo;
		Integer idCategoriaNew = modulo.getCategoria().getIdCategoria();
		// controllo l'esistente
		List<CategoriaEntity> categorie = categoriaDAO.findByIdModulo(modulo.getIdModulo());
		if (categorie!=null && !categorie.isEmpty()) {
			// le confronto per capire se aggiornarla o fare niente
			if (!categorie.get(0).getIdCategoria().equals(idCategoriaNew)) {
				// aggiorno DB : cancella e inserisci
				categoriaDAO.deleteCategoriaModuloByIdModulo(modulo.getIdModulo());
				categoriaDAO.insertCategoriaModulo(new CategoriaModuloEntity(idCategoriaNew, modulo.getIdModulo(), new Date(), user.getIdentificativoUtente()));
			}
			modulo.setCategoria(CategoriaMapper.buildFromCategoriaEntity(categorie.get(0)));
		} else {
			// devo inserire la categoria
			categoriaDAO.insertCategoriaModulo(new CategoriaModuloEntity(idCategoriaNew, modulo.getIdModulo(), new Date(), user.getIdentificativoUtente()));
		}
		return completaModuloCategoria(modulo);
	}
	
	@Override
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato) throws BusinessException {
		return cambiaStato(user, idModulo, idVersioneModulo, newStato, new Date());
	}
	
	@Override
	@Transactional
	public Modulo cambiaStato(UserInfo user, Long idModulo, Long idVersioneModulo, DecodificaStatoModulo newStato, Date inDataOra) throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::cambiaStato] BEGIN");
			log.debug("[" + CLASS_NAME + "::cambiaStato] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  newStato="+newStato.getCodice()+"  inDataOra="+inDataOra);
		}
		try {
			if (inDataOra==null) {
				inDataOra = new Date();
				log.debug("[" + CLASS_NAME + "::cambiaStato] inDataOra="+inDataOra);
			}
			ModuloCronologiaStatiEntity lastCron = moduloDAO.findLastCronologia(idVersioneModulo);
			DecodificaStatoModulo lastStato = DecodificaStatoModulo.byId(lastCron.getIdStato());
			if (!lastStato.getStatiDestinazione().contains(newStato.getCodice())) {
				log.error("[" + CLASS_NAME + "::cambiaStato] destinazione statoModulo " + newStato + " non prevista per idModulo=" + idModulo + 
					"  idVersioneModulo=" + idVersioneModulo + "  statoAttuale=" + lastStato.getCodice());
				throw new BusinessException("Stato modulo di destinazione impossibile", "MOONBOBL-30210");
			}
			if (inDataOra.before(lastCron.getDataInizioValidita())) { // lastCron.getDataInizioValidita()
				log.error("[" + CLASS_NAME + "::cambiaStato] Altri stati prenotati nel futuro  per idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
				throw new BusinessException("Errore data inizio validita ultimo stato è futuro al data del nuovo cambio stato. Inserire un data superiore a "+lastCron.getDataInizioValidita(), "MOONBOBL-30211");
			}
			if (DecodificaStatoModulo.PUBBLICATO == newStato) {
				// Se pubblichiamo, controllo che non ci sia già un'altra versione del modulo pubblicata
				if (esisteAlraVersioneModuloPubblicataAllaDataOra(idModulo, idVersioneModulo, inDataOra)) {
					log.error("[" + CLASS_NAME + "::cambiaStato] destinazione statoModulo " + newStato + " non possibile per idModulo=" + idModulo + 
						"  idVersioneModulo=" + idVersioneModulo + "  MOONBOBL-30212 Esiste già un'altra versione del modulo pubblicata.");
					throw new BusinessException("Esiste già un'altra versione del modulo pubblicata.", "MOONBOBL-30212");
				}
			}
			lastCron.setDataFineValidita(inDataOra);
			int r = moduloDAO.updateCronologia(lastCron);
			
			ModuloCronologiaStatiEntity newCron = new ModuloCronologiaStatiEntity();
			newCron.setIdVersioneModulo(idVersioneModulo);
			newCron.setIdStato(newStato.getId());
			newCron.setDataInizioValidita(inDataOra);
			newCron.setDataFineValidita(null);					
			Long idNewCron = moduloDAO.insertCronologia(newCron);
			return getModuloById(user, idModulo, idVersioneModulo, null);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::cambiaStato] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  newStato="+newStato.getCodice(), e);
			throw new BusinessException("Errore Cambia Stato Modulo", "MOONBOBL-30200");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::cambiaStato] END");
			}
		}
	}
	
	
	private boolean esisteAlraVersioneModuloPubblicataAllaDataOra(Long idModulo, Long idVersioneModulo, Date inDataOra) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::esisteAlraVersioneModuloPubblicataAllaDataOra] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  inDataOra="+inDataOra);
		}
		boolean result = true;
		try {
			ModuloVersionatoEntity moduloPubblicato = moduloDAO.findModuloVersionatoPubblicatoById(idModulo, inDataOra);
			log.warn("[" + CLASS_NAME + "::esisteAlraVersioneModuloPubblicataAllaDataOra] KO, Esite versione pubblicate alla data per idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  inDataOra="+inDataOra+"   moduloPubblicatoTrovato="+moduloPubblicato);
		} catch (ItemNotFoundDAOException notFound) {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::esisteAlraVersioneModuloPubblicataAllaDataOra] OK, non esite versione pubblicate alla data per idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  inDataOra="+inDataOra);
				result = false; // OK, non esite versione pubblicate alla data
			}
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::esisteAlraVersioneModuloPubblicataAllaDataOra] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo+"  inDataOra="+inDataOra, daoe);
			throw new BusinessException(daoe);
		}
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::esisteAlraVersioneModuloPubblicataAllaDataOra] result="+result);
		}
		return result;
	}
	
	private boolean existsOverFutureCron(ModuloCronologiaStatiEntity currentCron) {
		// TODO Verificare se exists altri record di cronologie oltre la data fine di quello passato in argument
		return false;
	}
	
	
	@Override
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getModuloByCodice] BEGIN");
			log.debug("[" + CLASS_NAME + "::getModuloByCodice] IN codiceModulo="+codiceModulo+"  versione="+versione);
		}
		try {
			ModuloVersionatoEntity moduloE = moduloDAO.findModuloVersionatoByCodice(codiceModulo, versione);
			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(moduloE.getIdVersioneModulo());

			Modulo modulo = ModuloMapper.buildFromModuloVersionatoEntity(moduloE);
			modulo.setStruttura(struttura.getStruttura());
			modulo.setTipoStruttura(struttura.getTipoStruttura());
			modulo.setIdModuloStruttura(struttura.getIdModuloStruttura());
			
			return completaModuloObjAttributiBO(modulo); //completaModuloCategoria(modulo));

		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getModuloByCodice] Risorsa nont trovata codiceModulo="+codiceModulo+"  versione="+versione);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getModuloByCodice] DAOException codiceModulo="+codiceModulo+"  versione="+versione, e);
			throw new BusinessException("Errore ricerca modulo per codice");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getModuloByCodice] END");
			}
		}
	}


	public boolean verificaAbilitazioneModulo(Long idModulo, UserInfo user) throws BusinessException {
		try {
			if (DecodificaTipoUtente.ADM.isCorrectType(user)) {
				return true; // non serve l'inserimento di auth per ADM
			}
			utenteModuloDAO.findByCfUtenteModulo(user.getIdentificativoUtente(), idModulo);
			return true;
		} catch (ItemNotFoundDAOException e) {
			return false;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::verificaAbilitazioneModulo] Errore", e);
			throw new BusinessException("Errore verifica abilitazione modulo");
 		}
	}

	private String toJson (List<ModuloAttributoEntity> moduloAttributi) throws BusinessException {
		try {
			JsonFactory jfactory = new JsonFactory();
		    Writer writer = new StringWriter();
		    JsonGenerator g = jfactory.createJsonGenerator(writer);
		    g.writeStartObject(); // {
		    g.writeObjectFieldStart("attributi"); // data: {

		    //g.writeArrayFieldStart("attributi"); // "attributi" : [

			for (ModuloAttributoEntity modAttr : moduloAttributi) {

			    g.writeStringField(modAttr.getNomeAttributo(), modAttr.getValore());

			}
		    //g.writeEndArray(); // ]

		    g.writeEndObject(); // } end of data
		    g.writeEndObject();
		    g.close();
		    return writer.toString();
		} catch(Exception e) {
	    	log.error("[" + CLASS_NAME + "::toJson] Errore", e);
	    	throw new BusinessException("ERROR ModuliServiceImpl.toJson");
	    }
	}
	
	@Override
	public Modulo updateStruttura(UserInfo user, Long idModulo, Long idVersioneModulo, String newStruttua) throws BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::updateStruttura] BEGIN");
			log.debug("[" + CLASS_NAME + "::updateStruttura] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
		}
		try {
			Date now = new Date();
			String attoreIns = user.getIdentificativoUtente();

			ModuloStrutturaEntity lastStruttura = strutturaDAO.findByIdVersioneModulo(idVersioneModulo);

			// STRUTTURA
			lastStruttura.setStruttura(newStruttua);
			try {
				strutturaDAO.update(lastStruttura);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::updateStruttura] Errore invocazione DAO strutturaDAO",e );
				throw new BusinessException("Errore aggiornamento struttura");
			}

			return getModuloById(user, idModulo, idVersioneModulo, "struttura");

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::updateStruttura] Errore invocazione DAO ", e);
			throw new BusinessException("Errore aggiornamento struttura");
		}
	}
	
	public String modificaContesto(String testo, Long idModulo) throws Exception
	{

	    Pattern p = Pattern.compile("moonfobl", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	    
	    String result = p.matcher(testo).replaceAll("moonbobl");
	    
	    if (idModulo.intValue() == 20 || 
	    		idModulo.intValue() == 21 || idModulo.intValue() == 28 ||
	    		idModulo.intValue() == 14 || idModulo.intValue() == 35 )
	    {
		    Pattern pvh = Pattern.compile("moon-torinofacile\\.patrim", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		    
		    result = pvh.matcher(result).replaceAll("moon-bo\\.patrim");
	    }

	        	    
	    return result;
	}

	@Override
	public List<ModuloAttributo> getAttributiModulo(Long idModulo) throws BusinessException {
		try {
			return moduloAttributiDAO.findByIdModulo(idModulo).stream()
				.map(ModuloAttributoMapper::remap)
				.collect(Collectors.toList());
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getAttributiModulo] Errore invocazione DAO for "+idModulo, e);
			throw new BusinessException("Errore ricerca attributi modulo");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getAttributiModulo] Errore for "+idModulo, e);
			throw new BusinessException("Errore generica getAttributiModulo");
		}
	}
	@Override
	public String getObjAttributiModuloBO(Long idModulo) throws BusinessException {
		try {
			return toJson(moduloAttributiDAO.findByIdModulo(idModulo));
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getAttributiModuloBO] Errore invocazione DAO for "+idModulo, e);
			throw new BusinessException("Errore ricerca attributi modulo BO");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getAttributiModuloBO] Errore for "+idModulo, e);
			throw new BusinessException("Errore generica getAttributiModuloBO");
		}
	}

	@Override
	public List<StatoModulo> getElencoStatiModulo(String codiceProvenienza, String codiceDestinazione) throws BusinessException {
		try {
			if (StrUtils.isEmpty(codiceProvenienza) && StrUtils.isEmpty(codiceDestinazione)) {
				return Arrays.stream(DecodificaStatoModulo.values())
					.map(StatoModuloMapper::buildFromDecodifica)
					.collect(Collectors.toList());
			} else {
				if (!StrUtils.isEmpty(codiceProvenienza) && StrUtils.isEmpty(codiceDestinazione)) {
					return DecodificaStatoModulo.byCodice(codiceProvenienza).getStatiDestinazione().stream()
						.map(statoModulo -> DecodificaStatoModulo.byCodice(statoModulo))
						.map(StatoModuloMapper::buildFromDecodifica)
						.collect(Collectors.toList());
				}
				if (StrUtils.isEmpty(codiceProvenienza) && !StrUtils.isEmpty(codiceDestinazione)) {
					return DecodificaStatoModulo.byCodice(codiceDestinazione).getStatiProvenienza().stream()
						.map(statoModulo -> DecodificaStatoModulo.byCodice(statoModulo))
						.map(StatoModuloMapper::buildFromDecodifica)
						.collect(Collectors.toList());
				}
				throw new BusinessException("NOT IMPLEMENTED.");
			}
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getElencoStatiModulo] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca statiModulo");
		} catch (BusinessException be) {
			throw be;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getElencoStatiModulo] Errore", e);
			throw new BusinessException("Errore generica getElencoStatiModulo");
		}
	}

	@Override
	public InitModulo getInitNuovaVersioneModuloById(UserInfo user, Long idModulo) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] BEGIN");
			log.debug("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] IN idModulo="+idModulo);
		}
		try {
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
			List<ModuloVersionatoEntity> moduli = moduloDAO.find(filter, moduloDAO.ORDER_BY_DATA_UPD_DESC);
			
			List<InitModuloVersione> versioni = moduli.stream().map(this::mapMV2InitModuloVersione).collect(Collectors.toList());
			ModuloVersionatoEntity moduloVersionatoMaggioreVersione = findModuloVersionatoMaggioreVersione(moduli);
			
			InitModulo initModulo = new InitModulo();
			initModulo.setIdModulo(idModulo);
			initModulo.setCodiceModulo(moduloVersionatoMaggioreVersione.getCodiceModulo());
			initModulo.setVersioni(versioni);
			initModulo.setUltimaVersione(moduloVersionatoMaggioreVersione.getVersioneModulo());
			String[] versioneSplits = moduloVersionatoMaggioreVersione.getVersioneModulo().split("\\.");
			initModulo.setUltimaMaggioreVersione(Integer.parseInt(versioneSplits[0]));
			initModulo.setUltimaMinoreVersione(Integer.parseInt(versioneSplits[1]));
			initModulo.setUltimaPatchVersione(Integer.parseInt(versioneSplits[2]));
			return initModulo;
		} catch (ItemNotFoundDAOException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] Risorsa non trovata idModulo="+idModulo);
			throw new ItemNotFoundBusinessException();
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] DAOException idModulo="+idModulo, e);
			throw new BusinessException("Errore DAO getInitNuovaVersioneModuloById");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] Exception idModulo="+idModulo, e);
			throw new BusinessException("Errore generica in getInitNuovaVersioneModuloById");
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getInitNuovaVersioneModuloById] END");
			}
		}
	}
	private ModuloVersionatoEntity findModuloVersionatoMaggioreVersione(List<ModuloVersionatoEntity> moduli) {
		ModuloVersionatoEntity moduloVersionatoMaggioreVersione = null;
		for (ModuloVersionatoEntity moduloVersionato : moduli) {
			if (moduloVersionatoMaggioreVersione==null) {
				moduloVersionatoMaggioreVersione = moduloVersionato;
			} else {
				if (compareVersions(moduloVersionato.getVersioneModulo(),moduloVersionatoMaggioreVersione.getVersioneModulo())>0) {
					moduloVersionatoMaggioreVersione = moduloVersionato;
				}
			}
		}
		return moduloVersionatoMaggioreVersione;
	}
	private InitModuloVersione mapMV2InitModuloVersione(ModuloVersionatoEntity moduloVersionato) {
		InitModuloVersione imv = new InitModuloVersione();
		imv.setIdVersioneModulo(moduloVersionato.getIdVersioneModulo());
		imv.setVersione(moduloVersionato.getVersioneModulo());
		imv.setStato(StatoModuloMapper.buildFromModuloVersionatoEntity(moduloVersionato));
		return imv;
	}
	public static int compareVersions(String version1, String version2) {
	    int comparisonResult = 0;
	    
	    String[] version1Splits = version1.split("\\.");
	    String[] version2Splits = version2.split("\\.");
	    int maxLengthOfVersionSplits = Math.max(version1Splits.length, version2Splits.length);

	    for (int i = 0; i < maxLengthOfVersionSplits; i++){
	        Integer v1 = i < version1Splits.length ? Integer.parseInt(version1Splits[i]) : 0;
	        Integer v2 = i < version2Splits.length ? Integer.parseInt(version2Splits[i]) : 0;
	        int compare = v1.compareTo(v2);
	        if (compare != 0) {
	            comparisonResult = compare;
	            break;
	        }
	    }
	    return comparisonResult;
	}
	
	@Override
	@Transactional
	public void deleteModulo(UserInfo user, Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::deleteModulo] BEGIN " + idModulo + "/" + idVersioneModulo);
		}
		try {
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			if (!DecodificaStatoModulo.IN_COSTRUZIONE.isCorrectId(entity)) {
				String msg = "Modulo " + idModulo + "/" + idVersioneModulo + " non in stato INIT. idStato = " + entity.getIdStato();
				log.error("[" + CLASS_NAME + "::deleteModulo] " + msg);
				throw new BusinessException(msg);
			}
			//
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
			boolean cancellaSoloLaVersione = (moduloDAO.find(filter).stream().count()>1);

			if (cancellaSoloLaVersione) {
				// MODULO VERSIONATO
				strutturaDAO.delete(idVersioneModulo);
				moduloDAO.deleteCronologia(idVersioneModulo);
				moduloDAO.deleteVersione(idVersioneModulo);
			} else {
// TODO			FAQ&Supporto
// TODO			protocollo
				moduloAttributiDAO.deleteAllByIdModulo(idModulo);
				processoModuloDAO.deleteAllByIdModulo(idModulo);
				categoriaDAO.deleteCategoriaModuloByIdModulo(idModulo);
				portaleModuloDAO.deleteAllByIdModulo(idModulo);
				
				// MODULO VERSIONATO
				strutturaDAO.delete(idVersioneModulo);
				moduloDAO.deleteCronologia(idVersioneModulo);
				moduloDAO.deleteVersione(idVersioneModulo);

				// MODULO
				moduloProgressivoDAO.delete(idModulo);				
				utenteModuloDAO.deleteAllByIdModulo(idModulo);
				moduloDAO.delete(idModulo);
			}

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::deleteModulo] Errore invocazione DAO ", e);
			throw new BusinessException("Errore cancellazione modulo");
		}
	}
	
	@Override
	@Transactional
	public void savePortaliModulo(UserInfo user, Long idModulo, Long idVersioneModulo, final List<Long> nuoviPortali) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::savePortaliModulo] BEGIN " + idModulo + "/" + idVersioneModulo);
		}
		try {
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			final List<Long> portaliAttuali = portaleDAO.findByIdModulo(idModulo).stream()
				.map(p -> p.getIdPortale())
				.collect(Collectors.toList());
			log.debug("[" + CLASS_NAME + "::savePortaliModulo] portaliAttuali " + portaliAttuali);
			if (portaliAttuali.size() > 0 ) { // REMOVE
				List<Long> portaliToDelete = new ArrayList<Long>(portaliAttuali);
				portaliToDelete.removeAll(nuoviPortali);
				log.debug("[" + CLASS_NAME + "::savePortaliModulo] portaliToDelete " + portaliToDelete);
				for (Long idPortale : portaliToDelete) {
					portaleModuloDAO.delete(idPortale,idModulo);
				}
			}
			if (nuoviPortali.size() > 0 ) {	// ADD
				List<Long> portaliToAdd = new ArrayList<Long>(nuoviPortali);
				portaliToAdd.removeAll(portaliAttuali);
				log.debug("[" + CLASS_NAME + "::savePortaliModulo] portaliToAdd " + portaliToAdd);
				for (Long idPortale : portaliToAdd) {
					portaleModuloDAO.insert(new PortaleModuloEntity(idPortale, idModulo, new Date(), user.getIdentificativoUtente()));
				}
			}
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::savePortaliModulo] Errore invocazione DAO ", e);
			throw new BusinessException("Errore savePortaliModulo");
		}
	}
	
	@Override
	public Modulo nuovaVersione(UserInfo user, Long idModulo, NuovaVersioneModuloRequest body) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::nuovaVersione] BEGIN idModulo:" + idModulo + "   body:" + body);
		}
		try {
			Date now = new Date();
			String attoreIns = user.getIdentificativoUtente();
			// Valida che body.getIdVersionePartenza() sia del idModulo
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, body.getIdVersionePartenza());
			// Valida che la versione sia maggiore a la maggiore esistente
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
			List<ModuloVersionatoEntity> moduli = moduloDAO.find(filter, moduloDAO.ORDER_BY_DATA_UPD_DESC);
			ModuloVersionatoEntity moduloVersionatoMaggioreVersione = findModuloVersionatoMaggioreVersione(moduli);
			if (compareVersions(body.getVersione(),moduloVersionatoMaggioreVersione.getVersioneModulo())<=0) {
				throw new BusinessException("La nuova versione deve essere maggiore di "+moduloVersionatoMaggioreVersione.getVersioneModulo(),"ERRCLI-100100");
			}
			
			// VERSIONE MODULO
			ModuloVersioneEntity versioneEntity = new ModuloVersioneEntity();
			versioneEntity.setIdModulo(idModulo);
			versioneEntity.setVersioneModulo(body.getVersione());
			versioneEntity.setDataUpd(now);
			versioneEntity.setAttoreUpd(attoreIns);
			Long idVersioneModulo;
			try {
				idVersioneModulo = moduloDAO.insertVersione(versioneEntity);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::nuovaVersione] Errore invocazione DAO moduloDAO", e);
				throw new BusinessException("Errore inserimento nuovaVersione");
			}
			
			// CRONOLOGIA
			ModuloCronologiaStatiEntity cron = new ModuloCronologiaStatiEntity();
			cron.setIdVersioneModulo(idVersioneModulo);
			cron.setIdStato(DecodificaStatoModulo.IN_COSTRUZIONE.getId());
			cron.setDataInizioValidita(now);
			cron.setDataFineValidita(null);
			try {
				Long idCron = moduloDAO.insertCronologia(cron);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::nuovaVersione] Errore invocazione DAO cronologiaDAO", e);
				throw new BusinessException("Errore inserimento nuovaVersione-cronologia");
			}
			
			// STRUTTURA
//			ModuloStrutturaEntity struttura = strutturaDAO.findByIdVersioneModulo(body.getIdVersionePartenza());
//			struttura.setIdModuloStruttura(null);
//			struttura.setIdVersioneModulo(idVersioneModulo);
			try {
				Long idStruttura = strutturaDAO.copy(idModulo, body.getIdVersionePartenza(), idVersioneModulo);
//				Long idStruttura = strutturaDAO.insert(struttura);
			} catch (DAOException e) {
				log.error("[" + CLASS_NAME + "::nuovaVersione] Errore invocazione DAO strutturaDAO", e);
				throw new BusinessException("Errore inserimento nuovaVersione-struttura");
			}
			
			return getModuloById(user, idModulo, idVersioneModulo, "struttura,portali") ;

		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::nuovaVersione] Errore invocazione DAO ", e);
			throw new BusinessException("Errore aggiornamento struttura");
		}
	}
	
	@Override
	public InitModuloCambiaStato getInitCambiaStatoById(UserInfo user, Long idModulo, Long idVersioneModulo) throws ItemNotFoundBusinessException, BusinessException {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getInitCambiaStatoById] BEGIN");
			log.debug("[" + CLASS_NAME + "::getInitCambiaStatoById] IN idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
		}
		try {
			ModuloVersionatoEntity entity = moduloDAO.findModuloVersionatoById(idModulo, idVersioneModulo);
			List<StatoModulo> allCronologie = moduloDAO.findAllCronologia(idVersioneModulo).stream()
				.map(StatoModuloMapper::buildFromModuloCronologiaStatiEntity)
				.collect(Collectors.toList());
			ModuloCronologiaStatiEntity lastCron = moduloDAO.findLastCronologia(idVersioneModulo);
			//
			InitModuloCambiaStato result = new InitModuloCambiaStato();
			result.setIdModulo(entity.getIdModulo());
			result.setIdVersioneModulo(entity.getIdVersioneModulo());
			result.setCodiceModulo(entity.getCodiceModulo());
			result.setCronologia(allCronologie);
			result.setDataMinCambioStato(lastCron.getDataInizioValidita());
			return result;
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::getInitCambiaStatoById] DAOException idModulo="+idModulo+"  idVersioneModulo="+idVersioneModulo);
			throw new BusinessException(daoe);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getInitCambiaStatoById] END");
			}
		}

	}
	
	@Override
	public List<ModuloVersioneStato> getVersioniModuloById(Long idModulo, String fields) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getVersioniModuloById] BEGIN");
			log.debug("[" + CLASS_NAME + "::getVersioniModuloById] IN idModulo="+idModulo+"  fields="+fields);
		}
		return moduloDAO.findVersioniModuloById(idModulo, fields);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiGenerali(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiGenerali] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiGenerali] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_GENERALI);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiEmail(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEmail] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEmail] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_EMAIL);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiNotificatore(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiNotificatore] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiNotificatore] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_NOTIFY);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiProtocollo(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiProtocollo] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiProtocollo] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_PROTOCOLLO);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiCosmo(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiCosmo] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiCosmo] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_COSMO);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiAzione(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiAzione] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiAzione] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_AZIONE);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiEstraiDichiarante(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEstraiDichiarante] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEstraiDichiarante] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_ESTRAI_DICH);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiCrm(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiCrm] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiCrm] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_CRM);
	}
	
	@Override
	public List<ModuloAttributo> aggiornaInserisciAttributiEpay(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEpay] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiEpay] IN idModulo="+idModulo+"  attributi="+attributiDaSalvalre);
		}
		return aggiornaInserisciAttributiSet(idModulo, attributiDaSalvalre, user, ATTRIBUTI_EPAY);
	}
	
	private List<ModuloAttributo> aggiornaInserisciAttributiSet(Long idModulo, ModuloAttributo[] attributiDaSalvalre, UserInfo user, Set<String> FILTER_SET) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] BEGIN");
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] IN idModulo="+idModulo+"  attributiDaSalvalre="+attributiDaSalvalre);
			for (ModuloAttributo a : attributiDaSalvalre) {
				log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] IN attributo: " + a.toString());
			}
		}
		int upd = 0, ins= 0;
		List<ModuloAttributoEntity> currentAttributi = moduloAttributiDAO.findByIdModulo(idModulo);
		for (ModuloAttributo a : attributiDaSalvalre) {
			if (!FILTER_SET.contains(a.getNome())) {
				log.warn("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] IGNORED a: " + a + " NOT attribut OF " + FILTER_SET);
			}
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] FOR a: " + a);
			ModuloAttributoEntity currentAttr = currentAttributi.stream()
				.filter(ca -> a.getNome().equals(ca.getNomeAttributo()))
				.findAny()
				.orElse(null);
			if (currentAttr != null) {
				if (!a.getValore().equals(currentAttr.getValore())) {
					log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] UPDATE " + a.getNome() + " with ID " + currentAttr.getIdAttributo());
					currentAttr.setValore(a.getValore());
					currentAttr.setDataUpd(new Date());
					currentAttr.setAttoreUpd(user.getIdentificativoUtente());
					moduloAttributiDAO.update(currentAttr);
					upd++;
				} else {
					log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] STESSO VALORE (abitualmente gia non trasmesso dal WCL) " + a.getNome() + " with ID " + currentAttr.getIdAttributo());
				}
			} else {
				log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] INSERT " + a.getNome() );
				currentAttr = new ModuloAttributoEntity();
				currentAttr.setIdModulo(idModulo);
				currentAttr.setNomeAttributo(a.getNome());
				currentAttr.setValore(a.getValore());
				currentAttr.setDataUpd(new Date());
				currentAttr.setAttoreUpd(user.getIdentificativoUtente());
				moduloAttributiDAO.insert(currentAttr);
				ins++;
			} 
		}
		List<ModuloAttributo> result = moduloAttributiDAO.findByIdModulo(idModulo).stream()
				.filter(ma -> FILTER_SET.contains(ma.getNomeAttributo()))
				.map(ModuloAttributoMapper::remap)
				.collect(Collectors.toList());
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] idModulo="+idModulo+"  updated:"+upd+"  inserted:"+ins);
			log.debug("[" + CLASS_NAME + "::aggiornaInserisciAttributiSet] result=" + result.stream().map(Object::toString).collect(Collectors.joining(";")));
		}
		return result;
	}
	
	@Override
	public List<UtenteModuloAbilitato> getUtentiAbilitati(Long idModulo, String fields) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::getUtentiAbilitati] IN idModulo="+idModulo+"  fields="+fields);
		}
		try {
			List<UtenteModuloAbilitato> result = utenteModuloDAO.findUtenteModuloAbilitatoByIdModulo(idModulo).stream()
				.map(UtenteModuloAbilitatoMapper::buildFromEntity)
				.collect(Collectors.toList());
			return result;
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::getUtentiAbilitati] Errore invocazione DAO for "+idModulo, daoe);
			throw new BusinessException(daoe);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::getUtentiAbilitati] Errore for "+idModulo, e);
			throw new BusinessException("Errore generica getUtentiAbilitati");
		}
	}
	
	@Override
	public boolean hasProtocolloParameters(Long idModulo) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::hasProtocolloParameters] IN idModulo="+idModulo);
		}
		try {
			return protocolloParametroDAO.countByIdModulo(idModulo)>0?true:false;
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::hasProtocolloParameters] Errore invocazione DAO for "+idModulo, daoe);
			throw new BusinessException(daoe);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::hasProtocolloParameters] Errore for "+idModulo, e);
			throw new BusinessException("Errore generica hasProtocolloParameters");
		}
	}
	
	@Override
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws ItemNotFoundBusinessException, TooManyItemFoundBusinessException, BusinessException {
		try {
			return moonsrvDAO.getModuloPubblicatoByCodice(codiceModulo);
		} catch (DAOException e) {
			log.error("[" + CLASS_NAME + "::getModuloPubblicatoByCodice] Errore invocazione DAO", e);
			throw new BusinessException("Errore ricerca modulo pubblicato per codice");
		}
	}

	@Override
	public boolean hasProtocolloBo(Long idModulo) {
		if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::hasProtocolloBo] IN idModulo="+idModulo);
		}
		try {
			List<Workflow> wList = workflowService.getElencoAzioniPossibili(
					WorkflowFilter.builder()
						.idModulo(idModulo)
						.idAzione(DecodificaAzione.PROTOCOLLA.getIdAzione()).build());
			return (wList==null || wList.isEmpty()) ? false : true;
		} catch (DAOException daoe) {
			log.error("[" + CLASS_NAME + "::hasProtocolloBo] Errore invocazione DAO for "+idModulo, daoe);
			throw new BusinessException(daoe);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::hasProtocolloBo] Errore for "+idModulo, e);
			throw new BusinessException("Errore generica hasProtocolloBo");
		}
	}
	
}
