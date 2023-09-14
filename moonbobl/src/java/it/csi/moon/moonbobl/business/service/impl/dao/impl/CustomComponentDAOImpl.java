/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.CustomComponentDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.CustomComponentEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.FunzioneEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 *  DAO per l'accesso alle componenti custom formio
 * 
 * @see CustomComponentEntity
 * 
 * @author Danilo
 *
 */
@Component
public class CustomComponentDAOImpl extends JdbcTemplateDAO implements CustomComponentDAO {

	private static final String CLASS_NAME = "CustomComponentDAOImpl";
	
	private static final  String FIND_BY_ID  = "SELECT id_component, json_component from moon_bo_t_formio_custom_components where id_component = :id_component";
	
	private static final  String FIND = "SELECT id_component, json_component from moon_bo_t_formio_custom_components";
	
	/**
	 * Ottiene il custom component identificato per {@code idUtente}
	 * 
	 * @param {@code idComponet} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return custom component
	 * 
	 * @throws ItemNotFoundDAOException custom component non trovato.
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public CustomComponentEntity findById(String idComponent) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_component", idComponent);
			return (CustomComponentEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(CustomComponentEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public List<CustomComponentEntity> find() throws DAOException {
		return (List<CustomComponentEntity>)getCustomJdbcTemplate().query(FIND, BeanPropertyRowMapper.newInstance(CustomComponentEntity.class));
	}
	
	
}
