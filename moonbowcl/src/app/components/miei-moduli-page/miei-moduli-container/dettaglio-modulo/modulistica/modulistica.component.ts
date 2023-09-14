/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { PortaleModuloLogonMode } from './../../../../../model/dto/portale-modulo-logon-mode';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { PortaliIf } from 'src/app/model/dto/portali-if';
import { AlertService } from 'src/app/modules/alert';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { LogonModeIf } from 'src/app/model/dto/logon-mode-if';
import { HttpErrorResponse } from '@angular/common/http';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-miei-moduli-dettaglio-modulistica',
  templateUrl: './modulistica.component.html',
  styleUrls: ['./modulistica.component.scss']
})
export class ModulisticaComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Input() elencoPortali: PortaliIf[];
  @Output('alertService') alert = new EventEmitter();

  modeError = false;

  portaliLogonMode: PortaleModuloLogonMode[];
  hasPortaliLogonMode = false;
  portaliNonAbilitate: PortaliIf[];
  elencoLogonMode: LogonModeIf[];

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  editMode = false;

  faTrashAlt = faTrashAlt;

  newIdPortale: number;
  newIdLogonMode: number;
  newFiltro: string;

  alertOptions = {
    id: 'alert-moduli-moduliscita',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
    if (this.isAdmin || this.isOpAdmin) {
      this.isModificabile = true;
    }
  }

  ngOnInit(): void {
    this.alert.emit({ clear: true });
    this.init();
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getLogonMode().subscribe(
      (res) => {
        this.elencoLogonMode = res;
        this.spinnerService.hide();
      },
      (err: MoonboError) => {
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false });
        this.modeError = true;
      }
    );
    this.moonboblService.getPortaliLogonMode(this.moduloSelezionato.idModulo).subscribe(
      (res) => {
        // Init degli attributi per viewMode
        this.portaliLogonMode = res;
        if (this.portaliLogonMode && this.portaliLogonMode.length > 0) {
          this.hasPortaliLogonMode = true;
        } else {
          this.hasPortaliLogonMode = false;
        }
        // tslint:disable-next-line:max-line-length
        this.aggiornaPortaliNonAbilitati();
        this.spinnerService.hide();
      },
      (err: MoonboError) => {
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false });
        this.modeError = true;
        this.spinnerService.hide();
      }
    );
  }

  aggiornaPortaliNonAbilitati() {
    this.portaliNonAbilitate = [];
    const currIdEnte = this.sharedService.userLogged.ente.idEnte;
    // Portale 1-ALL-* not used for modulistica
    const elencoIdPortaliAbilitati = [1, ...this.portaliLogonMode?.map(plm => plm.portale.idPortale)];
    log('modulistica::aggiornaPortaliNonAbilitati() elencoIdPortaliAbilitati = ' + elencoIdPortaliAbilitati);
    if (elencoIdPortaliAbilitati && this.elencoPortali) {
      log('modulistica::aggiornaPortaliNonAbilitati() BEFORE this.elencoPortali = ' + this.elencoPortali.map(p => p.idPortale));
      this.portaliNonAbilitate = this.elencoPortali.filter(p => (elencoIdPortaliAbilitati.indexOf(p.idPortale) === -1));
      log('modulistica::aggiornaPortaliNonAbilitati() AFTER this.portaliNonAbilitate = ' + this.portaliNonAbilitate.map(p => p.idPortale));
    }
    if (this.portaliNonAbilitate && this.portaliNonAbilitate.length > 0) {
      this.newIdPortale = this.portaliNonAbilitate[0].idPortale;
    }
  }

  aggiungi() {
    log('modulistica::aggiungi() newIdPortale = ' + this.newIdPortale +
      ' newIdLogonMode = ' + this.newIdLogonMode + ' newFiltro = ' + this.newFiltro);
    this.moonboblService.postPortaleModuloLogonMode(this.newIdPortale, this.moduloSelezionato.idModulo,
      this.newIdLogonMode, this.newFiltro).subscribe(
        (res) => {
          this.alertService.success('Salvataggio PortaleModuloLogonMode effettuato con successo !', this.alertOptions);
          log('modulistica::aggiungi() moonboblService.postPortaleModuloLogonMode() res = ' + JSON.stringify(res));
          const newPortaleModuloLogonMode = res;
          this.portaliLogonMode.push(newPortaleModuloLogonMode);
          log('modulistica::aggiungi() moonboblService.postAggiungiAreaRuolo() this.portaliLogonMode = '
            + JSON.stringify(this.portaliLogonMode));
          // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
          this.aggiornaPortaliNonAbilitati();
//          this.eventModifica.emit(this.utenteSelezionato);
        },
        (err) => {
//        alert(err.errorMsg);
          this.alertService.error('Impossibile effettuare il salvataggio del nuovo PortaleModuloLogonMode !' +
            err.errorMsg, this.alertOptions);
          log('modulistica::aggiungi() Impossibile effettuare il salvataggio del nuovo PortaleModuloLogonMode ! ' +
            err.errorMsg);
        }
      );
  }

  elimina(plmToDelete: PortaleModuloLogonMode) {
    log('modulistica::elimina() plm = ' + JSON.stringify(plmToDelete) );
    this.moonboblService.deletePortaleModuloLogonMode(plmToDelete)
      .subscribe({
        next: () => {
          this.portaliLogonMode = this.portaliLogonMode.filter(plm => plm.portale.idPortale !== plmToDelete.portale.idPortale);
          log('modulistica::elimina() Delete successful.');
        },
        error: (e: HttpErrorResponse) => {
          log('modulistica::elimina() errore di cancellazione : ' + e.message);
        },
        complete: () => {
          log('modulistica::elimina() complete.');
          this.aggiornaPortaliNonAbilitati();
        }
      });
  }

}

function log(a: any) {
  // console.log(a);
}
