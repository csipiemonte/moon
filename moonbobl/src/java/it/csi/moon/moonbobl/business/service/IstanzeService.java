/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaSaveResponse;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.moonfobl.DatiAggiuntiviHeaders;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.ResponsePaginated;
import it.csi.moon.moonbobl.dto.moonfobl.Stato;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonbobl.exceptions.business.UnivocitaIstanzaBusinessException;

/**
 * @author Laurent
 * Metodi di business relativi ai istanze
 */
public interface IstanzeService {
	public List<Istanza> getElencoIstanze(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;	
	public ResponsePaginated<Istanza> getElencoIstanzePaginate(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public List<Istanza> getElencoIstanzeInLavorazione(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public Istanza getIstanzaById(UserInfo user, Long idIstanza) throws ItemNotFoundBusinessException, BusinessException;
	public Istanza getIstanzaBozzaById(UserInfo user, Long idIstanza) throws BusinessException;
	public Istanza getIstanzaCompletaById(UserInfo user, Long idIstanza, String nomePortale) throws BusinessException;
	public Istanza getInitIstanza(UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params, HttpServletRequest httpRequest) throws UnivocitaIstanzaBusinessException, BusinessException;	
	public Istanza saveIstanza(UserInfo user, Istanza istanza) throws BusinessException;
	public Istanza saveIstanza(UserInfo user, Long idIstanza, Istanza body) throws BusinessException;
	public Istanza deleteIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public Istanza patchIstanza(UserInfo user, Long idIstanza, Istanza partialIstanza) throws BusinessException;
	public byte[] getPdfIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public byte[] getRicevutaPdfByIstanza(UserInfo user, Long idIstanza) throws BusinessException;
	public List<Stato> getElencoStatiBoSuElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public List<Istanza> getElencoIstanze(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati, String fields) throws BusinessException;
	public ResponsePaginated<Istanza> getElencoArchivioIstanzePaginate(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public ResponsePaginated<Istanza> getElencoIstanzeInLavorazionePaginate(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public ResponsePaginated<Istanza> getElencoIstanzeInBozzaPaginate(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
//	public ResponsePaginated<Istanza> getElencoArchivioIstanzePaginate(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws BusinessException;
	public void protocolla(UserInfo user, Long idIstanza) throws BusinessException;
	public void rinviaEmail(UserInfo user, Long idIstanza, String dest) throws BusinessException;
	public void rinviaEmails(Long idTag, String dest);
//	public byte[] getJsonIstanza(UserInfo user, Long idIstanza) throws BusinessException;
//	public String generaSalvaPdf(Long idIstanza) throws BusinessException;	
	public Istanza riportaInBozza(UserInfo user, Long idIstanza);
	public IstanzaSaveResponse invia(UserInfo user, Long idIstanza, DatiAggiuntiviHeaders daHeaders, String ipAddress);
	public IstanzaSaveResponse saveCompilaIstanza(UserInfo user, Istanza body);
	public ResponsePaginated<Istanza> getElencoIstanzeDaCompletarePaginate(IstanzeFilter filter,
			Optional<IstanzeSorter> optSorter, String filtroRicercaDati);
	public byte[] getCustomJsonIstanzeByModulo(UserInfo user, Long idModulo, String codiceEstrazione, Date createdStart, Date createdEnd) throws BusinessException;
}
