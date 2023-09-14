/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { faBackward } from '@fortawesome/free-solid-svg-icons';
import { faFilePdf } from '@fortawesome/free-solid-svg-icons';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { faSyncAlt } from '@fortawesome/free-solid-svg-icons';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app/services/shared.service';
import * as _ from 'lodash';
import { Istanza } from 'src/app/model/dto/istanza';
import { Workflow } from 'src/app/model/dto/workflow';
import { StoricoWorkflow } from 'src/app/model/dto/storicoWorkflow';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { ModalActionComponent } from 'src/app/components/modal/modal-action/modal-action.component';
import { Allegato } from 'src/app/model/dto/allegato';
import { LogEmail } from 'src/app/model/dto/log-email';
import { LogPraticaCosmo } from 'src/app/model/dto/log-pratica-cosmo';
import { Messaggi } from 'src/app/common/messaggi';
import { IstanzaDettaglioComponent } from './istanza-dettaglio/istanza-dettaglio.component';
import { AlertService } from 'src/app/modules/alert';
import { LogServizioCosmo } from 'src/app/model/dto/log-servizio-cosmo';
import { STORAGE_KEYS } from 'src/app/common/costanti';
import { LogMyDocs } from 'src/app/model/dto/log-mydocs';
import { TicketCrmRichiesta } from 'src/app/model/dto/ticket-crm-richiesta';

@Component({
  selector: 'app-istanza-container',
  templateUrl: './istanza-container.component.html',
  styleUrls: ['./istanza-container.component.scss']
})

export class IstanzaContainerComponent implements OnInit {

  private idIstanza: number = null;
  public msgErr: string = null;
  public istanza: Istanza;
  public nextWorkflow: Workflow[] = [];
  public numOpzioni: string = null;
  public prevAction: Workflow;
  faBackward = faBackward;
  faArrowRight = faArrowRight;
  faSyncAlt = faSyncAlt;
  faEye = faEye;
  faFilePdf = faFilePdf;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;
  faEdit = faEdit;
  msg = '';
  isAdmin = false;
  isUtenteAbilitatoWorkflow = false;
  isIstanzaOfModuloWithWFCosmo = false;

  isConPagamenti = false;
  isPagamentiOpen = false; // BO: false, FO: true
  isStoricoOpen = false;
  storicoWorkflow: StoricoWorkflow[]; // undefined on init
  isAllegatiOpen = false;
  allegati: Allegato[]; // undefined on init
  isLogEmailOpen = false;
  logEmail: LogEmail[];
  isLogCosmoOpen = false;
  isLogMyDocsOpen = false;
  logCosmo: LogPraticaCosmo[];
  logServizioCosmo: LogServizioCosmo[];
  logMyDocs: LogMyDocs[];
  isOperazioniAvanzateOpen = false;

  isLogTicketOpen = false;
  logTicket: TicketCrmRichiesta[];
  isIstanzaOfModuloWithTicket = false;

  isOperatoreMinSIMP = false;

  isInAttesaDiPagamento = false;
  isPagatoMaNonInviato = false;
  alertOptions = {
    id: 'alert-istanza-container',
    autoClose: false,
    keepAfterRouteChange: true
  };


  msgErroreAzione: string;


  //@ViewChild(IstanzaDettaglioComponent, { static: true }) dettaglioChild: IstanzaDettaglioComponent;
  // @ViewChild(IstanzaStoricoComponent, { static: true }) storicoChild: IstanzaStoricoComponent;
  // @ViewChild(IstanzaAllegatiComponent, { static: true }) allegatiChild: IstanzaAllegatiComponent;
  // @ViewChild(LogCosmoComponent, { static: true }) logCosmoChild: IstanzaAllegatiComponent;
  // @ViewChild(LogEmailComponent, { static: true }) logEmailChild: IstanzaAllegatiComponent;

  constructor(private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private router: Router,
    public location: Location,
    private modalService: NgbModal,
    private sharedService: SharedService,
    private alertService: AlertService
  ) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
    this.isUtenteAbilitatoWorkflow = this.sharedService.UserLogged?.hasRuoloOperatorMinSIMP() ||
      this.sharedService.UserLogged?.isTipoADMIN() || this.sharedService.UserLogged?.hasRuoloOperatorCOMP(); 
    this.isOperatoreMinSIMP = this.sharedService.UserLogged?.hasRuoloOperatorMinSIMP();
  }

  ngOnInit() {
    this.msgErroreAzione = "";
    this.idIstanza = this.route.snapshot.params.id;
    if (!this.idIstanza) {
      console.log('id non definito');
      return;
    }
    this.getIstanza(this.idIstanza);
    this.getnextWorkflows(this.idIstanza);
    this.getprevWorkflow(this.idIstanza);
  }

  refresh(): void {
    this.spinnerService.show();
    this.ngOnInit();
    this.isStoricoOpen ? this.getStoricoWorkflow() : this.storicoWorkflow = null;
    this.isLogCosmoOpen ? this.getLogPraticaCosmo() : this.logCosmo = null;
    this.isLogEmailOpen ? this.getLogEmail() : this.logEmail = null;
    this.isLogTicketOpen ? this.getLogTicket() : this.logTicket = null;
    this.spinnerService.hide();
  }

  doAzione(idWorkflow: number, nomeAzione: string) {
    log('istanza-container::doAzione  idWorkflow=' + idWorkflow);
    this.moonboblService.doAzione(this.idIstanza, idWorkflow).subscribe(response => {
      const respIstanza = response as Istanza;
      this.refresh();
    },
      error => {
        console.log(error);
        this.spinnerService.hide();
        // this.msgErroreAzione = Messaggi.msgErroreAzione1 + "'" + nomeAzione + "'" + Messaggi.msgErroreAzione2;
        this.alertService.error(Messaggi.msgErroreAzione1 + "'" + nomeAzione + "'" + Messaggi.msgErroreAzione2, this.alertOptions);
      });

    // this.router.navigate(['istanza/' + this.idIstanza]);
    // this.refresh();
  }

  confermaAzione(nextAction: Workflow) {
    log('istanza-container::confermaAzione');
    const mdRef = this.modalService.open(ModalActionComponent);
    mdRef.componentInstance.modal_titolo = 'Conferma azione';
    mdRef.componentInstance.modal_contenuto = nextAction.nomeAzione;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      if (!nextAction.isAzioneConDati) {
        this.doAzione(nextAction.idWorkflow, nextAction.nomeAzione);
      } else {
        this.completaAzione(nextAction.idWorkflow);
      }
      // this.storicoWorkflow = null;
    }, (reason) => {
      console.log(reason);
    });
  }

  confermaAnnulla(idWorkflow: number) {
    console.log('inizio confermaAzione');

    const mdRef = this.modalService.open(ModalActionComponent);
    mdRef.componentInstance.modal_titolo = 'Conferma azione';
    mdRef.componentInstance.modal_contenuto = 'Annulla passo precedente';
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      this.annullaAzione(this.idIstanza, idWorkflow);
      // this.storicoWorkflow = null;
    }, (reason) => {
      console.log(reason);
    });
  }

  completaAzione(idWorkflow: number) {
    this.router.navigate(['istanze/' + this.idIstanza + '/completa-azione/' + idWorkflow]);
    // this.storicoWorkflow = null;
  }

  annullaAzione(idIstanza: number, idWorkflow: number) {
    this.moonboblService.annullaAzione(idIstanza, idWorkflow).subscribe(response => {
      const respIstanza = response as Istanza;
      this.refresh();
    });
    // this.refresh();
  }

  getIstanza(idIstanza: number) {
    this.moonboblService.getIstanza(idIstanza)
      .then(istanza => {
        this.istanza = istanza;
        this.readModuloAttributes();
        this.initConPagamenti();
        this.initLogCosmo();
        this.getLogEmail();
        this.initLogMyDocs();
        this.getLogTicket();
      })
      .catch(errore => {
        console.log('***' + errore);
        this.msgErr = errore;
      });
  }


  readModuloAttributes() {
    log('istanza-container::readModuloAttributes ' + 'modattr' + this.istanza.modulo.idModulo);
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + this.istanza.modulo.idModulo));
    if (!datiAtt) {
      log('istanza-container::readModuloAttributes ERROR NOT FOUND datiAtt: modattr' + this.istanza.modulo.idModulo);
      return;
    }
    log('istanza-container::readModuloAttributes datiAtt = ' + datiAtt);
    log('istanza-container::readModuloAttributes datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));

    if (datiAtt.attributi?.PSIT_COSMO && datiAtt.attributi.PSIT_COSMO === 'S') {
      this.isIstanzaOfModuloWithWFCosmo = true;
    }
    if (datiAtt.attributi?.PSIT_CRM && datiAtt.attributi.PSIT_CRM === 'S') {
      this.isIstanzaOfModuloWithTicket = true;
    }

  }

  initConPagamenti() {
    log('istanza-container::initConPagamenti() BEGIN');
    if (this.istanza && this.istanza.pagamenti) {
      log('istanza-container::initConPagamenti() TRUE');
      this.isConPagamenti = true;
      if ((this.istanza.stato?.nome === 'IN PAGAMENTO' || this.istanza.stato?.nome === 'INVIATA') && !this.istanza?.dataEsitoPagamento) {
        log('istanza-container::initConPagamenti IN ATTESA DI PAGAMENTO');
        this.isInAttesaDiPagamento = true;
      }
      if (this.istanza.stato?.nome === 'IN PAGAMENTO' && this.istanza?.dataEsitoPagamento) {
        log('istanza-container::initConPagamenti ALERT DEVI INVIARE');
        this.isPagatoMaNonInviato = true;
      }
    }
  }

  initLogCosmo() {
    log('istanza-container::initLogCosmo() isIstanzaOfModuloWithWFCosmo=' + this.isIstanzaOfModuloWithWFCosmo);
    if (this.isIstanzaOfModuloWithWFCosmo) {
      this.getLogPraticaCosmo();
      this.getLogServizioCosmo();
    }
  }

  initLogMyDocs() {
    log('istanza-container::initLogMyDocs()');
    this.getLogMyDocs();
  }

  getnextWorkflows(idIstanza: number) {
    this.moonboblService.getWorkflows(idIstanza).then(
      elencoWorkflow => {
        this.nextWorkflow = elencoWorkflow;
        if (this.nextWorkflow.length === 0) {
          this.msg = 'Nessuno step trovato !';
        }
        if (this.nextWorkflow.length === 1) {
          this.numOpzioni = '1 opzione';
        } else {
          this.numOpzioni = this.nextWorkflow.length + ' opzioni';
        }

        //        this.getStoricoWorkflow(idIstanza);
        this.spinnerService.hide();
      }
    ).catch(errore => {
      this.spinnerService.hide();
      console.log('***' + errore);
    });
  }

  getprevWorkflow(idIstanza: number) {
    this.moonboblService.getPrevWorkflow(idIstanza).then(
      prevWorkflow => {
        this.prevAction = prevWorkflow;
        this.spinnerService.hide();
      }
    ).catch(errore => {
      this.spinnerService.hide();
      console.log('***' + errore);
    });
  }

  getStoricoWorkflow() {
    log('istanza-container::getStoricoWorkflow() storicoWorkflow=' + this.storicoWorkflow);
    if (!this.idIstanza) {
      return;
    }
    // if (this.storicoWorkflow) {
    //   log('istanza-container::getStoricoWorkflow() FROM CACHE this.storicoWorkflow=' + this.storicoWorkflow);
    //   return this.storicoWorkflow;
    // }
    this.moonboblService.geStoricotWorkflow(this.idIstanza)
      .then(storicoWorkflow => {
        log('istanza-container::getStoricoWorkflow() moonboblService.getStoricoWorkflow() storicoWorkflow=' + storicoWorkflow);
        this.storicoWorkflow = storicoWorkflow;
        //        this.spinnerService.hide();
      }).catch(errore => {
        //      this.spinnerService.hide();
        log('istanza-container::getStoricoWorkflow() moonboblService.getStoricoWorkflow() ERR=' + errore);
      });
  }

  getAllegati(): Allegato[] {
    log('istanza-container::getAllegati() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
    // if (this.allegati) {
    //   log('istanza-container::getAllegati() FROM CACHE this.allegati=' + this.allegati);
    //   return this.allegati;
    // }
    //    this.spinnerService.show();
    this.moonboblService.getAllegati(this.idIstanza)
      .then(allegati => {
        log('istanza-container::getAllegati() moonboblService.getAllegati() allegati=' + allegati);
        this.allegati = allegati;
        //          this.spinnerService.hide();
      }).catch(errore => {
        //        this.spinnerService.hide();
        log('istanza-container::getAllegati() moonboblService.getAllegati() ERR=' + errore);
      });
  }

  getLogEmail(): LogEmail[] {
    log('istanza-container::getLogEmail() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
    // if (this.logEmail) {
    //   log('istanza-container::getLogEmail() FROM CACHE this.logEmail=' + this.logEmail);
    //   return this.logEmail;
    // }
    //    this.spinnerService.show();
    this.moonboblService.getLogEmail(this.idIstanza)
      .then(logEmail => {
        log('istanza-container::getLogEmail() moonboblService.getLogEmail() logEmail=' + logEmail);
        this.logEmail = logEmail;
        //        this.spinnerService.hide();
      }).catch(errore => {
        //        this.spinnerService.hide();
        log('istanza-container::getLogEmail() moonboblService.getLogEmail() ERR=' + errore);
      });
  }

  getLogPraticaCosmo() {
    log('istanza-container::getLogPraticaCosmo() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
    // if (this.logCosmo) {
    //   log('istanza-container::getLogPraticaCosmo() FROM CACHE this.logCosmo=' + this.logCosmo);
    //   return this.logCosmo;
    // }
    //    this.spinnerService.show();
    this.moonboblService.getLogPraticaCosmo(this.idIstanza)
      .then(logCosmo => {
        log('istanza-container::getLogPraticaCosmo() moonboblService.getLogPraticaCosmo() logCosmo=' + logCosmo);
        this.logCosmo = logCosmo;
        //          this.spinnerService.hide();
      }).catch(errore => {
        //        this.spinnerService.hide();
        log('istanza-container::getLogPraticaCosmo() moonboblService.getLogPraticaCosmo() ERR=' + errore);
      });
  }


  getLogServizioCosmo() {
    log('istanza-container::getLogServizioCosmo() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
 
    this.moonboblService.getLogServizioCosmo(this.idIstanza)
      .then(logServizioCosmo => {
        log('istanza-container::getLogServizioCosmo() moonboblService.getLogServizioCosmo() logServizioCosmo=' + logServizioCosmo);
        this.logServizioCosmo = logServizioCosmo;
        //          this.spinnerService.hide();
      }).catch(errore => {
        //        this.spinnerService.hide();
        log('istanza-container::getLogServizioCosmo() moonboblService.getLogServizioCosmo() ERR=' + errore);
      });
  }

  getLogMyDocs() {
    log('istanza-container::getLogMyDocs() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }

    this.moonboblService.getLogMyDocs(this.idIstanza)
      .then(logMyDocs => {
        log('istanza-container::getLogMyDocs() moonboblService.getLogMyDocs() mydocs =' + logMyDocs);
        this.logMyDocs = logMyDocs;
        //          this.spinnerService.hide();
      }).catch(errore => {
        //        this.spinnerService.hide();
        log('istanza-container::getLogMyDocs() moonboblService.getLogMyDocs() ERR=' + errore);
      });
  }

  getLogTicket() {
    log('istanza-container::getLogTicket() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }

    if(this.isIstanzaOfModuloWithTicket){
      this.moonboblService.getLogTicket(this.idIstanza)
      .then(logTicket => {
        log('istanza-container::getLogTicket() moonboblService.getLogTicket() Ticket =' + logTicket);
        this.logTicket = logTicket;
      }).catch(errore => {
        log('istanza-container::getLogTicket() moonboblService.getLogTicket() ERR=' + errore);
      });
    }
    
  }

  tornaIndietro() {
    // import {Location} from '@angular/common';
    // this._location.back();
    // this.router.navigate(['istanze/'] );
    // gestione del ritorno al router chiamante attraverso service routing-state
    // this.router.navigate([this.previousRoute] );

    this.router.navigate([this.sharedService.nav.route]);
  }

  togglePagamenti() {
    this.isPagamentiOpen = !this.isPagamentiOpen;
  }
  togglePagamentiTooltip(): string {
    return this.isPagamentiOpen ? 'Chiudi pagamenti' : 'Apri pagamenti';
  }

  toggleStorico() {
    this.isStoricoOpen = !this.isStoricoOpen;
  }
  toggleStoricoTooltip(): string {
    return this.isStoricoOpen ? 'Chiudi storico' : 'Apri storico';
  }
  toggleStoricoIcon(): string {
    return this.isStoricoOpen ? 'faAngleUp' : 'faAngleDown';
  }

  toggleAllegati() {
    this.isAllegatiOpen = !this.isAllegatiOpen;
  }

  toggleLogEmail() {
    this.isLogEmailOpen = !this.isLogEmailOpen;
  }

  toggleLogMyDocs() {
    this.isLogMyDocsOpen = !this.isLogMyDocsOpen;
  }

  toggleLogTicket() {
    this.isLogTicketOpen = !this.isLogTicketOpen;
  }

  toggleLogCosmo() {
    this.isLogCosmoOpen = !this.isLogCosmoOpen;
  }

  toggleOperazioniAvanzate() {
    this.isOperazioniAvanzateOpen = !this.isOperazioniAvanzateOpen;
  }

  getAlert(message) {
    const type = message.type;
    const text = message.text;

    switch (type) {
      case 'success': {
        this.alertService.success(text, this.alertOptions);
        break;
      }
      case 'info': {
        this.alertService.info(text, this.alertOptions);
        break;
      }
      case 'error': {
        this.alertService.error(text, this.alertOptions);
        break;
      }
      case 'warn': {
        this.alertService.warn(text, this.alertOptions);
        break;
      }
      default: {
        this.alertService.warn(text, this.alertOptions);
        break;
      }
    }

  }

}

function log(a: any) {
  //  console.log(a);
}
