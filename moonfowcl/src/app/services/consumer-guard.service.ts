/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {User} from '../model/common/user';
import {StorageManager} from '../util/storage-manager';

@Injectable({
  providedIn: 'root'
})
export class ConsumerGuardService implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user && user.consumerParams) {
      this.router.navigate(['/forbidden']);
      return false;
    } else {
      return true;
    }
  }
}
