/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonfobl.business.service.impl.dao.impl;


import java.security.SecureRandom;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.entity.ModuloEntity;
import it.csi.moon.commons.entity.ModuloProgressivoEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.moonfobl.business.service.impl.dao.ModuloProgressivoDAO;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.DAOException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso al progressivo di un modulo
 * 
 * @see ModuloProgressivoEntity
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
@Component
public class ModuloProgressivoDAOImpl extends JdbcTemplateDAO implements ModuloProgressivoDAO {
	private static final String CLASS_NAME = "ModuloProgressivoDAOImpl";
	
	private static final  String FIND = "SELECT id_modulo, id_tipo_codice_istanza, progressivo, anno_riferimento, lunghezza" +
		" FROM moon_io_d_moduloprogressivo" +
		" WHERE id_modulo = :id_modulo" +
		" AND id_tipo_codice_istanza = :id_tipo_codice_istanza";
	private static final  String INSERT = "INSERT INTO moon_io_d_moduloprogressivo (id_modulo, id_tipo_codice_istanza, progressivo, anno_riferimento, lunghezza)" + 
		" VALUES (:id_modulo, :id_tipo_codice_istanza, :progressivo, :anno_riferimento, :lunghezza)";
	private static final  String UPDATE = "UPDATE moon_io_d_moduloprogressivo" +
		" SET progressivo=:progressivo, anno_riferimento=:anno_riferimento, lunghezza=:lunghezza" +
		" WHERE id_modulo = :id_modulo" +
		" AND id_tipo_codice_istanza = :id_tipo_codice_istanza";
	private static final String DELETE = "DELETE FROM moon_io_d_moduloprogressivo WHERE id_modulo = :id_modulo";

	private static final int MAX_LENGTH_CODICE_INSTANCE = 50;
	
	@Override
	public ModuloProgressivoEntity findByIdModulo(Long idModulo, Integer idTipoCodiceIstanza) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdModulo] IN idModulo: "+idModulo);
			LOG.debug("[" + CLASS_NAME + "::findByIdModulo] IN idTipoCodiceIstanza: "+idTipoCodiceIstanza);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", idModulo);
			params.addValue("id_tipo_codice_istanza", idTipoCodiceIstanza);
			return (ModuloProgressivoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND, params, BeanPropertyRowMapper.newInstance(ModuloProgressivoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModulo] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

//	@Override
	private ModuloProgressivoEntity findByModuloForUpdate(ModuloEntity modulo) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::findByIdModuloForUpdate] IN modulo: "+modulo);
			LOG.debug("[" + CLASS_NAME + "::findByIdModuloForUpdate] IN idModulo: "+modulo.getIdModulo());
			LOG.debug("[" + CLASS_NAME + "::findByIdModuloForUpdate] IN idTipoCodiceIstanza: "+modulo.getIdTipoCodiceIstanza());
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", modulo.getIdModulo());
			params.addValue("id_tipo_codice_istanza", modulo.getIdTipoCodiceIstanza());
			String order = "";
			if(modulo.getIdTipoCodiceIstanza().equals(2)) {
				order = " AND anno_riferimento IS NOT NULL ORDER BY anno_riferimento DESC LIMIT 1";
			}
			return (ModuloProgressivoEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(FIND + order + " FOR UPDATE", params, BeanPropertyRowMapper.newInstance(ModuloProgressivoEntity.class)  );
		} catch (EmptyResultDataAccessException emptyEx) {
			LOG.error("[" + CLASS_NAME + "::findByIdModuloForUpdate] Elemento non trovato: "+emptyEx.getMessage(),emptyEx);
			throw new ItemNotFoundDAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::findByIdModuloForUpdate] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}
	
	@Override
	public void insert(ModuloProgressivoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::insert] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(INSERT, mapModuloProgressivoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::insert] Record inseriti: " + numRecord);
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::insert] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE INSERIMENTO MODULO PROGRESSIVO");
		}
	}


	@Override
	public int update(ModuloProgressivoEntity entity) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::update] IN entity: "+entity);
			int numRecord = getCustomNamedParameterJdbcTemplateImpl().update(UPDATE, mapModuloProgressivoEntityParameters(entity));
			LOG.debug("[" + CLASS_NAME + "::update] Record aggiornati: " + numRecord);
			return numRecord;
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::update] Errore database: "+e.getMessage(),e);
			throw new DAOException("ERRORE AGGIORNAMENTO MODULO PROGRESSIVO");
		}
	}
    
	@Override
	public int delete(Long id) throws DAOException {
		try {
			LOG.debug("[" + CLASS_NAME + "::delete] IN id: "+id);
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_modulo", id);
			//return (ModuloEntity)getCustomNamedParameterJdbcTemplateImpl().queryForObject(findModuloByID, params, new ModuloRowMapper());
			int numRows = getCustomNamedParameterJdbcTemplateImpl().update(DELETE, params);
			LOG.debug("[" + CLASS_NAME + "::delete] Record aggiornati: " + numRows);
			return numRows;
		} 
		catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::delete] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());
		}
	}

	/**
	 * Genera il codiceIstanza univoco
	 * Usato durante l'inserimento di una nuova istanza di un modulo
	 *  - 000015
	 *  - 2020-0000123
	 *  
	 * @param modulo
	 * @return String codiceInstanza
	 * @throws BusinessException
	 */
	@Override
	public String generateCodiceIstanzaForIdModulo(ModuloVersionatoEntity moduloE, String descTipoCodiceIstanza) throws DAOException {
		String codiceInstanza = null;
		LOG.debug("[" + CLASS_NAME + "::generateCodiceIstanzaForIdModulo] IN moduloE: "+moduloE);
		Integer year = Calendar.getInstance().get(Calendar.YEAR);

		String SEPARATOR = ".";
		StringBuilder sb = new StringBuilder();
		
		// 1. Determino la composizione del codice istanza
		String[] composizione = descTipoCodiceIstanza.split("\\.");
		Pattern pstr = Pattern.compile("^str\\d+");
		Pattern pnum = Pattern.compile("^num\\d+");
		Boolean isCodiceOk = false;
		/* determino prima il tipo di separatore da usare, se indicato
		 *  altrimenti uso il valore di default
		 */
		Pattern pSep = Pattern.compile("^sep");
		for (String elemento: composizione)
		{
			Matcher mSep = pSep.matcher(elemento);
			if (mSep.find()) {
				String sep = elemento.substring(3,4);
				SEPARATOR = (sep.equalsIgnoreCase("n")) ? "" : sep;
			}
		}
		
		for (String elemento: composizione)
		{
			String str = "";
			Matcher mstr = pstr.matcher(elemento);
			Matcher mnum = pnum.matcher(elemento);
			if (elemento.equals("codice_modulo")) {
				str = moduloE.getCodiceModulo();
			}
			else if (elemento.equals("versione")) {
				str = moduloE.getVersioneModulo();
			}
			else if (elemento.equals("anno")) {
				str = year.toString();
			}
			else if (elemento.equals("prog_annuale")) {
				Boolean isAnnuale = true;
				str = getProgressivo(moduloE, isAnnuale, year);
				isCodiceOk = true; 
			}
			else if (elemento.equals("prog")) {
				Boolean isAnnuale = false;
				str = getProgressivo(moduloE, isAnnuale, year);
				isCodiceOk = true;
			}
			else if (mstr.find()) {
				int strLenght = Integer.parseInt(elemento.substring(3));
				str = randomString (strLenght);
				isCodiceOk = true;
			}
			else if (mnum.find()) {
				int numLenght = Integer.parseInt(elemento.substring(3));
				str = randomNum (numLenght);
				isCodiceOk = true;
			}
			
			if (sb.toString().equals("")) {
				sb.append(str);
			}
			else {
				if (!str.equals("")) {
					sb.append(SEPARATOR).append(str);
				}
			}
		}
		
		if (! isCodiceOk) {
			StringBuilder sbDef = new StringBuilder();
			sbDef.append(moduloE.getCodiceModulo()).append(SEPARATOR).append(randomString (8));
			codiceInstanza = sbDef.toString();
		}
		else {
			codiceInstanza = sb.toString();
		}
		
		if (codiceInstanza.length() > MAX_LENGTH_CODICE_INSTANCE) {
			LOG.error("[" + CLASS_NAME + "::generateCodiceIstanzaForIdModulo] ERRORE MAX_LENGTH_CODICE_INSTANCE "+MAX_LENGTH_CODICE_INSTANCE+" superato !  codiceInstanza: " + codiceInstanza);
		}
		LOG.debug("[" + CLASS_NAME + "::generateCodiceIstanzaForIdModulo] codiceInstanza: " + codiceInstanza);
		return codiceInstanza;
	}
	
	private String getProgressivo(ModuloEntity moduloE, Boolean isAnnuale, Integer year) throws DAOException {
		String progressivo = "";
		
		ModuloProgressivoEntity progressivoE = null;
		try {
			progressivoE = findByModuloForUpdate(moduloE);
			boolean needInsert = false;
			if (isAnnuale) {
				// Progressivo annuale
				if (!year.equals(progressivoE.getAnnoRiferimento())) {
					progressivoE.setProgressivo(1L); // gia incrementato
					progressivoE.setAnnoRiferimento(year);
					needInsert = true;
				} else {
					progressivoE.setProgressivo(progressivoE.getProgressivo()+1); // incremento
				}
			} else { 
				// Progressivo senza azzeramento annuale
				progressivoE.setProgressivo(progressivoE.getProgressivo()+1);
			}
			if (needInsert) {
				insert(progressivoE);
			} else {
				update(progressivoE);
			}
		} catch (ItemNotFoundDAOException e) {
			// non esiste ancora nessun record in tabella
			progressivoE = new ModuloProgressivoEntity();
			progressivoE.setIdModulo(moduloE.getIdModulo());
			progressivoE.setIdTipoCodiceIstanza(moduloE.getIdTipoCodiceIstanza());
			progressivoE.setProgressivo(1L); // gia incrementato
			if (isAnnuale) {
				progressivoE.setAnnoRiferimento(year);
			}
			progressivoE.setLunghezza(7); // DEFAULT da DB
			insert(progressivoE);
		}
		progressivo = padProgressivo(progressivoE);
		return progressivo;
	}
	
	
	private String padProgressivo(ModuloProgressivoEntity progressivoE) {
		return String.format("%1$" + progressivoE.getLunghezza() + "s", progressivoE.getProgressivo()).replace(' ', '0');
	}
	

	private String randomString( int len ) {
		String AB = "ABCDEFGHJKLMNPQRSTUVWXYZ";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder( len );
	    for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}

	private String randomNum( int len ) {
		String num = "123456789";
		String numWidthZero = "1234567890";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder( len );
		// La sequenza non inizia mai per 0
		sb.append( num.charAt( rnd.nextInt(num.length()) ) );
	    for( int i = 1; i < len; i++ ) 
	      sb.append( numWidthZero.charAt( rnd.nextInt(numWidthZero.length()) ) );
	   return sb.toString();
	}
	
    private MapSqlParameterSource mapModuloProgressivoEntityParameters(ModuloProgressivoEntity entity) {
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	
    	params.addValue("id_modulo", entity.getIdModulo());
    	params.addValue("id_tipo_codice_istanza", entity.getIdTipoCodiceIstanza());
    	params.addValue("progressivo" , entity.getProgressivo());
    	params.addValue("anno_riferimento", entity.getAnnoRiferimento());
    	params.addValue("lunghezza", entity.getLunghezza());
    	return params;
    }
}
