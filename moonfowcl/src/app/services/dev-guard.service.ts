/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable, isDevMode} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DevGuardService implements CanActivate {

  constructor() {
  }

  // attivata se sono in sviluppo
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return isDevMode();
  }
}
