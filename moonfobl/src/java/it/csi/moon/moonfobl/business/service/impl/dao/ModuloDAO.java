/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ModuloVersioneEntity;
import it.csi.moon.moonfobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

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
	
	public static final String ORDER_BY_DATA_UPD_DESC = " ORDER BY data_upd DESC, id_versione_modulo DESC";
	public static final String ORDER_BY_OGGETTO_ASC = " ORDER BY oggetto_modulo ASC, id_versione_modulo DESC";
	
	public void initCache();
	
	public ModuloEntity findById(Long id) throws ItemNotFoundDAOException, DAOException;
	public ModuloEntity findByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException;

	public List<ModuloEntity> getElencoModuli() throws DAOException;
	public List<ModuloVersionatoEntity> find(ModuliFilter filter) throws DAOException;
	public List<ModuloVersionatoEntity> find(ModuliFilter filter, String order) throws DAOException;

	public ModuloCronologiaStatiEntity findCurrentCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;

	public ModuloVersioneEntity findVersioneById(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersioneEntity findVersione(Long idModulo, String versione) throws ItemNotFoundDAOException, DAOException;

	public ModuloVersionatoEntity findModuloVersionatoById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersionatoEntity findModuloVersionatoByCodice(String codiceModulo, String versione) throws ItemNotFoundDAOException, DAOException;
	public ModuloVersionatoEntity findModuloVersionatoPubblicatoByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException;

	public Long insert(ModuloEntity entity) throws DAOException;
	public Long insertCronologia(ModuloCronologiaStatiEntity entity) throws DAOException;
	public Long insertVersione(ModuloVersioneEntity entity) throws DAOException;
	
	public int update(ModuloEntity entity) throws DAOException;
	public int updateCronologia(ModuloCronologiaStatiEntity entity) throws DAOException;
	public int updateVersione(ModuloVersioneEntity entity) throws DAOException;

	public int delete(Long id) throws DAOException;
	public int deleteCronologia(Long idVersioneModulo) throws DAOException;
	public int deleteVersione(Long idVersioneModulo) throws DAOException;

	public ModuloVersioneEntity findVersionePubblicatoByIdModulo(Long idModulo) throws DAOException;

	public Long findIdProcesso(Long idModulo) throws DAOException;
	public List<ModuloVersioneStato> findVersioniModuloById(Long idModulo, String stato);

}
