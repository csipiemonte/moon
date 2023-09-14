/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { SharedService } from '../../../services/shared.service';
import { Modulo } from '../../../model/dto/modulo';
import { MoonboblService } from '../../../services/moonbobl.service';
import { Subject } from 'rxjs';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalActionComponent } from '../../modal/modal-action/modal-action.component';
import { faHome, faSave, faPlus } from '@fortawesome/free-solid-svg-icons';
import { FormIOModulo } from 'src/app/model/dto/form-iomodulo';
import { Categoria } from '../../../model/dto/categoria';
import { MoonboError } from '../../../model/common/moonbo-error';
import { ErrorNotificationService } from '../../../services/error-notification.service';
import { Processo } from '../../../model/dto/processo';
import { uniqueByPropMaxValue } from 'src/app/common/utils/array-util';
import { UserInfo } from '../../../model/common';
import { PortaliIf } from 'src/app/model/dto/portali-if';
import { TipoCodiceIstanza } from 'src/app/model/dto/tipo-codice-istanza';
import { AlertService } from 'src/app/modules/alert';
import { MsgModulo } from 'src/app/common/messaggi';

enum VIEWS {
    ELENCO = 'elenco-moduli',
    CREA = 'crea-modulo',
    DETTAGLIO_MODULO = 'dettaglio-modulo',
    MODIFICA = 'modifica-modulo',
    ERRORE = 'errore',
    CAMBIA_STATO = 'cambia-stato',
    DATI_GENEARLI = 'dati-generali',
    NUOVA_VERSIONE = 'nuova-versione',
    DUPLICA_MODULO = 'duplica-modulo'
}


@Component({
    selector: 'app-miei-moduli-container',
    templateUrl: './miei-moduli-container.component.html',
    styleUrls: ['./miei-moduli-container.component.css'],
    encapsulation: ViewEncapsulation.None
})
export class MieiModuliContainerComponent implements OnInit {

    elencoModuli: Modulo[];
    elencoCategorie: Categoria[];
    elencoProcessi: Processo[];
    elencoPortali: PortaliIf[];
    elencoTipoCodiceIstanza: TipoCodiceIstanza[];

    currentView: string;
    selectedModulo: Modulo;
    moduloInLavorazione: Modulo;
    resetFormioEditor$ = new Subject<void>();

    faHome = faHome;
    faSave = faSave;
    faPlus = faPlus;
    user: UserInfo;
    private closeResult: string;

    isModuloModificabile = false;

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

    constructor(private sharedService: SharedService,
        private moonservice: MoonboblService,
        private spinnerService: NgxSpinnerService,
        private modalService: NgbModal,
        private moonboblService: MoonboblService,
        protected alertService: AlertService
    ) {
    }

    ngOnInit(): void {
        this.spinnerService.show();
        this.user = this.sharedService.UserLogged;
        if (this.sharedService.mieiModuli.length > 0) {
            this.elencoModuli = this.sharedService.mieiModuli;
            this.ordinaModuli();
        } else {
            this.getElencoModuli();
        }

        this.moonservice.getElencoCategorie().subscribe(
            (res) => {
                this.elencoCategorie = res;
                this.spinnerService.hide();
            },
            error => {
                // alert('Errore recupero Categorie');
                this.alertService.error(MsgModulo.ERRORE_CATEGORIE, this.alertOptionsNoAutoClose);
                this.spinnerService.hide();
            }
        );
        this.moonservice.getElencoProcessi().subscribe(
            (res) => {
                this.elencoProcessi = res;
                this.spinnerService.hide();
            },
            error => {
                // alert('Errore recupero Elenco Processi');
                this.alertService.error(MsgModulo.ERRORE_PROCESSI, this.alertOptionsNoAutoClose);
                this.spinnerService.hide();
            }
        );
        this.moonservice.getPortali().subscribe(
            (res) => {
                this.elencoPortali = res;
                this.spinnerService.hide();
            },
            error => {
                // alert('Errore recupero Elenco Portali');
                this.alertService.error(MsgModulo.ERRORE_PORTALI, this.alertOptionsNoAutoClose);
                this.spinnerService.hide();
            }
        );
        this.moonservice.getElencoTipoCodiceIstanza().subscribe(
            (res) => {
                this.elencoTipoCodiceIstanza = res;
                this.spinnerService.hide();
            },
            error => {
                // alert('Errore recupero Elenco TipoCodiceIstanza');
                this.alertService.error(MsgModulo.ERRORE_CODICI_TIPO_ISTANZA, this.alertOptionsNoAutoClose);
                this.spinnerService.hide();
            }
        );

        this.currentView = VIEWS.ELENCO;
    }

    creaModulo() {
        this.currentView = VIEWS.CREA;
    }


    modificaModulo(modulo: Modulo) {
        this.selectedModulo = modulo;
        this.currentView = VIEWS.MODIFICA;
    }


    getElencoModuli(): void {
        this.moonboblService.getElencoModuliForce(true, null).subscribe(
            moduli => {
                this.elencoModuli = [];
                this.elencoModuli = moduli;
                this.ordinaModuli();
                this.currentView = VIEWS.ELENCO;

                this.spinnerService.hide();
            }
        );
    }

    setVistaModuli($event: any) {

        console.log('###  SET VISTA MODULI ###');
        // console.log($event);

        // this.elencoModuli = this.sharedService.mieiModuli;
        // this.ordinaModuli();
        // this.getElencoModuli();

        // if (this.sharedService.mieiModuli.length > 0 && ($event === 'back')) {
        if (this.sharedService.mieiModuli.length > 0) {
            this.elencoModuli = this.sharedService.mieiModuli;
            this.ordinaModuli();
            this.currentView = VIEWS.ELENCO;
            this.spinnerService.hide();
        } else {
            this.getElencoModuli();
            // this.getModuli();
        }

        if ($event === 'back') {
            this.currentView = VIEWS.ELENCO;
            this.spinnerService.hide();
        }
        if ($event.startsWith('save')) {
            const idModulo = $event.split('-')[1];
            console.log('miei-moduli-container::setVistaModuli ' + $event + ' idModulo=' + idModulo);
            this.moonboblService.getElencoModuliForce(true, idModulo).subscribe(
                moduliOfOne => {
                    if (moduliOfOne.length === 1) {
                        this.elencoModuli = this.elencoModuli
                            .map(m => m.idModulo === moduliOfOne[0].idModulo ? moduliOfOne[0] : m);
                        this.sharedService.mieiModuli = this.sharedService.mieiModuli
                            .map(m => m.idModulo === moduliOfOne[0].idModulo ? moduliOfOne[0] : m);
                    }
                }
            );
        }
    }

    private ordinaModuli() {
        this.elencoModuli.sort(
            ((m1, m2) => m2.idModulo - m1.idModulo)
        );
    }

    cambiaStato(modulo: Modulo) {
        this.moduloInLavorazione = modulo;
        this.currentView = VIEWS.CAMBIA_STATO;
    }

    dettaglioModulo(modulo: Modulo) {
        this.moduloInLavorazione = modulo;
        this.currentView = VIEWS.DETTAGLIO_MODULO;
    }

    datiGenerali(modulo: Modulo) {
        this.moduloInLavorazione = modulo;
        this.currentView = VIEWS.DATI_GENEARLI;
    }

    eliminaModulo(m: Modulo) {
        // eliminazione del modulo
        const modalRef = this.modalService.open(ModalActionComponent);
        modalRef.componentInstance.modal_titolo = 'MoonBackoffice';
        modalRef.componentInstance.modal_contenuto = 'Vuoi eliminare il modulo con codice ' + m.codiceModulo + '?';
        modalRef.result.then(
            (result) => {
                console.log('Result:' + result);
                // this.closeResult = `Closed with: ${result}`;
                if (result === 'OK') {
                    this.spinnerService.show();
                    this.moonboblService.eliminaModulo(m).subscribe(
                        () => {
                            // elimino il modulo da array
                            this.sharedService.mieiModuli.splice(this.elencoModuli.findIndex(el => el.idModulo === m.idModulo), 1);
                            this.elencoModuli.splice(this.elencoModuli.findIndex(el => el.idModulo === m.idModulo), 1);
                            this.spinnerService.hide();
                        }, (err: MoonboError) => {
                            // informazioni sulla chiamata
                            this.spinnerService.hide();
                            // this.errNotificationError.notification.next(err);
                            alert(err.errorMsg);
                        }
                    );
                }
            }, (reason) => {
                // faccio nullla
            }
        );
    }

    nuovaVersione(modulo: Modulo) {
        this.moduloInLavorazione = modulo;
        this.currentView = VIEWS.NUOVA_VERSIONE;
    }
    duplicaModulo(modulo: Modulo) {
        this.moduloInLavorazione = modulo;
        this.currentView = VIEWS.DUPLICA_MODULO;
    }

    /*
       Gestione evento back scatenata dal conponente formio-editor
     */
    backFromFormIOEditor() {
        this.currentView = VIEWS.ELENCO;
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
