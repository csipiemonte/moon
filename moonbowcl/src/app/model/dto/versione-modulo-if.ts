/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {StatoModulo} from './statoModulo';

export interface VersioneModuloIf {
    idVersioneModulo: number;
    versione: string;
    stato: StatoModulo;
}
