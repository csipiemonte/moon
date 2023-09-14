/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.istat.Comune;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio.ScuoleDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.ScuolaEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso aper l'accesso alle scuole Modulo Voucher studio
 * 
 * 
 * @author Danilo
 * 
 * @since 1.0.0
 */


@Component
public class ScuoleDAOImpl extends JdbcTemplateDAO implements ScuoleDAO {

	private static final String CLASS_NAME = "ScuoleDAOImpl";
	
	private static final String FIND = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher;";
	
	private static final String FIND_BY_ID = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher WHERE id= :id;";
	
	private static final String FIND_BY_CODICE_ISTAT = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher WHERE codice_istat= :codice_istat;";
	
	private static final String FIND_BY_ID_ORDINE_SCUOLA = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher WHERE id_ordine_scuola= :id_ordine_scuola;";
		
	private static final String FIND_BY_ID_TIPO_SCUOLA = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher WHERE id_tipo_scuola= :id_tipo_scuola;";
	
	private static final String FIND_BY_CODICE_ISTAT_ID_ORDINE_SCUOLA = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole_voucher WHERE codice_istat= :codice_istat  AND id_ordine_scuola= :id_ordine_scuola order by denominazione asc";
	
	private static final String FIND_COMUNI_BY_PROVINCIA = "SELECT distinct s.codice_istat codice, c.nome_comune nome \r\n" + 
			"from moon_ext_regp_scuole_voucher s\r\n" + 
			"left join moon_fo_c_comune c \r\n" + 
			"on s.codice_istat = c.codice_comune \r\n"  +
			"where c.codice_provincia = :codice_istat \r\n" +
			"order by c.nome_comune asc	";	

	private static final String FIND_COMUNI_FUORI_REGIONE = "SELECT distinct s.codice_istat codice, 'Fuori regione' nome \r\n" + 
			"from moon_ext_regp_scuole_voucher s\r\n" + 
			"left join moon_fo_c_comune c \r\n" + 
			"on s.codice_istat = c.codice_comune \r\n" + 
			"where s.codice_istat = :codice_istat";
	
	
	private static final String FIND_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole order by denominazione asc";
	
	private static final String FIND_BY_CODICE_ISTAT_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE codice_istat= :codice_istat order by denominazione asc";
	
	private static final String FIND_BY_CODICE_ISTAT_ID_ORDINE_SCUOLA_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE codice_istat= :codice_istat  AND id_ordine_scuola= :id_ordine_scuola order by denominazione asc";
	
	private static final String FIND_BY_ANNO_SCOLASTICO_CODICE_ISTAT_IN_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE anno_scolastico = :anno_scolastico and codice_istat in (:codice_istat ) order by denominazione asc";	
	
	private static final String FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE anno_scolastico = :anno_scolastico and id_ordine_scuola= :id_ordine_scuola and codice_istat = :codice_istat order by denominazione asc";	
	
	private static final String FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_IN_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE anno_scolastico = :anno_scolastico and id_ordine_scuola= :id_ordine_scuola and codice_istat in (:codice_istat)  order by denominazione asc";	
	
	private static final String FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_NOT_IN_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola  \r\n" + 
			"FROM moon_ext_regp_scuole WHERE anno_scolastico = :anno_scolastico and id_ordine_scuola= :id_ordine_scuola and codice_istat not in (:codice_istat) order by denominazione asc";	
		
	private static final String FIND_BY_ID_TIPO_SCUOLA_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE id_tipo_scuola= :id_tipo_scuola  order by denominazione asc";
	
	private static final String FIND_BY_ID_ORDINE_SCUOLA_V2 = "SELECT id,id_scuola,codice_istat,denominazione,id_ordine_scuola,id_tipo_scuola   \r\n" + 
			"FROM moon_ext_regp_scuole WHERE id_ordine_scuola= :id_ordine_scuola  order by denominazione asc";
			
	
	
	private static final String FIND_COMUNI_BY_PROVINCIA_V2 = "SELECT distinct s.codice_istat codice, c.nome_comune nome \r\n" + 
			"from moon_ext_regp_scuole s\r\n" + 
			"left join moon_fo_c_comune c \r\n" + 
			"on s.codice_istat = c.codice_comune \r\n"  +
			"where c.codice_provincia = :codice_istat \r\n" +
			"order by c.nome_comune asc	";	

	private static final String FIND_COMUNI_FUORI_REGIONE_V2 = "SELECT distinct s.codice_istat codice, 'Fuori regione' nome \r\n" + 
			"from moon_ext_regp_scuole s\r\n" + 
			"left join moon_fo_c_comune c \r\n" + 
			"on s.codice_istat = c.codice_comune \r\n" + 
			"where s.codice_istat = :codice_istat";
	

	@Override
	public List<ScuolaEntity> find() throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::find]");
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public ScuolaEntity findById(String id) throws ItemNotFoundDAOException, DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id = " + id);
			if (id == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", id);
			return (ScuolaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params,
					BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] findById scuola non trovata per id = " + id);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstat(String codiceIstat) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstat] IN codice istat = " + codiceIstat);
			if (codiceIstat == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstat);							
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CODICE_ISTAT,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstat] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByIdOrdine(Integer idOrdineScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdOrdine] IN codice istat = " + idOrdineScuola);
			if (idOrdineScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ordine_scuola", idOrdineScuola);							
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ORDINE_SCUOLA,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdOrdine] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByIdTipo(Integer idTipoScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdTipo] IN tipo scuola = " + idTipoScuola);
			if (idTipoScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_tipo_scuola", idTipoScuola);							
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ORDINE_SCUOLA,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));			
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdTipo] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstatIdOrdine(String codiceIstat, Integer idOrdineScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdine] IN codice istat = " + codiceIstat);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdine] IN id ordine = " + idOrdineScuola);
			if (codiceIstat == null || idOrdineScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstat);	
			params.addValue("id_ordine_scuola", idOrdineScuola);		
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CODICE_ISTAT_ID_ORDINE_SCUOLA,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatIdOrdine] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	

	@Override
	public List<Comune> findByCodiceIstatProvincia(String codiceIstat) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatProvincia] IN codice istat = " + codiceIstat);
			if (codiceIstat == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();							
			String query = FIND_COMUNI_BY_PROVINCIA;
			if  (codiceIstat.equals("999")) {
				 params.addValue("codice_istat", "999999");
				 query = FIND_COMUNI_FUORI_REGIONE;
			} else {
				 params.addValue("codice_istat", codiceIstat);	
			}
			return (List<Comune>) getCustomNamedParameterJdbcTemplateImpl().query(query,params,BeanPropertyRowMapper.newInstance(Comune.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatProvincia] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	
	@Override
	public List<ScuolaEntity> findV2() throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findV2]");
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_V2,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::find] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	

	@Override
	public List<ScuolaEntity> findByCodiceIstatIdOrdineV2(String codiceIstat, Integer idOrdineScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineV2] IN codice istat = " + codiceIstat);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineV2] IN id ordine = " + idOrdineScuola);		 			
			if (codiceIstat == null || idOrdineScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstat);	
			params.addValue("id_ordine_scuola", idOrdineScuola);		
			return (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CODICE_ISTAT_ID_ORDINE_SCUOLA_V2,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstatV2(String codiceIstat) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatV2] IN codice istat = " + codiceIstat);
			if (codiceIstat == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstat);							
			return (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_CODICE_ISTAT_V2,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByIdOrdineV2(Integer idOrdineScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdOrdineV2] IN codice istat = " + idOrdineScuola);
			if (idOrdineScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_ordine_scuola", idOrdineScuola);							
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_ORDINE_SCUOLA_V2,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdOrdineV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByIdTipoV2(Integer idTipoScuola) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdTipoV2] IN tipo scuola = " + idTipoScuola);
			if (idTipoScuola == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_tipo_scuola", idTipoScuola);							
			return  (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_BY_ID_TIPO_SCUOLA_V2,params,BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdTipoV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstatIdOrdineAnnoScolasticoV2(String codiceIstat, Integer idOrdineScuola, String annoScolastico)
			throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineAnnoScolasticoV2] IN codice istat = " + codiceIstat);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineAnnoScolasticoV2] IN id ordine = " + idOrdineScuola);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineAnnoScolasticoV2] IN anno scolastico = " + annoScolastico);
			if (codiceIstat == null || idOrdineScuola == null || annoScolastico == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstat);
			params.addValue("id_ordine_scuola", idOrdineScuola);
			params.addValue("anno_scolastico", annoScolastico);
			return (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(
					FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_V2, params,
					BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineAnnoScolasticoV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstatInIdOrdineAnnoScolasticoV2(List<String> codiciIstat, Integer idOrdineScuola, String annoScolastico)
			throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatInIdOrdineAnnoScolasticoV2] IN codice istat = " + codiciIstat);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatInIdOrdineAnnoScolasticoV2] IN id ordine = " + idOrdineScuola);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatInIdOrdineAnnoScolasticoV2] IN anno scolastico = " + annoScolastico);
			if (codiciIstat == null || idOrdineScuola == null || annoScolastico == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiciIstat);
			params.addValue("id_ordine_scuola", idOrdineScuola);
			params.addValue("anno_scolastico", annoScolastico);
			return (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(
					FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_IN_V2, params,
					BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatInIdOrdineAnnoScolasticoV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<ScuolaEntity> findByCodiceIstatNotInIdOrdineAnnoScolasticoV2(List<String> codiceIstatNotIn,
			Integer idOrdineScuola, String annoScolastico) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatNotInIdOrdineAnnoScolasticoV2] IN codice istat = " + codiceIstatNotIn);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatNotInIdOrdineAnnoScolasticoV2] IN id ordine = " + idOrdineScuola);
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatNotInIdOrdineAnnoScolasticoV2] IN anno scolastico = " + annoScolastico);			
			if (codiceIstatNotIn == null || idOrdineScuola == null || annoScolastico == null) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_istat", codiceIstatNotIn);
			params.addValue("id_ordine_scuola", idOrdineScuola);
			params.addValue("anno_scolastico", annoScolastico);
			return (List<ScuolaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(
					FIND_BY_ANNO_SCOLASTICO_ID_ORDINE_SCUOLA_CODICE_ISTAT_NOT_IN_V2, params,
					BeanPropertyRowMapper.newInstance(ScuolaEntity.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatNotInIdOrdineAnnoScolasticoV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	@Override
	public List<Comune> findByCodiceIstatProvinciaV2(String codiceIstat) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByCodiceIstatProvinciaV2] IN codice istat = " + codiceIstat);
			if (codiceIstat == null ) {
				return null;
			}
			MapSqlParameterSource params = new MapSqlParameterSource();							
			String query = FIND_COMUNI_BY_PROVINCIA_V2;
			if  (codiceIstat.equals("999"))	{
				params.addValue("codice_istat", "999999");
				query = FIND_COMUNI_FUORI_REGIONE_V2;
			} else {
				params.addValue("codice_istat", codiceIstat);	
			}
			return  (List<Comune>) getCustomNamedParameterJdbcTemplateImpl().query(query,params,BeanPropertyRowMapper.newInstance(Comune.class));
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCodiceIstatProvinciaV2] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}

}
