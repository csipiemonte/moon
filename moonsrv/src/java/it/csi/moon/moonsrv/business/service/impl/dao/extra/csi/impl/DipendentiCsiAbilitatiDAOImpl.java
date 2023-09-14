/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.extra.csi.impl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.extra.csi.DipendentiCsiAbilitatiDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.dto.DipendentiCsiEntity;
import it.csi.moon.moonsrv.business.service.impl.dao.impl.JdbcTemplateDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.exceptions.business.ItemNotFoundDAOException;

@Component
public class DipendentiCsiAbilitatiDAOImpl extends JdbcTemplateDAO implements DipendentiCsiAbilitatiDAO {
	private static final String CLASS_NAME= "DipendentiCsiAbilitatiDAOImpl";


	private static final  String FIND = "SELECT id_dipendente, cognome, nome, d.matricola, codice_fiscale, fo_i_liv, resp_fo_i_liv, "
			+ "fo_ii_liv, fo_iii_liv, fo_iv_liv, funzione_diretta, resp_funz_diretta, email, indirizzo, "
			+ "gg_standard, gg_aggiuntivi, gg_limite, tipoOrario, numeroOrePartTime, profiloOrarioPartTime, " 
			+ "categoria, legge104, certmedico, telelavoro, giornitelelavoro "
			+ "FROM moon.moon_ext_dipendenti_csi d, moon_ext_dipendenti_csi_abilitati c, moon_ext_dipendenti_agil a "
			+ "where d.matricola = a.matricola and d.matricola = c.matricola "
			+ "and codice_fiscale = :codice_fiscale ";
	
	private static final  String FIND_ABILITATI = FIND + " and c.id_modulo = :id_modulo";

	@Override
	public List<DipendentiCsiEntity> findByCF(String cf) throws DAOException {
		List<DipendentiCsiEntity>  result = null;
		
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_fiscale", cf);
			LOG.debug("[" + CLASS_NAME + "::findByCF] CF: "+cf);
			List<DipendentiCsiEntity> lista = (List<DipendentiCsiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND, params, BeanPropertyRowMapper.newInstance(DipendentiCsiEntity.class));
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
	public List<DipendentiCsiEntity> findAbilitati(Long idModulo, String cf) throws DAOException {

		List<DipendentiCsiEntity>  result = null;
		
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("codice_fiscale", cf);
			params.addValue("id_modulo", idModulo);
			LOG.debug("[" + CLASS_NAME + "::findAbilitati] idModulo: "+idModulo);
			List<DipendentiCsiEntity> lista = (List<DipendentiCsiEntity>)getCustomNamedParameterJdbcTemplateImpl().query(FIND_ABILITATI, params, BeanPropertyRowMapper.newInstance(DipendentiCsiEntity.class));
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

