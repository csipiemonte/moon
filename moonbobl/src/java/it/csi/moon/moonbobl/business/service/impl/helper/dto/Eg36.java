/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Eg36 {
	private List<Struttura36> struttura36;

	public List<Struttura36> getStruttura36() {
		return struttura36;
	}

	public void setStruttura36(List<Struttura36> struttura36) {
		this.struttura36 = struttura36;
	}
}
