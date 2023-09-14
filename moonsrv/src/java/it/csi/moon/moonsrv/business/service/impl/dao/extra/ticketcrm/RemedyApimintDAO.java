/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.ticketcrm;

import java.util.List;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaApplicativa;
import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaTicket;
import it.csi.apimint.troubleticketing.v1.dto.ConfigurationItem;
import it.csi.apimint.troubleticketing.v1.dto.Ente;
import it.csi.apimint.troubleticketing.v1.dto.InfoNotaWLog;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.RichiedentePerAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.apimint.troubleticketing.v1.dto.TicketExpo;
import it.csi.apimint.troubleticketing.v1.dto.TicketSnapshot;

/**
* DAO TroubleTicketing Remedy via servizi REST 
* - via API Manager Outer
* 
* @author Laurent Pissard
* 
* @since 1.0.0
*/
public interface RemedyApimintDAO {

	//
	public List<RichiedenteDaAnagrafica> findUsers(String emailFilter, String cognomeFilter, String nomeFilter);
	public RichiedenteDaAnagrafica findUserById(String personId);
	public RichiedentePerAnagrafica createUser(RichiedentePerAnagrafica u);
	
	//
	public List<Ente> findEnti();
	
	//
	public List<CategoriaOperativaTicket> findCategorieOperative();
	public List<CategoriaApplicativa> getCategorieApplicative();

	//
	public List<ConfigurationItem> getConfigurationItems(String personId);
	
	//
	public Ticket createTicket(Ticket ticket);
	public LavorazioneTicket getWorkinfoTicket(String ticketId);
	public List<TicketSnapshot> getLastUpdated();
	public List<TicketExpo> getLastRegistered();
	//
	public InfoNotaWLog putInfoDettagli(String ticketId,String tipologia,String riepilogo,String nomeFile,byte[] pdf);
}
 