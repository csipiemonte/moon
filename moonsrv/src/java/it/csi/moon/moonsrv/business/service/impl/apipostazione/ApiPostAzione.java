/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.apipostazione;

import java.util.List;

import it.csi.moon.commons.dto.api.PostAzione;

public interface ApiPostAzione {

	public void execute(List<PostAzione> postAzioni);

	//
	public String sendEmail(PostAzione postAzione);
	
}
