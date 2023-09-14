/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service;

/**
 * trasmissione buoni taxi csv
 * 
 * @author Danilo
 */
public interface TrasmettiService {

	public Integer trasmettiBuoniTaxi();
	
	public byte[] getRowsBuoniTaxi();
	
}
