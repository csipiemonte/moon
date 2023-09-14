/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper.dto.regp;



/**
 * Entity per il rapporto di fine concessione
 * <br>
 * 
 * @author Alberto
 *
 * @since 1.0.0
 */
public class RapportoFineConcessioneEntity {

	private String titolare;
	private String email;
	private String cur;
	
	
	public RapportoFineConcessioneEntity() {
		super();
	}

	public RapportoFineConcessioneEntity( String titolare,String email,	String cur) {
			super();
			this.titolare=titolare;
			this.email = email;
			this.cur = cur;
		}

	public String getTitolare() {
		return titolare;
	}

	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(String cur) {
		this.cur = cur;
	}

		
}
