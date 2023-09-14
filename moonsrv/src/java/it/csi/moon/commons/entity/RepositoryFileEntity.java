/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

import java.util.Arrays;

/**
 * Entity della tabella dei file allegati di notifica
 * <br>
 * <br>Tabella moon_fo_t_repository_file
 * <br>PK: idFile 
 * <br>Usato per salvare la notifica della PA dal BO
 * <br>Usato in lettura nel caso di invio mail notifica da moonsrv
 * <br>Usato per salvare gli possibili allegati la richiesta di integrazione della PA dal BO
 * <br>Usato per salvare gli possibili allegati della risposta di integrazione da parte del cittadino da FO
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class RepositoryFileEntity extends RepositoryFileLazyEntity {
	
	private byte[] contenuto;
	
	public byte[] getContenuto() {
		return contenuto;
	}
	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
	}

	public boolean isValid() {
		return (contenuto != null && contenuto.length > 0) && super.isValid();
	}


	@Override
	public String toString() {
		return "RepositoryFileEntity [" + super.toString() 
				+ ", contenuto=" + ((contenuto==null)?"null":(Arrays.toString(contenuto).substring(0, 10)+"... len="+contenuto.length))
				+ "]";
	}

	public String toStringFULL() {
		return "RepositoryFileEntity [" + super.toString()
		+ ", contenuto=" + contenuto
		+ "]";
	}
	
}
