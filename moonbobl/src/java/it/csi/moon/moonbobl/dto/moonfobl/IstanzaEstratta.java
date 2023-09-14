/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

public class IstanzaEstratta extends Istanza {
	
	int rowNumber;

	public IstanzaEstratta() {	
		super();
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
	public String toString() {
		return "IstanzaEstratta [rowNumber=" + rowNumber + "]";
	}



}
