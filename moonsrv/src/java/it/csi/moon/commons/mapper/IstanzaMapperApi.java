/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import java.util.Date;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.entity.IstanzaCronologiaStatiEntity;
import it.csi.moon.commons.entity.IstanzaDatiEntity;
import it.csi.moon.commons.entity.IstanzaEntity;
import it.csi.moon.commons.entity.ModuloVersionatoEntity;
import it.csi.moon.commons.entity.StatoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;

/**
 * Contruttore di oggetto JSON Modulo per le istanze 
 *  da IstanzaEntity {@code entity}, StatoEntity {@code statoEntity}, ModuloEntity {@code moduloEntity}
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaMapperApi {
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloEntity) {
		
		Istanza istanza = new Istanza();
		
//		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
		if (statoE!=null) {
			istanza.setStato(StatoMapper.buildFromEntityForApi(statoE));
		}
		istanza.setCreated(entity.getDataCreazione());
//		istanza.setModified(null); // valorizzate con IstanzaCronologiaStatiEntity
		istanza.setData(null); // valorizzate con IstanzaDatiEntity
		istanza.setModulo(ModuloMapperApi.buildFromModuloVersionatoEntity(moduloEntity));
//		istanza.setAttoreIns(entity.getAttoreIns());
//		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
//		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
//		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setImportanza(entity.getImportanza());
//		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setCurrentStep(null);
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
//		istanza.setIdEnte(entity.getIdEnte());
//		istanza.setModified(cronE.getDataInizio());
		istanza.setData(datiE.getDatiIstanza());
//		istanza.setDatiAggiuntivi(entity.getDatiAggiuntivi());

		return istanza;
	}
	
	
	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, StatoEntity statoEntity, 
			ModuloVersionatoEntity moduloEntity/*, MapModuloAttributi mapModuloAttributi*/) {
		
		Istanza istanza = new Istanza();
		
//		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntityForApi(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
//		istanza.setModified(null); // valorizzate con IstanzaCronologiaStatiEntity
		istanza.setData(null); // valorizzate con IstanzaDatiEntity
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapperApi.buildFromModuloEntity(moduloEntity/*, mapModuloAttributi*/));
		}
//		istanza.setAttoreIns(entity.getAttoreIns());
//		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
//		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
//		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setFlagArchiviata(null);
		istanza.setFlagTest(null);
		istanza.setImportanza(entity.getImportanza());
//		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setCurrentStep(null);
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
//		istanza.setIdEnte(entity.getIdEnte());
		
		return istanza;
	}

//	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
//			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloE, MapModuloAttributi mapModuloAttributi) {
//		Istanza istanza = buildFromIstanzaEntity(entity, statoE, moduloE/*,  mapModuloAttributi*/);
//		istanza.setModified(cronE.getDataInizio());
//		istanza.setData(datiE.getDatiIstanza());
//		return istanza;
//	}

	public static Istanza buildFromIstanzaEntity(IstanzaEntity entity, String datiE, StatoEntity statoE,
			ModuloVersionatoEntity moduloE, MapModuloAttributi mapModuloAttributi) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoE, moduloE/*,  mapModuloAttributi*/);
		istanza.setData(datiE);
		return istanza;
	}

	// FOR BOdirect
	public static Istanza buildFromIstanzaEntityWithComuneDtInvio(IstanzaEntity entity, StatoEntity statoEntity, 
			ModuloVersionatoEntity moduloEntity, /*MapModuloAttributi attributi,*/
			String nomeComune, Date dataInvio) {
		Istanza istanza = buildFromIstanzaEntity(entity, statoEntity, moduloEntity/*, attributi*/);
		istanza.setMetadata(nomeComune);
		return istanza;
	}
	public static Istanza buildFromIstanzaEntityWithComuneDtInvio(IstanzaEntity entity, IstanzaCronologiaStatiEntity cronE,
			IstanzaDatiEntity datiE, StatoEntity statoE, ModuloVersionatoEntity moduloE, MapModuloAttributi attributi,
			String nomeComune, Date dataInvio) {
		Istanza istanza = buildFromIstanzaEntity(entity, cronE, datiE, statoE, moduloE/*, attributi*/);
		istanza.setModified(cronE.getDataInizio());
		istanza.setData(datiE.getDatiIstanza());
		return istanza;
	}
	
	public static Istanza buildFromIstanzaEntityForApi(IstanzaEntity entity, StatoEntity statoEntity, 
			ModuloVersionatoEntity moduloEntity, MapModuloAttributi mapModuloAttributi) {
		Istanza istanza = new Istanza();
		
//		istanza.setIdIstanza(entity.getIdIstanza());
		istanza.setCodiceIstanza(entity.getCodiceIstanza());
		istanza.setIdentificativoUtente(entity.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(entity.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(entity.getCognomeDichiarante());
		istanza.setNomeDichiarante(entity.getNomeDichiarante());
		if (statoEntity!=null) {
			istanza.setStato(StatoMapper.buildFromEntityForApi(statoEntity));
		}
		istanza.setCreated(entity.getDataCreazione());
//		istanza.setModified(null); // valorizzate con IstanzaCronologiaStatiEntity
		istanza.setData(null); // valorizzate con IstanzaDatiEntity
		if (moduloEntity!=null) {
			istanza.setModulo(ModuloMapperApi.buildFromModuloEntity(moduloEntity/*, mapModuloAttributi*/));
		}
//		istanza.setAttoreIns(entity.getAttoreIns());
//		istanza.setAttoreUpd(entity.getAttoreUpd());
		istanza.setFlagEliminata("S".equals(entity.getFlagEliminata()));
//		istanza.setFlagArchiviata("S".equals(entity.getFlagArchiviata()));
//		istanza.setFlagTest("S".equals(entity.getFlagTest()));
		istanza.setFlagArchiviata(null);
		istanza.setFlagTest(null);
		istanza.setImportanza(entity.getImportanza());
//		istanza.setCurrentStep(entity.getCurrentStep());
		istanza.setCurrentStep(null);
		istanza.setNumeroProtocollo(entity.getNumeroProtocollo());
		istanza.setDataProtocollo(entity.getDataProtocollo());
//		istanza.setIdEnte(entity.getIdEnte());
		
		return istanza;
//		result.setIdIstanza(null);
//		result.getStato().setIdStato(null);
//		result.setIdEnte(null);
//		return istanza;
	}
	
	public static Istanza buildFromIstanzaDuplicata(Istanza istanzaDuplicata) {
		
		Istanza istanza = new Istanza();
		
		istanza.setCodiceIstanza(istanzaDuplicata.getCodiceIstanza());
		istanza.setIdentificativoUtente(istanzaDuplicata.getIdentificativoUtente());
		istanza.setCodiceFiscaleDichiarante(istanzaDuplicata.getCodiceFiscaleDichiarante());
		istanza.setCognomeDichiarante(istanzaDuplicata.getCognomeDichiarante());
		istanza.setNomeDichiarante(istanzaDuplicata.getNomeDichiarante());	
		istanza.setStato(istanzaDuplicata.getStato());		
		istanza.setCreated(istanzaDuplicata.getCreated());
		istanza.setData(istanzaDuplicata.getData()); 
		istanza.setModulo(istanzaDuplicata.getModulo());
		istanza.setFlagEliminata(istanzaDuplicata.getFlagEliminata());
		istanza.setImportanza(istanzaDuplicata.getImportanza());
		istanza.setCurrentStep(null);

		return istanza;
	}
}
