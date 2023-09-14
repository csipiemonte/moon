/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao;

import java.util.Date;
import java.util.List;

import it.csi.moon.moonbobl.business.service.impl.dto.ModuliFilter;
import it.csi.moon.moonbobl.business.service.impl.dto.MyDocsRichiestaEntity;
import it.csi.moon.moonbobl.dto.IstanzaInitBLParams;
import it.csi.moon.moonbobl.dto.extra.demografia.ComponenteFamiglia;
import it.csi.moon.moonbobl.dto.moonfobl.CampoModulo;
import it.csi.moon.moonbobl.dto.moonfobl.Categoria;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaMoonsrv;
import it.csi.moon.moonbobl.dto.moonfobl.Modulo;
import it.csi.moon.moonbobl.dto.moonfobl.ModuloClass;
import it.csi.moon.moonbobl.dto.moonfobl.ProtocolloParametro;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.dto.moonfobl.ValutazioneModuloSintesi;
import it.csi.moon.moonbobl.dto.moonfobl.VerificaPagamento;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundBusinessException;

public interface MoonsrvDAO {

	public String pingMoonsrv() throws DAOException;
	
	public List<Modulo> getModuli(ModuliFilter filter) throws DAOException;
	public Modulo getModuloById(Long idModulo, Long idVersioneModulo) throws DAOException;
	public Modulo getModuloByCodice(String codiceModulo, String versione) throws DAOException;
	
	public Istanza initIstanza(UserInfo user, Long idModulo) throws DAOException;
	public byte[] getPdfByIdIstanza(UserInfo user, Long idIstanza) throws DAOException;

	public String sendEmail(EmailRequest request) throws DAOException;
	
	public String sendEmailWithAttachment(EmailRequest request) throws DAOException;
	
	public String protocolla(Long idIstanza) throws DAOException;
		
	public List<Categoria> getCategorie() throws DAOException;
	
	public String protocollaIntegrazione(Long idIstanza, Long idStoricoWorkflow) throws DAOException;

	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws DAOException;
	
	public String inviaRispostaIntegrazione(Long idIstanza) throws DAOException;
	
	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type) throws DAOException;
	public List<ProtocolloParametro> getProtocolloParametri(Long idModulo, Long idVersioneModulo) throws DAOException;
	
	public String protocollaFile(Long idFile) throws DAOException;
	
//	public String rigeneraSalvaPdf(Long idIstanza) throws DAOException;

	public  byte[] generaPdf(IstanzaMoonsrv istanza) throws DAOException;
	
	public  byte[] generaPdf(Long idIstanza) throws DAOException;
	
	public  byte[] getAllegatoFruitore(String codice) throws DAOException;

	public List<CampoModulo> getCampiModulo(Long idModulo, Long idVersioneModulo, String type, String onlyFirstLevel)
			throws DAOException;

	public Istanza initIstanza(String ipAdress, UserInfo user, Long idModulo, Long idVersioneModulo,
			IstanzaInitBLParams params) throws ItemNotFoundBusinessException, DAOException;
	
	public String creaTicketCrm(Long idIstanza) throws DAOException;

	public ModuloClass uploadModuloClass(Long idModulo, int idTipologia, byte[] bytes) throws DAOException;
	public void deleteModuloClass(Long idModulo, int idTipologia) throws DAOException;
	
	public byte[] getContenutoIndexByUid(String uuidIndex);

	public String getPrintMapperName(String codiceModulo) throws DAOException;
	public String getProtocolloManagerName(String codiceModulo) throws DAOException;
	public String getEpayManagerName(String codiceModulo) throws DAOException;
	
	public Modulo getModuloPubblicatoByCodice(String codiceModulo) throws DAOException;

	public VerificaPagamento getVerificaPagamento(String idEpay) throws DAOException;

	public List<ValutazioneModuloSintesi> getValutazioneModuloSintesi( Long idModulo) throws DAOException;
	
	public List<ComponenteFamiglia> getFamigliaANPR(String codiceFiscale, String clientProfileKey, String ipAdress, String utente) throws DAOException;
	
	public String pubblicaFileMyDocs(Long idFile) throws DAOException;
	
//	public String pubblicaFileMyDocs(Long idFile, Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	
	public String pubblicaMyDocs(Long idIstanza, Long idStoricoWorkflow) throws DAOException;
	
	public String pubblicaFileMyDocs(Long idFile, Long idRichiesta) throws DAOException;

	public String pubblicaIstanzaMyDocs(Long idIstanza, Long idRichiesta) throws DAOException;
	
	public List<MyDocsRichiestaEntity> getLogRichiesteMyDocs(Long idIstanza) throws DAOException;

	public byte[] getReport(String codiceModulo, String codiceEstrazione, Date createdStart,
			Date createdEnd) throws DAOException;

	public String richiestaNotifyByIdIstanza(Long idIstanza) throws DAOException;
	
}
