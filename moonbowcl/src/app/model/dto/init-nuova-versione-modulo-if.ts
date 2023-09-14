/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {VersioneModuloIf} from './versione-modulo-if';

export interface InitNuovaVersioneModuloIf {
    idModulo: number,
    codiceModulo: string,
    versioni: VersioneModuloIf[];
    ultimaVersione: string;
    ultimaMaggioreVersione: number;
    ultimaMinoreVersione: number;
    ultimaPatchVersione: number;

}
