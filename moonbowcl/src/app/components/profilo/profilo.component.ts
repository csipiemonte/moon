/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { UserInfo } from 'src/app/model/common/user-info';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-profilo',
  templateUrl: './profilo.component.html',
  styleUrls: ['./profilo.component.scss']
})
export class ProfiloComponent implements OnInit {

  currentUser: UserInfo = null;
  isAdmin = false;

  constructor(private sharedService: SharedService) { }

  ngOnInit(): void {
    this.currentUser = this.sharedService.UserLogged;
    log('profilo::ngOnInit() currentUser =' + JSON.stringify(this.currentUser));
    this.isAdmin = this.currentUser?.isTipoADMIN();
  }

}

function log(a: any) {
  console.log(a);
}
