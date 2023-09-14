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

import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.DipendentiRPEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.regp.DipendentiAbilitatiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class DipendentiAbilitatiDAOImpl extends JdbcTemplateDAO implements DipendentiAbilitatiDAO {
	private static final String CLASS_NAME= "DipendentiAbilitatiDAOImpl";


	private static final  String FIND = "SELECT * FROM moon_ext_dipendenti_rp WHERE codice_fiscale = :codice_fiscale";
	
	private static final  String FIND_ABILITATI = "SELECT d.* FROM moon_ext_dipendenti_rp d, moon_ext_dipendenti_rp_abilitati a"
			+ " WHERE d.id_dipendente = a.id_dipendente and a.id_modulo = :id_modulo";

	@Override
	public List<DipendentiRPEntity> findByCF(String cf) throws DAOException {
		List<DipendentiRPEntity>  result = null;
		
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_fiscale", cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] CF: "+cf);
			List<DipendentiRPEntity> lista = (List<DipendentiRPEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND, params, BeanPropertyRowMapper.newInstance(DipendentiRPEntity.class));
			result =  lista;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByCf] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByCF] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
			return result;

	}
	
	@Override
	public List<DipendentiRPEntity> findAbilitati(Long idModulo) throws DAOException {

		List<DipendentiRPEntity>  result = null;
		
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			LOG.debug("[" + CLASS_NAME + "::findAbilitati] idModulo: "+idModulo);
			List<DipendentiRPEntity> lista = (List<DipendentiRPEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_ABILITATI, params, BeanPropertyRowMapper.newInstance(DipendentiRPEntity.class));
			result =  lista;
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findAbilitati] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findAbilitati] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
			return result;

	}
}

