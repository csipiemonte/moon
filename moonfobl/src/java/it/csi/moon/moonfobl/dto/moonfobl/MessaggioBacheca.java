/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

import it.csi.moon.commons.dto.Modulo;

public class MessaggioBacheca {
	
	private String tipo;
	private String messaggio;

	@Override
	public String toString() {
		return "MessaggioBacheca [tipo=" + tipo + ", messaggio=" + messaggio + "]";
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public MessaggioBacheca() {
		// TODO Auto-generated constructor stub
	}
	public MessaggioBacheca(String tipo, String messaggio) {
		this.tipo = tipo;
		this.messaggio = messaggio;
	}

}
