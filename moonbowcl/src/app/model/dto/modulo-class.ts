/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export enum ModuloClassTipologia {
    INIT = 1,
    PRINT_MAPPER = 2,
    PROTOCOLLO_MANAGER = 3
}

export class ModuloClass {
    idModulo: number;
    tipologia: number;
    nomeClasse: string;
}
