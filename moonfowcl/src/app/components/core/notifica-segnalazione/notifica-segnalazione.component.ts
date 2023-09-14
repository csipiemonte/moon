/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NavSelection} from 'src/app/common/nav-selection';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {User} from 'src/app/model/common/user';
import {Nav} from 'src/app/model/dto/nav';
import {SharedService} from 'src/app/services/shared.service';
import {StorageManager} from 'src/app/util/storage-manager';

;

@Component({
  selector: 'app-notifica-segnalazione',
  templateUrl: './notifica-segnalazione.component.html',
  styleUrls: ['./notifica-segnalazione.component.scss']
})
export class NotificaSegnalazioneComponent implements OnInit {

  codice: string;
  descrizione: string;
  titolo: string;
  titoloErrore: string;
  isUrlEmbedded: boolean = false;
  urlEmbedded: string = undefined;

  constructor(private router: Router,
              private route: ActivatedRoute, private sharedService: SharedService) { }

  ngOnInit() {
    this.urlEmbedded = this.getUrlRitorno();
    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.titolo = this.sharedService.notifica.titolo;
    this.titoloErrore = (this.titolo) ? this.titolo : ((this.descrizione) ? 'Si è verificato il seguente errore: ' : 'Si è verificata una anomalia');
    console.log('codice segnalazione: ' + this.codice);
  }

  goToIstanze() {
    this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
    this.router.navigate(['home']);
  }

  goBack(){
    window.location.href = this.urlEmbedded;
  }

  getUrlRitorno() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    let url = undefined;
    if (user) {
      if(user.embeddedNavigator){
        if (user.embeddedNavigator.options && user.embeddedNavigator.options.urlTornaIstanze) {
          url = user.embeddedNavigator.options.urlTornaIstanze;
        }
        if (!url) {
          // if embedded and no url set: no showing back button
          this.isUrlEmbedded = true;
        }
      }
      if (user.consumerParams) {
        if (user.consumerParams.backUrl) {
          url = user.consumerParams.backUrl;
        }
      }
      if (!url) {
        // if embedded and no url set: no showing back button
        this.isUrlEmbedded = true;
      }
    }
    return url;
  }

}
