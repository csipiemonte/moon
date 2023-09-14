/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import { STORAGE_KEYS } from '../common/costanti';
import {StorageManager} from '../common/utils/storage-manager';


@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const jwt = StorageManager.get(STORAGE_KEYS.MOON_JWT_TOKEN);
    if (!jwt) {
      this.router.navigate(['/forbidden']);
      return false;
    } else {
      return true;
    }
  }
}
