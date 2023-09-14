/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {
  faAngleDown,
  faAngleUp,
  faArrowRight,
  faBackward,
  faCaretSquareRight,
  faEdit,
  faEye,
  faFilePdf,
  faPaperclip,
  faSyncAlt
} from '@fortawesome/free-solid-svg-icons';
import {NgbModal,NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import * as _ from 'lodash';
import {NgEventBus} from 'ng-event-bus';
import {WebSocketSubject} from 'rxjs/webSocket';
import {AZIONI, MODULI, STATI} from 'src/app/common/costanti';
import {Messaggi} from 'src/app/common/messaggi';
import {NavSelection} from 'src/app/common/nav-selection';
import {ModalBasicComponent} from 'src/app/components/modal-basic/modal-basic.component';
import {ModalCloseComponent} from 'src/app/components/modal-close/modal-close.component';
import {ModalFeedbackComponent} from 'src/app/components/modal-feedback/modal-feedback.component';
import {ModalNotifyComponent} from 'src/app/components/modal-notify/modal-notify.component';
import {STORAGE_KEYS, Costanti} from 'src/app/common/costanti';
import {User} from 'src/app/model/common/user';
import {Allegato} from 'src/app/model/dto/allegato';
import {DatiPagamento} from 'src/app/model/dto/dati-pagamento';
import {Documento} from 'src/app/model/dto/documento';
import {EpayNotificaMsg} from 'src/app/model/dto/epay-notifica-msg';
import {Istanza} from 'src/app/model/dto/istanza';
import {Nav} from 'src/app/model/dto/nav';
import {Notifica} from 'src/app/model/dto/notifica';
import {StoricoWorkflow} from 'src/app/model/dto/storicoWorkflow';
import {Workflow} from 'src/app/model/dto/workflow';
import {WsEpayNotificaMsg} from 'src/app/model/dto/ws-epay-notifica-msg';
import {DecodificaAzione} from 'src/app/model/util/decodifica/azione-decodifica';
import {HeartBeat} from 'src/app/model/util/heart-beat';
import {MoonfoblService} from 'src/app/services/moonfobl.service';
import {SecurityService} from 'src/app/services/security.service';
import {saveBlobIE} from 'src/app/services/service.utils';
import {SharedService} from 'src/app/services/shared.service';
import {ValidationService} from 'src/app/services/validation.service';
import {StorageManager} from 'src/app/util/storage-manager';
import {defaultEnvironment} from '../../../../../../environments/default.environment';
import {WebSocketEpayNotificaService} from '../../../../../services/web-socket-epay-notifica.service';
import {IstanzaDettaglioComponent} from './istanza-dettaglio/istanza-dettaglio.component';
import { ModalPayComponent } from 'src/app/components/modal-pay/modal-pay.component';


@Component({
  selector: 'app-istanza-container',
  templateUrl: './istanza-container.component.html',
  styleUrls: ['./istanza-container.component.scss']
})

export class IstanzaContainerComponent implements OnInit, OnDestroy {
  private idIstanza: number = null;
  public istanza: Istanza;
  public msgErr: string = null;
  public nextWorkflow: Workflow[] = [];
  public numOpzioni: string = null;
  public prevAction: Workflow;
  epayNotificaMsg: EpayNotificaMsg;
  isConPagamenti = false;
  faBackward = faBackward;
  faArrowRight = faArrowRight;
  faSyncAlt = faSyncAlt;
  faEye = faEye;
  faFilePdf = faFilePdf;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;
  faPaperclip = faPaperclip;
  faEdit = faEdit;
  faCaretSquareRight = faCaretSquareRight;
  msg = '';
  isAdmin = false;
  isUtenteAbilitatoWorkflow = false;
  isPagamentiOpen = true; // BO: false, FO: true
  isInAttesaDiPagamento = false;
  isPagatoMaNonInviato = false;
  isStoricoOpen = false;
  storicoWorkflow: StoricoWorkflow[]; // undefined on init
  isAllegatiOpen = false;
  isDocumentiOpen = false;
  allegati: Allegato[]; // undefined on init
  documenti: Documento[]; // undefined on init
  isBozza: boolean;
  isUrlEmbeddedTornaIstanze: boolean = false;
  breadcrumbBackTitle: string;
  state: any;
  STATI = STATI;
  DecodificaAzione = DecodificaAzione;

  @ViewChild(IstanzaDettaglioComponent) dettaglioIstanza: IstanzaDettaglioComponent;
  hb: HeartBeat;
  private epayNotificaWsSubscription: WebSocketSubject<WsEpayNotificaMsg> = null;

  constructor(
    private moonfoblService: MoonfoblService,
    private route: ActivatedRoute,
    private router: Router,
    public location: Location,
    private modalService: NgbModal,
    private securityService: SecurityService,
    private sharedService: SharedService,
    private validationService: ValidationService,
    private eventBus: NgEventBus,
    private wsEpayNotificaService: WebSocketEpayNotificaService
  ) {
    this.state = this.router.getCurrentNavigation()?.extras.state;
  }

  ngOnDestroy(): void {
    if (this.epayNotificaWsSubscription) {
      this.epayNotificaWsSubscription.unsubscribe();
      this.hb.stopBeat();
    }
  }

  ngOnInit() {

    this.getUrlTornaIstanze();

    this.route.paramMap.subscribe(
      (params) => {
        if (!(params.get('id'))) {
          log('istanza-container::ngOnInit() id non definito');
          return;
        }
        this.idIstanza = +params.get('id');
        this.getIstanza(this.idIstanza);
        // fixme da eliminare
        /*this.getnextWorkflows(this.idIstanza);
        // Se lo stato è Bozza non devo invocarlo
        if (this.istanza.stato.idStato !== STATI.BOZZA) {
          this.getprevWorkflow(this.idIstanza);
        } */

        // this.getAllegati(this.idIstanza); // viene effettuato solo se richiesto il panello allegati
        // this.getStoricoWorkflow(this.idIstanza);
      });
  }

  backToIstanze() {
    this.router.navigate(['home/istanze/']);
  }


  getIstanza(idIstanza: number) {
    this.moonfoblService.getIstanza(idIstanza)
      .subscribe(istanza => {
          this.istanza = istanza;
          if ((Number(this.istanza.stato.idStato) === STATI.DA_INVIARE) && this.state) {
            this.router.navigate(['/manage-form', 'SUMMARY', this.istanza.idIstanza], { state: { caller: this.state.caller } });
          } else {
            this.getnextWorkflows(this.idIstanza);
            // Se lo stato è Bozza non devo invocarlo
            if (this.istanza.stato.idStato !== STATI.BOZZA) {
              this.getprevWorkflow(this.idIstanza);
            }
            this.initConPagamenti();
            // this.updateDocumentoDettaglio(istanza);
          }
        }
      );
  }

  initConPagamenti() {
    log('istanza-container::initConPagamenti() BEGIN');
    if (this.istanza && (this.istanza.iuv || this.istanza.codiceAvviso || this.istanza.pagamenti)) {
      log('istanza-container::initConPagamenti() TRUE');
      this.isConPagamenti = true;
      if ((this.istanza.stato?.nome === 'IN PAGAMENTO' || this.istanza.stato?.nome === 'INVIATA') && !this.istanza?.dataEsitoPagamento) {
        log('istanza-container::initConPagamenti IN ATTESA DI PAGAMENTO');
        this.isInAttesaDiPagamento = true;
      }
      if (this.istanza.stato?.nome === 'IN PAGAMENTO' && this.istanza?.dataEsitoPagamento) {
        log('istanza-container::initConPagamenti ALERT DEVI INVIARE');
        this.isPagatoMaNonInviato = true;
        this.isInAttesaDiPagamento = false;
      }
    }
  }

  // updateDocumentoDettaglio(istanza: Istanza) {
  //   if (istanza) {
  //     console.log(' istanza = ' + istanza.idIstanza);
  //     if ((this.istanza?.stato?.idStato === STATI.NOTIFICATA) ||
  //       (this.istanza?.stato?.idStato === STATI.DEFINITA) ||
  //       (this.istanza?.stato?.idStato === STATI.RESPINTA) ||
  //       (this.istanza?.stato?.idStato === STATI.ACCOLTA)) {
  //       this.moonfoblService.getDocumentoNotifica(this.istanza.idIstanza)
  //         .subscribe(doc => {
  //           this.dettaglioIstanza.documento = doc;
  //           if (this.dettaglioIstanza.documento) {
  //             this.dettaglioIstanza.isDocumentoPresent = true;
  //             this.dettaglioIstanza.nomeDocumento = this.dettaglioIstanza.documento.descrizione ?
  //               this.dettaglioIstanza.documento.descrizione : this.dettaglioIstanza.documento.nomeFile;
  //           }
  //         });
  //     }
  //   }
  // }

  refresh(): void {
    this.clearCache();
    this.ngOnInit();
  }

  doAzione(nextAction: Workflow) {
    if (nextAction.codiceAzione === DecodificaAzione['INVIA'].codice) {
      const check = this.validationService.checkDatiIstanza(this.istanza);
      // if (this.istanza.modulo.codiceModulo == 'VOUCHER') {
      if (!check) {
        return false;
        // } else if (nextAction.nomeAzione === 'Pagamento online') {
      } else if (nextAction.codiceAzione === DecodificaAzione['PAGA_ONLINE'].codice) {
        log('istanza-container::doAzione()' + nextAction.nomeAzione);
      } else {
        this.moonfoblService.inviaIstanza(this.istanza).subscribe(
          res => {
            // this.inviaFeedback(this.istanza, function () {
            //   const mdRefThanks = this.modalService.open(ModalCloseComponent);
            //   mdRefThanks.componentInstance.modal_titolo = Messaggi.messaggioThanksFeedback;
            //   mdRefThanks.componentInstance.modal_buttons = false;
            //   mdRefThanks.result.then((result) => {
            //     log('istanza-container::inviaFeedback() Closed with: ${result}' + result);
            //     this.preparaNotificaInvio(res);
            //     log('istanza-container::doAzione() istanza inviata');
            //     this.router.navigate(['home/notifica-invio-modulo']);

            //   }, (errore) => {
            //     log('istanza-container::inviaFeedback() ERRORE ' + errore);
            //   });
            // }.bind(this));

            this.preparaNotificaInvio(res);
            log('istanza-container::doAzione() istanza inviata');
            this.router.navigate(['home/notifica-invio-modulo']);
          }
        );

      }
    } else {

      this.moonfoblService.doAzione(this.idIstanza, nextAction.idAzione).subscribe(istanzaSaveResponse => {
        // switch nextAction.idAzione per pagamento online 60 online 61 sportello
        console.log('Azione selezionata:' + nextAction.codiceAzione);
        switch (nextAction.codiceAzione) {
          case DecodificaAzione['PAGA_ONLINE'].codice:
            console.log('******* PAGA ONLINE');
            if (istanzaSaveResponse.urlRedirect) {
              try {
                console.log(istanzaSaveResponse.urlRedirect);
                // sottoscrizione web socket
                this.subscribeIuvOfIstanza(true);
                // redirect PagoPa
                const a = document.createElement('a');
                a.target = '_blank';
                a.href = istanzaSaveResponse.urlRedirect;
                a.click();
              } catch (e) {
                console.log(e);
                const notifica = new Notifica('', 'Errore Gestione webSocket', 'ERROR', null);
                this.securityService.goToNotificationError(notifica);
              }
            } // if url redirect
            break;
          case DecodificaAzione['PAGA_SPORTELLO'].codice:
            console.log('***** PAGA_SPORTELLO');
            this.subscribeIuvOfIstanza(false);
            break;

          case DecodificaAzione['MODIFICA'].codice:
            console.log('***** MODIFICA');
            this.router.navigate(['/manage-form', 'UPDATE', this.idIstanza]);
            break;
        } // switch
        this.refresh();
      });
    }
    return true;
  }

  subscribeIuvOfIstanza(wsMandatory: boolean) {
    this.epayNotificaWsSubscription = this.wsEpayNotificaService.getWs();
    let epayNotificaWsSubscriptionOK = true;
    if (this.epayNotificaWsSubscription) {
      log('subscribeIuvOfIstanza] web socket istanziato');
      this.epayNotificaWsSubscription.subscribe((notificaMsg) => {
          if (notificaMsg.cmd === 'epayNotification') {
            this.manageNotifica(notificaMsg.data);
            this.hb.stopBeat();
          }
        },
        err => {
          log('subscribeIuvOfIstanza] epayNotificaWsSubscription.subscribe ERR: ' + err.message);
          epayNotificaWsSubscriptionOK = false;
          if (wsMandatory) {
            const notifica = new Notifica('', 'Errore inizializzazione webSocket', 'ERROR', null);
            this.securityService.goToNotificationError(notifica);
          }
        },
        () => {
          log('subscribeIuvOfIstanza] complete');
        }
      );
      if (epayNotificaWsSubscriptionOK) {
        // subscribe su iuv
        log('subscribeIuvOfIstanza] iuv sottoscritto ' + this.istanza.iuv);
        const msgSubscribe = { cmd: 'subscribe', iuv: this.istanza.iuv, data: null, authToken: '' };
        this.epayNotificaWsSubscription.next(msgSubscribe);
        this.hb = new HeartBeat(() => {
            log('subscribeIuvOfIstanza] beat ' + JSON.stringify(msgSubscribe));
            this.epayNotificaWsSubscription.next(msgSubscribe);
          },
          defaultEnvironment.websocketMaxIdleTime);
        this.hb.startBeat();
      }
    }
  }


  // inviaFeedback(istanza: Istanza, callback) {
  //   const mdRefFeedabck = this.modalService.open(ModalFeedbackComponent);
  //   mdRefFeedabck.componentInstance.modal_titolo = Messaggi.messaggioFeedback;
  //   // mdRefFeedabck.componentInstance.modal_contenuto = Messaggi.messaggioFeedback;
  //   mdRefFeedabck.componentInstance.istanza = istanza;
  //   mdRefFeedabck.result.then((result) => {
  //     log('istanza-container::inviaFeedback() Closed with: ${result}' + result);
  //     callback();
  //   }, (errore) => {
  //     log('istanza-container::inviaFeedback() ERRORE ' + errore);
  //   });
  // }

  annullaConfermaPagamento(){
    const mdRef = this.modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = Messaggi.messaggioConfermaAnnullamentoPagamento;
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioAnnullamentoPagamento;
    mdRef.result.then((result) => {
      log('istanza-container::annullaConfermaPagamento() Closed with: ${result}' + result);
      this.annullaPagamento();

    }, (errore) => {
      log('istanza-container::annullaConfermaPagamento() ERRORE ' + errore);
    });
  }


  annullaPagamento() {
    log('istanza-container::annullaPagamento BEGIN');
    const annullaAction = new Workflow();
    annullaAction.idAzione = 41; // ANNULLA
    // se sottoscritto, annullo sottoscrizione iuv precedente
    if (this.epayNotificaWsSubscription) {
      this.epayNotificaWsSubscription.unsubscribe();
    }
    this.doAzione(annullaAction);
  }

  confermaAzione(nextAction: Workflow) {
    log('istanza-container::confermaAzione() BEGIN');


    if (nextAction.idStatoWfPartenza === 1) {
      this.modificaIstanza();
    } else {

      let mdRef;

  //  {{istanza?.codiceAvviso }}<

      if (nextAction.codiceAzione === DecodificaAzione['PAGA_SPORTELLO'].codice) {
        let options: NgbModalOptions = {
          size: 'xl'
        };
        mdRef = this.modalService.open(ModalPayComponent , options);
        mdRef.componentInstance.modal_switch_label = Messaggi.messaggioIstruzioniPagamento;
      }
      else{
        mdRef = this.modalService.open(ModalBasicComponent);
      }
      mdRef.componentInstance.modal_titolo = 'Conferma azione "' + nextAction.nomeAzione + '"';
      mdRef.componentInstance.modal_contenuto = nextAction.nomeAzione;
      if (nextAction.codiceAzione === DecodificaAzione['INVIA'].codice) {
        let messaggioConfermaInvio = Messaggi.messaggioConfermaInvio;
        mdRef.componentInstance.modal_contenuto = messaggioConfermaInvio;
      }
      if (nextAction.codiceAzione === DecodificaAzione['GESTISCI_PAGAMENTO'].codice) {
        mdRef.componentInstance.modal_titolo = Messaggi.messaggioConfermaAvvioPagamento;
        mdRef.componentInstance.modal_contenuto = Messaggi.messaggioAvvioPagamento;
      }
      if (nextAction.codiceAzione === DecodificaAzione['PAGA_ONLINE'].codice) {
        mdRef.componentInstance.modal_titolo = Messaggi.messaggioConfermaPagaOnline;
        mdRef.componentInstance.modal_contenuto = Messaggi.messaggioPagaOnline;
      }
      if (nextAction.codiceAzione === DecodificaAzione['PAGA_SPORTELLO'].codice) {

        mdRef.componentInstance.modal_titolo = Messaggi.messaggioConfermaGestisciPagamento;
        mdRef.componentInstance.modal_contenuto = Messaggi.messaggioGestisciPagamento1+Messaggi.messaggioGestisciPagamento2+this.istanza?.codiceAvviso+Messaggi.messaggioGestisciPagamento3+Messaggi.messaggioGestisciPagamento4;

      }
      mdRef.result.then((result) => {
        log('istanza-container::confermaAzione() Closed with: ${result}' + result);
        // Check azione pagamento
        if (nextAction.codiceAzione === DecodificaAzione['PAGA_SPORTELLO'].codice) {
          const datiPagamento: DatiPagamento = null;
          this.moonfoblService.pagamentoSportello(datiPagamento);
        }

        if (!nextAction.isAzioneConDati) {
          if (!this.doAzione(nextAction)) {
            const errModal = this.modalService.open(ModalNotifyComponent);
            errModal.componentInstance.modal_titolo = 'Errore';
            errModal.componentInstance.modal_contenuto = Messaggi.messaggioDatiNonCompleti;
          }
        } else {
          this.completaAzione(nextAction.idWorkflow, nextAction.idAzione);
        }

        this.refresh();

      }, (errore) => {
        log('istanza-container::confermaAzione() ERRORE ' + errore);
      });
    }
  }

  clearCache(){
    this.storicoWorkflow = null;
    this.allegati = null;
    this.documenti = null;
    this.getStoricoWorkflow();
    // this.getDocumenti();
    this.getAllegati();
    // this.getDocumentiProtocollati();
    this.getDocumentiEmessiDaUfficio();

    console.log("clear cache storico, allegati, documenti");
  }

  confermaAnnulla(idWorkflow: number) {
    console.log('inizio confermaAzione');

    const mdRef = this.modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Conferma azione';
    mdRef.componentInstance.modal_contenuto = 'Annulla passo precedente';
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      this.annullaAzione(this.idIstanza, idWorkflow);
    }, (errore) => {
      log('istanza-container::confermaAnnulla() ERRORE ' + errore);
    });
  }

  notificaIntegrazione(idWorkflow: number) {
    console.log('inizio notificaIntegrazione');

    const mdRef = this.modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Istruzioni per l\'integrazione';
    mdRef.componentInstance.modal_contenuto = Messaggi.istruzioniIntegrazione;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      this.router.navigate(['home/istanze/' + this.idIstanza + '/completa-azione/' + idWorkflow]);
    }, (reason) => {
      console.log(reason);
    });
  }

  completaAzione(idWorkflow: number, idAzione: number) {
    // this.router.navigate(['istanze/' + this.idIstanza + '/completa-azione/' + idWorkflow]);
    if (idAzione === AZIONI.INVIA_INTEGRAZIONE) {
      this.notificaIntegrazione(idWorkflow);
    } else {
      this.router.navigate(['home/istanze/' + this.idIstanza + '/completa-azione/' + idWorkflow]);
    }
  }

  annullaAzione(idIstanza: number, idWorkflow: number) {
    this.moonfoblService.annullaAzione(idIstanza, idWorkflow).subscribe(response => {
      const respIstanza = response as Istanza;
    });
    this.refresh();
  }

  isAbilitatoModifica() {
    if (this.istanza && this.istanza.stato && this.istanza.stato.idStato === Costanti.ISTANZA_STATO_BOZZA) {
      return true;
    } else {
      return false;
    }
  }


  getnextWorkflows(idIstanza: number) {
    this.moonfoblService.getWorkflows(idIstanza).subscribe(
      elencoWorkflow => {
        this.nextWorkflow = elencoWorkflow;
        if (this.nextWorkflow?.length === 0) {
          this.msg = 'Nessuno step trovato !';
        }
        if (this.nextWorkflow?.length === 1) {
          this.numOpzioni = '1 opzione';
        } else {
          this.numOpzioni = this.nextWorkflow?.length + ' opzioni';
        }
        //        this.getStoricoWorkflow(idIstanza);
      }
      , err =>{
        console.error(err);
      }
    );

  }

  getprevWorkflow(idIstanza: number) {
    this.moonfoblService.getPrevWorkflow(idIstanza).subscribe(
      prevWorkflow => {
        this.prevAction = prevWorkflow;
      }
      , err =>{
        console.error(err);
      }
    );

  }

  getStoricoWorkflow() {
    log('istanza-container::getStoricoWorkflow() storicoWorkflow=' + this.storicoWorkflow);
    if (!this.idIstanza) {
      return;
    }
    if (this.storicoWorkflow) {
      log('istanza-container::getStoricoWorkflow() FROM CACHE this.storicoWorkflow=' + this.storicoWorkflow);
      return this.storicoWorkflow;
    }
    this.moonfoblService.geStoricoWorkflow(this.idIstanza)
      .subscribe(storicoWorkflow => {
        log('istanza-container::getStoricoWorkflow() moonfoblService.getStoricoWorkflow() storicoWorkflow=' + storicoWorkflow);
        this.storicoWorkflow = storicoWorkflow;
        //
      });
  }

  getAllegati(): Allegato[] {
    log('istanza-container::getAllegati() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
    if (this.allegati) {
      log('istanza-container::getAllegati() FROM CACHE this.allegati=' + this.allegati);
      return this.allegati;
    }
    //
    this.moonfoblService.getAllegati(this.idIstanza)
      .subscribe(allegati => {
        log('istanza-container::getAllegati() moonfoblService.getAllegati() allegati=' + allegati);
        this.allegati = allegati;
        //
      });
  }

  // getDocumenti(): Documento[] {
  //   log('istanza-container::getDocumenti() idIstanza=' + this.idIstanza);
  //   if (!this.idIstanza) {
  //     return;
  //   }
  //   if (this.documenti) {
  //     log('istanza-container::getDocumenti() FROM CACHE this.allegati=' + this.documenti);
  //     return this.documenti;
  //   }
  //   //
  //   this.moonfoblService.getDocumenti(this.idIstanza)
  //     .subscribe(documenti => {
  //       log('istanza-container::getDocumenti() moonfoblService.getDocumenti() documenti=' + documenti);
  //       this.documenti = documenti;
  //       //
  //     });
  // }

  // getDocumentiProtocollati(): Documento[] {
  //   log('istanza-container::getDocumentiProtocollati() idIstanza=' + this.idIstanza);
  //   if (!this.idIstanza) {
  //     return;
  //   }
  //   if (this.documenti) {
  //     log('istanza-container::getDocumentiProtocollati() FROM CACHE this.documenti=' + this.documenti);
  //     return this.documenti;
  //   }
  //   //
  //   this.moonfoblService.getDocumentiProtocollati(this.idIstanza)
  //     .subscribe(documenti => {
  //       log('istanza-container::getDocumentiProtocollati() moonfoblService.getDocumentiProtocollati() documenti=' + documenti);
  //       this.documenti = documenti;
  //       //
  //     });
  // }

  getDocumentiEmessiDaUfficio(): Documento[] {
    log('istanza-container::getDocumentiEmessiDaUfficio() idIstanza=' + this.idIstanza);
    if (!this.idIstanza) {
      return;
    }
    if (this.documenti) {
      log('istanza-container::getDocumentiEmessiDaUfficio() FROM CACHE this.documenti=' + this.documenti);
      return this.documenti;
    }
    //
    this.moonfoblService.getDocumentiEmessiDaUfficio(this.idIstanza)
      .subscribe(documenti => {
        log('istanza-container::getDocumentiEmessiDaUfficio() moonfoblService.getDocumentiEmessiDaUfficio() documenti=' + documenti);
        this.documenti = documenti;
        //
      });
    }




  tornaIndietro() {
    let url = this.getUrlTornaIstanze();
    if (url) {
      window.location.href = url;
    } else {
      if ((this.sharedService.nav.active === NavSelection.CERCA_ISTANZA) || (this.sharedService.nav.active === NavSelection.NUOVA_ISTANZA)) {
        // reset nav
        this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'home/istanze');
        this.sharedService.isDirectRouterLink = true;
      }
      this.eventBus.cast('active-nav-bar:enable', this.sharedService.nav.active);
      this.router.navigate([this.sharedService.nav.route]);
    }
  }

  mostraDettaglioIstanza() {
    this.router.navigate(['/manage-form', 'VIEW', this.idIstanza]);
  }

  mostraDettaglioWorkflow(idStoricoWorkflow: number) {
    this.router.navigate(['/home/istanze/' + this.idIstanza + '/dettaglio-workflow/' + idStoricoWorkflow]);
  }

  modificaIstanza() {
    this.router.navigate(['/manage-form', 'UPDATE', this.idIstanza]);
  }

  downloadPdf() {

    this.moonfoblService.getPdf('' + this.idIstanza)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/pdf' });
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }
        */
        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = this.istanza.codiceIstanza + '.pdf';
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      });

    setTimeout(() => {

    }, 10000);

  }


  // downloadNotifica() {

  //   this.moonfoblService.getNotifica(this.idIstanza + '')
  //     .subscribe(x => {

  //       // It is necessary to create a new blob object with mime-type explicitly set
  //       // otherwise only Chrome works like it should
  //       const newBlob = new Blob([x], { type: 'application/pdf' });
  //       saveBlobIE(newBlob);
  //       // IE doesn't allow using a blob object directly as link href
  //       // instead it is necessary to use msSaveOrOpenBlob
  //       /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
  //         window.navigator.msSaveOrOpenBlob(newBlob);
  //         return;
  //       }
  //       */
  //       // For other browsers:
  //       // Create a link pointing to the ObjectURL containing the blob.
  //       const data = window.URL.createObjectURL(newBlob);

  //       const link = document.createElement('a');
  //       link.href = data;
  //       link.download = this.istanza.codiceIstanza + '.pdf';
  //       // this is necessary as link.click() does not work on the latest firefox
  //       link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

  //       setTimeout(function () {
  //         // For Firefox it is necessary to delay revoking the ObjectURL
  //         window.URL.revokeObjectURL(data);
  //         link.remove();
  //       }, 100);
  //     });

  //   setTimeout(() => {

  //   }, 10000);

  // }

  downloadAllegato(formioNameFile: string, contentType: string) {
    console.log('formioNameFile ' + formioNameFile);

    this.moonfoblService.getAllegato(formioNameFile)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: contentType });
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }
        */
        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);
        const link = document.createElement('a');
        link.href = data;
        link.download = this.normalizzaDaNomeFileFormio(formioNameFile);
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      });

    setTimeout(() => {

    }, 10000);
  }

  toggleDocumenti() {
    this.isDocumentiOpen = !this.isDocumentiOpen;
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

  togglePagamenti() {
    this.isPagamentiOpen = !this.isPagamentiOpen;
  }

  togglePagamentiTooltip(): string {
    return this.isPagamentiOpen ? 'Chiudi pagamenti' : 'Apri pagamenti';
  }

  downloadJson() {

    // var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.istanza.data));
    // let dataStr = "data:text/json;charset=utf-8," + this.istanza.data;
    let dataStr = 'data:text/json;charset=utf-8,' + this.getJsonDataToDownload();

    let codicIstanza = this.istanza.codiceIstanza;
    let downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute('href', dataStr);
    downloadAnchorNode.setAttribute('download', codicIstanza + '.json');
    document.body.appendChild(downloadAnchorNode); // required for firefox
    downloadAnchorNode.click();
    downloadAnchorNode.remove();

    setTimeout(() => {

    }, 100);
  }

  downloadRicevuta(idFile: number) {
    console.log('idFile ricevuta' + idFile);

    this.moonfoblService.getRicevuta(idFile)
      .subscribe(x => {

        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/pdf' });
        saveBlobIE(newBlob);
        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }
        */
        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = 'RicevutaAccettazione_' + this.istanza.codiceIstanza + '.pdf';
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      });

    setTimeout(() => {

    }, 10000);

  }

  getJsonDataToDownload() {
    let jsonData = JSON.parse(this.istanza.data);
    let data = null;
    if (jsonData.data.data) {
      data = jsonData.data.data;
      if (_.isEqual(data, jsonData.data.data)) {
        delete jsonData.data.data;
      }
      console.log('json = ' + jsonData);
    }
    return JSON.stringify(jsonData);
  }

  eseguiIntegrazioneIstanza() {
    this.router.navigate(['/home/richiesta-integrazione/' + this.idIstanza]);
    log(this.idIstanza);
  }

  preparaNotificaInvio(response: Object) {
    const codice = response['codice'];
    const descrizione = response['descrizione'];
    const titolo = response['titolo'];
    const istanza: Istanza = response['istanza'];
    const notifica = new Notifica(codice, descrizione, titolo, istanza);
    this.sharedService.notifica = notifica;
  }

  private normalizzaDaNomeFileFormio(formioNameFile: string) {
    let estensione = formioNameFile.substring(formioNameFile.lastIndexOf('.') + 1, formioNameFile.length);
    let name = formioNameFile.substring(0, formioNameFile.lastIndexOf('.') - 37);
    return name + '.' + estensione;
  }

  private manageNotifica(data: EpayNotificaMsg) {
    // console.log('Messaggio ricevuto: ' + JSON.stringify(data) );
    this.epayNotificaMsg = data;
    this.isInAttesaDiPagamento = false;
    this.isPagatoMaNonInviato = false;
    this.refresh();
  }

  getUrlTornaIstanze() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    let url = undefined;
    // if (user && user.embeddedNavigator) {
    //   if (user.embeddedNavigator.options && user.embeddedNavigator.options.urlTornaIstanze) {
    //     url = user.embeddedNavigator.options.urlTornaIstanze;
    //   }
    //   if (!url) {
    //     this.isUrlEmbeddedTornaIstanze = true;
    //   }
    // }
    this.breadcrumbBackTitle = "Le mie istanze";

    if (user) {
      if (user.embeddedNavigator) {
        if (user.embeddedNavigator.options && user.embeddedNavigator.options.urlTornaIstanze) {
          url = user.embeddedNavigator.options.urlTornaIstanze;
        }
        if (!url) {
          // if embedded and no url set: no showing back button
          this.isUrlEmbeddedTornaIstanze = true;
        }
      }
      if (user.consumerParams) {
        if (user.consumerParams.backUrl) {
          url = user.consumerParams.backUrl;
          this.breadcrumbBackTitle = "Torna indietro";
        }
      }
    }
    return url;
  }

}

function log(a: any) {
  console.log(a);
}
