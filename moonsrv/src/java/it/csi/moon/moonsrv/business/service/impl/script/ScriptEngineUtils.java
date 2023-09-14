/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.script;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.dto.Pagamento;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonsrv.business.service.IstanzeService;

public class ScriptEngineUtils implements Bindings {

    private Istanza istanza = null;
    private Object modulo = null;
    private MapModuloAttributi attributi = null;
    
	@Autowired
    IstanzeService istanzeService;

	public ScriptEngineUtils() {
	}
	public ScriptEngineUtils(Istanza istanza) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		this.istanza = istanza;
	}
	public ScriptEngineUtils(Istanza istanza, Modulo modulo) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		this.istanza = istanza;
		this.modulo = modulo;
	}
	public ScriptEngineUtils(Istanza istanza, Modulo modulo, MapModuloAttributi attributi) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		this.istanza = istanza;
		this.modulo = modulo;
		this.attributi = attributi;
	}

	//
	public Istanza getIstanza() {
		return istanza;
	}
	public Object getModulo() {
		return modulo;
	}
	public MapModuloAttributi getAttributi() {
		return attributi;
	}

	//
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		// TODO Auto-generated method stub

	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	//
	//
	public boolean isIstanzaPayed() {
		this.istanza = istanzeService.completaIstanzaEpay(this.istanza);
		boolean result = false;
		if (this.istanza.getPagamenti() != null) {
			for (Pagamento p : this.istanza.getPagamenti()) {
				if (p.getNotifica()!=null && p.getNotifica().getImportoPagato()!=null) {
					result = true;
				}
			}
		}
		return result;
	}
	
}
