/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.protocollo;

import java.util.Arrays;

import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.util.MapProtocolloAttributi;

/**
 * Parametri per la protocollazione
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ProtocolloParams {

	MapProtocolloAttributi conf;
	byte[] content;
	RepositoryFileEntity repositoryFile;
	private String resoconto;
	
	public MapProtocolloAttributi getConf() {
		return conf;
	}
	public void setConf(MapProtocolloAttributi conf) {
		this.conf = conf;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public RepositoryFileEntity getRepositoryFile() {
		return repositoryFile;
	}
	public void setRepositoryFile(RepositoryFileEntity repositoryFile) {
		this.repositoryFile = repositoryFile;
	}
	public String getResoconto() {
		return resoconto;
	}
	public void setResoconto(String resoconto) {
		this.resoconto = resoconto;
	}
	
	
	public String toStringFull() {
		return "ProtocolloParams [conf=" + conf + /*", idDocumento=" + idDocumento + */ ", content=" + Arrays.toString(content)
			+ ", repositoryFile=" + repositoryFile.toStringFULL()  + ", resoconto=" + resoconto + "]";
	}
	@Override
	public String toString() {
		return "ProtocolloParams [conf=" + conf + /*", idDocumento=" + idDocumento + */ ", content=" + ((content==null)?"null":Arrays.toString(content).substring(0, 10)+"... len="+content.length)
			+ ", repositoryFile=" + repositoryFile + ", resoconto=" + resoconto + "]";
	}
}
