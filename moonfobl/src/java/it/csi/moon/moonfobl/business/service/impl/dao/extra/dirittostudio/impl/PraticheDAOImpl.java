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

import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dirittostudio.PraticheDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.dto.PraticaEdiliziaEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso alle pratiche edilizie coto
 * 
 * 
 * @author Alberto
 * 
 * @since 1.0.0
 */


@Component
public class PraticheDAOImpl extends JdbcTemplateDAO implements PraticheDAO {

	private static final String CLASS_NAME = "PraticheDAOImpl";
	
	private static final String SELECT = "SELECT id_praed_pratiche, p.id_pratica, anno, id_registro, progressivo, "
			+ "data_protocollo, desc_estesa_opera, num_archivio, maglia, desc_tipo_provv, data_provv, num_provv,"
			+ "id_via_topon, id_civico_topon, sedime, desc_via, num_civico, bis, bisinterno, interno, interno2, "
			+ "cap, scala, secondario, fl_fronte, angolo, piano, fl_provvisorio, data_fine " 
			+ "FROM moon_ext_praedi_pratiche p, moon_ext_praedi_ubicaz u " + 
			" WHERE p.id_pratica=u.id_pratica and data_fine = '' ";
	
	private static final String FIND_BY_ID = SELECT  
			+ " and id_praed_pratiche= :id" ;
	
	private static final String FIND_PROG_BY_ANNO_REGISTRO = "SELECT distinct progressivo " + 
			"FROM moon_ext_praedi_pratiche WHERE anno= :anno and id_registro= :registro " +
			"order by progressivo";
	
	private static final String FIND_PRATICA_BY_ANNO_REG_PROG = SELECT  
			+ " and anno= :anno and id_registro= :registro and progressivo= :progressivo limit 1" ;
	

	@Override
	public PraticaEdiliziaEntity findById(Integer id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findById] IN id = " + id);
			if (id == null)
				return null;
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", id);
			return (PraticaEdiliziaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_BY_ID, params,
					BeanPropertyRowMapper.newInstance(PraticaEdiliziaEntity.class));
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findById] findById pratica non trovata per id = " + id);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: " + e.getMessage(), e);
			throw new DAOException(getMsgErrDefault());
		}
	}



	@Override
	public List<String> findProgressiviPerRegistroAnno(String anno, Integer registro) throws DAOException {
		try {
			List<String> result = null;
			LOG.debug("[" + CLASS_NAME + "::findProgressiviPerRegistroAnno] input anno= " + anno + " - registro=" + registro);

			if (anno == null || registro == null)
				return null;
			 			 		 	
			 MapSqlParameterSource params = new MapSqlParameterSource();							
			 String query = FIND_PROG_BY_ANNO_REGISTRO;
			 params.addValue("anno", anno);
			 params.addValue("registro", registro);
			 result = (List<String>) getCustomNamedParameterJdbcTemplateImpl().queryForList(query,params,String.class);
			 return result;
				
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::findByCodiceIstatProvinciaV2] Errore database: " + e.getMessage(), e);
				throw new DAOException(getMsgErrDefault());
			}
	}



	@Override
	public List<PraticaEdiliziaEntity> findByAnnoRegProg(String anno, Integer registro, String progressivo)
			throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findProgressiviPerRegistroAnno] input anno= " + anno + " - registro=" + registro
					 + " - progressivo=" + progressivo);
			 			 			
			if (anno == null || registro == null || progressivo == null)
				return null;
			    
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("anno", anno);
				params.addValue("registro", registro);
				params.addValue("progressivo", progressivo);
				
				return  (List<PraticaEdiliziaEntity>) getCustomNamedParameterJdbcTemplateImpl().query(FIND_PRATICA_BY_ANNO_REG_PROG,params,BeanPropertyRowMapper.newInstance(PraticaEdiliziaEntity.class));
				
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineV2] Errore database: " + e.getMessage(), e);
				throw new DAOException(getMsgErrDefault());
			}
	}

	@Override
	public PraticaEdiliziaEntity findPraticaByAnnoRegProg(String anno, Integer registro, String progressivo)
			throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findProgressiviPerRegistroAnno] input anno= " + anno + " - registro=" + registro
					 + " - progressivo=" + progressivo);
			 			 			
			if (anno == null || registro == null || progressivo == null)
				return null;
			    
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("anno", anno);
				params.addValue("registro", registro);
				params.addValue("progressivo", progressivo);
				
				return (PraticaEdiliziaEntity) getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND_PRATICA_BY_ANNO_REG_PROG, params,
						BeanPropertyRowMapper.newInstance(PraticaEdiliziaEntity.class));
				
			} catch (Exception e) {
				LOG.error("[" + CLASS_NAME + "::findByCodiceIstatIdOrdineV2] Errore database: " + e.getMessage(), e);
				throw new DAOException(getMsgErrDefault());
			}
	}



}
