/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.tecno;

import java.util.List;

import it.csi.apimint.troubleticketing.v1.dto.CategoriaApplicativa;
import it.csi.apimint.troubleticketing.v1.dto.CategoriaOperativaTicket;
import it.csi.apimint.troubleticketing.v1.dto.ConfigurationItem;
import it.csi.apimint.troubleticketing.v1.dto.Ente;
import it.csi.apimint.troubleticketing.v1.dto.LavorazioneTicket;
import it.csi.apimint.troubleticketing.v1.dto.RichiedenteDaAnagrafica;
import it.csi.apimint.troubleticketing.v1.dto.Ticket;
import it.csi.apimint.troubleticketing.v1.dto.TicketExpo;
import it.csi.apimint.troubleticketing.v1.dto.TicketSnapshot;

/**
 * Metodi di business di TroubleTicketing Remedy via servizi REST
 * 
 * @author Laurent
 *
 * @since 1.0.0 - 17/05/2021 - Versione initiale
 */
public interface RemedyService {
	
    //
	// Entity RichiedenteDaAnagrafica
	public List<RichiedenteDaAnagrafica> getRichiedenteDaAnagrafica(String email, String cognome, String nome);
	public RichiedenteDaAnagrafica getRichiedenteDaAnagraficaById(String personId);
	
	//
	// Entity Ente
	public List<Ente> getEnti();
	
	//
	// Entity Categorie
	public List<CategoriaOperativaTicket> getCategorieOperative();
	public List<CategoriaApplicativa> getCategorieApplicative();
	
	//
	// Entity ConfigurationItem
	public List<ConfigurationItem> getConfigurationItems(String personId);
	
	//
	// Entity Ticket
	public Ticket createTicket(Ticket ticket);
	public LavorazioneTicket getWorkinfoTicket(String ticketId);
	public List<TicketSnapshot> getLastUpdated();
	public List<TicketExpo> getLastRegistered();
}
