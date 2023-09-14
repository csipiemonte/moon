/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Ed03 {
	private List<Struttura03> struttura03;

	public List<Struttura03> getStruttura03() {
		return struttura03;
	}

	public void setStruttura03(List<Struttura03> struttura03) {
		this.struttura03 = struttura03;
	}
	
}
