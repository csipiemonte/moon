/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class TicketCrmRichiesta {

	idRichiesta: number;
	dataRichiesta: Date;
	codiceRichiesta: string;
	uuidRichiesta: string;
	stato: string;
	tipoDoc: number;
	idIstanza: number;
	idAllegatoIstanza: number;
	idFile: number;
	idModulo: number;
	idArea: number;
	idEnte: number;
	idTicketingSystem: number;
	uuidTicketingSystem: string;
	note: string;
	codiceEsito: string;
	descEsito: string;
	dataUpd: Date;
	nomeFile: string;

}