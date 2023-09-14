/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class ModuloPk {
    pk: string;
    // idModulo: number;
    // idVersioneModulo: number;

    constructor(idModulo: number, idVersioneModulo: number) {    
        this.pk = idModulo + '-'+ idVersioneModulo;
    }

    getIdModulo(){
       return this.pk.split("-")[0];
    }
}