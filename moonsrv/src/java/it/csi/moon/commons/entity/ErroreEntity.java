/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/*
 * Entity relativo alla tabella moon_l_errori
 * 
 */
public class ErroreEntity {
	
	private String uuid;
	private Integer idComponente;
	private String inetAdress;
	private Date dataIns;
	private String attoreIns;
	private String codice;
	private String message;
	private String className;
	private String methodName;
	private Long idIstanza;
	private Long idModulo;
	private String exClassName;
	private String exMessage;
	private String exCause;
	private String exTrace;
	private String info;
	private Long elapsedTimeMs;
	private String note;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getIdComponente() {
		return idComponente;
	}
	public void setIdComponente(Integer idComponente) {
		this.idComponente = idComponente;
	}
	public String getInetAdress() {
		return inetAdress;
	}
	public void setInetAdress(String inetAdress) {
		this.inetAdress = inetAdress;
	}
	public Date getDataIns() {
		return dataIns;
	}
	public void setDataIns(Date dataIns) {
		this.dataIns = dataIns;
	}
	public String getAttoreIns() {
		return attoreIns;
	}
	public void setAttoreIns(String attoreIns) {
		this.attoreIns = attoreIns;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Long getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public Long getIdModulo() {
		return idModulo;
	}
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}
	public String getExClassName() {
		return exClassName;
	}
	public void setExClassName(String exClassName) {
		this.exClassName = exClassName;
	}
	public String getExMessage() {
		return exMessage;
	}
	public void setExMessage(String exMessage) {
		this.exMessage = exMessage;
	}
	public String getExCause() {
		return exCause;
	}
	public void setExCause(String exCause) {
		this.exCause = exCause;
	}
	public String getExTrace() {
		return exTrace;
	}
	public void setExTrace(String exTrace) {
		this.exTrace = exTrace;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Long getElapsedTimeMs() {
		return elapsedTimeMs;
	}
	public void setElapsedTimeMs(Long elapsedTimeMs) {
		this.elapsedTimeMs = elapsedTimeMs;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Override
	public String toString() {
		return "ErroreEntity [uuid=" + uuid + ", idComponente=" + idComponente + ", inetAdress=" + inetAdress
				+ ", dataIns=" + dataIns + ", attoreIns="
				+ attoreIns + ", codice=" + codice + ", message=" + message + ", className=" + className
				+ ", methodName=" + methodName + ", idIstanza=" + idIstanza + ", idModulo=" + idModulo + ", exClassName="
				+ exClassName + ", exMessage=" + exMessage + ", exCause=" + exCause + ", exTrace=" + exTrace
				+ ", info=" + info + ", elapsedTimeMs=" + elapsedTimeMs + ", note=" + note + "]";
	}
	
}
