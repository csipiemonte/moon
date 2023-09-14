/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, ElementRef, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormioComponent, FormioOptions} from '@formio/angular';
import {faHome} from '@fortawesome/free-solid-svg-icons';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import * as _ from 'lodash';
import {NgEventBus} from 'ng-event-bus';
import {interval} from 'rxjs';
import {map} from 'rxjs/operators';
import {AlertTypeDesc} from 'src/app/common/alert-type-desc';
import {Costanti, STATI, STORAGE_KEYS} from 'src/app/common/costanti';
import {User} from 'src/app/model/common/user';
import {Istanza} from 'src/app/model/dto/istanza';
import {Nav} from 'src/app/model/dto/nav';
import {Notifica} from 'src/app/model/dto/notifica';
import {DecodificaAzione} from 'src/app/model/util/decodifica/azione-decodifica';
import {CustomFileService} from 'src/app/plugin/custom-file.service';
import {StorageManager} from 'src/app/util/storage-manager';
import {environment} from '../../../../environments/environment';
import {InfoNavigation} from '../../../common/info-navigation';
import {Messaggi} from '../../../common/messaggi';
import {NavSelection} from '../../../common/nav-selection';
import {StatiForm} from '../../../common/stati-form';
import {FormIOOptions} from '../../../model/dto/formio-options';
import {Modulo} from '../../../model/dto/modulo';
import {ConfigService} from '../../../services/config.service';
import {MoonfoblService} from '../../../services/moonfobl.service';
import {SharedService} from '../../../services/shared.service';
import {ModalBasicComponent} from '../../modal-basic/modal-basic.component';
import {ModalConfirmComponent} from '../../modal-confirm/modal-confirm.component';
import {ConsumerParams} from '../../../model/common/consumer-params';
import {SecurityService} from 'src/app/services/security.service';

@Component({
  selector: 'app-manage-form',
  templateUrl: './manage-form.component.html',
  styleUrls: ['./manage-form.component.scss'],
})
export class ManageFormComponent implements OnInit, OnDestroy {
  public struttura: any = null;
  // public data: any = {};
  public data: any;
  public msgErr: string = null;
  public options: FormioOptions;
  public modulo: Modulo;
  public readOnly = false;
  public language = "it";
  // componente renderizzato da Formio
  formio: FormioComponent;
  // indica se i dati sono cambiati
  private frmChanged = false;
  private statoFrm: string;
  private idIstanza: number = null;
  private idModulo: number = null;
  private idVersioneModulo: number;
  private codiceModulo: string = null;
  private page: number;
  private inizializzaModulo = true;
  // indica se Modulo e' FRM o WIZ
  // private tipoStruttura = '';
  public tipoStruttura = "";
  // step corrente salvato da utente
  private currentStep: number;
  private codiceStato: number;
  private msgInfo: string;
  private formioUrl: string;
  private buttNext: HTMLInputElement;
  private from: string;
  private infoNavigation: InfoNavigation = new InfoNavigation();
  private timerSubscription;
  private savedData: string = null;
  private formIoOptions: FormIOOptions;


  titolo: string;
  descrizioneModulo: string;
  FileService: CustomFileService;
  faHome = faHome;

  formioBreadCrumbClickable: boolean = false;
  renderOptions: any;

  isUrlEmbeddedEsciModulo = false;
  isUrlEmbeddedTornaIstanze = false;
  breadcrumbBackTitle: string;

  state: any;
  isSummary: boolean = false;
  isReadOnly: boolean = false;
  moduloLoaded: boolean = false;
  isNotificaErrore: boolean = false;
  titoloNotificaErrore: string = "";
  messaggioNotificaErrore: string = "";


  constructor(
    private moonnservice: MoonfoblService,
    private config: ConfigService,
    private acRoute: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private sharedService: SharedService,
    private elementRef: ElementRef,
    private customFileService: CustomFileService,
    private eventBus: NgEventBus,
    private securityService: SecurityService
  ) {
    this.FileService = customFileService;
    this.formIoOptions = new FormIOOptions();
    this.state = this.router.getCurrentNavigation()?.extras.state;
  }

  get consumerParams(): ConsumerParams {

    let user: User = StorageManager.get(STORAGE_KEYS.USER);
    return user?.consumerParams;

  }

  setStrutturaByModulo(idModulo, idVersioneModulo, stato) {
    // fixme idModulo => this.idModulo
    this.moonnservice
      .getModulo(idModulo, idVersioneModulo)
      .subscribe((data) => {
        // leggo  se Wizard o FORM
        this.tipoStruttura = data['tipoStruttura'];

        console.log(data['struttura']);
        this.struttura = JSON.parse(data['struttura']);
        console.log('Struttura caricata: ' + this.struttura);
        // se devo inzializzare il modulo

        if (this.statoFrm === 'VIEW') {
          this.struttura.display = 'form';
          this.isReadOnly = true;
        }

        if (this.statoFrm === 'SUMMARY') {
          this.struttura.display = 'form';

          if (stato === STATI.DA_INVIARE) {
            this.isSummary = true;
            this.eventBus.cast('nav-bar:anchor:disable', true);
          }
          // reset nav
          this.sharedService.isDirectRouterLink = true;
        }

        this.codiceModulo = data['codiceModulo'];
        console.log('codice modulo: ' + this.codiceModulo);

        if (this.inizializzaModulo) {
          let datiInit = {};
          if (this.sharedService.datiContoTerzi['ct']) {
            //
            if (this.sharedService.datiContoTerzi.dati['cf']) {
              datiInit = {
                codiceFiscale:
                  this.sharedService.datiContoTerzi.dati['cf'].toUpperCase(),
              };
            }
          }
          // fixme idModulo => this.idModulo
          this.moonnservice
            .getDatiInizializzazioneModulo(idModulo, idVersioneModulo, datiInit)
            .subscribe(
              (response) => {
                if (response['data'].length > 0) {
                  console.log('responseDATA:' + response['data']);
                  this.data = JSON.parse(response['data']);
                }
              },
              (error) => {
                let msg = error?.message
                  ? error.message
                  : 'Errore in inizializzazione modulo';
                this.eventBus.cast('alert:set', {
                  text: msg,
                  type: AlertTypeDesc.ERROR,
                });
                // this.eventBus.cast('alert:set', { text: 'Errore in inizializzazione modulo', type: AlertTypeDesc.ERROR });
              }
            );
        }
      });
  }

  ngOnInit() {
    // setting attributo isUrlEmbeddedEsciModulo
    this.getUrlEsciModulo();

    this.renderOptions = {
      breadcrumbSettings: { clickable: false },
      language: 'it',
      fileService: this.FileService,
    };

    this.statoFrm = this.acRoute.snapshot.params.command;
    this.formioUrl =
      this.config.getBEServer() + "/moonfobl/restfacade/be/istanze/";
    this.infoNavigation.from = this.from;
    this.infoNavigation.page = this.page;
    this.options = this.formIoOptions;

    // this.caller = this.acRoute.snapshot.queryParams?.caller;
    // this.state = this.router.getCurrentNavigation()?.extras.state;

    if (this.statoFrm === "SUMMARY") {
      this.idIstanza = this.acRoute.snapshot.params.idIstanza;
      this.getIstanza(this.idIstanza);
      this.readOnly = true;
      return;
    }

    // Modifica istanza
    if (this.statoFrm === "UPDATE") {
      // this.eventBus.cast('nav-bar:hide', true);
      this.eventBus.cast("nav-bar:anchor:disable", true);

      this.idIstanza = this.acRoute.snapshot.params.idIstanza;
      this.getIstanza(this.idIstanza);
      return;
    }

    if (this.statoFrm === "VIEW") {
      this.idIstanza = this.acRoute.snapshot.params.idIstanza;
      this.getIstanza(this.idIstanza);
      this.readOnly = true;
      return;
    }

    if (this.statoFrm === "MODIFY") {
      this.idIstanza = this.acRoute.snapshot.params.idIstanza;
      this.getIstanza(this.idIstanza);
      this.readOnly = false;
      return;
    }

    // Nuova Istanza
    if (this.statoFrm === "NEW") {
      this.idModulo = this.acRoute.snapshot.params.idModulo;
      this.idVersioneModulo = this.acRoute.snapshot.params.idVersioneModulo;

      // this.moonnservice.getModulo(this.idModulo, this.idVersioneModulo).subscribe(data => {

      this.moonnservice
        .getModuloWithFields(
          this.idModulo,
          this.idVersioneModulo,
          "struttura,attributiWCL"
        )
        .subscribe((data) => {
          this.formioBreadCrumbClickable =
            this.moonnservice.getModuloAttributo(
              data as Modulo,
              "FORMIO_BREADCRUMB_CLICKABLE"
            ) === 'S'
              ? true
              : false;
          this.renderOptions = this.formioBreadCrumbClickable
            ? {
              breadcrumbSettings: { clickable: true },
              language: 'it',
              fileService: this.FileService,
            }
            : {
              breadcrumbSettings: { clickable: false },
              language: 'it',
              fileService: this.FileService,
            };

          this.tipoStruttura = data['tipoStruttura'];
          this.struttura = JSON.parse(data['struttura']);
          console.log(JSON.stringify(this.struttura));
          this.titolo = data['oggettoModulo'];
          this.moduloLoaded = true;
          this.descrizioneModulo = data['descrizioneModulo'];
          // Impostazione AUTO_SAVE
          this.setAuto_Save(data as Modulo);
          // se devo inzializzare il modulo
          if (this.inizializzaModulo) {
            let datiInit = {};
            if (this.sharedService.datiContoTerzi['ct']) {
              //
              const cf = this.sharedService.datiContoTerzi.dati['cf_pIva']
                ? this.sharedService.datiContoTerzi.dati[
                  'cf_pIva'
                  ].toUpperCase()
                : this.sharedService.datiContoTerzi.dati['cf_pIva'];
              if (cf) {
                datiInit = {
                  codiceFiscale: cf.toUpperCase(),
                  isConcessionario:
                    this.sharedService.datiContoTerzi.dati["isConcessionario"],
                };
              }
              // resetto dai shared altrimenti restano valorizzati nelle chiamate successive
              this.sharedService.datiContoTerzi.ct = false;
              this.sharedService.datiContoTerzi.dati = {};
            }
            this.moonnservice
              .getDatiInizializzazioneModulo(
                this.idModulo,
                this.idVersioneModulo,
                datiInit
              )
              .subscribe(
                (response) => {

                  this.eventBus.cast("nav-bar:anchor:disable", true);

                  if (
                    response['data'] &&
                    response['data'] !== null &&
                    response['data'].length > 0
                  ) {
                    console.log('responseDATA:' + response['data']);
                    this.data = JSON.parse(response['data']);
                  }
                },
                (error) => {

                  this.sharedService.isDirectRouterLink = true;

                  const message = error?.message ? error?.message : Messaggi.messaggioInit;
                  const title = error?.title ? error?.title : Messaggi.messaggioInitTitle;

                  this.isNotificaErrore = true;
                  this.messaggioNotificaErrore = message;
                  this.titoloNotificaErrore = title;

                }
              );
          }
          // non setto readOnly, rimane default true
        });
    }
  } // ngOninit

  /* Eventi gestione Form*/

  // submission del form
  onSubmit(submission: any) {
    let codiceStato: number;
    /*
    proprietà state contiene info su operazione
    draft: ho premiuto save
    submitted: ho premuto submitted
     */
    // this.data = submission['data'];

    this.data = _.cloneDeepWith(submission["data"]);
    if (submission["state"] === StatiForm.codiceStatoDRAFTFormio) {
      codiceStato = StatiForm.codiceStatoBOZZA;

      console.log(
        "SUBMISSION " + JSON.stringify(submission.data) + "**********"
      );
      if (this.checkSubmission(submission)) {
        this.sottomettiForm(
          { data: submission.data },
          this.idModulo,
          this.idVersioneModulo,
          this.idIstanza,
          this.codiceStato,
          this.currentStep
        );
      }
      window.scrollTo(0, 0);
    }
    if (submission["state"] === StatiForm.codiceStatoSUBMITTEDFormio) {
      codiceStato = StatiForm.codiceStatoCOMPLETATO;

      const mdRef = this.modalService.open(ModalBasicComponent);
      mdRef.componentInstance.modal_titolo = this.titolo;
      mdRef.componentInstance.modal_contenuto =
        Messaggi.messaggioConfermaCompleta;
      mdRef.result.then(
        (result) => {
          console.log("Closed with: ${result}" + result);

          if (result === "Conferma") {
            console.log(
              "SUBMISSION " + JSON.stringify(submission.data) + "**********"
            );

            if (this.checkSubmission(submission)) {
              this.sottomettiForm(
                { data: submission.data },
                this.idModulo,
                this.idVersioneModulo,
                this.idIstanza,
                this.codiceStato,
                this.currentStep
              );
            }
            window.scrollTo(0, 0);
          }
        },
        (reason) => {
          console.log(reason);
        }
      );
    }
  }

  // Inserire gestione errori su subscription
  sottomettiForm(
    data: any,
    idModulo: number,
    idVersioneModulo: number,
    idIstanza: number,
    codiceStato: number,
    currentStep: number
  ) {
    this.moonnservice
      .salvaIstanza(
        data,
        idModulo,
        idVersioneModulo,
        this.codiceModulo,
        idIstanza,
        codiceStato,
        currentStep
      )
      .subscribe((response) => {
        console.log(response);
        // aggiorno idIstanza per salvataggi successivi
        this.idIstanza = response["istanza"].idIstanza;
        // devo ricavare idIstanza creato e aggiornarlo
        this.frmChanged = false;
        console.log("Id Istanza generato: " + this.idIstanza);
        this.msgInfo = "Salvataggio effettuato correttamente";

        // Modifica Gestione Pagamenti
        // passo controllo al workflow
        // const url = '/home/istanza/' + this.idIstanza;
        const urlDettaglioIstanza = "home/istanza/" + this.idIstanza;
        const istanze = "/home/istanze/";
        console.log(" Url  => navigate: " + urlDettaglioIstanza);
        // torno alle mie istanze

        if (codiceStato === StatiForm.codiceStatoBOZZA) {
          const mdRef = this.modalService.open(ModalConfirmComponent);
          // mdRef.componentInstance.modal_titolo = "Conferma";
          mdRef.componentInstance.modal_titolo = `Salvataggio eseguito`;
          mdRef.componentInstance.modal_contenuto =
            `I dati sono stati salvati correttamente`;
          mdRef.componentInstance.exit = true;
          mdRef.componentInstance.exitMessage = `Esci dalla compilazione`;
          mdRef.componentInstance.exitCallback = this.backToListForm.bind(this);

          mdRef.result.then(
            (result) => {
              console.log("Closed with: ${result}" + result);
            },
            (reason) => {
              console.log(reason);
            }
          );
        } else {
          this.sharedService.nav = new Nav(
            NavSelection.NUOVA_ISTANZA,
            urlDettaglioIstanza
          );
          this.router.navigate([urlDettaglioIstanza], {
            state: { caller: NavSelection.NUOVA_ISTANZA },
          });
        }
      });
  }

  preparaNotificaInvio(response: Object) {
    const codice = response["codice"];
    const descrizione = response["descrizione"];
    const titolo = response["titolo"];
    const istanza: Istanza = response["istanza"];
    const notifica = new Notifica(codice, descrizione, titolo, istanza);
    this.sharedService.notifica = notifica;
  }

  checkSubmission(submission: object): boolean {
    try {
      if (submission["state"] === StatiForm.codiceStatoDRAFTFormio) {
        this.codiceStato = StatiForm.codiceStatoBOZZA;
      }
      if (submission["state"] === StatiForm.codiceStatoSUBMITTEDFormio) {
        this.codiceStato = StatiForm.codiceStatoCOMPLETATO;
      }
      if (
        Costanti.TIPO_STRUTTURA_WIZARD.localeCompare(this.tipoStruttura) === 0
      ) {
        this.currentStep = this.formio.formio.page;
      }
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }

  onChange(eventChanged: any) {
    // console.log("on change data = "+this.data);
    if (!this.data) {
      this.data = {};
    }

    try {
      if (eventChanged.isModified) {
        this.frmChanged = true;
      }
    } catch (err) {
      console.log(err);
    }
    const dom: HTMLElement = this.elementRef.nativeElement;
    if (
      dom.querySelector(".btn-wizard-nav-next") !== undefined &&
      dom.querySelector(".btn-wizard-nav-next") !== null
    ) {
      this.buttNext = <HTMLInputElement>(
        dom.querySelector(".btn-wizard-nav-next")
      );
      this.buttNext.disabled = false;
      if (
        this.buttNext.querySelector(
          ".fa.fa-refresh.fa-spin.button-icon-right"
        ) != null
      ) {
        (<HTMLInputElement>(
          this.buttNext.querySelector(
            ".fa.fa-refresh.fa-spin.button-icon-right"
          )
        )).className = "fa";
      }
    }
  }

  onNextPage(next: any) {
    window.scroll(0, 0);
    console.log("NEXT");
  }

  onPrevPage(prev: any) {
    window.scroll(0, 0);
    console.log("PREV");
  }

  onFormLoad(event: any) {
    this.frmChanged = false;
  }

  // Ritorna elenco dei form

  backToListForm() {
    let url = this.getUrlEsciModulo();
    // url undefined: NOT embedded / consumer behaviour
    // url defined: embedded / consumer behaviour
    if (url) {
      if (this.frmChanged && !this.readOnly) {
        const mdRef = this.modalService.open(ModalBasicComponent);
        mdRef.componentInstance.modal_titolo = "Conferma";
        mdRef.componentInstance.modal_contenuto = Messaggi.messaggioUscita;
        mdRef.result.then(
          (result) => {
            window.location.href = url;
          },
          (reason) => {
            console.log(reason);
          }
        );
      } else {
        window.location.href = url;
      }
    } else {
      const target = this.backToCaller();
      if (this.frmChanged && !this.readOnly) {
        const mdRef = this.modalService.open(ModalBasicComponent);
        mdRef.componentInstance.modal_titolo = "Conferma";
        mdRef.componentInstance.modal_contenuto = Messaggi.messaggioUscita;
        mdRef.result.then(
          (result) => {
            this.router.navigate([target]);
          },
          (reason) => {
            console.log(reason);
          }
        );
      } else {
        this.router.navigate([target]);
      } // form changed
    }
  }

  backToCaller() {
    this.sharedService.isDirectRouterLink = true;
    let target = "";
    switch (this.statoFrm) {
      case "SUMMARY":
        if (this.state.caller === NavSelection.NUOVA_ISTANZA) {
          target = "home/categorie";
        } else {
          target = "home/istanze";
        }
        break;
      case "NEW":
        target = "home/categorie";
        break;
      case "VIEW":
        target = "home/istanza/" + this.idIstanza;
        break;
      default:
        target = "home/istanze";
    }
    return target;
  }

  getUrlEsciModulo() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    // if url undefined not embedded / consumer behaviour
    let url = undefined;
    if (user) {
      if (user.embeddedNavigator) {
        if (
          user.embeddedNavigator.options &&
          user.embeddedNavigator.options.urlEsciModulo
        ) {
          url = user.embeddedNavigator.options.urlEsciModulo;
        }
        if (!url) {
          // if embedded and no url set: no showing back button
          this.isUrlEmbeddedEsciModulo = true;
        }
      }
      if (user.consumerParams) {
        if (user.consumerParams.backUrl) {
          url = user.consumerParams.backUrl;
        }
        if (!url) {
          // if embedded and no url set: no showing back button
          this.isUrlEmbeddedEsciModulo = true;
        }
      }
    }

    return url;
  }

  getUrlTornaIstanze() {
    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    let url = undefined;

    if (user) {
      if (user.embeddedNavigator) {
        if (
          user.embeddedNavigator.options &&
          user.embeddedNavigator.options.urlTornaIstanze
        ) {
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
        }
      }
    }

    return url;
  }

  toListAfterSubmit(codiceStato: number) {
    if (codiceStato === StatiForm.codiceStatoINVIATO) {
      this.sharedService.nav = new Nav(
        this.sharedService.nav.active,
        "home/notifica-invio-modulo"
      );
      this.router.navigate([this.sharedService.nav.route]);
    } else if (codiceStato === StatiForm.codiceStatoCOMPLETATO) {
      this.sharedService.nav = new Nav(
        this.sharedService.nav.active,
        "home/notifica-stato-completato"
      );
      this.router.navigate([this.sharedService.nav.route]);
    } else {
      const mdRef = this.modalService.open(ModalConfirmComponent);
      mdRef.componentInstance.modal_titolo = "Conferma";
      mdRef.componentInstance.modal_contenuto =
        "I dati sono stati salvati correttamente ";

      let url = this.getUrlTornaIstanze();

      if (url) {
        mdRef.result.then(
          (result) => {
            console.log("Closed with: ${result}" + result);
            if (codiceStato === StatiForm.codiceStatoINVIATO) {
              window.location.href = url;
            } else {
            }
          },
          (reason) => {
            console.log(reason);
          }
        );
      } else {
        mdRef.result.then(
          (result) => {
            console.log("Closed with: ${result}" + result);
            if (codiceStato === StatiForm.codiceStatoINVIATO) {
              this.router.navigate(["home"]);
            } else {
            }
          },
          (reason) => {
            console.log(reason);
          }
        );
      }
    }
  }

  goToDettaglioIstanza() {
    this.router.navigate(["/home/istanza/" + this.idIstanza]);
  }

  modificaIstanza() {
    this.router.navigate(["/manage-form", "UPDATE", this.idIstanza]);
    this.riportaInBozza(
      function () {
        this.isSummary = false;
        this.readOnly = false;
        this.ngOnInit();
      }.bind(this)
    );
  }

  onReady($form: FormioComponent) {
    this.frmChanged = false;
    this.formio = $form;
    this.frmChanged = false;
    // In caso di wizard imposto ultimo step selezionato da utente se non sono in SUMMARRY
    if (this.statoFrm !== "SUMMARY" && this.statoFrm !== "VIEW") {
      if (
        Costanti.TIPO_STRUTTURA_WIZARD.localeCompare(this.tipoStruttura) === 0
      ) {
        const wizard = this.formio.formio;
        if (!(this.currentStep == null)) {
          wizard.setPage(this.currentStep);
        }
      }
    }
  }

  riportaInBozza(callback: () => void) {
    this.moonnservice
      .doAzione(this.idIstanza, DecodificaAzione.RIPORTA_IN_BOZZA.id)
      .subscribe((istanzaSaveResponse) => {
        console.log("*** RIPORTA IN BOZZA " + istanzaSaveResponse);
        callback();
      });
  }

  private autoSave() {
    this.timerSubscription = interval(environment.autoSaveInterval)
      .pipe(
        map(() => {
          this.salvaTemp();
        })
      )
      .subscribe();
  }

  /*
   Procedura salvataggio temporaneo
   */

  private salvaTemp() {
    if (typeof this.formio !== undefined && typeof this.formio !== undefined) {
      if (!this.formio.formio.invalid) {
        // chiedere conferma del salva
        if (
          this.savedData !== JSON.stringify(this.formio.formio["_submission"])
        ) {
          // aggiorno i dati per confronto successivo
          this.savedData = JSON.stringify(this.formio.formio["_submission"]);

          this.moonnservice
            .salvaIstanza(
              this.formio.formio["_submission"],
              this.idModulo,
              this.idVersioneModulo,
              this.codiceModulo,
              this.idIstanza,
              StatiForm.codiceStatoBOZZA,
              this.currentStep,
              false
            )
            .subscribe(
              (response) => {
                const n = parseInt(response["istanza"].idIstanza, 10);
                this.idIstanza = n;

                this.frmChanged = false;
              },
              (errore) => {
                this.eventBus.cast("alert:set", {
                  text: "Errore in salvataggio automatico",
                  type: AlertTypeDesc.ERROR,
                });
              }
            );
        }
      }
    }
  }

  ngOnDestroy(): void {
    // show nav bar
    this.eventBus.cast('nav-bar:hide', false);
    this.eventBus.cast('nav-bar:anchor:disable', false);

    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
    }
  }

  getIstanza(idIstanza: number) {
    this.inizializzaModulo = false;
    this.moonnservice.getIstanza(idIstanza).subscribe((istanza) => {
      this.idModulo = istanza.modulo.idModulo;
      // branch versioning  - versione modulo
      this.idVersioneModulo = istanza.modulo.idVersioneModulo;

      this.data = JSON.parse(istanza.data);
      // inizializzo i dati per controllare salvataggio automatico
      this.savedData = JSON.stringify(this.data);

      this.titolo = istanza.modulo.oggettoModulo;
      this.moduloLoaded = true;
      this.descrizioneModulo = istanza.modulo.descrizioneModulo;
      this.setStrutturaByModulo(
        istanza.modulo.idModulo,
        istanza.modulo.idVersioneModulo,
        istanza.stato.idStato
      );
      // loggo lo step salvato in istanza
      this.currentStep = istanza['currentStep'];
      // Impostazione AUTO_SAVE
      this.setAuto_Save(istanza.modulo);

      if (istanza.stato.idStato >= 2) {
        this.readOnly = true;
        // se readOnlu non setto change
        this.frmChanged = false;
      }
    });
  }

  private getAuto_Save(attr: string): boolean {
    if (attr) {
      return attr === 'S';
    } else {
      return false;
    }
  }

  private setAuto_Save(m: Modulo) {
    const flag_auto_save = this.moonnservice.getModuloAttributo(
      m,
      'ISTANZA_AUTO_SAVE'
    );
    if (flag_auto_save && flag_auto_save === 'S') {
      this.autoSave();
    }
  }

}
