/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ProcessiView, ProcessiViewEvent} from '../../../../processi.model';
import { faSave, faHome } from '@fortawesome/free-solid-svg-icons';
import { Processo } from 'src/app/model/dto/processo';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedService } from 'src/app/services/shared.service';
import { AlertType } from 'src/app/modules/alert';
import { AlertService } from 'src/app/modules/alert/alert.service';

@Component({
  selector: 'app-processi-crea',
  templateUrl: './processi-crea.component.html',
  styleUrls: ['./processi-crea.component.scss']
})
export class ProcessiCreaComponent implements OnInit {
  
  processo: Processo = new Processo();

  isAdmin = false;

  @Output() backEvent = new EventEmitter<ProcessiViewEvent>();

  faSave = faSave;
  faHome = faHome;

  alertOptions = {
    id: 'alert-processi-crea',
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private library: FaIconLibrary,
    private alertService: AlertService
  ) {
    library.addIcons(faSave, faHome);
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit() {
    log('processi-crea::ngOnInit()');
  }

  onSubmit(ev: any) {
    log('processi-crea::onSubmit()' + ev);
    this.salva();
  }

  salva() {
    log('processi-crea::salva()');
    log('processi-crea::salva() ' + JSON.stringify(this.processo));
    this.spinnerService.show();

    this.moonboblService.postProcesso(this.processo).subscribe({
      'next': (res) => {
        const risposta = res as Processo;
        this.spinnerService.hide();

        const processiViewEvent = new ProcessiViewEvent();
        processiViewEvent.alertMessage = 'Processo ' + risposta.nomeProcesso +
          ' (' + risposta.codiceProcesso + ') inserito con id ' + risposta.idProcesso;
        processiViewEvent.alertType = AlertType.Success;
        processiViewEvent.processo = risposta; //_constructorUtenteEnteAbilitato(risposta);
        processiViewEvent.view = ProcessiView.DETTAGLIO;
        this.backEvent.emit(processiViewEvent);
      },
      'error': (err) => {
        log('processi-crea::salva() ' + JSON.stringify(err));
        this.spinnerService.hide();
        this.alertService.error('Impossibile effettuare il salvataggio.<br>' + err.error?.msg, this.alertOptions);
      }
    });
  }

  changeDisplay() {
  }

  back() {
    this.backEvent.emit(new ProcessiViewEvent());
  }
}

function log(a: any) {
  console.log(a);
}
