/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ext.ExtTaxiEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.BuoniTaxiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;


/**
 * DAO per l'accesso ai buoni Taxi
 * 
 * @author Luca
 *
 */
@Component
public class BuoniTaxiDAOimpl extends JdbcTemplateDAO implements BuoniTaxiDAO  {
	private static final String CLASS_NAME= "BuoniTaxiDAOimpl";

	private static final String SELECT_FIELDS = "SELECT id, codice_fiscale, idfamiglia, nome, cognome, data_nascita, email, cellulare, categoria, codice_buono, codice_fiscale_richiedente, data_ins, data_export";

	private static final  String FIND_BY_CF = SELECT_FIELDS +
			" FROM moon_ext_buono_taxi " +
			" WHERE codice_fiscale=:codice_fiscale";
	private static final  String FIND_BY_CODICE_BUONO = SELECT_FIELDS +
			" FROM moon_ext_buono_taxi " +
			" WHERE codice_buono=:codice_buono";
	private static final String INSERT = "INSERT INTO moon_ext_buono_taxi(id, codice_fiscale, idfamiglia, nome, cognome, data_nascita, email, cellulare, categoria, codice_buono, codice_fiscale_richiedente)" + 
			"VALUES (:id,:codice_fiscale,:idfamiglia,:nome,:cognome,:data_nascita,:email,:cellulare,:categoria,:codice_buono,:codice_fiscale_richiedente)";
	private static final String SEQ_ID = "SELECT nextval('moon_ext_buono_taxi_id_seq')";
	
	private static final String INSERT_REF_ISTANZA = "INSERT INTO moon_ext_buono_taxi_istanza(id_istanza, id_buono) " + 
			"VALUES (:id_istanza,:id_buono)";
	
	private static final  String FIND_BY_ID_ISTANZA = SELECT_FIELDS +
			" FROM moon_ext_buono_taxi b, moon_ext_buono_taxi_istanza i" +
			" WHERE b.id=i.id_buono and id_istanza=:id_istanza";	
	
	private static final  String FIND_ALL = "SELECT codice_fiscale,codice_buono FROM moon_ext_buono_taxi order by data_ins";
	
	@Override
	public ExtTaxiEntity findByCf(String cf) throws DAOException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_fiscale", cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] CF: "+cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] params: "+params);
			//		List<ExtTaxiEntity> lista = (List<ExtTaxiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND, params, BeanPropertyRowMapper.newInstance(MobilityamociEntity.class));
			//		return (ExtTaxiEntity) lista.get(0);
			return (ExtTaxiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CF, params, BeanPropertyRowMapper.newInstance(ExtTaxiEntity.class)  );

		} catch (EmptyResultDataAccessException emptyEx) {
			//LOG.warn("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			return null;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}


	}

	@Override
	public Integer insert(ExtTaxiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer id = getCustomNamedParameterJdbcTemplateImpl().queryForInt(SEQ_ID, new MapSqlParameterSource() );
			entity.setId(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}
	
	@Override
	public Integer insertRefIstanza(Long idIstanza, Integer idBuono) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN idIstanza: "+idIstanza+ " idBuono: "+ idBuono);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_buono" , idBuono);
			
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_REF_ISTANZA, params);
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO RIFERIMENTO ISTANZA");
		}
	}

	@Override
	public List<ExtTaxiEntity> findByCodiceBuono(String codice) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_buono", codice);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceBuono] CodiceBuono: "+codice);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceBuono] params: "+params);
			return (List<ExtTaxiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CODICE_BUONO, params, BeanPropertyRowMapper.newInstance(ExtTaxiEntity.class));
			//return (ExtTaxiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_codice_buono, params, BeanPropertyRowMapper.newInstance(ExtTaxiEntity.class)  );

		} catch (EmptyResultDataAccessException emptyEx) {
			//LOG.warn("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			return null;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}

	}
	
	@Override
	public List<ExtTaxiEntity> findByIdIstanza(Long idIstanza) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			LOG.debug("[" + CLASS_NAME + "::findByIdIstanza] id_istanza: "+idIstanza);
			return (List<ExtTaxiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(ExtTaxiEntity.class));

		} catch (EmptyResultDataAccessException emptyEx) {
			//LOG.warn("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			return null;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}

	}	
	
	@Override
	public List<ExtTaxiEntity> findAll() {
		try {
			
			LOG.debug("[" + CLASS_NAME + "::findAll]  ");
			return (List<ExtTaxiEntity>) getCustomJdbcTemplate().query(FIND_ALL, BeanPropertyRowMapper.newInstance(ExtTaxiEntity.class));

		} catch (EmptyResultDataAccessException emptyEx) {
			//LOG.warn("[" + CLASS_NAME + "::findAllForExport] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			return null;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findAll] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}

	}	
	
	
	private MapSqlParameterSource mapEntityParameters(ExtTaxiEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", entity.getId());
		params.addValue("codice_fiscale" , entity.getCodice_fiscale());
		LOG.info("ATTENZIONE: ENTITY FAMIGLIA: DAO: " + entity.getIdfamiglia());
		params.addValue("idfamiglia", entity.getIdfamiglia());	
		params.addValue("nome", entity.getNome());
		params.addValue("cognome", entity.getCognome());
		params.addValue("data_nascita", entity.getData_nascita());
		params.addValue("email", entity.getEmail());
		params.addValue("cellulare", entity.getCellulare());
		params.addValue("categoria", entity.getCategoria());
		params.addValue("codice_buono", entity.getCodice_buono());
		params.addValue("codice_fiscale_richiedente", entity.getCodice_fiscale_richiedente());

		return params;
	}
	

}
