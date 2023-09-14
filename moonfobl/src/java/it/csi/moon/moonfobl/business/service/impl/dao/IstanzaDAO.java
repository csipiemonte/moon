/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;
import java.util.Optional;

import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonfobl.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle istanze direttamente su Database
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface IstanzaDAO {
	public IstanzaEntity findById(Long id) throws DAOException;
	public IstanzaEntity findByCd(String codiceIstanza) throws DAOException;
	public IstanzaCronologiaStatiEntity findLastCronologia(Long idIstanza) throws DAOException;
	public IstanzaDatiEntity findDati(Long idIstanza, Long idCronologiaStati) throws DAOException;
	public List<IstanzaEntity> find(IstanzeFilter filter, String filtroRicerca, Optional<IstanzeSorter> sorter) throws DAOException;
	public Integer count(IstanzeFilter filter, String filtroRicerca) throws DAOException;
	
	public IstanzaDatiEntity findLastCronDati(Long idIstanza) throws DAOException;
	public IstanzaCronologiaStatiEntity findInvio(Long idIstanza) throws DAOException;
	
	public Long insert(IstanzaEntity entity) throws DAOException;
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException;
	
	public int update(IstanzaEntity entity) throws DAOException;
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public int updateDati(IstanzaDatiEntity entity) throws DAOException;
	
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException;
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome) throws DAOException;
	
	public Integer countIstanzeByModuloHash(Long idModulo, String hash, String codiceIstanza) throws DAOException;
	
}
