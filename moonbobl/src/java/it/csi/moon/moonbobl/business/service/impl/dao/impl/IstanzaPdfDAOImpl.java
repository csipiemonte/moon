/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.impl;

import java.sql.Types;
import java.util.Optional;

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

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaPdfDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaPdfEntity;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

@Component
public class IstanzaPdfDAOImpl extends JdbcTemplateDAO implements IstanzaPdfDAO {
	
	private static final String CLASS_NAME = "IstanzaPdfDAOImpl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_FULL_FILEDS = "SELECT id_pdf_istanza, id_istanza, id_modulo, hash_pdf, contenuto_pdf, resoconto, data_ins, attore_ins, data_upd, attore_upd" + 
		" FROM moon_fo_t_pdf_istanza ";
	
	private static final String FIND_BY_ID = SELECT_FULL_FILEDS +
		" WHERE id_pdf_istanza = :id_pdf_istanza";
	private static final String FIND_BY_ID_ISTANZA = SELECT_FULL_FILEDS +
		" WHERE id_istanza = :id_istanza";
		
	private static final String INSERT = "INSERT INTO moon_fo_t_pdf_istanza (" + 
			"id_pdf_istanza,id_istanza, id_modulo, hash_pdf, contenuto_pdf, resoconto, data_ins, attore_ins, data_upd, attore_upd)" + 
			" VALUES (?,?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_NAMED = "INSERT INTO moon_fo_t_pdf_istanza (" + 
			"id_pdf_istanza,id_istanza, id_modulo, hash_pdf, contenuto_pdf, resoconto, data_ins, attore_ins, data_upd, attore_upd)" + 
			" VALUES (:id_pdf_istanza,:id_istanza,:id_modulo,:hash_pdf,:contenuto_pdf,:resoconto,:data_ins,:attore_ins,:data_upd,:attore_upd)"; 
	
	private static final String UPDATE = "UPDATE moon_fo_t_pdf_istanza" + 
			" SET id_istanza=?, id_modulo=?, hash_pdf=?, contenuto_pdf=?, resoconto=?, data_upd=?, attore_upd=?" + // data_ins=?, attore_ins=?, 
			" WHERE id_pdf_istanza=?";
	private static final String UPDATE_NAMED = "UPDATE moon_fo_t_pdf_istanza" + 
			" SET fid_istanza=:id_istanza, id_modulo=:id_modulo, hash_pdf=:hash_pdf, contenuto_pdf=:contenuto_pdf, resoconto=:resoconto, data_upd=:data_upd, attore_upd=:attore_upd" + 
			" WHERE id_pdf_istanza=:id_pdf_istanza"; // , data_ins=:data_ins, attore_ins=:attore_ins

	private static final String DELETE = "DELETE FROM moon_fo_t_pdf_istanza WHERE id_pdf_istanza = :id_pdf_istanza";
	private static final String DELETE_BY_ID_ISTANZA = "DELETE FROM moon_fo_t_pdf_istanza WHERE id_istanza = :id_istanza";

	private static final String SEQ_ID = "SELECT nextval('moon_fo_t_pdf_istanza_id_pdf_istanza_seq')";
	
	@Override
	public IstanzaPdfEntity findById(Long id) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findById] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_pdf_istanza", id);
			return (IstanzaPdfEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params, BeanPropertyRowMapper.newInstance(IstanzaPdfEntity.class) );
		} catch (EmptyResultDataAccessException emptyEx) {
			log.error("[" + CLASS_NAME + "::findById] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	
	@Override
	public Optional<IstanzaPdfEntity> findByIdIstanza(Long idIstanza) throws ItemNotFoundDAOException, DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::findByIdIstanza] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			return Optional.of((IstanzaPdfEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID_ISTANZA, params, BeanPropertyRowMapper.newInstance(IstanzaPdfEntity.class) ));
		} catch (EmptyResultDataAccessException emptyEx) {
			log.warn("[" + CLASS_NAME + "::findByIdIstanza] Elemento non trovato. idIstanza:" + idIstanza);
			return Optional.empty();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::findByIdIstanza] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
	@Override
	public Long insert(IstanzaPdfEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::insert] IN entity: " + entity);

			Long id = getCustomNamedParameterJdbcTemplateImpl().queryForLong(SEQ_ID, new MapSqlParameterSource() );
			entity.setIdPdfIstanza(id);

			if (log.isDebugEnabled()) {
				log.debug("================================================");
				log.debug(INSERT);
				log.debug("entity: "+entity);
			}
			  
			LobHandler lobHandler = new DefaultLobHandler();
			jdbcTemplate.update(INSERT,
					new SqlParameterValue(Types.BIGINT, entity.getIdPdfIstanza()),
					new SqlParameterValue(Types.BIGINT, entity.getIdIstanza()),
					new SqlParameterValue(Types.BIGINT, entity.getIdModulo()),
					new SqlParameterValue(Types.VARCHAR, entity.getHashPdf()),
					new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenutoPdf(), lobHandler)),
					new SqlParameterValue(Types.VARCHAR, entity.getResoconto()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataIns()),
		    		new SqlParameterValue(Types.VARCHAR, entity.getAttoreIns()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataUpd()),
					new SqlParameterValue(Types.VARCHAR, entity.getAttoreUpd())
					);
			
			return entity.getIdPdfIstanza();

			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::insert] Errore database: ", e);
			if (e.getMessage().contains("ak_moon_fo_t_pdf_istanza_id_istanza")) {
				throw new DAOException("AKConstraintViolation");
			}
			throw new DAOException("ERRORE INSERIMENTO PDF ISTANZA");
		}
	}


	@Override
	public int update(IstanzaPdfEntity entity) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::update] IN entity: " + entity);
			log.debug("================================================");
			log.debug(UPDATE);
						  
			LobHandler lobHandler = new DefaultLobHandler();
			int numRecord = jdbcTemplate.update(UPDATE,
					new SqlParameterValue(Types.BIGINT, entity.getIdIstanza()),
					new SqlParameterValue(Types.BIGINT, entity.getIdModulo()),
					new SqlParameterValue(Types.VARCHAR, entity.getHashPdf()),
					new SqlParameterValue(Types.BLOB, new SqlLobValue(entity.getContenutoPdf(), lobHandler)),
					new SqlParameterValue(Types.VARCHAR, entity.getResoconto()),
//					new SqlParameterValue(Types.TIMESTAMP, entity.getDataIns()),
//		    		new SqlParameterValue(Types.VARCHAR, entity.getAttoreIns()),
					new SqlParameterValue(Types.TIMESTAMP, entity.getDataUpd()),
					new SqlParameterValue(Types.VARCHAR, entity.getAttoreUpd()),
					new SqlParameterValue(Types.BIGINT, entity.getIdPdfIstanza())
					);
			
			return numRecord;
			
			// https://docs.spring.io/spring/docs/3.0.0.M4/reference/html/ch12s07.html
		} catch (Exception e) {
//			System.out.println("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage());
//			e.printStackTrace();
			log.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE UPDATE PDF ISTANZA");
		}
	}


	@Override
	public int delete(Long id) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_pdf_istanza", id);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			log.debug("[" + CLASS_NAME + "::delete] Record deleted: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public int deleteByIdIstanza(Long idIstanza) throws DAOException {
		try {
			log.debug("[" + CLASS_NAME + "::delete] IN idIstanza: "+idIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_istanza", idIstanza);
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE_BY_ID_ISTANZA, params);
			log.debug("[" + CLASS_NAME + "::delete] Record deleted: " + numRows);
			return numRows;
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
    private MapSqlParameterSource mapEntityParameters(IstanzaPdfEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	params.addValue("id_pdf_istanza", entity.getIdPdfIstanza());
    	params.addValue("id_istanza" , entity.getIdIstanza());
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("hash_pdf", entity.getHashPdf(), Types.VARCHAR);
    	params.addValue("contenuto_pdf", entity.getContenutoPdf(), Types.BLOB);
    	params.addValue("resoconto", entity.getResoconto(), Types.VARCHAR);
    	params.addValue("data_ins", entity.getDataIns(), Types.TIMESTAMP);
    	params.addValue("attore_ins", entity.getAttoreIns(), Types.INTEGER);
    	params.addValue("data_upd", entity.getDataUpd(), Types.TIMESTAMP);
    	params.addValue("attore_upd", entity.getAttoreUpd(), Types.VARCHAR);
    	return params;
    }
    
}
