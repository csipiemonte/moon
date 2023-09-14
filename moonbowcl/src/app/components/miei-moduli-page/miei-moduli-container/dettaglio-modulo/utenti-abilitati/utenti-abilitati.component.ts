/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { Utente } from 'src/app/model/dto/utente';
import { UtenteModuloAbilitato } from 'src/app/model/dto/utente modulo-abilitato';
import { AlertService } from 'src/app/modules/alert';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-miei-moduli-dettaglio-utenti-abilitati',
  templateUrl: './utenti-abilitati.component.html',
  styleUrls: ['./utenti-abilitati.component.scss']
})
export class UtentiAbilitatiComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;

  utentiAbilitati: UtenteModuloAbilitato[];

  isAdmin = false;
  isOpMinADM = false;

  faTrashAlt = faTrashAlt;

  mapOperatori: Map<string, Utente> = new Map<string, Utente>();

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpMinADM = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    this.spinnerService.show();
    this.moonboblService.getUtentiAbilitatiByModulo(this.moduloSelezionato.idModulo).subscribe(
      (res) => {
        this.utentiAbilitati = res;
        this.popolaMapOperatori();
        this.spinnerService.hide();
      },
      (err) => {
        alert(err.errorMsg);
        this.spinnerService.hide();
      }
    );
  }

  popolaMapOperatori() {
    const attori = this.utentiAbilitati.map(u => u.attoreUpdAbilitazione);
    const distinctAttori = attori.filter((n, i) => attori.indexOf(n) === i); // [...new Set(attori)];
    log('utenti-abilitati::popolaMapOperatori() distinctAttori=' + distinctAttori);
    distinctAttori.forEach(attore => {
      log('utenti-abilitati::popolaMapOperatori() LOOP attore = ' + attore);
      if (attore !== 'ADMIN') {
        this.moonboblService.getUtenteByIdentificativo(attore).subscribe(
          (resOperatore: Utente) => {
            this.mapOperatori.set(attore, resOperatore);
          },
          (err: MoonboError) => {
            log('utenti-abilitati::popolaMapOperatori() distinctAttori=' + err.errorMsg);
          }
        );
      }
    });
    log('utenti-abilitati::popolaMapOperatori() mapOperatori=' + this.mapOperatori);
  }

  eliminaUtenteAbilitato(idUtente: number) {
    log('utenti-abilitati::eliminaUtenteAbilitato() idUtente = ' + idUtente);
    this.moonboblService.deleteUtenteModulo(idUtente, this.moduloSelezionato.idModulo).subscribe(
      (res) => {
        log('utenti-abilitati::eliminaUtenteAbilitato() moonboblService.deleteUtenteModulo() Done.');
        // this.init();
        for (let i = 0; i < this.utentiAbilitati.length; i++) {
          if (this.utentiAbilitati[i].idUtente === idUtente) {
            this.utentiAbilitati.splice(i, 1);
          }
        }
      },
      (err) => {
        alert(err.errorMsg);
      }
    );
  }

}

function log(a: any) {
  // console.log(a);
}
