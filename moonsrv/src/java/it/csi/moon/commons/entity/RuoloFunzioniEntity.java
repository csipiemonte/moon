/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Date;

/**
 * Entity della tabella ruolo funzioni
 * <br>
 * <br>Tabella moon_fo_r_ruolo_funzioni
 * <br>PK: idRuolo, idFunzione
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class RuoloFunzioniEntity  {
		private Integer idRuolo;
		private Integer idFunzione;
		private Date dataUpd;
		private String attoreUpd;
		
		public RuoloFunzioniEntity() {
			super();
		}

		public RuoloFunzioniEntity(Integer idRuolo, Integer idFunzione, String nomeRuolo, Date dataUpd,
				String attoreUpd) {
			super();
			this.idRuolo = idRuolo;
			this.idFunzione = idFunzione;
			this.dataUpd = dataUpd;
			this.attoreUpd = attoreUpd;
		}

		public Integer getIdRuolo() {
			return idRuolo;
		}

		public void setIdRuolo(Integer idRuolo) {
			this.idRuolo = idRuolo;
		}

		public Integer getIdFunzione() {
			return idFunzione;
		}

		public void setIdFunzione(Integer idFunzione) {
			this.idFunzione = idFunzione;
		}


		public Date getDataUpd() {
			return dataUpd;
		}

		public void setDataUpd(Date dataUpd) {
			this.dataUpd = dataUpd;
		}

		public String getAttoreUpd() {
			return attoreUpd;
		}

		public void setAttoreUpd(String attoreUpd) {
			this.attoreUpd = attoreUpd;
		}
		

}
