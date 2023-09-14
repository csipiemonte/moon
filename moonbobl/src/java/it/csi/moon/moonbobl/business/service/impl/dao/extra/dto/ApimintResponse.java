/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.dto;

import java.util.UUID;

public class ApimintResponse {
	UUID uuid;
	String rispostaRequest;
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getRispostaRequest() {
		return rispostaRequest;
	}
	public void setRispostaRequest(String rispostaRequest) {
		this.rispostaRequest = rispostaRequest;
	}
	
	
}
