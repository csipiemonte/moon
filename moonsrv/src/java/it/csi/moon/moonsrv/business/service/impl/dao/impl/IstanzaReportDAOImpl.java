/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.Consumer;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import it.csi.moon.commons.dto.api.IstanzaReport;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaReportDAO;
import it.csi.moon.moonsrv.business.service.mapper.report.ReportMapperFactory;
import it.csi.moon.moonsrv.exceptions.business.DAOException;

/**
 * DAO per l'accesso alla reportistica istanze 
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
@Component
public class IstanzaReportDAOImpl extends JdbcTemplateDAO implements IstanzaReportDAO {

	private static final String CLASS_NAME = "IstanzaReportDAOImpl";

    private static final String FIND_ISTANZE = 
    		"select uuid_ticketing_system,id_istanza,codice_istanza,numero_protocollo,data_protocollo,codice_fiscale_dichiarante,data_creazione,dati_istanza,data_chiusura,data_ultimo_invio_integrazione,dati_azione,nome_operatore from ("
    		+ " select ts.uuid_ticketing_system, i.id_istanza, i.codice_istanza, i.numero_protocollo, i.data_protocollo, i.codice_fiscale_dichiarante, i.data_creazione, d.dati_istanza"
    		+" , opt_first_chiusura.min_dt_inizio as data_chiusura"
    		+" , opt_last_integrazione.max_dt_inizio as data_ultimo_invio_integrazione"
    		+" , opt_last_dati.dati_azione as dati_azione"
    		+" , u_ope.cognome ||' '|| u_ope.nome as nome_operatore"
    		+" from moon_fo_t_istanza i"
    		+" inner join moon_fo_t_cronologia_stati c on c.id_istanza = i.id_istanza and c.data_fine is null"
    		+" inner join moon_fo_t_dati_istanza d on d.id_istanza = i.id_istanza and d.id_cronologia_stati = c.id_cronologia_stati"
    		+" left join moon_wf_d_stato s on s.id_stato_wf = i.id_stato_wf and s.codice_stato_wf in ('ACCOLTA','RESPINTA','CHIUSA_SENZA_COMUNICAZIONE')"
    		+" inner join moon_wf_t_storico_workflow sw on sw.id_istanza = i.id_istanza and sw.data_fine is null"
    		+" left join moon_fo_t_utente u_ope on u_ope.identificativo_utente = sw.attore_upd"
    		+" left join moon_ts_t_richiesta ts on ts.id_istanza = i.id_istanza"
			+" left join (select id_istanza, dati_azione, data_inizio as max_dt_inizio from moon_wf_t_storico_workflow where (id_istanza,id_storico_workflow) in (select ii.id_istanza, max(sw_dati.id_storico_workflow) as id_storico_workflow from moon_fo_t_istanza ii"
			+" inner join moon_wf_t_storico_workflow sw_dati on sw_dati.id_istanza = ii.id_istanza "
			+" inner join moon_wf_d_azione a_dati on a_dati.id_azione = sw_dati.id_azione and a_dati.codice_azione in ('CHIUDI', 'CHIUDI_SENZA_COMUNICAZIONE')"
			+" inner join moon_wf_d_stato s_dati on s_dati.id_stato_wf = sw_dati.id_stato_wf_arrivo and s_dati.codice_stato_wf in ('CONCLUSA_POSITIVAMENTE', 'CONCLUSA_NEGATIVAMENTE', 'CHIUSA_SENZA_COMUNICAZIONE') where ii.id_modulo = ? group by ii.id_istanza)) opt_last_dati on opt_last_dati.id_istanza=i.id_istanza"    		
    		+" left join (select ii.id_istanza, min(sw_chiusura.data_inizio) as min_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_chiusura on sw_chiusura.id_istanza = ii.id_istanza"
    		+" inner join moon_wf_d_azione a_chiusura on a_chiusura.id_azione = sw_chiusura.id_azione and a_chiusura.codice_azione in ('ACCOGLI','RESPINGI','CHIUDI_SENZA_COMUNICAZIONE')"
    		+" inner join moon_wf_d_stato s_chiusura on s_chiusura.id_stato_wf = sw_chiusura.id_stato_wf_arrivo and s_chiusura.codice_stato_wf in ('ACCOLTA','RESPINTA','CHIUSA_SENZA_COMUNICAZIONE') where ii.id_modulo = ? group by ii.id_istanza) opt_first_chiusura on opt_first_chiusura.id_istanza=i.id_istanza"
    		+" left join (select ii.id_istanza, max(sw_integrazione.data_inizio) as max_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_integrazione on sw_integrazione.id_istanza = ii.id_istanza"
    		+" inner join moon_wf_d_azione a_integrazione on a_integrazione.id_azione = sw_integrazione.id_azione and a_integrazione.codice_azione = 'INVIA_INTEGRAZIONE'"
    		+" inner join moon_wf_d_stato s_integrazione on s_integrazione.id_stato_wf = sw_integrazione.id_stato_wf_arrivo and s_integrazione.codice_stato_wf = 'INTEGRAZIONE_INVIATA' where ii.id_modulo = ? group by ii.id_istanza) opt_last_integrazione on opt_last_integrazione.id_istanza=i.id_istanza"
    		+" where i.id_modulo = ?"    		
    		+" and i.fl_test = 'N'"
    		+" and i.fl_eliminata = 'N' ) as i where 1 = 1";
    

    private static final String FIND_ISTANZE_APERTE =
    		 "select uuid_ticketing_system,id_istanza,codice_istanza,numero_protocollo,data_protocollo,codice_fiscale_dichiarante,data_creazione,dati_istanza,data_chiusura,data_ultimo_invio_integrazione,dati_azione,nome_operatore from ("
    		 + " select ts.uuid_ticketing_system, i.id_istanza, i.codice_istanza, i.numero_protocollo, i.data_protocollo, i.codice_fiscale_dichiarante, i.data_creazione, d.dati_istanza"
    		 +" , opt_first_chiusura.min_dt_inizio as data_chiusura"
    		 +" , opt_last_integrazione.max_dt_inizio as data_ultimo_invio_integrazione"
    		 +" , null as dati_azione"
    		 +" , u_ope.cognome ||' '|| u_ope.nome as nome_operatore"
    		 +" from moon_fo_t_istanza i"
    		 +" inner join moon_fo_t_cronologia_stati c on c.id_istanza = i.id_istanza and c.data_fine is null"
    		 +" inner join moon_fo_t_dati_istanza d on d.id_istanza = i.id_istanza and d.id_cronologia_stati = c.id_cronologia_stati"
    		 +" inner join moon_wf_d_stato s on s.id_stato_wf = i.id_stato_wf and s.codice_stato_wf not in ('BOZZA','COMPLETATA','DA_PAGARE')"
    		 +" inner join moon_wf_t_storico_workflow sw on sw.id_istanza = i.id_istanza and sw.data_fine is null"
    		 +" left join moon_fo_t_utente u_ope on u_ope.identificativo_utente = sw.attore_upd"
    		 +" left join moon_ts_t_richiesta ts on ts.id_istanza = i.id_istanza"    		 
    		 +" left join (select ii.id_istanza, min(sw_chiusura.data_inizio) as min_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_chiusura on sw_chiusura.id_istanza = ii.id_istanza"
    	     +" inner join moon_wf_d_azione a_chiusura on a_chiusura.id_azione = sw_chiusura.id_azione and a_chiusura.codice_azione in ('ACCOGLI','RESPINGI','CHIUDI_SENZA_COMUNICAZIONE')"
    		 +" inner join moon_wf_d_stato s_chiusura on s_chiusura.id_stato_wf = sw_chiusura.id_stato_wf_arrivo and s_chiusura.codice_stato_wf in ('ACCOLTA','RESPINTA','CHIUSA_SENZA_COMUNICAZIONE') where ii.id_modulo = ? group by ii.id_istanza) opt_first_chiusura on opt_first_chiusura.id_istanza=i.id_istanza"    	     
    		 +" left join (select ii.id_istanza, max(sw_integrazione.data_inizio) as max_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_integrazione on sw_integrazione.id_istanza = ii.id_istanza"
    		 +" inner join moon_wf_d_azione a_integrazione on a_integrazione.id_azione = sw_integrazione.id_azione and a_integrazione.codice_azione = 'INVIA_INTEGRAZIONE'"
    		 +" inner join moon_wf_d_stato s_integrazione on s_integrazione.id_stato_wf = sw_integrazione.id_stato_wf_arrivo and s_integrazione.codice_stato_wf = 'INTEGRAZIONE_INVIATA' where ii.id_modulo = ? group by ii.id_istanza) opt_last_integrazione on opt_last_integrazione.id_istanza=i.id_istanza"
    		 +" where i.id_modulo = ?"
    		 +" and i.fl_test = 'N'"
    		 +" and i.fl_eliminata = 'N'"
    		 +" and opt_first_chiusura.min_dt_inizio is null ) as i where 1 = 1";
    
    private static final String FIND_ISTANZE_CHIUSE = 
    		"select uuid_ticketing_system,id_istanza,codice_istanza,numero_protocollo,data_protocollo,codice_fiscale_dichiarante,data_creazione,dati_istanza,data_chiusura,data_ultimo_invio_integrazione,dati_azione,nome_operatore from ("
    		+ " select ts.uuid_ticketing_system, i.id_istanza, i.codice_istanza, i.numero_protocollo, i.data_protocollo, i.codice_fiscale_dichiarante, i.data_creazione, d.dati_istanza"
    		+" , opt_first_chiusura.min_dt_inizio as data_chiusura"
    		+" , opt_last_integrazione.max_dt_inizio as data_ultimo_invio_integrazione"
    		+" , opt_last_dati.dati_azione as dati_azione"
    		+" , u_ope.cognome ||' '|| u_ope.nome as nome_operatore"
    		+" from moon_fo_t_istanza i"
    		+" inner join moon_fo_t_cronologia_stati c on c.id_istanza = i.id_istanza and c.data_fine is null"
    		+" inner join moon_fo_t_dati_istanza d on d.id_istanza = i.id_istanza and d.id_cronologia_stati = c.id_cronologia_stati"
    		+" left join moon_wf_d_stato s on s.id_stato_wf = i.id_stato_wf and s.codice_stato_wf in ('ACCOLTA','RESPINTA','CHIUSA_SENZA_COMUNICAZIONE')"
    		+" inner join moon_wf_t_storico_workflow sw on sw.id_istanza = i.id_istanza and sw.data_fine is null"
    		+" left join moon_fo_t_utente u_ope on u_ope.identificativo_utente = sw.attore_upd"
    		+" left join moon_ts_t_richiesta ts on ts.id_istanza = i.id_istanza"
			+" left join (select id_istanza, dati_azione, data_inizio as max_dt_inizio from moon_wf_t_storico_workflow where (id_istanza,id_storico_workflow) in (select ii.id_istanza, max(sw_dati.id_storico_workflow) as id_storico_workflow from moon_fo_t_istanza ii"
			+" inner join moon_wf_t_storico_workflow sw_dati on sw_dati.id_istanza = ii.id_istanza "
			+" inner join moon_wf_d_azione a_dati on a_dati.id_azione = sw_dati.id_azione and a_dati.codice_azione in ('CHIUDI', 'CHIUDI_SENZA_COMUNICAZIONE')"
			+" inner join moon_wf_d_stato s_dati on s_dati.id_stato_wf = sw_dati.id_stato_wf_arrivo and s_dati.codice_stato_wf in ('CONCLUSA_POSITIVAMENTE', 'CONCLUSA_NEGATIVAMENTE', 'CHIUSA_SENZA_COMUNICAZIONE') where ii.id_modulo = ? group by ii.id_istanza)) opt_last_dati on opt_last_dati.id_istanza=i.id_istanza"    		
    		+" left join (select ii.id_istanza, min(sw_chiusura.data_inizio) as min_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_chiusura on sw_chiusura.id_istanza = ii.id_istanza"
    		+" inner join moon_wf_d_azione a_chiusura on a_chiusura.id_azione = sw_chiusura.id_azione and a_chiusura.codice_azione in ('ACCOGLI','RESPINGI','CHIUDI_SENZA_COMUNICAZIONE')"
    		+" inner join moon_wf_d_stato s_chiusura on s_chiusura.id_stato_wf = sw_chiusura.id_stato_wf_arrivo and s_chiusura.codice_stato_wf in ('ACCOLTA','RESPINTA','CHIUSA_SENZA_COMUNICAZIONE') where ii.id_modulo = ? group by ii.id_istanza) opt_first_chiusura on opt_first_chiusura.id_istanza=i.id_istanza"
    		+" left join (select ii.id_istanza, max(sw_integrazione.data_inizio) as max_dt_inizio from moon_fo_t_istanza ii inner join moon_wf_t_storico_workflow sw_integrazione on sw_integrazione.id_istanza = ii.id_istanza"
    		+" inner join moon_wf_d_azione a_integrazione on a_integrazione.id_azione = sw_integrazione.id_azione and a_integrazione.codice_azione = 'INVIA_INTEGRAZIONE'"
    		+" inner join moon_wf_d_stato s_integrazione on s_integrazione.id_stato_wf = sw_integrazione.id_stato_wf_arrivo and s_integrazione.codice_stato_wf = 'INTEGRAZIONE_INVIATA' where ii.id_modulo = ? group by ii.id_istanza) opt_last_integrazione on opt_last_integrazione.id_istanza=i.id_istanza"
    		+" where i.id_modulo = ?"    		
    		+" and i.fl_test = 'N'"
    		+" and i.fl_eliminata = 'N'"
    		+" and opt_first_chiusura.min_dt_inizio is not null ) as i where 1 = 1";


	private StringBuilder readFilterApiReport(Long idModulo, Date dataDa, Date dataA, MapSqlParameterSource params) {
		StringBuilder sqlFindForApi = new StringBuilder();
		//idModulo
		if (idModulo != null) {
			sqlFindForApi = sqlFindForApi.append(" and i.id_modulo = :idModulo");
			params.addValue("idModulo", idModulo);
		}
		// Range Date
		if (!StringUtils.isEmpty(dataDa) && StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa ");
			params.addValue("dataDa", dataDa);			
		} else if (!StringUtils.isEmpty(dataA) && StringUtils.isEmpty(dataDa)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione <= :dataA ");
			params.addValue("dataA", dataA);
		} else if (!StringUtils.isEmpty(dataDa) && !StringUtils.isEmpty(dataA)) {
			sqlFindForApi = sqlFindForApi.append(" and i.data_creazione >= :dataDa and i.data_creazione <= :dataA ");
			params.addValue("dataDa", dataDa);
			params.addValue("dataA", dataA);
		}
		return sqlFindForApi;
	}
	
	public enum DATEDAA {DA,A,RANGE,NOW};
	
	@Override
	public int findForApiReport(String codiceEstrazione, Long idModulo, Date dateDa, Date dateA, Consumer<IstanzaReport> consumer) {	    	
    	int count = 1;	
    	MapSqlParameterSource params = new MapSqlParameterSource();
    	StringBuilder sb = null;
    	String dateFilterName = "i.data_creazione";
		switch (codiceEstrazione.toUpperCase()) {
			case ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_APERTE:
				sb = new StringBuilder(FIND_ISTANZE_APERTE);
				break;
			case ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_CHIUSE:
				sb = new StringBuilder(FIND_ISTANZE_CHIUSE);
				dateFilterName = "i.data_chiusura";
				break;
			default:
				sb = new StringBuilder(FIND_ISTANZE);
		}
		
		DATEDAA dtCase = null;
		if (StringUtils.isEmpty(dateDa) && StringUtils.isEmpty(dateA)) {
//			sb = sb.append(" and i.data_creazione between now() - interval '24 hours' and now() ");
			sb = sb.append(" and "+dateFilterName+" >= CURRENT_DATE - 1 + '00:00:00.000000'::time and "+dateFilterName+" <= CURRENT_DATE - 1 + '23:59:59.999999'::time");
			
			dtCase = DATEDAA.NOW;
		}
//		else if (!StringUtils.isEmpty(dateDa) && StringUtils.isEmpty(dateA)) {
//			sb = sb.append(" and "+dateFilterName+" >= ? ");
//			dtCase = DATEDAA.DA;					
//		} else if (!StringUtils.isEmpty(dateA) && StringUtils.isEmpty(dateDa)) {
//			sb = sb.append(" and "+dateFilterName+" <= ? ");
//			dtCase = DATEDAA.A;
//		} else if (!StringUtils.isEmpty(dateDa) && !StringUtils.isEmpty(dateA)) {
//			sb = sb.append(" and  "+dateFilterName+" >= ? and "+dateFilterName+"  <= ? ");
//			dtCase = DATEDAA.RANGE;
//		}
		
		else if (!StringUtils.isEmpty(dateDa) && StringUtils.isEmpty(dateA)) {
			sb = sb.append(" and "+dateFilterName+" >= ? ");
			dtCase = DATEDAA.DA;					
		} else if (!StringUtils.isEmpty(dateA) && StringUtils.isEmpty(dateDa)) {
			sb = sb.append(" and "+dateFilterName+" < ? " );
			dtCase = DATEDAA.A;
		} else if (!StringUtils.isEmpty(dateDa) && !StringUtils.isEmpty(dateA)) {
			sb = sb.append(" and  "+dateFilterName+" >= ? and "+dateFilterName+" < ? ");
			dtCase = DATEDAA.RANGE;
		}		
		
		//order
		sb = sb.append(" order by i.codice_istanza desc");
		
		try (Connection conn = getCustomJdbcTemplate().getDataSource().getConnection();
			PreparedStatement stmt = conn.prepareStatement(sb.toString());) {
			setPreparedStatementForApiReport(codiceEstrazione,stmt,idModulo,dateDa,dateA,dtCase);
			stmt.setFetchSize(1);
	        ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                final IstanzaReport i = new IstanzaReport();
                i.setNumeroTicketOtrs(rs.getString("uuid_ticketing_system"));
            	i.setIdIstanza(rs.getLong("id_istanza"));
            	i.setCodiceIstanza(rs.getString("codice_istanza"));
            	i.setNumeroProtocollo(rs.getString("numero_protocollo"));
            	i.setDataProtocollo(rs.getTimestamp("data_protocollo"));
            	i.setCodiceFiscaleDichiarante(rs.getString("codice_fiscale_dichiarante"));
            	i.setCreated(rs.getTimestamp("data_creazione"));
            	i.setData(rs.getString("dati_istanza")); 
            	i.setDataChiusura(rs.getTimestamp("data_chiusura"));
            	i.setDataUltimoInvioIntegrazione(rs.getTimestamp("data_ultimo_invio_integrazione"));
            	i.setDatiAzione(rs.getString("dati_azione"));
            	i.setNomeOperatore(rs.getString("nome_operatore"));
                consumer.accept(i);	                    
            }
            return count-1;	            
		} catch (SQLException e) {
			LOG.error("[" + CLASS_NAME + "::findForApiReport] Errore database: "+e.getMessage(),e);
			throw new DAOException(getMsgErrDefault());				
		}
	}
	
//	private void setPreparedStatementForApiReport(PreparedStatement stmt, Long idModulo, Date dateDa, Date dateA,
//			DATEDAA val) throws SQLException {
//		
//		int msInADay = 1000 * 60 * 60 * 24;
//		stmt.setLong(1, idModulo);
//		if (val != null) {
//			switch (val) {
//			case NOW:
//				break;
//			case DA:
//				stmt.setDate(2, new java.sql.Date(dateDa.getTime()));
//				break;
//			case A:
//				stmt.setDate(2, new java.sql.Date(dateA.getTime()+msInADay));
//				break;
//			case RANGE:
//				stmt.setDate(2, new java.sql.Date(dateDa.getTime()));
//				stmt.setDate(3, new java.sql.Date(dateA.getTime()+msInADay));
//				break;
//			default:
//				LOG.error("[" + CLASS_NAME + "::setPreparedStatementForApiReport] Errore Enum DATEDAA: "+val);
//				throw new DAOException(getMsgErrDefault());
//			}
//		}
//	}
	
	
	private void setPreparedStatementForApiReport(String codiceEstrazione,PreparedStatement stmt, Long idModulo, Date dateDa, Date dateA,
			DATEDAA val) throws SQLException {
		
		stmt.setLong(1, idModulo);
		stmt.setLong(2, idModulo);
		stmt.setLong(3, idModulo);
		
		switch (codiceEstrazione.toUpperCase()) {
			case ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_CHIUSE:
				stmt.setLong(4, idModulo);			
				break;
			default:
				break;
		}
		
		int msInADay = 1000 * 60 * 60 * 24;
//		stmt.setLong(1, idModulo);
		if (val != null) {
			switch (val) {
			case NOW:
				break;
			case DA:		
				if (codiceEstrazione.toUpperCase().equals(ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_APERTE)) {
					stmt.setDate(4, new java.sql.Date(dateDa.getTime()));
				}
				else {
					stmt.setDate(5, new java.sql.Date(dateDa.getTime()));
				}							
				break;
			case A:							
				if (codiceEstrazione.toUpperCase().equals(ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_APERTE)) {
					stmt.setDate(4, new java.sql.Date(dateA.getTime()+msInADay));
				}
				else {
					stmt.setDate(5, new java.sql.Date(dateA.getTime()+msInADay));
				}	
				break;
			case RANGE:			
				if (codiceEstrazione.toUpperCase().equals(ReportMapperFactory.CODICI_TIPO_ESTRAZIONE.TARI_APERTE)) {					
					stmt.setDate(4, new java.sql.Date(dateDa.getTime()));
					stmt.setDate(5, new java.sql.Date(dateA.getTime()+msInADay));
				}
				else {
					stmt.setDate(5, new java.sql.Date(dateDa.getTime()));
					stmt.setDate(6, new java.sql.Date(dateA.getTime()+msInADay));
				}											
				break;
			default:
				LOG.error("[" + CLASS_NAME + "::setPreparedStatementForApiReport] Errore Enum DATEDAA: "+val);
				throw new DAOException(getMsgErrDefault());
			}
		}
	}
	
	


}
