/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.AllegatoMessaggioSupportoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoMessaggioSupportoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class AllegatoMessaggioSupportoDAOImpl extends JdbcTemplateDAO implements AllegatoMessaggioSupportoDAO {
	
	private final static String CLASS_NAME = "AllegatoDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final  String SELECT_FIELDS = "SELECT id_allegato_messaggio_supporto, id_messaggio_supporto, nome, dimensione, contenuto, content_type" +
			" FROM moon_fo_t_allegato_messaggio_supporto ";
	
	private static final  String FIND_BY_ID = SELECT_FIELDS +
			" WHERE id_allegato_messaggio_supporto = :id_allegato_messaggio_supporto";
	
	private static final  String FIND_BY_ID_MESSAGGIO = SELECT_FIELDS +
			" WHERE id_messaggio_supporto = :id_messaggio_supporto";
	
	private static final String INSERT = "INSERT INTO moon_fo_t_allegato_messaggio_supporto (" + 
			" id_allegato_messaggio_supporto, id_messaggio_supporto, nome, dimensione, contenuto, content_type)" + 
			" VALUES (?,?,?,?,?,?)";
	private static final String INSERT_NAMED = "INSERT INTO moon_fo_t_allegato_messaggio_supporto (" + 
			" id_allegato_messaggio_supporto, id_messaggio_supporto, nome, dimensione, contenuto, content_type)" + 
			" VALUES (:id_allegato_messaggio_supporto,:id_messaggio_supporto,:nome,:dimensione,:contenuto,:content_type)";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_allegato_messaggio__id_allegato_messaggio_support_seq')";
	
	@Override
	public AllegatoMessaggioSupportoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_allegato", id);
			return (AllegatoMessaggioSupportoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AllegatoMessaggioSupportoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<AllegatoMessaggioSupportoEntity> findByIdMessaggioSupporto(Long idMessaggioSupporto) throws DAOException {
		
		List<AllegatoMessaggioSupportoEntity> result = null;
		log.debug("[" + CLASS_NAME + "::findByIdMessaggioSupporto] IN idMessaggioSupporto: " + idMessaggioSupporto);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_messaggio_supporto", idMessaggioSupporto);
		
		log.debug("[" + CLASS_NAME + "::findByIdMessaggioSupporto] params: " + params);
		result = (List<AllegatoMessaggioSupportoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MESSAGGIO, params,
				BeanPropertyRowMapper.newInstance(AllegatoMessaggioSupportoEntity.class));

		return result;
	}	
	
	@Override
	public Long insert(AllegatoMessaggioSupportoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAllegatoMessaggioSupporto(id);

			log.debug(INSERT);
			log.debug("[" + CLASS_NAME + "::insert] entity: "+entity);
			  
			LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.update(INSERT,
					new SqlParameterValue(Types.BIGINT, entity.getIdAllegatoMessaggioSupporto()),
					new SqlParameterValue(Types.BIGINT, entity.getIdMessaggioSupporto()),
					new SqlParameterValue(Types.VARCHAR, entity.getNome()),
					new SqlParameterValue(Types.INTEGER, entity.getDimensione()),
					new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
					new SqlParameterValue(Types.VARCHAR, entity.getContentType())
					);
			
			return entity.getIdAllegatoMessaggioSupporto();
			  
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ALLEGATO");
		}
	}
    
}
