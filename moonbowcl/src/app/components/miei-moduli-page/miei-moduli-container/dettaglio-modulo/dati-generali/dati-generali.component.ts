/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { map } from 'rxjs';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome, faSync, faTrashAlt, faEye, faSearch, faSearchPlus } from '@fortawesome/free-solid-svg-icons';
import { Categoria } from 'src/app/model/dto/categoria';
import { Processo } from 'src/app/model/dto/processo';
import { PortaliIf } from 'src/app/model/dto/portali-if';
import { TipoCodiceIstanza } from 'src/app/model/dto/tipo-codice-istanza';
import { VersioneStato } from 'src/app/model/dto/versione-stato';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalActionComponent } from 'src/app/components/modal/modal-action/modal-action.component';
import { Utente } from 'src/app/model/dto/utente';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgDatiGenerali } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-dati-generali',
  templateUrl: './dati-generali.component.html',
  styleUrls: ['./dati-generali.component.scss']
})
export class DatiGeneraliComponent implements OnInit {
  [x: string]: any;

  @Input() moduloSelezionato: Modulo;
  @Input() elencoCategorie: Categoria[];
  @Input() elencoProcessi: Processo[];
  @Input() elencoPortali: PortaliIf[];
  @Input() elencoTipoCodiceIstanza: TipoCodiceIstanza[];
  @Output() eventCambiaStato = new EventEmitter<Modulo>();
  @Output() eventModificaModulo = new EventEmitter<Modulo>();
  @Output() eventEliminaModulo = new EventEmitter<Modulo>();
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  moduloEdit: Modulo = new Modulo();
  // Modulo passato al componente cambia stato
  moduloInModifica: Modulo;
  moduloEditIdPortali: number[];
  portaliChanged = false;
  tipoCodiceIstanza = new TipoCodiceIstanza();

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faSync = faSync;
  faTrashAlt = faTrashAlt;
  faEye = faEye;
  faSearch = faSearch;
  faSearchPlus = faSearchPlus;
  modeError = false;

  formTypes = [
    { tipo: 'FRM', descrizione: 'FORM' },
    { tipo: 'WIZ', descrizione: 'WIZARD' }];

  mapOperatori: Map<string, Utente> = new Map<string, Utente>();

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

  currTemplate: string;
  currentUser: any;

  thumbnailProcesso: any;

  titleValutazioni = 'Valutazioni del Modulo';
  valutazioni: any;
  sommaValutazioni = 0;
  colorsValutazione = ['lightgray','green','orange','red'];

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private modalService: NgbModal,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.currentUser = this.sharedService.UserLogged;
  }

  ngOnInit(): void {
    this.alert.emit({ clear: true });
    this.init();
    this.currTemplate = 'datiGenerali';
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
      this.moduloSelezionato.idVersioneModulo,
      'struttra,portali,processo,versioniCurrentStato').subscribe((resModulo: Modulo) => {
        // Init degli attributi per viewMode
        this.modulo = resModulo;
        this.popolaMapOperatori();
        // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
        this.aggiornaModuloEditFromModulo();
        // tslint:disable-next-line:max-line-length
        if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
          this.isModificabile = true;
        }

        this.tipoCodiceIstanza = this.elencoTipoCodiceIstanza
          .find(t => t.idTipoCodiceIstanza === this.modulo.idTipoCodiceIstanza);
        this.initValutazioni();
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

  private aggiornaModuloEditFromModulo() {
    this.moduloEdit.idModulo = this.modulo.idModulo;
    this.moduloEdit.codiceModulo = this.modulo.codiceModulo;
    this.moduloEdit.oggettoModulo = this.modulo.oggettoModulo;
    this.moduloEdit.descrizioneModulo = this.modulo.descrizioneModulo;
    this.moduloEdit.idTipoCodiceIstanza = this.modulo.idTipoCodiceIstanza;
    this.moduloEdit.tipoStruttura = this.modulo.tipoStruttura;
    this.moduloEdit.categoria.idCategoria = this.modulo.categoria.idCategoria;
    this.moduloEdit.processo.idProcesso = this.modulo.processo.idProcesso;
    //        this.moduloEdit.portali = this.modulo.portali.map((p) => p); // uso map() per fare il clone()
    this.moduloEditIdPortali = this.modulo.portali.map((p) => p.idPortale);
  }

  popolaMapOperatori() {
    const attori = this.modulo.versioni.map(v => v.attoreUpd);
    const distinctAttori = attori.filter((n, i) => attori.indexOf(n) === i); // [...new Set(attori)];
    log('dati-generali::popolaMapOperatori() distinctAttori=' + distinctAttori);
    distinctAttori.forEach(attore => {
      log('dati-generali::popolaMapOperatori() LOOP attore = ' + attore);
      if (attore !== 'ADMIN') {
        this.moonboblService.getUtenteByIdentificativo(attore).subscribe(
          (resOperatore: Utente) => {
            this.mapOperatori.set(attore, resOperatore);
          },
          (err: MoonboError) => {
            log('dati-generali::popolaMapOperatori() distinctAttori=' + err.errorMsg);
          }
        );
      }
    });
    log('dati-generali::popolaMapOperatori() mapOperatori=' + this.mapOperatori);
  }

  salva() {
    this.spinnerService.show();
    const oggettoDescrizioneModificato = this.modulo.oggettoModulo !== this.moduloEdit.oggettoModulo ||
      this.modulo.descrizioneModulo !== this.moduloEdit.descrizioneModulo;
    this.aggiornaModuloFromModuloEdit();
    this.moonboblService.aggiornaModulo(this.modulo).subscribe(
      () => {
        this.salvaPortaliIfChanged();
        this.spinnerService.hide();
        // this.alertService.success('Salvataggio dati generali effettuato con successo !', this.alertOptions);
        this.alert.emit({ text: MsgDatiGenerali.SUCCESS_DATI_GENERALI, type: 'success', autoclose: true });
        this.init();
        if (oggettoDescrizioneModificato) {
          // Per aggiornare la lista dei moduli, nel caso di ritorno alla lista dei moduli
          this.sharedService.mieiModuli = this.sharedService.mieiModuli.filter(el => el.idModulo !== this.modulo.idModulo);
          this.sharedService.mieiModuli.push(this.modulo);
        }
      },
      (err: MoonboError) => {
        this.spinnerService.hide();
        // this.alertService.error('Impossibile effettuare il salvataggio dati generali !' + err.errorMsg, this.alertOptions);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false });

      }
    );
  }

  private aggiornaModuloFromModuloEdit() {
    this.modulo.oggettoModulo = this.moduloEdit.oggettoModulo;
    this.modulo.descrizioneModulo = this.moduloEdit.descrizioneModulo;
    this.modulo.idTipoCodiceIstanza = this.moduloEdit.idTipoCodiceIstanza;
    this.modulo.tipoStruttura = this.moduloEdit.tipoStruttura;
    this.modulo.categoria = new Categoria();
    this.modulo.categoria.idCategoria = this.moduloEdit.categoria.idCategoria;
    // i portali non verra salvato qui, ma le mettiamo per bellezza
    this.portaliChanged = JSON.stringify(this.modulo.portali?.map((p) => p.idPortale)) !== JSON.stringify(this.moduloEditIdPortali);
    this.modulo.portali = this.elencoPortali.filter(p => this.moduloEditIdPortali.includes(p.idPortale));
    this.modulo.processo = new Processo();
    this.modulo.processo.idProcesso = this.moduloEdit.processo.idProcesso;
    this.modulo.struttura = this.getStrutturaAggiornata();
  }

  get moduloStrutturaDisplay(): string {
    return this.moduloEdit.tipoStruttura === 'WIZ' ? 'wizard' : 'form';
  }

  private getStrutturaAggiornata() {
    const display = this.moduloStrutturaDisplay;
    const jsonObj = JSON.parse(this.modulo.struttura);
    jsonObj.display = display;
    return JSON.stringify(jsonObj);
  }

  private salvaPortaliIfChanged() {
    if (!this.portaliChanged) {
      log('salvaPortali() unchanged.');
      return;
    }
    this.moonboblService.salvaPortali(this.modulo, this.moduloEditIdPortali).subscribe(
      () => {
        log('moonboblService.salvaPortali OK. ' + this.moduloEditIdPortali);
      },
      (err: MoonboError) => {
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
        this.modeError = true;
      }
    );
  }

  cambiaStato(versione: VersioneStato) {
    log('dati-generali::cambiaStato() versione Selected: ' + JSON.stringify(versione));
    log('dati-generali::cambiaStato() VersioneStato: ' + versione.idModulo + '/v/' + versione.idVersioneModulo);
    this.moduloInModifica = { ...this.modulo };
    this.moduloInModifica.idModulo = versione.idModulo;
    this.moduloInModifica.idVersioneModulo = versione.idVersioneModulo;
    this.moduloInModifica.versioneModulo = versione.versioneModulo;
    this.moduloInModifica.stato.codice = versione.codice;
    this.moduloInModifica.stato.descrizione = versione.descrizione;
    this.currTemplate = 'cambiaStato';
  }

  editFormIo(versione: VersioneStato) {
    log('dati-generali::editFormIo() VersioneStato: ' + versione.idModulo + '/v/' + versione.idVersioneModulo);
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
            this.gotoModificaModulo(versione);
          }
        }, (reason) => {
          // faccio nullla
        }
      );
    } else {
      this.gotoModificaModulo(versione);
    }
  }

  gotoModificaModulo(versione: VersioneStato) {
    this.moduloInModifica = { ...this.modulo };
    this.moduloInModifica.idModulo = versione.idModulo;
    this.moduloInModifica.idVersioneModulo = versione.idVersioneModulo;
    this.moduloInModifica.stato.codice = versione.codice;
    this.moduloInModifica.stato.descrizione = versione.descrizione;
    this.currTemplate = 'modificaModulo';
  }

  eliminaModulo(versione: VersioneStato) {
    log('dati-generali::eliminaModulo() VersioneStato: ' + versione.idModulo + '/v/' + versione.idVersioneModulo);
    const modulo = new Modulo();
    modulo.idModulo = versione.idModulo;
    modulo.idVersioneModulo = versione.idVersioneModulo;
    modulo.codiceModulo = this.modulo.codiceModulo;
    modulo.descrizioneModulo = this.modulo.descrizioneModulo;
    modulo.descrizioneModulo = this.modulo.descrizioneModulo;
    log('dati-generali::eliminaModulo() ... emit(modulo[' + modulo.idModulo + '/v/' + modulo.idVersioneModulo + '])');
    this.eventEliminaModulo.emit(modulo);
  }

  eliminaVersioneModulo(versione: VersioneStato) {
    log('dati-generali::eliminaVersioneModulo() VersioneStato: ' + versione.idModulo + '/v/' + versione.idVersioneModulo);
    const modulo = new Modulo();
    modulo.idModulo = versione.idModulo;
    modulo.idVersioneModulo = versione.idVersioneModulo;
    modulo.codiceModulo = this.modulo.codiceModulo;
    modulo.descrizioneModulo = this.modulo.descrizioneModulo;
    modulo.descrizioneModulo = this.modulo.descrizioneModulo;
    log('dati-generali::eliminaVersioneModulo() ... emit(modulo[' + modulo.idModulo + '/v/' + modulo.idVersioneModulo + '])');
    this.eventEliminaModulo.emit(modulo);
  }

  backOperazione($event: string) {
    this.moduloInModifica = null;
    this.currTemplate = 'datiGenerali';
    this.spinnerService.hide();
  }

  caricaImageProcesso() {
    if (!this.modulo.processo.idProcesso || this.thumbnailProcesso) {
      return;
    }
    this.moonboblService.getImageProcesso(this.modulo.processo.idProcesso)
      .subscribe((baseImage: any) => {
        log('[dati-generali::caricaImageProcesso] getImageProcesso.subscribe ...');
        const reader = new FileReader();
        reader.onload = (e) => this.thumbnailProcesso = e.target.result;
        reader.readAsDataURL(new Blob([baseImage], { type: 'image/png' }));
      });
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

  initValutazioni() {
    // this.valutazioni = [
    //   { idValutazione: 0, descValutazione: "Senza Valutazione", numeroIstanze: 0, percent: 0.0 },
    //   { idValutazione: 1, descValutazione: "Buono", numeroIstanze: 50, percent: 48.98 },
    //   { idValutazione: 2, descValutazione: "Medio", numeroIstanze: 27, percent: 27.0 },
    //   { idValutazione: 3, descValutazione: "Cattivo", numeroIstanze: 24, percent: 23.78 }
    // ];
    this.moonboblService.getValutazioneModuloSintesi(this.moduloSelezionato.idModulo).subscribe(
      (res) => {
        this.valutazioni = res;
        this.sommaValutazioni = this.valutazioni.map(v => v['numeroIstanze']).reduce((partialSum, a) => partialSum + a, 0);
        this.titleValutazioni = this.valutazioni.map(v => formatTitleValutazione(v)).join('\r\n');
      },
      (err: MoonboError) => {
        log('dati-generali::getValutazioneModuloSintesi() ' + err.errorMsg);
      }
    );
  }

}

function formatTitleValutazione(v: any): string {
  const istanz_ = v['numeroIstanze']<2?'istanza':'istanze';
  return `${v['descValutazione']}  ${v['numeroIstanze']} ${istanz_}  (${v['percent'].toFixed(2)}%) `;  //  | number: '1.0-2'
}

function log(a: any) {
  // console.log(a);
}
