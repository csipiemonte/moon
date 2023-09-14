/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import it.csi.moon.commons.entity.ext.ExtTaxiEntity;
import it.csi.moon.moonsrv.business.service.TrasmettiService;
import it.csi.moon.moonsrv.business.service.impl.dao.extra.coto.BuoniTaxiDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;


/**
 *  Trasmissione buoni taxi csv
 * 
 * @author Danilo
 */

@Component
public class TrasmettiServiceImpl implements TrasmettiService{
	
	private static final String CLASS_NAME = "TrasmettiServiceImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private static final char CSV_SEPARATOR = ';';
	
	@Autowired
	BuoniTaxiDAO buoniTaxiDAO;
	
	@Override
	public Integer trasmettiBuoniTaxi() throws BusinessException {
		LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - inizio");
		LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - generazione byte array");
		List<String> rowsList = null;
		try {
			rowsList = getRows();
			StringBuilder sb = new StringBuilder();
			for (String row : rowsList) {
				sb.append(row);
				sb.append("\n");
			}
			String allRows = sb.toString();
			byte[] bytes = allRows.getBytes("UTF-8");
										
			Calendar today = Calendar.getInstance();
			DateFormat dF = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String timestampForFileName = dF.format(today.getTime());
			String fileName = "codici_buoni_taxi" + "_" +timestampForFileName+".csv";	
			
			LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - nome file: "+fileName);
					
			String server = EnvProperties.readFromFile(EnvProperties.FTP_SERVER);
			int port =  Integer.valueOf(EnvProperties.readFromFile(EnvProperties.FTP_PORT));
			
			String user = EnvProperties.readFromFile(EnvProperties.FTP_USER);
			String pass = EnvProperties.readFromFile(EnvProperties.FTP_PASSWORD);
			
			LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - inizio trasmissione");
			InputStream is = new ByteArrayInputStream(bytes);			
			try {
				
				JSch jsch = new JSch();
				Session session = jsch.getSession(user, server, port);
				session.setConfig("StrictHostKeyChecking", "no");
				session.setConfig("PreferredAuthentications", "password");
				session.setPassword(pass);
				
				LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - session connect");
				session.connect();

				ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
				
				LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - sftp channel connect");
				sftpChannel.connect();

				sftpChannel.put(is, "/"+fileName);	
										
				sftpChannel.exit();
				session.disconnect();
				
			  } catch (JSchException e) {
				  LOG.error("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - Errore invocazione servizio - problema JSch -", e);
				  throw new BusinessException("Errore trasmettiBuoniTaxi ");
				
		      } catch (SftpException e) {
		    	  LOG.error("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - Errore invocazione servizio - problema Sftp -", e);
					throw new BusinessException("Errore trasmettiBuoniTaxi ");				
		      }
			LOG.info("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - fine trasmissione");
			return rowsList.size();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + ":: trasmettiBuoniTaxi - Errore generico invocazione servizio - ", e);
			throw new BusinessException("Errore trasmettiBuoniTaxi ");
		}
	}
	
	
	public List<String> getRows() throws Exception {
		try {
			List<ExtTaxiEntity> elenco = buoniTaxiDAO.findAll();			
			List<String> rows = new ArrayList<>();
			
			// riga di intestazione
			StringBuilder sb = new StringBuilder();
			sb.append("Codice fiscale").append(CSV_SEPARATOR);
			sb.append("Codice buono"); //.append(CSV_SEPARATOR);
			rows.add(sb.toString());		
			//righe codici
			if (elenco != null && elenco.size() > 0) {
				for (ExtTaxiEntity  entity : elenco) 
				{
					StringBuilder row = new StringBuilder();
					row.append(entity.getCodice_fiscale()).append(CSV_SEPARATOR);
					row.append(entity.getCodice_buono()); //.append(CSV_SEPARATOR);						
					rows.add(row.toString());
				}								
			}			
			return rows;
		} catch (DAOException e) {
			LOG.error("[" + CLASS_NAME + ":: getRows - Errore invocazione servizio - ", e);
			throw new BusinessException("Errore getRows ");
		}
	}


	@Override
	public byte[] getRowsBuoniTaxi() throws BusinessException{	
		try {
		    List<String> rowsList = getRows();
			StringBuilder sb = new StringBuilder();
			for (String row : rowsList) {
				sb.append(row);
				sb.append("\n");
			}
			String allRows = sb.toString();
			byte[] bytes = allRows.getBytes("UTF-8");	
			return bytes;
		} catch (UnsupportedEncodingException e) {
			LOG.error("[" + CLASS_NAME + ":: getRowsBuoniTaxi - Errore coding - ", e);
			throw new BusinessException("Errore getRowsBuoniTaxi ");
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + ":: getRowsBuoniTaxi - Errore generico - ", e);
			throw new BusinessException("Errore getRowsBuoniTaxi ");
		}
	}

}
