/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.script;

import java.util.List;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.commons.dto.Modulo;
import it.csi.moon.commons.entity.ModuloAttributoEntity;
import it.csi.moon.commons.util.MapModuloAttributi;
import it.csi.moon.moonsrv.business.service.IstanzeService;
import it.csi.moon.moonsrv.business.service.ModuliService;
import it.csi.moon.moonsrv.business.service.impl.dao.ModuloAttributiDAO;

@Component
public class ScriptEngineManager {

	@Autowired
	IstanzeService istanzeService;
	@Autowired
	ModuliService moduliService;
	@Autowired
	ModuloAttributiDAO moduloAttributiDAO;
	
	public ScriptEngine getEngine(String codiceEngine) {
		return getEngine(codiceEngine, null);
	}
	
	public ScriptEngine getEngine(String codiceEngine, Long idIstanza) {
		if (codiceEngine==null) {
			return null;
		}
		ScriptEngine engine = null;
		ScriptEngineUtils seu = null;
		Istanza istanza = null;
		Modulo modulo = null;
		final javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
	    engine = manager.getEngineByName("nashorn");
		switch (codiceEngine) {
			case "JS": // Without utis
				break;
			case "JS_I": // Istanza
				seu = new ScriptEngineUtils(getIstanza(idIstanza));
			    engine.setBindings(seu, ScriptContext.GLOBAL_SCOPE);
			    engine.put("utils", seu);
				break;
			case "JS_IA": // Istanza + Modulo + ModuloAttributi
				istanza = getIstanza(idIstanza);
				seu = new ScriptEngineUtils(istanza, modulo, getModuloAttributi(istanza.getModulo().getIdModulo()));
			    engine.setBindings(seu, ScriptContext.GLOBAL_SCOPE);
			    engine.put("utils", seu);
				break;
			default:
		}
		return engine;
	}


	private Istanza getIstanza(Long idIstanza) {
		return istanzeService.getIstanzaById(idIstanza);
	}
	
	private MapModuloAttributi getModuloAttributi(Long idModulo) {
		List<ModuloAttributoEntity> listAttributi = moduloAttributiDAO.findByIdModulo(idModulo);
		MapModuloAttributi attributi = new MapModuloAttributi(listAttributi);
		return attributi;
	}

}
