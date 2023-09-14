/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.moonprint;

public class Metadata {
	public String dataPresentazione;
	public String qrContent;
	public String numeroIstanza;
	public HeaderFooter header;
	public HeaderFooter footer;
	
	@Override
	public String toString() {
		return "Metadata [dataPresentazione=" + dataPresentazione + ", qrContent=" + qrContent + ", numeroIstanza="
				+ numeroIstanza + ", header=" + header + ", footer=" + footer + "]";
	}
	
}
