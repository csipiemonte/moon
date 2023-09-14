/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.dto.moonfobl;

public class EmbeddedNavParams {

	private String codiceIstanza;
	private String codiceModulo;
	private Long idIstanza;
	private Long idModulo;
	private Long idVersioneModulo;

	public EmbeddedNavParams() {
		super();
	}
	public EmbeddedNavParams(EmbeddedNavParams embeddedNavParamsToClone) {
		super();
		this.codiceIstanza = embeddedNavParamsToClone.getCodiceIstanza();
		this.codiceModulo = embeddedNavParamsToClone.getCodiceModulo();
		this.idIstanza = embeddedNavParamsToClone.getIdIstanza();
		this.idModulo = embeddedNavParamsToClone.getIdModulo();
		this.idVersioneModulo = embeddedNavParamsToClone.getIdVersioneModulo();
	}

	public static class Builder {
		private String _codiceIstanza;
		private String _codiceModulo;
		private Long _idIstanza;
		private Long _idModulo;
		private Long _idVersioneModulo;

		public Builder codiceIstanza(String codiceIstanza) {
			this._codiceIstanza = codiceIstanza;
			return this;
		}

		public Builder idIstanza(Long idIstanza) {
			this._idIstanza = idIstanza;
			return this;
		}

		public Builder idModulo(Long idModulo) {
			this._idModulo = idModulo;
			return this;
		}

		public Builder idVersioneModulo(Long idVersioneModulo) {
			this._idVersioneModulo = idVersioneModulo;
			return this;
		}

		public Builder codiceModulo(String codiceModulo) {
			this._codiceModulo = codiceModulo;
			return this;
		}

		public EmbeddedNavParams build() {
			return new EmbeddedNavParams(this);
		}
	}

	public EmbeddedNavParams(Builder builder) {
		setCodiceIstanza(builder._codiceIstanza);
		setIdIstanza(builder._idIstanza);
		setIdModulo(builder._idModulo);
		setIdVersioneModulo(builder._idVersioneModulo);
		setCodiceModulo(builder._codiceModulo);
	}

	/**
	 * @return the codiceIstanza
	 */
	public String getCodiceIstanza() {
		return codiceIstanza;
	}

	/**
	 * @return the codiceModulo
	 */
	public String getCodiceModulo() {
		return codiceModulo;
	}

	/**
	 * @return the idIstanza
	 */
	public Long getIdIstanza() {
		return idIstanza;
	}

	/**
	 * @return the idModulo
	 */
	public Long getIdModulo() {
		return idModulo;
	}

	/**
	 * @return the idVersioneModulo
	 */
	public Long getIdVersioneModulo() {
		return idVersioneModulo;
	}

	/**
	 * @param codiceIstanza the codiceIstanza to set
	 */
	public void setCodiceIstanza(String codiceIstanza) {
		this.codiceIstanza = codiceIstanza;
	}

	/**
	 * @param codiceModulo the codiceModulo to set
	 */
	public void setCodiceModulo(String codiceModulo) {
		this.codiceModulo = codiceModulo;
	}

	/**
	 * @param idIstanza the idIstanza to set
	 */
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}

	/**
	 * @param idModulo the idModulo to set
	 */
	public void setIdModulo(Long idModulo) {
		this.idModulo = idModulo;
	}

	/**
	 * @param idVersioneModulo the idVersioneModulo to set
	 */
	public void setIdVersioneModulo(Long idVersioneModulo) {
		this.idVersioneModulo = idVersioneModulo;
	}

}
