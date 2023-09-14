/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportVerificaAttributiModulo {
	
	private boolean confValida = true;
	private Long idModulo;
	private String categoriaAttributi;
	private List<String> messaggi;
	
	public ReportVerificaAttributiModulo() {
		super();
	}
	public ReportVerificaAttributiModulo(Long idModulo, String categoriaAttributi) {
		super();
		this.idModulo = idModulo;
		this.categoriaAttributi = categoriaAttributi;
	}
	
	public boolean isConfValida() {
		return confValida;
	}
	public void setConfValida(boolean confValida) {
		this.confValida = confValida;
	}
	public final Long getIdModulo() {
		return idModulo;
	}
	public final void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public final String getCategoriaAttributi() {
		return categoriaAttributi;
	}
	public final void setCategoriaAttributi(String categoriaAttributi) {
		this.categoriaAttributi = categoriaAttributi;
	}
	public final List<String> getMessaggi() {
		return messaggi;
	}
	public final void setMessaggi(List<String> messaggi) {
		this.messaggi = messaggi;
		if (messaggi!=null && messaggi.size()>0) {
			this.confValida = false;
		}
	}
	public final void addErrorMessagge(String messagge) {
		if (this.messaggi == null) {
			this.messaggi = new ArrayList<>();
		}
		this.messaggi.add(messagge);
		this.confValida = false;
	}
	
	@Override
	public String toString() {
		return "ReportVerificaAttributiModulo [confValida=" + confValida + ", idModulo=" + idModulo
				+ ", categoriaAttributi=" + categoriaAttributi + ", messaggi=" + messaggi + "]";
	}

}
