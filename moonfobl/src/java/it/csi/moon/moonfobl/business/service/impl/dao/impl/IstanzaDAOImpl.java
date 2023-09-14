/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.IstanzeFilter;
import it.csi.moon.commons.entity.IstanzeSorter;
import it.csi.moon.moonfobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 * 
 * @author Laurent Pissard
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
	private static final String COGNOME_DICHIARANTE = "cognome_dichiarante";
	private static final String NOME_DICHIARANTE = "nome_dichiarante";
	private static final String IMPORTANZA = "importanza";
	private static final String ID_TAG = "id_tag";
	private static final String GRUPPO_OPERATORE_FO = "gruppo_operatore_fo";
	private static final String ID_AMBITO = "id_ambito";
	private static final String ID_VISIBILITA_AMBITO = "id_visibilita_ambito";
	
	private static final String FIND_SELECT_COUNT = "SELECT count(*)";
	private static final String FIND_SELECT_FIELDS = "SELECT i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente," +
		" i.codice_fiscale_dichiarante, i.nome_dichiarante, i.cognome_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins," +
		" i.attore_upd, i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza," +
		" i.numero_protocollo, i.data_protocollo, i.current_step, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi";
	
	private static final String FIND_BY_ID = FIND_SELECT_FIELDS +
		" FROM moon_fo_t_istanza i " +
		" WHERE i.id_istanza = :id_istanza";
	
	private static final String FIND_BY_CD = FIND_SELECT_FIELDS +
		" FROM moon_fo_t_istanza i " +
		" WHERE i.codice_istanza = :codice_istanza";
	
	private static final String FIND_FROM = 
		" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_wf_d_stato s" +
		" WHERE cron.id_istanza = i.id_istanza" + 
		" AND cron.data_fine IS NULL" +
		" AND s.id_stato_wf=cron.id_stato_wf";
	
	private static final String FIND_FROM_JSON = 
		" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_wf_d_stato s, moon_fo_t_dati_istanza dat" +
		" WHERE cron.id_istanza = i.id_istanza" + 
		" AND cron.data_fine IS NULL" +
		" AND cron.id_cronologia_stati = dat.id_cronologia_stati"+
		" AND dat.id_istanza = i.id_istanza" +
		" AND s.id_stato_wf=cron.id_stato_wf";
	
	private static final String COUNT = FIND_SELECT_COUNT + FIND_FROM;
	private static final String COUNT_JSON = FIND_SELECT_COUNT + FIND_FROM_JSON;
	private static final String FIND = FIND_SELECT_FIELDS + FIND_FROM;
	private static final String FIND_JSON = FIND_SELECT_FIELDS + FIND_FROM_JSON;
	
	private static final String FIND_LAST_CRON = "SELECT id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd" +
		" FROM moon_fo_t_cronologia_stati" +
		" WHERE id_istanza=:id_istanza" + 
		" AND data_fine IS NULL";
	private static final String FIND_DATI = "SELECT id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,id_tipo_modifica,id_stepcompilazione,data_upd,attore_upd" +
		" FROM moon_fo_t_dati_istanza" +
		" WHERE id_istanza=:id_istanza" + 
		" AND id_cronologia_stati=:id_cronologia_stati";

	private static final String FIND_LASTCRON_DATI = "SELECT dat.id_dati_istanza, dat.id_cronologia_stati, "
		+ "dat.id_istanza, dat.dati_istanza, dat.id_tipo_modifica, "
		+ "dat.id_stepcompilazione, dat.data_upd, dat.attore_upd" +
		" FROM moon_fo_t_dati_istanza dat, moon_fo_t_cronologia_stati cron" +
		" WHERE dat.id_istanza=:id_istanza AND dat.id_cronologia_stati=cron.id_cronologia_stati" + 
		" AND data_fine IS NULL";

	private static final String FIND_INVIO = "SELECT id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd" +
		" FROM moon_fo_t_cronologia_stati" +
		" WHERE id_istanza=:id_istanza" + 
		" AND id_stato_wf >=2 AND data_fine IS NULL";

	private static final String INSERT = "INSERT INTO moon_fo_t_istanza(id_istanza, codice_istanza, id_modulo, identificativo_utente, codice_fiscale_dichiarante, cognome_dichiarante, nome_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata, fl_archiviata, fl_test, importanza, numero_protocollo, data_protocollo, current_step, id_versione_modulo, id_ente, hash_univocita, gruppo_operatore_fo, dati_aggiuntivi)"
			+ " VALUES (:id_istanza, :codice_istanza, :id_modulo, :identificativo_utente, :codice_fiscale_dichiarante, :cognome_dichiarante, :nome_dichiarante, :id_stato_wf, :data_creazione, :attore_ins, :attore_upd, :fl_eliminata, :fl_archiviata, :fl_test, :importanza, :numero_protocollo, :data_protocollo, :current_step, :id_versione_modulo, :id_ente, :hash_univocita, :gruppo_operatore_fo, :dati_aggiuntivi)";

	private static final String INSERT_CRON = "INSERT INTO moon_fo_t_cronologia_stati(id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd)" +
		" VALUES (:id_cronologia_stati,:id_istanza,:id_stato_wf,:id_azione_svolta,:data_inizio,:data_fine,:attore_ins,:attore_upd)";
	private static final String INSERT_DATI = "INSERT INTO moon_fo_t_dati_istanza(id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,id_tipo_modifica,id_stepcompilazione,data_upd,attore_upd)" +
		" VALUES (:id_dati_istanza,:id_cronologia_stati,:id_istanza,:dati_istanza,:id_tipo_modifica,:id_stepcompilazione,:data_upd,:attore_upd)";
	
	private static final String UPDATE = "UPDATE moon_fo_t_istanza" + 
		" SET codice_istanza=:codice_istanza, id_modulo=:id_modulo, codice_fiscale_dichiarante=:codice_fiscale_dichiarante, identificativo_utente=:identificativo_utente, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante, " +
		" id_stato_wf=:id_stato_wf, data_creazione=:data_creazione, attore_ins=:attore_ins, attore_upd=:attore_upd, " +
		" fl_eliminata=:fl_eliminata, fl_archiviata=:fl_archiviata, fl_test=:fl_test, importanza=:importanza, numero_protocollo=:numero_protocollo, " +
		" data_protocollo=:data_protocollo, current_step=:current_step, " + 
		" id_versione_modulo=:id_versione_modulo, id_ente=:id_ente, hash_univocita=:hash_univocita," +
		" dati_aggiuntivi=:dati_aggiuntivi " +
		" WHERE id_istanza= :id_istanza";
	
	private static final String UPDATE_CRON = "UPDATE moon_fo_t_cronologia_stati" + 
		" SET id_istanza=:id_istanza,id_stato_wf=:id_stato_wf,id_azione_svolta=:id_azione_svolta,data_inizio=:data_inizio,data_fine=:data_fine,attore_ins=:attore_ins,attore_upd=:attore_upd" +
		" WHERE id_cronologia_stati=:id_cronologia_stati";
	private static final String UPDATE_DATI = "UPDATE moon_fo_t_dati_istanza" + 
		" SET id_cronologia_stati=:id_cronologia_stati,id_istanza=:id_istanza,dati_istanza=:dati_istanza,id_tipo_modifica=:id_tipo_modifica,id_stepcompilazione=:id_stepcompilazione,data_upd=:data_upd,attore_upd=:attore_upd" +
		" WHERE id_dati_istanza=:id_dati_istanza";
	private static final String UPDATE_ONLY_STATO = "UPDATE moon_fo_t_istanza" + 
		" SET id_stato_wf=:id_stato_wf " +
		" WHERE id_istanza= :id_istanza";
	private static final String UPDATE_ONLY_DICHIARANTE = "UPDATE moon_fo_t_istanza" + 
		" SET codice_fiscale_dichiarante=:codice_fiscale_dichiarante, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante " +
		" WHERE id_istanza= :id_istanza";
	
	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_istanza_id_istanza_seq')";
	private static final String SEQ_ID_CRON = "SELECT nextval('moon_fo_t_cronologia_stati_id_cronologia_stati_seq')";
	private static final String SEQ_ID_DATI = "SELECT nextval('moon_fo_t_dati_istanza_id_dati_istanza_seq')";
	
	
	@Override
	public IstanzaEntity findById(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", id);
			return (IstanzaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public IstanzaEntity findByCd(String codiceIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCd] IN codiceIstanza: " + codiceIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istanza", codiceIstanza);
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
			LOG.debug("[" + CLASS_NAME + "::findLastCronologia] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_CRON, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
	@Override
	public IstanzaDatiEntity findDati(Long idIstanza, Long idCronologiaStati) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findDati] IN idIstanza: "+idIstanza);
			LOG.debug("[" + CLASS_NAME + "::findDati] IN idCronologiaStati: "+idCronologiaStati);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_cronologia_stati", idCronologiaStati);
			return (IstanzaDatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_DATI, params, BeanPropertyRowMapper.newInstance(IstanzaDatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findDati] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findDati] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public IstanzaDatiEntity findLastCronDati(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastCronDati] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaDatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LASTCRON_DATI, params, BeanPropertyRowMapper.newInstance(IstanzaDatiEntity.class)  );
			
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastCronDati] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastCronDati] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public IstanzaCronologiaStatiEntity findInvio(Long idIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findLastCronologia] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_INVIO, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findLastCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<IstanzaEntity> find(IstanzeFilter filter, String filtroRicerca, Optional<IstanzeSorter> sorter) throws DAOException {
		List<IstanzaEntity> result = null;
		LOG.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);
		
		StringBuilder sb;
		if (filter.isFindInJsonData())
		{
			sb = new StringBuilder(FIND_JSON);
		}
		else {
			sb = new StringBuilder(FIND);
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		
		// Filtro
		sb.append(readFilter(filter, params));
		
		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		} else {
			// Ordinamento di default, se non presente ; serve sopratutto nel caso di paginazione
			sb.append(" ORDER BY i.id_istanza DESC");
		}
		
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}
		
		LOG.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));
		
		return result;
	}
	
	
	@Override
	public Integer count(IstanzeFilter filter, String filtroRicerca) throws DAOException {
		Integer result = null;
		LOG.debug("[" + CLASS_NAME + "::count] IN filter: "+filter);
		
		StringBuilder sb;
		if (filter.isFindInJsonData())
		{
			sb = new StringBuilder(COUNT_JSON);
		}
		else {
			sb = new StringBuilder(COUNT);
		}
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		// Filtro
		sb.append(readFilter(filter, params));

		LOG.debug("[" + CLASS_NAME + "::count] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);
		
		return result;
	}

	
	private StringBuilder readFilter(IstanzeFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		Optional<Long> longOpt = Optional.empty();
		Optional<String> strOpt = Optional.empty();
		Optional<Integer> intOpt = Optional.empty();
		if (filter!=null) {
			readFilterProperty(filter.getIdIstanza(), " AND i.id_istanza = :id_istanza", ID_ISTANZA, sb, params);
			readFilterProperty(filter.getCodiceIstanza(), " AND i.codice_istanza = :codice_istanza", CODICE_ISTANZA, sb, params);
			readFilterProperty(filter.getIdModulo(), " AND i.id_modulo = :id_modulo", ID_MODULO, sb, params);
			readFilterProperty(filter.getIdVersioneModulo(), " AND i.id_versione_modulo = :id_versione_modulo", ID_VERSIONE_MODULO, sb, params);
			readFilterPropertyContains(filter.getTitoloModulo(), " AND i.id_modulo IN (SELECT id_modulo FROM moon_io_d_modulo WHERE oggetto_modulo LIKE :titolo_modulo OR descrizione_modulo LIKE :titolo_modulo)", TITOLO_MODULO, sb, params);
			readFilterProperty(filter.getIdentificativoUtente(), " AND i.identificativo_utente = :identificativo_utente", IDENTIFICATIVO_UTENTE, sb, params);
			readFilterPropertyContainsUC(filter.getCodiceFiscaleDichiarante(), " AND upper(i.codice_fiscale_dichiarante) LIKE :codice_fiscale_dichiarante", CODICE_FISCALE_DICHIARANTE, sb, params);
			readFilterPropertyContainsUC(filter.getCognomeDichiarante(), " AND upper(i.cognome_dichiarante) LIKE :cognome_dichiarante", COGNOME_DICHIARANTE, sb, params);
			readFilterPropertyContainsUC(filter.getNomeDichiarante(), " AND upper(i.nome_dichiarante) LIKE :nome_dichiarante", NOME_DICHIARANTE, sb, params);

			strOpt = filter.getCodiceFiscaleJson();
			if (strOpt.isPresent()) {			
				sb.append(" AND dat.dati_istanza like '%\""+ StringUtils.substringAfterLast(strOpt.get(),".")+ "\":\"" + strOpt.get() + "%'");
			}
			strOpt = filter.getCognomeJson();
			if (strOpt.isPresent()) {
				sb.append(" AND dat.dati_istanza like '%\""+ StringUtils.substringAfterLast(strOpt.get(),".")+ "\":\"" + strOpt.get() + "%'");
			}
			strOpt = filter.getNomeJson();
			if (strOpt.isPresent()) {
				sb.append(" AND dat.dati_istanza like '%\""+ StringUtils.substringAfterLast(strOpt.get(),".")+ "\":\"" + strOpt.get() + "%'");
			}
			strOpt = filter.getNomePortale();
			Optional<Integer> intAmbOpt = filter.getIdAmbito();
			Optional<Integer> intVisOpt = filter.getIdVisibilitaAmbito();
//			LOG.info("[" + CLASS_NAME + "::readFilter] params: "+strOpt+"  intAmbOpt:"+intAmbOpt+"  intVisOpt:"+intVisOpt);
			if (strOpt.isPresent() && intAmbOpt.isEmpty() && intVisOpt.isPresent()) {
				params.addValue("nome_portale", strOpt.get());
				params.addValue("id_visibilita_ambito", intVisOpt.get());
				sb.append(" AND i.id_modulo IN (SELECT rpm.id_modulo FROM moon_fo_d_portale p, moon_fo_r_portale_modulo rpm, moon_fo_d_ambito amb, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod")
				.append(" WHERE p.nome_portale = :nome_portale AND rpm.id_portale = p.id_portale ")
				.append(" AND catmod.id_modulo = rpm.id_modulo AND amb.id_visibilita_ambito = :id_visibilita_ambito AND cat.id_ambito = amb.id_ambito AND catmod.id_categoria = cat.id_categoria)");
			} else {
				if (strOpt.isPresent()) {
					params.addValue("nome_portale", strOpt.get());
					sb.append(" AND i.id_modulo IN (SELECT rpm.id_modulo FROM moon_fo_d_portale p, moon_fo_r_portale_modulo rpm WHERE p.nome_portale = :nome_portale AND rpm.id_portale = p.id_portale) ");
				}
				if (intAmbOpt.isPresent()) {
					params.addValue("id_ambito", intAmbOpt.get());
					sb.append(" AND i.id_modulo IN (SELECT catmod.id_modulo")
							.append(" FROM moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod")
							.append(" WHERE cat.id_ambito = :id_ambito AND catmod.id_categoria = cat.id_categoria)");
				}
				if (intVisOpt.isPresent()) {
					params.addValue("id_visibilita_ambito", intVisOpt.get());
					sb.append(" AND i.id_modulo IN (SELECT catmod.id_modulo")
					.append(" FROM moon_fo_d_ambito amb, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod")
					.append(" WHERE amb.id_visibilita_ambito = :id_visibilita_ambito AND cat.id_ambito = amb.id_ambito AND catmod.id_categoria = cat.id_categoria)");
				}
			}
			Optional<List<Integer>> lstIntOpt = filter.getStatiIstanza();
			if (lstIntOpt.isPresent()) {
				List<Integer> statiFilter = lstIntOpt.get();
				if (!statiFilter.isEmpty()) {
					if (statiFilter.size() > 1) {
						params.addValue("statiFilter", statiFilter);
						sb.append(" AND i.id_stato_wf IN (:statiFilter)");
					} else {
						params.addValue("id_stato_wf", statiFilter.get(0));
						sb.append(" AND (i.id_stato_wf = :id_stato_wf)");
					}
				}
			}
			lstIntOpt = filter.getStatiBo();
			if (lstIntOpt.isPresent()) {
				params.addValue("statiBo", lstIntOpt.get());
				sb.append(", moon_wf_t_storico_workflow sw WHERE sw.id_istanza = i.id_istanza AND sw.data_fine is NULL AND sw.id_stato_wf_arrivo IN (:statiBo)");
			}
			intOpt = filter.getIdTabFo();
			if (intOpt.isPresent()) {
				params.addValue("id_tab_fo", intOpt.get());
				sb.append(" AND (s.id_tab_fo = :id_tab_fo)");
			}
			intOpt = filter.getImportanza();
			if (intOpt.isPresent()) {
				params.addValue("importanza", intOpt.get());
				sb.append(" AND i.importanza = :importanza ");
			}
			if (!IstanzeFilter.EnumFilterFlagEliminata.TUTTI.equals(filter.getFlagEliminata())) {
				sb.append(" AND i.fl_eliminata = :fl_eliminata ");
				params.addValue("fl_eliminata",
					IstanzeFilter.EnumFilterFlagEliminata.ELIMINATI.equals(filter.getFlagEliminata())?'S':'N');
			}
			if (!IstanzeFilter.EnumFilterFlagArchiviata.TUTTI.equals(filter.getFlagArchiviata())) {
				sb.append(" AND i.fl_archiviata = :fl_archiviata ");
				params.addValue("fl_archiviata",
					IstanzeFilter.EnumFilterFlagArchiviata.ARCHIVIATI.equals(filter.getFlagArchiviata())?'S':'N');
			}
			if (!IstanzeFilter.EnumFilterFlagTest.TUTTI.equals(filter.getFlagTest())) {
				sb.append(" AND i.fl_test = :fl_test ");
				params.addValue("fl_test",
					IstanzeFilter.EnumFilterFlagTest.TEST.equals(filter.getFlagTest())?'S':'N');
			}
			Optional<Date> dtOpt = filter.getCreatedStart();
			if (dtOpt.isPresent()) {
				params.addValue("data_creazione_start", dtOpt.get());
				sb.append(" AND i.data_creazione >= :data_creazione_start ");
			}
			dtOpt = filter.getCreatedEnd();
			if (dtOpt.isPresent()) {
				Date createdEnd = Date.from(dtOpt.get().toInstant().plus(1, ChronoUnit.DAYS));
				sb.append(" AND i.data_creazione < :data_creazione_end ");
				params.addValue("data_creazione_end", createdEnd);
			}
			readFilterProperty(filter.getIdEnte(), " AND i.id_ente = :id_ente", "id_ente", sb, params);
			readFilterProperty(filter.getIdTag(), " AND i.id_istanza IN (SELECT id_istanza FROM moon_fo_r_tag_istanza WHERE id_tag=:id_tag)", ID_TAG, sb, params);
			readFilterProperty(filter.getGruppoOperatoreFo(), " AND i.gruppo_operatore_fo = :gruppo_operatore_fo", GRUPPO_OPERATORE_FO, sb, params);

//			intOpt = filter.getIdAmbito();
//			if (intOpt.isPresent()) {
//				params.addValue("id_ambito", intOpt.get());
//				sb.append(" AND i.id_modulo IN (SELECT catmod.id_modulo")
//						.append(" FROM moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod")
//						.append(" WHERE cat.id_ambito = :id_ambito AND catmod.id_categoria = cat.id_categoria)");
//			}
//			intOpt = filter.getIdVisibilitaAmbito();
//			if (intOpt.isPresent()) {
//				params.addValue("id_visibilita_ambito", intOpt.get());
//				sb.append(" AND i.id_modulo IN (SELECT catmod.id_modulo")
//				.append(" FROM moon_fo_d_ambito amb, moon_fo_d_categoria cat, moon_fo_r_categoria_modulo catmod")
//				.append(" WHERE amb.id_visibilita_ambito = :id_visibilita_ambito AND cat.id_ambito = amb.id_ambito AND catmod.id_categoria = cat.id_categoria)");
//			}
		}
		return sb;
	}


	@Override
	public Long insert(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdIstanza(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}
	
	
	@Override
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] IN entity: "+entity);
			Long idCron = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_CRON, new MapSqlParameterSource() );
			entity.setIdCronologiaStati(idCron);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CRON, mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertCronologia] Record inseriti: " + numRecord);
			return idCron;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA CRONOLOGIA");
		}
	}
	@Override
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insertDati] IN entity: "+entity);
			Long idDati = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_DATI, new MapSqlParameterSource() );
			entity.setIdDatiIstanza(idDati);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_DATI, mapDatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insertDati] Record inseriti: " + numRecord);
			return idDati;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insertDati] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA DATI");
		}
	}


	@Override
	public int update(IstanzaEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}
	@Override
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateCronologia] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_CRON, mapCronologiaStatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA CRONOLOGIA");
		}
	}
	
	
	@Override
	public int updateDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDati] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_DATI, mapDatiEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA DATI");
		}
	}
	
	
//	id_istanza, codice_istanza, id_modulo, codice_fiscale_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata
    private MapSqlParameterSource mapEntityParameters(IstanzaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("codice_istanza" , entity.getCodiceIstanza(), Types.VARCHAR);
    	params.addValue("id_modulo", entity.getIdModulo());
		params.addValue("identificativo_utente", entity.getIdentificativoUtente(), Types.VARCHAR);
    	params.addValue("codice_fiscale_dichiarante", entity.getCodiceFiscaleDichiarante(), Types.VARCHAR);
    	params.addValue("cognome_dichiarante", entity.getCognomeDichiarante(), Types.VARCHAR);
    	params.addValue("nome_dichiarante", entity.getNomeDichiarante(), Types.VARCHAR);
    	params.addValue("id_stato_wf", entity.getIdStatoWf());
    	params.addValue("data_creazione", entity.getDataCreazione(), Types.TIMESTAMP);
    	params.addValue("attore_ins", entity.getAttoreIns(), Types.VARCHAR);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	params.addValue("fl_eliminata", entity.getFlagEliminata(), Types.VARCHAR);
    	params.addValue("fl_archiviata", entity.getFlagArchiviata(), Types.VARCHAR);
    	params.addValue("fl_test", entity.getFlagTest(), Types.VARCHAR);
    	params.addValue("importanza", entity.getImportanza(), Types.INTEGER);
    	params.addValue("numero_protocollo", entity.getNumeroProtocollo(), Types.VARCHAR);
    	params.addValue("data_protocollo", entity.getDataProtocollo(), Types.TIMESTAMP);
    	params.addValue("current_step", entity.getCurrentStep(), Types.INTEGER);
		params.addValue("id_versione_modulo", entity.getIdVersioneModulo(), Types.INTEGER);
		params.addValue("id_ente", entity.getIdEnte(), Types.INTEGER);
		params.addValue("hash_univocita", entity.getHashUnivocita(), Types.VARCHAR);
		params.addValue("gruppo_operatore_fo", entity.getGruppoOperatoreFo(), Types.VARCHAR);
		params.addValue("dati_aggiuntivi", entity.getDatiAggiuntivi(), Types.VARCHAR);
    	return params;
    }

//	id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd
    private MapSqlParameterSource mapCronologiaStatiEntityParameters(IstanzaCronologiaStatiEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_cronologia_stati", entity.getIdCronologiaStati());
    	params.addValue("id_istanza" , entity.getIdIstanza());
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
    	params.addValue("id_istanza" , entity.getIdIstanza());
    	params.addValue("dati_istanza", entity.getDatiIstanza(), Types.VARCHAR);
    	params.addValue("id_tipo_modifica", entity.getIdTipoModifica());
    	params.addValue("id_stepcompilazione", entity.getIdStepCompilazione());
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }


	@Override
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN idIstanza: "+idIstanza+" TO stato="+idStatoWf);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_stato_wf", idStatoWf);
	    	params.addValue("id_istanza", idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ONLY_STATO, params);
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO STATO ISTANZA");
		}
	}

	
	@Override
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::updateDichiarante] IN idIstanza: "+idIstanza+"  with CF="+codiceFiscale+"  cognome="+cognome+"  nome="+nome);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("codice_fiscale_dichiarante", codiceFiscale);
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
	public Integer countIstanzeByModuloHash(Long idModulo, String hash, String codiceIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] IN idModulo: "+idModulo+"   hash: "+hash);
			String sql = "SELECT COUNT(*) FROM moon_fo_t_istanza WHERE id_modulo=:id_modulo AND hash_univocita=:hash_univocita AND fl_eliminata='N' AND fl_archiviata='N' ";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("hash_univocita", hash);
			
			if (codiceIstanza != null) {
				sql += " AND codice_istanza not in (:codice_istanza)";
				params.addValue("codice_istanza", codiceIstanza);
			}
			Integer result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sql, params);
			LOG.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] OUT result: "+result);
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::countIstanzeByModuloHash] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


}
