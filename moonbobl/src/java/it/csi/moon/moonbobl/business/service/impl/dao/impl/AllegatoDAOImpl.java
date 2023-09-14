/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

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

import it.csi.moon.moonbobl.business.service.impl.dao.AllegatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.AllegatoLazyEntity;
import it.csi.moon.moonbobl.dto.moonfobl.AllegatiSummary;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModuloFormioFileName;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class AllegatoDAOImpl extends JdbcTemplateDAO implements AllegatoDAO {
	
	private static final String CLASS_NAME = "AllegatoDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_FULL_FIELDS = "SELECT id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza,contenuto," +
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,id_istanza,key,full_key,label, fl_firmato" + 
			" FROM moon_fo_t_allegati_istanza ";
	private static final String SELECT_LAZY_FIELDS = "SELECT id_allegato,formio_key,formio_name_file,formio_dir,codice_file,hash_file,nome_file,lunghezza," +
			" content_type,media_type,sub_media_type,ip_address,estensione,uuid_index,fl_eliminato,data_creazione,id_istanza,key,full_key,label, fl_firmato" + 
			" FROM moon_fo_t_allegati_istanza ";
	
	private static final String FIND_BY_ID = SELECT_FULL_FIELDS +
			" WHERE id_allegato = :id_allegato";
	private static final String FIND_BY_CD = SELECT_FULL_FIELDS +
			" WHERE codice_file = :codice_file";
	private static final String FIND_BY_FIO_NANE_FILE = SELECT_FULL_FIELDS +
			" WHERE formio_name_file = :formio_name_file";
	private static final String FIND_BY_ID_ISTANZA_CD = SELECT_FULL_FIELDS + 
			" WHERE codice_file = :codice_file and id_istanza = :id_istanza";

	private static final String FIND_SUMMARY = "SELECT count(id_allegato) numero_allegati, sum(lunghezza) dimensione_totale"
			+ " FROM moon_fo_t_allegati_istanza"
			+ " WHERE id_istanza = :id_istanza";
	
	private static final String FIND_BY_ID_ISTANZA = SELECT_FULL_FIELDS +
			" WHERE id_istanza = :id_istanza";
	private static final String FIND_LAZY_BY_ID_ISTANZA = SELECT_LAZY_FIELDS +
			" WHERE id_istanza = :id_istanza";
	
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
	
	private static final String UPDATE_ID_ISTANZA = "UPDATE moon_fo_t_allegati_istanza SET id_istanza=:id_istanza, key=:key, full_key=:full_key, label=:label WHERE formio_name_file=:formio_name_file";
	private static final String UPDATE_FL_FIRMATO = "UPDATE moon_fo_t_allegati_istanza SET fl_firmato=:fl_firmato WHERE id_allegato=:id_allegato";
	private static final String UPDATE_FL_ELIMINATO = "UPDATE moon_fo_t_allegati_istanza SET fl_eliminato=:fl_eliminato WHERE id_allegato=:id_allegato";
	private static final String UPDATE_FL_ELIMINATO_BY_FORMIONAME = "UPDATE moon_fo_t_allegati_istanza SET fl_eliminato=:fl_eliminato WHERE formio_name_file=:formio_name_file";

	private static final String RESET_ID_ISTANZA = "UPDATE moon_fo_t_allegati_istanza SET id_istanza=null WHERE id_istanza = :id_istanza";
	
	private static final String DELETE = "DELETE FROM moon_fo_t_allegati_istanza where id_allegato = :id_allegato";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_allegati_istanza_id_allegato_seq')";
	
	@Override
	public AllegatoEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_allegato", id);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByFormIoNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByFormIoNameFile] IN formioNameFile: "+formioNameFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("formio_name_file", formioNameFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_FIO_NANE_FILE, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByFormIoNameFile] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByFormIoNameFile] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByCodice(String codiceFile) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByCodice] IN codiceFile: "+codiceFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_file", codiceFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatiSummary selectSummary(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::selectSummary] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (AllegatiSummary)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_SUMMARY, params, BeanPropertyRowMapper.newInstance(AllegatiSummary.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::selectSummary] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::selectSummary] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public AllegatoEntity findByIdIstanzaCodice(Long idIstanza,String codiceFile) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByIdIstanzaCodice] IN idIstanza: "+idIstanza);
			log.debug("[" + CLASS_NAME + "::findByIdIstanzaCodice] IN codiceFile: "+codiceFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("codice_file", codiceFile);
			return (AllegatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA_CD, params, BeanPropertyRowMapper.newInstance(AllegatoEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdIstanzaCodice] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanzaCodice] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Deprecated
	@Override
	public List<AllegatoEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		List<AllegatoEntity> result = null;
		log.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: " + idIstanza);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_istanza", idIstanza);
		
		log.debug("[" + CLASS_NAME + "::findByIdIstanza] params: " + params);
		result = (List<AllegatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(AllegatoEntity.class));

		return result;
	}
	@Override
	public List<AllegatoLazyEntity> findLazyByIdIstanza(Long idIstanza) throws DAOException {
		List<AllegatoLazyEntity> result = null;
		log.debug("[" + CLASS_NAME + "::findLazyByIdIstanza] IN idIstanza: " + idIstanza);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_istanza", idIstanza);
		
		log.debug("[" + CLASS_NAME + "::findLazyByIdIstanza] params: " + params);
		result = (List<AllegatoLazyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_LAZY_BY_ID_ISTANZA, params,
				BeanPropertyRowMapper.newInstance(AllegatoLazyEntity.class));

		return result;
	}

	@Override
	public Long insert(AllegatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAllegato(id);
			
			if (log.isDebugEnabled()) {
			  log.debug("================================================");
			  log.debug(INSERT);
			  log.debug("entity: "+entity);
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
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ALLEGATO");
		}
	}

	@Override
	public int delete(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_allegato", id);
			//return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(findModuloByID, params, new ModuloRowMapper());
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int update(AllegatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			log.debug("================================================");
			log.debug(UPDATE);
						  
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
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ALLEGATO");
		}
	}

	
	@Override
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN campoFormioNomeFile: "+campoFormioNomeFile);
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN id istanza: "+idIstanza);
			}
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ISTANZA, mapCampoFormioIdIstanzaParameters(campoFormioNomeFile,idIstanza));
			if (log.isDebugEnabled()) {
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdIstanza] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA ALLEGATO");
		}
	}
	
	@Override
	public int updateFlFirmato(AllegatoLazyEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlFirmato] IN entity: "+entity);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_firmato", entity.getFlFirmato(), Types.VARCHAR);
			params.addValue("id_allegato", entity.getIdAllegato(), Types.BIGINT);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_FIRMATO, params);
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlFirmato] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateFlFirmato] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_FIRMARO ALLEGATO");
		}
	}

	@Override
	public int updateFlEliminato(AllegatoEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlEliminato] IN entity: "+entity);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_eliminato", entity.getFlEliminato(), Types.VARCHAR);
			params.addValue("id_allegato", entity.getIdAllegato(), Types.BIGINT);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_ELIMINATO, params);
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlEliminato] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateFlEliminato] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_ELIMINATO ALLEGATO");
		}
	}

	@Override
	public int resetIdIstanza(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::resetIdIstanza] IN id istanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza" , idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(RESET_ID_ISTANZA, params);
			log.debug("[" + CLASS_NAME + "::resetIdIstanza] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::resetIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE RESET ID ISTANZA ALLEGATO");
		}
	}
	
    
    private MapSqlParameterSource mapEntityParameters(AllegatoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_allegato", entity.getIdAllegato(), Types.BIGINT);
    	params.addValue("formio_key" , entity.getFormioKey(), Types.VARCHAR);
    	params.addValue("formio_name_file", entity.getFormioNameFile(), Types.VARCHAR);
    	params.addValue("formio_dir", entity.getFormioDir(), Types.VARCHAR);
    	params.addValue("codice_file", entity.getCodiceFile(), Types.VARCHAR);
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
    	params.addValue("uuid_index", entity.getFlEliminato(), Types.VARCHAR);
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
		params.addValue("formio_name_file", campoFormioNomeFile.getFormIoFileName(), Types.VARCHAR);
		params.addValue("key", StringUtils.left(campoFormioNomeFile.getKey(),255), Types.VARCHAR);
		params.addValue("full_key", StringUtils.left(campoFormioNomeFile.getFullKey(),255), Types.VARCHAR);
		params.addValue("label", StringUtils.left(campoFormioNomeFile.getLabel(),255), Types.VARCHAR);
		params.addValue("id_istanza", idIstanza, Types.BIGINT);
		return params;
	}

	@Override
	public int deleteAllegatoByNameFormio(String nomeFile) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::deleteAllegatoByNameFormio] IN nomeFile: "+nomeFile);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_eliminato", "S", Types.VARCHAR);
			params.addValue("formio_name_file", nomeFile, Types.VARCHAR);
			
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_ELIMINATO_BY_FORMIONAME, params);
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlEliminatoByFormioName] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateFlEliminatoByFormioName] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_ELIMINATO ALLEGATO");
		}
	}

	/**
	 * Usato per la duplicazione di un istanza con i suoi allegati
	 * entity : entity pricipale da inserire (escluso di contenuto)
	 * idAllegatoOfContenuto : idAllegato da dove recuperare il contenuto
	 */
	@Override
	public Long insert(AllegatoLazyEntity entity, Long idAllegatoOfContenuto) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert2] IN entity: " + entity);
			log.debug("[" + CLASS_NAME + "::insert2] IN idAllegatoOfContenuto: " + idAllegatoOfContenuto);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAllegato(id);
			
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::insert2] ================================================");
				log.debug("[" + CLASS_NAME + "::insert2] " + INSERT_WITH_COPY_CONTENUTO);
				log.debug("[" + CLASS_NAME + "::insert2] entity: " + entity);
				log.debug("[" + CLASS_NAME + "::insert2] entity: " + entity);
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
			log.error("[" + CLASS_NAME + "::insert2] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO COPIA ALLEGATO");
		}
	}

	@Override
	public int updateContenuto(Long newIdAllegato, byte[] bytes) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateContenuto] IN newIdAllegato: " + newIdAllegato);
			LobHandler lobHandler = new DefaultLobHandler();
			int numRecord = jdbcTemplate.update(UPDATE_CONTENUTO,
		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(bytes, lobHandler)),
					new SqlParameterValue(Types.BIGINT, newIdAllegato)
					);
			return numRecord;
			
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateContenuto] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ALLEGATO");
		}
	}
	
}