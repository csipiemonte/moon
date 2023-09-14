/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileLazyEntity;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModuloFormioFileName;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class RepositoryFileDAOImpl extends JdbcTemplateDAO implements RepositoryFileDAO {
	
	private static final String CLASS_NAME = "RepositoryFileDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_ = "SELECT id_file,nome_file,contenuto,fl_eliminato,data_creazione,id_istanza, "
			+ "formio_key,formio_name_file,codice_file,hash_file,content_type,id_tipologia,codice_file,id_storico_workflow,lunghezza,descrizione,fl_firmato,tipologia_fruitore,ref_url,numero_protocollo,data_protocollo, "
			+ "uuid_index, key,full_key,label, id_tipologia_mydocs";
	private static final String SELECT_F_ = "SELECT f.id_file,f.nome_file,f.contenuto,f.fl_eliminato,f.data_creazione,f.id_istanza, "
			+ "f.formio_key,f.formio_name_file,f.codice_file,f.hash_file,f.content_type,f.id_tipologia,f.codice_file,f.id_storico_workflow,f.lunghezza,f.descrizione,f.fl_firmato,f.tipologia_fruitore,f.ref_url,f.numero_protocollo,f.data_protocollo,"
			+ "f.uuid_index,f.key,f.full_key,f.label, f.id_tipologia_mydocs";
	private static final String SELECT_FROM = SELECT_ +
			" FROM moon_fo_t_repository_file ";
	
	private static final String FIND_BY_ID = SELECT_FROM +
			" WHERE id_file = :id_file";
	private static final String FIND_BY_FORMIO = SELECT_FROM +
			" WHERE formio_name_file = :formio_name_file";
	
	private static final String FIND_BY_ISTANZA_ID_ST_NOME_FILE = SELECT_FROM +
			" WHERE nome_file = :nome_file" +
			" AND id_istanza = :id_istanza" +
			" AND id_storico_workflow = :id_storico_workflow";
	
	private static final String FIND_BY_ID_ISTANZA_STORICO_WF = SELECT_FROM +
			" WHERE id_istanza = :id_istanza" +
			" AND id_storico_workflow = :id_storico_workflow" +
			" ORDER BY data_creazione ASC";
	
	private static final String FIND_BY_ID_ISTANZA_STORICO_TIPO = SELECT_FROM +
			" WHERE id_istanza = :id_istanza" +
			" AND id_storico_workflow = :id_storico_workflow" +
			" AND id_tipologia = :id_tipologia" +
			" ORDER BY data_creazione ASC";
	
	private static final String FIND_BY_ID_ISTANZA_TIPOLOGIA = SELECT_FROM +
			" WHERE id_istanza = :id_istanza" +
			" AND id_tipologia = :id_tipologia" +
			" ORDER BY data_creazione ASC";
	
	private static final String FIND_BY_ID_ISTANZA = SELECT_FROM +
			" WHERE id_istanza = :id_istanza";
	
	private static final String FIND_BY_ID_NOTIFICA = SELECT_F_ +
			" FROM moon_fo_t_repository_file f, moon_fo_r_allegato_notifica n" +
			" WHERE f.id_file = n.id_file AND n.id_notifica = :id_notifica";

	private static final String FIND_PROTOCOLLATI_BY_ID_ISTANZA = SELECT_FROM +
			" WHERE id_istanza = :id_istanza and numero_protocollo is not null";
	
	private static final String INSERT = "INSERT INTO moon_fo_t_repository_file "
			+ " (id_file,nome_file,contenuto,fl_eliminato,data_creazione,"
			+ "id_istanza,formio_key,formio_name_file,hash_file,"
			+ "content_type,id_tipologia,codice_file,id_storico_workflow,fl_firmato,tipologia_fruitore,"
			+ "ref_url,descrizione,lunghezza,numero_protocollo,data_protocollo,"
			+ "uuid_index,\"key\",full_key,\"label\",id_tipologia_mydocs)"
			+ " VALUES (?,?,?,?,?,"
			+ "?,?,?,?,"
			+ "?,?,?,?,?,?,"
			+ "?,?,?,?,?,"
			+ "?,?,?,?,?)";
	
	private static final String UPDATE = "UPDATE moon_fo_t_repository_file" +
			" SET nome_file=?, contenuto=?, fl_eliminato=?, data_creazione=?, id_istanza=?, id_tipologia=?, fl_firmato=?" +
			" WHERE id_file=?";
	
	private static final String UPDATE_ID_ISTANZA = "UPDATE moon_fo_t_repository_file SET id_istanza=:id_istanza WHERE id_file=:id_file";
	private static final String UPDATE_FL_FIRMATO = "UPDATE moon_fo_t_repository_file SET fl_firmato=:fl_firmato WHERE id_file=:id_file";
	private static final String UPDATE_ID_ISTANZA_KFKL_BY_FORMIO = "UPDATE moon_fo_t_repository_file" +
			" SET id_istanza=:id_istanza, key=:key, full_key=:full_key, label=:label" +
			" WHERE formio_name_file=:formio_name_file";
	
	private static final String DELETE = "DELETE FROM moon_fo_t_repository_file WHERE id_file = :id_file";
	private static final String UPDATE_ELIMINA = "UPDATE moon_fo_t_repository_file SET fl_eliminato='S' WHERE id_file=:id_file";
	
	private static final String UPDATE_ID_ST_WF_ID_ISTANZA_BY_FORMIO = "UPDATE moon_fo_t_repository_file" +
			" SET id_istanza=:id_istanza, id_storico_workflow=:id_storico_workflow" +
			" WHERE formio_name_file=:formio_name_file AND (id_istanza IS NULL OR id_istanza=:id_istanza)";
	private static final String UPDATE_ID_ST_WF_BY_ID_ISTANZA_FILE = "UPDATE moon_fo_t_repository_file" +
			" SET id_storico_workflow=:id_storico_workflow " +
			" WHERE id_istanza=:id_istanza" +
			" AND id_file=:id_file";
	private static final String UPDATE_PROTOCOLLO = "UPDATE moon_fo_t_repository_file" +
			" SET numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo" +
			" WHERE id_file = :id_file";
	private static final String UPDATE_UUID_INDEX = "UPDATE moon_fo_t_repository_file" +
			" SET uuid_index=:uuid_index" +
			" WHERE id_file=:id_file";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_repository_file_id_file_seq')";
	
	
	@Override
	public RepositoryFileEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_file", id);
			return (RepositoryFileEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public RepositoryFileEntity findByNomeFileIstanzaIdStWf(String nomeFile, Long idIstanza, Long idStoricoWorkflow) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByNomeFileIstanzaIdStWf] IN nomeFile: "+nomeFile);
			log.debug("[" + CLASS_NAME + "::findByNomeFileIstanzaIdStWf] IN idIstanza: "+idIstanza);
			log.debug("[" + CLASS_NAME + "::findByNomeFileIstanzaIdStWf] IN idStoricoWorkflow: "+idStoricoWorkflow);
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("nome_file", nomeFile);
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			return (RepositoryFileEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ISTANZA_ID_ST_NOME_FILE, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByNomeFileIstanzaIdStWf] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByNomeFileIstanzaIdStWf] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public RepositoryFileEntity findByFormioNameFile(String formioNameFile) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByFormioNameFile] IN formioNameFile: "+formioNameFile);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("formio_name_file", formioNameFile);
			return (RepositoryFileEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_FORMIO, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByFormioNameFile] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByFormioNameFile] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	

	//
	// LIST
	//
	@Override
	public List<RepositoryFileEntity> findByFilter(RepositoryFileFilter filter) {
		List<RepositoryFileEntity> result = null;
		try {
			log.debug("[" + CLASS_NAME + "::findByFilter] IN filter: " + filter);
	
			StringBuilder sb = new StringBuilder(SELECT_FROM);
			MapSqlParameterSource params = new MapSqlParameterSource();
	
			sb = readFilter(filter, sb, params);
			
			log.debug("[" + CLASS_NAME + "::findByFilter] params: " + params);
			result = (List<RepositoryFileEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
					BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class));
			
			return result;
		}catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByFilter] Errore database: "+e.getMessage(),e);
			throw new DAOException();
		}
	}

	private StringBuilder readFilter(RepositoryFileFilter filter, StringBuilder sbSelectFrom, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		
		if (filter != null) {
			boolean first = true;
			if(filter.getIdFile().isPresent()) {
				sb.append(whereIfFirstOrAnd(first))
				.append("id_file = :id_file ");
			params.addValue("id_file", filter.getIdFile().get());
			first = false;
			}
			if(filter.getNomeFile().isPresent()) {
				sb.append(whereIfFirstOrAnd(first))
				.append("nome_file = :nome_file ");
			params.addValue("nome_file", filter.getNomeFile().get());
			first = false;
			}
			if (!RepositoryFileFilter.EnumFilterFlagEliminato.TUTTI.equals(filter.getFlagEliminato())) {
				sb.append(whereIfFirstOrAnd(first))
					.append("fl_eliminato = :fl_eliminato ");
				params.addValue("fl_eliminato",
					IstanzeFilter.EnumFilterFlagEliminata.ELIMINATI.equals(filter.getFlagEliminato())?'S':'N');
				first = false;
			}
			if (filter.getTipiFile().isPresent()) {
				sb.append(whereIfFirstOrAnd(first))
				.append(" id_tipologia IN (:tipiFile)");
				params.addValue("tipiFile", filter.getTipiFile().get().stream().mapToInt(d -> d.getId()).boxed().collect(Collectors.toList()));
				first = false;
			}
			if(filter.getIdIstanza().isPresent()) {
				sb.append(whereIfFirstOrAnd(first))
				.append("id_istanza = :id_istanza ");
			params.addValue("id_istanza", filter.getIdIstanza().get());
			first = false;
			}
			if(filter.getIdStoricoWorkflow().isPresent()) {
				sb.append(whereIfFirstOrAnd(first))
				.append("id_storico_workflow = :id_storico_workflow ");
			params.addValue("id_storico_workflow", filter.getIdStoricoWorkflow().get());
			first = false;
			}
		}
		if(sb.length()>0) {
			return sbSelectFrom.append(sb); 
		}
		
		return sbSelectFrom;
	}
	
	private String whereIfFirstOrAnd(boolean first) {
		return first?" WHERE ":" AND ";
		//		return first?"":" AND ";
	}
	
	@Override
	public List<RepositoryFileEntity> findByIdIstanza(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (List<RepositoryFileEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<RepositoryFileEntity> findProtocollatiByIdIstanza(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (List<RepositoryFileEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_PROTOCOLLATI_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findProtocollatiByIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<RepositoryFileLazyEntity> findLazyByIdIstanzaTipologia(Long idIstanza, Integer idTipologia) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::findLazyByIdIstanzaTipologia] IN idIstanza: " + idIstanza);
				log.debug("[" + CLASS_NAME + "::findLazyByIdIstanzaTipologia] IN idTipologia: " + idTipologia);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_tipologia", idTipologia);
			return (List<RepositoryFileLazyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA_TIPOLOGIA, params, BeanPropertyRowMapper.newInstance(RepositoryFileLazyEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findLazyByIdIstanzaTipologia] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findLazyByIdIstanzaTipologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	/**
	 * Restituisce elenco allegati individuati per {@code idNotifica}
	 * @param idNotifica
	 * @return la lista allegati per idNotifica
	 * @throws DAOException se si verificano altri errori.
	 */
	@Override
	public List<RepositoryFileLazyEntity> findLazyByIdNotifica(Long idNotifica) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findLazyByIdNotifica] IN idNotifica="+idNotifica);
			List<RepositoryFileLazyEntity> result = null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_notifica", idNotifica);
			result = (List<RepositoryFileLazyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_NOTIFICA, params, BeanPropertyRowMapper.newInstance(RepositoryFileLazyEntity.class));
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findLazyByIdNotifica] Errore database: "+e.getMessage(),e);
			throw new DAOException();
		}
	}
	
	//
	// LIST LAZY
	//
	@Override
	public List<RepositoryFileLazyEntity> findByIdIstanzaStoricoWf(Long idIstanza, Long idStoricoWorkflow) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::findByIdIstanzaStoricoWf] IN idIstanza: "+idIstanza);
				log.debug("[" + CLASS_NAME + "::findByIdIstanzaStoricoWf] IN idStoricoWorkflow: "+idStoricoWorkflow);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			return (List<RepositoryFileLazyEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ISTANZA_STORICO_WF, params, BeanPropertyRowMapper.newInstance(RepositoryFileLazyEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByIdIstanzaStoricoWf] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanzaStoricoWf] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	//
	//
	@Override
	public Long insert(RepositoryFileEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdFile(id);
			if(entity.getDataCreazione()==null) {
				entity.setDataCreazione(new Date());
			}
			log.debug("================================================");
			log.debug(INSERT);
			log.debug("entity: "+entity);
			LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.update(INSERT,
//				+ " (id_file,nome_file,contenuto,fl_eliminato,data_creazione,"
				new SqlParameterValue(Types.BIGINT, entity.getIdFile()),
				new SqlParameterValue(Types.VARCHAR, entity.getNomeFile()),
	    		new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
				new SqlParameterValue(Types.VARCHAR, entity.getFlEliminato()), // DEFAULT 'N'::bpchar
				new SqlParameterValue(Types.TIMESTAMP, entity.getDataCreazione()),
//				+ "id_istanza,formio_key,formio_name_file,hash_file,"
	    		new SqlParameterValue(Types.BIGINT, entity.getIdIstanza()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getFormioKey()),
				new SqlParameterValue(Types.VARCHAR, entity.getFormioNameFile()),
				new SqlParameterValue(Types.VARCHAR, entity.getHashFile()),
//				+ "content_type,id_tipologia,codice_file,id_storico_workflow,fl_firmato,tipologia_fruitore,"
				new SqlParameterValue(Types.VARCHAR, entity.getContentType()),
	    		new SqlParameterValue(Types.INTEGER, entity.getIdTipologia()),
				new SqlParameterValue(Types.VARCHAR, entity.getCodiceFile()),
	    		new SqlParameterValue(Types.BIGINT, entity.getIdStoricoWorkflow()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getFlFirmato()), // DEFAULT 'N'::bpchar
	    		new SqlParameterValue(Types.VARCHAR, entity.getTipologiaFruitore()),
//	    		+ "ref_url,descrizione,lunghezza,numero_protocollo,data_protocollo,"
	    		new SqlParameterValue(Types.VARCHAR, entity.getRefUrl()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getDescrizione()),
	    		new SqlParameterValue(Types.INTEGER, entity.getLunghezza()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getNumeroProtocollo()),
	    		new SqlParameterValue(Types.TIMESTAMP, entity.getDataProtocollo()),
//	    		+ "uuid_index,\"key\",full_key,\"label\",id_tipologia_mydocs)"
	    		new SqlParameterValue(Types.VARCHAR, entity.getUuidIndex()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getKey()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getFullKey()),
	    		new SqlParameterValue(Types.VARCHAR, entity.getLabel()),
				new SqlParameterValue(Types.INTEGER, entity.getIdTipologiaMydocs())
			);
			return entity.getIdFile();
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO RepositoryFile");
		}
	}
	
	@Override
	public int elimina(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_file", id);
			int numRecord = jdbcTemplate.update(UPDATE_ELIMINA, params);
			return numRecord;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int delete(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_file", id);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int update(RepositoryFileEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			log.debug("================================================");
			log.debug(UPDATE);
			
			LobHandler lobHandler = new DefaultLobHandler();
			int numRecord = jdbcTemplate.update(UPDATE,
					new SqlParameterValue(Types.VARCHAR, entity.getNomeFile()),
		    		new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenuto(), lobHandler)),
					new SqlParameterValue(Types.VARCHAR, entity.getFlEliminato()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataCreazione()),
					new SqlParameterValue(Types.BIGINT, entity.getIdIstanza()),
		    		new SqlParameterValue(Types.INTEGER, entity.getIdTipologia()),
		    		new SqlParameterValue(Types.VARCHAR, entity.getFlFirmato()),
					new SqlParameterValue(Types.BIGINT, entity.getIdFile())
				);
			
			return numRecord;
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ALLEGATO");
		}
	}
	
	@Override
	public int updateIdIstanza(Long idFile, Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN idFile: " + idFile);
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN idIstanza: " + idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_file", idFile);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ISTANZA, params);
			log.debug("[" + CLASS_NAME + "::updateIdIstanza] Record aggiornati: " + numRecord);
			return numRecord;
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE ID_ISTANZA");
		}
	}
	
	@Override
	public int updateFlFirmato(RepositoryFileLazyEntity entity) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlFirmato] IN entity: "+entity);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("fl_firmato", entity.getFlFirmato(), Types.VARCHAR);
	    	params.addValue("id_file", entity.getIdFile());
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_FL_FIRMATO, params);
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateFlFirmato] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateFlFirmato] Errore database: "+e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO FL_FIRMARO REPOSITORY_FILE");
		}
	}
	
	@Override
	public int updateIdStoricoWorkflowIdIstanzaByFormioFileName(Long idIstanza, String formioNomeFile, Long idStoricoWorkflow) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowIdIstanzaByFormioFileName] IN idIstanza: "+idIstanza);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowIdIstanzaByFormioFileName] IN formio_nome_file: "+formioNomeFile);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowIdIstanzaByFormioFileName] IN idStoricoWorkflow: "+idStoricoWorkflow);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			params.addValue("id_istanza" , idIstanza);
			params.addValue("formio_name_file" , formioNomeFile);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ST_WF_ID_ISTANZA_BY_FORMIO, params);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowIdIstanzaByFormioFileName] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdStoricoWorkflowIdIstanzaByFormioFileName] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ID_ST_WF ON REPO_FILE BY FORMIO_FILE_NAME");
		}
	}
	
	@Override
	public int updateIdStoricoWorkflowByIdFile(Long idIstanza, Long idFile, Long idStoricoWorkflow) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowByIdFile] IN idIstanza: "+idIstanza);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowByIdFile] IN idFile: "+idFile);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowByIdFile] IN idStoricoWorkflow: "+idStoricoWorkflow);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			params.addValue("id_istanza" , idIstanza);
			params.addValue("id_file" , idFile);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ST_WF_BY_ID_ISTANZA_FILE, params);
			log.debug("[" + CLASS_NAME + "::updateIdStoricoWorkflowByIdFile] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdStoricoWorkflowByIdFile] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA ALLEGATO");
		}
	}


	@Override
	public int updateIdIstanza(CampoModuloFormioFileName campoFormioNomeFile, Long idIstanza) throws DAOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN campoFormioNomeFile: "+campoFormioNomeFile);
				log.debug("[" + CLASS_NAME + "::updateIdIstanza] IN id istanza: "+idIstanza);
			}
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ID_ISTANZA_KFKL_BY_FORMIO, mapCampoFormioIdIstanzaParameters(campoFormioNomeFile,idIstanza));
			if (log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::updateIdIstanza] Record aggiornati: " + numRecord);
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateIdIstanza] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA REPOSITORY FILE");
		}
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
	public int updateProtocollo(Long idFile, String numeroProtocollo, Date dataProtocollo)
			throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateProtocollo] IN idFile: " + idFile + "  numeroProtocollo: " + numeroProtocollo + "  dataProtocollo: " + dataProtocollo );
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_file", idFile);
			params.addValue("numero_protocollo", numeroProtocollo, Types.VARCHAR);
			params.addValue("data_protocollo", dataProtocollo, Types.TIMESTAMP);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_PROTOCOLLO, params);
			log.debug("[" + CLASS_NAME + "::updateProtocollo] Record aggiornati: " + numRecord);
			if (numRecord==0) {
				log.error("[" + CLASS_NAME + "::updateProtocollo] Elemento non trovato: idFile=" + idFile);
				throw new ItemNotFoundDAOException();
			}
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateProtocollo] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}
	
	@Override
	public int updateUuidIndex(RepositoryFileEntity entity) {
		try {
			log.debug("[" + CLASS_NAME + "::updateUuidIndex] IN repository idfile: "+entity.getIdFile());
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("uuid_index", entity.getUuidIndex());
	    	params.addValue("id_file", entity.getIdFile());
	    	
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_UUID_INDEX, params);
			log.debug("[" + CLASS_NAME + "::updateUuidIndex] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateUuidIndex] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO UUID_INDEX REPO FILE");
		}
	}
    

	@Override
	public Optional<RepositoryFileEntity> findRenderedFileByIdIstanzaStoricoTipo(Long idIstanza, Long idStoricoWorkflow, Integer idTipologia) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findRenderedFileByIdIstanzaStoricoTipo] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_storico_workflow", idStoricoWorkflow);
			params.addValue("id_tipologia", idTipologia);
			return Optional.of((RepositoryFileEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA_STORICO_TIPO, params, BeanPropertyRowMapper.newInstance(RepositoryFileEntity.class) ));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findRenderedFileByIdIstanzaStoricoTipo] Elemento non trovato. idIstanza:" + idIstanza);
			return Optional.empty();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findRenderedFileByIdIstanzaStoricoTipo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	public void commit() throws DAOException {
		try {
			getCustomJdbcTemplate().getDataSource().getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			new DAOException();
		}
	}
}
