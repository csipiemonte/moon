/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.StatoDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.StatiFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai stati delle istanze
 * <br>
 * <br>Tabella principale : moon_wf_d_stato
 * 
 * @see StatoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */

@Component
public class StatoDAOImpl extends JdbcTemplateDAO implements StatoDAO {
	
	private static final String CLASS_NAME = "StatoDAOImpl";
	
	private static final String ID_STATO_WF = "id_stato_wf";
	private static final String CODICE_STATO_WF = "codice_stato_wf";
	private static final String NOME_STATO_WF = "nome_stato_wf";
	private static final String NOME_PORTALE = "nome_portale";
	private static final String ID_ENTE = "id_ente";
	private static final String ID_AMBITO = "id_ambito";
	private static final String ID_VISIBILITA_AMBITO = "id_visibilita_ambito";
	private static final String ID_MODULO = "id_modulo";
	
	private static final String FIND_SELECT_FIELDS = "SELECT id_stato_wf, codice_stato_wf, nome_stato_wf, desc_stato_wf, id_tab_fo ";
	private static final String FIND_SELECT_FIELDS_S = "SELECT distinct s.id_stato_wf, s.codice_stato_wf, s.nome_stato_wf, s.desc_stato_wf, s.id_tab_fo";
	private static final String FROM_MOON_WF_D_STATO = " FROM moon_wf_d_stato";
	
	private static final  String FIND_BY_ID = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_STATO +
		" WHERE id_stato_wf = :id_stato_wf";
	
	private static final String FIND_BY_CD = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_STATO +
		" WHERE codice_stato_wf = :codice_stato_wf";
	
	private static final String FIND_BY_NOME = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_STATO +
		" WHERE nome_stato_wf = :nome_stato_wf";

	private static final String FIND = FIND_SELECT_FIELDS +
		FROM_MOON_WF_D_STATO +
		" WHERE 1=1";
	private static final String FIND_MOD_FILTER = FIND_SELECT_FIELDS_S +
			" FROM moon_fo_t_istanza i, moon_wf_d_stato s " +
			" WHERE i.id_stato_wf = s.id_stato_wf";
	
	private static final String INSERT = "INSERT INTO moon_wf_d_stato(" +
		" id_stato_wf, codice_stato_wf, nome_stato_wf, desc_stato_wf, id_tab_fo)" +
		" VALUES (:id_stato_wf, :codice_stato_wf, :nome_stato_wf, :desc_stato_wf, :id_tab_fo)";

	private static final String DELETE = "DELETE FROM moon_wf_d_stato WHERE id_stato_wf = :id_stato_wf";

	private static final String UPDATE = "UPDATE moon_wf_d_stato" +
		" SET codice_ente=:codice_ente, nome_ente=:nome_ente, descrizione_ente=:descrizione_ente, fl_attivo=:fl_attivo, id_tipo_ente=:id_tipo_ente, logo=:logo, indirizzo=:indirizzo, data_upd=:data_upd, attore_upd=:attore_upd, id_ente_padre=:id_ente_padre, codice_ipa=:codice_ipa" +
		" WHERE id_stato_wf = :id_stato_wf";

	private static final String SEQ_ID = "SELECT nextval('moon_wf_d_stato_id_stato_wf_seq')";
	
	private static final Duration DURATION_CACHE = Duration.ofMinutes(30);
	private static Map<Integer, StatoEntity> cache = new HashMap<>();
	private LocalTime lastResetCache = LocalTime.now();

	@Override
	public StatoEntity findById(Integer idStatoWf) throws ItemNotFoundDAOException, DAOException {
		try {
			if(log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::findById] IN idStatoWf: "+idStatoWf);
			}
			StatoEntity result = cache.get(idStatoWf);
			if(result==null) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue(ID_STATO_WF, idStatoWf);
				result = (StatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(StatoEntity.class) );
				cache.put(idStatoWf, result);
			}
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+idStatoWf, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+idStatoWf, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public StatoEntity findByCd(String codiceStato) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_STATO_WF, codiceStato);
			return (StatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(StatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCd] Elemento non trovato: "+codiceStato, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCd] Errore database: "+codiceStato, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public StatoEntity findByNome(String nomeStato) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(NOME_STATO_WF, nomeStato);
			return (StatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_NOME, params, BeanPropertyRowMapper.newInstance(StatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByNome] Elemento non trovato: "+nomeStato, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByNome] Errore database: "+nomeStato, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<StatoEntity> find() {
		log.debug("[" + CLASS_NAME + "::find] BEGIN");
		return (List<StatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND, BeanPropertyRowMapper.newInstance(StatoEntity.class));
	}
	@Override
	public List<StatoEntity> find(StatiFilter filter) {
		List<StatoEntity> result = null;
		log.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		StringBuilder sb = new StringBuilder(FIND)
				.append(readFilter(filter, params));
		
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<StatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(StatoEntity.class));
		
		return result;
	}
	private StringBuilder readFilter(StatiFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			readFilterProperty(filter.getIdModulo(), " AND id_stato_wf IN (SELECT DISTINCT i.id_stato_wf FROM moon_fo_t_istanza i WHERE i.id_modulo=:id_modulo)", ID_MODULO, sb, params);
		}
		return sb.append(" ORDER BY nome_stato_wf ASC");
	}
	
	@Override
	public List<StatoEntity> find(ModuliFilter filter) {
		List<StatoEntity> result = null;
		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		StringBuilder sb = new StringBuilder(FIND_MOD_FILTER)
				.append(readFilter(filter, params));
		
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<StatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(StatoEntity.class));
		
		return result;
	}
	private StringBuilder readFilter(ModuliFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			readFilterProperty(filter.getNomePortale(), " AND i.id_modulo IN (SELECT rpm.id_modulo FROM moon_fo_d_portale p, moon_fo_r_portale_modulo rpm WHERE p.nome_portale = :nome_portale and rpm.id_portale = p.id_portale)", NOME_PORTALE, sb, params);
			readFilterPropertyUC(filter.getConPresenzaIstanzeUser(), " AND i.identificativo_utente = :identificativo_utente", "identificativo_utente", sb, params);		
			readFilterProperty(filter.getIdEnte(), " AND i.id_ente = :id_ente", ID_ENTE, sb, params);
			readFilterProperty(filter.getIdAmbito(), " AND i.id_modulo IN (SELECT catmod.id_modulo" +
					" FROM moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod" +
					" WHERE cat.id_ambito = :id_ambito AND catmod.id_categoria = cat.id_categoria)", ID_AMBITO, sb, params);
			readFilterProperty(filter.getIdVisibilitaAmbito(), " AND i.id_modulo IN (SELECT catmod.id_modulo" +
					" FROM moon_fo_d_ambito amb, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod" +
					" WHERE amb.id_visibilita_ambito = :id_visibilita_ambito AND cat.id_ambito = amb.id_ambito AND catmod.id_categoria = cat.id_categoria)", ID_VISIBILITA_AMBITO, sb, params);
		}
		return sb.append(" ORDER BY s.nome_stato_wf ASC");
	}
	
	
	/**
	 * Inserisce un stato nel sistema
	 * 
	 * @param {@code entity} lo stato da inserire. Cannot be {@code null}
	 * 
	 * @return la chiave primaria {@code idStatoWf} generato durante l'inserimento.
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public Integer insert(StatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Integer idStatoWf = (int) getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdStatoWf(idStatoWf);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapStatoEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idStatoWf;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO STATO");
		}

	}

	/**
	 * Aggiorna un stato nel sistema sulla base della sua chiave primaria {@code idStatoWf} 
	 * 
	 * @param {@code entity} lo stato da aggiornare. Cannot be {@code null}
	 * 
	 * @return il numero di righe aggiornate nel database (0 se nessun stato aggiornato, 1 se stato aggiornato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int update(StatoEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapStatoEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO STATO");
		}
	}

	/**
	 * Cancella un stato dal sistema per chiave primaria {@code idStatoWf}.
	 * Per essere attuabile, non devono essere presenti referenze dello stato in cancellazione nelle entity collegate
	 * 
	 * @param {@code idStatoWf} la chiave primaria. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun stato cancellato, 1 se stato cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(Integer idStatoWf) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idStatoWf: "+idStatoWf);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_STATO_WF, idStatoWf);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE ELIMINAZIONE STATO");
		}
	}

	/**
	 * Cancella uno stato dal sistema per entita Stato {@code entity}. L'entita deve avere valorizzata almeno la sua chiave primaria {@code idStatoWf}.
	 * Per essere attuabile, non devono essere presenti referenze dello stato in cancellazione nelle entity collegate
	 * 
	 * @param {@code entity} lo stato da cancellare. Cannot be {@code null}
	 * 
	 * @return il numero di righe cancellate nel database (0 se nessun stato cancellato, 1 se stato cancellato).
	 * 
	 * @throws DAOException se si verificano errori.
	 */
	@Override
	public int delete(StatoEntity entity) throws DAOException {
		return delete(entity.getIdStatoWf());
	}
	

	// id_stato_wf, codice_stato_wf, nome_stato_wf, desc_stato_wf, id_tab_fo
    private MapSqlParameterSource mapStatoEntityParameters(StatoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_stato_wf", entity.getIdStatoWf());
    	params.addValue("codice_stato_wf" , entity.getCodiceStatoWf(), Types.VARCHAR);
    	params.addValue("nome_stato_wf" , entity.getNomeStatoWf(), Types.VARCHAR);
    	params.addValue("desc_stato_wf", entity.getDescStatoWf(), Types.VARCHAR);
    	params.addValue("id_tab_fo", entity.getIdTabFo());
    	return params;
    }

    
    //
    //
    public void forceResetModuliCache() {
    	cache.clear();
    	lastResetCache = LocalTime.now();
    	log.info("[" + CLASS_NAME + "::forceResetModuliCache] BEGIN END");
    }
    public void initCache() {
    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetModuliCache();
    	}
    }
}
