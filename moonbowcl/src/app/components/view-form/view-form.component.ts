/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, Params, Router, UrlSegment } from '@angular/router';
import { Location } from '@angular/common';
import { Modulo } from '../../model/dto/modulo';
import { MoonboblService } from '../../services/moonbobl.service';
import { ConfigService } from '../../config.service';
import * as common from '../../model/common';
import { SecurityService } from '../../security.service';
import { FormioOptions } from '@formio/angular';
import { Messaggi } from '../../common/messaggi';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalBasicComponent } from '../modal/modal-basic/modal-basic.component';
import { StatiForm } from '../../common/stati-form';
import { Istanza } from 'src/app/model/dto/istanza';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { FormioComponent } from '@formio/angular/public-api';
import { SharedService } from 'src/app/services/shared.service';
import * as _ from 'lodash';
import { NgxSpinnerService } from 'ngx-spinner';
import { Notifica } from 'src/app/model/dto/notifica';
import { Nav } from 'src/app/model/dto/nav';
import { Caller } from 'src/app/common/caller';
import { CustomFileService } from 'src/app/plugin/custom-file.service';
import { Identita } from 'src/app/common/identita';

@Component({
  selector: 'app-view-form',
  templateUrl: './view-form.component.html',
  styleUrls: ['./view-form.component.css']
})

export class ViewFormComponent implements OnInit {
  public struttura: any = null;
  public initData: any = null;
  // public data: any = null;
  public data: any;
  public msgErr: string = null;
  public options: FormioOptions;
  public modulo: Modulo;
  public oggettoModulo;
  public readOnly = false;
  public istanzaSelected: Istanza;

  private alertMessage: string = Messaggi.msgModified;
  // indica se i dati sono cambiati
  private frmChanged = false;
  private statoFrm: string;
  private idIstanza: number = null;
  private idModulo: number = null;
  private idVersioneModulo: number = null;
  private page: number;
  private inizializzaModulo = true;
  faHome = faHome;
  titolo: string;
  descrizioneModulo: string;
  private currentStep: number;
  private codiceStato: number;
  loadingForm = false;

  FileService: CustomFileService;
  renderOptions: any;

  constructor(
    private securityService: SecurityService,
    private _location: Location,
    private moonnservice: MoonboblService,
    private config: ConfigService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private sharedService: SharedService,
    private spinnerService: NgxSpinnerService,
    private customFileService: CustomFileService
  ) {
    this.FileService = customFileService;
  }

  currentUser: common.UserInfo = null;
  public form: FormioComponent;

  ngAfterViewChecked() {

    if (this.istanzaSelected && this.istanzaSelected.stato && this.istanzaSelected.stato.idStato >= 2) {
      let buttonSalvaInBozza = Array.from(document.querySelectorAll('button')).find(el => el.innerText.toLowerCase().includes("salva"));
      if (buttonSalvaInBozza && !buttonSalvaInBozza.classList.contains('btn-wizard-nav-submit')) { buttonSalvaInBozza.style.display = "none" }
    }
    if (!(this.statoFrm === 'NEW' || this.statoFrm === 'BOZZA_DA_INVIARE') && document.getElementsByClassName('btn-wizard-nav-submit') && document.getElementsByClassName('btn-wizard-nav-submit')[0]) {
      if (this.istanzaSelected && this.istanzaSelected.stato && this.istanzaSelected.stato.idStato === 1) {
        document.getElementsByClassName('btn-wizard-nav-submit')[0].innerHTML = 'Salva istanza';
      }
      else {
        document.getElementsByClassName('btn-wizard-nav-submit')[0].innerHTML = 'Salva modifiche istanza';
      }
    }
    else if (document.getElementsByClassName('btn-wizard-nav-submit') && document.getElementsByClassName('btn-wizard-nav-submit')[0]) {
      document.getElementsByClassName('btn-wizard-nav-submit')[0].innerHTML = 'Salva e prosegui';
    }

    // document.getElementsByClassName('alert-success')[0].innerHTML = 'OK';
  }

  ngOnInit() {

    this.currentUser = this.sharedService.UserLogged;

    this.renderOptions = {
      breadcrumbSettings: { clickable: true },
      language: "it",
      fileService: this.FileService,
    };

    if (this.route.snapshot.url[0].path === 'istanze') {
      this.statoFrm = 'VIEW';
    }

    if (this.route.snapshot.url[0].path === 'istanzebozza') {
      this.statoFrm = 'EDIT';
    }

    if (this.route.snapshot.url[0].path === 'modificaistanza') {
      this.statoFrm = 'EDIT_POST_INVIO';
    }

    if (this.route.snapshot.url[0].path === 'istanzebozza-da-inviare') {
      this.statoFrm = 'BOZZA_DA_INVIARE';
    }

    if (this.route.snapshot.url[0].path === 'nuova-istanza') {
      this.statoFrm = 'NEW';
    }



    /*
    Controllo parametro opzionale page in queryParam
     */
    this.route.queryParams.subscribe(
      (params) => {
        this.page = +params['page'];
        console.log('Pagina corrente: ' + this.page);
      }
    );

    this.route.paramMap.subscribe(
      (params) => {
        if (!(params.get('id') || params.get('idModulo'))) {
          console.log('id non definito');
          return;
        }

        this.options = {
          alerts: { 'submitMessage': this.alertMessage },
          errors: { 'message': Messaggi.msgErrForm },
          i18n: {
            language: 'it',
            it: {
              complete: 'Invio completato',
              error: 'Si prega di correggere gli errori prima di inviare.',
              required: '{{field}} è obbligatorio',
              unique: '{{field}} deve essere unico',
              array: '{{field}} deve essere un elenco',
              array_nonempty: '{{field}} deve essere un elenco non vuoto',
              // eslint-disable-line camelcase
              nonarray: '{{field}} non deve essere un elenco',
              select: '{{field}} contiene una selezione non valida',
              pattern: '{{field}} non corrisponde al formato previsto {{pattern}}',
              minLength: '{{field}} deve avere almeno {{length}} caratteri.',
              maxLength: '{{field}} non deve avere più di {{length}} caratteri.',
              minWords: '{{field}} deve avere almeno {{length}} parole.',
              maxWords: '{{field}} non deve avere più di {{length}} parole.',
              min: '{{field}} non può essere minore di {{min}}.',
              max: '{{field}} non può essere maggiore di {{max}}.',
              maxDate: '{{field}} non può essere posteriore alla data {{- maxDate}}',
              minDate: '{{field}} non può essere antecedente alla data {{- minDate}}',
              maxYear: '{{field}} non può essere posteriore all\'anno {{maxYear}}',
              minYear: '{{field}} non può essere antecedente all\'anno {{minYear}}',
              invalid_email: '{{field}} deve essere una email valida.',
              // eslint-disable-line camelcase
              invalid_url: '{{field}} deve essere un url valido.',
              // eslint-disable-line camelcase
              invalid_regex: '{{field}} non corrisponde al formato previsto {{regex}}.',
              // eslint-disable-line camelcase
              invalid_date: '{{field}} non è una data valida.',
              // eslint-disable-line camelcase
              invalid_day: '{{field}} non è un giorno valido.',
              // eslint-disable-line camelcase
              mask: '{{field}} non corrisponde al formato previsto.',
              stripe: '{{stripe}}',
              month: 'Mese',
              day: 'Giorno',
              year: 'Anno',
              january: 'gennaio',
              february: 'febbraio',
              march: 'marzo',
              april: 'aprile',
              may: 'maggio',
              june: 'giugno',
              july: 'luglio',
              august: 'agosto',
              september: 'settembre',
              october: 'ottobre',
              november: 'novembre',
              december: 'dicembre',
              next: 'Successivo',
              previous: 'Precedente',
              cancel: 'Cancella',
              submit: 'Invia Form',
              'Drop files to attach,': 'Sposta qui un file da allegare',
              'browse': 'selezionalo',
              'or': 'o',
              'File Name': 'nome file',
              'Size': 'dimensione'
            }
          }
        };

        if (this.statoFrm === 'VIEW') {
          this.idIstanza = +params.get('id');
          this.getIstanza(this.idIstanza, 'VIEW');
          return;
        }
        if (this.statoFrm === 'EDIT') {
          this.idIstanza = +params.get('id');
          this.getIstanzaBozza(this.idIstanza);
          return;
        }
        if (this.statoFrm === 'EDIT_POST_INVIO') {
          this.idIstanza = +params.get('id');
          this.getIstanza(this.idIstanza, 'EDIT');
          return;
        }
        if (this.statoFrm === 'BOZZA_DA_INVIARE') {
          this.idIstanza = +params.get('id');
          this.getIstanzaBozza(this.idIstanza);
          return;
        }


        if (this.statoFrm === 'NEW') {
          // this.idModulo = this.acRoute.snapshot.params.idModulo;
          // this.idVersioneModulo = this.acRoute.snapshot.params.idVersioneModulo;
          this.idModulo = +params.get('idModulo');
          this.idVersioneModulo = +params.get('idVersioneModulo');

          this.setStrutturaByModulo(this.idModulo, this.idVersioneModulo);

        }
      });
  }

  setStrutturaByModulo(idModulo, idVersioneModulo) {

    this.loadingForm = true;

    //this.spinnerService.show();
    this.moonnservice.getModuloWithFields(this.idModulo, idVersioneModulo, 'strutturaBO').subscribe(data => {

      //console.log(data['struttura']);
      this.struttura = JSON.parse(data['struttura']);
      console.log('Struttura caricata: ' + JSON.stringify(this.struttura));
      // se devo inzializzare il modulo

      this.titolo = data['oggettoModulo'];
      this.descrizioneModulo = data['descrizioneModulo'];

      if (this.inizializzaModulo) {
        let datiInit = {};

        if (this.sharedService.datiCompilaOperatore.dati['cf_pIva']) {
          datiInit = {
            codiceFiscale: this.sharedService.datiCompilaOperatore.dati['cf_pIva'],
            nome: this.sharedService.datiCompilaOperatore.dati['nome'],
            cognome: this.sharedService.datiCompilaOperatore.dati['cognome']
          };
        }
   
        this.spinnerService.show();
        this.moonnservice.getDatiInizializzazioneModulo(idModulo, idVersioneModulo, datiInit).subscribe(
          response => {
            if (response['data'].length > 0) {
              console.log('responseDATA:' + response['data']);
              this.data = JSON.parse(response['data']);
              this.spinnerService.hide();
              this.loadingForm = false;
            }
          },
          errore => {
            this.spinnerService.hide();
            this.loadingForm = false;
            if (errore.error) {
              const notifica = new Notifica(errore.error.code, errore.error.msg, errore.error.title, null);
              this.securityService.goToNotificationError(notifica);
            } else {
              const notifica = new Notifica(errore.status, errore.statusText, 'ERRORE SISTEMA', null);
              this.securityService.goToNotificationError(notifica);
            }
          }
        );
      }

      // })
      // .catch(errore => {
      //   console.log('***' + errore);
      //   this.msgErr = errore;
      // });
    },
      errore => {
        console.log(JSON.stringify(errore));
        //this.spinnerService.hide();
        //this.loadingForm = false;
        if (errore.error) {
          const notifica = new Notifica(errore.error.code, errore.error.msg, errore.error.title, null);
          this.securityService.goToNotificationError(notifica);
        } else {
          const notifica = new Notifica(errore.status, errore.statusText, 'ERRORE SISTEMA', null);
          this.securityService.goToNotificationError(notifica);
        }
      });
  }

  getIstanza(idIstanza: number, typeAction: string) {
    this.inizializzaModulo = false;
    this.moonnservice.getIstanza(idIstanza)
      .then(istanza => {
        this.idModulo = istanza.modulo.idModulo;
        this.idVersioneModulo = istanza.modulo.idVersioneModulo;
        this.oggettoModulo = istanza.modulo.oggettoModulo;

        //console.log(istanza.data);

        this.data = JSON.parse(istanza.data);
        console.log(this.data);

        this.setStrutturaByModulo(istanza.modulo.idModulo, this.idVersioneModulo);
        // modulo inviato

        if (typeAction === 'VIEW' && istanza.stato.idStato >= 2) {
          this.readOnly = true;
          // se readOnlu non setto change
          this.frmChanged = false;
        }
        this.istanzaSelected = istanza;
      }
      )
      .catch(errore => {
        console.log('***' + errore);
        this.msgErr = errore;
      });
  }

  getIstanzaBozza(idIstanza: number) {
    this.inizializzaModulo = false;
    this.moonnservice.getIstanzaBozza(idIstanza)
      .then(istanza => {
        this.idModulo = istanza.modulo.idModulo;
        this.idVersioneModulo = istanza.modulo.idVersioneModulo;
        this.oggettoModulo = istanza.modulo.oggettoModulo;
        console.log(istanza.data);
        this.data = JSON.parse(istanza.data);
        console.log(this.data);
        this.setStrutturaByModulo(istanza.modulo.idModulo, this.idVersioneModulo);
        // modulo inviato
        //if (istanza.stato.idStato >= 2) {
        if (istanza.stato.idStato != 1 && istanza.stato.idStato != 10) {
          this.readOnly = true;
          // se readOnlu non setto change
          this.frmChanged = false;
        }
        this.istanzaSelected = istanza;
      }
      )
      .catch(errore => {
        console.log('***' + errore);
        this.msgErr = errore;
      });
  }

  /* Eventi gestione Form*/

  // submission del form
  onSubmit(submission: any) {
    //non usato : implementazione hooks beforeSubmit con getsione errore
    console.log('submitted');
  }

  // Inserire gestione errori su subscription
  sottomettiForm(submission: any, idModulo: number, idVersioneModulo: number, idIstanza: number, codiceStato: number, callback: Function) {
    // if (!(JSON.stringify(this.initData) === JSON.stringify(this.data))){
    //if (!(_.isEqual(this.initData, this.data))) {

    if (!(_.isEqualWith(this.initData, this.data, (value1, value2, key) => {
      return String(key).startsWith('salva') ? true : undefined
    }))) {

      this.loadingForm = false;
      this.spinnerService.show();



      if (this.statoFrm == 'NEW' || this.statoFrm == 'BOZZA_DA_INVIARE') {
        //this.moonnservice.salvaCompilaIstanza({ 'data': submission.data }, idModulo, idVersioneModulo, idIstanza, codiceStato).subscribe(

        const payload = {
          'idIstanza': idIstanza,
          'stato': { 'idStato': codiceStato },
          'data': JSON.stringify({ 'data': submission.data }),
          'modulo': { 'idModulo': idModulo, 'idVersioneModulo': idVersioneModulo }
        };

        if(this.sharedService?.datiCompilaOperatore?.dati && this.sharedService?.datiCompilaOperatore?.dati['no_conto_terzi']){
          payload['attoreIns'] = Identita.NO_CONTO_TERZI;
         }

        this.moonnservice.salvaCompila(payload).subscribe(  
          response => {
            console.log(response);
            //reset initData
            this.initData = _.cloneDeepWith(this.data.data);

            this.idIstanza = response['istanza'].idIstanza;
            this.frmChanged = false;
            console.log('Id Istanza generato: ' + this.idIstanza);
            this.spinnerService.hide();

            this.preparaNotificaInvio(response);

            let caller = (this.statoFrm === StatiForm.nomeStatoNEW) ? Caller.COMPILA : Caller.DA_INVIARE;

            if (this.codiceStato === StatiForm.codiceStatoINVIATO) {
              this.sharedService.nav = new Nav(this.sharedService.nav.active, 'notifica-invio-modulo');
              this.router.navigate([this.sharedService.nav.route + '/' + caller]);
            } else if (this.codiceStato === StatiForm.codiceStatoCOMPLETATO) {
              this.sharedService.nav = new Nav(this.sharedService.nav.active, 'notifica-stato-completato');
              this.router.navigate([this.sharedService.nav.route + '/' + caller]);
            } else {
              callback(null, submission);
            }
          },
          errore => {
            console.log(JSON.stringify(errore));
            this.spinnerService.hide();
            callback({
              message: "Non è possibile salvare il modulo",
              component: null
            }, null)
          }
        );
      }
      else if (this.statoFrm == 'EDIT_POST_INVIO') {
        // NOTIFICA MODIFICA
        this.router.navigate(['istanze/notifica-modifica/' + idIstanza, { data: JSON.stringify(submission.data), idModulo, idVersioneModulo, idIstanza, codiceStato }]);
      }
      else {
        this.moonnservice.salvaIstanza({ 'data': submission.data }, idModulo, idVersioneModulo, idIstanza, codiceStato).subscribe(
          response => {
            console.log(response);
            //reset initData
            this.initData = _.cloneDeepWith(this.data.data);
            // aggiorno idIstanza per salvataggi successivi
            this.idIstanza = response['idIstanza'];
            // devo ricavare idIstanza creato e aggiornarlo

            this.frmChanged = false;
            console.log('Id Istanza generato: ' + this.idIstanza);

            this.spinnerService.hide();
            callback(null, submission);
          },
          errore => {
            console.log(JSON.stringify(errore));
            this.spinnerService.hide();
            callback({
              message: "Non è possibile salvare il modulo",
              component: null
            }, null)
          }
        );

      }
    }
    else {
      callback({
        message: "Dati non modificati",
        component: null
      }, null)

    }
  }

  onChange(eventChanged: any) {

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

  }

  onNextPage(next: any) {
    console.log('NEXT');
  }

  onPrevPage(prev: any) {
    console.log('PREV');
  }

  onFormLoad(event: any) {
    this.frmChanged = false;

    console.log(event);
    if (event['data'] === undefined) {

    }
  }

  onReady(form: FormioComponent) {

    this.setInitData();

    // binding this
    form.options.hooks.beforeSubmit = this.handleBeforeSubmit.bind(this);
  }

  setInitData() {

    if (this.data) {
      this.initData = _.cloneDeepWith(this.data.data);
      let data = null;
      if (this.initData && this.initData.data) {
        data = this.initData.data;
        if (_.isEqual(data, this.initData.data)) {
          delete this.initData.data;
        }
      }
    }


  }

  handleBeforeSubmit(submission, callback) {
    let codiceStato: number;

    this.data = submission['data'];
    if (submission['state'] === StatiForm.codiceStatoDRAFTFormio) {
      codiceStato = StatiForm.codiceStatoBOZZA;
    }

    if (submission['state'] === StatiForm.codiceStatoSUBMITTEDFormio) {
      console.log('inizio verifica utente');

      console.log('current user Codice fiscale' + this.currentUser.codFisc);
      console.log('current user nome' + this.currentUser.nome);
      console.log('current user cognonome' + this.currentUser.cognome);

      console.log('fine verifica utente');
      codiceStato = StatiForm.codiceStatoINVIATO;
    }

    console.log('SUBMISSION ' + JSON.stringify(submission.data) + '**********');
    if (this.checkSubmission(submission)) {
      this.sottomettiForm({ 'data': submission.data }, this.idModulo, this.idVersioneModulo, this.idIstanza, this.codiceStato, callback);
    }


  }


  // Ritorna elenco dei form
  backToListForm() {
    if (this.frmChanged && !this.readOnly) {
      const mdRef = this.modalService.open(ModalBasicComponent);
      mdRef.componentInstance.modal_titolo = 'Moon';
      mdRef.componentInstance.modal_contenuto = 'Sicuro di uscire ? ';
      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);

        if (this.statoFrm === 'NEW') {
          this.router.navigate(['nuova-istanza']);
        }
        if (this.statoFrm === 'VIEW' || this.statoFrm === 'EDIT') {
          this.router.navigate(['istanze'], { queryParams: { page: this.page } });
        }
      }, (reason) => {
        console.log(reason);
      });
    } else {
      if (this.statoFrm === 'NEW') {
        this.router.navigate(['nuova-istanza']);
      }
      if (this.statoFrm === 'VIEW' || this.statoFrm === 'EDIT') {
        this.router.navigate(['istanze'], { queryParams: { page: this.page } });
      }
    }// form chaged
  }

  back() {
    this.spinnerService.show();
    if (this.statoFrm === 'NEW') {
      this.router.navigate(['categorie']);
    } else {
      this._location.back();
    }
  }

  preparaNotificaInvio(response: Object) {
    const codice = response['codice'];
    const descrizione = response['descrizione'];
    const titolo = response['titolo'];
    const istanza: Istanza = response['istanza'];
    const notifica = new Notifica(codice, descrizione, titolo, istanza);
    this.sharedService.notifica = notifica;
  }

  checkSubmission(submission: object): boolean {
    try {
      if (submission['state'] === StatiForm.codiceStatoDRAFTFormio) {
        this.codiceStato = StatiForm.codiceStatoBOZZA;
      }
      if (submission['state'] === StatiForm.codiceStatoSUBMITTEDFormio) {
        if ((this.statoFrm === 'NEW') || (this.statoFrm === 'BOZZA_DA_INVIARE')) {
          this.codiceStato = StatiForm.codiceStatoCOMPLETATO;
        }
        else {
          this.codiceStato = StatiForm.codiceStatoINVIATO;
        }
      }
      // if (Costanti.TIPO_STRUTTURA_WIZARD.localeCompare(this.tipoStruttura) === 0) {
      //   this.currentStep = this.formio.formio.page;
      // }
      return true;
    } catch (error) {
      console.log(error);
      return false;
    }
  }

}
