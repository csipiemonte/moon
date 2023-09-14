/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AreaModuloFilter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle collocazione di un modulo nell'area (un ente)
 * <br>Un modulo potra essere solo su un area di un ente, ma potra essere su piu area di diversi enti.
 * <br>
 * <br>Tabella moon_fo_r_area_modulo
 * <br>PK: idArea,idModulo
 * <br>AK: idModulo,idEnte
 * 
 * @see AreaModuloEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

public interface AreaModuloDAO {
	
	public AreaModuloEntity findByIdAreaModulo(Long idArea, Long idModulo) throws ItemNotFoundDAOException,DAOException;
	@Deprecated
	public AreaModuloEntity findByIdModuloEnte(Long idModulo, Long idEnte) throws ItemNotFoundDAOException,DAOException;
	public AreaModuloEntity findByIdModulo(Long idModulo) throws ItemNotFoundDAOException,DAOException;	
	public AreaModuloEntity findByCdModulo(String codiceModulo) throws ItemNotFoundDAOException,DAOException;	
	
	public List<AreaModuloEntity> find(AreaModuloFilter filter) throws DAOException;
	
}
