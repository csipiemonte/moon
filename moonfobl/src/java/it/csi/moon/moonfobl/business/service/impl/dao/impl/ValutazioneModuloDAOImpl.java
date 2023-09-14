/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.entity.ValutazioneModuloEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.ValutazioneModuloDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle valutazioni utente dei moduli
 * 
 * @see ValutazioneModuloEntity
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
public class ValutazioneModuloDAOImpl extends JdbcTemplateDAO implements ValutazioneModuloDAO {

	private static final String CLASS_NAME = "ValutazioneModuloDAOImpl";

	private static final String SELECT_ = "SELECT id_istanza, id_modulo, id_versione_modulo, data_ins, id_valutazione, valutazione";
	
	private static final String FIND_BY_ID_MODULO = SELECT_ +
			" FROM moon_fo_t_valutazione_modulo" +
			" WHERE id_modulo = :id_modulo";

	private static final String INSERT = "INSERT INTO moon_fo_t_valutazione_modulo(id_istanza, id_modulo, id_versione_modulo, data_ins, id_valutazione, valutazione)" +
			" VALUES (:id_istanza, :id_modulo, :id_versione_modulo, :data_ins, :id_valutazione, :valutazione::json)";


	/**
	 * Ottiene tutte le valutazione per un modulo specifico {@code idModulo}
	 * 
	 * @param {@code idModulo} il modulo usato come filtro delle valutazione. Cannot be {@code null}
	 * 
	 * @return la lista delle valutazione del modulo
	 * 
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<ValutazioneModuloEntity> findByIdModulo(Long idModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			return (List<ValutazioneModuloEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO, params, BeanPropertyRowMapper.newInstance(ValutazioneModuloEntity.class) );
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Inserisci un ruolo nel sistema
	 * 
	 * @param {@code entity} il ruolo da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idRuolo} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public void insert(ValutazioneModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapValutazioneModuloEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO");
		}
	}
	

	private MapSqlParameterSource mapValutazioneModuloEntityParameters(ValutazioneModuloEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_istanza", entity.getIdIstanza());
		params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("id_versione_modulo", entity.getIdVersioneModulo());
		params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
		params.addValue("id_valutazione", entity.getIdValutazione());
		params.addValue("valutazione", mapAsJsonString(entity.getValutazione()), Types.VARCHAR);
		return params;
	}
	
	private String mapAsJsonString(Object o) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.setSerializationInclusion(Include.NON_EMPTY);
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::mapAsJsonString] Errore o: " + o, e);
			throw new DAOException("mapAsJsonString");
		}
	}
	
}
