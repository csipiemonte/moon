/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ModuloExportInfo
 * 
 * @author Francesco
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ModuloExportInfo {

	private String version;
	private String from;
	private String databaseJndiName;
	private String databaseURL;
	private Date at;
	private List<String> warnings;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getDatabaseJndiName() {
		return databaseJndiName;
	}
	public void setDatabaseJndiName(String databaseJndiName) {
		this.databaseJndiName = databaseJndiName;
	}
	public String getDatabaseURL() {
		return databaseURL;
	}
	public void setDatabaseURL(String databaseURL) {
		this.databaseURL = databaseURL;
	}
	public Date getAt() {
		return at;
	}
	public void setAt(Date at) {
		this.at = at;
	}
	public List<String> getWarnings() {
		return warnings;
	}
	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}
	public void addWarning(String warning) {
		if(this.warnings == null) {
			this.warnings = new ArrayList<>();
		}
		this.warnings.add(warning);
	}
}
