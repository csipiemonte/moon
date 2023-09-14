/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NavSelection } from 'src/app/common/nav-selection';
import { Nav } from 'src/app/model/dto/nav';
import { SharedService } from 'src/app/services/shared.service';

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
  showBack: any;

  constructor(private router: Router,
    private route: ActivatedRoute, private sharedService: SharedService) { }

  ngOnInit() {
    this.codice = this.sharedService.notifica.codice;
    this.descrizione = this.sharedService.notifica.descrizione;
    this.titolo = this.sharedService.notifica.titolo;
    this.showBack = this.sharedService.notifica.showBack;
    this.titoloErrore = (this.titolo) ? this.titolo : ((this.descrizione) ? 'Si è verificato il seguente errore: ': 'Si è verificata una anomalia');

    console.log("codice segnalazione: "+this.codice);


  }

  goToIstanze() {
    this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'istanze');
    this.router.navigate(['istanze']);
  }

}
