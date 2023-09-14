/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 *
 */
package it.csi.moon.moonbobl.business.service.impl.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaEstrattoreDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzeFilter;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaEstratta;
import it.csi.moon.moonbobl.exceptions.business.DAOException;

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
public class IstanzaEstrattoreDAOImpl extends JdbcTemplateDAO implements IstanzaEstrattoreDAO {
	private final static String CLASS_NAME= "IstanzaEstrattoreDAOImpl";

    private final static String ESTRAI_SQL = "select i.id_istanza, i.codice_istanza, i.codice_fiscale_dichiarante, i.data_creazione, d.dati_istanza "
    		+ " from moon_fo_t_istanza i, moon_fo_t_cronologia_stati c, moon_fo_t_dati_istanza d"
    		+ " where i.id_modulo = ?"
    		+ " and i.id_stato_wf not in (1,3,10,60)"
    		+ " and i.fl_test = 'N'"
    		+ " and i.fl_eliminata = 'N'"
    		+ " and c.id_istanza = i.id_istanza"
    		+ " and c.data_fine is null"
    		+ " and d.id_cronologia_stati = c.id_cronologia_stati "; 
          
    @Override
	public int estraiIstanze(IstanzeFilter filter, Consumer<IstanzaEstratta> consumer)   { 
    	Connection conn = null;
    	PreparedStatement stmt = null;		
    	int count = 1;
		try {
			conn = getCustomJdbcTemplate().getDataSource().getConnection();
				
			StringBuilder sql = new StringBuilder();
			sql.append(ESTRAI_SQL);
			
			if(!StringUtils.isEmpty(filter.getStrContainsInDati())) {
				sql.append(" and d.dati_istanza like '%"+ filter.getStrContainsInDati() + "%'");							
			}
			stmt = conn.prepareStatement(sql.toString());	
			List<Long> moduli =  filter.getIdModuli().get();			
			stmt.setLong(1,moduli.get(0));			
			stmt.setFetchSize(1);
	        ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                final IstanzaEstratta i = new IstanzaEstratta();
                i.setRowNumber(count++);
            	i.setIdIstanza(rs.getLong("id_istanza"));
            	i.setCodiceIstanza(rs.getString("codice_istanza"));
            	i.setCodiceFiscaleDichiarante(rs.getString("codice_fiscale_dichiarante"));
            	i.setCreated(rs.getTimestamp("data_creazione"));
            	i.setData(rs.getString("dati_istanza"));          	
                consumer.accept(i);	                    
            }
            return count-1;	            
		} catch (SQLException e) {
			log.error("[" + CLASS_NAME + "::estraiIstanze] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());				
		}
  		finally {
  			if (stmt != null) {
  			    try {
  			      stmt.close();
  			    } catch (Exception e) {
  			    	log.error("[" + CLASS_NAME + "::estraiIstanze] Errore database: "+e.getMessage(),e);
  					throw new DAOException(getMsgErrDefault());	
  			    }
  			  }
  			  if (conn != null) {
  			    try {
  			      conn.close();
  			    } catch (Exception e) {
  			      log.error("[" + CLASS_NAME + "::estraiIstanze] Errore database: "+e.getMessage(),e);
  				  throw new DAOException(getMsgErrDefault());	
  			    }
  			  }
  			return count;
  		}
	}

}
