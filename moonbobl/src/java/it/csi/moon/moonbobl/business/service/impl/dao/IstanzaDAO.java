/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;
import java.util.Optional;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 */
public interface IstanzaDAO {
	public IstanzaEntity findById(Long id) throws DAOException;
	public IstanzaCronologiaStatiEntity findLastCronologia(Long idIstanza) throws DAOException;
	public IstanzaCronologiaStatiEntity findInvio(Long idIstanza) throws DAOException;
	public IstanzaDatiEntity findDati(Long idIstanza, Long idCronologiaStati) throws DAOException;
	public IstanzaDatiEntity findLastCronDati(Long idIstanza) throws DAOException;
	
	public List<IstanzaEntity> find(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException;
	public List<IstanzaEntity> findInLavorazione(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException;
	public Integer countInLavorazione(IstanzeFilter filter, String filtroRicercaDati) throws DAOException;
	public List<IstanzaEntity> findInBozza(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException;
	public Integer countInBozza(IstanzeFilter filter, String filtroRicercaDati) throws DAOException;
	public Integer count(Integer stato, IstanzeFilter filter, String filtroRicercaDati) throws DAOException;
	public Long insert(IstanzaEntity entity) throws DAOException;
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException;
	
	public int update(IstanzaEntity entity) throws DAOException;
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public int updateDati(IstanzaDatiEntity entity) throws DAOException;
	public int updateProtocollo(IstanzaEntity entity) throws DAOException;
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException;
	
	public List<IstanzaEntity> find( IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException;
	
	public Integer countArchivio(IstanzeFilter filter, String filtroRicercaDati) throws DAOException;
	public List<IstanzaEntity> findArchivio(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException;

	public List<Integer> findDistinctIdStatiBoPossibili(IstanzeFilter filter, String filtroRicercaDati);
	
	public Integer countIstanzeByModuloHash(Long idModulo, String hash, String codiceIstanza) throws DAOException;
	public List<IstanzaEntity> findDaCompletare(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati)
			throws DAOException;
	public Integer countDaCompletare(IstanzeFilter filter, String filtroRicercaDati) throws DAOException;
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome);
	
	public String findEmailUfficioByIdIstanza(Long idIstanza) throws DAOException;
}
