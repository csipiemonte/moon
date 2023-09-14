/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.commons.entity.IstanzaApiEntity;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapperFactory;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class IstanzaDAOImpl extends JdbcTemplateDAO implements IstanzaDAO {

	private static final String CLASS_NAME = "IstanzaDAOImpl";

	private static final String ID_ISTANZA = "id_istanza";
	private static final String CODICE_ISTANZA = "codice_istanza";
	private static final String ID_MODULO = "id_modulo";
	private static final String ID_VERSIONE_MODULO = "id_versione_modulo";
	private static final String TITOLO_MODULO = "titolo_modulo";
	private static final String IDENTIFICATIVO_UTENTE = "identificativo_utente";
	private static final String CODICE_FISCALE_DICHIARANTE = "codice_fiscale_dichiarante";
	private static final String IMPORTANZA = "importanza";
	private static final String ID_TAG = "id_tag";
	private static final String GRUPPO_OPERATORE_FO = "gruppo_operatore_fo";
	private static final String ID_AMBITO = "id_ambito";
	private static final String ID_VISIBILITA_AMBITO = "id_visibilita_ambito";
	
	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT i.id_istanza, i.codice_istanza, i.id_modulo, i.identificativo_utente, i.codice_fiscale_dichiarante, i.cognome_dichiarante, i.nome_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, i.numero_protocollo, i.data_protocollo, i.current_step, i.id_versione_modulo, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi";
	
	private static final String FIND_SELECT_FIELDS_FOR_API = "SELECT i.codice_istanza, i.codice_fiscale_dichiarante, i.cognome_dichiarante, i.nome_dichiarante, i.data_creazione, sw.id_stato_wf_arrivo, sw.nome_stato_wf_arrivo";

	private static final String FIND_BY_ID = FIND_SELECT_FIELDS
			+ " FROM moon_fo_t_istanza i "
			+ " WHERE i.id_istanza = :id_istanza";

	private static final String FIND_BY_CD = FIND_SELECT_FIELDS
			+ " FROM moon_fo_t_istanza i "
			+ " WHERE i.codice_istanza = :codice_istanza";
	private static final String FIND_FROM = " FROM moon_fo_t_istanza i";

	private static final String COUNT = FIND_SELECT_COUNT + FIND_FROM;
	private static final String FIND = FIND_SELECT_FIELDS + FIND_FROM;

	private static final String FIND_LAST_CRON = "SELECT id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd"
			+ " FROM moon_fo_t_cronologia_stati WHERE id_istanza=:id_istanza AND data_fine IS NULL";
	private static final String FIND_DATI = "SELECT id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,id_tipo_modifica,id_stepcompilazione,data_upd,attore_upd"
			+ " FROM moon_fo_t_dati_istanza WHERE id_istanza=:id_istanza"
			+ " AND id_cronologia_stati=:id_cronologia_stati";

	private static final String INSERT = "INSERT INTO moon_fo_t_istanza(id_istanza, codice_istanza, id_modulo, identificativo_utente, codice_fiscale_dichiarante, cognome_dichiarante, nome_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata, fl_archiviata, fl_test, importanza, numero_protocollo, data_protocollo, current_step, id_versione_modulo, id_ente, hash_univocita, gruppo_operatore_fo, dati_aggiuntivi)"
			+ " VALUES (:id_istanza, :codice_istanza, :id_modulo, :identificativo_utente, :codice_fiscale_dichiarante, :cognome_dichiarante, :nome_dichiarante, :id_stato_wf, :data_creazione, :attore_ins, :attore_upd, :fl_eliminata, :fl_archiviata, :fl_test, :importanza, :numero_protocollo, :data_protocollo, :current_step, :id_versione_modulo, :id_ente, :hash_univocita, :gruppo_operatore_fo, :dati_aggiuntivi)";
	private static final String INSERT_CRON = "INSERT INTO moon_fo_t_cronologia_stati(id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd)"
			+ " VALUES (:id_cronologia_stati,:id_istanza,:id_stato_wf,:id_azione_svolta,:data_inizio,:data_fine,:attore_ins,:attore_upd)";
	private static final String INSERT_DATI = "INSERT INTO moon_fo_t_dati_istanza(id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,id_tipo_modifica,id_stepcompilazione,data_upd,attore_upd)"
			+ " VALUES (:id_dati_istanza,:id_cronologia_stati,:id_istanza,:dati_istanza,:id_tipo_modifica,:id_stepcompilazione,:data_upd,:attore_upd)";

	private static final String UPDATE = "UPDATE moon_fo_t_istanza"
			+ " SET codice_istanza=:codice_istanza, id_modulo=:id_modulo, identificativo_utente=:identificativo_utente, codice_fiscale_dichiarante=:codice_fiscale_dichiarante, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante, "
			+ " id_stato_wf=:id_stato_wf, data_creazione=:data_creazione, attore_ins=:attore_ins, attore_upd=:attore_upd, fl_eliminata=:fl_eliminata, fl_archiviata=:fl_archiviata, fl_test=:fl_test, importanza=:importanza, numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo, current_step=:current_step,"
			+ " id_versione_modulo=:id_versione_modulo, id_ente=:id_ente, hash_univocita=:hash_univocita, "
			+ " dati_aggiuntivi=:dati_aggiuntivi " 
			+ " WHERE id_istanza= :id_istanza";
	private static final String UPDATE_CRON = "UPDATE moon_fo_t_cronologia_stati"
			+ " SET id_istanza=:id_istanza,id_stato_wf=:id_stato_wf,id_azione_svolta=:id_azione_svolta,data_inizio=:data_inizio,data_fine=:data_fine,attore_ins=:attore_ins,attore_upd=:attore_upd"
			+ " WHERE id_cronologia_stati=:id_cronologia_stati";
	private static final String UPDATE_DATI = "UPDATE moon_fo_t_dati_istanza"
			+ " SET id_cronologia_stati=:id_cronologia_stati,id_istanza=:id_istanza,dati_istanza=:dati_istanza,id_tipo_modifica=:id_tipo_modifica,id_stepcompilazione=:id_stepcompilazione,data_upd=:data_upd,attore_upd=:attore_upd"
			+ " WHERE id_dati_istanza=:id_dati_istanza";

	private static final String FROM_WHERE_API = " FROM moon_fo_t_istanza i, moon_wf_t_storico_workflow sw"
			+ " WHERE i.id_modulo = :id_modulo"
			+ " and i.fl_eliminata='N'"
			+ " and sw.id_istanza = i.id_istanza"
			+ " and sw.id_stato_wf_arrivo = :id_stato_wf_arrivo"
			+ " and sw.data_fine is NULL";
	private static final String FIND_FOR_API = "select i.codice_istanza" + FROM_WHERE_API;
	private static final String FIND_ENTITY_FOR_API = FIND_SELECT_FIELDS_FOR_API + FROM_WHERE_API;
	private static final String COUNT_FOR_API = "select count(i.codice_istanza)" + FROM_WHERE_API;

	private static final String UPDATE_PROTOCOLLO = "UPDATE moon_fo_t_istanza"
			+ " SET numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo"
			+ " WHERE id_istanza= :id_istanza";
	private static final String UPDATE_HASH_UNIVOCITA = "UPDATE moon_fo_t_istanza"
			+ " SET hash_univocita=:hash_univocita"
			+ " WHERE id_istanza= :id_istanza";
																			   
	private static final String UPDATE_ONLY_STATO = "UPDATE moon_fo_t_istanza" +
			" SET id_stato_wf=:id_stato_wf " +
			" WHERE id_istanza= :id_istanza";
	private static final String UPDATE_ONLY_DICHIARANTE = "UPDATE moon_fo_t_istanza" + 
			" SET codice_fiscale_dichiarante=:codice_fiscale_dichiarante, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante " +
			" WHERE id_istanza= :id_istanza";
	
	private static final String UPDATE_UUID_INDEX = "UPDATE moon_fo_t_istanza"
			+ " SET uuid_index=:uuid_index"
			+ " WHERE id_istanza= :id_istanza";
	
      

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_istanza_id_istanza_seq')";
	private static final String SEQ_ID_CRON = "SELECT nextval('moon_fo_t_cronologia_stati_id_cronologia_stati_seq')";
	private static final String SEQ_ID_DATI = "SELECT nextval('moon_fo_t_dati_istanza_id_dati_istanza_seq')";
	
	
	public enum ORDINAMENTO {
		ASC("ASC"),
		DESC("DESC");
		public final String value;
		private ORDINAMENTO(String value) {
		        this.value = value;
		}
	};
		

	@Override
	public IstanzaEntity findById(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id: " + id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", id);
			return (IstanzaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params,
					BeanPropertyRowMapper.newInstance(IstanzaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public IstanzaEntity findByCd(String codiceIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCd] IN codiceIstanza: " + codiceIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(CODICE_ISTANZA, codiceIstanza);
			return (IstanzaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_CD, params,
					BeanPropertyRowMapper.newInstance(IstanzaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCd] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCd] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public IstanzaCronologiaStatiEntity findLastCronologia(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastCronologia] IN idIstanza: " + idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaCronologiaStatiEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_LAST_CRON, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato: " + emptyEx.getMessage(),
					emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public IstanzaDatiEntity findDati(Long idIstanza, Long idCronologiaStati) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findDati] IN idIstanza: " + idIstanza);
			LOG.debug("[" + CLASS_NAME + "::findDati] IN idCronologiaStati: " + idCronologiaStati);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_cronologia_stati", idCronologiaStati);
			return (IstanzaDatiEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_DATI, params,
					BeanPropertyRowMapper.newInstance(IstanzaDatiEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findDati] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findDati] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<IstanzaEntity> find(IstanzeFilter filter, Optional<IstanzeSorter> sorter) throws DAOException {
		List<IstanzaEntity> result = null;
		LOG.debug("[" + CLASS_NAME + "::find] IN filter: " + filter);

		StringBuilder sb = new StringBuilder(FIND);
		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
//		sb.append(readFilter(filter, params));
		sb = readFilter(filter, sb, params);

		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		} else {
			// Ordinamento di default, se non presente ; serve sopratutto nel caso di
			// paginazione
			sb.append(" ORDER BY i.id_istanza DESC");
		}

		// Paginazione
		if (filter != null && filter.isUsePagination()) {
			sb.append(" LIMIT " + filter.getLimit() + " OFFSET " + filter.getOffset());
		}

		LOG.debug("[" + CLASS_NAME + "::find] params: " + params);
		result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params,
				BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}

	@Override
	public Integer count(IstanzeFilter filter) throws DAOException {
		Integer result = null;
		LOG.debug("[" + CLASS_NAME + "::count] IN filter: " + filter);

		StringBuilder sb = new StringBuilder(COUNT);
		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
//		sb.append(readFilter(filter, params));
		sb = readFilter(filter, sb, params);

		LOG.debug("[" + CLASS_NAME + "::count] params: " + params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}

	private StringBuilder readFilter(IstanzeFilter filter, StringBuilder sbSelectFrom, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		boolean sbSelectFromAdded = false;
		if (filter != null) {
			readFilterPropertyWa(filter.getIdIstanza() , "i.id_istanza = :id_istanza", ID_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getCodiceIstanza(), "i.codice_istanza = :codice_istanza", CODICE_ISTANZA, sb, params);
			readFilterPropertyWa(filter.getIdModulo(), "i.id_modulo = :id_modulo", ID_MODULO, sb, params);
			readFilterPropertyWa(filter.getIdVersioneModulo(), "i.id_versione_modulo = :id_versione_modulo", ID_VERSIONE_MODULO, sb, params);
			readFilterPropertyContainsWa(filter.getTitoloModulo(), "i.id_modulo IN (SELECT id_modulo FROM moon_io_d_modulo WHERE oggetto_modulo LIKE :titolo_modulo OR descrizione_modulo LIKE :titolo_modulo)", TITOLO_MODULO, sb, params);
			readFilterPropertyWa(filter.getIdentificativoUtente(), "i.identificativo_utente = :identificativo_utente", IDENTIFICATIVO_UTENTE, sb, params);
			readFilterPropertyWa(filter.getCodiceFiscaleDichiarante(), "i.codice_fiscale_dichiarante = :codice_fiscale_dichiarante", CODICE_FISCALE_DICHIARANTE, sb, params);
			readFilterPropertyWa(filter.getStatiIstanza(), "i.id_stato_wf IN (:statiFilter)", "statiFilter", sb, params);
			readFilterPropertyWa(filter.getNotStatiFo(), "i.id_stato_wf NOT IN (:notStatiFo)", "notStatiFo", sb, params);

			Optional<List<Integer>> lstIntOpt = filter.getStatiIstanza();
			lstIntOpt = filter.getStatiBo();
			if (lstIntOpt.isPresent()) {
				sbSelectFrom.append(", moon_wf_t_storico_workflow sw WHERE sw.id_istanza = i.id_istanza and sw.data_fine is NULL AND sw.id_stato_wf_arrivo IN (:statiBo)");
				params.addValue("statiBo", lstIntOpt.get());
				sbSelectFromAdded = true;
			}
			lstIntOpt = filter.getNotStatiBo();
			if (lstIntOpt.isPresent()) {
				sbSelectFrom.append(", moon_wf_t_storico_workflow sw WHERE sw.id_istanza = i.id_istanza and sw.data_fine is NULL AND sw.id_stato_wf_arrivo NOT IN (:notStatiBo)");
				params.addValue("notStatiBo", lstIntOpt.get());
				sbSelectFromAdded = true;
			}
			readFilterPropertyWa(filter.getImportanza(), "i.importanza = :importanza", IMPORTANZA, sb, params);
			if (!IstanzeFilter.EnumFilterFlagEliminata.TUTTI.equals(filter.getFlagEliminata())) {
				appendWhereOrAnd(sb)
					.append("i.fl_eliminata = :fl_eliminata ");
				params.addValue("fl_eliminata",
					IstanzeFilter.EnumFilterFlagEliminata.ELIMINATI.equals(filter.getFlagEliminata())?'S':'N');
			}
			if (!IstanzeFilter.EnumFilterFlagArchiviata.TUTTI.equals(filter.getFlagArchiviata())) {
				appendWhereOrAnd(sb)
					.append("i.fl_archiviata = :fl_archiviata ");
				params.addValue("fl_archiviata",
					IstanzeFilter.EnumFilterFlagArchiviata.ARCHIVIATI.equals(filter.getFlagArchiviata())?'S':'N');
			}
			if (!IstanzeFilter.EnumFilterFlagTest.TUTTI.equals(filter.getFlagTest())) {
				appendWhereOrAnd(sb)
					.append("i.fl_test = :fl_test ");
				params.addValue("fl_test",
					IstanzeFilter.EnumFilterFlagTest.TEST.equals(filter.getFlagTest())?'S':'N');
			}
			readFilterPropertyWa(filter.getCreatedStart(), "i.data_creazione >= :data_creazione_start", "data_creazione_start", sb, params);
			Optional<Date> dtOpt = filter.getCreatedEnd();
			if (dtOpt.isPresent()) {
				Date createdEnd = Date.from(dtOpt.get().toInstant().plus(1, ChronoUnit.DAYS));
				appendWhereOrAnd(sb)
					.append("i.data_creazione < :data_creazione_end ");
				params.addValue("data_creazione_end", createdEnd);
			}
			Optional<Long> idEnteOpt = filter.getIdEnte();
			Optional<Long> idAreaOpt = filter.getIdArea();
			if (idEnteOpt.isPresent() || idAreaOpt.isPresent()) {
				appendWhereOrAnd(sb)
					.append(" EXISTS (SELECT 1 FROM moon_fo_r_area_modulo ram" + 
						" WHERE ram.id_modulo = i.id_modulo" + 
						(idEnteOpt.isPresent()?" AND ram.id_ente = :id_ente":"") +
						(idAreaOpt.isPresent()?" AND ram.id_area = :id_area":"") +
						")");				
				if (idEnteOpt.isPresent()) {
	                  params.addValue("id_ente", idEnteOpt.get());
				}
				if (idAreaOpt.isPresent()) {
	                  params.addValue("id_area", idAreaOpt.get());
				}
			}
			readFilterPropertyWa(filter.getIdTag(), "i.id_istanza IN (SELECT id_istanza FROM moon_fo_r_tag_istanza WHERE id_tag=:id_tag)", ID_TAG, sb, params);
			readFilterPropertyWa(filter.getGruppoOperatoreFo(), "i.gruppo_operatore_fo = :gruppo_operatore_fo", GRUPPO_OPERATORE_FO, sb, params);
			readFilterPropertyWa(filter.getIdAmbito(), "i.id_modulo IN (SELECT catmod.id_modulo" +
					" FROM moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod" +
					" WHERE cat.id_ambito = :id_ambito AND catmod.id_categoria = cat.id_categoria)", ID_AMBITO, sb, params);
			readFilterPropertyWa(filter.getIdVisibilitaAmbito(), "i.id_modulo IN (SELECT catmod.id_modulo" +
					" FROM moon_fo_d_ambito amb, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod" +
					" WHERE amb.id_visibilita_ambito = :id_visibilita_ambito AND cat.id_ambito = amb.id_ambito AND catmod.id_categoria = cat.id_categoria)", ID_VISIBILITA_AMBITO, sb, params);
		}
		
		if(sbSelectFromAdded && sb.length()>0) {
			return sbSelectFrom.append(" AND ").append(sb);
		}
		return sbSelectFrom.append(sbSelectFromAdded?"":" WHERE ").append(sb);
	}
	
	@Override
	protected StringBuilder appendWhereOrAnd(StringBuilder sb) {
		return sb.append(sb.length()==0?"":" AND ");
	}

	
	@Override
	public Long insert(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource());
			entity.setIdIstanza(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}

	@Override
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] IN entity: " + entity);
			Long idCron = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_CRON,
					new MapSqlParameterSource());
			entity.setIdCronologiaStati(idCron);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CRON,
					mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] Record inseriti: " + numRecord);
			return idCron;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertCronologia] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA CRONOLOGIA");
		}
	}

	@Override
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertDati] IN entity: " + entity);
			Long idDati = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_DATI,
					new MapSqlParameterSource());
			entity.setIdDatiIstanza(idDati);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_DATI,
					mapDatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertDati] Record inseriti: " + numRecord);
			return idDati;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertDati] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA DATI");
		}
	}

	@Override
	public int update(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}

	@Override
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateCronologia] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_CRON,
					mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::updateCronologia] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateCronologia] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA CRONOLOGIA");
		}
	}

	@Override
	public int updateDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDati] IN entity: " + entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_DATI,
					mapDatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::updateDati] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateDati] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA DATI");
		}
	}

	@Override
	public int updateProtocollo(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateProtocollo] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_istanza", entity.getIdIstanza());
	    	params.addValue("numero_protocollo", entity.getNumeroProtocollo());
	    	params.addValue("data_protocollo", entity.getDataProtocollo());

			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_PROTOCOLLO, params);
			LOG.debug("[" + CLASS_NAME + "::updateProtocollo] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateProtocollo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO PROTOCOLLO ISTANZA");
		}
	}
	
	@Override
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateStato] IN idIstanza: "+idIstanza+" TO stato="+idStatoWf);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_stato_wf", idStatoWf);
	    	params.addValue("id_istanza", idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ONLY_STATO, params);
			LOG.debug("[" + CLASS_NAME + "::updateStato] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateStato] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO STATO ISTANZA");
		}
	}

//	@Override
//	public int delete(Long id) throws DAOException {
//		try {
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			params.addValue("id_modulo", id);
//			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
//			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
//			return numRows;
//		} 
//		catch (Exception e) {
//			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
//			throw new DAOException(getMsgErrDefault());
//		}
//	}
	

	@Override
	public List<IstanzaApiEntity> findForApi(Integer idFruitore, Long idModulo, Integer idStato, Long idVersioneModulo,
			Long idEnte, String identificativoUtente, Date dataDa, Date dataA, String ordinamento, boolean test)
			throws DAOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idFruitore: " + idFruitore);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idModulo: " + idModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idStato: " + idStato);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idVersioneModulo: " + idVersioneModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idEnte: " + idEnte);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN identificativoUtente: " + identificativoUtente);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataDa: " + dataDa);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataA: " + dataA);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN ordinamento: " + ordinamento);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN test: " + test);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_MODULO, idModulo);
		params.addValue("id_stato_wf_arrivo", idStato);

		StringBuilder sqlFindForApi = new StringBuilder();
		sqlFindForApi.append(FIND_ENTITY_FOR_API);
		sqlFindForApi.append(
				readFilterApi(idFruitore, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, test, params));

		if (ordinamento == null || !(ordinamento.toUpperCase().equals(ORDINAMENTO.ASC.value)
				|| ordinamento.toUpperCase().equals(ORDINAMENTO.DESC.value))) {
			ordinamento = ORDINAMENTO.DESC.value;
		}

		sqlFindForApi.append(" ORDER BY i.data_creazione " + ordinamento);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForApi] sqlFindForApi: " + sqlFindForApi.toString());
		}
			
		return (List<IstanzaApiEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sqlFindForApi.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaApiEntity.class));
	}


	
	@Override
	public List<String> findForApi(Integer idFruitore, Long idModulo, Integer idStato, Long idVersioneModulo, Long idEnte, String identificativoUtente, Date dataDa, Date dataA, boolean test) throws DAOException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idFruitore: " + idFruitore);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idModulo: " + idModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idStato: " + idStato);		
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idVersioneModulo: " + idVersioneModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idEnte: " + idEnte);																								  
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN identificativoUtente: " + identificativoUtente);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataDa: " + dataDa);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataA: " + dataA);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN test: " + test);
		}	
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_MODULO, idModulo);
		params.addValue("id_stato_wf_arrivo", idStato);

		StringBuilder sqlFindForApi = new StringBuilder();
		sqlFindForApi.append(FIND_FOR_API);
		sqlFindForApi.append(readFilterApi(idFruitore, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, test, params));
		sqlFindForApi.append(" ORDER BY i.data_creazione DESC");
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForApi] sqlFindForApi: " + sqlFindForApi.toString());
		}
		return getCustomNamedParameterJdbcTemplateImpl().queryForList(sqlFindForApi.toString(), params, String.class);
	}


	private StringBuilder readFilterApi(Integer idFruitore, Long idVersioneModulo, Long idEnte, String identificativoUtente, Date dataDa, Date dataA, boolean test, MapSqlParameterSource params) {
		StringBuilder sqlFindForApi = new StringBuilder();
		// Range Date
		if (!StringUtils.isEmpty(dataDa) && StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa ");
			params.addValue("dataDa", dataDa);			
		} else if (!StringUtils.isEmpty(dataA) && StringUtils.isEmpty(dataDa)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione <= :dataA ");
			params.addValue("dataA", dataA);
		} else if (!StringUtils.isEmpty(dataDa) && !StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa and i.data_creazione <= :dataA ");
			params.addValue("dataDa", dataDa);
			params.addValue("dataA", dataA);
		}
		// VersionModulo
		if (idVersioneModulo != null) {
			sqlFindForApi = sqlFindForApi.append(" and i.id_versione_modulo = :idVersioneModulo");
			params.addValue("idVersioneModulo", idVersioneModulo);
		}
		// Ente (o Visibilita del fruitore)
		if (idEnte != null) {
			sqlFindForApi = sqlFindForApi.append(" and i.id_ente = :idEnte");
			params.addValue("idEnte", idEnte);
		} else {
			sqlFindForApi = sqlFindForApi.append(" and i.id_ente IN (SELECT fre.id_ente FROM moon_wf_r_fruitore_ente fre WHERE fre.id_fruitore=:idFruitore)");
			params.addValue("idFruitore", idFruitore);
		}
		// Test
		sqlFindForApi = sqlFindForApi.append(" and i.fl_test = :fl_test");
		params.addValue("fl_test", test?"S":"N");
						 																																	  
		// identificativoUtente
		if (identificativoUtente != null) {
			sqlFindForApi = sqlFindForApi.append(" and i.identificativo_utente = :identificativo_utente");
			params.addValue(IDENTIFICATIVO_UTENTE, identificativoUtente);
		}
		return sqlFindForApi;
	}
	
	private StringBuilder readFilterApiReport(Long idModulo, Date dataDa, Date dataA, MapSqlParameterSource params) {
		StringBuilder sqlFindForApi = new StringBuilder();
		
		//idModulo
		if (idModulo != null) {
			sqlFindForApi = sqlFindForApi.append(" and i.id_modulo = :idModulo");
			params.addValue("idModulo", idModulo);
		}
		
		// Range Date
		if (!StringUtils.isEmpty(dataDa) && StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa ");
			params.addValue("dataDa", dataDa);			
		} else if (!StringUtils.isEmpty(dataA) && StringUtils.isEmpty(dataDa)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione <= :dataA ");
			params.addValue("dataA", dataA);
		} else if (!StringUtils.isEmpty(dataDa) && !StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa and i.data_creazione <= :dataA ");
			params.addValue("dataDa", dataDa);
			params.addValue("dataA", dataA);
		}

		return sqlFindForApi;
	}
	
						
	
	@Override
	public List<String> findForApi(Integer idFruitore, Long idModulo, Integer idStato, Long idVersioneModulo, Long idEnte,String identificativoUtente,Date dataDa, Date dataA, boolean test, Integer offset, Integer limit) throws DAOException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idFruitore: " + idFruitore);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idModulo: " + idModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idStato: " + idStato);		
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idVersioneModulo: " + idVersioneModulo);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN idEnte: " + idEnte);																								   
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN identificativoUtente: " + identificativoUtente);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataDa: " + dataDa);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN dataA: " + dataA);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN test: " + test);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN offset: " + offset);
			LOG.debug("[" + CLASS_NAME + "::findForApi] IN limit: " + limit);
		}
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_MODULO, idModulo);
		params.addValue("id_stato_wf_arrivo", idStato);

		StringBuilder sqlFindForApi = new StringBuilder();
		sqlFindForApi.append(FIND_FOR_API);
		sqlFindForApi.append(readFilterApi(idFruitore, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, test, params));		
		// Paginazione
		if (limit >0  && offset >=0) {
			sqlFindForApi = sqlFindForApi.append(" order by i.data_creazione DESC");
			sqlFindForApi = sqlFindForApi.append(" LIMIT "+limit+" OFFSET "+offset);
		}

		return getCustomNamedParameterJdbcTemplateImpl().queryForList(sqlFindForApi.toString(), params, String.class);
	}
	
	public enum DATEDAA {DA,A,RANGE,NOW};
	
	
	
	@Override
	public Integer countForApi(Integer idFruitore, Long idModulo, Integer idStato, Long idVersioneModulo, Long idEnte, String identificativoUtente, Date dataDa, Date dataA, boolean test) throws DAOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN idFruitore: " + idFruitore);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN idModulo: " + idModulo);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN idStato: " + idStato);		
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN idVersioneModulo: " + idVersioneModulo);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN idEnte: " + idEnte);																								  
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN identificativoUtente: " + identificativoUtente);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN dataDa: " + dataDa);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN dataA: " + dataA);
			LOG.debug("[" + CLASS_NAME + "::countForApi] IN test: " + test);
		}
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue(ID_MODULO, idModulo);
		params.addValue("id_stato_wf_arrivo", idStato);

		StringBuilder sqlCountForApi = new StringBuilder(COUNT_FOR_API);
		sqlCountForApi.append(readFilterApi(idFruitore, idVersioneModulo, idEnte, identificativoUtente, dataDa, dataA, test, params));	
		
		return getCustomNamedParameterJdbcTemplateImpl().queryForInt(sqlCountForApi.toString(), params);

	}	

	
	@Override
	public int updateProtocollo(Long idIstanza, String numeroProtocollo, Date dataProtocollo) {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateProtocollo] IN idIstanza: " + idIstanza + "  numeroProtocollo: " + numeroProtocollo + "  dataProtocollo: " + dataProtocollo );
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("numero_protocollo", numeroProtocollo, Types.VARCHAR);
			params.addValue("data_protocollo", dataProtocollo, Types.TIMESTAMP);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_PROTOCOLLO, params);
			LOG.debug("[" + CLASS_NAME + "::updateProtocollo] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateProtocollo] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}
	
	@Override
	public int updateHashUnivocita(Long idIstanza, String hashUnivocita) {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateHashUnivocita] IN idIstanza: " + idIstanza + "  hashUnivocita: " + hashUnivocita );
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("hash_univocita", hashUnivocita, Types.VARCHAR);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_HASH_UNIVOCITA, params);
			LOG.debug("[" + CLASS_NAME + "::updateHashUnivocita] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateHashUnivocita] Errore database: " + e.getMessage(), e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}
	
	@Override
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome) {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDichiarante] IN idIstanza: "+idIstanza+"  with CF="+codiceFiscale+"  cognome="+cognome+"  nome="+nome);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue(CODICE_FISCALE_DICHIARANTE, codiceFiscale);
	    	params.addValue("cognome_dichiarante", cognome);
	    	params.addValue("nome_dichiarante", nome);
	    	params.addValue("id_istanza", idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ONLY_DICHIARANTE, params);
			LOG.debug("[" + CLASS_NAME + "::updateDichiarante] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateDichiarante] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO DICHIARANTE ISTANZA");
		}
	}
	
	@Override
	public Integer countIstanzeByModuloHash(Long idModulo, String hash) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] IN idModulo: "+idModulo+"   hash: "+hash);
			String sql = "SELECT COUNT(*) FROM moon_fo_t_istanza WHERE id_modulo=:id_modulo AND hash_univocita=:hash_univocita AND fl_eliminata='N' AND fl_archiviata='N'";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue(ID_MODULO, idModulo);
			params.addValue("hash_univocita", hash, Types.VARCHAR);
			Integer result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sql, params);
			LOG.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] OUT result: "+result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countIstanzeByModuloHash] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
//	id_istanza, codice_istanza, id_modulo, codice_fiscale_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata
	private MapSqlParameterSource mapEntityParameters(IstanzaEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_istanza", entity.getIdIstanza());
		params.addValue(CODICE_ISTANZA, entity.getCodiceIstanza(), Types.VARCHAR);
		params.addValue(ID_MODULO, entity.getIdModulo());
		params.addValue(IDENTIFICATIVO_UTENTE, entity.getIdentificativoUtente(), Types.VARCHAR);
		params.addValue(CODICE_FISCALE_DICHIARANTE, entity.getCodiceFiscaleDichiarante(), Types.VARCHAR);
    	params.addValue("cognome_dichiarante", entity.getCognomeDichiarante(), Types.VARCHAR);
    	params.addValue("nome_dichiarante", entity.getNomeDichiarante(), Types.VARCHAR);
		params.addValue("id_stato_wf", entity.getIdStatoWf());
		params.addValue("data_creazione", entity.getDataCreazione(), Types.TIMESTAMP);
		params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		params.addValue("fl_eliminata", entity.getFlagEliminata(), Types.VARCHAR);
		params.addValue("fl_archiviata", entity.getFlagArchiviata(), Types.VARCHAR);
		params.addValue("fl_test", entity.getFlagTest(), Types.VARCHAR);
		params.addValue(IMPORTANZA, entity.getImportanza(), Types.INTEGER);
		params.addValue("numero_protocollo", entity.getNumeroProtocollo(), Types.VARCHAR);
		params.addValue("data_protocollo", entity.getDataProtocollo(), Types.TIMESTAMP);
		params.addValue("current_step", entity.getCurrentStep(), Types.INTEGER);
		params.addValue(ID_VERSIONE_MODULO, entity.getIdVersioneModulo(), Types.INTEGER);
		params.addValue("id_ente", entity.getIdEnte(), Types.INTEGER);
		params.addValue("hash_univocita", entity.getHashUnivocita(), Types.VARCHAR);
		params.addValue(GRUPPO_OPERATORE_FO, entity.getGruppoOperatoreFo(), Types.VARCHAR);
		params.addValue("dati_aggiuntivi", entity.getDatiAggiuntivi(), Types.VARCHAR);
		return params;
	}

//	id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd
	private MapSqlParameterSource mapCronologiaStatiEntityParameters(IstanzaCronologiaStatiEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_cronologia_stati", entity.getIdCronologiaStati());
		params.addValue("id_istanza", entity.getIdIstanza());
		params.addValue("id_stato_wf", entity.getIdStatoWf());
		params.addValue("id_azione_svolta", entity.getIdAzioneSvolta());
		params.addValue("data_inizio", entity.getDataInizio(), Types.TIMESTAMP);
		params.addValue("data_fine", entity.getDataFine(), Types.TIMESTAMP);
		params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}

//	id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,cod_tipo_modifica_dati,id_stepcompilazione,data_upd,attore_upd
	private MapSqlParameterSource mapDatiEntityParameters(IstanzaDatiEntity entity) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_dati_istanza", entity.getIdDatiIstanza());
		params.addValue("id_cronologia_stati", entity.getIdCronologiaStati());
		params.addValue("id_istanza", entity.getIdIstanza());
		params.addValue("dati_istanza", entity.getDatiIstanza(), Types.VARCHAR);
		params.addValue("id_tipo_modifica", entity.getIdTipoModifica());
		params.addValue("id_stepcompilazione", entity.getIdStepCompilazione());
		params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
		params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
		return params;
	}



	@Override
	public int updateUuidIndex(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateUuidIndex] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_istanza", entity.getIdIstanza());
	    	params.addValue("uuid_index", entity.getUuidIndex());
	    	
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_UUID_INDEX, params);
			LOG.debug("[" + CLASS_NAME + "::updateUuidIndex] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::updateUuidIndex] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO UUID_INDEX ISTANZA");
		}
	}



}
