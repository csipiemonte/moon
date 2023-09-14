/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.TicketingSystemRichiestaEntity;
import it.csi.moon.commons.entity.TicketingSystemRichiestaFilter;
import it.csi.moon.moonsrv.business.be.TicketCrmRichiesteApi;
import it.csi.moon.moonsrv.business.service.impl.dao.TicketingSystemRichiestaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;

@Component
public class TicketCrmRichiesteApiImpl extends MoonBaseApiImpl implements TicketCrmRichiesteApi {
	
	private static final String CLASS_NAME = "TicketCrmRichiesteApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	@Autowired
	private TicketingSystemRichiestaDAO ticketingSystemRichiestaDAO;

	@Override
	public Response getTicketCrmRichieste(
			String idRichiestaQP, String codiceQP,
			String idIstanzaQP, String idModuloQP,
			String tipoDocQP, String statoQP,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getTicketCrmRichieste] BEGIN");
			Long idRichiesta = validaLong(idRichiestaQP);
			Long idIstanza = validaLong(idIstanzaQP);
			Long idModulo = validaLong(idModuloQP);
			Integer tipoDoc = validaInteger(tipoDocQP);
			TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
			filter.setIdRichiesta(idRichiesta);
			filter.setCodiceRichiesta(codiceQP);
			filter.setIdIstanza(idIstanza);
			filter.setIdModulo(idModulo);
			filter.setTipoDoc(tipoDoc);
			filter.setStato(statoQP);
			List<TicketingSystemRichiestaEntity> elenco = ticketingSystemRichiestaDAO.find(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getTicketCrmRichieste] Errore servizio getTicketCrmRichieste", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTicketCrmRichieste] Errore generico servizio getTicketCrmRichieste",ex);
			throw new ServiceException("Errore generico getTicketCrmRgetTicketCrmRichiesteichiesta");
		}  
	}

	@Override
	public Response getTicketCrmRichiesteIstanzaTx(String idIstanzaPP, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.debug("[" + CLASS_NAME + "::getTicketCrmRichiesteIstanzaTx] BEGIN");
			Long idIstanza = validaLongRequired(idIstanzaPP);
			TicketingSystemRichiestaFilter filter = new TicketingSystemRichiestaFilter();
			filter.setIdIstanza(idIstanza);
			filter.setTipoDoc(TicketingSystemRichiestaEntity.TipoDoc.ISTANZA.getId());
			filter.setStato(TicketingSystemRichiestaEntity.Stato.TX.name());
			List<TicketingSystemRichiestaEntity> elenco = ticketingSystemRichiestaDAO.find(filter);
			return Response.ok(elenco).build();
		} catch (BusinessException be) {
			LOG.warn("[" + CLASS_NAME + "::getTicketCrmRichiesteIstanzaTx] Errore servizio getTicketCrmRichiesteIstanzaTx", be);
			throw new ServiceException(be);
		} catch (Exception ex) {
			LOG.error("[" + CLASS_NAME + "::getTicketCrmRichiesteIstanzaTx] Errore generico servizio getTicketCrmRichiesteIstanzaTx", ex);
			throw new ServiceException("Errore generico getTicketCrmRichiesteIstanzaTx");
		} 
	}
	
}
