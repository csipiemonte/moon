/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay;

import it.csi.moon.moonsrv.business.ws.epay.type.ResponseType;
import it.csi.moon.moonsrv.business.ws.epay.type.TrasmettiNotifichePagamentoRequest;
import it.csi.moon.moonsrv.business.ws.epay.type.TrasmettiRTRequest;

public interface EpayServiceCallback {

	public ResponseType riceviRicevutaTelematica(TrasmettiRTRequest rt);
	public ResponseType riceviNotificaPagamento(TrasmettiNotifichePagamentoRequest notifichePagamento);
	
}
