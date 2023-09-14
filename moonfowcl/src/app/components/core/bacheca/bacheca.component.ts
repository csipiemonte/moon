/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { AlertService } from 'src/app/modules/alert/alert.service';
import { MoonfoblService } from 'src/app/services/moonfobl.service';
import { environment } from 'src/environments/environment';
import { StorageManager } from "src/app/util/storage-manager";
import { User } from 'src/app/model/common/user';
import { STORAGE_KEYS } from 'src/app/common/costanti';
import { Component, OnInit } from '@angular/core';
import { NgEventBus } from 'ng-event-bus';
import { NavSelection } from 'src/app/common/nav-selection';
import { AlertTypeDesc } from 'src/app/common/alert-type-desc';


@Component({
  selector: 'app-bacheca',
  templateUrl: './bacheca.component.html',
  styleUrls: ['./bacheca.component.scss']
})
export class BachecaComponent implements OnInit {

  pathAssets = environment.pathAssets;

  constructor(private moonfoblService: MoonfoblService,
              private eventBus: NgEventBus) {
  }
  user: User = StorageManager.get(STORAGE_KEYS.USER);
  ngOnInit() {    
    this.getElencoMessaggi();
    this.eventBus.cast('active-nav-bar:enable', NavSelection.BACHECA);
  }

  getElencoMessaggi() {
    this.moonfoblService.getMessaggiBacheca().subscribe(messaggi => {

      if (messaggi.length > 0) {
        for (let messaggio of messaggi) {
          this.eventBus.cast('alert:set', { text: messaggio.messaggio, type: AlertTypeDesc.INFO, clear:false} );
        }
      }
    });
    console.log(this.user);
    
  }


}
