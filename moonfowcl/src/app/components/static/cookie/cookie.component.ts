/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {NgEventBus} from 'ng-event-bus';
import {SharedService} from 'src/app/services/shared.service';

@Component({
  selector: 'app-cookie',
  templateUrl: './cookie.component.html',
  styleUrls: ['./cookie.component.scss']
})
export class CookieComponent implements OnInit {

  constructor(private sharedService: SharedService, private eventBus: NgEventBus) { }

  ngOnInit(): void {
    this.sharedService.isDirectRouterLink = true;
  }


}
