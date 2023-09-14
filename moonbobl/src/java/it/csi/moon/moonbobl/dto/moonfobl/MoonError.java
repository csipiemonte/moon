/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;


/**
 * Error restituita dai servizi REST
 * 
 * @author Francesco
 *
 * @since 1.0.0
 */
public class MoonError	 {
	
	private static final long serialVersionUID = 1L;
	private String msg= null;
	private String code = "MOONSRV-00000";
	private String title = "ERRORE";
	
	public MoonError() {
		super();
	}
	
	public MoonError(String msg, String code, String title) {
		super();
		this.msg = msg;
		this.code = code;
		this.title = title;
	}

	@ApiModelProperty(value = "")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	@ApiModelProperty(value = "")
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@ApiModelProperty(value = "")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MoonError error = (MoonError) o;
		return Objects.equals(code, error.code) &&
				Objects.equals(msg, error.msg) &&
				Objects.equals(title, error.title);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, msg, title);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MoonError {\n");
		sb.append("		code: ").append(toIndentedString(code)).append("\n");
		sb.append("		msg: ").append(toIndentedString(msg)).append("\n");
		sb.append("		title: ").append(toIndentedString(title)).append("\n");
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
		return o.toString().replace("\n", "\n		");
	}
}

