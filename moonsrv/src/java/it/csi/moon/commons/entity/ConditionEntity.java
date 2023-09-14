/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * 
 * Tabella moon_wf_t_condition
 * PK: idCondition
 * AK: codiceCondition
 * 
 * @author Laurent
 *
 */
public class ConditionEntity {
	
	private Long idCondition;
	private String codiceCondition;
	private String descCondition;
	private String codiceEngine;
	private String script;
	
	public Long getIdCondition() {
		return idCondition;
	}
	public void setIdCondition(Long idCondition) {
		this.idCondition = idCondition;
	}
	public String getCodiceCondition() {
		return codiceCondition;
	}
	public void setCodiceCondition(String codiceCondition) {
		this.codiceCondition = codiceCondition;
	}
	public String getDescCondition() {
		return descCondition;
	}
	public void setDescCondition(String descCondition) {
		this.descCondition = descCondition;
	}
	public String getCodiceEngine() {
		return codiceEngine;
	}
	public void setCodiceEngine(String codiceEngine) {
		this.codiceEngine = codiceEngine;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}

}
