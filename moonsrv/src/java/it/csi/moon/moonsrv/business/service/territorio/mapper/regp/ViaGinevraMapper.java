/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.territorio.mapper.regp;

import it.csi.moon.commons.dto.extra.territorio.Via;

public class ViaGinevraMapper {
	
	private static final String CLASS_NAME = "ViaGinevraMapper";
	
	// FROM Sedime
	public static Via remapSedime(it.csi.territorio.ginevra.lanci.sedime.cxfclient.Sedime sedime) {
		if(sedime==null)
			return null;
		
		Via result = new Via(Integer.valueOf(String.valueOf(sedime.getId())), sedime.getPreposizione()==null?sedime.getSedime():String.join(" ", sedime.getSedime(), sedime.getPreposizione()));
		return result;
	}
	
	// FROM ViaComunale
	public static Via remapViaComunale(it.csi.territorio.ginevra.lanci.via.cxfclient.ViaComunale viaComunale) {
		if(viaComunale==null)
			return null;
		
//		Via result = new Via(Integer.valueOf(String.valueOf(viaComunale.getIdL2())), String.join(" ",viaComunale.getSedime(), viaComunale.getNomeBreve(), String.valueOf(viaComunale.getIdComune()), String.valueOf(viaComunale.getIdLocalita()), viaComunale.getNomeLocalita()).toUpperCase());
		Via result = new Via(Integer.valueOf(String.valueOf(viaComunale.getIdL2())), String.join(" ",viaComunale.getSedime(), viaComunale.getNomeBreve()).toUpperCase());
		return result;
	}
	
	// FROM Civico
	public static Via remapCivico(it.csi.territorio.ginevra.lanci.civico.cxfclient.Civico civico) {
		if(civico==null)
			return null;
		
		Via result = new Via(Integer.valueOf(String.valueOf(civico.getId())), String.join("   ", civico.getGeometria(), String.valueOf(civico.getIdArcoRiferimento()), civico.getIndirizzoStampa(), String.valueOf(civico.getNumero()), civico.getSubalterno(), civico.getTipoAcquisizione(), civico.getTipoCivico()));
		return result;
	}
	
}
