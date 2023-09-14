/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { Modulo } from '../../../../model/dto/modulo';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MoonboblService } from '../../../../services/moonbobl.service';
import { Categoria } from '../../../../model/dto/categoria';
import { faClone, faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { ErrorNotificationService } from '../../../../services/error-notification.service';
import { Processo } from '../../../../model/dto/processo';
import { SharedService } from 'src/app/services/shared.service';
import { PortaliIf } from 'src/app/model/dto/portali-if';
import { TipoCodiceIstanza } from 'src/app/model/dto/tipo-codice-istanza';
import { environment } from 'src/environments/environment';
import { AlertService } from 'src/app/modules/alert';

@Component({
  selector: 'app-miei-moduli-dettaglio',
  templateUrl: './dettaglio-modulo.component.html',
  styleUrls: ['./dettaglio-modulo.component.css']
})
export class DettaglioModuloComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Input() elencoCategorie: Categoria[];
  @Input() elencoProcessi: Processo[];
  @Input() elencoPortali: PortaliIf[];
  @Input() elencoTipoCodiceIstanza: TipoCodiceIstanza[];
  // @Output() eventDuplicaModulo = new EventEmitter<Modulo>();
  @Output() eventCambiaStato = new EventEmitter<Modulo>();
  @Output() eventModificaModulo = new EventEmitter<Modulo>();
  // @Output() eventDuplicaModulo = new EventEmitter<Modulo>();
  @Output() eventEliminaModulo = new EventEmitter<Modulo>();
  @Output() backEvent = new EventEmitter<string>();

//  modulo: Modulo = new Modulo();

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;
  navVertActive = 'dati-generali';
  envModuliDettaglioTabEnable: any;

  spanTitleModulo = '';

  alertId = 'alert-miei-moduli-dettaglio_modulo';
  alertOptions = {
      id: this.alertId,
      autoClose: true,
      keepAfterRouteChange: false
  };
  alertOptionsNoAutoClose = {
      id: this.alertId,
      autoClose: false,
      keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.envModuliDettaglioTabEnable = environment.moduliDettaglioTabEnable;
  }

  ngOnInit(): void {
    this.spanTitleModulo = this.moduloSelezionato.oggettoModulo
      + (this.isAdmin ? '[' + this.moduloSelezionato.idModulo + ']' : '');
  }

  cambiaStato(modulo: Modulo) {
    log('dettaglio-modulo::cambiaStato() modulo = ' + modulo);
    // $event.preventDefault();
    this.eventCambiaStato.emit(modulo);
  }


  modificaModulo(modulo: Modulo) {
    log('dettaglio-modulo::modificaModulo() modulo = ' + modulo);
    // $event.preventDefault();
    this.eventModificaModulo.emit(modulo);
  }

/*
  duplicaModulo($event: MouseEvent) {
    $event.preventDefault();
    this.onDuplicaModulo.emit(this.modulo);
  }
*/
  eliminaModulo(modulo: Modulo) {
    log('dettaglio-modulo::eliminaModulo() modulo = ' + modulo);
    // $event.preventDefault();
    this.eventEliminaModulo.emit(modulo);
  }

  back() {
//    this.spinnerService.show();
    this.backEvent.emit('back');
  }

  getAlert(message) {
    const type = message.type;
    const text = message.text;
    let options = {};

    if (message.autoclose) {
        options = this.alertOptions;
    } else {
        options = this.alertOptionsNoAutoClose;
    }
    if (message.clear) {
        this.alertService.clear(this.alertId);
    } else {
        switch (type) {
            case 'success': {
                this.alertService.success(text, options);
                break;
            }
            case 'info': {
                this.alertService.info(text, options);
                break;
            }
            case 'error': {
                this.alertService.error(text, options);
                break;
            }
            case 'warn': {
                this.alertService.warn(text, options);
                break;
            }
            default: {
                this.alertService.warn(text, options);
                break;
            }
        }
    }
}


}

function log(a: any) {
  // console.log(a);
}
