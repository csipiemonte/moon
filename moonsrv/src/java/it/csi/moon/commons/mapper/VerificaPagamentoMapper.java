/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

/**
 * 
 */
package it.csi.moon.commons.mapper;

import java.util.Base64;

import it.csi.moon.commons.dto.VerificaPagamento;
import it.csi.moon.commons.dto.extra.epay.GetRtResponse;

/**
 * @author franc
 *
 */
public class VerificaPagamentoMapper {
	
	public static VerificaPagamento buildFromRT(GetRtResponse rt) {
		VerificaPagamento vp = new VerificaPagamento();
		vp.setIdEpay(rt.getIdentificativoPagamento());
		vp.setCodiceEsito(rt.getCodiceEsito());
		vp.setDescrizioneEsito(rt.getDescrizioneEsito());
		vp.setDescrizioneStatoPagamento(rt.getDescrizioneStatoPagamento());
		vp.setIuvOriginario(rt.getIuvOriginario());
		vp.setIuvEffettivo(rt.getIuvOriginario());
		vp.setRicevutaPdf(rt.getRicevutaPdf()!=null?rt.getRicevutaPdf().getBytes():null);
		if (rt.getRtXml()!=null) {
			byte[] decodedBytes = Base64.getDecoder().decode(rt.getRtXml());
			String decodedXml = new String(decodedBytes);
			vp.setRtXml(decodedXml);
		}
		return vp;
	}

}
