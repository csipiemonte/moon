/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Campo {
    label: string;
    key: string;
    type: string;
    fullKey: string;
    gridKey: string;
    gridFullKey: string;

    constructor(key: string,  label: string, type: string,fullKey: string, gridKey: string, gridFullKey: string) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.fullKey = fullKey;
        this.gridKey = gridKey;
        this.gridFullKey = gridFullKey;
      }
}
