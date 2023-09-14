/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai attributi di un modulo
 * 
 * @see ModuloAttributoEntity
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuloAttributiDAOImpl extends JdbcTemplateDAO implements ModuloAttributiDAO {
	private static final String CLASS_NAME = "ModuloAttributiDAOImpl";
	
	private static final String FIND_BY_ID = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_attributo = :id_attributo";
	private static final String FIND_BY_NOME = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo AND nome_attributo = :nome_attributo";
	private static final String FIND_BY_ID_MODULO = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo";
	private static final String FIND_BY_ID_MODULO_WCL = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo IN ('RIPORTA_IN_BOZZA_ABILITATO','CONTO_TERZI','STAMPA_PDF','ISTANZA_AUTO_SAVE','FORMIO_BREADCRUMB_CLICKABLE','DURATA_PROCEDIMENTO_CONF')";
	private static final String FIND_BY_ID_MODULO_COSMO = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_COSMO','PSIT_COSMO_CONF')";
	private static final String FIND_BY_ID_MODULO_CRM = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_CRM','PSIT_CRM_SYSTEM','PSIT_CRM_CONF','PSIT_CRM_CONF_NEXTCRM','PSIT_CRM_CONF_R2U','PSIT_CRM_CONF_OTRS')";
	private static final String FIND_BY_ID_MODULO_EPAY = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_EPAY','PSIT_EPAY_CONF')";
	private static final String FIND_BY_ID_MODULO_EMAIL = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_EMAIL','PSIT_EMAIL_CONF')";
	private static final String FIND_BY_ID_MODULO_EXTRAI_DICHIARANTE = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_EXTRAI_DICHIARANTE')";
	private static final String FIND_BY_ID_MODULO_PROTOCOLLO = "SELECT id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd" +
			" FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo" +
			" AND nome_attributo in ('PSIT_PROTOCOLLO','PSIT_PROTOCOLLO_INTEGRAZIONE', 'PRT_METADATI', 'PCPT_IN_EMAIL', 'PCPT_IN_EMAIL_CONF', 'PROTOCOLLA_BO','PROTOCOLLA_INTEGRAZIONE_BO')";
	
	private static final  String INSERT = "INSERT INTO moon_io_d_moduloattributi (id_attributo, id_modulo, nome_attributo, valore, data_upd, attore_upd)" + 
			" VALUES (:id_attributo, :id_modulo, :nome_attributo, :valore, :data_upd, :attore_upd)";

	private static final  String UPDATE = "UPDATE moon_io_d_moduloattributi" +
			" SET id_modulo=:id_modulo, nome_attributo=:nome_attributo, valore=:valore, data_upd=:data_upd, attore_upd=:attore_upd" +
			" WHERE id_attributo = :id_attributo";

	private static final String DELETE = "DELETE FROM moon_io_d_moduloattributi WHERE id_attributo = :id_attributo";
	private static final String DELETE_ALL_BY_MODULO = "DELETE FROM moon_io_d_moduloattributi WHERE id_modulo = :id_modulo";
		
	private static final String SEQ_ID = "SELECT nextval('moon_io_d_moduloattributi_id_attributo_seq')";
	
	@Override
	public List<ModuloAttributoEntity> findByIdModulo(Long idModulo) throws DAOException {
		List<ModuloAttributoEntity> result = null;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			result = ( List<ModuloAttributoEntity>)  getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_MODULO,
					params, BeanPropertyRowMapper.newInstance(ModuloAttributoEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public List<ModuloAttributoEntity> findByIdModuloFilter(Long idModulo, ModuloAttributoFilter filter) throws DAOException {
		List<ModuloAttributoEntity> result = null;
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			String sql;
			switch (filter) {
				case CRM: sql = FIND_BY_ID_MODULO_CRM; break;
				case COSMO: sql = FIND_BY_ID_MODULO_COSMO; break;
				case WCL: sql = FIND_BY_ID_MODULO_WCL; break;
				case EMAIL: sql = FIND_BY_ID_MODULO_EMAIL; break;
				case EXTRAI_DICHIARANTE: sql = FIND_BY_ID_MODULO_EXTRAI_DICHIARANTE; break;
				case EPAY: sql = FIND_BY_ID_MODULO_EPAY; break;
				case PROTOCOLLO: sql = FIND_BY_ID_MODULO_PROTOCOLLO; break;
				default: sql = FIND_BY_ID_MODULO;
			}
			result = ( List<ModuloAttributoEntity>)  getCustomNamedParameterJdbcTemplateImpl().query(sql,
					params, BeanPropertyRowMapper.newInstance(ModuloAttributoEntity.class));
			return result;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModuloFilter] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloAttributoEntity findById(Long idAttributo) throws ItemNotFoundDAOException, DAOException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_attributo", idAttributo);
			return (ModuloAttributoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(ModuloAttributoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public ModuloAttributoEntity findByNome(Long idModulo, String nomeAttributo) throws ItemNotFoundDAOException, DAOException  {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("nome_attributo", nomeAttributo);
			return (ModuloAttributoEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(
					FIND_BY_NOME, params, BeanPropertyRowMapper.newInstance(ModuloAttributoEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByNome] Elemento non trovato: " + emptyEx.getMessage(), emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByNome] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public Long insert(ModuloAttributoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			Long idAttributo = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdAttributo(idAttributo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapModuloAttributoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
			return idAttributo;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO STRUTTURA");
		}
	}

	@Override
	public int update(ModuloAttributoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapModuloAttributoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO STRUTTURA");
		}
	}
	

	@Override
	public int delete(Long idAttributo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN idAttributo: "+idAttributo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_attributo", idAttributo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO STRUTTURA");
		}
	}

	@Override
	public int deleteAllByIdModulo(Long idModulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] IN idModulo: "+idModulo);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_ALL_BY_MODULO, params);
			LOG.debug("[" + CLASS_NAME + "::deleteAllByIdModulo] Record cancellati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteAllByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE DELETE ATTRIBUTI");
		}
	}
	
    private MapSqlParameterSource mapModuloAttributoEntityParameters(ModuloAttributoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_attributo", entity.getIdAttributo());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("nome_attributo" , entity.getNomeAttributo());
    	params.addValue("valore", entity.getValore());
    	params.addValue("data_upd" , entity.getDataUpd());
    	params.addValue("attore_upd", entity.getAttoreUpd());
    	
    	return params;
    }

}
