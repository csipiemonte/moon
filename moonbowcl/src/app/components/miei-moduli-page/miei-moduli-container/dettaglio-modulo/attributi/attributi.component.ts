/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { Attributi } from 'src/app/model/dto/attr/attributi';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrGenerali } from 'src/app/common/messaggi';
import { ModuloClassTipologia } from 'src/app/model/dto/modulo-class';
import { HttpErrorResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalAlertComponent } from 'src/app/components/modal/modal-alert/modal-alert.component';

@Component({
  selector: 'app-miei-moduli-dettaglio-attributi',
  templateUrl: './attributi.component.html',
  styleUrls: ['./attributi.component.scss']
})
export class AttributiComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  attributi = new Object();
  attributiEdit = new Attributi();

  elencoModuloAttributi: ModuloAttributo[];
  maInitObbligatoria: ModuloAttributo;
  maInitObbligatoriaEdit: ModuloAttributo;
  maInitNomeClass: ModuloAttributo;
  maInitNomeClassEdit: ModuloAttributo;
  maContoTerzi: ModuloAttributo;
  maContoTerziEdit: ModuloAttributo;
  maCompilaBO: ModuloAttributo;
  maCompilaBOEdit: ModuloAttributo;
  maIstanzaAutoSave: ModuloAttributo;
  maIstanzaAutoSaveEdit: ModuloAttributo;
  maHashUnivocita: ModuloAttributo;
  maHashUnivocitaEdit: ModuloAttributo;
  maHashUnivocitaFields: ModuloAttributo;
  maHashUnivocitaFieldsEdit: ModuloAttributo;
  maMsgUnivocita: ModuloAttributo;
  maMsgUnivocitaEdit: ModuloAttributo;
  maStampaPdf: ModuloAttributo;
  maStampaPdfEdit: ModuloAttributo;
  maTipoFilterBo: ModuloAttributo;
  maTipoFilterBoEdit: ModuloAttributo;
  maStampaDinamica: ModuloAttributo;
  maStampaDinamicaEdit: ModuloAttributo;
  maModificaIstanzaInviata: ModuloAttributo;
  maModificaIstanzaInviataEdit: ModuloAttributo;
  maFormioBreadcrumbClickable: ModuloAttributo;
  maFormioBreadcrumbClickableEdit: ModuloAttributo;

  titleUpd: Map<string, string> = new Map<string, string>();

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;

  printMapperName = '';
  printMapperModuloClassName = '';

  alertOptions = {
    id: 'alert-moduli-attributi',
    autoClose: true,
    keepAfterRouteChange: false
  };

  maInitObbligatoriaEditT = false;
  maIstanzaAutoSaveEditT = false;
  maHashUnivocitaEditT = false;
  maStampaPdfEditT = false;
  maStampaDinamicaEditT = false;
  maModificaIstanzaInviataEditT = false;
  maFormioBreadcrumbClickableEditT = false;

  filePrintMapperToUpload: File | null = null;

  constructor(
              private moonboblService: MoonboblService,
              private spinnerService: NgxSpinnerService,
              private sharedService: SharedService,
              private errNotificationError: ErrorNotificationService,
              private modalService: NgbModal,
              protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {

    this.alert.emit({clear: true});

    this.spinnerService.show();
    this.moonboblService.getModuloWithFields( this.moduloSelezionato.idModulo,
                                              this.moduloSelezionato.idVersioneModulo,
                                              'attributiGenerali').subscribe((modulo: Modulo) => {
        // Init degli attributi per viewMode
        this.modulo = modulo;
        this.elencoModuloAttributi = modulo.attributi;
        log('ngOnInit() modulo.attributi: ' + modulo.attributi);
        this.init();

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
    this.initPrintMapperName();
    this.initPrintMapperModuloClassName();
  }

  initPrintMapperName() {
    log('attributi::initPrintMapperName() this.moduloSelezionato = ' + this.moduloSelezionato);
    this.moonboblService.getPrintMapperName(this.moduloSelezionato).subscribe({
      'next': (resPMN) => {
        this.printMapperName = resPMN;
        log('attributi::initPrintMapperName() this.initPrintMapperName = ' + this.initPrintMapperName);
      },
      'error': (err) => {
        log('attributi::initPrintMapperName() err = ' + err);
      },
      'complete': () => {
        log('attributi::initPrintMapperName() complete ');
      }
    });
  }

  initPrintMapperModuloClassName() {
    log('attributi::initPrintMapperModuloClassName() this.moduloSelezionato = ' + this.moduloSelezionato);
    this.moonboblService.getModuloClassByTipologia(this.moduloSelezionato, 2).subscribe({
      'next': (resPMN) => {
        this.printMapperModuloClassName = resPMN.nomeClasse;
        log('attributi::initPrintMapperModuloClassName() this.printMapperModuloClassName = ' + this.printMapperModuloClassName);
      },
      'error': (err) => {
        log('attributi::initPrintMapperModuloClassName() err = ' + err);
      },
      'complete': () => {
        log('attributi::initPrintMapperModuloClassName() complete ');
      }
    });
  }

  init() {
    log('init() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maInitObbligatoria = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'INIT_OBBLIGATORIA', 'N');
    this.maInitObbligatoriaEdit = { ...this.maInitObbligatoria };
    this.maInitObbligatoriaEditT = this.maInitObbligatoriaEdit ? (this.maInitObbligatoriaEdit['valore'] === 'N' ? false : true) : false;

    log('init() this.maInitObbligatoria: ' + JSON.stringify(this.maInitObbligatoria));
    log('init() this.maInitObbligatoriaEdit: ' + JSON.stringify(this.maInitObbligatoriaEdit));
    //
    this.maInitNomeClass = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'INIT_NOME_CLASS', '');
    this.maInitNomeClassEdit = { ...this.maInitNomeClass };
    log('init() this.maInitNomeClass: ' + JSON.stringify(this.maInitNomeClass));
    log('init() this.maInitNomeClassEdit: ' + JSON.stringify(this.maInitNomeClassEdit));
    //
    this.maContoTerzi = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'CONTO_TERZI', '');
    this.maContoTerziEdit = { ...this.maContoTerzi };
    log('init() this.maContoTerzi: ' + JSON.stringify(this.maContoTerzi));
    log('init() this.maContoTerziEdit: ' + JSON.stringify(this.maContoTerziEdit));
    //
    this.maCompilaBO = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'COMPILA_BO', '');
    this.maCompilaBOEdit = { ...this.maCompilaBO };
    log('init() this.maCompilaBO: ' + JSON.stringify(this.maCompilaBO));
    log('init() this.maCompilaBOEdit: ' + JSON.stringify(this.maCompilaBOEdit));
    //
    this.maIstanzaAutoSave = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'ISTANZA_AUTO_SAVE', 'N');
    this.maIstanzaAutoSaveEdit = { ...this.maIstanzaAutoSave };
    this.maIstanzaAutoSaveEditT = this.maIstanzaAutoSaveEdit ? (this.maIstanzaAutoSaveEdit['valore'] === 'N' ? false : true) : false;

    log('init() this.maIstanzaAutoSave: ' + JSON.stringify(this.maIstanzaAutoSave));
    log('init() this.maIstanzaAutoSaveEdit: ' + JSON.stringify(this.maIstanzaAutoSaveEdit));
    //
    this.maHashUnivocita = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'HASH_UNIVOCITA', 'N');
    this.maHashUnivocitaEdit = { ...this.maHashUnivocita };
    this.maHashUnivocitaEditT = this.maHashUnivocitaEdit ? (this.maHashUnivocitaEdit['valore'] === 'N' ? false : true) : false;

    log('init() this.maHashUnivocita: ' + JSON.stringify(this.maHashUnivocita));
    log('init() this.maHashUnivocitaEdit: ' + JSON.stringify(this.maHashUnivocitaEdit));
    //
    this.maHashUnivocitaFields = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'HASH_UNIVOCITA_FIELDS', '');
    this.maHashUnivocitaFieldsEdit = { ...this.maHashUnivocitaFields };
    log('init() this.maHashUnivocitaFields: ' + JSON.stringify(this.maHashUnivocitaFields));
    log('init() this.maHashUnivocitaFieldsEdit: ' + JSON.stringify(this.maHashUnivocitaFieldsEdit));
    //
    this.maMsgUnivocita = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'MSG_UNIVOCITA', '');
    this.maMsgUnivocitaEdit = { ...this.maMsgUnivocita };
    log('init() this.maMsgUnivocita: ' + JSON.stringify(this.maMsgUnivocita));
    log('init() this.maMsgUnivocitaEdit: ' + JSON.stringify(this.maMsgUnivocitaEdit));
    //
    this.maStampaPdf = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'STAMPA_PDF', 'N');
    this.maStampaPdfEdit = { ...this.maStampaPdf }; 
    this.maStampaPdfEditT = this.maStampaPdfEdit ? (this.maStampaPdfEdit['valore'] === 'N' ? false : true) : false;
    log('init() this.maStampaPdf: ' + JSON.stringify(this.maStampaPdf));
    log('init() this.maStampaPdfEdit: ' + JSON.stringify(this.maStampaPdfEdit));
    //
    this.maTipoFilterBo = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'TIPO_FILTER_BO', '');
    this.maTipoFilterBoEdit = { ...this.maTipoFilterBo };
    log('init() this.maTipoFilterBo: ' + JSON.stringify(this.maTipoFilterBo));
    log('init() this.maTipoFilterBoEdit: ' + JSON.stringify(this.maTipoFilterBoEdit));
    //
    this.maStampaDinamica = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'STAMPA_DINAMICA', 'N');
    this.maStampaDinamicaEdit = { ...this.maStampaDinamica };
    this.maStampaDinamicaEditT = this.maStampaDinamicaEdit ? (this.maStampaDinamicaEdit['valore'] === 'N' ? false : true) : false;
    log('init() this.maStampaDinamica: ' + JSON.stringify(this.maStampaDinamica));
    log('init() this.maStampaDinamicaEdit: ' + JSON.stringify(this.maStampaDinamicaEdit));
    //
    this.maModificaIstanzaInviata = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'MODIFICA_ISTANZA_INVIATA', 'N');
    this.maModificaIstanzaInviataEdit = { ...this.maModificaIstanzaInviata };
    this.maModificaIstanzaInviataEditT = this.maModificaIstanzaInviataEdit ? (this.maModificaIstanzaInviataEdit['valore'] === 'N' ?
      false : true) : false;
    log('init() this.maModificaIstanzaInviata: ' + JSON.stringify(this.maModificaIstanzaInviata));
    log('init() this.maModificaIstanzaInviataEdit: ' + JSON.stringify(this.maModificaIstanzaInviataEdit));
    //
    this.maFormioBreadcrumbClickable = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'FORMIO_BREADCRUMB_CLICKABLE', 'N');
    this.maFormioBreadcrumbClickableEdit = { ...this.maFormioBreadcrumbClickable };
    this.maFormioBreadcrumbClickableEditT = this.maFormioBreadcrumbClickableEdit ? (this.maFormioBreadcrumbClickableEdit['valore'] === 'N' ?
      false : true) : false;
    log('init() this.maFormioBreadcrumbClickable: ' + JSON.stringify(this.maFormioBreadcrumbClickable));
    log('init() this.maFormioBreadcrumbClickableEdit: ' + JSON.stringify(this.maFormioBreadcrumbClickableEdit));
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

    if (this.maInitObbligatoriaEdit) { this.maInitObbligatoriaEdit['valore'] = (this.maInitObbligatoriaEditT ? 'S' : 'N'); }
    if (this.maIstanzaAutoSaveEdit) { this.maIstanzaAutoSaveEdit['valore'] = (this.maIstanzaAutoSaveEditT ? 'S' : 'N'); }
    if (this.maIstanzaAutoSaveEdit) { this.maHashUnivocitaEdit['valore'] = (this.maHashUnivocitaEditT ? 'S' : 'N'); }
    if (this.maStampaPdfEdit) { this.maStampaPdfEdit['valore'] = (this.maStampaPdfEditT ? 'S' : 'N'); }
    if (this.maStampaDinamicaEdit) { this.maStampaDinamicaEdit['valore'] = (this.maStampaDinamicaEditT ? 'S' : 'N'); }
    if (this.maModificaIstanzaInviataEdit) {
      this.maModificaIstanzaInviataEdit['valore'] = (this.maModificaIstanzaInviataEditT ? 'S' : 'N');
    }
    if (this.maFormioBreadcrumbClickableEdit) {
      this.maFormioBreadcrumbClickableEdit['valore'] = (this.maFormioBreadcrumbClickableEditT ? 'S' : 'N');
    }

    verificaToSave(maToSave, this.maInitObbligatoria, this.maInitObbligatoriaEdit);
    verificaToSave(maToSave, this.maInitNomeClass, this.maInitNomeClassEdit);
    verificaToSave(maToSave, this.maContoTerzi, this.maContoTerziEdit);
    verificaToSave(maToSave, this.maCompilaBO, this.maCompilaBOEdit);
    verificaToSave(maToSave, this.maIstanzaAutoSave, this.maIstanzaAutoSaveEdit);
    verificaToSave(maToSave, this.maHashUnivocita, this.maHashUnivocitaEdit);
    verificaToSave(maToSave, this.maHashUnivocitaFields, this.maHashUnivocitaFieldsEdit);
    verificaToSave(maToSave, this.maMsgUnivocita, this.maMsgUnivocitaEdit);
    verificaToSave(maToSave, this.maStampaPdf, this.maStampaPdfEdit);
    verificaToSave(maToSave, this.maTipoFilterBo, this.maTipoFilterBoEdit);
    verificaToSave(maToSave, this.maStampaDinamica, this.maStampaDinamicaEdit);
    verificaToSave(maToSave, this.maModificaIstanzaInviata, this.maModificaIstanzaInviataEdit);
    verificaToSave(maToSave, this.maFormioBreadcrumbClickable, this.maFormioBreadcrumbClickableEdit);
    log('Save maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiGenerali(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          this.init();
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi generali effettuato con successo !', this.alertOptions);
          this.alert.emit({ text: MsgAttrGenerali.SUCCESS_ATTR_GENERALI, type: 'success', autoclose:true});
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi generali !' + err.errorMsg, this.alertOptions);
          this.alert.emit({ text: MsgAttrGenerali.ERROR_ATTR_GENERALI, type: 'error', autoclose:false});
        }
      );
      this.spinnerService.hide();
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrGenerali.INFO_NO_MODIFY_ATTR_GENERALI, type: 'info', autoclose:false});
    }
  }

  handleFilePrintMapperInput(files: FileList) {
    if (files != null) {
      this.filePrintMapperToUpload = files.item(0);
    }
  }

  uploadFileToServer() {
    log('uploadFileToServer ... ');
    var msg = '';
    if (this.filePrintMapperToUpload != null) {
      this.spinnerService.show();
      this.moonboblService.postFileModuloClass(this.modulo, ModuloClassTipologia.PRINT_MAPPER, this.filePrintMapperToUpload)
      .subscribe({
        next: (v) => console.log(v),
        error: (e: HttpErrorResponse ) => {
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

        this.filePrintMapperToUpload = null;
        },
        complete: () => {
          log('complete.');
          this.spinnerService.hide();
          const mdRef = this.modalService.open(ModalAlertComponent);
          mdRef.componentInstance.modal_titolo = 'Avviso';
          mdRef.componentInstance.modal_contenuto = 'Upload avventuto con successo!';
          this.initPrintMapperModuloClassName();
        }
      });
    } else {
      const mdRef = this.modalService.open(ModalAlertComponent);
        mdRef.componentInstance.modal_titolo = 'Avviso';
        mdRef.componentInstance.modal_contenuto = 'Nessun file selezionato!';
    }
  }

  deletePrintMapper() {
    log('deletePrintMapper ... ');
    this.moonboblService.deleteModuloClass(this.modulo, ModuloClassTipologia.PRINT_MAPPER)
      .subscribe({
        next: () => log('Delete successful.'),
        error: (e: HttpErrorResponse) => {
          log('errore di cancellazione : ' + e.message);
        },
        complete: () => {
          log('complete.');
          this.initPrintMapperModuloClassName();
        }
      });
  }

}


function verificaToSave(result: ModuloAttributo[], ma: ModuloAttributo, maEdit: ModuloAttributo): ModuloAttributo[] {
  if (ma?.valore === maEdit['valore']) {
    log('Unchanged ' + ma['nome'] + ': ' + ma['valore']);
  } else {
    if (maEdit.nome && (maEdit.valore || (!maEdit.valore && ma.valore))) {
      log('Changed ' + ma['nome'] + ' FROM: ' + ma['valore'] + ' TO: ' + maEdit['valore']);
      result.push(maEdit);
    } else {
      log('verificaToSave INVALID ' + ma['nome'] + ' ma: ' + JSON.stringify(ma) + '  maEdit: ' + JSON.stringify(maEdit));
    }
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
