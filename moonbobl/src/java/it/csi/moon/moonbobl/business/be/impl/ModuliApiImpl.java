/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.ModuliApi;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.ModuliService;
import it.csi.moon.moonbobl.business.service.ModuloClassService;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.InitModulo;
import it.csi.moon.moonbobl.dto.moonfobl.InitModuloCambiaStato;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloAttributo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonbobl.dto.moonfobl.NuovaVersioneModuloRequest;
import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloParametro;
import it.csi.moon.moonbobl.dto.moonfobl.StatoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.UtenteModuloAbilitato;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaStatoModulo;

@Component
public class ModuliApiImpl extends MoonBaseApiImpl implements ModuliApi {
	
	private final static String CLASS_NAME = "ModuliApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();	
	  
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModuloDAO moduloDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	@Autowired
	ModuloClassService moduloClassService;
	
	@Autowired 
	AuditService auditService;
	
	public Response getModuli(String idModuloQP, String onlyLastVersione, String otherIdentificativoUtente, String pubblicatoBO, String fields,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getModuli] IN idModuloQP: " + idModuloQP);
				log.debug("[" + CLASS_NAME + "::getModuli] IN onlyLastVersioneQP: " + onlyLastVersione);
				log.debug("[" + CLASS_NAME + "::getModuli] IN otherIdUtenteQP: " + otherIdentificativoUtente);
				log.debug("[" + CLASS_NAME + "::getModuli] IN fieldsQP: " + fields);
				log.debug("[" + CLASS_NAME + "::getModuli] IN pubblicatoBO: " + pubblicatoBO);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLong(idModuloQP);
			List<Modulo> elenco = moduliService.getElencoModuliAbilitati(user, idModulo, onlyLastVersione, otherIdentificativoUtente, pubblicatoBO);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getModuli", 
														""));
// Da Fissare su WCL se elenco è vuota !
//			if (elenco.size() == 0)
//				throw new ResourceNotFoundException();
			return Response.ok(elenco).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getModuli] Errore BusinessException ", e);
			throw new ServiceException("Errore servizio elenco moduli");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getModuli] Errore generico servizio getModuli", ex);
			throw new ServiceException("Errore generico servizio elenco moduli");
		}  
	}


	@Override
	public Response getModuloById(String idModuloPP, String idVersioneModuloPP, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getModuloById] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::getModuloById] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::getModuloById] IN fields: " + fields);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			Modulo modulo = moduliService.getModuloById(user, idModulo, idVersioneModulo, fields);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getModuli-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			return Response.ok(modulo).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getModuloById] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP);
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getModuloById] modulo non trovato " + idModuloPP + "/" + idVersioneModuloPP);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getModuloById] Errore BusinessException " + idModuloPP + "/" + idVersioneModuloPP);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getModuloById] Errore generico servizio getModuloById " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio get modulo");
		} 
	}
	
      
	@Override
	public Response getStrutturaByIdStruttura(String idStrutturaPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getStrutturaByIdStruttura] IN idStrutturaPP: " + idStrutturaPP);
			}
			Long idStruttura = validaLongRequired(idStrutturaPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			String struttura = moduliService.getStrutturaByIdStruttura(user, idStruttura);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getStruttura-idStruttura", 
														idStruttura.toString() ));
			return Response.ok(struttura).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] struttura non trovata " + idStrutturaPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore BusinessException " + idStrutturaPP, e);
			throw new ServiceException("Errore servizio getStrutturaByIdStruttura");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore UnprocessableEntityException " + idStrutturaPP, uee);
			throw new ServiceException("Errore generico servizio get modulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getStrutturaByIdStruttura] Errore generico servizio getStrutturaByIdStruttura " + idStrutturaPP, ex);
			throw new ServiceException("Errore generico servizio getStrutturaByIdStruttura");
		} 
	}


	@Override
	public Response saveModulo(Modulo body,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) throws ServiceException {
		try {
			log.debug("[" + CLASS_NAME + "::saveModulo] IN \n body: "+body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Modulo modulo = moduliService.insertModulo(user, body);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.INSERT,
														"saveModulo-idModulo", 
														modulo.getIdModulo().toString()));
			return Response.ok(modulo).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::saveModulo] Errore BusinessException", be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::saveModulo]  Errore generico servizio saveModulo",ex);
			throw new ServiceException("Errore generico servizio inserisci modulo");
		}
	}
	
	@Override
	public Response putModulo(String idModuloPP, String idVersioneModuloPP, Modulo body,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::putModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::putModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::putModulo] IN body: "+body);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			Modulo modulo = moduliService.updateModulo(user, body);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.UPDATE,
														"updateModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			return Response.ok(modulo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putModulo] Errore BusinessException " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio aggiorna modulo");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::putModulo] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP, uee);
			throw new ServiceException("Errore generico servizio get modulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putModulo] Errore generico servizio putModulo " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio aggiorna modulo");
		}
	}

	@Override
	public Response getInitCambiaStatoById(String idModuloPP, String idVersioneModuloPP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getInitCambiaStatoById] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::getInitCambiaStatoById] IN idVersioneModuloPP: " + idVersioneModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			InitModuloCambiaStato result = moduliService.getInitCambiaStatoById(user, idModulo, idVersioneModulo);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.UPDATE,
														"initCambiaStatoModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			return Response.ok(result).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getInitCambiaStatoById] Errore servizio getInitCambiaStatoById " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio getInitCambiaStatoById");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getInitCambiaStatoById] Errore generico servizio getInitCambiaStatoById " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio getInitCambiaStatoById");
		}
	}
	
	@Override
	public Response pubblicaModulo(String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::pubblicaModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::pubblicaModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			Modulo modulo = moduliService.cambiaStato(user, idModulo, idVersioneModulo, DecodificaStatoModulo.PUBBLICATO);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.UPDATE,
														"pubblicaModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			return Response.ok(modulo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::pubblicaModulo] Errore servizio pubblicaModulo " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio pubblica modulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::pubblicaModulo] Errore generico servizio pubblicaModulo " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio pubblic modulo");
		}
	}

	@Override
	public Response cambiaStatoModulo(String idModuloPP, String idVersioneModuloPP, String newStatoPP, 
		String inDataOraQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::cambiaStatoModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::cambiaStatoModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::cambiaStatoModulo] IN newStatoPP: " + newStatoPP);
				log.debug("[" + CLASS_NAME + "::cambiaStatoModulo] IN inDataOraQP: " + inDataOraQP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			String newStato = validaStringRequired(newStatoPP);
			Date inDataOra = validaDateTime(inDataOraQP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Modulo modulo = moduliService.cambiaStato(user, idModulo, idVersioneModulo, DecodificaStatoModulo.byCodice(newStato), inDataOra);
			return Response.ok(modulo).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::cambiaStatoModulo] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP + "/" + newStatoPP, uee);
			throw new ServiceException("Errore generico servizio get modulo");
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::cambiaStatoModulo] Errore servizio cambiaStatoModulo " + idModuloPP + "/" + idVersioneModuloPP + "/" + newStatoPP, be);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::cambiaStatoModulo] Errore generico servizio cambiaStatoModulo " + idModuloPP + "/" + idVersioneModuloPP + "/" + newStatoPP, ex);
			throw new ServiceException("Errore generico servizio cambiaStatoModulo");
		}
	}



	@Override
	public Response putStruttura(String idModuloPP, String idVersioneModuloPP, Modulo body,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::putStruttura] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::putStruttura] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::putStruttura] IN body: " + body);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			//
			Modulo modulo = moduliService.updateStruttura(user, idModulo, idVersioneModulo, body.getStruttura());
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.UPDATE,
														"updateStruttura-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			return Response.ok(modulo).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::putStruttura]  Errore servizio putStruttura",e);
			throw new ServiceException("Errore servizio aggiorna modulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::putStruttura]  Errore generico servizio putStruttura",ex);
			throw new ServiceException("Errore generico servizio aggiorna struttura modulo");
		}
	}
	
	@Override
	public Response getCampiModulo(String idModuloPP, String idVersioneModuloPP, String onlyFirstLevel,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getCampiModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::getCampiModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::getCampiModulo] IN onlyFirstLevel: " + onlyFirstLevel);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getCampiModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			List<CampoModulo> campi = moonsrvDAO.getCampiModulo(idModulo, idVersioneModulo, null,onlyFirstLevel);
			return Response.ok(campi).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getCampiModulo] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCampiModulo] Errore servizio getCampiModulo",e);
			throw new ServiceException("Errore servizio getCampiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCampiModulo] Errore generico servizio getCampiModulo",ex);
			throw new ServiceException("Errore generico servizio getCampiModulo");
		} 
	}


	/*
	 * DA RIVEDERE FIXME PRIMA DA FISSARE SU MOONSRV CON NUOVO SERVIZIO SOLO PER ID_MODULO (bisogna fare la somme di tutti campi in una map IN MONNSRV)
	 */
	@Override
	public Response getCampiModuloVersioneCorrente(String idModuloPP, String onlyFirstLevel,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getCampiModuloVersioneCorrente] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::getCampiModuloVersioneCorrente] IN onlyFirstLevel: " + onlyFirstLevel);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			ModuliFilter filter = new ModuliFilter();
			filter.setIdModulo(idModulo);
			List<ModuloVersionatoEntity> moduli = moduloDAO.find(filter);
			ModuloVersionatoEntity modulo = moduli.get(0); // FIXME Multiversione non funziona, bisogna fare la somme di tutti campi in una map
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getCampiModuloVersioneCorrente-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + modulo.getIdVersioneModulo()));
			List<CampoModulo> campi = moonsrvDAO.getCampiModulo(idModulo, modulo.getIdVersioneModulo(),null,onlyFirstLevel);
			return Response.ok(campi).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getCampiModuloVersioneCorrente] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getCampiModuloVersioneCorrente] Errore servizio getCampiModuloVersioneCorrente",e);
			throw new ServiceException("Errore servizio getCampiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getCampiModuloVersioneCorrente] Errore generico servizio getCampiModuloVersioneCorrente",ex);
			throw new ServiceException("Errore generico servizio getCampiModuloVersioneCorrente");
		} 
	}



	@Override
	public Response getAttributiModulo(String idModuloPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getAttributiModulo] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			List<ModuloAttributo> attributi = moduliService.getAttributiModulo(idModulo);
			return Response.ok(attributi).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getAttributiModulo] Errore servizio getAttributiModulo",e);
			throw new ServiceException("Errore servizio getAttributiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getAttributiModulo] Errore generico servizio getAttributiModulo",ex);
			throw new ServiceException("Errore generico servizio getAttributiModulo");
		}
	}


	@Override
	public Response getObjAttributiModuloBO(String idModuloPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getObjAttributiModuloBO] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			String attributiJSON = moduliService.getObjAttributiModuloBO(idModulo);
			return Response.ok(attributiJSON).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getObjAttributiModuloBO] Errore servizio",e);
			throw new ServiceException("Errore servizio getAttributiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getObjAttributiModuloBO] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio getObjAttributiModuloBO");
		}
	}
	
	@Override
    public Response postAttributiGenerali( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiGenerali] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiGenerali(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiGenerali] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiGenerali] Throwable",ex);
			throw new ServiceException();
		}
    }
    
	@Override
    public Response postAttributiEmail( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiEmail] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiEmail(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiEmail] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiEmail] Throwable",ex);
			throw new ServiceException();
		}
    }
    
	@Override
    public Response postAttributiNotificatore( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
   		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiNotificatore] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiNotificatore(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiNotificatore] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiNotificatore] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiProtocollo( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiProtocollo] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiProtocollo(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiProtocollo] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiProtocollo] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiCosmo( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiCosmo] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiCosmo(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiCosmo] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiCosmo] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiAzione( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiAzione] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiAzione(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiAzione] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiAzione] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiEstraiDichiarante( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiEstraiDichiarante] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiEstraiDichiarante(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiEstraiDichiarante] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiEstraiDichiarante] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiCrm( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiCrm] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiCrm(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiCrm] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiCrm] Throwable",ex);
			throw new ServiceException();
		}
    }

	@Override
    public Response postAttributiEpay( @PathParam("idModulo") String idModuloPP, ModuloAttributo[] attributi,
       		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::postAttributiEpay] IN idModuloPP: " + idModuloPP);
			}
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloAttributo> resp = moduliService.aggiornaInserisciAttributiEpay(idModulo, attributi, user);
			return Response.ok(resp).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::postAttributiEpay] BusinessException");
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postAttributiEpay] Throwable",ex);
			throw new ServiceException();
		}
    }
    
	private void verificaAbilitazioneModulo(Long idModulo, UserInfo user) throws ServiceException {
		boolean isUtenteAbilitato = moduliService.verificaAbilitazioneModulo(idModulo, user);
		if (!isUtenteAbilitato) {
			log.error("[" + CLASS_NAME + "::verificaAbilitazioneModulo] NonAbilitato " + user.getIdentificativoUtente() + " / " + idModulo);
			throw new ServiceException("Utente non abilitato al modulo indicato");
		}
	}


	@Override
	public Response getElencoStatiModulo(String codiceProvenienza, String codiceDestinazione,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws BusinessException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getElencoStatiModulo] IN codiceProvenienza: " + codiceProvenienza + "  codiceDestinazione: " + codiceDestinazione);
			}
			List<StatoModulo> stati = moduliService.getElencoStatiModulo(codiceProvenienza, codiceDestinazione);
			return Response.ok(stati).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getElencoStatiModulo] Errore servizio getElencoStatiModulo",e);
			throw new ServiceException("Errore servizio getElencoStatiModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getElencoStatiModulo] Errore generico servizio getElencoStatiModulo",ex);
			throw new ServiceException("Errore generico servizio getElencoStatiModulo");
		}
	}
	

	@Override
	public Response getinitNuovaVersioneModuloById(String idModuloPP, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getinitNuovaVersioneModuloById] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			InitModulo initModulo = moduliService.getInitNuovaVersioneModuloById(user, idModulo);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getinitNuovaVersioneModuloById-idModulo", 
														idModulo.toString()));
			return Response.ok(initModulo).build();
		} catch (ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getinitNuovaVersioneModuloById] modulo non trovato " + idModuloPP);
			throw new ResourceNotFoundException();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getinitNuovaVersioneModuloById] Errore BusinessException " + idModuloPP, be);
			throw new ServiceException(be.getMessage());
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getinitNuovaVersioneModuloById] Errore UnprocessableEntityException " + idModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getinitNuovaVersioneModuloById] Errore generico servizio getinitNuovaVersioneModuloById " + idModuloPP, ex);
			throw new ServiceException("Errore generico servizio getinitNuovaVersioneModuloById");
		} 
	}
	
	
	@Override
	public Response deleteModulo(String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::deleteModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::deleteModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.DELETE,
														"deleteModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			moduliService.deleteModulo(user, idModulo, idVersioneModulo);
			return Response.ok().build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::deleteModulo] Errore BusinessException " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException(e.getMessage());
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::deleteModulo] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::deleteModulo] Errore generico servizio " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio deleteModulo");
		}
	}


	@Override
	public Response savePortaliModulo(String idModuloPP, String idVersioneModuloPP, List<Long> portali,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::savePortaliModulo] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::savePortaliModulo] IN idVersioneModuloPP: " + idVersioneModuloPP);
				log.debug("[" + CLASS_NAME + "::savePortaliModulo] IN portali: "+portali);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			moduliService.savePortaliModulo(user, idModulo, idVersioneModulo, portali);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.INSERT,
														"savePortaliModulo-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString() + " - " + portali));
			return Response.ok().build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::savePortaliModulo] Errore BusinessException " + idModuloPP + "/" + idVersioneModuloPP, e);
			throw new ServiceException("Errore servizio savePortaliModulo");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::savePortaliModulo] Errore UnprocessableEntityException " + idModuloPP + "/" + idVersioneModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::savePortaliModulo] Errore generico servizio " + idModuloPP + "/" + idVersioneModuloPP, ex);
			throw new ServiceException("Errore generico servizio savePortaliModulo");
		}
	}


	@Override
	public Response nuovaVersione(String idModuloPP, NuovaVersioneModuloRequest body, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws ResourceNotFoundException, ServiceException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::nuovaVersione] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::nuovaVersione] IN body: " + body);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			validaNuovaVersioneModuloRequest(body);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			Modulo modulo = moduliService.nuovaVersione(user, idModulo, body);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.INSERT,
														"nuovaVersione-idModulo-versione-idVersionePartenza", 
														idModulo.toString() + "-" + body.getVersione() + " - " + body.getIdVersionePartenza().toString()));
			return Response.ok(modulo).build();
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::nuovaVersione] BusinessException " + idModuloPP, be);
			throw new ServiceException(be);
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::nuovaVersione] UnprocessableEntityException " + idModuloPP, uee);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::nuovaVersione] Throwable " + idModuloPP, ex);
			throw new ServiceException("Errore generico servizio nuovaVersione");
		}
	}


	private void validaNuovaVersioneModuloRequest(NuovaVersioneModuloRequest body) throws UnprocessableEntityException {
		if (body==null) throw new UnprocessableEntityException();
		validaStringRequired(body.getVersione());
		if (body.getIdVersionePartenza()==null) throw new UnprocessableEntityException();
	}
	
	@Override
	public Response getVersioniModuloById(String idModuloPP, String fields, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getVersioniModuloById] IN idModuloPP: " + idModuloPP + "  fields: " + fields);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloVersioneStato> stati = moduliService.getVersioniModuloById(idModulo, fields);
			return Response.ok(stati).build();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getVersioniModuloById] Errore servizio",e);
			throw new ServiceException("Errore servizio getElencoStatiModulo");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getVersioniModuloById] Errore UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getVersioniModuloById] Errore generico servizio",ex);
			throw new ServiceException("Errore generico servizio getVersioniModuloById");
		}
	}
	
	@Override
    public Response getUtentiAbilitati( String idModuloPP, String fields,
    	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest ) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getUtentiAbilitati] IN idModuloPP: " + idModuloPP + "  fields: " + fields);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			List<UtenteModuloAbilitato> list = moduliService.getUtentiAbilitati(idModulo, fields);
			return Response.ok(list).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getUtentiAbilitati] UnprocessableEntityException idModulo = " + idModuloPP);
			throw uee;
		} catch (BusinessException be) {
			log.error("[" + CLASS_NAME + "::getUtentiAbilitati] BusinessException idModulo = " + idModuloPP);
			throw new ServiceException(be);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getUtentiAbilitati] Errore generico servizio", ex);
			throw new ServiceException("Errore generico servizio getUtentiAbilitati");
		}
    }

	@Override
	public Response getPrtParametri(String idModuloPP, String idVersioneModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::getPrtParametri] IN idModuloPP: " + idModuloPP);
				log.debug("[" + CLASS_NAME + "::getPrtParametri] IN idVersioneModuloPP: " + idVersioneModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			Long idVersioneModulo = validaLongRequired(idVersioneModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"getPrtParametri-idModulo-idVersioneModulo", 
														idModulo.toString() + "-" + idVersioneModulo.toString()));
			List<ProtocolloParametro> ris = moonsrvDAO.getProtocolloParametri(idModulo, idVersioneModulo);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::getPrtParametri] struttura non trovata",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getPrtParametri] Errore servizio getPrtParametri",e);
			throw new ServiceException("Errore servizio getPrtParametri");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getPrtParametri] Errore UnprocessableEntityException idModuloPP/idVersioneModuloPP=" + idModuloPP + "/" + idVersioneModuloPP);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPrtParametri] Errore generico servizio getPrtParametri",ex);
			throw new ServiceException("Errore generico servizio getPrtParametri");
		} 
	}

	@Override
	public Response hasPrtParametri(String idModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::hasPrtParametri] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"has-protocollo-parametri-idModulo", 
														idModulo.toString()));
			boolean ris = moduliService.hasProtocolloParameters(idModulo);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::hasPrtParametri] ItemNotFoundBusinessException",notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::hasPrtParametri] Errore servizio hasPrtParametri",e);
			throw new ServiceException("Errore servizio hasPrtParametri");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::hasPrtParametri] Errore UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::hasPrtParametri] Errore generico servizio hasPrtParametri",ex);
			throw new ServiceException("Errore generico servizio hasPrtParametri");
		} 
	}


	@Override
	public Response postModuloClass(String idModuloPP, int idTipologia, MultipartFormDataInput file, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::postModuloClass] IN idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			byte[] bytes = file.getParts().get(0).getBody(InputStream.class, null).readAllBytes();
			Long idModulo = validaLongRequired(idModuloPP);
			ModuloClass ris = moduloClassService.uploadFileClass(idModulo,idTipologia,bytes);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::postModuloClass] Errore UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (BusinessException e) {
			throw new ServiceException(e);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::postModuloClass] Errore generico servizio postModuloClass",ex);
			throw new ServiceException("Errore generico servizio postModuloClass");
		} 
	}
	
	@Override
	public Response deleteModuloClass(String idModuloPP, int idTipologia,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::deleteModuloClass] IN idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			Long idModulo = validaLongRequired(idModuloPP);
			moduloClassService.delete(idModulo, idTipologia);
			return Response.ok().build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::deleteModuloClass] Errore UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (BusinessException e) {
			throw new ServiceException(e);
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::deleteModuloClass] Errore generico servizio deleteModuloClass",ex);
			throw new ServiceException("Errore generico servizio deleteModuloClass");
		} 
	}

	@Override
	public Response getFileClassByIdModuloTipologia(String idModuloPP, int idTipologia,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] IN idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			Long idModulo = validaLongRequired(idModuloPP);
			ModuloClass ris = moduloClassService.getFileClassByIdModuloTipologia(idModulo,idTipologia);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] Errore UnprocessableEntityException idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			throw uee;
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.warn("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] ItemNotFoundBusinessException for idModuloPP=" + idModuloPP + " idTipologia=" + idTipologia);
			throw new ResourceNotFoundException("FileClassByIdModuloTipologia ItemNotFound");
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] Errore servizio hasPrtParametri",e);
			throw new ServiceException("Errore generico servizio getFileClassByIdModuloTipologia");
		}  catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModuloTipologia] Errore generico servizio getFileClassByIdModuloTipologia",ex);
			throw new ServiceException("Errore generico servizio getFileClassByIdModuloTipologia");
		} 
	}


	@Override
	public Response getFileClassByIdModulo(String idModuloPP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.debug("[" + CLASS_NAME + "::getFileClassByIdModulo] IN idModuloPP=" + idModuloPP);
			Long idModulo = validaLongRequired(idModuloPP);
			List<ModuloClass> ris = moduloClassService.getFileClassByIdModulo(idModulo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModulo] Errore UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModulo] Errore servizio getFileClassByIdModulo",e);
			throw new ServiceException("Errore servizio getFileClassByIdModulo");
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getFileClassByIdModulo] Errore generico servizio getFileClassByIdModulo",ex);
			throw new ServiceException("Errore generico servizio getFileClassByIdModulo");
		} 
	}
	
	
	@Override
    public Response getPrintMapperName( @PathParam("codiceModulo") String codiceModuloPP,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
		try {
			String codiceModulo = validaStringRequired(codiceModuloPP);
			String ris = moonsrvDAO.getPrintMapperName(codiceModulo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getPrintMapperName] Errore UnprocessableEntityException codiceModuloPP=" + codiceModuloPP);
			throw uee;
		}  catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getPrintMapperName] Errore generico servizio getPrintMapperName",ex);
			throw new ServiceException("Errore generico servizio getPrintMapperName");
		} 
	}
	
	@Override
    public Response getProtocolloManagerName( @PathParam("codiceModulo") String codiceModuloPP,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
		try {
			String codiceModulo = validaStringRequired(codiceModuloPP);
			String ris = moonsrvDAO.getProtocolloManagerName(codiceModulo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getProtocolloManagerName] Errore UnprocessableEntityException codiceModuloPP=" + codiceModuloPP);
			throw uee;
		}  catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getProtocolloManagerName] Errore generico servizio getProtocolloManagerName",ex);
			throw new ServiceException("Errore generico servizio getProtocolloManagerName");
		} 
	}
	
	@Override
	public Response hasPrtBo(String idModuloPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::hasPrtBo] IN idModuloPP: " + idModuloPP);
			}
			Long idModulo = validaLongRequired(idModuloPP);
			//
			UserInfo user = retrieveUserInfo(httpRequest);
			verificaAbilitazioneModulo(idModulo, user);
			// Audit Operazione
			auditService.traceOperazione(new AuditEntity(httpRequest.getRemoteAddr(), 
														auditService.retrieveUser(user), 
														AuditEntity.EnumOperazione.READ,
														"has-protocollo-bo-idModulo", 
														idModulo.toString()));
			boolean ris = moduliService.hasProtocolloBo(idModulo);
			log.info("[" + CLASS_NAME + "::hasPrtBo] IN idModuloPP: " + idModuloPP + " OUT: " + ris);
			return Response.ok(ris).build();
		} catch(ItemNotFoundBusinessException notFoundEx) {
			log.error("[" + CLASS_NAME + "::hasPrtBo] ItemNotFoundBusinessException idModuloPP=" + idModuloPP, notFoundEx);
			throw new ResourceNotFoundException();
		} catch (BusinessException e) {
			log.error("[" + CLASS_NAME + "::hasPrtBo] BusinessException idModuloPP=" + idModuloPP, e);
			throw new ServiceException("Errore servizio hasPrtBo");
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::hasPrtBo] UnprocessableEntityException idModuloPP=" + idModuloPP);
			throw uee;
		} catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::hasPrtBo] Errore generico servizio hasPrtBo",ex);
			throw new ServiceException("Errore generico servizio hasPrtBo");
		} 
	}
	
	@Override
    public Response getEpayManagerName( @PathParam("codiceModulo") String codiceModuloPP,
        	@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest ) {
		try {
			String codiceModulo = validaStringRequired(codiceModuloPP);
			String ris = moonsrvDAO.getEpayManagerName(codiceModulo);
			return Response.ok(ris).build();
		} catch (UnprocessableEntityException uee) {
			log.error("[" + CLASS_NAME + "::getEpayManagerName] Errore UnprocessableEntityException codiceModuloPP=" + codiceModuloPP);
			throw uee;
		}  catch (Throwable ex) {
			log.error("[" + CLASS_NAME + "::getEpayManagerName] Errore generico servizio getEpayManagerName",ex);
			throw new ServiceException("Errore generico servizio getEpayManagerName");
		} 
	}
	
}
