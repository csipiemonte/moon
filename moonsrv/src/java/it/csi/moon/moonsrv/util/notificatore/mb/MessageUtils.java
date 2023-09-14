/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util.notificatore.mb;

import it.csi.apirest.notify.mb.v1.dto.Message;

/**
 * Class utility su Notificatore Messages
 * 
 * @author Laurent Pissard
 * 
 * @version 1.0.0 - 15/05/2020 - versione iniziale
 */
public class MessageUtils {

	public static boolean isPushValorized(Message message) {
		if (message!=null && message.getPayload()!=null &&
			message.getPayload().getPush()!=null &&
			message.getPayload().getPush().getTitle()!=null) {
			return true;
		}
		return false;
	}

}
