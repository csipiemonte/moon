/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { AreaRuolo } from './area-ruolo';

export class EnteAreaRuolo {
    idEnte: number;
    codiceEnte: string;
    nomeEnte: string;
    descrizioneEnte: string;
    idTipoEnte: number;
    areeRuoli: AreaRuolo[];
}
