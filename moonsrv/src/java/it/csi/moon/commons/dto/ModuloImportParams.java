/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

public class ModuloImportParams {

	public enum EnumModuloImportModalita {
		IMPORT("IMPORT"), IMPORT_ONLY_STRUTTURE("IMPORT_ONLY_STRUTTURE"), DIFF("DIFF"), ;
		private String codice;
		EnumModuloImportModalita(String codice) {this.codice=codice;}
		public String getCodice() { return codice; }
		public static EnumModuloImportModalita byCodice(String codice) {
			for(EnumModuloImportModalita e : values()) {
				if(e.getCodice().equals(codice)) { return e; }
			}
			return null;
		}
	}

	private EnumModuloImportModalita modalita;
	private String codiceModuloTarget;
	
	public EnumModuloImportModalita getModalita() {
		return modalita;
	}
	public void setModalita(EnumModuloImportModalita modalita) {
		this.modalita = modalita;
	}
	public String getCodiceModuloTarget() {
		return codiceModuloTarget;
	}
	public void setCodiceModuloTarget(String codiceModuloTarget) {
		this.codiceModuloTarget = codiceModuloTarget;
	}
	
}
