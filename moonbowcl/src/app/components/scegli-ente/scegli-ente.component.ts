/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { STORAGE_KEYS } from 'src/app/common/costanti';
import { NavSelection } from 'src/app/common/nav-selection';
import { Ente } from 'src/app/model/dto/ente';
import { Nav } from 'src/app/model/dto/nav';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { ObserveService } from 'src/app/services/observe.service';
import { SharedService } from 'src/app/services/shared.service';
import {StorageManager} from '../../common/utils/storage-manager';

@Component({
  selector: 'app-scegli-ente',
  templateUrl: './scegli-ente.component.html',
  styleUrls: ['./scegli-ente.component.scss']
})
export class ScegliEnteComponent implements OnInit {

  idEnte: number;
  enteSelezionato: Ente;
  elencoEnti: Ente[];
  msg: string;


  constructor(private moonboblService: MoonboblService,
    private sharedService: SharedService,
    private handleException: HandleExceptionService, 
    private router: Router,
    private observeService: ObserveService,
    private spinnerService: NgxSpinnerService) { }

  ngOnInit() {
    if (this.sharedService.elencoEnti && this.sharedService.elencoEnti.length > 0) {
      this.elencoEnti = this.sharedService.elencoEnti;
    }
    else {
      this.spinnerService.show();
      this.moonboblService.getElencoEnti().subscribe((enti) => {
        this.elencoEnti = enti;
        if (this.elencoEnti.length === 0) {
          this.msg = 'Non sono presenti enti ';
        } else {
          console.log('Enti: ' + this.elencoEnti);
        }
        this.spinnerService.hide();
      },
        errore => {
          this.spinnerService.hide();
        }
      );
    }
  }

  selezionaEnte(id) {
    let ente = new Ente();
    ente.idEnte = id;
    this.enteSelezionato = ente;
  }

  annullaSelezione() {
    this.idEnte = null;
  }

  aggiornaEnte() {
    this.spinnerService.show();
    setTimeout(() => {
      this.moonboblService.aggiornaEnte(this.enteSelezionato).subscribe(response => {
        console.log(response);
        console.log('set observable for header update - response of patch imposta ente');
        this.observeService.setEventResponse(response);
        const objString = JSON.stringify(response.body);
        const objParse = JSON.parse(objString);
        StorageManager.add(STORAGE_KEYS.MOON_JWT_TOKEN, objParse.idMoonToken);
        this.spinnerService.hide();
        this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'istanze');
        this.router.navigate(['']);
        this.spinnerService.hide();
      },
        (error) => {
          //this.msg = this.handleException.handleNotBlockingError(error).message;
          console.log('ERROR aggiorna ente: ' + error);
          this.spinnerService.hide();
        });

    }, 500);
  }
}
