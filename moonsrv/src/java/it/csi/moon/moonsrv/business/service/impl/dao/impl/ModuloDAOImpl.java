/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.commons.entity.ModuloCronologiaStatiEntity;
import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.ModuloVersioneEntity;
import it.csi.moon.commons.util.decodifica.DecodificaStatoModulo;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

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

	private static final String CLASS_NAME = "ModuloDAOImpl";

	private static final String ID_MODULO = "id_modulo";
	private static final String CODICE_MODULO = "codice_modulo";
	private static final String OGGETTO_MODULO = "oggetto_modulo";
	private static final String DESCRIZIONE_MODULO = "descrizione_modulo";
	private static final String ID_VERSIONE_MODULO = "id_versione_modulo";
	private static final String VERSIONE_MODULO = "versione_modulo";
	private static final String ID_VISIBILITA_AMBITO = "id_visibilita_ambito";
	private static final String ID_AMBITO = "id_ambito";
	private static final String NOME_PORTALE = "nome_portale";
	private static final String IDENTIFICATIVO_UTENTE = "identificativo_utente";
	private static final String FLAG_PROTOCOLLO_INTEGRATO = "flag_protocollo_integrato";
	private static final String ID_TIPO_CODICE_ISTANZA = "id_tipo_codice_istanza";
	private static final String FLAG_IS_RISERVATO = "flag_is_riservato";
	private static final String ID_AREA = "id_area";
	private static final String ID_ENTE = "id_ente";
	private static final String CODICE_AREA = "codice_area";
	private static final String CODICE_ENTE = "codice_ente";
	private static final String CODICE_AMBITO = "codice_ambito";
	private static final String CODICE_FISCALE_DICHIARANTE = "codice_fiscale_dichiarante";
	private static final String ID_FRUITORE = "id_fruitore";
	
	private static final String SELECT_FIELDS = "SELECT id_modulo, codice_modulo, oggetto_modulo, descrizione_modulo, data_ins, data_upd, attore_upd, flag_is_riservato, id_tipo_codice_istanza, flag_protocollo_integrato ";

	private static final String FIND_BY_ID = SELECT_FIELDS +
		" FROM moon_io_d_modulo WHERE id_modulo = :id_modulo";
	private static final String FIND_BY_CD = SELECT_FIELDS +
		" FROM moon_io_d_modulo WHERE codice_modulo = :codice_modulo";
	private static final String ELENCO = SELECT_FIELDS +
		" FROM moon_io_d_modulo";
	
	private static final String WHERE_ID_VERSIONE_MODULO = " WHERE id_versione_modulo=:id_versione_modulo";

	private static final String FIND_ALL_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
			" FROM moon_io_r_cronologia_statomodulo" +
			" WHERE id_versione_modulo=:id_versione_modulo" +
			" ORDER BY data_inizio_validita";
	private static final String FIND_CURRENT_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
		" FROM moon_io_r_cronologia_statomodulo" +
		WHERE_ID_VERSIONE_MODULO +
		" AND data_inizio_validita <= :now" + 
		" AND (data_fine_validita > :now OR data_fine_validita IS NULL)";
	private static final String FIND_LAST_CRON_ID_VERS = "SELECT id_cron, id_versione_modulo, id_stato, data_inizio_validita, data_fine_validita" +
		" FROM moon_io_r_cronologia_statomodulo" +
		" WHERE id_versione_modulo=:id_versione_modulo" +
		" AND data_fine_validita IS NULL";
	
	private static final String FIND_VERS_BY_ID = "SELECT id_versione_modulo, id_modulo, versione_modulo, attore_upd" +
		" FROM moon_io_d_versione_modulo" +
		WHERE_ID_VERSIONE_MODULO;
	
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
		WHERE_ID_VERSIONE_MODULO;
	
	private static final String DELETE = "DELETE FROM moon_io_d_modulo WHERE id_modulo = :id_modulo";
	private static final String DELETE_CRON = "DELETE FROM moon_io_r_cronologia_statomodulo WHERE id_versione_modulo = :id_versione_modulo";
	private static final String DELETE_VERS = "DELETE FROM moon_io_d_versione_modulo WHERE id_versione_modulo = :id_versione_modulo";
	
	private static final String SEQ_ID = "SELECT nextval('moon_io_d_modulo_id_modulo_seq')";
	private static final String SEQ_ID_VERS = "SELECT nextval('moon_io_d_versione_modulo_id_versione_modulo_seq')";
	private static final String SEQ_ID_CRON = "SELECT nextval('moon_io_r_cronologia_statomodulo_id_cron_seq')";
	
	private static final String SELECT_FIELDS_M = "SELECT m.id_modulo, m.codice_modulo, m.oggetto_modulo, m.descrizione_modulo, m.data_ins, m.flag_is_riservato, m.id_tipo_codice_istanza, m.flag_protocollo_integrato ";
	private static final String FIELDS_V_CRON_CAT = 
		", v.id_versione_modulo, v.versione_modulo, v.data_upd, v.attore_upd " +
		", cron.id_cron, cron.id_stato, cron.data_inizio_validita, cron.data_fine_validita " +
		", cat.id_categoria, cat.nome_categoria, amb.codice_ambito, amb.nome_ambito, amb.color";
	private static final String FIELD_RANK_LAST_VERS = ", rank() OVER (PARTITION BY m.id_modulo ORDER BY v.id_versione_modulo DESC)";
	private static final String SELECT_FIND = SELECT_FIELDS_M + FIELDS_V_CRON_CAT;
	private static final String FROM_FIND = 
		" FROM moon_io_d_modulo m, moon_io_d_versione_modulo v, moon_io_r_cronologia_statomodulo cron, moon_fo_r_categoria_modulo catmod, moon_fo_d_categoria cat, moon_fo_d_ambito amb" +
		" WHERE v.id_modulo = m.id_modulo" +
		" AND cron.id_versione_modulo = v.id_versione_modulo" +
		" AND catmod.id_modulo = m.id_modulo" +
		" AND cat.id_categoria = catmod.id_categoria" +
		" AND amb.id_ambito = cat.id_ambito";

	private static final String FROM_FIND_WITH_UTENTE = 
		" FROM moon_io_d_modulo m, moon_io_d_versione_modulo v, moon_io_r_cronologia_statomodulo cron, moon_fo_r_categoria_modulo catmod, moon_fo_d_categoria cat, moon_fo_d_ambito amb, moon_fo_r_utente_modulo um, moon_fo_t_utente u" +
		" WHERE v.id_modulo = m.id_modulo" +
		" AND cron.id_versione_modulo = v.id_versione_modulo" +
		" AND catmod.id_modulo = m.id_modulo" +
		" AND cat.id_categoria = catmod.id_categoria" +
		" AND amb.id_ambito = cat.id_ambito" +
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

	private static final String FIND_VERS_PUBB_BY_IDMOD = "SELECT v.*" +
			" FROM moon_io_d_versione_modulo v, moon_io_r_cronologia_statomodulo cron" +
			" WHERE v.id_versione_modulo = cron.id_versione_modulo"+ 
			" AND cron.id_stato=40 "+
			" AND v.id_modulo=:id_modulo" +
			CURRENT_STATO;
	
	private static final Duration DURATION_CACHE = Duration.ofMinutes(30);
	private static Map<Long, ModuloEntity> cache = new HashMap<>();
	private LocalTime lastResetCache = LocalTime.now();


	@Override
	public ModuloEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			if(LOG.isDebugEnabled()) {
				LOG.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			}
			ModuloEntity result = cache.get(id);
			if(result==null) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue(ID_MODULO, id);
				result = (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloEntity.class) );
				cache.put(id, result);
			}
			return result;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+id);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+id, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloEntity findByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodice] IN codiceModulo: "+codiceModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_MODULO, codiceModulo);
			return getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params, BeanPropertyRowMapper.newInstance(ModuloEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Elemento non trovato: "+codiceModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodice] Errore database: "+codiceModulo, e);
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
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdModulo(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (DuplicateKeyException de) {
			LOG.error("[" + CLASS_NAME + "::insert] DuplicateKeyException: ");
			throw new DAOException("Codice Modulo già presente");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
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
				.append(SELECT_FIND).append(FIELD_RANK_LAST_VERS)
				.append(filter.getUtenteAbilitato().isPresent()?FROM_FIND_WITH_UTENTE:FROM_FIND)
				.append(readFilter(filter, params)) // Filtro
				.append(") a where a.rank=1");
		} else {
			sb = new StringBuilder(SELECT_FIND)
				.append(filter.getUtenteAbilitato().isPresent()?FROM_FIND_WITH_UTENTE:FROM_FIND)
				.append(readFilter(filter, params)); // Filtro
		}
		sb.append(order);
		LOG.info("[" + CLASS_NAME + "::find] sql: "+sb.toString());
		LOG.info("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<ModuloVersionatoEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class));
		return result;
	}

	private StringBuilder readFilter(ModuliFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			readFilterProperty(filter.getConPresenzaIstanzeUser(), " AND m.id_modulo IN (SELECT DISTINCT i.id_modulo FROM moon_fo_t_istanza i WHERE i.codice_fiscale_dichiarante=:codice_fiscale_dichiarante AND i.fl_eliminata ='N')", CODICE_FISCALE_DICHIARANTE, sb, params);
			readFilterProperty(filter.getCodiceModulo(), " AND codice_modulo = :codice_modulo", CODICE_MODULO, sb, params);
			readFilterProperty(filter.getIdModulo(), " AND m.id_modulo=:id_modulo", ID_MODULO, sb, params);
			readFilterProperty(filter.getIdVersioneModulo(), " AND v.id_versione_modulo=:id_versione_modulo", ID_VERSIONE_MODULO, sb, params);
			readFilterPropertyContains(filter.getVersioneModulo(), " AND v.versione_modulo LIKE :versione_modulo", VERSIONE_MODULO, sb, params);
			readFilterPropertyContains(filter.getOggettoModulo(), " AND oggetto_modulo LIKE :oggetto_modulo", OGGETTO_MODULO, sb, params);
			readFilterPropertyContains(filter.getDescrizioneModulo(), " AND descrizione_modulo LIKE :descrizione_modulo", DESCRIZIONE_MODULO, sb, params);
			readFilterProperty(filter.getFlagIsRiservato(), " AND flag_is_riservato = :flag_is_riservato", FLAG_IS_RISERVATO, sb, params);
			readFilterProperty(filter.getIdTipoCodiceIstanza(), " AND id_tipo_codice_istanza = :id_tipo_codice_istanza", ID_TIPO_CODICE_ISTANZA, sb, params);
			readFilterProperty(filter.getFlagProtocolloIntegrato(), " AND flag_protocollo_integrato = :flag_protocollo_integrato", FLAG_PROTOCOLLO_INTEGRATO, sb, params);
			Optional<DecodificaStatoModulo> dStatoOpt = filter.getStatoModulo();
			if (dStatoOpt.isPresent()) {
				// filtro solo su UN stato per adesso
				sb.append(" AND cron.id_stato = (SELECT id_stato FROM moon_io_d_statomodulo WHERE codice_stato = :codice_stato_cron)");
				params.addValue("codice_stato_cron", dStatoOpt.get().getCodice());
			}
			if (!filter.isFindAllCronologie()) {
				sb.append(" AND cron.data_inizio_validita <= :now ");
				sb.append(" AND (cron.data_fine_validita > :now OR cron.data_fine_validita IS NULL)");
				params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			}
			readFilterProperty(filter.getUtenteAbilitato(), " AND u.identificativo_utente = :identificativo_utente", IDENTIFICATIVO_UTENTE, sb, params);
			readFilterProperty(filter.getNomePortale(), " AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_portale_modulo rpm INNER JOIN moon_fo_d_portale p on rpm.id_portale=p.id_portale" + 
					" WHERE rpm.id_modulo = m.id_modulo" + 
					" AND p.nome_portale = :nome_portale)", NOME_PORTALE, sb, params);

			Optional<Long> idEnteOpt = filter.getIdEnte();
			Optional<Long> idAreaOpt = filter.getIdArea();
			if (idEnteOpt.isPresent() || idAreaOpt.isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_area_modulo ram" + 
					" WHERE ram.id_modulo = m.id_modulo" + 
					(idEnteOpt.isPresent()?" AND ram.id_ente = :id_ente":"") +
					(idAreaOpt.isPresent()?" AND ram.id_area = :id_area":"") +
					")");				
				if (idEnteOpt.isPresent()) {
	                  params.addValue(ID_ENTE, idEnteOpt.get());
				}
				if (idAreaOpt.isPresent()) {
	                  params.addValue(ID_AREA, idAreaOpt.get());
				}
			}
			Optional<String> cdEnteOpt = filter.getCodiceEnte();
			Optional<String> cdAreaOpt = filter.getCodiceArea();
			if (cdEnteOpt.isPresent() || cdAreaOpt.isPresent()) {
				sb.append(" AND EXISTS (SELECT 1" + 
					" FROM moon_fo_r_area_modulo ram" + 
					" INNER JOIN moon_fo_d_ente e on (e.id_ente=ram.id_ente)" +
					" INNER JOIN moon_fo_d_area a on (a.id_area=ram.id_area)" +
					" WHERE ram.id_modulo = m.id_modulo" +
					(cdEnteOpt.isPresent()?" AND e.codice_ente = :codice_ente":"") +
					(cdAreaOpt.isPresent()?" AND a.codice_area = :codice_area":"") +
					")");				
				if (cdEnteOpt.isPresent()) {
	                  params.addValue(CODICE_ENTE, cdEnteOpt.get());
				}
				if (cdAreaOpt.isPresent()) {
	                  params.addValue(CODICE_AREA, cdAreaOpt.get());
				}
			}
			readFilterProperty(filter.getIdAmbito(), " AND cat.id_ambito = :id_ambito", ID_AMBITO, sb, params);
			readFilterProperty(filter.getCodiceAmbito(), " AND amb.codice_ambito = :codice_ambito", CODICE_AMBITO, sb, params);
			readFilterProperty(filter.getIdVisibilitaAmbito(), " AND amb.id_visibilita_ambito = :id_visibilita_ambito", ID_VISIBILITA_AMBITO, sb, params);
			if (filter.getIdFruitore().isPresent() && filter.getCodiceEnte().isEmpty()) {
				readFilterProperty(filter.getIdFruitore(), " AND EXISTS (SELECT 1"
						+ "	FROM moon_fo_r_area_modulo ram"
						+ "	INNER JOIN moon_wf_r_fruitore_ente fe ON fe.id_ente = ram.id_ente"
						+ "	WHERE ram.id_modulo = m.id_modulo"
						+ "	AND fe.id_fruitore = :id_fruitore)", ID_FRUITORE, sb, params);
			}
			if (filter.getAttributoPresente().isPresent()) {
				readFilterProperty(filter.getAttributoPresente(), " AND EXISTS (SELECT 1" + 
						" FROM moon_io_d_moduloattributi ma" + 
						" WHERE ma.id_modulo = m.id_modulo" + 
						" AND ma.nome_attributo = :nome_attributo" + 
						" AND ma.valore is not null AND length(ma.valore) > 0)", "nome_attributo", sb, params);
			}
		}
		return sb;
	}

	@Override
	public int update(ModuloEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO");
		}
	}
	
	
	@Override
	public int delete(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, id);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public int deleteCronologia(Long idVersioneModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_VERSIONE_MODULO, idVersioneModulo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_CRON, params);
			LOG.debug("[" + CLASS_NAME + "::deleteCronologia] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public int deleteVersione(Long idVersioneModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteVersione] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_VERSIONE_MODULO, idVersioneModulo);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_VERS, params);
			LOG.debug("[" + CLASS_NAME + "::deleteVersione] Record aggiornati: " + numRows);
			return numRows;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public List<ModuloCronologiaStatiEntity> findAllCronologia(Long idVersioneModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findAllCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (List<ModuloCronologiaStatiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_ALL_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class) );
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findAllCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public ModuloCronologiaStatiEntity findCurrentCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findCurrentCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_VERSIONE_MODULO, idVersioneModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_CURRENT_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findCurrentCronologia] Elemento non trovato per idVersioneModulo: " + idVersioneModulo, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findCurrentCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	@Override
	public ModuloCronologiaStatiEntity findLastCronologia(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastCronologia] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_versione_modulo", idVersioneModulo);
			return (ModuloCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_CRON_ID_VERS, params, BeanPropertyRowMapper.newInstance(ModuloCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato per idVersioneModulo: " + idVersioneModulo, emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Errore database per idVersioneModulo: " + idVersioneModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ModuloVersioneEntity findVersioneById(Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findVersioneById] IN idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_VERSIONE_MODULO, idVersioneModulo);
			return (ModuloVersioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_VERS_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloVersioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findVersioneById] Elemento non trovato per idVersioneModulo: "+idVersioneModulo,emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findVersioneById] Errore database per idVersioneModulo: "+idVersioneModulo,e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersioneEntity findVersione(Long idModulo, String versione) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findVersione] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, idModulo);
			params.addValue(VERSIONE_MODULO, versione);
			return (ModuloVersioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_VERS_BY_ID_MODULO_AND_VERSIONE, params, BeanPropertyRowMapper.newInstance(ModuloVersioneEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findVersione] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersionatoEntity findModuloVersionatoById(Long idModulo, Long idVersioneModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findModuloVersionatoById] IN idModulo: "+idModulo+"  idVersioneModulo: "+idVersioneModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, idModulo);
			params.addValue(ID_VERSIONE_MODULO, idVersioneModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoById] Elemento non trovato :: idModulo: "+idModulo+"  idVersioneModulo: "+idVersioneModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloVersionatoEntity findModuloVersionatoByCodice(String codiceModulo, String versione) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findModuloVersionatoByCodice] IN codiceModulo: "+codiceModulo+"  versione: "+versione);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_MODULO, codiceModulo);
			params.addValue(VERSIONE_MODULO, versione);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_CD, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoByCodice] Elemento non trovato: "+codiceModulo + " - " + versione);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoByCodice] Exception per: "+codiceModulo + " - " + versione, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	public ModuloVersionatoEntity findModuloVersionatoPubblicatoByCodice(String codiceModulo) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] IN codiceModulo: "+codiceModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_MODULO, codiceModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_CD_PUB, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] Elemento non trovato: "+codiceModulo);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoByCodice] Exception per: "+codiceModulo, e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ModuloVersionatoEntity findModuloVersionatoPubblicatoById(Long idModulo, Date inDataOra) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] IN idModulo: "+idModulo+"  inDataOra: "+inDataOra);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, idModulo);
			params.addValue("now", inDataOra, Types.TIMESTAMP);
			return (ModuloVersionatoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(SELECT_FIND + FROM_FIND + CURRENT_STATO + BY_ID_PUB, params, BeanPropertyRowMapper.newInstance(ModuloVersionatoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] Elemento non trovato :: idModulo: "+idModulo+"  inDataOra: "+inDataOra);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findModuloVersionatoPubblicatoById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public Long insertCronologia(ModuloCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] IN entity: "+entity);
			Long idCron = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_CRON, new MapSqlParameterSource() );
			entity.setIdCron(idCron);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CRON, mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] Record inseriti: " + numRecord);
			return idCron;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA CRONOLOGIA");
		}
	}

	@Override
	public Long insertVersione(ModuloVersioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertVersione] IN entity: "+entity);
			Long idVersioneModulo = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_VERS, new MapSqlParameterSource() );
			entity.setIdVersioneModulo(idVersioneModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_VERS, mapVersioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertVersione] Record inseriti: " + numRecord);
			return idVersioneModulo;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO VERSIONE");
		}
	}
	

	@Override
	public int updateCronologia(ModuloCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateCronologia] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_CRON, mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::updateCronologia] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO CRONOLOGIA");
		}
	}
	
	@Override
	public int updateVersione(ModuloVersioneEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateVersione] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_VERS, mapVersioneEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::updateVersione] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateVersione] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO VERSIONE");
		}
	}

	@Override
	public ModuloVersioneEntity findVersionePubblicatoByIdModulo(Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findVersionePubblicatoByIdModulo] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, idModulo);
			params.addValue("now", new Timestamp(System.currentTimeMillis()), Types.TIMESTAMP);
			return (ModuloVersioneEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_VERS_PUBB_BY_IDMOD, params, BeanPropertyRowMapper.newInstance(ModuloVersioneEntity.class)   );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findVersionePubblicatoByIdModulo] Elemento non trovato per idModulo: "+idModulo,emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findVersionePubblicatoByIdModulo] Errore database per idModulo: "+idModulo,e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
    private MapSqlParameterSource mapEntityParameters(ModuloEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue(ID_MODULO, entity.getIdModulo());
    	params.addValue(CODICE_MODULO, entity.getCodiceModulo(), Types.VARCHAR);
    	params.addValue(OGGETTO_MODULO, entity.getOggettoModulo(), Types.VARCHAR);
    	params.addValue(DESCRIZIONE_MODULO, entity.getDescrizioneModulo(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	params.addValue(FLAG_IS_RISERVATO, entity.getFlagIsRiservato(), Types.VARCHAR);
    	params.addValue(ID_TIPO_CODICE_ISTANZA, entity.getIdTipoCodiceIstanza(), Types.INTEGER);
    	params.addValue(FLAG_PROTOCOLLO_INTEGRATO, entity.getFlagProtocolloIntegrato(), Types.VARCHAR);
    	return params;
    }

    private MapSqlParameterSource mapCronologiaStatiEntityParameters(ModuloCronologiaStatiEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_cron", entity.getIdCron());
    	params.addValue(ID_VERSIONE_MODULO, entity.getIdVersioneModulo());
    	params.addValue("id_stato" , entity.getIdStato());
    	params.addValue("data_inizio_validita", entity.getDataInizioValidita(), Types.TIMESTAMP);
    	params.addValue("data_fine_validita", entity.getDataFineValidita(), Types.TIMESTAMP);
    	return params;
    }

    private MapSqlParameterSource mapVersioneEntityParameters(ModuloVersioneEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue(ID_VERSIONE_MODULO, entity.getIdVersioneModulo());
    	params.addValue(ID_MODULO , entity.getIdModulo());
    	params.addValue(VERSIONE_MODULO , entity.getVersioneModulo(), Types.VARCHAR);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }
    
    
    //
    //
    public void forceResetModuliCache() {
    	cache.clear();
    	lastResetCache = LocalTime.now();
    	LOG.info("[" + CLASS_NAME + "::forceResetModuliCache] BEGIN END");
    }
    public void initCache() {
    	if (Duration.between(lastResetCache, LocalTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetModuliCache();
    	}
    }

}
