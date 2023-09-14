/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao;
import it.csi.moon.moonsrv.util.AspectIndex;
//import it.doqui.index.cxfclient.NodeResponse;
import it.doqui.index.cxfclient.SearchResponse;


public interface IndexDAO {

	public String luceneSearch(String path);
	public SearchResponse luceneSearchTest(String path);

	public String creaFolder(String uid_parent, String name_folder);
	public String creaFolderWithAspect(String uid_parent, String name_folder, AspectIndex aspect);
	public String creaDocumento(String uid_parent, String nomeFile, byte[] bytes, String mimeType, AspectIndex aspect);
	public byte[] getContentByUid(String uid);
	public String getPathByUid(String uid);

}
