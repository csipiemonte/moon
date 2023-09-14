/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.regp.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.FineConcEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.regp.FineConcDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class FineConcDAOImpl extends JdbcTemplateDAO implements FineConcDAO {
	private static final String CLASS_NAME= "FineConcDAOImpl";

//	private static final  String FIND = "SELECT matricola , cognome , nome , codice_fiscale , cod_strut , categoria, "
//			+ "comune_nascita , provincia_nascita, comune_residenza, provincia_residenza, indirizzo_residenza, email , "
//			+ "data_nascita , data_inizio, data_fine , desc_abbonamento_anno_precedente , abbonamento_da , abbonamento_a, num_tessera_anno_precedente "
//			+ " FROM moon_ext_mobilityamoci "
//			+ " WHERE codice_fiscale = :codice_fiscale";
	private static final  String FIND = "SELECT  * FROM moon_ext_fine_conc WHERE codice_fiscale_abilitato = :codice_fiscale";

	@Override
	public List<FineConcEntity> findByCF(String cf) throws DAOException {
		List<FineConcEntity>  result = null;
		
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_fiscale", cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] CF: "+cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] params: "+params);
			List<FineConcEntity> lista = (List<FineConcEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND, params, BeanPropertyRowMapper.newInstance(FineConcEntity.class));
			result =  lista;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCf] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findById] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
			return result;

		}
	}

