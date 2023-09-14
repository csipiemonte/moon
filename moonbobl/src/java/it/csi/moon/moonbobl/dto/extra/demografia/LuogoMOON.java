/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.extra.demografia;

public class LuogoMOON {

	Nazione nazione;
//	Regione regione;
//	Provincia provincia;
//	Comune comune;
//	String comuneEstero;
	String descrizioneLuogo;
	
	
	public Nazione getNazione() {
		return nazione;
	}
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}
//	public Regione getRegione() {
//		return regione;
//	}
//	public void setRegione(Regione regione) {
//		this.regione = regione;
//	}
//	public Provincia getProvincia() {
//		return provincia;
//	}
//	public void setProvincia(Provincia provincia) {
//		this.provincia = provincia;
//	}
//	public Comune getComune() {
//		return comune;
//	}
//	public void setComune(Comune comune) {
//		this.comune = comune;
//	}
//	public String getComuneEstero() {
//		return comuneEstero;
//	}
//	public void setComuneEstero(String comuneEstero) {
//		this.comuneEstero = comuneEstero;
//	}
	public String getDescrizioneLuogo() {
		return descrizioneLuogo;
	}
	public void setDescrizioneLuogo(String descrizioneLuogo) {
		this.descrizioneLuogo = descrizioneLuogo;
	}

}
