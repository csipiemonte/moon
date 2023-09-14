/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Map;

public class SysInfo {

	private String ipAddress = null;
	private String hostName = null;
	private String canonicalHostName = null;
	private Map<String,String> env = null;
	private Map<String,String> prop = null;
    
	public SysInfo() {
		super();
	}

	public SysInfo(String ipAddress, String hostName, String canonicalHostName) {
		super();
		this.ipAddress = ipAddress;
		this.hostName = hostName;
		this.canonicalHostName = canonicalHostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getCanonicalHostName() {
		return canonicalHostName;
	}
	public void setCanonicalHostName(String canonicalHostName) {
		this.canonicalHostName = canonicalHostName;
	}

	public Map<String,String> getEnv() {
		return env;
	}
	public void setEnv(Map<String,String> env) {
		this.env = env;
	}
	public Map<String,String> getProp() {
		return prop;
	}
	public void setProp(Map<String,String> prop) {
		this.prop = prop;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class SysInfo {\n");
		sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
		sb.append("    hostName: ").append(toIndentedString(hostName)).append("\n");
		sb.append("    canonicalHostName: ").append(toIndentedString(canonicalHostName)).append("\n");
		sb.append("    env: ").append(toIndentedString(env)).append("\n");
		sb.append("    prop: ").append(toIndentedString(prop)).append("\n");
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
