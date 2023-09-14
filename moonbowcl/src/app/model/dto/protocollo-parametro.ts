/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Area } from "./area";
import { Ente } from "./ente";

export class ProtocolloParametro {
    idParametro: number;
    ente: Ente;
    area: Area;
    idModulo: number;
    nomeAttributo: string;
    valore: string;
    dataUpd: Date;
    attoreUpd: string;
    order: number;
}
