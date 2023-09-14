/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.IstanzeApi;
import it.csi.moon.moonbobl.business.service.AllegatiService;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.CosmoService;
import it.csi.moon.moonbobl.business.service.IstanzeService;
import it.csi.moon.moonbobl.business.service.LogEmailService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.UtentiService;
import it.csi.moon.moonbobl.business.service.impl.dao.EpayNotificaPagamentoDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.EpayRichiestaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.EpayRichiestaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter.EnumFilterFlagArchiviata;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorterBuilder;
import it.csi.moon.moonbobl.business.service.impl.dto.MyDocsRichiestaEntity;
import it.csi.moon.moonbobl.business.service.mapper.PagamentoMapper;
import it.csi.moon.moonbobl.business.service.mapper.PagamentoNotificaMapper;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.Allegato;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.LogEmail;
import it.csi.moon.moonbobl.dto.moonfobl.LogPraticaCosmo;
import it.csi.moon.moonbobl.dto.moonfobl.LogServizioCosmo;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.Pagamento;
import it.csi.moon.moonbobl.dto.moonfobl.PagamentoNotifica;
import it.csi.moon.moonbobl.dto.moonfobl.ResponsePaginated;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnauthorizedBusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnauthorizedException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.HeadersUtils;
import it.csi.moon.moonbobl.util.JacksonUtils;
import it.csi.moon.moonbobl.util.LoggerAccessor;


@Component
public class IstanzeApiImpl extends MoonBaseApiImpl implements IstanzeApi {
	
	private final static String CLASS_NAME = "IstanzeApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	private IstanzeService istanzeService;
	@Autowired
	private ModuliService moduliService;
	@Autowired
	private UtentiService utentiService;
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	EpayRichiestaDAO epayRichiestaDAO;	
	@Autowired
	EpayNotificaPagamentoDAO epayNotificaPagamentoDAO;
	@Autowired 
	AuditService auditService;
	@Autowired
	LogEmailService logEmailService;
	@Autowired
	CosmoService cosmoService;
	@Autowired
	MoonsrvDAO moonSrvDAO;
	
	public Response getInitIstanza(String idModuloPP, String idVersioneModuloPP, IstanzaInitBLParams params,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN idModuloPP: "+idModuloPP);
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN idVersioneModuloPP: "+idVersioneModuloPP);
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN params: "+params);
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getInitIstanza] IN user: "+user);
			Istanza istanza = istanzeService.getInitIstanza(user, idModulo, idVersioneModulo, params, httpRequest);
			
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"InitIstanza-idModulo-idVersioneModulo", 
					idModuloPP + "-" + idVersioneModulo);
			auditService.traceOperazione(auditEntity);
			
			return Response.ok(istanza).build();
		} catch (UnivocitaIstanzaBusinessException uibe) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] UnivocitaIstanzaBusinessException getInitIstanza " + uibe);
			throw new ServiceException(uibe.getMessage(),uibe.getCode());
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore servizio getInitIstanza",e);
			throw new ServiceException("Errore servizio inizializzazione istanza");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore UnprocessableEntityException getInitIstanza",uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getInitIstanza] Errore generico servizio getInitIstanza",ex);
			throw new ServiceException("Errore generico servizio inizializzazione istanza");
		} 
	}

	public Response getIstanzaById(Long idIstanza,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: "+idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			String nomePortale = (String) httpRequest.getSession().getAttribute(Constants.SESSION_PORTALNAME);
			Istanza istanza = istanzeService.getIstanzaCompletaById(user, idIstanza, nomePortale);
			// Audit Operazione
			auditService.getIstanza(httpRequest.getRemoteAddr(), user, idIstanza,nomePortale);
			return Response.ok(istanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] modulo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore servizio getIstanzaById",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore generico servizio getIstanzaById",ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}
 
	public Response getIstanzaBozzaById(Long idIstanza,SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanzaById] IN idIstanza: "+idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanza = istanzeService.getIstanzaBozzaById(user, idIstanza);
			// Audit Operazione
						AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
								auditService.retrieveUser(user), 
								AuditEntity.EnumOperazione.READ,
								"getIstanzaBozza-idIstanza", 
								idIstanza.toString());
						auditService.traceOperazione(auditEntity);
			return Response.ok(istanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] modulo non trovato",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore servizio getIstanzaById",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzaById] Errore generico servizio getIstanzaById",ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}
	
	public Response getIstanze(Integer stato, String sort, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanze] IN stato: "+stato);
			log.debug("[" + CLASS_NAME + "::getIstanze] IN sort: "+sort);
			
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getIstanze] utente abilitato: "+ user.getIdentificativoUtente());
			
			IstanzeFilter filter = new IstanzeFilter();

			List<Modulo> elencoModuli = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
			// Audit Operazione
						AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
								auditService.retrieveUser(user), 
								AuditEntity.EnumOperazione.READ,
								"elencoModuliAbilitati", 
								"");
						auditService.traceOperazione(auditEntity);
			if (elencoModuli == null)
			{
				throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
			}
			ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
			if (elencoModuli.size() > 0)
			{
				for (Modulo  modulo : elencoModuli) {
					elencoIdModuli.add(modulo.getIdModulo());					
				}
				filter.setIdModuli(elencoIdModuli);
			}
		
			// considero solo le istanze che non sono state eliminate
//			filter.setStatoEliminata(EnumFilterStatoEliminata.ATTIVI);

			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(stato, filter, optSorter, null);
			return Response.ok(elenco).build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze",ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}
  
	public Response getPdf(Istanza body, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		// do some magic!
		return Response.ok().build();
	}
  
	public Response putIstanza(Long idIstanza, Istanza body, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::putIstanza] IN idIstanza:"+idIstanza+"\nIstanza body: "+body);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanza = istanzeService.saveIstanza(user, idIstanza, body);
			// Audit Operazione
						AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
								auditService.retrieveUser(user), 
								AuditEntity.EnumOperazione.UPDATE,
								"PUT_Istanza-idIstanza", 
								idIstanza.toString() );
						auditService.traceOperazione(auditEntity);
			return Response.ok(istanza).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putIstanza] Errore servizio putIstanza",e);
			throw new ServiceException("Errore servizio aggiorna istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putIstanza] Errore generico servizio putIstanza",ex);
			throw new ServiceException("Errore generico servizio aggiorna istanza");
		}
	}

	public Response saveIstanza(Istanza body, SecurityContext securityContext, HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::saveIstanza] IN Istanza body: "+body);
			UserInfo user = retrieveUserInfoWithRoles(httpRequest);
			Istanza istanza = istanzeService.saveIstanza(user, body);
			// Audit Operazione
				
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					body.getIdIstanza() == null ? AuditEntity.EnumOperazione.INSERT:AuditEntity.EnumOperazione.UPDATE,
					"SAVE_Istanza-idIstanza", 
					body.getIdIstanza() == null ? null : body.getIdIstanza().toString() );
		
			auditService.traceOperazione(auditEntity);
		
			return Response.ok(istanza).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore servizio saveIstanza",e);
			throw new ServiceException("Errore servizio salva istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore generico servizio saveIstanza",ex);
			throw new ServiceException("Errore generico servizio salva istanza");
		} 
	}

	@Override
	public Response deleteIstanzaById(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::deleteIstanzaById] IN idIstanza: "+idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanza = istanzeService.deleteIstanza(user, idIstanza);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.DELETE,
					"idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(istanza).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::deleteIstanzaById] istanza non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::deleteIstanzaById] Errore servizio deleteIstanzaById",e);
			throw new ServiceException("Errore servizio delete istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::deleteIstanzaById] Errore generico servizio deleteIstanzaById",ex);
			throw new ServiceException("Errore generico servizio delete istanza");
		} 
	}

	@Override
	public Response patchIstanzaById(Long idIstanza, Istanza partialIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::patchIstanzaById] IN idIstanza:"+idIstanza+"\nIstanza body: "+partialIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, partialIstanza);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.UPDATE,
					"PATCH_Istanza-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(istanza).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::patchIstanzaById] Errore servizio patchIstanzaById",e);
			throw new ServiceException("Errore servizio aggiorna partial istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::patchIstanzaById] Errore generico servizio patchIstanzaById",ex);
			throw new ServiceException("Errore generico servizio aggiorna partial istanza");
		}
    }

	@Override
	public Response starIstanza(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::starIstanza] IN idIstanza:"+idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanzaStared = new Istanza();
			istanzaStared.setImportanza(1);
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, istanzaStared);
			return Response.ok(istanza).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::starIstanza] Errore servizio starIstanza",e);
			throw new ServiceException("Errore servizio star istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::starIstanza] Errore generico servizio starIstanza",ex);
			throw new ServiceException("Errore generico servizio star istanza");
		}
	}

	@Override
	public Response unstarIstanza(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::unstarIstanza] IN idIstanza:"+idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			Istanza istanzaUnstared = new Istanza();
			istanzaUnstared.setImportanza(0);
			Istanza istanza = istanzeService.patchIstanza(user, idIstanza, istanzaUnstared);
			return Response.ok(istanza).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::unstarIstanza] Errore servizio unstarIstanza",e);
			throw new ServiceException("Errore servizio unstar istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::unstarIstanza] Errore generico servizio unstarIstanza",ex);
			throw new ServiceException("Errore generico servizio unstar istanza");
		}
	}

	@Override
	public Response getIstanzeByModulo(Long idModulo, Integer stato, String sort, String filtroRicercaDati, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanze] IN stato: "+stato);
			log.debug("[" + CLASS_NAME + "::getIstanze] IN idModulo: "+idModulo);
			log.debug("[" + CLASS_NAME + "::getIstanze] IN sort: "+sort);
			
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getIstanze] utente abilitato: "+ user.getIdentificativoUtente());
			
			IstanzeFilter filter = new IstanzeFilter();
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}
			
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			List<Istanza> elenco = istanzeService.getElencoIstanze(stato, filter, optSorter, filtroRicercaDati);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"IstanzeByModulo-idModulo-CF_dichiarante", 
					idModulo.toString() + '-' + cfDichiarante);
			auditService.traceOperazione(auditEntity);
			return Response.ok(elenco).build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze",ex);
			throw new ServiceException("Errore generico servizio elenco istanze ");
		} 
	}

	
	@Override
	public Response getIstanzePaginate(Long idModulo, Long idEnte, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante, 
			Integer stato, 
			String codiceIstanza, String protocollo, 
    		Date startDate, Date endDate, 
			String sort, String filtroRicercaDati,
			String filtroEpay,
			String offsetStr, String limitStr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN stato: "+stato);
				log.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN idModulo: "+idModulo);
				log.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getIstanzePaginate] IN sort: "+sort);
			}
			
			UserInfo user = retrieveUserInfo(httpRequest);
			log.info("[" + CLASS_NAME + "::getIstanzePaginate] utente abilitato: "+ user.getIdentificativoUtente());
			log.info("[" + CLASS_NAME + "::getIstanzePaginate] utente abilitato: "+ user);
			
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}

			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);
					
			IstanzeFilter filter = new IstanzeFilter();
			
			filter.setFiltroEpay(filtroEpay);
			
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			} else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modulo ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
			}
			
			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			if (codiceIstanza != null && !codiceIstanza.equals("")) {
				filter.setCodiceIstanza(codiceIstanza);
			}
			if (protocollo != null && !protocollo.equals("")) {
				filter.setProtocollo(protocollo);
			}

			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			//List<Istanza> elenco = istanzeService.getElencoIstanze(stato, filter, optSorter, filtroRicercaDati);
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService.getElencoIstanzePaginate(stato, filter, optSorter, filtroRicercaDati);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"IstanzePaginate-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(response).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate", e);
			throw new UnauthorizedException(e.getMessage());
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore servizio getIstanzePaginate", e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzePaginate] Errore generico servizio getIstanzePaginate", ex);
			throw new ServiceException("Errore generico servizio elenco istanze paginate");
		}
	}
	
	@Override
	public Response getIstanzeArchivioPaginate(Long idModulo, Long idEnte,List<String> stati,
			String codiceIstanza, String protocollo, 
    		Date stateStart, Date stateEnd, 
    		Date createdStart, Date createdEnd, 
    		String codiceFiscale, String cognome, String nome,
			String sort, String filtroRicercaDati,
			String filtroEpay, 
			String offsetStr, String limitStr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN stati: "+stati);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN idModulo: "+idModulo);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN codiceIstanza: "+codiceIstanza);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN protocollo: "+protocollo);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN filtroRicercaDati: "+filtroRicercaDati);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN stateStart: "+stateStart);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN stateEnd: "+stateEnd);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN createdStart: "+createdStart);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN createdEnd: "+createdEnd);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN codiceFiscale: "+codiceFiscale);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN cognome: "+cognome);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN nome: "+nome);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN offsetStr: "+offsetStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN limitStr: "+limitStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] IN sort: "+sort);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getIstanze] utente abilitato: "+ user.getIdentificativoUtente());
			
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}

			
			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);
			
			IstanzeFilter filter = new IstanzeFilter();
						 
			filter.setFiltroEpay(filtroEpay);
						
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);			
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modulo ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}
		
			if (codiceIstanza != null && !codiceIstanza.equals("")) {
				filter.setCodiceIstanza(codiceIstanza);
			}
			if (protocollo != null && !protocollo.equals("")) {
				filter.setProtocollo(protocollo);
			}
			if (codiceFiscale != null && !codiceFiscale.equals("")) {
				filter.setCodiceFiscaleDichiarante(codiceFiscale);
			}
			if (cognome != null && !cognome.equals("")) {
				filter.setCognomeDichiarante(cognome);
			}
			if (nome != null && !nome.equals("")) {
				filter.setNomeDichiarante(nome);
			}
						
			filter.setStatiIstanza(stati.stream().map(Integer::parseInt).collect(Collectors.toList()));

			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
//			if (!filter.getStatiIstanza().isEmpty() && filter.getStatiIstanza().get().size() >= 1) {
//				filter.setStateStart(createdStart);
//				filter.setStateEnd(createdEnd);
//			} else {
//				filter.setCreatedStart(createdStart);
//				filter.setCreatedEnd(createdEnd);
//			}
				
			filter.setStateStart(stateStart);
			filter.setStateEnd(stateEnd);
		
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
						
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService.getElencoArchivioIstanzePaginate(filter, optSorter, filtroRicercaDati);
			// Audit Operazione
						AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
								auditService.retrieveUser(user), 
								AuditEntity.EnumOperazione.READ,
								"IstanzeArchivioPaginate-filtro", 
								filter.toString() );
						auditService.traceOperazione(auditEntity);

			return Response.ok(response).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] Errore servizio getIstanzeArchivioPaginate", e);
			throw new UnauthorizedException(e.getMessage());			
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] Errore servizio getIstanzeArchivioPaginate",e);
			throw new ServiceException("Errore servizio elenco istanze paginate");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzeArchivioPaginate] Errore generico servizio getIstanzeArchivioPaginate",ex);
			throw new ServiceException("Errore generico servizio elenco istanze paginate");
		} 
	}
	
	@Override
	public Response getIstanzeInLavByModulo(Long idModulo, String filtroRicercaDati, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getIstanze] IN idModulo: "+idModulo);
			
			UserInfo user = retrieveUserInfo(httpRequest);
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}
			log.debug("[" + CLASS_NAME + "::getIstanze] utente abilitato: "+ user.getIdentificativoUtente());
			
			IstanzeFilter filter = new IstanzeFilter();
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}
							
			filter.setFlagArchiviata(EnumFilterFlagArchiviata.NONARCHIVIATI);
			
			String sort = "";
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			List<Istanza> elenco = istanzeService.getElencoIstanzeInLavorazione(filter, optSorter, filtroRicercaDati);
			// Audit Operazione
						AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
								auditService.retrieveUser(user), 
								AuditEntity.EnumOperazione.READ,
								"IstanzeInLavorazioneByModulo-filtro", 
								filter.toString() );
						auditService.traceOperazione(auditEntity);

			return Response.ok(elenco).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze", e);
			throw new UnauthorizedException(e.getMessage());						
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore servizio getIstanze",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanze] Errore generico servizio getIstanze",ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		} 
	}
	
	@Override
	public Response getIstanzeInLavByModuloPaginate(Long idModulo, Long idEnte,String filtroRicercaDati,String filtroEpay, 
			String sort, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante,
			String offsetStr, String limitStr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN idModulo: "+idModulo);
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN dichiarante: "+cfDichiarante);	
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN filtroRicercaDati: "+filtroRicercaDati);	
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN offsetStr: "+offsetStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN limitStr: "+limitStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] IN sort: "+sort);			
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
     		}
			
			log.debug("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] utente abilitato: "+ user.getIdentificativoUtente());
			
			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);
			
			IstanzeFilter filter = new IstanzeFilter();
			
			filter.setFiltroEpay(filtroEpay);
			
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}
				
			filter.setFlagArchiviata(EnumFilterFlagArchiviata.NONARCHIVIATI);
				
			//String sort = "";
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			

			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService.getElencoIstanzeInLavorazionePaginate(filter, optSorter, filtroRicercaDati);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"IstanzeInLavorazionePaginateByModulo-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(response).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] Errore servizio getIstanze", e);
			throw new UnauthorizedException(e.getMessage());
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] Errore servizio getIstanze", e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzeInLavByModuloPaginate] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		}
	}	
	
	@Override
	public Response getIstanzeInBozzaByModuloPaginate(Long idModulo,Long idEnte, String filtroRicercaDati, String sort, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante, 
			String offsetStr, String limitStr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN dichiarante: "+cfDichiarante);	
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN filtroRicercaDati: "+filtroRicercaDati);	
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN offsetStr: "+offsetStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN limitStr: "+limitStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] IN sort: "+sort);				
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}
			log.debug("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] utente abilitato: "+ user.getIdentificativoUtente());
			
			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);
			
			IstanzeFilter filter = new IstanzeFilter();
			
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitatiCurrentUserEnte(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}

			filter.setFlagArchiviata(EnumFilterFlagArchiviata.NONARCHIVIATI);
			
			//String sort = "";
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService.getElencoIstanzeInBozzaPaginate(filter, optSorter, filtroRicercaDati);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"IstanzeInBozzaByModuloPaginate-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(response).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] Errore servizio getIstanze", e);
			throw new UnauthorizedException(e.getMessage());
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] Errore servizio getIstanze", e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzeInBozzaByModuloPaginate] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		}

	}	
	
	public Response getPdfById(String idIstanzaQP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				log.debug("[" + CLASS_NAME + "::getPdfById] IN idIstanzaQP:"+idIstanzaQP);
				Long idIstanza = validaLongRequired(idIstanzaQP);
				//
				UserInfo user = retrieveUserInfo(httpRequest);
				//
				byte[] bytes = istanzeService.getPdfIstanza(user, idIstanza);
				log.debug("[" + CLASS_NAME + "::getPdfById] bytes.length=" + bytes.length);
				auditService.getPdf(httpRequest.getRemoteAddr(), user, idIstanza);
		        return Response.ok(bytes).build();
//	    		.header("Cache-Control", "no-cache, no-store, must-revalidate")
//	    		.header("Pragma", "no-cache")
//	    		.header("Expires", "0")
//	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
//	    		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//	    		.build();
			} catch(ItemNotFoundBusinessException notFoundEx) {
				log.error("[" + CLASS_NAME + "::getPdfById] istanza non trovata", notFoundEx);
				throw new ResourceNotFoundException();
			} catch (BusinessException e) {
				log.error("[" + CLASS_NAME + "::getPdfById] Errore servizio getPdfById", e);
				throw new ServiceException("Errore servizio delete istanza");
			} catch (Throwable ex) {
				log.error("[" + CLASS_NAME + "::getPdfById] Errore generico servizio getPdfById", ex);
				throw new ServiceException("Errore generico servizio print istanza");
			} 
		}
	
	
	public Response getRicevutaPdfById(String idIstanzaQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getRicevutaPdfById] IN idIstanzaQP:"+idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			byte[] bytes = istanzeService.getRicevutaPdfByIstanza(user, idIstanza);
			log.debug("[" + CLASS_NAME + "::getRicevutaPdfById] bytes.length=" + bytes.length);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"GET_RICEVUTA_PDF-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
	        return Response.ok(bytes).build();
//	    		.header("Cache-Control", "no-cache, no-store, must-revalidate")
//	    		.header("Pragma", "no-cache")
//	    		.header("Expires", "0")
//	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=response.pdf")
//	    		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
//	    		.build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getRicevutaPdfById] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getRicevutaPdfById] Errore servizio getRicevutaPdfById", e);
			throw new ServiceException("Errore servizio delete istanza");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getRicevutaPdfById] Errore generico servizio getRicevutaPdfById", ex);
			throw new ServiceException("Errore generico servizio print istanza");
		} 
	}


	@Override
	public Response getJsonIstanzeByModulo(Long idModulo, Long idEnte, List<String> stati, Date stateStart, Date stateEnd, Date createdStart, Date createdEnd,String sort, String filtroRicerca, String filtroEpay, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN stati: "+stati);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN stateStart: "+stateStart);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN stateEnd: "+stateEnd);				
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN createdStart: "+createdStart);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN createdEnd: "+createdEnd);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN filtroRicerca: "+filtroRicerca);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN filtroEpay: "+filtroEpay);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] IN sort: "+sort);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] utente abilitato: "+ user.getIdentificativoUtente());
			
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicerca);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}	
			IstanzeFilter filter = new IstanzeFilter();			
			filter.setIdModuli(Arrays.asList(idModulo));
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));				
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
//			if (idModulo != null) {
//				filter.setIdModuli(Arrays.asList(idModulo));
//			}
//			else {
//				List<Modulo> elenco = moduliService.getElencoModuliAbilitati(user);
//				if (elenco == null)
//				{
//					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
//				}
//				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
//				if (elenco.size() > 0)
//				{
//					for (Modulo  modulo : elenco) {
//						elencoIdModuli.add(modulo.getIdModulo());					
//					}
//					filter.setIdModuli(elencoIdModuli);
//				}				
//			}			
			// considero solo le istanze che non sono state eliminate
//			filter.setStatoEliminata(EnumFilterStatoEliminata.ATTIVI);			
			filter.setStatiIstanza(stati.stream().map(Integer::parseInt).collect(Collectors.toList()));			
			
//			if (!filter.getStatiIstanza().isEmpty() && filter.getStatiIstanza().get().size() >= 1) {
//				filter.setStateStart(createdStart);
//				filter.setStateEnd(createdEnd);
//			} else {
//				filter.setCreatedStart(createdStart);
//				filter.setCreatedEnd(createdEnd);
//			}

			filter.setStateStart(stateStart);
			filter.setStateEnd(stateEnd);			
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
		
			filter.setFiltroEpay(filtroEpay);
			
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter, filtroRicerca, "data");
			log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] elenco.size()=" + elenco.size());
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"ExportIstanzeByModulo-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			JSONArray jsonArray=new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			int i = 0;
			for (Istanza  istanza : elenco) {				
//				IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(istanza.getIdIstanza());
				JsonNode istanceNode = JsonNodeFactory.instance.objectNode();									
			    ((ObjectNode) istanceNode).put("idIstanza", istanza.getIdIstanza());
			    ((ObjectNode) istanceNode).put("codiceIstanza", istanza.getCodiceIstanza());
			    ((ObjectNode) istanceNode).put("stato", istanza.getStato().getCodice());
			    ((ObjectNode) istanceNode).put("dataInvio", (istanza.getCreated() != null) ? istanza.getCreated().toString() : "");
			    ((ObjectNode) istanceNode).put("codiceFiscaleDichiarante", istanza.getCodiceFiscaleDichiarante());
			    ((ObjectNode) istanceNode).put("numeroProtocollo", (istanza.getNumeroProtocollo()==null)?"":istanza.getNumeroProtocollo());	
			    ((ObjectNode) istanceNode).put("dataProtocollo", (istanza.getDataProtocollo()==null)?"":sdf.format(istanza.getDataProtocollo()));
			    
				if (filter.getFiltroEpay() != null) {
					istanza = completaIstanzaEpay(istanza);
					List<Pagamento> pagamenti = istanza.getPagamenti();
					if (pagamenti != null) {
						Pagamento pagamento = pagamenti.stream().filter(e -> e.getDataAnnullamento() == null).findAny()
								.orElse(null);
						String statoPagamento = null;
						String codiceAvviso = null;
						String iuv = null;
						String dataEsitoPagamento = null;
						String importo = null;
						if (pagamento != null) {							
							statoPagamento = "non pagato";
							codiceAvviso = pagamento.getCodiceAvviso();
							iuv = pagamento.getIuv();
						
							((ObjectNode) istanceNode).put("codiceAvviso", (codiceAvviso == null) ? "": codiceAvviso);
							((ObjectNode) istanceNode).put("iuv", (iuv == null) ? "": iuv);
							PagamentoNotifica notifica = pagamento.getNotifica();
							if (notifica != null) {
								dataEsitoPagamento = (notifica.getDataEsitoPagamento() == null)
										? "" : sdf.format(notifica.getDataEsitoPagamento());
								importo = notifica.getImportoPagato() == null ? "": notifica.getImportoPagato().toString() ;
								((ObjectNode) istanceNode).put("importo", importo);
								((ObjectNode) istanceNode).put("dataEsitoPagamento", dataEsitoPagamento);
								
								statoPagamento = (notifica.getDataEsitoPagamento() == null) ? "non pagato": "pagato";
							}
							
							((ObjectNode) istanceNode).put("statoPagamento", statoPagamento);
						}
					}
				}
			    
			    JsonNode dataNode;
			    JsonNode root;			    
				if (istanza.getData() != null) {
					ObjectMapper objtMapper = new ObjectMapper();
					objtMapper.configure(
						DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);				
					JsonNode nodeDatiIstanza = objtMapper.readValue((String)istanza.getData()/* datiE.getDatiIstanza()*/, JsonNode.class);
					dataNode = nodeDatiIstanza.get("data");
					root = JacksonUtils.merge(istanceNode, dataNode);
				} else {
					log.debug("[" + CLASS_NAME + "::getIstanzeJson] dati istanza non presenti per id_istanza: " + istanza.getIdIstanza());
					dataNode = JsonNodeFactory.instance.objectNode();
					root = JacksonUtils.merge(istanceNode, dataNode);
				}
				jsonArray.put(root);	
			}				
			String json = jsonArray.toString();			
			log.debug("[" + CLASS_NAME + "::getJsonIstanzeByModulo] json: "+json);				
			return Response.ok(json).build();		
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzeByModulo] Errore servizio getJsonIstanzeByModulo", e);
			throw new UnauthorizedException(e.getMessage());										
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzeByModulo] Errore servizio getJsonIstanzeByModulo",e);
			throw new ServiceException("Errore servizio elenco istanze json per export csv");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzeByModulo] Errore generico servizio getJsonIstanzeByModulo",ex);
			throw new ServiceException("Errore servizio elenco istanze json per export csv ");
		} 
	}
	

	@Override
	public Response getCustomJsonIstanzeByModulo(Long idModulo, String codiceEstrazione, 
			String createdDataOraStartQP, String createdDataOraEndQP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] IN codiceEstrazione: "+codiceEstrazione);
				log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] IN createdDataOraStartQP: "+createdDataOraStartQP);
				log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] IN createdDataOraEndQP: "+createdDataOraEndQP);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			Date createdStart = validaDateTime(createdDataOraStartQP);
			Date createdEnd = validaDateTime(createdDataOraEndQP);

			log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] utente abilitato: "+ user.getIdentificativoUtente());
			
			// check abilitazione ad ente
//			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicerca);
//			if (!isUtenteAbilitato) {
//				throw new UnauthorizedBusinessException("Utente non abilitato");
//			}	

			byte[] bytes = istanzeService.getCustomJsonIstanzeByModulo(user, idModulo, codiceEstrazione, createdStart, createdEnd);
			log.debug("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] bytes.length=" + bytes.length);
			String fileName = idModulo+"_"+codiceEstrazione;
			String ext = "csv";
			
	        return Response.ok(bytes)
	    		.header("Cache-Control", "no-cache, no-store, must-revalidate")
	    		.header("Pragma", "no-cache")
	    		.header("Expires", "0")
        		.header(HttpHeaders.CONTENT_TYPE, "text/csv")
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+fileName+"."+ext)
	    		.header(HttpHeaders.CONTENT_LENGTH, bytes.length)
	    		.build();
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] Errore servizio getCustomJsonIstanzeByModulo", e);
			throw new UnauthorizedException(e.getMessage());										
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] Errore servizio getCustomJsonIstanzeByModulo",e);
			throw new ServiceException("Errore servizio elenco istanze json per export csv");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCustomJsonIstanzeByModulo] Errore generico servizio getCustomJsonIstanzeByModulo",ex);
			throw new ServiceException("Errore servizio elenco istanze json per export csv ");
		} 
	}
	
	
	@Override
	public Response getCountJsonIstanzeByModulo(Long idModulo, Long idEnte, List<String> stati, Date stateStart,
			Date stateEnd,Date createdStart, Date createdEnd, String sort, String filtroRicerca, String filtroEpay, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN stati: "+stati);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN stateStart: "+stateStart);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN stateEnd: "+stateEnd);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN createdStart: "+createdStart);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN createdEnd: "+createdEnd);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN filtroRicerca: "+filtroRicerca);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN filtroEpay: "+filtroEpay);
				log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] IN sort: "+sort);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] utente abilitato: "+ user.getIdentificativoUtente());
			
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicerca);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}	
			IstanzeFilter filter = new IstanzeFilter();			
			filter.setIdModuli(Arrays.asList(idModulo));
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));				
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
		
			// considero solo le istanze che non sono state eliminate
//			filter.setStatoEliminata(EnumFilterStatoEliminata.ATTIVI);			
			filter.setStatiIstanza(stati.stream().map(Integer::parseInt).collect(Collectors.toList()));			
			
//			if (!filter.getStatiIstanza().isEmpty() && filter.getStatiIstanza().get().size() >= 1) {
//				filter.setStateStart(createdStart);
//				filter.setStateEnd(createdEnd);
//			} else {
//				filter.setCreatedStart(createdStart);
//				filter.setCreatedEnd(createdEnd);
//			}
//			
			
			filter.setStateStart(stateStart);
			filter.setStateEnd(stateEnd);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
			
			filter.setFiltroEpay(filtroEpay);
		
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
//			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter, filtroRicerca, "data");
			// Audit Operazione
//			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
//					auditService.retrieveUser(user), 
//					AuditEntity.EnumOperazione.READ,
//					"ExportIstanzeByModulo-filtro", 
//					filter.toString() );
//			auditService.traceOperazione(auditEntity);
			
			Integer count = istanzaDAO.countArchivio(filter, filtroRicerca);
					
			JsonNode istanceNode = JsonNodeFactory.instance.objectNode();									
		    ((ObjectNode) istanceNode).put("count", count.toString());
		    				
			String json = istanceNode.toString();		
			
			log.debug("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] json: "+json);				
			return Response.ok(json).build();		
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] Errore servizio getCountJsonIstanzeByModulo", e);
			throw new UnauthorizedException(e.getMessage());										
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] Errore servizio getCountJsonIstanzeByModulo",e);
			throw new ServiceException("Errore servizio elenco istanze json per export csv");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCountJsonIstanzeByModulo] Errore generico servizio getCountJsonIstanzeByModulo",ex);
			throw new ServiceException("Errore servizio elenco istanze json per export csv ");
		} 
	}
	
	
	@Override
	public Response getJsonIstanzePaginateByModulo(Long idModulo, Long idEnte, List<String> stati, Date stateStart, Date stateEnd, Date createdStart, Date createdEnd,String sort, String filtroRicerca, String filtroEpay,
			String offsetStr, String limitStr, SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN stati: "+stati);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN stateStart: "+stateStart);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN stateEnd: "+stateEnd);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN createdStart: "+createdStart);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN createdEnd: "+createdEnd);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN sort: "+sort);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN offset: "+offsetStr);
				log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] IN limit: "+limitStr);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] utente abilitato: "+ user.getIdentificativoUtente());
			
			// check abilitazione ad ente
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicerca);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}	
			IstanzeFilter filter = new IstanzeFilter();	
			
			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);			
			
			filter.setIdModuli(Arrays.asList(idModulo));
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));				
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
		
			// considero solo le istanze che non sono state eliminate
//			filter.setStatoEliminata(EnumFilterStatoEliminata.ATTIVI);			
			filter.setStatiIstanza(stati.stream().map(Integer::parseInt).collect(Collectors.toList()));	
			
//			if (!filter.getStatiIstanza().isEmpty() && filter.getStatiIstanza().get().size() >= 1) {
//				filter.setStateStart(createdStart);
//				filter.setStateEnd(createdEnd);
//			} else {
//				filter.setCreatedStart(createdStart);
//				filter.setCreatedEnd(createdEnd);
//			}
//			
			filter.setStateStart(stateStart);
			filter.setStateEnd(stateEnd);
			filter.setCreatedStart(createdStart);
			filter.setCreatedEnd(createdEnd);
					
			filter.setSort(sort);
			
			filter.setFiltroEpay(filtroEpay);
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			List<Istanza> elenco = istanzeService.getElencoIstanze(filter, optSorter, filtroRicerca, "data");
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"ExportIstanzeByModulo-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			JSONArray jsonArray=new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			int i = 0;
			for (Istanza  istanza : elenco) {				
//				IstanzaDatiEntity datiE = istanzaDAO.findLastCronDati(istanza.getIdIstanza());
				ObjectMapper objtMapper = new ObjectMapper();
				objtMapper.configure(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);				
				JsonNode istanceNode = JsonNodeFactory.instance.objectNode();									
			    ((ObjectNode) istanceNode).put("idIstanza", istanza.getIdIstanza());
			    ((ObjectNode) istanceNode).put("codiceIstanza", istanza.getCodiceIstanza());
			    ((ObjectNode) istanceNode).put("stato", istanza.getStato().getCodice());
			    ((ObjectNode) istanceNode).put("dataInvio", (istanza.getCreated() != null) ? istanza.getCreated().toString() : "");
			    ((ObjectNode) istanceNode).put("codiceFiscaleDichiarante", istanza.getCodiceFiscaleDichiarante());		
			    
				if (filter.getFiltroEpay() != null) {
					istanza = completaIstanzaEpay(istanza);
					List<Pagamento> pagamenti = istanza.getPagamenti();
					if (pagamenti != null) {
						Pagamento pagamento = pagamenti.stream().filter(e -> e.getDataAnnullamento() == null).findAny()
								.orElse(null);
						String statoPagamento = null;
						String codiceAvviso = null;
						String iuv = null;
						String dataEsitoPagamento = null;
						String importo = null;
						if (pagamento != null) {							
							statoPagamento = "non pagato";
							codiceAvviso = pagamento.getCodiceAvviso();
							iuv = pagamento.getIuv();
						
							((ObjectNode) istanceNode).put("codiceAvviso", (codiceAvviso == null) ? "": codiceAvviso);
							((ObjectNode) istanceNode).put("iuv", (iuv == null) ? "": iuv);
							PagamentoNotifica notifica = pagamento.getNotifica();
							if (notifica != null) {
								dataEsitoPagamento = (notifica.getDataEsitoPagamento() == null)
										? "" : sdf.format(notifica.getDataEsitoPagamento());
								importo = notifica.getImportoPagato() == null ? "": notifica.getImportoPagato().toString();
								((ObjectNode) istanceNode).put("importo", importo);
								((ObjectNode) istanceNode).put("dataEsitoPagamento", dataEsitoPagamento);
								
								statoPagamento = (notifica.getDataEsitoPagamento() == null) ? "non pagato": "pagato";
							}
							
							((ObjectNode) istanceNode).put("statoPagamento", statoPagamento);
						}
					}
				}
				
			    JsonNode dataNode;
			    JsonNode root;			    
				if (istanza.getData() != null) {
					JsonNode nodeDatiIstanza = objtMapper.readValue((String)istanza.getData()/* datiE.getDatiIstanza()*/, JsonNode.class);
					dataNode = nodeDatiIstanza.get("data");
					root = JacksonUtils.merge(istanceNode, dataNode);
				} else {
					log.debug("[" + CLASS_NAME + "::getIstanzeJson] dati istanza non presenti per id_istanza: " + istanza.getIdIstanza());
					dataNode = JsonNodeFactory.instance.objectNode();
					root = JacksonUtils.merge(istanceNode, dataNode);
				}
				jsonArray.put(root);	
			}				
			String json = jsonArray.toString();			
			log.debug("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] json: "+json);				
			return Response.ok(json).build();		
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] Errore servizio getJsonIstanzePaginateByModulo", e);
			throw new UnauthorizedException(e.getMessage());										
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] Errore servizio getJsonIstanzePaginateByModulo",e);
			throw new ServiceException("Errore servizio elenco istanze json per export csv");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getJsonIstanzePaginateByModulo] Errore generico servizio getJsonIstanzePaginateByModulo",ex);
			throw new ServiceException("Errore servizio elenco istanze json pginate per export csv ");
		} 
	}	
	
	@Override
	public Response getAllegati(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getAllegati] IN idIstanza: "+idIstanza);
			
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getIstanze] utente abilitato: "+ user.getIdentificativoUtente());
			
			List<Allegato> elenco = allegatiService.findLazyByIdIstanza(idIstanza);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"GET_ALLEGATI-idIstanza", 
					idIstanza.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(elenco).build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getAllegati] Errore servizio getAllegati",e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAllegati] Errore generico servizio getAllegati",ex);
			throw new ServiceException("Errore generico servizio elenco allegati per istanza");
		} 
	}
	
	@Override
	public Response getAllegato(String formioNameFile, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getAllegato] IN formioFileName: "+formioNameFile);
			
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getAllegato] utente abilitato: "+ user.getIdentificativoUtente());
			
			Allegato fileAllegato = allegatiService.getByFormIoNameFile(formioNameFile);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"GET_ALLEGATO-nomeFileFormio", 
					formioNameFile );
			auditService.traceOperazione(auditEntity);
			return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
					.header("Content-Type", fileAllegato.getContentType())
					.build();
			
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getAllegato] Errore servizio getAllegato",e);
			throw new ServiceException("Errore servizio getAllegato");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAllegato] Errore generico servizio getAllegato",ex);
			throw new ServiceException("Errore generico servizio getAllegato");
		} 
	}
	
    //
    // PROTOCOLLO
    //
	@Override
	public Response postProtocolla(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::postProtocolla] IN idIstanza: " + idIstanza);
			UserInfo user = retrieveUserInfo(httpRequest);
			istanzeService.protocolla(user, idIstanza);
			log.debug("[" + CLASS_NAME + "::postProtocolla] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postProtocolla] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postProtocolla] Errore servizio postProtocolla", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postProtocolla] Errore generico servizio postProtocolla", ex);
			throw new ServiceException("Errore generico servizio protocolla istanza");
		} 
	}


    //
    // Ticket CRM
    //
	@Override
	public Response postCreaTicketCrmIstanza(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] IN idIstanza: " + idIstanza);
			moonSrvDAO.creaTicketCrm(idIstanza);
			log.debug("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] Errore servizio postCreaTicketCrmIstanza", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postCreaTicketCrmIstanza] Errore generico servizio postCreaTicketCrmIstanza", ex);
			throw new ServiceException("Errore generico servizio postCreaTicketCrmIstanza istanza");
		} 
	}

	
    //
    // Email
    //
	@Override
	public Response postRinviaEmail(Long idIstanza, String dest,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postRinviaEmail] IN idIstanza: " + idIstanza);
				log.debug("[" + CLASS_NAME + "::postRinviaEmail] IN dest: " + dest);
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			istanzeService.rinviaEmail(user, idIstanza, dest);
			log.debug("[" + CLASS_NAME + "::postRinviaEmail] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postRinviaEmail] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postRinviaEmail] Errore servizio postRinviaEmail", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postRinviaEmail] Errore generico servizio postRinviaEmail", ex);
			throw new ServiceException("Errore generico servizio postRinviaEmail");
		} 
	}

	@Override
	public Response postRinviaEmails(String idTagQP, String dest, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postRinviaEmails] IN idTag: " + idTagQP);
				log.debug("[" + CLASS_NAME + "::postRinviaEmails] IN dest: " + dest);
			}
//			String xMoon = httpRequest.getHeader("X-Moon");
//			if (!"moon4!ESTA_RAGA_21".equals(xMoon)) {
//				throw new UnprocessableEntityException("Specify X-Moon Secure.");
//			}
			Long idTag = validaLongRequired(idTagQP);
			istanzeService.rinviaEmails(idTag, dest);
			log.debug("[" + CLASS_NAME + "::postRinviaEmails] END");
	        return Response.ok().build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::postRinviaEmails] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postRinviaEmails] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postRinviaEmails] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio postRinviaEmails");
		} 
	}

	@Override
	public Response getJsonById(String idIstanzaQP, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {			
			log.debug("[" + CLASS_NAME + "::getJsonById] IN idIstanzaQP: "+idIstanzaQP);			
					
			UserInfo user = retrieveUserInfo(httpRequest);
			log.debug("[" + CLASS_NAME + "::getJsonById] utente abilitato: "+ user.getIdentificativoUtente());
			
			Long idIstanza = validaLongRequired(idIstanzaQP);
					
			Istanza istanza = istanzeService.getIstanzaById(user, idIstanza);		
			String json = (String) istanza.getData();
			
			byte[] jsonByte = json.getBytes("UTF-8");
			
			log.debug("[" + CLASS_NAME + "::getJsonById] json: "+json);				
			return Response.ok(jsonByte).build();	
						
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonById] Errore servizio getJsonById", e);
			throw new UnauthorizedException(e.getMessage());										
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getJsonById] Errore servizio getJsonById",e);
			throw new ServiceException("Errore servizio export json dati");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getJsonById] Errore generico servizio getJsonById",ex);
			throw new ServiceException("Errore servizio export json dati");
		} 
	}

	@Override
	public Response riportaInBozza(String idIstanzaQP,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::riportaInBozza] IN idIstanzaQP:"+idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			//
			Istanza istanza = istanzeService.riportaInBozza(user, idIstanza);
			return Response.ok(istanza).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::riportaInBozza] Errore servizio riportaInBozza", be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::riportaInBozza] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::riportaInBozza] Errore generico servizio riportaInBozza",ex);
			throw new ServiceException("Errore generico servizio riportaInBozza");
		}
	}
	
	@Override
	public Response invia(String idIstanzaQP,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			log.debug("[" + CLASS_NAME + "::invia] IN idIstanzaQP:"+idIstanzaQP);
			Long idIstanza = validaLongRequired(idIstanzaQP);
			UserInfo user = retrieveUserInfo(httpRequest);
			
			DatiAggiuntiviHeaders daHeaders = HeadersUtils.readFromHeaders(httpHeaders);
			IstanzaSaveResponse istanza = istanzeService.invia(user, idIstanza, daHeaders, httpRequest.getRemoteAddr());
			
			return Response.ok(istanza).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::invia] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::invia] Errore servizio invia", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::invia] Errore generico servizio invia",ex);
			throw new ServiceException("Errore generico servizio invia");
		}
	}

	@Override
	public Response saveIstanzaCompila(Istanza body, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::saveIstanzaCompila] IN Istanza body: "+body);
			UserInfo user = retrieveUserInfoWithRoles(httpRequest);
			auditService.saveIstanza(httpRequest.getRemoteAddr(), user, body.getIdIstanza());
			IstanzaSaveResponse result = istanzeService.saveCompilaIstanza(user, body);
			return Response.ok(result).build();
		} catch (UnivocitaIstanzaBusinessException uibe) {
			log.error("[" + CLASS_NAME + "::saveIstanza] UnivocitaIstanzaBusinessException saveIstanzaCompila " + uibe);
			throw new ServiceException(uibe.getMessage(),uibe.getCode());
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore servizio saveIstanzaCompila", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::saveIstanza] Errore generico servizio saveIstanzaCompila",ex);
			ex.printStackTrace();
			throw new ServiceException("Errore generico servizio salva istanza");
		} 
	}

	@Override
	public Response getIstanzeDaCompletareByModuloPaginate(Long idModulo,Long idEnte, String filtroRicercaDati, String sort, 
			String cfDichiarante,String nomeDichiarante,String cognomeDichiarante,
			String offsetStr, String limitStr,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN idModulo: "+idModulo);			
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN idEnte: "+idEnte);
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN dichiarante: "+cfDichiarante);	
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN filtroRicercaDati: "+filtroRicercaDati);	
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN offsetStr: "+offsetStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN limitStr: "+limitStr);
				log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] IN sort: "+sort);				
			}
			UserInfo user = retrieveUserInfo(httpRequest);
			Boolean isUtenteAbilitato = utentiService.isUtenteAbilitato(user, idModulo, filtroRicercaDati);
			if (!isUtenteAbilitato) {
				throw new UnauthorizedBusinessException("Utente non abilitato");
			}
			log.debug("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] utente abilitato: "+ user.getIdentificativoUtente());
			
			Integer offset = validaInteger0Based(offsetStr);
			Integer limit = validaInteger1Based(limitStr);
			
			IstanzeFilter filter = new IstanzeFilter();
			
			//filter.setIdEnte((idEnte != null) ? idEnte : ( (user.getEnte() != null) ? user.getEnte().getIdEnte() : null));
			filter.setIdEnte(user.isMultiEntePortale()?user.getEnte().getIdEnte():null);
			
			if (idModulo != null) {
				filter.setIdModuli(Arrays.asList(idModulo));
			}
			else {
				List<Modulo> elenco = moduliService.getElencoModuliAbilitati(user);
				if (elenco == null)
				{
					throw new ServiceException("Utente non risulta abilitato ad alcun modello ");
				}
				ArrayList<Long> elencoIdModuli = new ArrayList<Long>();
				if (elenco.size() > 0)
				{
					for (Modulo  modulo : elenco) {
						elencoIdModuli.add(modulo.getIdModulo());					
					}
					filter.setIdModuli(elencoIdModuli);
				}
					
			}

			filter.setFlagArchiviata(EnumFilterFlagArchiviata.NONARCHIVIATI);
			
			//String sort = "";
			filter.setSort(sort);
			Optional<IstanzeSorter> optSorter = new IstanzeSorterBuilder(sort).build();
			
			// dichiarante
			if (cfDichiarante != null && !cfDichiarante.equals("")) {
				filter.setCodiceFiscaleDichiarante(cfDichiarante);
			}
			if (nomeDichiarante != null && !nomeDichiarante.equals("")) {
				filter.setNomeDichiarante(nomeDichiarante);
			}
			if (cognomeDichiarante != null && !cognomeDichiarante.equals("")) {
				filter.setCognomeDichiarante(cognomeDichiarante);
			}
			
			filter.setUsePagination(true);
			filter.setOffset(offset);
			filter.setLimit(limit);
			
			
			ResponsePaginated<Istanza> response = (ResponsePaginated<Istanza>) istanzeService.getElencoIstanzeDaCompletarePaginate(filter, optSorter, filtroRicercaDati);
			// Audit Operazione
			AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
					auditService.retrieveUser(user), 
					AuditEntity.EnumOperazione.READ,
					"IstanzeInBozzaByModuloPaginate-filtro", 
					filter.toString() );
			auditService.traceOperazione(auditEntity);
			return Response.ok(response).build();
			
		} catch (UnauthorizedBusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] Errore servizio getIstanze", e);
			throw new UnauthorizedException(e.getMessage());
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] Errore servizio getIstanze", e);
			throw new ServiceException("Errore servizio elenco istanze");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getIstanzeDaCompletareByModuloPaginate] Errore generico servizio getIstanze", ex);
			throw new ServiceException("Errore generico servizio elenco istanze");
		}
	}
	
	@Override
	public Response getLogEmail(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getLogEmail] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, "idIstanza");
			List<LogEmail> ris = logEmailService.getElencoLogEmailByIdIstanza(idIstanza);
			log.debug("[" + CLASS_NAME + "::getLogEmail] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getLogEmail] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getLogEmail] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getLogEmail] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getLogEmail");
		}
	}
	
    //
    // WF COSMO
    //
	
	@Override
	public Response postCreaPraticaEdAvviaProcessoCosmo(Long idIstanza, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] IN idIstanza: " + idIstanza);
			moonSrvDAO.creaPraticaEdAvviaProcesso(idIstanza);
			log.debug("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postCreaPraticaEdAvviaProcessoCosmo] Errore generico servizio postCreaPraticaEdAvviaProcessoCosmo", ex);
			throw new ServiceException("Errore generico servizio CreaPraticaEdAvviaProcessoCosmo");
		} 
	}
	
	
	@Override
	public Response inviaRispostaIntegrazioneCosmo(Long idIstanza, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] IN idIstanza: " + idIstanza);
			moonSrvDAO.inviaRispostaIntegrazione(idIstanza);
			log.debug("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::inviaRispostaIntegrazioneCosmo] Errore generico servizio inviaRispostaIntegrazioneCosmo", ex);
			throw new ServiceException("Errore generico servizio inviaRispostaIntegrazioneCosmo");
		} 
	}	
	
	
	@Override
	public Response getLogPraticaCosmo(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getLogPraticaCosmo] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, "idIstanza");
			List<LogPraticaCosmo> ris = cosmoService.getElencoLogPraticaByIdIstanza(idIstanza);
			log.debug("[" + CLASS_NAME + "::getLogPraticaCosmo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getLogPraticaCosmo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getLogPraticaCosmo] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getLogPraticaCosmo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getLogPraticaCosmo");
		}
	}
	
	@Override
	public Response getLogServizioCosmo(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getLogServizioCosmo] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, "idIstanza");
			List<LogServizioCosmo> ris = cosmoService.getElencoLogServizioByIdIstanza(idIstanza);
			log.debug("[" + CLASS_NAME + "::getLogServizioCosmo] END");
	        return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getLogServizioCosmo] Errore UnprocessableEntityException",uee);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getLogServizioCosmo] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getLogServizioCosmo] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getLogServizioCosmo");
		}
	}

	private Istanza completaIstanzaEpay(Istanza istanza) {
		try {
			List<EpayRichiestaEntity> richiesteE = epayRichiestaDAO.findByIdIstanza(istanza.getIdIstanza());
			if (richiesteE != null && richiesteE.size()>0) {
				EpayRichiestaEntity richiesta = richiesteE.get(richiesteE.size()-1);
				istanza.setDataEsitoPagamento(richiesta.getDataNotificaPagamento());
				istanza.setPagamenti(richiesteE.stream()
					.map( r -> {
						Pagamento res = PagamentoMapper.buildFromEpayRichiestaEntity(r);
						return completaPagamentoNotifica(res, r.getIdNotificaPagamento());
					})
					.collect(Collectors.toList()));
			}
		} catch (Exception e) {
			log.warn("[" + CLASS_NAME + "::completaIstanzaEpay] Exception but continue istanza:" + istanza.getIdIstanza(), e);
		}
		return istanza;
	}
	
	Pagamento completaPagamentoNotifica(Pagamento pagamento, Long idNotifica) {
		if (idNotifica == null)
			return pagamento;
		pagamento.setNotifica(PagamentoNotificaMapper.buildFromEntity(epayNotificaPagamentoDAO.findById(idNotifica)));
		return pagamento;
	}
	
	
	@Override
	public Response pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] IN idIstanza: " + idIstanza);
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] IN idStoricoWorkflow: " + idStoricoWorkflow);
			moonSrvDAO.pubblicaMyDocs(idIstanza, idStoricoWorkflow);
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] Errore generico servizio pubblicaMyDocs", ex);
			throw new ServiceException("Errore generico servizio pubblicaMyDocs");
		} 
	}	
	
	@Override
	public Response pubblicaIstanzaMyDocs(Long idIstanza, Long idRichiesta, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] IN idIstanza: " + idIstanza);
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] IN idRichiesta: " + idRichiesta);
			moonSrvDAO.pubblicaMyDocs(idIstanza, idRichiesta);
			log.debug("[" + CLASS_NAME + "::pubblicaMyDocs] END");
	        return Response.ok().build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] istanza non trovata", notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pubblicaMyDocs] Errore generico servizio pubblicaMyDocs", ex);
			throw new ServiceException("Errore generico servizio pubblicaMyDocs");
		} 
	}	
	
	@Override
	public Response getLogMyDocs(Long idIstanza,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getLogMyDocs] IN idIstanza: " + idIstanza);
			}
			required(idIstanza, "idIstanza");
			List<MyDocsRichiestaEntity> ris = moonSrvDAO.getLogRichiesteMyDocs(idIstanza);
			log.debug("[" + CLASS_NAME + "::getLogMyDocs] END");
	        return Response.ok(ris).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getLogMyDocs] BusinessException " + be.getMessage());
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getLogMyDocs] Errore generico servizio pubblicaMyDocs", ex);
			throw new ServiceException("Errore generico servizio pubblicaMyDocs");
		} 
	}


}
