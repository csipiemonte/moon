/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {NgEventBus} from 'ng-event-bus';
import {SharedService} from 'src/app/services/shared.service';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-aiuto',
  templateUrl: './aiuto.component.html',
  styleUrls: ['./aiuto.component.scss']
})
export class AiutoComponent implements OnInit {


  constructor(private sharedService: SharedService, private eventBus: NgEventBus) { }

  ngOnInit(): void {
    this.sharedService.isDirectRouterLink = true;
  }


  scrollTo(element: any): void {
    (document.getElementById(element) as HTMLElement).scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
  }
}
