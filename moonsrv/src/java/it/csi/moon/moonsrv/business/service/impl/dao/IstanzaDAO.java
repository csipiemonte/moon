/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.commons.entity.IstanzaApiEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
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
	public List<IstanzaEntity> find(IstanzeFilter filter, Optional<IstanzeSorter> sorter) throws DAOException;
	public Integer count(IstanzeFilter filter) throws DAOException;
	
	public Long insert(IstanzaEntity entity) throws DAOException;
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException;
	
	public int update(IstanzaEntity entity) throws DAOException;
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException;
	public int updateDati(IstanzaDatiEntity entity) throws DAOException;
	public int updateProtocollo(IstanzaEntity entity) throws DAOException;
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException;
	
	public List<String> findForApi(Integer idFruitore, Long idModulo, Integer idStato,Long idVersioneModulo,Long idEnte,String identificativoUtente, Date dataDa, Date dataA, boolean test) throws DAOException;
	public List<String> findForApi(Integer idFruitore, Long idModulo, Integer idStato,Long idVersioneModulo,Long idEnte,String identificativoUtente, Date dataDa, Date dataA, boolean test, Integer offset,Integer limit) throws DAOException;
	public List<IstanzaApiEntity> findForApi(Integer idFruitore, Long idModulo, Integer idStato,Long idVersioneModulo,Long idEnte,String identificativoUtente, Date dataDa, Date dataA, String ordinamento, boolean test) throws DAOException;
	
	public Integer countForApi(Integer idFruitore, Long idModulo, Integer idStato, Long idVersioneModulo, Long idEnte, String identificativoUtente, Date dataDa,Date dataA, boolean test) throws DAOException;
	
	public int updateProtocollo(Long idIstanza, String extractNumeroProtocollo, Date extractDataProtocollo);
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome);
	
	public Integer countIstanzeByModuloHash(Long idModulo, String hash) throws DAOException;
	public int updateHashUnivocita(Long idIstanza, String hashUnivocita);
	public int updateUuidIndex(IstanzaEntity entity) throws DAOException;																  
}
