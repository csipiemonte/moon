/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.wf;

import java.util.List;

import it.csi.cosmo.callback.v1.dto.AggiornaStatoPraticaRequest;
import it.csi.cosmo.callback.v1.dto.Esito;
import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.LogPraticaCosmo;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;

/**
 * Metodi di business di COSMO per le gestione del Workflow con Flowable
 * 
 * @author Laurent
 */
public interface CosmoService {

	public String creaPraticaEdAvviaProcesso(Long idIstanza) throws BusinessException;
	public String creaPraticaEdAvviaProcesso(Istanza istanza) throws BusinessException;
	public Esito callbackPutStatoPraticaV1(String idPratica, AggiornaStatoPraticaRequest pratica);

	public List<LogPraticaCosmo> getElencoLogPraticaByIdIstanza(Long idIstanza);
	public String inviaRispostaIntegrazione(Long idIstanza);
	public byte[] getAllegato(String idPratica);
	
}
