/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

public class BuildInfo {

	private String version = null;
	private String buildTime = null;
	private String target = null;

	public BuildInfo() {
		super();
	}

	public BuildInfo(String version, String buildTime) {
		super();
		this.version = version;
		this.buildTime = buildTime;
	}
	public BuildInfo(String version, String buildTime, String target) {
		super();
		this.version = version;
		this.buildTime = buildTime;
		this.target = target;
	}

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBuildTime() {
		return buildTime;
	}
	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BuildInfo obj = (BuildInfo) o;
		return Objects.equals(version, obj.version) && 
			Objects.equals(buildTime, obj.buildTime) && 
			Objects.equals(target, obj.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, buildTime, target);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BuildInfo {\n");
		sb.append("    version: ").append(toIndentedString(version)).append("\n");
		sb.append("    buildTime: ").append(toIndentedString(buildTime)).append("\n");
		sb.append("    target: ").append(toIndentedString(target)).append("\n");
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
