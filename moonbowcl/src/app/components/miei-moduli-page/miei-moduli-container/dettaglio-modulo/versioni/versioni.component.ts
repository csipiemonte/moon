/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
// fixme import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome,  faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import {AngularTreeGridComponent} from 'angular-tree-grid';
import * as moment from 'moment';
import {Costanti} from 'src/app/common/costanti';
import {TreeGridOpComponent} from '../../../../common/tree-grid-op/tree-grid-op.component';
import {Subscription} from 'rxjs';
import {ObserveService} from '../../../../../services/observe.service';
import {VersioneStato} from '../../../../../model/dto/versione-stato';
import {ModalActionComponent} from '../../../../modal/modal-action/modal-action.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'src/app/modules/alert';


@Component({
  selector: 'app-miei-moduli-dettaglio-versioni',
  templateUrl: './versioni.component.html',
  styleUrls: ['./versioni.component.scss']
})
export class VersioniComponent implements OnInit, OnDestroy {

  @Input() moduloSelezionato: Modulo;
  @ViewChild('angularGrid', {static: true}) angularGrid: AngularTreeGridComponent;
  @Output('alertService') alert = new EventEmitter();


  modulo: Modulo = new Modulo();
  private dispatcherEventSubCambiaStato: Subscription;
  private dispatcherEventSubEditFormio: Subscription;
  moduloDaModificare: Modulo;

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;


  versioniData: any = [];
  versioniConfigs: any = {
    id_field: 'id',
    parent_id_field: 'idParent',
    parent_display_field: 'versioneModulo',
    css: { // Optional
      expand_class: 'fas fa-caret-right',
      collapse_class: 'fas fa-caret-down',
      add_class: 'fa fa-plus',
      edit_class: 'fa fa-pencil',
      delete_class: 'fa fa-trash',
      save_class: 'fa fa-save',
      cancel_class: 'fa fa-remove',
    },
    columns: [
      {
        name: 'versioneModulo',
        header: 'Versione',
      },
      {
        name: 'dataUpd',
        header: 'Dt Ultimo Aggiornamento',
        renderer: function (value) {
          if (value) {
            return moment(value).format(Costanti.DATE_TIME_FORMAT);
          } else {
            return '';
          }
        }
      },
      {
        name: 'attoreUpd',
        header: 'Attore Ultimo Aggiornamento',
      },
      {
        name: 'codice',
        header: 'Stato',
      },
      {
        name: 'dataInizioValidita',
        header: 'Dt Inizio Validita',
        renderer: function (value) {
          if (value) {
            return moment(value).format(Costanti.DATE_TIME_FORMAT);
          } else {
            return '';
          }
        }
      },
      {
        name: 'dataFineValidita',
        header: 'Dt Fine Validita',
        renderer: function (value) {
          if (value) {
            return moment(value).format(Costanti.DATE_TIME_FORMAT);
          } else {
            return '';
          }
        }
      },
      {
        name: 'op',
        header: 'Operazione',
        type: 'custom',
        component: TreeGridOpComponent
      }
    ]
  };
  currTemplate: any;


  alertId = 'alert-miei-moduli-container';
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
              private _observeService: ObserveService,
              private modalService: NgbModal,
              protected alertService: AlertService
              ) {
    this.currTemplate = 'treegrid';
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    // this.moduloDaEditare = new Modulo();

    // dati hanno struttura di  VersioneStato
    this.dispatcherEventSubEditFormio = this._observeService.getClickEditOnTreeGridOperation().subscribe(versione => {
      console.log('dispatcherEventSubEditFormio versione=' + JSON.stringify(versione));
      if (versione.codice === 'PUB') {
        const modalRef = this.modalService.open(ModalActionComponent);
        modalRef.componentInstance.modal_titolo = 'MoonBackoffice';
        modalRef.componentInstance.modal_contenuto = 'La versione  ' + versione.versioneModulo +
            ' è già pubblicata ! Sei sicurro di volere modificare il form ?';
        modalRef.result.then(
            (result) => {
              console.log('dati-generali::editFormIo() Result:' + result);
              // this.closeResult = `Closed with: ${result}`;
              if (result === 'OK') {
                this.goToModificaModulo(versione);
              }
            }, (reason) => {
              // faccio nullla
            }
        )
      } else {
        setTimeout(() => this.goToModificaModulo(versione), 100);
      }
    });

    this.dispatcherEventSubCambiaStato = this._observeService.getClickCambiaStatoOnTreeGridOperation().subscribe(versione => {
      console.log('dispatcherEventSubCambiaStato versione=' + JSON.stringify(versione));
      this.cambiaStato(versione);
    });
  }

  private async goToModificaModulo(versione: VersioneStato) {
    this.moduloDaModificare = { ...this.moduloSelezionato };
    this.moduloDaModificare.idModulo = versione.idModulo;
    this.moduloDaModificare.idVersioneModulo = versione.idVersioneModulo;
    this.moduloDaModificare.stato.codice = versione.codice;
    this.moduloDaModificare.stato.descrizione = versione.descrizione;
    this.currTemplate = 'modificaModulo';
  }


  ngOnInit(): void {
    this.alert.emit({clear: true});    
    this.spinnerService.show();
    this.modulo = this.moduloSelezionato;
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
        this.moduloSelezionato.idVersioneModulo,
        'versioni').subscribe((modulo: Modulo) => {
          this.modulo = modulo;
          this.versioniData = [...modulo.versioni];
          // tslint:disable-next-line:max-line-length
          if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
            this.isModificabile = true;
          }

          this.spinnerService.hide();
        },
        (err: MoonboError) => {
          // informazioni sulla chiamata
          // this.errNotificationError.notification.next(err);
          // alert(err.errorMsg);
          this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
          this.modeError = true;
          this.spinnerService.hide();
        }
    );
  }

  selectRow($event) {
    console.log($event);
  }

  backOperazione($event: any) {
    this.currTemplate = 'treegrid';
    this.spinnerService.hide();
  }

  ngOnDestroy() {
    this.dispatcherEventSubEditFormio.unsubscribe();
    this.dispatcherEventSubCambiaStato.unsubscribe();
  }

  cambiaStato(versione: VersioneStato) {
    log('versioni::cambiaStato() versione Selected: ' + JSON.stringify(versione));
    log('versioni::cambiaStato() VersioneStato: ' + versione.idModulo + '/v/' + versione.idVersioneModulo);
    this.moduloDaModificare = { ...this.moduloSelezionato };
    this.moduloDaModificare.idModulo = versione.idModulo;
    this.moduloDaModificare.idVersioneModulo = versione.idVersioneModulo;
    this.moduloDaModificare.versioneModulo = versione.versioneModulo;
    this.moduloDaModificare.stato.codice = versione.codice;
    this.moduloDaModificare.stato.descrizione = versione.descrizione;
    this.currTemplate = 'cambiaStato';
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
