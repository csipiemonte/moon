/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.extra.demografia.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.extra.demografia.RelazioniParentelaApi;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.demografia.AnprRelazioniParentelaDAO;
import it.csi.moon.moonbobl.dto.extra.demografia.RelazioneParentela;
import it.csi.moon.moonbobl.dto.moonfobl.Error;

@Component
public class RelazioniParentelaApiImpl implements RelazioniParentelaApi {

	@Autowired
	@Qualifier("mock")
    AnprRelazioniParentelaDAO anprRelazioniParentelaDAO;
	
	@Override
	public Response getRelazioniParentela(String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<RelazioneParentela> ris = anprRelazioniParentelaDAO.findAll();
		return Response.ok(ris).build();
	}
	
	@Override
	public Response getRelazioneParentelaById(Integer codice, String fields, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		RelazioneParentela ris = anprRelazioniParentelaDAO.findByPK(codice);
		if (ris != null) {
			return Response.ok(ris).build();
		}
		else {
			Error err = new Error();
			err.setCode("404");
			err.setErrorMessage("relazioniParentela "+codice+" non trovata");
			return Response.serverError().entity(err).status(404).build();
		}
	}

}
