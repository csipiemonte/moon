/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SharedService} from '../../../services/shared.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{
  activeId: number;

  constructor(
    private sharedService: SharedService,
    private router: Router,
  ) {
    // this.activeId = this.sharedService.nav.active;
  }

  ngOnInit() {

    // this.activeId = this.sharedService.nav.active;

    console.log(' *** is direct router link = '+this.sharedService.isDirectRouterLink)

    //importante per la gestione delle back con routing passato nello SharedService versus lik diretto
    if( this.sharedService.isDirectRouterLink){
      this.sharedService.isDirectRouterLink = false;
      this.router.navigate([this.router.url]);
    } else{
      this.router.navigate([this.sharedService.nav.route]);
    }


  }

}
