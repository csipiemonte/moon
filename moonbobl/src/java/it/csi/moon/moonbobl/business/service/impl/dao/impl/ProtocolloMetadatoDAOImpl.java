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

import it.csi.moon.moonbobl.business.service.impl.dao.ProtocolloMetadatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ProtocolloMetadatoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai metadati del protocollo
 * <br>
 * <br>Tabella principale : moon_pr_d_metadato
 * 
 * @see ProtocolloMetadatoEntity
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 12/05/2022 - versione iniziale
 */
@Component
public class ProtocolloMetadatoDAOImpl extends JdbcTemplateDAO implements ProtocolloMetadatoDAO {
	
	private static final String CLASS_NAME = "ProtocolloMetadatoDAOImpl";
	
	private static final String SELECT_FILEDS_ = "SELECT id_metadato, nome_metadato, default_value, ordine ";
	private static final String FIND_BY_ID = SELECT_FILEDS_ +
			" FROM moon_pr_d_metadato WHERE id_metadato = :id_metadato";
	
	private static final String FIND_ALL = SELECT_FILEDS_
			+ " FROM moon_pr_d_metadato"
			+ " ORDER BY ordine";
	
	private static final  String INSERT = "INSERT INTO moon_pr_d_metadato (id_metadato, nome_metadato, default_value, ordine)" + 
			" VALUES (:id_metadato, :nome_metadato, :default_value, :ordine)";

	private static final  String UPDATE = "UPDATE moon_pr_d_metadato" +
			" SET nome_metadato = :nome_metadato, default_value = :default_value, ordine = :ordine" +
			" WHERE id_metadato = :id_metadato";

	private static final String DELETE = "DELETE FROM moon_pr_d_metadato WHERE id_metadato = :id_metadato";
		
	private static final String SEQ_ID = "SELECT nextval('moon_pr_d_metadato_id_metadato_seq')";

	
	@Override
	public List<ProtocolloMetadatoEntity> findAll() throws DAOException {
		List<ProtocolloMetadatoEntity> result = null;
		try {
			result = ( List<ProtocolloMetadatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL,
				BeanPropertyRowMapper.newInstance(ProtocolloMetadatoEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findAll] Errore database: " + e.getMessage(), e);
			throw new DAOException();
		}
	}
	
	@Override
	public ProtocolloMetadatoEntity findById(Long idMetadato) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_metadato", idMetadato);
			return (ProtocolloMetadatoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ProtocolloMetadatoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException();
		}
	}

	@Override
	public Long insert(ProtocolloMetadatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idMetadato = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdMetadato(idMetadato);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapProtocolloMetadatoEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idMetadato;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERT PROTOCOLLO METADATO");
		}
	}

	@Override
	public int update(ProtocolloMetadatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapProtocolloMetadatoEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE PROTOCOLLO METADATO");
		}
	}
	

	@Override
	public int delete(Long idMetadato) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idMetadato: "+idMetadato);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_metadato", idMetadato);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE PROTOCOLLO METADATO");
		}
	}
	
    private MapSqlParameterSource mapProtocolloMetadatoEntityParameters(ProtocolloMetadatoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_metadato", entity.getIdMetadato());
    	params.addValue("nome_metadato", entity.getNomeMetadato());
    	params.addValue("default_value", entity.getDefaultValue());
    	params.addValue("ordine", entity.getOrdine());
    	
    	return params;
    }


}
