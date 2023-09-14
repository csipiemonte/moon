/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { isDevMode } from "@angular/core";
import {environment} from '../../../environments/environment';

export const AppConfig = {

    get apiUrl() {
        console.log('******   getAPiUrl********');

        if (isDevMode()) {
            console.log(' location :'+ `${window.location}`);
            return `${window.location.protocol}//${window.location.hostname}:10110`;

        }
        else {
                if (environment.apiUrlIncludeContext) {
                    return `${window.location.protocol}//${window.location.hostname}:${window.location.port}/moonbobl`;
                } else {
                    return `${window.location.protocol}//${window.location.hostname}:${window.location.port}`;
                }
            }


    }

}
