/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {StorageManager} from '../util/storage-manager';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const jwt = StorageManager.get(STORAGE_KEYS.JWT_MOON);
    if (!jwt) {
      this.router.navigate(['/forbidden']);
      return false;
    } else {
      return true;
    }
  }
}
