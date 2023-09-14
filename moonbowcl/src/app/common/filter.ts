/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { CurrentFilter } from "./current-filter";

export class Filter {

    currentFilters: Map<number, CurrentFilter>;

    getCurrentFilter(sezione) {
        if (this.currentFilters) {
            return this.currentFilters.get(sezione);
        }
    }

    setCurrentFilter(sezione: number, cf: CurrentFilter) {
        if (this.currentFilters) {
            this.currentFilters.set(sezione, cf);
        }
        else {
            this.currentFilters = new Map<number, CurrentFilter>();
        }
    }

    constructor() {

    }

}