/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.dto;

import java.util.Optional;

import it.csi.moon.commons.dto.IstanzaInitParams;
import it.csi.moon.commons.util.MapModuloAttributi;

/**
 * Parametri payload per la richiesta di inizializzazione di un istanza per un determinato modulo identificato
 * <br>Servizio /istanze/init/{@code idModulo}
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class IstanzaInitCompletedParams {

	private IstanzaInitParams istanzaInitParams;
	private MapModuloAttributi moduloAttributi;
	private NotificatoreDatiInit notificatoreDatiInit;
	
	public IstanzaInitCompletedParams() {
		super();
	}
	public IstanzaInitCompletedParams(IstanzaInitParams initParams) {
		super();
		this.istanzaInitParams = initParams;
	}

	//
	public IstanzaInitParams getIstanzaInitParams() {
		return istanzaInitParams;
	}
	public void setIstanzaInitParams(IstanzaInitParams istanzaInitParams) {
		this.istanzaInitParams = istanzaInitParams;
	}
	public MapModuloAttributi getModuloAttributi() {
		return moduloAttributi;
	}
	public void setModuloAttributi(MapModuloAttributi moduloAttributi) {
		this.moduloAttributi = moduloAttributi;
	}
	public Optional<NotificatoreDatiInit> getNotificatoreDatiInit() {
		return Optional.ofNullable(notificatoreDatiInit);
	}
	public void setNotificatoreDatiInit(NotificatoreDatiInit notificatoreDatiInit) {
		this.notificatoreDatiInit = notificatoreDatiInit;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class IstanzaInitCompletedParams {\n");
		sb.append("    istanzaInitParams: ").append(toIndentedString(istanzaInitParams)).append("\n");
		sb.append("    moduloAttributi: ").append(toIndentedString(moduloAttributi)).append("\n");
		sb.append("    notificatoreDatiInit: ").append(toIndentedString(notificatoreDatiInit)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
  
}

