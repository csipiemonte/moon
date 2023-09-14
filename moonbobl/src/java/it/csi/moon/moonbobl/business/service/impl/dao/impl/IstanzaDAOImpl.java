/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeSorter;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;
import it.csi.moon.moonbobl.util.Constants;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 *
 * @see IstanzaEntity
 * @see IstanzaCronologiaStatiEntity
 * @see IstanzaDatiEntity
 *
 * @author Alberto
 *
 */
@Component
public class IstanzaDAOImpl extends JdbcTemplateDAO implements IstanzaDAO {
	private final static String CLASS_NAME= "IstanzaDAOImpl";

	private static final String SELECT_FIELDS_ = "SELECT i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente," +
			" i.codice_fiscale_dichiarante, i.nome_dichiarante, i.cognome_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins," +
			" i.attore_upd, i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza," +
			" i.numero_protocollo, i.data_protocollo, i.current_step, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi";
	
	private static final  String FIND_BY_ID = SELECT_FIELDS_ +
			" FROM moon_fo_t_istanza i " +
			" WHERE i.id_istanza = :id_istanza";

	private static final String FIND = SELECT_FIELDS_ +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND cron.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	private static final String FIND_BOZZA = SELECT_FIELDS_ +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10,60)" +
			" AND cron.data_fine IS NULL" +
	        " AND i.codice_fiscale_dichiarante = i.attore_ins";
	
	private static final String FIND_DA_COMPLETARE = SELECT_FIELDS_ +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10)" +
			" AND cron.data_fine IS NULL" +
	        " AND i.codice_fiscale_dichiarante <> i.attore_ins";

	private static final String FIND_FINAL_STATE = SELECT_FIELDS_ + ", wf.id_stato_wf_arrivo, wf.data_inizio as dataStato" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza,  moon_wf_t_storico_workflow wf" +
			" WHERE wf.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";
	
	private static final String FIND_FINAL_ID_STATE = "SELECT distinct wf.id_stato_wf_arrivo id_stato_wf" +
			" FROM moon_fo_t_istanza i, moon_wf_t_storico_workflow wf" +
			" WHERE wf.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL";
	
	private static final String FIND_WITH_DATA_FILTER = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, "
			+ "i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, "
			+ "i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND cron.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	private static final String FIND_WITH_DATA_FILTER_BOZZA = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, "
			+ "i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, "
			+ "i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10,60)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante = i.attore_ins";
	
	private static final String FIND_WITH_DATA_FILTER_DA_COMPLETARE = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, "
			+ "i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, "
			+ "i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante <> i.attore_ins";

	private static final String FIND_FINAL_STATE_WITH_DATA_FILTER = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, "
			+ "i.codice_fiscale_dichiarante, wf.id_stato_wf_arrivo id_stato_wf, wf.id_stato_wf_arrivo, i.data_creazione, wf.data_inizio data_stato, i.attore_ins, i.attore_upd, "
			+ "i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, " +
			"i.numero_protocollo, i.data_protocollo, i.nome_dichiarante, i.cognome_dichiarante, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i" + 
			" LEFT JOIN moon_ep_t_richiesta ep ON i.id_istanza = ep.id_istanza" +
			" LEFT JOIN moon_wf_t_storico_workflow wf ON wf.id_istanza = i.id_istanza" +
			" INNER JOIN moon_fo_t_dati_istanza dat ON dat.id_istanza = i.id_istanza" +
			" WHERE wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";
	private static final String FIND_FINAL_ID_STATE_WITH_DATA_FILTER = "SELECT distinct wf.id_stato_wf_arrivo id_stato_wf " +
			" FROM moon_fo_t_istanza i" +
			" LEFT JOIN moon_wf_t_storico_workflow wf ON wf.id_istanza = i.id_istanza" +
			" INNER JOIN moon_fo_t_dati_istanza dat ON dat.id_istanza = i.id_istanza" +
			" WHERE wf.data_fine IS NULL";
	// -------------------------------------------------------------------------------------------

	private static final String FIND_INVIATE = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, " +
			"i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, " +
			"i.attore_upd, i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test, i.importanza, i.nome_dichiarante, i.cognome_dichiarante, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i" + 
			" INNER JOIN moon_wf_t_storico_workflow wf on i.id_istanza = wf.id_istanza" +
			" LEFT JOIN moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza" +
			" INNER JOIN moon_fo_t_cronologia_stati cron ON cron.id_istanza = i.id_istanza" +
			" WHERE cron.data_fine IS NULL" +
			" AND wf.id_stato_wf_arrivo = 2 AND wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	private static final String FIND_INVIATE_WITH_DATA_FILTER = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, " +
			"i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, " +
			"i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.importanza, i.fl_test AS flag_test, i.nome_dichiarante, i.cognome_dichiarante, i.id_ente, i.hash_univocita, i.gruppo_operatore_fo, i.dati_aggiuntivi" +
			" FROM moon_fo_t_istanza i" +
			" INNER JOIN moon_wf_t_storico_workflow wf ON i.id_istanza=wf.id_istanza" + 
			" LEFT JOIN moon_ep_t_richiesta ep ON i.id_istanza = ep.id_istanza" +
			" INNER JOIN moon_fo_t_cronologia_stati cron ON cron.id_istanza = i.id_istanza" +
			" INNER JOIN moon_fo_t_dati_istanza dat ON dat.id_istanza = i.id_istanza" +
			" WHERE cron.data_fine IS NULL" +
			" AND wf.id_stato_wf_arrivo = 2 AND wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	// ----------------------------
	// sono escluse le istanze completate=10 e inviate=2 e archiviate=4
	private static final String FIND_LAV = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, " +
			"i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, " +
			"i.attore_upd, i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test,  wf.id_stato_wf_arrivo,  wf.data_inizio, i.nome_dichiarante, i.cognome_dichiarante, i.id_ente" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf" +
			" WHERE wf.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL AND wf.id_stato_wf_arrivo != 10 AND wf.id_stato_wf_arrivo != 2 and wf.id_stato_wf_arrivo!=4 and wf.id_stato_wf_arrivo!=60" +
			" AND ep.data_del IS NULL";

	private static final String FIND_WITH_DATA_FILTER_LAV = "SELECT distinct i.id_istanza, i.codice_istanza, i.id_modulo, i.id_versione_modulo, i.identificativo_utente, "
			+ "i.codice_fiscale_dichiarante, i.id_stato_wf, i.data_creazione, i.attore_ins, i.attore_upd, "
			+ "i.fl_eliminata AS flag_eliminata, i.fl_archiviata AS flag_archiviata, i.fl_test AS flag_test,  wf.id_stato_wf_arrivo,  wf.data_inizio, i.nome_dichiarante, i.cognome_dichiarante, i.id_ente" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf, moon_fo_t_dati_istanza dat " +
			" WHERE wf.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL AND wf.id_stato_wf_arrivo != 10 AND wf.id_stato_wf_arrivo != 2 and wf.id_stato_wf_arrivo!=4 and wf.id_stato_wf_arrivo!=60" +
			" AND ep.data_del IS NULL";

	private static final String COUNT_LAV = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf" +
			" WHERE wf.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL AND wf.id_stato_wf_arrivo != 10 AND wf.id_stato_wf_arrivo != 2 and wf.id_stato_wf_arrivo!=4 and wf.id_stato_wf_arrivo!=60" +
			" AND ep.data_del IS NULL";

	private static final String COUNT_WITH_DATA_FILTER_LAV = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf, moon_fo_t_dati_istanza dat " +
			" WHERE wf.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL AND wf.id_stato_wf_arrivo != 10 AND wf.id_stato_wf_arrivo != 2 and wf.id_stato_wf_arrivo!=4 and wf.id_stato_wf_arrivo!=60"  +
			" AND ep.data_del IS NULL";


	private static final String COUNT_BOZZA = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10,60)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante = i.attore_ins";
	
	private static final String COUNT_DA_COMPLETARE = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante <> i.attore_ins";	

	private static final String COUNT_WITH_DATA_FILTER_BOZZA = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10,60)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante = i.attore_ins";
	
	private static final String COUNT_WITH_DATA_FILTER_DA_COMPLETARE = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i, moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND i.id_stato_wf in (1,10)" +
			" AND cron.data_fine IS NULL"+
	        " AND i.codice_fiscale_dichiarante <> i.attore_ins";	
	//--------------------------------

	private static final String FIND_SELECT_COUNT = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_wf_t_storico_workflow wf on i.id_istanza=wf.id_istanza"+
			" left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza," +
			" moon_fo_t_cronologia_stati cron" +
			" WHERE cron.id_istanza = i.id_istanza" +
			" AND cron.data_fine IS NULL" +
			" AND wf.id_stato_wf_arrivo =2 and wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	private static final String FIND_COUNT_FINAL_STATE =
			" SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf WHERE i.id_istanza=wf.id_istanza" +
		    " AND wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	private static final String FIND_SELECT_COUNT_WITH_DATA_FILTER = "SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_wf_t_storico_workflow wf on i.id_istanza=wf.id_istanza " +
			" left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza," +
			" moon_fo_t_cronologia_stati cron, moon_fo_t_dati_istanza dat " +
			" WHERE cron.id_istanza = i.id_istanza AND dat.id_istanza = i.id_istanza" +
			" AND cron.data_fine IS NULL" +
			" AND wf.id_stato_wf_arrivo =2 and wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";
	
	private static final String FIND_COUNT_FINAL_STATE_WITH_DATA_FILTER =
			" SELECT count(distinct i.id_istanza)" +
			" FROM moon_fo_t_istanza i left join moon_ep_t_richiesta ep on i.id_istanza = ep.id_istanza, moon_wf_t_storico_workflow wf, moon_fo_t_dati_istanza dat WHERE i.id_istanza = wf.id_istanza" +
			" AND dat.id_istanza = i.id_istanza" +
			" AND wf.data_fine IS NULL" +
			" AND ep.data_del IS NULL";

	static final String FIND_LAST_CRON = "SELECT id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd" +
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
			" AND id_stato_wf>=2 AND id_stato_wf!=10 AND data_fine IS NULL";

	private static final String INSERT = "INSERT INTO moon_fo_t_istanza(id_istanza, codice_istanza, id_modulo, identificativo_utente, codice_fiscale_dichiarante, cognome_dichiarante, nome_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata, fl_archiviata, fl_test, importanza, numero_protocollo, data_protocollo, current_step, id_versione_modulo, id_ente, hash_univocita, gruppo_operatore_fo, dati_aggiuntivi)"
			+ " VALUES (:id_istanza, :codice_istanza, :id_modulo, :identificativo_utente, :codice_fiscale_dichiarante, :cognome_dichiarante, :nome_dichiarante, :id_stato_wf, :data_creazione, :attore_ins, :attore_upd, :fl_eliminata, :fl_archiviata, :fl_test, :importanza, :numero_protocollo, :data_protocollo, :current_step, :id_versione_modulo, :id_ente, :hash_univocita, :gruppo_operatore_fo, :dati_aggiuntivi)";
	private static final String INSERT_CRON = "INSERT INTO moon_fo_t_cronologia_stati(id_cronologia_stati,id_istanza,id_stato_wf,id_azione_svolta,data_inizio,data_fine,attore_ins,attore_upd)" +
			" VALUES (:id_cronologia_stati,:id_istanza,:id_stato_wf,:id_azione_svolta,:data_inizio,:data_fine,:attore_ins,:attore_upd)";
	private static final String INSERT_DATI = "INSERT INTO moon_fo_t_dati_istanza(id_dati_istanza,id_cronologia_stati,id_istanza,dati_istanza,id_tipo_modifica,id_stepcompilazione,data_upd,attore_upd)" +
			" VALUES (:id_dati_istanza,:id_cronologia_stati,:id_istanza,:dati_istanza,:id_tipo_modifica,:id_stepcompilazione,:data_upd,:attore_upd)";

	private static final String UPDATE = "UPDATE moon_fo_t_istanza"
			+ " SET codice_istanza=:codice_istanza, id_modulo=:id_modulo, identificativo_utente=:identificativo_utente, codice_fiscale_dichiarante=:codice_fiscale_dichiarante, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante, "
			+ " id_stato_wf=:id_stato_wf, data_creazione=:data_creazione, attore_ins=:attore_ins, attore_upd=:attore_upd, fl_eliminata=:fl_eliminata, fl_archiviata=:fl_archiviata, fl_test=:fl_test, importanza=:importanza, numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo, current_step=:current_step,"
			+ " id_versione_modulo=:id_versione_modulo, id_ente=:id_ente, hash_univocita=:hash_univocita, "
			+ " dati_aggiuntivi=:dati_aggiuntivi " 
			+ " WHERE id_istanza= :id_istanza";
	private static final String UPDATE_CRON = "UPDATE moon_fo_t_cronologia_stati" +
			" SET id_istanza=:id_istanza,id_stato_wf=:id_stato_wf,id_azione_svolta=:id_azione_svolta,data_inizio=:data_inizio,data_fine=:data_fine,attore_ins=:attore_ins,attore_upd=:attore_upd" +
			" WHERE id_cronologia_stati=:id_cronologia_stati";
	private static final String UPDATE_DATI = "UPDATE moon_fo_t_dati_istanza" +
			" SET id_cronologia_stati=:id_cronologia_stati,id_istanza=:id_istanza,dati_istanza=:dati_istanza,id_tipo_modifica=:id_tipo_modifica,id_stepcompilazione=:id_stepcompilazione,data_upd=:data_upd,attore_upd=:attore_upd" +
			" WHERE id_dati_istanza=:id_dati_istanza";

	private static final String UPDATE_PROTOCOLLO = "UPDATE moon_fo_t_istanza" +
			" SET numero_protocollo=:numero_protocollo, data_protocollo=:data_protocollo " +
			" WHERE id_istanza= :id_istanza";

	private static final String UPDATE_ONLY_STATO = "UPDATE moon_fo_t_istanza" +
			" SET id_stato_wf=:id_stato_wf " +
			" WHERE id_istanza= :id_istanza";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_istanza_id_istanza_seq')";
	private static final String SEQ_ID_CRON = "SELECT nextval('moon_fo_t_cronologia_stati_id_cronologia_stati_seq')";
	private static final String SEQ_ID_DATI = "SELECT nextval('moon_fo_t_dati_istanza_id_dati_istanza_seq')";

	private static final String UPDATE_ONLY_DICHIARANTE = "UPDATE moon_fo_t_istanza" + 
		" SET codice_fiscale_dichiarante=:codice_fiscale_dichiarante, cognome_dichiarante=:cognome_dichiarante, nome_dichiarante=:nome_dichiarante " +
		" WHERE id_istanza= :id_istanza";

	private static final String FIND_EMAIL_BY_ID_ISTANZA = "SELECT email " +
			" FROM moon.moon_fo_d_area a, moon_fo_t_istanza i, moon_fo_r_area_modulo r  " +
			" WHERE r.id_modulo = i.id_modulo and r.id_area = a.id_area and " +
			" AND i.id_istanza= :id_istanza";

	@Override
	public IstanzaEntity findById(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", id);
			return (IstanzaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public String findEmailUfficioByIdIstanza(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findEmailUfficioByIdIstanza] IN id: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			String email = "";
			email = (String) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_EMAIL_BY_ID_ISTANZA, params, String.class);
					
			return email;
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public IstanzaCronologiaStatiEntity findLastCronologia(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findLastCronologia] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LAST_CRON, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findLastCronologia] Elemento non trovato: "+emptyEx.getMessage());
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findLastCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public IstanzaCronologiaStatiEntity findInvio(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findInvio] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaCronologiaStatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_INVIO, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class)  );
			/*
			List<IstanzaCronologiaStatiEntity> listCronologiaStatiEntity = (List<IstanzaCronologiaStatiEntity>) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_INVIO, params, BeanPropertyRowMapper.newInstance(IstanzaCronologiaStatiEntity.class)  );
			IstanzaCronologiaStatiEntity result = listCronologiaStatiEntity.get(0);
			return result;
			*/
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findInvio] Elemento non trovato for idIstanza: " + idIstanza);
//			throw new ItemNotFoundDAOException();
			return null;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findInvio] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public IstanzaDatiEntity findDati(Long idIstanza, Long idCronologiaStati) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findDati] IN idIstanza: "+idIstanza);
			log.debug("[" + CLASS_NAME + "::findDati] IN idCronologiaStati: "+idCronologiaStati);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			params.addValue("id_cronologia_stati", idCronologiaStati);
			return (IstanzaDatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_DATI, params, BeanPropertyRowMapper.newInstance(IstanzaDatiEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findDati] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findDati] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public IstanzaDatiEntity findLastCronDati(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findLastCronDati] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return (IstanzaDatiEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_LASTCRON_DATI, params, BeanPropertyRowMapper.newInstance(IstanzaDatiEntity.class)  );

		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findLastCronDati] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			log.info("[" + CLASS_NAME + "::findLastCronDati] IN idIstanza: "+idIstanza);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findLastCronDati] Errore database: "+e.getMessage(),e);
			log.info("[" + CLASS_NAME + "::findLastCronDati] IN idIstanza: "+idIstanza);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<IstanzaEntity> find(Integer stato, IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_WITH_DATA_FILTER;
		}
		int statowf = 0;
		if (stato != null && (stato.intValue() == 2 || stato.intValue() == 4)) {
			if (stato.intValue() == 2 )
			{
				statowf = 2;
			}
			if (stato.intValue() == 4 )
			{
				stato = 2;
				statowf = 4;
			}

			QUERY_NAME = FIND_INVIATE;
			if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
			{
				QUERY_NAME = FIND_INVIATE_WITH_DATA_FILTER;
			}
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(stato, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {				    					
//				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\""+ arrKeyValue[1] + "\":true%'");
				     	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}

		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY i.data_creazione");
		}
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}


	@Override
	public List<IstanzaEntity> findArchivio(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND_FINAL_STATE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals("")) {
			QUERY_NAME = FIND_FINAL_STATE_WITH_DATA_FILTER;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);

		MapSqlParameterSource params = new MapSqlParameterSource();

		if (filter.getCreatedEnd().isPresent()) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getCreatedEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setCreatedEnd(calendar.getTime());
		}
		
		if (filter.getStateEnd().isPresent()) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getStateEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setStateEnd(calendar.getTime());
		}		
		

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }				    
				}
			}
		}
		/*nel caso non sia stato impostato un filtro sui dati, ma il modulo richieda un filtro,
		 * allora si applica un filtro automatico
		 
		else {
			
		}
		*/
		
		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY i.data_creazione");
		}
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::findArchivio] params: "+params);
		result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}

	@Override
	public List<IstanzaEntity> find(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati) throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND_FINAL_STATE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_FINAL_STATE_WITH_DATA_FILTER;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);

		MapSqlParameterSource params = new MapSqlParameterSource();

		if (filter.getCreatedEnd().isPresent()) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getCreatedEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setCreatedEnd(calendar.getTime());
		}
		
		if (filter.getStateEnd().isPresent()) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getStateEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setStateEnd(calendar.getTime());
		}		


		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }				    
				}
			}
		}

		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY i.codice_istanza desc");
		}
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::find] params: "+params);
		result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}


	@Override
	public Integer count(Integer stato, IstanzeFilter filter, String filtroRicercaDati) throws DAOException {
		Integer result = null;
		log.debug("[" + CLASS_NAME + "::count] IN filter: "+filter);

		int statowf = 0;
		if (stato != null && (stato.intValue() == 2 || stato.intValue() == 4)) {
			if (stato.intValue() == 2 )
			{
				statowf = 2;
			}
			if (stato.intValue() == 4 )
			{
				stato = 2;
				statowf = 4;
			}
		}
		String QUERY_NAME = FIND_SELECT_COUNT;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_SELECT_COUNT_WITH_DATA_FILTER;
		}
		StringBuilder sb = new StringBuilder(QUERY_NAME);
		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(stato, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {				    	
//				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\""+ arrKeyValue[1] + "\":true%'");
				     	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}
		log.debug("[" + CLASS_NAME + "::count] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}


	@Override
	public Integer countArchivio(IstanzeFilter filter, String filtroRicercaDati) throws DAOException {
		Integer result = null;
		log.debug("[" + CLASS_NAME + "::countArchivio] IN filter: "+filter);

		String QUERY_NAME = FIND_COUNT_FINAL_STATE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals("")) {
			QUERY_NAME = FIND_COUNT_FINAL_STATE_WITH_DATA_FILTER;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		
		Date createdEnd = null;
		Date stateEnd = null;
				
		if (filter.getCreatedEnd().isPresent()) {
			
			createdEnd = filter.getCreatedEnd().get();
			
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getCreatedEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setCreatedEnd(calendar.getTime());
		}
		
		if (filter.getStateEnd().isPresent()) {
			
			stateEnd = filter.getStateEnd().get();
			
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getStateEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setStateEnd(calendar.getTime());
		}			
		

		// Filtro
		sb.append(readFilter(null, null, filter, params));
		
		// reset
		filter.setCreatedEnd(createdEnd);
		filter.setStateEnd(stateEnd);

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	 sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}
		log.debug("[" + CLASS_NAME + "::count] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}
	private StringBuilder readFilter(Integer stato, Integer statowf, IstanzeFilter filter, MapSqlParameterSource params) {
		StringBuilder sb = new StringBuilder();
		if (filter!=null) {
			if (filter.getIdIstanza().isPresent()) {
				sb.append(" AND id_istanza = :id_istanza ");
				params.addValue("id_istanza", filter.getIdIstanza().get());
			}
			if (filter.getCodiceIstanza().isPresent()) {
				sb.append(" AND i.codice_istanza like :codice_istanza ");
				params.addValue("codice_istanza", '%'+filter.getCodiceIstanza().get().toUpperCase()+'%');
			}
			if (filter.getIdModuli().isPresent()) {
				List<Long> elencoModuli = filter.getIdModuli().get();
				if(!elencoModuli.isEmpty()) {
					sb.append(" AND (");
					int i = 0;
					for (Long idModulo : elencoModuli) {
						sb.append((i==0)?"":" OR ");
						sb.append(" i.id_modulo = :id_modulo_" +i);
						params.addValue("id_modulo_"+i, idModulo);
						i++;
					}
					sb.append(") ");
				}

			}
			if (filter.getIdentificativoUtente().isPresent()) {
				sb.append(" AND i.identificativo_utente = :identificativo_utente ");
				params.addValue("identificativo_utente", filter.getIdentificativoUtente().get());
			}
			if (filter.getCodiceFiscaleDichiarante().isPresent()) {
				sb.append(" AND upper(i.codice_fiscale_dichiarante) like :codice_fiscale_dichiarante");
				params.addValue("codice_fiscale_dichiarante", filter.getCodiceFiscaleDichiarante().get().toUpperCase()+'%');
			}
			if (filter.getCognomeDichiarante().isPresent()) {
				sb.append(" AND upper(i.cognome_dichiarante) like :cognome_dichiarante ");
				params.addValue("cognome_dichiarante", filter.getCognomeDichiarante().get().toUpperCase()+'%');
			}
			if (filter.getNomeDichiarante().isPresent()) {
				sb.append(" AND upper(i.nome_dichiarante) like :nome_dichiarante ");
				params.addValue("nome_dichiarante", filter.getNomeDichiarante().get().toUpperCase()+'%');
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
			if (filter.getProtocollo().isPresent()) {
				sb.append(" AND i.numero_protocollo like :numero_protocollo ");
				params.addValue("numero_protocollo", filter.getProtocollo().get()+'%');
			}
			if (stato!=null) {
				sb.append(" AND i.id_stato_wf = :stato_wf ");
				params.addValue("stato_wf", stato);
			}
			if (statowf!= null && statowf.intValue()!=0) {
				sb.append(" AND wf.id_stato_wf_arrivo = :stato_wf_arrivo ");
				params.addValue("stato_wf_arrivo", statowf);

			}
			if (filter.getCreatedStart().isPresent()) {
				sb.append(" AND i.data_creazione >= :data_creazione_start ");
				params.addValue("data_creazione_start", filter.getCreatedStart().get());
			}
			if (filter.getCreatedEnd().isPresent()) {
//				Date createdEnd = Date.from(filter.getCreatedEnd().get().toInstant().plus(1, ChronoUnit.DAYS));
				sb.append(" AND i.data_creazione < :data_creazione_end ");
//				params.addValue("data_creazione_end", createdEnd);
				params.addValue("data_creazione_end",  filter.getCreatedEnd().get());
			}

			if (filter.getStateStart().isPresent()) {
				sb.append(" AND wf.data_inizio>= :data_state_start ");
				params.addValue("data_state_start", filter.getStateStart().get());
			}
			if (filter.getStateEnd().isPresent()) {
				sb.append(" AND wf.data_inizio < :data_state_end ");
				params.addValue("data_state_end", filter.getStateEnd().get());
			}

			// valutazione degli stati finali ( jon con tabella di storico workflow )
			if (filter.getStatiIstanza().isPresent()) {
				List<Integer> statiFilter = filter.getStatiIstanza().get();
				if (!statiFilter.isEmpty()) {
					if (statiFilter.size() > 1) {
						params.addValue("statiFilter", statiFilter);
						sb.append(" AND wf.id_stato_wf_arrivo IN (:statiFilter)"); // SPECIFIC BO wf.id_stato_wf_arrivo
					} else {
						params.addValue("id_stato_wf", statiFilter.get(0));
						sb.append(" AND (wf.id_stato_wf_arrivo = :id_stato_wf)"); // SPECIFIC BO wf.id_stato_wf_arrivo
					}
				}
			}			
			if (filter.getIdEnte().isPresent()) {
				sb.append(" AND i.id_ente = :id_ente ");
				params.addValue("id_ente", filter.getIdEnte().get());
			}
			if (filter.getIdTag().isPresent()) {
				sb.append(" AND i.id_istanza IN (SELECT id_istanza FROM moon_fo_r_tag_istanza WHERE id_tag=:id_tag) ");
				params.addValue("id_tag", filter.getIdTag().get());
			}
			if (filter.getFiltroEpay() != null) {
				if (filter.getFiltroEpay().equals(Constants.FILTRO_EPAY_PAID)) {
					sb.append(" and ep.id_notifica_pagamento is not null");
				}
				else if(filter.getFiltroEpay().equals(Constants.FILTRO_EPAY_UNPAID)) {
					sb.append(" and ep.id_notifica_pagamento is null");
				}
			}
						
		}
		return sb;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<IstanzaEntity> findInLavorazione(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati)
			throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND_LAV;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_WITH_DATA_FILTER_LAV;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);



		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(" AND i.id_stato_wf not in (1,3,4)");
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				       sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}
		//
		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY wf.data_inizio desc");
		}

		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::find] par: "+par);


	    result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Integer countInLavorazione(IstanzeFilter filter,  String filtroRicercaDati)
			throws DAOException {
		Integer result = null;

		String QUERY_NAME = COUNT_LAV;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = COUNT_WITH_DATA_FILTER_LAV;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(" AND i.id_stato_wf not in (1,3,4)");
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}

		// QUERY
		log.debug("[" + CLASS_NAME + "::countInLavorazione] par: "+par);

		log.debug("[" + CLASS_NAME + "::countInLavorazione] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<IstanzaEntity> findInBozza(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati)
			throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND_BOZZA;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_WITH_DATA_FILTER_BOZZA;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}
		//
		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY wf.data_inizio desc");
		}

		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::findInBozza] par: "+par);


	    result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Integer countInBozza(IstanzeFilter filter,  String filtroRicercaDati)
			throws DAOException {
		Integer result = null;

		String QUERY_NAME = COUNT_BOZZA;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = COUNT_WITH_DATA_FILTER_BOZZA;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}

		// QUERY
		log.debug("[" + CLASS_NAME + "::countInBozza] par: "+par);

		log.debug("[" + CLASS_NAME + "::countInBozza] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<IstanzaEntity> findDaCompletare(IstanzeFilter filter, Optional<IstanzeSorter> sorter, String filtroRicercaDati)
			throws DAOException {
		List<IstanzaEntity> result = null;

		String QUERY_NAME = FIND_DA_COMPLETARE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_WITH_DATA_FILTER_DA_COMPLETARE;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	 sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}
		//
		// Ordinamento
		if (sorter.isPresent()) {
			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
		}
		else {
			sb.append(" ORDER BY wf.data_inizio desc");
		}

		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::findInBozza] par: "+par);


	    result = (List<IstanzaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(IstanzaEntity.class));

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Integer countDaCompletare(IstanzeFilter filter,  String filtroRicercaDati)
			throws DAOException {
		Integer result = null;

		String QUERY_NAME = COUNT_DA_COMPLETARE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = COUNT_WITH_DATA_FILTER_DA_COMPLETARE;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);
		List par = new ArrayList();

		log.debug("[" + CLASS_NAME + "::find] IN filter: "+filter);

		MapSqlParameterSource params = new MapSqlParameterSource();

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}

		// QUERY
		log.debug("[" + CLASS_NAME + "::countInBozza] par: "+par);

		log.debug("[" + CLASS_NAME + "::countInBozza] params: "+params);
		result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sb.toString(), params);

		return result;
	}


	@Override
	public Long insert(IstanzaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdIstanza(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}


	@Override
	public Long insertCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insertCronologia] IN entity: "+entity);
			Long idCron = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_CRON, new MapSqlParameterSource() );
			entity.setIdCronologiaStati(idCron);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_CRON, mapCronologiaStatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insertCronologia] Record inseriti: " + numRecord);
			return idCron;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA CRONOLOGIA");
		}
	}
	@Override
	public Long insertDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insertDati] IN entity: "+entity);
			Long idDati = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID_DATI, new MapSqlParameterSource() );
			entity.setIdDatiIstanza(idDati);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT_DATI, mapDatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insertDati] Record inseriti: " + numRecord);
			return idDati;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insertDati] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA DATI");
		}
	}


	@Override
	public int update(IstanzaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}


	@Override
	public int updateCronologia(IstanzaCronologiaStatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateCronologia] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_CRON, mapCronologiaStatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::updateCronologia] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateCronologia] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA CRONOLOGIA");
		}
	}


	@Override
	public int updateDati(IstanzaDatiEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateDati] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_DATI, mapDatiEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::updateDati] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateDati] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA DATI");
		}
	}

	@Override
	public int updateProtocollo(IstanzaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateProtocollo] IN entity: "+entity);
			MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_istanza", entity.getIdIstanza());
	    	params.addValue("numero_protocollo", entity.getNumeroProtocollo());
	    	params.addValue("data_protocollo", entity.getDataProtocollo());

			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_PROTOCOLLO, params);
			log.debug("[" + CLASS_NAME + "::updateProtocollo] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateProtocollo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO PROTOCOLLO ISTANZA");
		}
	}
	
	@Override
	public int updateStato(Long idIstanza, Integer idStatoWf) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::updateStato] IN idIstanza: "+idIstanza+" TO stato="+idStatoWf);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("id_stato_wf", idStatoWf);
	    	params.addValue("id_istanza", idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ONLY_STATO, params);
			log.debug("[" + CLASS_NAME + "::updateStato] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateStato] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO STATO ISTANZA");
		}
	}

//	@Override
//	public int delete(Long id) throws DAOException {
//		try {
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			params.addValue("id_modulo", id);
//			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
//			log.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
//			return numRows;
//		}
//		catch (Exception e) {
//			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
//			throw new DAOException(getMsgErrDefault());
//		}
//	}


	@Override
	public List<Integer> findDistinctIdStatiBoPossibili(IstanzeFilter filter, String filtroRicercaDati) {
		List<Integer> result = null;

		String QUERY_NAME = FIND_FINAL_ID_STATE;
		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			QUERY_NAME = FIND_FINAL_ID_STATE_WITH_DATA_FILTER;
		}

		StringBuilder sb = new StringBuilder(QUERY_NAME);

		MapSqlParameterSource params = new MapSqlParameterSource();

		if (filter.getCreatedEnd().isPresent()) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(filter.getCreatedEnd().get());
		    calendar.add(Calendar.HOUR_OF_DAY, 23);
		    Date hoursAddedToDate = calendar.getTime();
		    calendar.setTime(hoursAddedToDate);
		    calendar.add(Calendar.MINUTE, 55);
		    filter.setCreatedEnd(calendar.getTime());
		}

		// Filtro
		sb.append(readFilter(null, null, filter, params));

		if (filtroRicercaDati != null && !filtroRicercaDati.equals(""))
		{
			String[] arrOfParams = filtroRicercaDati.split("@@");
			List<String> listParams = Arrays.asList(arrOfParams);
			if (listParams.size() > 0)
			{
				for (int i = 0; i < listParams.size(); i++) {
				    String param = listParams.get(i);
				    String[] arrKeyValue = param.split("=");
				    if (arrKeyValue[0].equals("comune")) {
				    	sb.append(" AND dat.dati_istanza like '%\"comune\":{\"codice\":"+NumberUtils.toInt(arrKeyValue[1]) + ",%'");
				    }
				    else if (arrKeyValue[0].equals("asr")) {
				    	sb.append(" AND dat.dati_istanza like '%\"asr\"%' AND dat.dati_istanza like '%\"codice\":\""+ arrKeyValue[1] + "\"%'");
				    }
				    else if (arrKeyValue[0].equals("CCR")) {
				    	sb.append(" AND dat.dati_istanza like '%\"commissione\"%' AND dat.dati_istanza like '%\"codice\":\""+arrKeyValue[1]+ "\"%'");
				    }
				}
			}
		}

		// Ordinamento
//		if (sorter.isPresent()) {
//			sb.append(" ORDER BY ").append(sorter.get().getOrderByFieldsJoinedForSQL());
//		}
//		else {
//			sb.append(" ORDER BY i.codice_istanza desc");
//		}
		// Paginazione
		if (filter!=null && filter.isUsePagination()) {
			sb.append(" LIMIT "+filter.getLimit()+" OFFSET "+filter.getOffset());
		}


		// QUERY
		log.debug("[" + CLASS_NAME + "::findDistinctIdStatiBoPossibili] params: "+params);
//		result = (List<Integer>) getCustomNamedParameterJdbcTemplateImpl().query(sb.toString(), params, BeanPropertyRowMapper.newInstance(Integer.class));
		result = (List<Integer>) getCustomNamedParameterJdbcTemplateImpl().queryForList(sb.toString(), params, Integer.class);

		return result;

	}
	
	@Override
	public int updateDichiarante(Long idIstanza, String codiceFiscale, String cognome, String nome) {
		try {
			log.debug("[" + CLASS_NAME + "::updateDichiarante] IN idIstanza: "+idIstanza+"  with CF="+codiceFiscale+"  cognome="+cognome+"  nome="+nome);
	    	MapSqlParameterSource params = new MapSqlParameterSource();
	    	params.addValue("codice_fiscale_dichiarante", codiceFiscale);
	    	params.addValue("cognome_dichiarante", cognome);
	    	params.addValue("nome_dichiarante", nome);
	    	params.addValue("id_istanza", idIstanza);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE_ONLY_DICHIARANTE, params);
			log.debug("[" + CLASS_NAME + "::updateDichiarante] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::updateDichiarante] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO DICHIARANTE ISTANZA");
		}
	}
	
	@Override
	public Integer countIstanzeByModuloHash(Long idModulo, String hash, String codiceIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] IN idModulo: "+idModulo+"   hash: "+hash);
			String sql = "SELECT COUNT(*) FROM moon_fo_t_istanza WHERE id_modulo=:id_modulo AND hash_univocita=:hash_univocita AND fl_eliminata='N' AND fl_archiviata='N' ";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("hash_univocita", hash);
			
			if (codiceIstanza != null) {
				sql += " AND codice_istanza not in (:codice_istanza)";
				params.addValue("codice_istanza", codiceIstanza);
			}
			Integer result = getCustomNamedParameterJdbcTemplateImpl().queryForInt(sql, params);
			log.debug("[" + CLASS_NAME + "::countIstanzeByModuloHash] OUT result: "+result);
			return result;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::countIstanzeByModuloHash] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}	
	
	
//	id_istanza, codice_istanza, id_modulo, codice_fiscale_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata
    private MapSqlParameterSource mapEntityParameters(IstanzaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_istanza", entity.getIdIstanza());
		params.addValue("codice_istanza", entity.getCodiceIstanza(), Types.VARCHAR);
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







}
