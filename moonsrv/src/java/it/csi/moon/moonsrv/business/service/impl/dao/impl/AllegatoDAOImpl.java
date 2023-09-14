/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import it.csi.moon.commons.dto.AllegatiSummary;
import it.csi.moon.commons.dto.CampoModuloFormioFileName;
import it.csi.moon.commons.dto.api.FruitoreAllegato;
import it.csi.moon.commons.entity.AllegatoEntity;
import it.csi.moon.commons.entity.AllegatoLazyEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class AllegatoDAOImpl extends JdbcTemplateDAO implements AllegatoDAO {

	private static final String CLASS_NAME = "AllegatoDAOImpl";

	private static final String FROM_MOON_FO_T_ALLEGATI_ISTANZA = " FROM moon_fo_t_allegati_istanza";
	private static final String WHERE_ID_ALLEGATO_ID_ALLEGATO = " WHERE id_allegato = :id_allegato";
	private static final String WHERE_ID_ISTANZA_ID_ISTANZA = " WHERE id_istanza = :id_istanza";
	private static final String ID_ALLEGATO = "id_allegato";
	private static final String FORMIO_NAME_FILE = "formio_name_file";
	private static final String CODICE_FILE = "codice_file";
	private static final String ID_ISTANZA = "id_istanza";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_FULL_FIELDS = "SELECT id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza,contenuto," +
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,id_istanza,key,full_key,label, fl_firmato" + 
			" FROM moon_fo_t_allegati_istanza ";
	private static final String SELECT_LAZY_FIELDS = "SELECT id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza," +
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,id_istanza,key,full_key,label, fl_firmato" + 
			" FROM moon_fo_t_allegati_istanza ";
	
	private static final String FIND_BY_ID = SELECT_FULL_FIELDS +
			WHERE_ID_ALLEGATO_ID_ALLEGATO;
	private static final String FIND_BY_CD = SELECT_FULL_FIELDS +
			" WHERE codice_file = :codice_file";
	private static final String FIND_BY_FIO_NANE_FILE = SELECT_FULL_FIELDS +
			" WHERE formio_name_file = :formio_name_file";
	private static final String FIND_BY_ID_ISTANZA_CD = SELECT_FULL_FIELDS + 
			" WHERE codice_file = :codice_file and id_istanza = :id_istanza";
	private static final String FIND_LAZY_BY_ID = SELECT_LAZY_FIELDS +
			WHERE_ID_ALLEGATO_ID_ALLEGATO;
	
	private static final String FIND_SUMMARY = "SELECT count(id_allegato) numero_allegati, sum(lunghezza) dimensione_totale"
			+ FROM_MOON_FO_T_ALLEGATI_ISTANZA
			+ WHERE_ID_ISTANZA_ID_ISTANZA;
	
	private static final String FIND_BY_ID_ISTANZA = SELECT_FULL_FIELDS +
			WHERE_ID_ISTANZA_ID_ISTANZA;
	private static final String FIND_LAZY_BY_ID_ISTANZA = SELECT_LAZY_FIELDS +
			WHERE_ID_ISTANZA_ID_ISTANZA;
	
	private static final String INSERT = "INSERT INTO moon_fo_t_allegati_istanza (" + 
			" id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza,contenuto, " + 
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,key,full_key,label,fl_firmato)" + 
			" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_WITH_COPY_CONTENUTO = "INSERT INTO moon_fo_t_allegati_istanza (" + 
			" id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza,contenuto, " + 
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,key,full_key,label,fl_firmato)" + 
			" SELECT ?,?,?,?,?,?,?,?,c.contenuto,?,?,?,?,?,?,?,?,?,?,?,? FROM moon_fo_t_allegati_istanza c WHERE c.id_allegato=?";
	private static final String INSERT_NAMED = "INSERT INTO moon_fo_t_allegati_istanza (" + 
			" id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza,contenuto, " + 
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,key,full_key,label,fl_firmato)" + 
			" VALUES (:id_allegato,:formio_key,:formio_name_file,:codice_file,:hash_file,:nome_file,:lunghezza,:contenuto," + 
			":content_type,:media_type,:sub_media_type,:ip_address,:estensione,:uuid_index,:fl_eliminato,:data_creazione,:key,:full_key,:label,:fl_firmato)";
	
	private static final String UPDATE = "UPDATE moon_fo_t_allegati_istanza" + 
			" SET formio_key=?, formio_name_file=?, formio_dir=?, codice_file=?, hash_file=?, nome_file=?, lunghezza=?, contenuto=?," +
			" content_type=?, media_type=?, sub_media_type=?, ip_address=?, estensione=?, uuid_index=?, fl_eliminato=?, data_creazione=?," + 
			" key=?, full_key=?, label=?, fl_firmato=?" + 
			" WHERE id_allegato=?";
	private static final String UPDATE_NAMED = "UPDATE moon_fo_t_allegati_istanza" + 
			" SET formio_key=:formio_key, formio_name_file=:formio_name_file, formio_dir=:formio_dir, codice_file=:codice_file, hash_file=:hash_file, nome_file=:nome_file, lunghezza=:lunghezza, contenuto=:contenuto," +
			" content_type=:content_type, media_type=:media_type, sub_media_type=:sub_media_type, ip_address=:ip_address, estensione=:estensione, uuid_index=:uuid_index, fl_eliminato=:fl_eliminato, data_creazione=:data_creazione," + 
			" key=:key, full_key=:full_key, label=:label, fl_firmato=fl_firmato" + 
			" WHERE id_allegato=:id_allegato";
	private static final String UPDATE_CONTENUTO = "UPDATE moon_fo_t_allegati_istanza SET contenuto=? WHERE id_allegato=?";
	private static final String DELETE_CONTENUTO = "UPDATE moon_fo_t_allegati_istanza SET contenuto=null WHERE id_allegato=?";
	
	private static final String UPDATE_ID_ISTANZA = "UPDATE moon_fo_t_allegati_istanza SET id_istanza=:id_istanza, key=:key, full_key=:full_key, label=:label WHERE formio_name_file=:formio_name_file";
	private static final String UPDATE_FL_FIRMATO = "UPDATE moon_fo_t_allegati_istanza SET fl_firmato=:fl_firmato WHERE id_allegato=:id_allegato";
	private static final String UPDATE_FL_ELIMINATO = "UPDATE moon_fo_t_allegati_istanza SET fl_eliminato=:fl_eliminato WHERE id_allegato=:id_allegato";
	private static final String UPDATE_FL_ELIMINATO_BY_FORMIONAME = "UPDATE moon_fo_t_allegati_istanza SET fl_eliminato=:fl_eliminato WHERE formio_name_file=:formio_name_file";
	private static final String UPDATE_UUID_INDEX = "UPDATE moon_fo_t_allegati_istanza SET uuid_index=:uuid_index WHERE id_allegato=:id_allegato";

	private static final String RESET_ID_ISTANZA = "UPDATE moon_fo_t_allegati_istanza SET id_istanza=null WHERE id_istanza = :id_istanza";
	
	private static final String DELETE = "DELETE FROM moon_fo_t_allegati_istanza where id_allegato = :id_allegato";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_allegati_istanza_id_allegato_seq')";
	
	@Override
	public AllegatoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ALLEGATO, id);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByFormIoNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByFormIoNameFile] IN formioNameFile: "+formioNameFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(FORMIO_NAME_FILE, formioNameFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_FIO_NANE_FILE, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByFormIoNameFile] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByFormIoNameFile] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByCodice(String codiceFile) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodice] IN codiceFile: "+codiceFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_FILE, codiceFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatiSummary selectSummary(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::selectSummary] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ISTANZA, idIstanza);
			return (AllegatiSummary)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_SUMMARY, params, BeanPropertyRowMapper.newInstance(AllegatiSummary.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::selectSummary] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::selectSummary] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByIdIstanzaCodice(Long idIstanza,String codiceFile) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdIstanzaCodice] IN idIstanza: "+idIstanza);
			LOG.debug("[" + CLASS_NAME + "::findByIdIstanzaCodice] IN codiceFile: "+codiceFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ISTANZA, idIstanza);
			params.addValue(CODICE_FILE, codiceFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA_CD, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanzaCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdIstanzaCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public AllegatoLazyEntity findLazyById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLazyById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ALLEGATO, id);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAZY_BY_ID, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLazyById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLazyById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<AllegatoEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		List<AllegatoEntity> result = null;
		LOG.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: " + idIstanza);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_ISTANZA, idIstanza);
		result = (List<AllegatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(AllegatoEntity.class));
		return result;
	}
	@Override
	public List<AllegatoLazyEntity> findLazyByIdIstanza(Long idIstanza) throws DAOException {
		List<AllegatoLazyEntity> result = null;
		LOG.debug("[" + CLASS_NAME + "::findLazyByIdIstanza] IN idIstanza: " + idIstanza);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_ISTANZA, idIstanza);
		result = (List<AllegatoLazyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_LAZY_BY_ID_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(AllegatoLazyEntity.class));
		return result;
	}
	
	@Override
	public List<FruitoreAllegato> findByIdIstanzaForApi(Long idIstanza) throws DAOException {
		List<FruitoreAllegato> result = null;
		LOG.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: " + idIstanza);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_ISTANZA, idIstanza);
		result = (List<FruitoreAllegato>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(FruitoreAllegato.class));
		return result;
	}	
	
	@Override
	public Long insert(AllegatoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAllegato(id);

			if (LOG.isDebugEnabled()) {
				LOG.debug("================================================");
				LOG.debug(INSERT);
				LOG.debug("entity: "+entity);
			}
			
			LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.update(INSERT,
					new SqlParameterValue(Types.BIGINT, entity.getIdAllegato()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioNameFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioDir()),
					new SqlParameterValue(Types.VARCHAR, entity.getCodiceFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getHashFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getNomeFile()),
					new SqlParameterValue(Types.INTEGER, entity.getLunghezza()),
		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
		    		new SqlParameterValue(Types.VARCHAR, entity.getContentType()),
		    		new SqlParameterValue(Types.VARCHAR, entity.getMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getSubMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getIpAddress()),
					new SqlParameterValue(Types.VARCHAR, entity.getEstensione()),
					new SqlParameterValue(Types.VARCHAR, entity.getUuidIndex()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlEliminato()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataCreazione()),
					new SqlParameterValue(Types.VARCHAR, entity.getKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFullKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getLabel()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlFirmato())
					);
			
			return entity.getIdAllegato();

			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ALLEGATO");
		}
	}

	@Override
	public int delete(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ALLEGATO, id);
			//return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(findModuloByID, params, new ModuloRowMapper());
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int update(AllegatoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			LOG.debug("================================================");
			LOG.debug(UPDATE);
						  
			LobHandler lobHandler = new DefaultLobHandler();
			int numRecord = jdbcTemplate.update(UPDATE,
					new SqlParameterValue(Types.VARCHAR, entity.getFormioKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioNameFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioDir()),
					new SqlParameterValue(Types.VARCHAR, entity.getCodiceFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getHashFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getNomeFile()),
					new SqlParameterValue(Types.INTEGER, entity.getLunghezza()),
		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
		    		new SqlParameterValue(Types.VARCHAR, entity.getContentType()),
		    		new SqlParameterValue(Types.VARCHAR, entity.getMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getSubMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getIpAddress()),
					new SqlParameterValue(Types.VARCHAR, entity.getEstensione()),
					new SqlParameterValue(Types.VARCHAR, entity.getUuidIndex()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlEliminato()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataCreazione()),
					new SqlParameterValue(Types.VARCHAR, entity.getKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFullKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getLabel()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlFirmato()),
					new SqlParameterValue(Types.BIGINT, entity.getIdAllegato())
					);

			return numRecord;
			
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ALLEGATO");
		}
	}

	
	@Override
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateIdIstanza] IN campoFormioNomeFile: "+campoFormioNomeFile);
				LOG.debug("[" + CLASS_NAME + "::updateIdIstanza] IN id istanza: "+idIstanza);
			}
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ISTANZA, mapCampoFormioIdIstanzaParameters(campoFormioNomeFile,idIstanza));
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateIdIstanza] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateIdIstanza] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA ALLEGATO");
		}
	}
	
	@Override
	public int updateFlFirmato(AllegatoLazyEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateFlFirmato2] IN entity: "+entity);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_firmato", entity.getFlFirmato(), Types.VARCHAR);
			params.addValue(ID_ALLEGATO, entity.getIdAllegato(), Types.BIGINT);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_FIRMATO, params);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateFlFirmato2] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateFlFirmato2] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_FIRMARO ALLEGATO_LAZY");
		}
	}
	
	@Override
	public int updateFlEliminato(AllegatoEntity entity) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateFlEliminato] IN entity: "+entity);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_eliminato", entity.getFlEliminato(), Types.VARCHAR);
			params.addValue(ID_ALLEGATO, entity.getIdAllegato(), Types.BIGINT);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_ELIMINATO, params);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::updateFlEliminato] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateFlEliminato] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_ELIMINATO ALLEGATO");
		}
	}

	@Override
	public int resetIdIstanza(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::resetIdIstanza] IN id istanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_ISTANZA , idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(RESET_ID_ISTANZA, params);
			LOG.debug("[" + CLASS_NAME + "::resetIdIstanza] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::resetIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE RESET ID ISTANZA ALLEGATO");
		}
	}
	
    
    private MapSqlParameterSource mapEntityParameters(AllegatoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue(ID_ALLEGATO, entity.getIdAllegato(), Types.BIGINT);
    	params.addValue("formio_key" , entity.getFormioKey(), Types.VARCHAR);
    	params.addValue(FORMIO_NAME_FILE, entity.getFormioNameFile(), Types.VARCHAR);
    	params.addValue("formio_dir", entity.getFormioDir(), Types.VARCHAR);
    	params.addValue(CODICE_FILE, entity.getCodiceFile(), Types.VARCHAR);
    	params.addValue("hash_file", entity.getHashFile(), Types.VARCHAR);
    	params.addValue("nome_file", entity.getNomeFile(), Types.VARCHAR);
    	params.addValue("lunghezza", entity.getLunghezza(), Types.INTEGER);
    	params.addValue("contenuto", entity.getContenuto(), Types.BLOB);
    	params.addValue("content_type", entity.getContentType(), Types.VARCHAR);
    	params.addValue("media_type", entity.getMediaType(), Types.VARCHAR);
    	params.addValue("sub_media_type", entity.getSubMediaType(), Types.VARCHAR);
    	params.addValue("ip_address", entity.getIpAddress(), Types.VARCHAR);
    	params.addValue("estensione", entity.getEstensione(), Types.VARCHAR);
    	params.addValue("uuid_index", entity.getUuidIndex(), Types.VARCHAR);
    	params.addValue("fl_eliminato", entity.getFlEliminato(), Types.VARCHAR);
    	params.addValue("data_creazione", entity.getDataCreazione(), Types.TIMESTAMP);
    	params.addValue("key", entity.getKey(), Types.VARCHAR);
    	params.addValue("full_key", entity.getFullKey(), Types.VARCHAR);
    	params.addValue("label", entity.getLabel(), Types.VARCHAR);
    	params.addValue("fl_firmato", entity.getFlFirmato(), Types.VARCHAR);
    	return params;
    }
    
//	id_istanza,formio_name_file
	private MapSqlParameterSource mapCampoFormioIdIstanzaParameters(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(FORMIO_NAME_FILE, campoFormioNomeFile.getFormIoFileName(), Types.VARCHAR);
		params.addValue("key", StringUtils.left(campoFormioNomeFile.getKey(),255), Types.VARCHAR);
		params.addValue("full_key", StringUtils.left(campoFormioNomeFile.getFullKey(),255), Types.VARCHAR);
		params.addValue("label", StringUtils.left(campoFormioNomeFile.getLabel(),255), Types.VARCHAR);
		params.addValue(ID_ISTANZA, idIstanza, Types.BIGINT);
		return params;
	}

	@Override
	public int deleteAllegatoByNameFormio(String nomeFile) throws DAOException {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteAllegatoByNameFormio] IN nomeFile: "+nomeFile);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_eliminato", "S", Types.VARCHAR);
			params.addValue(FORMIO_NAME_FILE, nomeFile, Types.VARCHAR);
			
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_ELIMINATO_BY_FORMIONAME, params);
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::deleteAllegatoByNameFormio] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateFlEliminatoByFormioName] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_ELIMINATO ALLEGATO");
		}
	}

	/**
	 * Usato per la duplicazione di un istanza con i suoi allegati
	 * entity : entity pricipale da inserire (escluso di contenuto)
	 * idAllegatoOfContenuto : idAllegato da dove recuperare il contenuto
	 */
	@Override
	public Long insertWithCopyContent(AllegatoLazyEntity entity, Long idAllegatoOfContenuto) throws DAOException {
		try {
			assert idAllegatoOfContenuto!=null;
			LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] IN entity: " + entity);
			LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] IN idAllegatoOfContenuto: " + idAllegatoOfContenuto);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAllegato(id);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] ================================================");
				LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] " + INSERT_WITH_COPY_CONTENUTO);
				LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] entity: " + entity);
				LOG.debug("[" + CLASS_NAME + "::insertWithCopyContent] entity: " + entity);
			}
			
			LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.update(INSERT_WITH_COPY_CONTENUTO,
					new SqlParameterValue(Types.BIGINT, entity.getIdAllegato()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioNameFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getFormioDir()),
					new SqlParameterValue(Types.VARCHAR, entity.getCodiceFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getHashFile()),
					new SqlParameterValue(Types.VARCHAR, entity.getNomeFile()),
					new SqlParameterValue(Types.INTEGER, entity.getLunghezza()),
//		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
		    		new SqlParameterValue(Types.VARCHAR, entity.getContentType()),
		    		new SqlParameterValue(Types.VARCHAR, entity.getMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getSubMediaType()),
					new SqlParameterValue(Types.VARCHAR, entity.getIpAddress()),
					new SqlParameterValue(Types.VARCHAR, entity.getEstensione()),
					new SqlParameterValue(Types.VARCHAR, entity.getUuidIndex()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlEliminato()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataCreazione()),
					new SqlParameterValue(Types.VARCHAR, entity.getKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getFullKey()),
					new SqlParameterValue(Types.VARCHAR, entity.getLabel()),
					new SqlParameterValue(Types.VARCHAR, entity.getFlFirmato()),
					new SqlParameterValue(Types.BIGINT, idAllegatoOfContenuto)
					);
			
			return entity.getIdAllegato(); 

			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertWithCopyContent] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO COPIA ALLEGATO WITH COPY CONTENT");
		}
	}

	@Override
	public int updateContenuto(Long newIdAllegato, byte[] bytes) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateContenuto] IN newIdAllegato: " + newIdAllegato);
			LobHandler lobHandler = new DefaultLobHandler();
			int numRecord = jdbcTemplate.update(UPDATE_CONTENUTO,
		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(bytes, lobHandler)),
					new SqlParameterValue(Types.BIGINT, newIdAllegato)
					);
			return numRecord;
			
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateContenuto] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ALLEGATO");
		}
	}

	@Override
	public int updateUuidIndex(AllegatoLazyEntity allegato) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateUuidIndex] IN allegato: "+allegato.getIdAllegato());
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("uuid_index", allegato.getUuidIndex());
	    	params.addValue(ID_ALLEGATO, allegato.getIdAllegato());
	    	
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_UUID_INDEX, params);
			LOG.debug("[" + CLASS_NAME + "::updateUuidIndex] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateUuidIndex] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO UUID_INDEX ALLEGATO");
		}
	}

	@Override
	public int deleteContenuto(Long idAllegato) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteContenuto] IN idAllegato: " + idAllegato);
			int numRecord = jdbcTemplate.update(DELETE_CONTENUTO,
					new SqlParameterValue(Types.BIGINT, idAllegato)
					);
			return numRecord;
			
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteContenuto] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE CONTENUTO ALLEGATO");
		}
	}
	
}
