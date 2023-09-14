/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.Date;
import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersioneEntity;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai moduli
 * 
 * @see ModuloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public interface ModuloDAO {
	
//	public void initCache();
	public static final String ORDER_BY_DATA_UPD_DESC = " ORDER BY data_upd DESC, id_versione_modulo DESC";
	public static final String ORDER_BY_OGGETTO_ASC = " ORDER BY oggetto_modulo ASC, id_versione_modulo DESC";
	
	public ModuloEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public ModuloEntity findByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException;
	
	public List<ModuloEntity> getElencoModuli() throws DAOException;
	public List<ModuloVersionatoEntity> find(ModuliFilter filter) throws DAOException;
	public List<ModuloVersionatoEntity> find(ModuliFilter filter, String order) throws DAOException;
	
	public List<ModuloCronologiaStatiEntity> findAllCronologia(Long idVersioneModulo) throws DAOException;
	public ModuloCronologiaStatiEntity findCurrentCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloCronologiaStatiEntity findLastCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;

	public ModuloVersioneEntity findVersioneById(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersioneEntity findVersione(Long idModulo, String versione) throws ItemNotFoundDAOException, DAOException;

	public ModuloVersionatoEntity findModuloVersionatoById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersionatoEntity findModuloVersionatoByCodice(String codiceModulo, String versione) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersionatoEntity findModuloVersionatoPubblicatoByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersionatoEntity findModuloVersionatoPubblicatoById(Long idModulo, Date inDataOra) throws ItemNotFoundDAOException, DAOException;

	public Long insert(ModuloEntity entity) throws DAOException;
	public Long insertCronologia(ModuloCronologiaStatiEntity entity) throws DAOException;
	public Long insertVersione(ModuloVersioneEntity entity) throws DAOException;
	
	public int update(ModuloEntity entity) throws DAOException;
	public int updateCronologia(ModuloCronologiaStatiEntity entity) throws DAOException;
	public int updateVersione(ModuloVersioneEntity entity) throws DAOException;

	public int delete(Long id) throws DAOException;
	public int deleteCronologia(Long idVersioneModulo) throws DAOException;
	public int deleteVersione(Long idVersioneModulo) throws DAOException;
	
	public List<ModuloVersioneStato> findVersioniModuloById(Long idModulo, String fields);
	public Long findIdProcesso(Long idModulo) throws DAOException;

}
