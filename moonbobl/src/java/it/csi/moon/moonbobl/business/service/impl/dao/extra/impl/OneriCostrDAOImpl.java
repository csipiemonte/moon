/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.impl.dao.extra.impl;


import java.sql.Types;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.OneriCostrDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrDomandaEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.extra.dto.OneriCostrIbanEntity;
import it.csi.moon.moonbobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle istanze e oggetti direttamente correlati
 * 
 * @author Laurent Pissard
 *
 * @since 1.0.0
 */
@Component
public class OneriCostrDAOImpl extends JdbcTemplateDAO implements OneriCostrDAO {
	private final static String CLASS_NAME= "OneriCostrDAOImpl";
	
	private static final String SELECT_FIELDS = "SELECT id_ext_domanda, id_istanza, codice_istanza\r\n" + 
		", codice_fiscale, cognome, nome, email, cellulare, flag_dichiarante_beneficiario\r\n" + 
		", ben_tipologia_beneficiario, ben_codice_fiscale, ben_cognome, ben_nome, ben_ragione_sociale, ben_piva, ben_data_fine_esercizio, ben_dimensione, ben_codice_ateco, ben_forma_giuridica\r\n" + 
		", ben_sede_nazione, ben_sede_regione, ben_sede_provincia, ben_sede_comune, ben_sede_indirizzo, ben_sede_cap\r\n" + 
		", prt_codice_provincia, prt_nome_provincia, prt_codice_comune, prt_nome_comune, prt_indirizzo, prt_cap, prt_tipologia_titolo_edilizio, prt_data_presentazione, prt_numero_pratica\r\n" +
		", prt_tipologia_intervento, prt_costo_presunto, prt_importo_presunto" + 
		", fo_codice_response, bo_esito_controlli, bo_operatore, bo_costo_validato, bo_importo_validato, bo_importo_pagato\r\n" + 
		", data_ins, data_upd, attore_upd, data_invio_contabilia "; 
	
	private static final  String FIND_BY_ID = SELECT_FIELDS +
		" FROM moon_ext_oneri_costr_domanda " +
		" WHERE id_ext_domanda=:id_ext_domanda";

	private static final  String FIND_BY_ID_ISTANZA = SELECT_FIELDS +
			" FROM moon_ext_oneri_costr_domanda " +
			" WHERE id_istanza=:id_istanza";
	
	
	private static final String INSERT = "INSERT INTO moon_ext_oneri_costr_domanda(id_ext_domanda, id_istanza, codice_istanza\r\n" + 
		", codice_fiscale, cognome, nome, email, cellulare, flag_dichiarante_beneficiario\r\n" + 
		", ben_tipologia_beneficiario, ben_codice_fiscale, ben_cognome, ben_nome, ben_ragione_sociale, ben_piva, ben_data_fine_esercizio, ben_dimensione, ben_codice_ateco, ben_forma_giuridica\r\n" + 
		", ben_sede_nazione, ben_sede_regione, ben_sede_provincia, ben_sede_comune, ben_sede_indirizzo, ben_sede_cap\r\n" + 
		", prt_codice_provincia, prt_nome_provincia, prt_codice_comune, prt_nome_comune, prt_indirizzo, prt_cap, prt_tipologia_titolo_edilizio, prt_data_presentazione, prt_numero_pratica\r\n" +
		", prt_tipologia_intervento, prt_costo_presunto, prt_importo_presunto" +
		", fo_codice_response, bo_esito_controlli, bo_operatore, bo_costo_validato, bo_importo_validato, bo_importo_pagato\r\n" + 
		", data_ins, data_upd, attore_upd, data_invio_contabilia)" +
		" VALUES (:id_ext_domanda, :id_istanza, :codice_istanza\r\n" + 
		", :codice_fiscale, :cognome, :nome, :email, :cellulare, :flag_dichiarante_beneficiario\r\n" + 
		", :ben_tipologia_beneficiario, :ben_codice_fiscale, :ben_cognome, :ben_nome, :ben_ragione_sociale, :ben_piva, :ben_data_fine_esercizio, :ben_dimensione, :ben_codice_ateco, :ben_forma_giuridica\r\n" + 
		", :ben_sede_nazione, :ben_sede_regione, :ben_sede_provincia, :ben_sede_comune, :ben_sede_indirizzo, :ben_sede_cap\r\n" + 
		", :prt_codice_provincia, :prt_nome_provincia, :prt_codice_comune, :prt_nome_comune, :prt_indirizzo, :prt_cap, :prt_tipologia_titolo_edilizio, :prt_data_presentazione, :prt_numero_pratica\r\n" +
		", :prt_tipologia_intervento, :prt_costo_presunto, :prt_importo_presunto\r\n" + 
		", :fo_codice_response, :bo_esito_controlli, :bo_operatore, :bo_costo_validato, :bo_importo_validato, :bo_importo_pagato\r\n" + 
		", :data_ins, :data_upd, :attore_upd, :data_invio_contabilia)";
		
	private static final String UPDATE = "UPDATE moon_ext_oneri_costr_domanda\r\n" + 
			" SET bo_operatore=:bo_operatore, " +
			"bo_costo_validato=:bo_costo_validato, " +
			"bo_importo_validato=:bo_importo_validato, " +
			"bo_importo_pagato=:bo_importo_pagato, " +
			"bo_esito_controlli=:bo_esito_controlli, " +
			"data_upd=now() " + 
			" WHERE id_istanza=:id_istanza";
	
	private static final String SEQ_ID = "SELECT nextval('moon_ext_oneri_costr_domanda_id_ext_domanda_seq')";

	private static final String SUM_PAGATO = "SELECT COALESCE(sum(bo_importo_pagato), 0 ) FROM moon_ext_oneri_costr_domanda";

	private static final String CNT_BEN_CF = "SELECT count(*) FROM moon_ext_oneri_costr_domanda WHERE ben_codice_fiscale=:ben_codice_fiscale";

	private static final String CNT_BEN_PIVA = "SELECT count(*) FROM moon_ext_oneri_costr_domanda WHERE ben_piva=:ben_piva";
	
	
	@Override
	public OneriCostrDomandaEntity findById(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ext_domanda", id);
			return (OneriCostrDomandaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(OneriCostrDomandaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}


	@Override
	public OneriCostrDomandaEntity findByIdIstanza(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", id);
			return (OneriCostrDomandaEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(OneriCostrDomandaEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public Long insert(OneriCostrDomandaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdExtDomanda(id);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return id;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO ISTANZA");
		}
	}


	@Override
	public int update(OneriCostrDomandaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapUpdEntityParameters(entity));
			log.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO ISTANZA");
		}
	}

    private MapSqlParameterSource mapUpdEntityParameters(OneriCostrDomandaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_istanza", entity.getIdIstanza());

    	params.addValue("bo_operatore" , entity.getBoOperatore(), Types.VARCHAR);
    	params.addValue("bo_costo_validato" , entity.getBoCostoValidato(), Types.BIGINT);
    	params.addValue("bo_importo_validato" , entity.getBoImportoValidato(), Types.BIGINT);
    	params.addValue("bo_importo_pagato" , entity.getBoImportoPagato(), Types.BIGINT);
    	params.addValue("bo_esito_controlli" , entity.getBoEsitoControlli(), Types.VARCHAR);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);

    	return params;
    }	

			
//	id_istanza, codice_istanza, id_modulo, codice_fiscale_dichiarante, id_stato_wf, data_creazione, attore_ins, attore_upd, fl_eliminata
    private MapSqlParameterSource mapEntityParameters(OneriCostrDomandaEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_ext_domanda", entity.getIdExtDomanda());
    	params.addValue("id_istanza", entity.getIdIstanza());
    	params.addValue("codice_istanza" , entity.getCodiceIstanza(), Types.VARCHAR);
    	
    	params.addValue("codice_fiscale" , entity.getCodiceFiscale(), Types.VARCHAR);
    	params.addValue("cognome" , entity.getCognome(), Types.VARCHAR);
    	params.addValue("nome" , entity.getNome(), Types.VARCHAR);
    	params.addValue("email" , entity.getEmail(), Types.VARCHAR);
    	params.addValue("cellulare" , entity.getCellulare(), Types.VARCHAR);
    	params.addValue("flag_dichiarante_beneficiario" , entity.getFlagDichiaranteBeneficiario(), Types.VARCHAR);
    	
    	params.addValue("ben_tipologia_beneficiario" , entity.getBenTipologiaBeneficiario(), Types.VARCHAR);
    	params.addValue("ben_codice_fiscale" , entity.getBenCodiceFiscale(), Types.VARCHAR);
    	params.addValue("ben_cognome" , entity.getBenCognome(), Types.VARCHAR);
    	params.addValue("ben_nome" , entity.getBenNome(), Types.VARCHAR);
    	params.addValue("ben_ragione_sociale" , entity.getBenRagioneSociale(), Types.VARCHAR);
    	params.addValue("ben_piva" , entity.getBenPiva(), Types.VARCHAR);
    	params.addValue("ben_data_fine_esercizio" , entity.getBenDataFineEsercizio(), Types.DATE);
    	params.addValue("ben_dimensione" , entity.getBenDimensione(), Types.VARCHAR);
    	params.addValue("ben_codice_ateco" , entity.getBenCodiceAteco(), Types.VARCHAR);
    	params.addValue("ben_forma_giuridica" , entity.getBenFormaGiuridica(), Types.VARCHAR);
    	
    	params.addValue("ben_sede_nazione" , entity.getBenSedeNazione(), Types.VARCHAR);
    	params.addValue("ben_sede_regione" , entity.getBenSedeRegione(), Types.VARCHAR);
    	params.addValue("ben_sede_provincia" , entity.getBenSedeProvincia(), Types.VARCHAR);
    	params.addValue("ben_sede_comune" , entity.getBenSedeComune(), Types.VARCHAR);
    	params.addValue("ben_sede_indirizzo" , entity.getBenSedeIndirizzo(), Types.VARCHAR);
    	params.addValue("ben_sede_cap" , entity.getBenSedeCap(), Types.VARCHAR);
    	
    	params.addValue("prt_codice_provincia" , entity.getPrtCodiceProvincia(), Types.VARCHAR);
    	params.addValue("prt_nome_provincia" , entity.getPrtNomeProvincia(), Types.VARCHAR);
    	params.addValue("prt_codice_comune" , entity.getPrtCodiceComune(), Types.VARCHAR);
    	params.addValue("prt_nome_comune" , entity.getPrtNomeComune(), Types.VARCHAR);
    	params.addValue("prt_indirizzo" , entity.getPrtIndirizzo(), Types.VARCHAR);
    	params.addValue("prt_cap" , entity.getPrtCap(), Types.VARCHAR);
    	params.addValue("prt_tipologia_titolo_edilizio" , entity.getPrtTipologiaTitoloEdilizio(), Types.VARCHAR);
    	params.addValue("prt_data_presentazione" , entity.getPrtDataPresentazione(), Types.DATE);
    	params.addValue("prt_numero_pratica" , entity.getPrtNumeroPratica(), Types.VARCHAR);
    	params.addValue("prt_tipologia_intervento" , entity.getPrtTipologiaIntervento(), Types.VARCHAR);
    	params.addValue("prt_costo_presunto" , entity.getPrtCostoPresunto(), Types.BIGINT);
    	params.addValue("prt_importo_presunto" , entity.getPrtImportoPresunto(), Types.BIGINT);
    	params.addValue("fo_codice_response" , entity.getFoCodiceResponse(), Types.VARCHAR);
    	
    	params.addValue("bo_esito_controlli" , entity.getBoEsitoControlli(), Types.VARCHAR);
    	params.addValue("bo_operatore" , entity.getBoOperatore(), Types.VARCHAR);
    	params.addValue("bo_costo_validato" , entity.getBoCostoValidato(), Types.BIGINT);
    	params.addValue("bo_importo_validato" , entity.getBoImportoValidato(), Types.BIGINT);
    	params.addValue("bo_importo_pagato" , entity.getBoImportoPagato(), Types.BIGINT);
  	
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	params.addValue("data_invio_contabilia", entity.getDataInvioContabilia(), Types.TIMESTAMP);
    	return params;
    }



	@Override
	public Long sumPagato() throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::sumPagato] BEGIN");
			return (Long)getCustomJdbcTemplate().queryForLong(SUM_PAGATO);
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::sumPagato] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			log.debug("[" + CLASS_NAME + "::sumPagato] END");
		}
	}



	@Override
	public Integer countCfPiva(OneriCostrDomandaEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::countCfPiva] BEGIN");
			MapSqlParameterSource params = new MapSqlParameterSource();
			if ("personaFisica".equalsIgnoreCase(entity.getBenTipologiaBeneficiario())) {
				params.addValue("ben_codice_fiscale", entity.getBenCodiceFiscale());
				return (Integer)getCustomNamedParameterJdbcTemplateImpl().queryForInt(CNT_BEN_CF, params);
			} else {
				params.addValue("ben_piva", entity.getBenPiva());
				return (Integer)getCustomNamedParameterJdbcTemplateImpl().queryForInt(CNT_BEN_PIVA, params);
			}
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::countCfPiva] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			log.debug("[" + CLASS_NAME + "::countCfPiva] END");
		}
	}



	@Override
	public void lock() throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::lock] BEGIN");
			getCustomJdbcTemplate().execute("LOCK TABLE moon_ext_oneri_costr_domanda IN ACCESS EXCLUSIVE MODE");
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::lock] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		} finally {
			log.debug("[" + CLASS_NAME + "::lock] END");
		}
	}
 

	
}
