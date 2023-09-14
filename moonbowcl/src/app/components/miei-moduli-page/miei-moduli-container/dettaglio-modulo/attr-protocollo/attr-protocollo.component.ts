/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ModuloClassTipologia } from 'src/app/model/dto/modulo-class';
import { ModuloClass } from './../../../../../model/dto/modulo-class';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome, faTrashAlt, faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { ProtocolloParametro } from 'src/app/model/dto/protocollo-parametro';
import { AttrEmailConf } from 'src/app/model/dto/attr/attrEmailConf';
import { MsgAttrProtocollo } from 'src/app/common/messaggi';
import { HttpErrorResponse } from '@angular/common/http';
import { ModalAlertComponent } from 'src/app/components/modal/modal-alert/modal-alert.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo',
  templateUrl: './attr-protocollo.component.html',
  styleUrls: ['./attr-protocollo.component.scss']
})
export class AttrProtocolloComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maProtocollo: ModuloAttributo = new ModuloAttributo();
  hasProtocolloBO: string = 'N';
  maProtocolloIntegrazione: ModuloAttributo = new ModuloAttributo();
  maProtocolloEdit: ModuloAttributo = new ModuloAttributo();
  maProtocolloIntegrazioneEdit: ModuloAttributo = new ModuloAttributo();
  //
  maPrtMetadati: ModuloAttributo = new ModuloAttributo();
  maPrtMetadatiEdit: ModuloAttributo = new ModuloAttributo();
  //
  maPrtInEmail: ModuloAttributo = new ModuloAttributo();
  maPrtInEmailEdit: ModuloAttributo = new ModuloAttributo();
  maPrtInEmailConf: ModuloAttributo = new ModuloAttributo();

  attrPrtInEmailConf: object = new Object();
  attrPrtInEmailConfEdit: AttrEmailConf = new AttrEmailConf();

  titleUpd: Map<string, string> = new Map<string, string>();

  prtParametri: ProtocolloParametro[];

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;
  modeError = false;

  bProtocolloEdit = false;
  bHasProtocolloBOEdit = false;
  bProtocolloIntegrazioneEdit = false;
  bPrtInEmailEdit = false;
  spinnerLevel = 0;

  showCc = false;
  showBcc = false;

  protocolloManagerName = '';
  protocolloModuloClassName = '';
  fileProtocolloManagerToUpload: File | null = null;

  alertOptions = {
    id: 'alert-moduli-attr-protocollo',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    private modalService: NgbModal,
    protected alertService: AlertService) {
      this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    this.alert.emit({ clear: true });
    this.init();
  }

  init(): void {
    this.showSpinner();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
                                            this.moduloSelezionato.idVersioneModulo,
                                            'attributiProtocollo').subscribe({
      'next': (modulo: Modulo) => {
        // Init degli attributi per viewMode
        this.modulo = modulo;
        this.elencoModuloAttributi = modulo.attributi;
        this.initPricipalsFields();
        this.initMetadatiFields();
        this.initEmailFields();

        // tslint:disable-next-line:max-line-length
        if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
          this.isModificabile = true;
        }

        this.hideSpinner();
      },
      'error': (err: MoonboError) => {
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false});
        this.modeError = true;
        this.hideSpinner();
      }
    });
    // ProtocolloParametri
    this.moonboblService.getProtocolloParametri(this.moduloSelezionato).subscribe({
      'next': (res) => {
        this.prtParametri = res;
        if (!this.prtParametri) {
          this.prtParametri = [];
        }
        this.hideSpinner();
      },
      'error': (err: MoonboError) => {
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false});
        this.modeError = true;
        this.hideSpinner();
      }
    });
    this.verifyHasProtocolloBO(this.moduloSelezionato.idModulo);
    // ProtocolloManagerName
    this.initProtocolloManagerName();
    this.initProtocolloModuloClassName();
  }

  initProtocolloManagerName() {
    log('attr-protocollo::initProtocolloManagerName() this.moduloSelezionato = ' + this.moduloSelezionato);
    this.moonboblService.getProtocolloManagerName(this.moduloSelezionato).subscribe({
      'next': (resPMN) => {
        this.protocolloManagerName = resPMN;
        log('attr-protocollo::initProtocolloManagerName() this.protocolloManagerName = ' + this.protocolloManagerName);
      },
      'error': (err) => {
        log('attr-protocollo::initProtocolloManagerName() err = ' + err);
      },
      'complete': () => {
        log('attr-protocollo::initProtocolloManagerName() complete ');
      }
    });
  }

  initProtocolloModuloClassName() {
    log('attr-protocollo::initProtocolloModuloClassName() this.moduloSelezionato = ' + this.moduloSelezionato);
    this.moonboblService.getModuloClassByTipologia(this.moduloSelezionato, ModuloClassTipologia.PROTOCOLLO_MANAGER).subscribe({
      'next': (resMC) => {
        this.protocolloModuloClassName = resMC.nomeClasse;
        log('attr-protocollo::initProtocolloModuloClassName() this.protocolloModuloClassName = ' + this.protocolloModuloClassName);
      },
      'error': (err) => {
        log('attr-protocollo::initProtocolloModuloClassName() err = ' + err);
      },
      'complete': () => {
        log('attr-protocollo::initProtocolloModuloClassName() complete ');
      }
    });
  }

  showSpinner() {
    log('attr-protocollo::showSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel++;
    if (this.spinnerLevel === 1) {
      log('attr-protocollo::showSpinner() spinnerService.show().');
      this.spinnerService.show();
    }
  }

  hideSpinner() {
    log('attr-protocollo::hideSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel--;
    if (this.spinnerLevel === 0) {
      log('attr-protocollo::hideSpinner() spinnerService.hide().');
      this.spinnerService.hide();
    }
  }

  initPricipalsFields() {
    log('attr-protocollo::initPricipalsFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maProtocollo = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_PROTOCOLLO', 'N');
    this.maProtocolloIntegrazione = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_PROTOCOLLO_INTEGRAZIONE', 'N');
    this.maProtocolloEdit = { ...this.maProtocollo };
    this.maProtocolloIntegrazioneEdit = { ...this.maProtocolloIntegrazione };
    this.bProtocolloEdit = this.maProtocolloEdit ? (this.maProtocolloEdit['valore'] === 'N' ? false : true) : false;
    this.bProtocolloIntegrazioneEdit = this.maProtocolloIntegrazioneEdit ?
      (this.maProtocolloIntegrazioneEdit['valore'] === 'N' ? false : true) : false;
    log('attr-protocollo::initPricipalsFields() this.maProtocollo: ' + JSON.stringify(this.maProtocollo));
    log('attr-protocollo::initPricipalsFields() this.maProtocolloEdit: ' + JSON.stringify(this.maProtocolloEdit));
  }

  initMetadatiFields() {
    log('attr-protocollo::initMetadatiFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maPrtMetadati = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PRT_METADATI', '[]');
    this.maPrtMetadatiEdit = { ...this.maPrtMetadati };
    log('attr-protocollo::initMetadatiFields() this.maPrtMetadati: ' + this.maPrtMetadati);
    log('attr-protocollo::initMetadatiFields() this.maPrtMetadati: ' + JSON.stringify(this.maPrtMetadati));
  }

  initEmailFields() {
    log('attr-protocollo::initEmailFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maPrtInEmail = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PCPT_IN_EMAIL', 'N');
    this.maPrtInEmailEdit = { ...this.maPrtInEmail };
    this.bPrtInEmailEdit = this.maPrtInEmailEdit ? (this.maPrtInEmailEdit['valore'] === 'N' ? false : true) : false;

    log('attr-protocollo::initEmailFields() this.maPrtInEmail: ' + JSON.stringify(this.maPrtInEmail));
    log('attr-protocollo::initEmailFields() this.maPrtInEmailEdit: ' + JSON.stringify(this.maPrtInEmailEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maPrtInEmailConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PCPT_IN_EMAIL_CONF');
      if (this.maPrtInEmailConf) {
        this.attrPrtInEmailConf = JSON.parse(this.maPrtInEmailConf.valore);
        this.attrPrtInEmailConfEdit = JSON.parse(this.maPrtInEmailConf.valore); // Object.assign({}, this.maPrtInEmailConf);
        this.showCc = (this.attrPrtInEmailConfEdit.cc || this.attrPrtInEmailConfEdit.cc_istanza_data_key) ? true : false;
        this.showBcc = (this.attrPrtInEmailConfEdit.bcc || this.attrPrtInEmailConfEdit.bcc_istanza_data_key) ? true : false;
        this.titleUpd.set(this.maPrtInEmailConf.nome, 'Ultima modifica il ' + this.maPrtInEmailConf.dataUpd + ' da ' + this.maPrtInEmailConf.attoreUpd);
      }
      log('attr-protocollo::initEmailFields() this.attrPrtInEmailConf: ' + JSON.stringify(this.attrPrtInEmailConf));
      log('attr-protocollo::initEmailFields() this.attrPrtInEmailConfEdit: ' + JSON.stringify(this.attrPrtInEmailConfEdit));
    }
    if (!this.maPrtInEmailConf) {
      this.maPrtInEmailConf = creaModuloAttributo('PCPT_IN_EMAIL_CONF', '{}');
    }
  }

  findOrCreateWithDefaultValue(moduloAttributi: ModuloAttributo[], nome: string, defaultValore: string): ModuloAttributo {
    let result: ModuloAttributo;
    if (moduloAttributi && moduloAttributi.length > 0) {
      result = moduloAttributi.find(ma => ma.nome === nome);
    }
    if (!result) {
      result = creaModuloAttributo(nome, defaultValore);
    } else {
      this.titleUpd.set(result.nome, 'Ultima modifica il ' + result.dataUpd + ' da ' + result.attoreUpd);
    }
    return result;
  }

  salva() {
    this.spinnerService.show();
    // tslint:disable-next-line:prefer-const
    let maToSave = [];

    // PricipalsFields
    if (this.maProtocolloEdit) { this.maProtocolloEdit['valore'] = (this.bProtocolloEdit ? 'S' : 'N'); }
    if (this.maProtocolloIntegrazioneEdit) { this.maProtocolloIntegrazioneEdit['valore'] = (this.bProtocolloIntegrazioneEdit ? 'S' : 'N'); }
    verificaToSave(maToSave, this.maProtocollo, this.maProtocolloEdit);
    verificaToSave(maToSave, this.maProtocolloIntegrazione, this.maProtocolloIntegrazioneEdit);
    // MetadatiFields

    // EmailFields
    if (this.maPrtInEmailEdit) { this.maPrtInEmailEdit['valore'] = (this.bPrtInEmailEdit ? 'S' : 'N'); }
    verificaToSave(maToSave, this.maPrtInEmail, this.maPrtInEmailEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PCPT_IN_EMAIL_CONF', this.attrPrtInEmailConf, this.prtInEmailConfJsonStr);

    // SAVE
    log('Save maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiProtocollo(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
        // this.alertService.success('Salvataggio attributi protocollo effettuato con successo !', this.alertOptions);

        this.alert.emit({ text: MsgAttrProtocollo.SUCCESS_ATTR_PROTOCOLLO, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi protocollo !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrProtocollo.ERROR_ATTR_PROTOCOLLO, type: 'error', autoclose:false});
        }
      );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrProtocollo.INFO_NO_MODIFY_ATTR_PROTOCOLLO, type: 'info', autoclose:false});
    }
  }

  get prtInEmailConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo string vuoto nel JSON, lo togliamo completamento
    // tslint:disable:max-line-length
    this.attrPrtInEmailConfEdit.to = ((this.attrPrtInEmailConfEdit.to || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.to.trim();
    this.attrPrtInEmailConfEdit.to_istanza_data_key = ((this.attrPrtInEmailConfEdit.to_istanza_data_key || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.to_istanza_data_key.trim();
    this.attrPrtInEmailConfEdit.cc = ((this.attrPrtInEmailConfEdit.cc || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.cc.trim();
    this.attrPrtInEmailConfEdit.cc_istanza_data_key = ((this.attrPrtInEmailConfEdit.cc_istanza_data_key || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.cc_istanza_data_key.trim();
    this.attrPrtInEmailConfEdit.bcc = ((this.attrPrtInEmailConfEdit.bcc || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.bcc.trim();
    this.attrPrtInEmailConfEdit.bcc_istanza_data_key = ((this.attrPrtInEmailConfEdit.bcc_istanza_data_key || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.bcc_istanza_data_key.trim();
    this.attrPrtInEmailConfEdit.subject = ((this.attrPrtInEmailConfEdit.subject || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.subject.trim();
    this.attrPrtInEmailConfEdit.text = ((this.attrPrtInEmailConfEdit.text || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.text.trim();
    this.attrPrtInEmailConfEdit.html = ((this.attrPrtInEmailConfEdit.html || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.html.trim();
//    this.attrPrtInEmailConfEdit.protocollo_to = ((this.attrPrtInEmailConfEdit.protocollo_to || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.protocollo_to.trim();
//    this.attrPrtInEmailConfEdit.subject_rinvio = ((this.attrPrtInEmailConfEdit.subject_rinvio || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.subject_rinvio.trim();
//    this.attrPrtInEmailConfEdit.text_rinvio = ((this.attrPrtInEmailConfEdit.text_rinvio || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.text_rinvio.trim();
//    this.attrPrtInEmailConfEdit.html_rinvio = ((this.attrPrtInEmailConfEdit.html_rinvio || '').trim().length === 0) ? undefined : this.attrPrtInEmailConfEdit.html_rinvio.trim();
    // tslint:enable:max-line-length
    return JSON.stringify(this.attrPrtInEmailConfEdit);
  }

  handleFileProtocolloManagerInput(files: FileList) {
    if (files != null) {
      this.fileProtocolloManagerToUpload = files.item(0);
    }
  }

  uploadFileToServer() {
    log('uploadFileToServer ... ');
    var msg = '';
    if (this.fileProtocolloManagerToUpload != null) {
      this.spinnerService.show();
      this.moonboblService.postFileModuloClass(this.modulo, ModuloClassTipologia.PROTOCOLLO_MANAGER, this.fileProtocolloManagerToUpload)
        .subscribe({
          next: (v) => console.log(v),
          error: (e: HttpErrorResponse) => {
            this.spinnerService.hide();
            if (e.error.code === 'MOONSRV-20701') {
              msg = 'Errore upload del file.class!';
            } else if (e.error.code === 'MOONSRV-20702') {
              msg = 'Errore Tipologia Class, file non salvato!';
            } else {
              msg = 'Errore servizio upload, file non salvato!';
            }

            const mdRef = this.modalService.open(ModalAlertComponent);
            mdRef.componentInstance.modal_titolo = 'Errore';
            mdRef.componentInstance.modal_contenuto = msg;

            this.fileProtocolloManagerToUpload = null;
          },
          complete: () => {
            log('complete.');
            this.spinnerService.hide();
            const mdRef = this.modalService.open(ModalAlertComponent);
            mdRef.componentInstance.modal_titolo = 'Avviso';
            mdRef.componentInstance.modal_contenuto = 'Upload avventuto con successo!';
            this.initProtocolloModuloClassName();
          }
        });
    } else {
      const mdRef = this.modalService.open(ModalAlertComponent);
      mdRef.componentInstance.modal_titolo = 'Avviso';
      mdRef.componentInstance.modal_contenuto = 'Nessun file selezionato!';
    }
  }

  deleteProtocolloManager() {
    log('deleteProtocolloManager ... ');
    this.moonboblService.deleteModuloClass(this.modulo, ModuloClassTipologia.PROTOCOLLO_MANAGER)
      .subscribe({
        next: () => log('Delete successful.'),
        error: (e: HttpErrorResponse) => {
          log('errore di cancellazione : ' + e.message);
        },
        complete: () => {
          log('complete.');
          this.initProtocolloModuloClassName();
        }
      });
  }

  verifyHasProtocolloBO(idModulo: number) {
    this.hasProtocolloBO = 'N';
    this.bHasProtocolloBOEdit = false;
    this.moonboblService.hasProtocolloBo(idModulo).then(
      ret => {
        if (ret) {
          this.hasProtocolloBO = 'S';
          this.bHasProtocolloBOEdit = true;
        }
      }
    );
  }

}

function verificaToSave(result: ModuloAttributo[], ma: ModuloAttributo, maEdit: ModuloAttributo): ModuloAttributo[] {
  if (ma?.valore === maEdit['valore']) {
    log('attr-protocollo::verificaToSave Unchanged ' + ma['nome'] + ': ' + ma['valore']);
  } else {
    if (maEdit.nome && (maEdit.valore || (!maEdit.valore && ma.valore))) {
      log('attr-protocollo::verificaToSave Changed ' + ma['nome'] + ' FROM: ' + ma['valore'] + ' TO: ' + maEdit['valore']);
      result.push(maEdit);
    } else {
      log('attr-protocollo::verificaToSave INVALID ' + ma['nome'] + ' ma: ' + JSON.stringify(ma) + '  maEdit: ' + JSON.stringify(maEdit));
    }
  }
  return result;
}

function addToSaveAttrConfJsonStr(result: ModuloAttributo[], nomeAttributoJson: string, attrConf: Object, confJsonStrEdit: string)
  : ModuloAttributo[] {
  if (attrConf && JSON.stringify(attrConf) === JSON.stringify(JSON.parse(confJsonStrEdit))) {
    log('attr-protocollo::addToSaveAttrConfJsonStr Unchanged ' + nomeAttributoJson + ': ' + JSON.stringify(attrConf));
  } else {
    log('attr-protocollo::addToSaveAttrConfJsonStr Changed ' + nomeAttributoJson + ' \nFROM: ' + JSON.stringify(attrConf) + ' \nTO: ' + confJsonStrEdit);
    result.push(creaModuloAttributo(nomeAttributoJson, confJsonStrEdit));
  }
  return result;
}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function log(a: any) {
  // console.log(a);
}
