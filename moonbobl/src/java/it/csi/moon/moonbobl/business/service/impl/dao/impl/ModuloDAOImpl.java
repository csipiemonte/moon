/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import it.csi.moon.moonbobl.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersioneEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ProcessoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloVersioneStato;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.decodifica.DecodificaProcessoIstanza;

/**
 * DAO per l'accesso ai moduli
 * 
 * @see ModuloEntity
 * 
 * @author Laurent
 * @author Francesco
 *
 * @since 1.0.0
 */
@Component
public class ModuloDAOImpl extends JdbcTemplateDAO implements ModuloDAO {
	private final static String CLASS_NAME = "ModuloDAOImpl";
	
	private static final  String SELECT_FIELDS = "SELECT id_modulo, codice_modulo, oggetto_modulo, descrizione_modulo, data_ins, data_upd, attore_upd, flag_is_riservato, id_tipo_codice_istanza, flag_protocollo_integrato ";

	private static final  String FIND_BY_ID = SELECT_FIELDS +
		" FROM moon_io_d_modulo WHERE id_modulo = :id_modulo";
	private static final  String FIND_BY_CD = SELECT_FIELDS +
		" FROM moon_io_d_modulo WHERE codice_modulo = :codice_modulo";
	private static final String ELENCO = SELECT_FIELDS +
		" FROM moon_io_d_modulo";
	
	private static final String FIND_ALL_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
			" FROM moon_io_r_cronologia_statomodulo" +
			" WHERE id_versione_modulo=:id_versione_modulo" +
			" ORDER BY data_inizio_validita";
	private static final String FIND_CURRENT_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
		" FROM moon_io_r_cronologia_statomodulo" +
		" WHERE id_versione_modulo=:id_versione_modulo" +
		" AND data_inizio_validita <= :now" + 
		" AND (data_fine_validita > :now OR data_fine_validita IS NULL)";
	private static final String FIND_LAST_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
		" FROM moon_io_r_cronologia_statomodulo" +
		" WHERE id_versione_modulo=:id_versione_modulo" +
		" AND data_fine_validita IS NULL";
	
	private static final String FIND_VERS_BY_ID = "SELECT id_versione_modulo, id_modulo, versione_modulo, attore_upd" +
		" FROM moon_io_d_versione_modulo" +
		" WHERE id_versione_modulo=:id_versione_modulo";
	
	private static final String FIND_VERS_BY_ID_MODULO_AND_VERSIONE = "SELECT id_versione_modulo, id_modulo, versione_modulo, attore_upd" +
		" FROM moon_io_d_versione_modulo" +
		" WHERE id_modulo=:id_modulo" +
		" AND versione_modulo=:versione_modulo";
	
	private static final String INSERT = "INSERT INTO moon_io_d_modulo(" + 
		" id_modulo, codice_modulo, oggetto_modulo, descrizione_modulo, data_ins, data_upd, attore_upd, flag_is_riservato, id_tipo_codice_istanza, " +
		" flag_protocollo_integrato)" + 
		" VALUES (:id_modulo, :codice_modulo, :oggetto_modulo, :descrizione_modulo, :data_ins, :data_upd, :attore_upd, :flag_is_riservato, :id_tipo_codice_istanza, :flag_protocollo_integrato)";
	private static final String INSERT_CRON = "INSERT INTO moon_io_r_cronologia_statomodulo(id_cron, id_versione_modulo, id_stato, data_inizio_validita,data_fine_validita)" + // MEV_VERSIONE  ADD field id_versione_modulo
		" VALUES (:id_cron,:id_versione_modulo, :id_stato, :data_inizio_validita, :data_fine_validita)";
	private static final String INSERT_VERS = "INSERT INTO moon_io_d_versione_modulo(id_versione_modulo, id_modulo, versione_modulo, data_upd, attore_upd)" +
		" VALUES (:id_versione_modulo, :id_modulo, :versione_modulo, :data_upd, :attore_upd)";
	
	private static final String UPDATE = "UPDATE moon_io_d_modulo" + 
		" SET codice_modulo=:codice_modulo, oggetto_modulo=:oggetto_modulo, " +
		" descrizione_modulo=:descrizione_modulo, data_ins=:data_ins, data_upd=:data_upd, attore_upd=:attore_upd, flag_is_riservato=:flag_is_riservato, id_tipo_codice_istanza=:id_tipo_codice_istanza, flag_protocollo_integrato=:flag_protocollo_integrato" + 
		" WHERE id_modulo=:id_modulo";
	private static final String UPDATE_CRON = "UPDATE moon_io_r_cronologia_statomodulo" +
		" SET id_versione_modulo=:id_versione_modulo,id_stato=:id_stato,data_inizio_validita=:data_inizio_validita,data_fine_validita=:data_fine_validita" +
		" WHERE id_cron=:id_cron";
	private static final String UPDATE_VERS = "UPDATE moon_io_d_versione_modulo" +
		" SET id_modulo=:id_modulo, versione_modulo=:versione_modulo, data_upd=:data_upd, attore_upd=:attore_upd" +
		" WHERE id_versione_modulo=:id_versione_modulo";
	
	private static final String DELETE = "DELETE FROM moon_io_d_modulo WHERE id_modulo = :id_modulo";
	private static final String DELETE_CRON = "DELETE FROM moon_io_r_cronologia_statomodulo WHERE id_versione_modulo = :id_versione_modulo";
	private static final String DELETE_VERS = "DELETE FROM moon_io_d_versione_modulo WHERE id_versione_modulo = :id_versione_modulo";
	
	private static final String SEQ_ID = "SELECT nextval('moon_io_d_modulo_id_modulo_seq')";
	private static final String SEQ_ID_VERS = "SELECT nextval('moon_io_d_versione_modulo_id_versione_modulo_seq')";
	private static final String SEQ_ID_CRON = "SELECT nextval('moon_io_r_cronologia_statomodulo_id_cron_seq')";
	
	private static final  String SELECT_FIELDS_M = "SELECT m.id_modulo, m.codice_modulo, m.oggetto_modulo, m.descrizione_modulo, m.data_ins, m.flag_is_riservato, m.id_tipo_codice_istanza, m.flag_protocollo_integrato ";
	private static final  String _FIELDS_V_CRON_CAT = 
		", v.id_versione_modulo, v.versione_modulo, v.data_upd, v.attore_upd " +
		", cron.id_cron, cron.id_stato, cron.data_inizio_validita, cron.data_fine_validita " +
		", cat.id_categoria, cat.nome_categoria";
	private static final String _FIELD_RANK_LAST_VERS = ", rank() OVER (PARTITION BY m.id_modulo ORDER BY v.id_versione_modulo DESC)";
	private static final String SELECT_FIND = SELECT_FIELDS_M + _FIELDS_V_CRON_CAT;
	private static final String FROM_FIND = 
		" FROM moon_io_d_modulo m, moon_io_d_versione_modulo v, moon_io_r_cronologia_statomodulo cron, moon_fo_r_categoria_modulo catmod, moon_fo_d_categoria cat" +
		" WHERE v.id_modulo = m.id_modulo" +
		" AND cron.id_versione_modulo = v.id_versione_modulo" +
		" AND catmod.id_modulo = m.id_modulo" +
		" AND cat.id_categoria = catmod.id_categoria";

	private static final String FROM_FIND_WITH_UTENTE = 
		" FROM moon_io_d_modulo m, moon_io_d_versione_modulo v, moon_io_r_cronologia_statomodulo cron, moon_fo_r_categoria_modulo catmod, moon_fo_d_categoria cat, moon_fo_r_utente_modulo um, moon_fo_t_utente u" +
		" WHERE v.id_modulo = m.id_modulo" +
		" AND cron.id_versione_modulo = v.id_versione_modulo" +
		" AND catmod.id_modulo = m.id_modulo" +
		" AND cat.id_categoria = catmod.id_categoria" +
		" AND m.id_modulo = um.id_modulo AND um.id_utente = u.id_utente ";
	
	private static final String CURRENT_STATO = " AND cron.data_inizio_validita <= :now" + 
		" AND (cron.data_fine_validita > :now OR cron.data_fine_validita IS NULL)";
	private static final String BY_ID = " AND m.id_modulo=:id_modulo" + 
		" AND v.id_versione_modulo=:id_versione_modulo";
	private static final String BY_CD = " AND m.codice_modulo=:codice_modulo" + 
		" AND v.versione_modulo=:versione_modulo";
	private static final String BY_CD_PUB = " AND m.codice_modulo=:codice_modulo" + 
		" AND cron.id_stato=40"; // 40-PUB;
	private static final String BY_ID_PUB = " AND m.id_modulo=:id_modulo" + 
			" AND cron.id_stato=40"; // 40-PUB;
	
	private static final String FIND_VERSIONI_STATI_PART1 = "select row_number() over(order by split_part(mv.versione_modulo,'.',1) DESC"
		+ ", split_part(mv.versione_modulo,'.',2) DESC"
		+ ", split_part(mv.versione_modulo,'.',3) DESC"
		+ ", cron.data_inizio_validita desc) as id"
		+ ", mv.id_modulo, mv.id_versione_modulo, mv.versione_modulo, mv.data_upd, mv.attore_upd"
		+ ", s.id_stato, s.codice_stato AS codice, s.descrizione_stato AS descrizione"
		+ ", cron.data_inizio_validita, cron.data_fine_validita"
		+ " FROM moon_io_d_versione_modulo mv"
		+ " INNER JOIN moon_io_r_cronologia_statomodulo cron on cron.id_versione_modulo=mv.id_versione_modulo";
	private static final String FIND_VERSIONI_STATI_PART2 = 
		  " INNER JOIN moon_io_d_statomodulo s on s.id_stato=cron.id_stato"
		+ " WHERE mv.id_modulo=:id_modulo"
		+ " ORDER BY split_part(mv.versione_modulo,'.',1) DESC"
		+ ", split_part(mv.versione_modulo,'.',2) DESC"
		+ ", split_part(mv.versione_modulo,'.',3) DESC"
		+ ", cron.data_inizio_validita desc";
	
	private static final String FIND_PROCESSO = "SELECT id_processo " +
			" FROM moon_wf_r_modulo_processo" +
			" WHERE id_modulo = :id_modulo and ordine=1" ;	
    	
	
//	private static Map<Long, ModuloEntity> cache = new HashMap<>();
//	private static LocalTime lastResetCache = LocalTime.now();
//	private static final Duration DURATION_CACHE = Duration.ofMinutes(30);
	
	@Override
	public ModuloEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			if(log.isDebugEnabled()) {
				log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", id);
			return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+id);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+id, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloEntity findByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByCodice] IN codiceModulo: "+codiceModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(ModuloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+codiceModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+codiceModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<ModuloEntity> getElencoModuli() throws DAOException {
		return ( List<ModuloEntity>)getCustomJdbcTemplate().query(ELENCO + ORDER_BY_DATA_UPD_DESC, BeanPropertyRowMapper.newInstance(ModuloEntity.class));
	}
	
	@Override
	public Long insert(ModuloEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdModulo(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (DuplicateKeyException de) {
			log.error("[" + CLASS_NAME + "::insert] DuplicateKeyException: ");
			throw new DAOException("Codice Modulo già presente","MOONBOBL-30101");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO");
		}
	}


	@Override
	public List<ModuloVersionatoEntity> find(ModuliFilter filter) throws DAOException {
		return find(filter, ORDER_BY_OGGETTO_ASC);
	}
	
	@Override
	public List<ModuloVersionatoEntity> find(ModuliFilter filter, String order) throws DAOException {
		List<ModuloVersionatoEntity> result = null;
		
		StringBuilder sb = null;
		MapSqlParameterSource params = new MapSqlParameterSource();

		if (filter.isOnlyLastVersione()) {
			sb = new StringBuilder("select a.* from (")
				.append(SELECT_FIND).append(_FIELD_RANK_LAST_VERS)
				.append(filter.getUtenteAbilitato().isPresent()?FROM_FIND_WITH_UTENTE:FROM_FIND)
				.append(readFilter(filter, params)) // Filtro
				.append(") a where a.rank=1");
		} else {
			sb = new StringBuilder(SELECT_FIND)
				.append(filter.getUtenteAbilitato().isPresent()?FROM_FIND_WITH_UTENTE:FROM_FIND)
				.append(readFilter(filter, params)); // Filtro
		}

		sb.append(order);
		log.info("[" + CLASS_NAME + "::find] filter: "+filter);
		log.info("[" + CLASS_NAME + "::find] sql: "+sb.toString());
		log.info("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<ModuloVersionatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class));
		
		return result;
	}

	private StringBuilder readFilter(ModuliFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			if (filter.getConPresenzaIstanzeUser().isPresent()) {
				sb.append(" AND m.id_modulo IN (SELECT DISTINCT i.id_modulo FROM moon_fo_t_istanza i WHERE i.codice_fiscale_dichiarante=:codice_fiscale_dichiarante)");
				params.addValue("codice_fiscale_dichiarante", filter.getConPresenzaIstanzeUser().get());
			}
			if (filter.getCodiceModulo().isPresent()) {
				sb.append(" AND codice_modulo = :codice_modulo ");
				params.addValue("codice_modulo", filter.getCodiceModulo().get());
			}
			if (filter.getIdModulo().isPresent()) {
				sb.append(" AND m.id_modulo=:id_modulo");
				params.addValue("id_modulo", filter.getIdModulo().get());
			}
			if (filter.getIdVersioneModulo().isPresent()) {
				sb.append(" AND v.id_versione_modulo=:id_versione_modulo");
				params.addValue("id_versione_modulo", filter.getIdVersioneModulo().get());
			}
			if (filter.getVersioneModulo().isPresent()) {
				sb.append(" AND v.versione_modulo LIKE :versione_modulo");
				params.addValue("versione_modulo", "%"+filter.getVersioneModulo().get()+"%");
			}
			if (filter.getOggettoModulo().isPresent()) {
				sb.append(" AND oggetto_modulo LIKE :oggetto_modulo ");
				params.addValue("oggetto_modulo", "%"+filter.getOggettoModulo().get()+"%");
			}
			if (filter.getDescrizioneModulo().isPresent()) {
				sb.append(" AND descrizione_modulo LIKE :descrizione_modulo ");
				params.addValue("descrizione_modulo", "%"+filter.getDescrizioneModulo().get()+"%");
			}
			if (filter.getFlagIsRiservato().isPresent()) {
				sb.append(" AND flag_is_riservato = :flag_is_riservato ");
				params.addValue("flag_is_riservato", filter.getFlagIsRiservato().get());
			}
			if (filter.getIdTipoCodiceIstanza().isPresent()) {
				sb.append(" AND id_tipo_codice_istanza = :id_tipo_codice_istanza ");
				params.addValue("id_tipo_codice_istanza", filter.getIdTipoCodiceIstanza().get());
			}
			if (filter.getFlagProtocolloIntegrato().isPresent()) {
				sb.append(" AND flag_protocollo_integrato = :flag_protocollo_integrato ");
				params.addValue("flag_protocollo_integrato", filter.getFlagProtocolloIntegrato().get());
			}
			if (filter.getStatoModulo().isPresent()) {
				// filtro solo su UN stato per adesso
				sb.append(" AND cron.id_stato = (SELECT id_stato FROM moon_io_d_statomodulo WHERE codice_stato = :codice_stato_cron)");
				params.addValue("codice_stato_cron", filter.getStatoModulo().get().getCodice());
			}
			if (!filter.isFindAllCronologie()) {
				sb.append(" AND cron.data_inizio_validita <= :now ");
				sb.append(" AND (cron.data_fine_validita > :now OR cron.data_fine_validita IS NULL)");
				params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			}
			if (filter.getUtenteAbilitato().isPresent()) {
				sb.append(" AND u.identificativo_utente = :identificativo_utente");
				params.addValue("identificativo_utente", filter.getUtenteAbilitato().get().toUpperCase());
			}
			if (filter.getNomePortale().isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_portale_modulo rpm INNER JOIN moon_fo_d_portale p on rpm.id_portale=p.id_portale" + 
					" WHERE rpm.id_modulo = m.id_modulo" + 
					" AND p.nome_portale = :nome_portale)");
				params.addValue("nome_portale", filter.getNomePortale().get());
			}
			if (filter.getIdEnte().isPresent() || filter.getIdArea().isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_area_modulo ram" + 
					" WHERE ram.id_modulo = m.id_modulo" + 
					(filter.getIdEnte().isPresent()?" AND ram.id_ente = :id_ente":"") +
					(filter.getIdArea().isPresent()?" AND ram.id_area = :id_area":"") +
					")");				
				if (filter.getIdEnte().isPresent()) {
	                  params.addValue("id_ente", filter.getIdEnte().get());
				}
				if (filter.getIdArea().isPresent()) {
	                  params.addValue("id_area", filter.getIdArea().get());
				}
			}
			if (filter.getAttributoPresente().isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
						" FROM moon_io_d_moduloattributi ma" + 
						" WHERE ma.id_modulo = m.id_modulo" + 
						" AND ma.nome_attributo = :nome_attributo" + 
						" AND ma.valore is not null and length(ma.valore) > 0)" );									
					params.addValue("nome_attributo", filter.getAttributoPresente().get());
			}
		}
		return sb;
	}

	@Override
	public int update(ModuloEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO");
		}
		
	}
	
	
	@Override
	public int delete(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", id);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public int deleteCronologia(Long idVersioneModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_CRON, params);
			log.debug("[" + CLASS_NAME + "::deleteCronologia] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::deleteCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public int deleteVersione(Long idVersioneModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::deleteVersione] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_VERS, params);
			log.debug("[" + CLASS_NAME + "::deleteVersione] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::deleteVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public List<ModuloCronologiaStatiEntity> findAllCronologia(Long idVersioneModulo) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findAllCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (List<ModuloCronologiaStatiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class) );
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findAllCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public ModuloCronologiaStatiEntity findCurrentCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findCurrentCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_CURRENT_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findCurrentCronologia] Elemento non trovato per idVersioneModulo: " + idVersioneModulo, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findCurrentCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public ModuloCronologiaStatiEntity findLastCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findLastCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (ModuloCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato per idVersioneModulo: " + idVersioneModulo, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findLastCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ModuloVersioneEntity findVersioneById(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findVersioneById] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (ModuloVersioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_VERS_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloVersioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findVersioneById] Elemento non trovato per idVersioneModulo: "+idVersioneModulo,emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findVersioneById] Errore database per idVersioneModulo: "+idVersioneModulo,e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersioneEntity findVersione(Long idModulo, String versione) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findVersione] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("versione_modulo", versione);
			return (ModuloVersioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_VERS_BY_ID_MODULO_AND_VERSIONE, params, BeanPropertyRowMapper.newInstance(ModuloVersioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findVersione] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersionatoEntity findModuloVersionatoById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findModuloVersionatoById] IN idModulo: "+idModulo+"  idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_versione_modulo", idVersioneModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoById] Elemento non trovato :: idModulo: "+idModulo+"  idVersioneModulo: "+idVersioneModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersionatoEntity findModuloVersionatoByCodice(String codiceModulo, String versione) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findModuloVersionatoByCodice] IN codiceModulo: "+codiceModulo+"  versione: "+versione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			params.addValue("versione_modulo", versione);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_CD, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoByCodice] Elemento non trovato: "+codiceModulo + " - " + versione);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoByCodice] Exception per: "+codiceModulo + " - " + versione, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	public ModuloVersionatoEntity findModuloVersionatoPubblicatoByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] IN codiceModulo: "+codiceModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_modulo", codiceModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_CD_PUB, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] Elemento non trovato: "+codiceModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] Exception per: "+codiceModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ModuloVersionatoEntity findModuloVersionatoPubblicatoById(Long idModulo, Date inDataOra) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] IN idModulo: "+idModulo+"  inDataOra: "+inDataOra);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("now", inDataOra, Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_ID_PUB, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] Elemento non trovato :: idModulo: "+idModulo+"  inDataOra: "+inDataOra);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public Long insertCronologia(ModuloCronologiaStatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insertCronologia] IN entity: "+entity);
			Long idCron = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_CRON, new MapSqlParameterSource() );
			entity.setIdCron(idCron);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CRON, mapCronologiaStatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insertCronologia] Record inseriti: " + numRecord);
			return idCron;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA CRONOLOGIA");
		}
	}

	@Override
	public Long insertVersione(ModuloVersioneEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insertVersione] IN entity: "+entity);
			Long idVersioneModulo = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_VERS, new MapSqlParameterSource() );
			entity.setIdVersioneModulo(idVersioneModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_VERS, mapVersioneEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insertVersione] Record inseriti: " + numRecord);
			return idVersioneModulo;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO VERSIONE");
		}
	}
	

	@Override
	public int updateCronologia(ModuloCronologiaStatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateCronologia] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_CRON, mapCronologiaStatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::updateCronologia] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO CRONOLOGIA");
		}
	}
	
	@Override
	public int updateVersione(ModuloVersioneEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateVersione] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_VERS, mapVersioneEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::updateVersione] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO VERSIONE");
		}
	}
	
	
    private MapSqlParameterSource mapEntityParameters(ModuloEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("codice_modulo" , entity.getCodiceModulo(), Types.VARCHAR);
    	params.addValue("oggetto_modulo", entity.getOggettoModulo(), Types.VARCHAR);
    	params.addValue("descrizione_modulo", entity.getDescrizioneModulo(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	params.addValue("flag_is_riservato", entity.getFlagIsRiservato(), Types.VARCHAR);
    	params.addValue("id_tipo_codice_istanza", entity.getIdTipoCodiceIstanza(), Types.INTEGER);
    	params.addValue("flag_protocollo_integrato", entity.getFlagProtocolloIntegrato(), Types.VARCHAR);
    	return params;
    }

    private MapSqlParameterSource mapCronologiaStatiEntityParameters(ModuloCronologiaStatiEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_cron", entity.getIdCron());
    	params.addValue("id_versione_modulo", entity.getIdVersioneModulo());
    	params.addValue("id_stato" , entity.getIdStato());
    	params.addValue("data_inizio_validita", entity.getDataInizioValidita(), Types.TIMESTAMP);
    	params.addValue("data_fine_validita", entity.getDataFineValidita(), Types.TIMESTAMP);
    	return params;
    }

    private MapSqlParameterSource mapVersioneEntityParameters(ModuloVersioneEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_versione_modulo", entity.getIdVersioneModulo());
    	params.addValue("id_modulo" , entity.getIdModulo());
    	params.addValue("versione_modulo" , entity.getVersioneModulo(), Types.VARCHAR);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }

	@Override
	public List<ModuloVersioneStato> findVersioniModuloById(Long idModulo, String fields) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_modulo", idModulo);
//		StringBuilder sb = null;
		StringBuilder sbSingleQuery = new StringBuilder(FIND_VERSIONI_STATI_PART1)
			.append(fields!=null&&fields.contains("CurrentStato")?" and cron.data_fine_validita is NULL":"")
			.append(FIND_VERSIONI_STATI_PART2);
//		// idParent
//		if(fields!=null&&fields.contains("ConIdParent")) {
//			sb = new StringBuilder("SELECT a.*, b.id AS id_parent FROM (")
//				.append(sbSingleQuery)
//				.append(") a INNER JOIN (")
//				.append(sbSingleQuery)
//				.append(") b ON (a.versione_modulo=b.versione_modulo AND b.data_fine_validita IS NULL)");
//		} else {
//			sb = sbSingleQuery;
//		}
		List<ModuloVersioneStato> result = (List<ModuloVersioneStato>) getCustomNamedParameterJdbcTemplateImpl().query(sbSingleQuery.toString(), params, BeanPropertyRowMapper.newInstance(ModuloVersioneStato.class));
		long idParent = 0;
		long idx = 1;
		long lastIdx = 1;
		for (ModuloVersioneStato r : result) {
			if (r.getDataFineValidita()==null) {
				idParent = 0;
				lastIdx = idx;
			} else {
				idParent = lastIdx;
			}
			r.setIdParent(idParent);
			idx++;
		}
		return result;
	}
	
	
	@Override
	public Long findIdProcesso(Long idModulo) throws DAOException {
		Long result = null;
		log.debug("[" + CLASS_NAME + "::findIdProcesso] IN idModulo: "+idModulo);
		
		StringBuilder sb = new StringBuilder(FIND_PROCESSO);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_modulo", idModulo);

		List<ProcessoEntity> resultList = ( List<ProcessoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), 
				params, BeanPropertyRowMapper.newInstance(ProcessoEntity.class));
		
		if (resultList == null || resultList.size() == 0) {
			result = DecodificaProcessoIstanza.COMPILAZIONE.getIdProcesso();
		}
		else {
			result = resultList.get(0).getIdProcesso();
		}
		
		return result;
	}
	
	

    
    //
    //
//    public void forceResetModuliCache() {
//    	cache.clear();
//    	lastResetCache = LocalTime.now();
//    	log.info("[" + CLASS_NAME + "::forceResetModuliCache] BEGIN END");
//    }
//    public void initCache() {
//    	if (Duration.between(this.lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
//    		forceResetModuliCache();
//    	}
//    }
    
}
