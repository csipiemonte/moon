/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.moonbobl.business.service.mapper;

import java.util.Date;

import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaCronologiaStatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaDatiEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.IstanzaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.ModuloVersionatoEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StatoEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.IstanzaMoonsrv;

/**
 * @author franc
 *
 */
public class IstanzaMapper {
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, ModuloVersionatoEntity moduloEntity,
			String nomeComune, Date dataInvio) {
		
		Istanza istanza = new Istanza();
		
		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
//		istanza.setNomeDichiarante((StringUtils.isEmpty(entity.getNomeDichiarante())?"":entity.getNomeDichiarante())+ " "+(StringUtils.isEmpty(entity.getCognomeDichiarante())?"":entity.getCognomeDichiarante()));
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntity(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
		istanza.setDataStato(entity.getDataStato());
		istanza.setModified(null); // Valorizzato dopo
		istanza.setData(null); // TODO : il piu importante !!! I DATI !!!
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapper.buildFromModuloVersionatoEntity(moduloEntity));
		}
		istanza.setAttoreIns(entity.getAttoreIns());
		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setImportanza(entity.getImportanza());
		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
		istanza.setMetadata(nomeComune);
//		istanza.setDataInvio(dataInvio); // TODO DA ELIMINARE
		istanza.setIdEnte(entity.getIdEnte());
		
		return istanza;
	}
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, ModuloVersionatoEntity moduloEntity,
			String nomeComune, Date dataInvio, Boolean isPagato) {
		
		Istanza istanza = new Istanza();
		
		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
//		istanza.setNomeDichiarante((StringUtils.isEmpty(entity.getNomeDichiarante())?"":entity.getNomeDichiarante())+ " "+(StringUtils.isEmpty(entity.getCognomeDichiarante())?"":entity.getCognomeDichiarante()));
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntity(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
		istanza.setDataStato(entity.getDataStato());
		istanza.setModified(null); // Valorizzato dopo
		istanza.setData(null); // TODO : il piu importante !!! I DATI !!!
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapper.buildFromModuloVersionatoEntity(moduloEntity));
		}
		istanza.setAttoreIns(entity.getAttoreIns());
		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setImportanza(entity.getImportanza());
		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
		istanza.setMetadata(nomeComune);
//		istanza.setDataInvio(dataInvio); // TODO DA ELIMINARE
		istanza.setIdEnte(entity.getIdEnte());
		
		istanza.setIsPagato(isPagato);
		
		return istanza;
	}
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloE,
			String nomeComune, Date dataInvio) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoE, moduloE, nomeComune, dataInvio);
		istanza.setModified(cronE.getDataInizio());
		istanza.setData(datiE.getDatiIstanza());
		return istanza;
	}

	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloE) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoE, moduloE, null, null);
		istanza.setModified(cronE.getDataInizio());
		istanza.setData(datiE.getDatiIstanza());
		return istanza;
	}

//	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, String datiE, StatoEntity statoE,
//			ModuloVersionatoEntity moduloE) {
//		Istanza istanza = buildFromIstanzaEntity(entity, statoE, moduloE, null, null);
//		istanza.setData(datiE);
//		return istanza;
//	}
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, ModuloVersionatoEntity moduloEntity,
			String nomeComune, Date dataInvio, String operatore) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoEntity, moduloEntity, nomeComune, dataInvio);
		istanza.setOperatore(operatore);
		return istanza;
	}
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, ModuloVersionatoEntity moduloEntity,
			String nomeComune, Date dataInvio, String operatore, Boolean isPagato) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoEntity, moduloEntity, nomeComune, dataInvio,isPagato);
		istanza.setOperatore(operatore);
		return istanza;
	}
	
	public static IstanzaMoonsrv buildIstanzaMoonsrvFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, ModuloVersionatoEntity moduloEntity) {
		
		IstanzaMoonsrv istanza = new IstanzaMoonsrv();
		
		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
//		istanza.setNomeDichiarante((StringUtils.isEmpty(entity.getNomeDichiarante())?"":entity.getNomeDichiarante())+ " "+(StringUtils.isEmpty(entity.getCognomeDichiarante())?"":entity.getCognomeDichiarante()));
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntity(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
		//istanza.setDataStato(entity.getDataStato());
		istanza.setModified(null); // Valorizzato dopo
		istanza.setData(null); // TODO : il piu importante !!! I DATI !!!
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapper.buildModuloMoosrvFromModuloVersionatoEntity(moduloEntity));
		}
		istanza.setAttoreIns(entity.getAttoreIns());
		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setImportanza(entity.getImportanza());
		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
		istanza.setMetadata("");
//		istanza.setDataInvio(dataInvio); // TODO DA ELIMINARE
		istanza.setIdEnte(entity.getIdEnte());
		
		return istanza;
	}
	
	public static IstanzaMoonsrv buildIstanzaMoonsrvFromIstanzaEntity(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloE) {
		IstanzaMoonsrv istanza = buildIstanzaMoonsrvFromIstanzaEntity(entity, statoE, moduloE);
		istanza.setModified(cronE.getDataInizio());
		istanza.setData(datiE.getDatiIstanza());
		return istanza;
	}
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, 
			ModuloVersionatoEntity moduloEntity/*, MapModuloAttributi mapModuloAttributi*/) {
		
		Istanza istanza = new Istanza();
		
		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntity(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
		istanza.setModified(null); // valorizzate con IstanzaCronologiaStatiEntity
		istanza.setData(null); // valorizzate con IstanzaDatiEntity
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapper.buildFromModuloEntity(moduloEntity/*, mapModuloAttributi*/));
		}
		istanza.setAttoreIns(entity.getAttoreIns());
		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setImportanza(entity.getImportanza());
		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
		istanza.setIdEnte(entity.getIdEnte());
		
		return istanza;
	}

	
//	public static Istanza buildFromIstanzaEntity(IstanzaEntity eIstanza,
//			IstanzaCronologiaStatiEntity eIstanzaCronologia, IstanzaDatiEntity eIstanzaDati, StatoEntity statoE,
//			ModuloVersionatoEntity moduloE/*, MapModuloAttributi attributi*/) {		
//		Istanza istanza = buildFromIstanzaEntity(eIstanza, statoE, moduloE/*,  attributi*/);
//		istanza.setData(eIstanzaDati);
//		return istanza;
//	}
	
		

}
