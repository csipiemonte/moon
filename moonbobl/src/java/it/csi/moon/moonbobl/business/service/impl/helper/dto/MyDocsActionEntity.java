/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto;

import java.util.List;

/**
 * Entity per MyDocs
 * <br>
 * 
 * @author Danilo
 *
 * @since 1.0.0
 */
public class MyDocsActionEntity {

	private List<String> formIoFileNames;


	public List<String> getFormIoFileNames() {
		return formIoFileNames;
	}

	public void setFormIoFileNames(List<String> formIoFileNames) {
		this.formIoFileNames = formIoFileNames;
	}

	@Override
	public String toString() {
		return "MyDocsActionEntity [formIoFileNames=" + formIoFileNames + "]";
	}
	
		
}
