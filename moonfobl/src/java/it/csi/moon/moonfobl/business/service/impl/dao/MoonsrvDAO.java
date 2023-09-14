/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao;

import java.util.List;

import it.csi.moon.commons.dto.CampoModulo;
import it.csi.moon.commons.dto.CreaIuvResponse;
import it.csi.moon.commons.dto.Documento;
import it.csi.moon.commons.dto.EPayPagoPAParams;
import it.csi.moon.commons.dto.EmailRequest;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.IstanzaInitBLParams;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.ReportVerificaFirma;
import it.csi.moon.commons.dto.UserInfo;
import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.commons.dto.extra.edilizia.PraticaEdilizia;
import it.csi.moon.commons.entity.ModuliFilter;
import it.csi.moon.moonfobl.exceptions.business.DAOException;

public interface MoonsrvDAO {

	public String pingMoonsrv() throws DAOException;
	
	public List<Modulo> getModuli(ModuliFilter filter) throws DAOException;
//	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws DAOException;
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo, String fileds) throws DAOException;
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws DAOException;
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws DAOException;
	
	public Istanza initIstanza(String ipAddress, UserInfo user, Long idModulo, Long idVersioneModulo, IstanzaInitBLParams params) throws DAOException;
	
	public String generaPdf(UserInfo user, Long idIstanza) throws DAOException;
	public byte[] getPdfByIdIstanza(UserInfo user, Long idIstanza) throws DAOException;
	public byte[] getNotificaByIdIstanza(UserInfo user, Long idIstanza) throws DAOException;

	public String sendEmail(EmailRequest request) throws DAOException;
	
	public String protocolla(Long idIstanza) throws DAOException;
	public String protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws DAOException;

	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws DAOException;
	
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws DAOException;
	public List<CampoModulo> getCampiDatiAzione(Long idDatiAzione, String type) throws DAOException;

	public String creaTicketCrm(Long idIstanza) throws DAOException;

	public CreaIuvResponse creaIUV(Long idIstanza) throws DAOException;
	public CreaIuvResponse pagoPA(Long idIstanza, EPayPagoPAParams pagoPAParams) throws DAOException;
	public CreaIuvResponse annullaIUV(Long idIstanza, String iuv) throws DAOException;
	
	public byte[] getAllegatoFruitore(String codice) throws DAOException;	
	public String inviaRispostaIntegrazioneCosmo(Long idIstanza) throws DAOException;		
	public Documento getDocumentoNotificaByIdIstanza(UserInfo user, Long idIstanza) throws DAOException;

	public byte[] getDocumentoByFormioNameFile(UserInfo user, String formioNameFile) throws DAOException;
	public byte[] getDocumentoByIdFile(UserInfo user, Long idFile) throws DAOException;

	public byte[] getContenutoIndexByUid(String uuidIndex);


	public List<ComponenteFamiglia> getFamigliaANPR(String codiceFiscale, String userJwt, String clientProfileKey, String ipAdress, String utente) throws DAOException;

	public ReportVerificaFirma verificaFirmaByContenuto(byte[] bytes) throws DAOException;

	public String pubblicaMyDocs(Long idIstanza) throws DAOException;

	public String richiestaNotifyByIdIstanza(Long idIstanza) throws DAOException;
	
	public List<PraticaEdilizia> getElencoPratiche(String anno, String numRegistro, String progressivo) throws DAOException;
}
