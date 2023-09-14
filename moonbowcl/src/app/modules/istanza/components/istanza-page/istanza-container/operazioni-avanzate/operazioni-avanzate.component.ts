/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, OnChanges, SimpleChanges, EventEmitter } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Istanza } from 'src/app/model/dto/istanza';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import * as _ from 'lodash';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalNotifyComponent } from 'src/app/components/modal/modal-notify/modal-notify.component';
import { Workflow } from 'src/app/model/dto/workflow';
import { STORAGE_KEYS } from 'src/app/common/costanti';

@Component({
  selector: 'app-istanza-operazioni-avanzate',
  templateUrl: './operazioni-avanzate.component.html',
  styleUrls: ['./operazioni-avanzate.component.scss']
})
export class OperazioniAvanzateComponent implements OnInit {

  attributi: any;

  isAdmin = false;

  moduloConProtocollazione = false;
  isEmailRinviabileUser = false;
  isEmailRinviabileProtocollo = false;
  isEmailRinviabilePostCallbackProtocollo = false;

  hasProcessProtocolloBO: boolean;

  @Input() istanza: Istanza;
  @Input() nextWorkflow: Workflow[];

  @Output('alertService') alert = new EventEmitter();

  constructor(
    private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    private sharedService: SharedService,
    private modalService: NgbModal
  ) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('istanza-operazioni-avanzate::ngOnInit() istanza=' + this.istanza);
    if (this.istanza) {
      this.refresh('ngOnInit');
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    log('istanza-operazioni-avanzate::ngOnChanges() changes.istanza.currentValue=' + changes.istanza?.currentValue);
    log('istanza-operazioni-avanzate::ngOnChanges() this.istanza=' + this.istanza);
    if (this.istanza && changes.istanza) {
      this.refresh('ngOnChanges');
    }
    // if (this.istanza && (changes.logCosmo || changes.logServizioCosmo)) {
    //   this.refreshCOSMO();
    // }
  }

  isAzioneProtocollaPresenteInNextWorkflow(): boolean {
    let result = false;
    if (this.nextWorkflow && this.nextWorkflow.length>0) {
      const protocolloNw = this.nextWorkflow.filter(w => w['codiceAzione'] === 'PROTOCOLLA');
      if (protocolloNw) {
        result = true;
      }
    }
    return result;
  }

  refresh(caller: string) {
    if (this.hasProcessProtocolloBO === undefined) {
      this.initHasProcessProtocolloBO(this.istanza.modulo.idModulo); 
    }
    log('istanza-operazioni-avanzate::refresh ' + 'modattr' + this.istanza.modulo.idModulo);
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + this.istanza.modulo.idModulo));
    if (!datiAtt) {
      log('istanza-operazioni-avanzate::refresh ERROR NOT FOUND datiAtt: modattr' + this.istanza.modulo.idModulo);
      return;
    }
    log('istanza-operazioni-avanzate::refresh datiAtt = ' + datiAtt);
    log('istanza-operazioni-avanzate::refresh datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    this.attributi = datiAtt.attributi;

    const moduloConProtocollazioneFO = (datiAtt.attributi?.PSIT_PROTOCOLLO && datiAtt.attributi.PSIT_PROTOCOLLO === 'S');
    this.moduloConProtocollazione = moduloConProtocollazioneFO;
    if (!moduloConProtocollazioneFO) {
      const isAzioneProtocollaPresente = this.isAzioneProtocollaPresenteInNextWorkflow();
      this.moduloConProtocollazione = this.hasProcessProtocolloBO && !isAzioneProtocollaPresente;
    }

    if (datiAtt.attributi?.PSIT_EMAIL && datiAtt.attributi.PSIT_EMAIL === 'S' &&
      (this.sharedService.UserLogged.isTipoADMIN() || this.sharedService.UserLogged.hasRuoloOperatorMinADV())) {
      this.isEmailRinviabileUser = true;
      if (datiAtt.attributi?.PSIT_EMAIL_CONF && datiAtt.attributi.PSIT_EMAIL_CONF.includes('protocollo_to') &&
        (this.sharedService.UserLogged.isTipoADMIN())) {
        this.isEmailRinviabileProtocollo = true;
      }
    }

    log('TIPO filtro PCPT_IN_EMAIL' + datiAtt.attributi.PCPT_IN_EMAIL);
    if (this.istanza.numeroProtocollo && datiAtt.attributi?.PCPT_IN_EMAIL && datiAtt.attributi.PCPT_IN_EMAIL === 'S' &&
      this.isAdmin) {
      this.isEmailRinviabilePostCallbackProtocollo = true;
    }
    // this.refreshCOSMO();
    // this.refreshCRM();
  }

  initHasProcessProtocolloBO(idModulo: number) {
    let result = false;
    this.moonboblService.hasProtocolloBo(idModulo).then(
      ret => {
          this.hasProcessProtocolloBO = ret;
      }
    );
  }

  protocolla() {
    this.moonboblService.protocolla(this.istanza.idIstanza).subscribe(() => {
      this.spinnerService.hide();
      this.notify('Protocollazione effettuata', function () { console.log('protocollazione a buon fine') });
    },
      (err: MoonboError) => {
        this.spinnerService.hide();
        this.alert.emit({ text: 'Protocollazione fallita', type: 'error' });
      }
    );
  }

  // dest: user/protocollo/user,protocollo/postCallbackPrt
  rinviaEmail(dest: string) {
    this.moonboblService.rinviaEmail(this.istanza.idIstanza, dest).subscribe(() => {
      this.spinnerService.hide();
      this.notify('Richiesta di rinvio Email effettuata', function () { console.log('re-invio email a buon fine') });
    },
      (err: MoonboError) => {
        this.spinnerService.hide();
        this.alert.emit({ text: 'Re-invio email con esito negativo', type: 'error' });
      }
    );
  }

  creaPraticaEdAvviaProcessoCosmo() {
    log('creaPraticaEdAvviaProcessoCosmo ' + this.istanza.idIstanza);
    this.spinnerService.show();
    this.moonboblService.creaPraticaEdAvviaProcessoCosmo(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        this.notify('Reinvio con esito positivo', function () { this.initLogCosmo.emit() }.bind(this));
        // this.initLogCosmo.emit(); // per refresh
        // this.refreshCOSMO();
      },
      (err: MoonboError) => {
        this.spinnerService.hide();
        this.alert.emit({ text: 'Reinvio con esito negativo', type: 'error' });
      }
    );
  }

  reinviaIntegrazioneCosmo() {
    log('reinviaIntegrazioneCosmo ' + this.istanza.idIstanza);
    this.spinnerService.show();
    this.moonboblService.inviaIntegrazione(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        this.notify('Reinvio con esito positivo', function () { this.initLogServizioCosmo.emit() }.bind(this));
        // this.initLogCosmo.emit(); // per refresh
        // this.refreshCOSMO();
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        this.alert.emit({ text: 'Reinvio con esito negativo', type: 'error' });
      }
    );
  }

  creaTicketCRM() {
    log('creaTicketCRM ' + this.istanza.idIstanza);
    this.moonboblService.creaTicketCrm(this.istanza.idIstanza).subscribe(
      () => {
        this.spinnerService.hide();
        this.notify('Richiesta di creazione Ticket CRM effettuata', function () { this.refreshCRM() }.bind(this));
        // this.refreshCRM();
      },
      (err: MoonboError) => {
        this.spinnerService.hide();
        this.alert.emit({ text: 'Invio email con esito negativo', type: 'error' });
      }
    );
  }

  notify(contenuto: string, doAction: () => void) {
    const mdRef = this.modalService.open(ModalNotifyComponent);
    mdRef.componentInstance.modal_titolo = 'Azione';
    mdRef.componentInstance.modal_contenuto = contenuto;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      doAction();
    }, (reason) => {
      console.log(reason);
      doAction();
    });
  }

}


function log(a: any) {
  //  console.log(a);
}
