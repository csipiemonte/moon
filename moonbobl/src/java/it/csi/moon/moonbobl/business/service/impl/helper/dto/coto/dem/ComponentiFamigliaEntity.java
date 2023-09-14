/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.coto.dem;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity dei soggetti che hanno indicato numero patente o targa in cambio indirizzo
 * <br>
 * @author Alberto
 *
 * @since 1.0.0
 */
public class ComponentiFamigliaEntity {

	private String nominativo;
	private String numeroPatente;
	private List<String> targhe;

	public ComponentiFamigliaEntity() {
		super();
		targhe = new ArrayList<String>();
	}

	public String getNominativo() {
		return nominativo;
	}
	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	public String getNumeroPatente() {
		return numeroPatente;
	}
	public void setNumeroPatente(String numeroPatente) {
		this.numeroPatente = numeroPatente;
	}
	public List<String> getTarghe() {
		return targhe;
	}
	public void setTarghe(List<String> targhe) {
		this.targhe = targhe;
	}

	@Override
	public String toString() {
		return "ComponentiFamigliaEntity [nominativo=" + nominativo + ", numeroPatente=" + numeroPatente + ", targhe="
				+ targhe + "]";
	}

}
