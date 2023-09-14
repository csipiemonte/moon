/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;
// Messaggi inviati dal client al server 
// mediante il prtocollo WebSocket

import it.csi.moon.commons.entity.EpayNotificaPagamentoEntity;

public class NotificaEpayWsMessage {
 String cmd;
 String iuv;
 EpayNotificaPagamentoEntity data;
 String authToken;
 
public String getCmd() {
	return cmd;
}
public void setCmd(String cmd) {
	this.cmd = cmd;
}
public String getIuv() {
	return iuv;
}
public void setIuv(String iuv) {
	this.iuv = iuv;
}
public EpayNotificaPagamentoEntity getData() {
	return data;
}
public void setData(EpayNotificaPagamentoEntity data) {
	this.data = data;
}
public String getAuthToken() {
	return authToken;
}
public void setAuthToken(String authToken) {
	this.authToken = authToken;
}
 
 
}
