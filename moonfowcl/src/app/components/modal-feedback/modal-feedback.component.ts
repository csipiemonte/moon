/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, ViewChild, ChangeDetectorRef, ElementRef, OnInit } from '@angular/core';
// import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Istanza} from '../../model/dto/istanza';
import {Valutazione, VALUTAZIONE, ValutazioneModulo} from '../../model/dto/valutazione-modulo';
import {MoonfoblService} from '../../services/moonfobl.service';
import { User } from 'src/app/model/common/user';
import { StorageManager } from "src/app/util/storage-manager";
import { STORAGE_KEYS } from 'src/app/common/costanti';
import { ValutazioneModuloCitFac } from 'src/app/model/dto/valutazione-modulo-citfac';
import { ConsumerParams } from 'src/app/model/common/consumer-params';

@Component({
  selector: 'app-modal-feedback',
  templateUrl: './modal-feedback.component.html',
  styleUrls: ['./modal-feedback.component.scss']
})
export class ModalFeedbackComponent implements OnInit {

  // @Input() modal_titolo;
  // @Input() modal_contenuto;
  @Input() istanza: Istanza;
  isBad: boolean = false;
  isGood: boolean = false;
  dettaglioValue: string;
  isDetails: Boolean = false;
  inviatoFeedback: boolean = false;

  valutazioneModulo: ValutazioneModulo = new ValutazioneModulo();
  valutazione: Valutazione;

  // sezione bad
  @ViewChild('trovaModulo') trovaModuloCheckbox: ElementRef;
  @ViewChild('compilaModulo') compilaModuloCheckbox: ElementRef;
  @ViewChild('inviaIstanza') inviaIstanzaCheckbox: ElementRef;
  @ViewChild('trovaIstanza') trovaIstanzaCheckbox: ElementRef;
  @ViewChild('comeProcedere') comeProcedereCheckbox: ElementRef;
  // sezione good
  @ViewChild('aspettoGrafico') aspettoGraficoCheckbox: ElementRef;
  @ViewChild('visualizzaResponsive') visualizzaResponsiveCheckbox: ElementRef;
  @ViewChild('proceduraCompilaInvio') proceduraCompilaInvioCheckbox: ElementRef;
  @ViewChild('sezioneAiuto') sezioneAiutoCheckbox: ElementRef;

  @ViewChild('dettaglio') dettaglioInputField;


  feedbackBgColor : string = '';
  

  constructor(private changeDetectorRef: ChangeDetectorRef,
              private moonfoblService: MoonfoblService,
              ) {

  }
  ngOnInit(): void {

    let user: User = StorageManager.get(STORAGE_KEYS.USER);
    if (user.consumerParams){
      let consumerParams : ConsumerParams = user.consumerParams;
      console.log(`is present consumerParams = ${JSON.stringify(consumerParams)}`);
      this.feedbackBgColor =  consumerParams.parameters?.header_bg_color['text'];
      console.log(`feedbackBgColor = ${this.feedbackBgColor}`);
    }

  }

  badFeedback() {
    this.isBad = true;
    this.isGood = false;
    this.isDetails = false
    this.valutazioneModulo.idValutazione = VALUTAZIONE.CATTIVO;
  }

  notSatisfiedFeedback(){
    this.isBad = true;
    this.isGood = false;
    this.isDetails = false
    this.valutazioneModulo.idValutazione = VALUTAZIONE.NONCONTENTO;
  }

  satisfiedFeedback(){
    this.isBad = false;
    this.isGood = true;
    this.isDetails = false
    this.valutazioneModulo.idValutazione = VALUTAZIONE.CONTENTO;
  }

  neutralFeedback() {
    this.isBad = true;
    this.isGood = false;
    this.isDetails = false
    this.valutazioneModulo.idValutazione = VALUTAZIONE.MEDIO;
  }

  goodFeedback() {
    this.isBad = false;
    this.isGood = true;
    this.isDetails = false
    this.valutazioneModulo.idValutazione = VALUTAZIONE.BUONO;
    

  }
  goBack(){
    this.isDetails = false
  }
  reset() {
    this.isBad = false;
    this.isGood = false;
    this.isDetails = false;
    this.valutazioneModulo = new ValutazioneModulo();
    this.changeDetectorRef.detectChanges();
  }
  

  toggleToActive(elementId, className) {
    const element: HTMLElement = document.getElementById(elementId);
    element.classList.add(className);
  }

  toggleToNormal(elementId, className) {
    const element: HTMLElement = document.getElementById(elementId);
    if (element.classList.contains(className)) {
      element.classList.remove(className);
    }
  }
  avanti(){
    this.valutazioneModulo.valutazione = this.getValutazione()
    this.isDetails = true
  }

  inviaFeedbacks() {
    this.inviaFeedbackMoon();

    const user: User = StorageManager.get(STORAGE_KEYS.USER);
    // console.log(user.consumerParams.consumer);
    const consumerCitFac = user?.consumerParams?.consumer === 'cittafacile';
    if (consumerCitFac) {
      this.inviaFeedbackCitFac();
    }
    this.inviatoFeedback = !this.inviatoFeedback
  }

  inviaFeedbackMoon() {
    this.valutazioneModulo.idIstanza = this.istanza.idIstanza;
    this.valutazioneModulo.idModulo = this.istanza.modulo.idModulo;
    this.valutazioneModulo.idVersioneModulo = this.istanza.modulo.idVersioneModulo;
    this.valutazioneModulo.valutazione
    this.valutazioneModulo.valutazione.dettaglio = this.dettaglioValue
    console.log(this.valutazioneModulo, 'Valutazione Modulo');
    // invio feedback
    this.moonfoblService.inserisciValutazione(this.valutazioneModulo).subscribe();
  }

  inviaFeedbackCitFac() {
    let valutazioneCitFac = new ValutazioneModuloCitFac();
    valutazioneCitFac.subject = this.istanza.modulo.oggettoModulo;
    valutazioneCitFac.code = this.istanza.modulo.codiceModulo;
    const v =this.valutazioneModulo;
    valutazioneCitFac.rating = v.idValutazione;
    valutazioneCitFac.option = v.valutazione.trovaModulo ? "negativo1" :
                               v.valutazione.compilaModulo ? "negativo2" : 
                               v.valutazione.inviaIstanza ? "negativo3" : 
                               v.valutazione.trovaIstanza ? "negativo4" : 
                               v.valutazione.comeProcedere ? "altro" : 
                               v.valutazione.aspettoGrafico ? "positivo1" :
                               v.valutazione.visualizzaResponsive ? "positivo2" : 
                               v.valutazione.proceduraCompilaInvio ? "positivo3" : 
                               v.valutazione.sezioneAiuto ? "positivo4" : null;
    valutazioneCitFac.details = this.dettaglioValue ? this.dettaglioValue : "";
    valutazioneCitFac.bind = "";
    console.log('valutazione Citta Facile', valutazioneCitFac);
    this.moonfoblService.postFeedbackCitFac(valutazioneCitFac).subscribe();
    
    
  }

  getValutazione(): Valutazione {
    let valutazione =  new Valutazione();
    switch (this.valutazioneModulo.idValutazione) {
      case VALUTAZIONE.BUONO:
        valutazione.trovaModulo = false;
        valutazione.compilaModulo = false;
        valutazione.inviaIstanza = false;
        valutazione.trovaIstanza = false;
        
        valutazione.aspettoGrafico = this.aspettoGraficoCheckbox.nativeElement.checked;
        valutazione.visualizzaResponsive = this.visualizzaResponsiveCheckbox.nativeElement.checked;
        valutazione.proceduraCompilaInvio = this.proceduraCompilaInvioCheckbox.nativeElement.checked;
        valutazione.sezioneAiuto = this.sezioneAiutoCheckbox.nativeElement.checked;
        valutazione.comeProcedere = this.comeProcedereCheckbox.nativeElement.checked;
        valutazione.dettaglio = this.dettaglioValue;
        break;
      case VALUTAZIONE.CONTENTO:
        valutazione.aspettoGrafico = this.aspettoGraficoCheckbox.nativeElement.checked;
        valutazione.visualizzaResponsive = this.visualizzaResponsiveCheckbox.nativeElement.checked;
        valutazione.proceduraCompilaInvio = this.proceduraCompilaInvioCheckbox.nativeElement.checked;
        valutazione.sezioneAiuto = this.sezioneAiutoCheckbox.nativeElement.checked;
        valutazione.comeProcedere = this.comeProcedereCheckbox.nativeElement.checked;
        valutazione.dettaglio = this.dettaglioValue;

        valutazione.trovaModulo = false;
        valutazione.compilaModulo = false;
        valutazione.inviaIstanza = false;
        valutazione.trovaIstanza = false;
        break;

      case VALUTAZIONE.MEDIO:
        valutazione.trovaModulo = this.trovaModuloCheckbox.nativeElement.checked;
        valutazione.compilaModulo = this.compilaModuloCheckbox.nativeElement.checked;
        valutazione.inviaIstanza = this.inviaIstanzaCheckbox.nativeElement.checked;
        valutazione.trovaIstanza = this.trovaIstanzaCheckbox.nativeElement.checked;
        valutazione.comeProcedere = this.comeProcedereCheckbox.nativeElement.checked;
        valutazione.dettaglio = this.dettaglioValue;

        valutazione.aspettoGrafico = false;
        valutazione.visualizzaResponsive = false;
        valutazione.proceduraCompilaInvio = false;
        valutazione.sezioneAiuto = false;
        break;

      case VALUTAZIONE.NONCONTENTO:
        valutazione.trovaModulo = this.trovaModuloCheckbox.nativeElement.checked;
        valutazione.compilaModulo = this.compilaModuloCheckbox.nativeElement.checked;
        valutazione.inviaIstanza = this.inviaIstanzaCheckbox.nativeElement.checked;
        valutazione.trovaIstanza = this.trovaIstanzaCheckbox.nativeElement.checked;
        valutazione.comeProcedere = this.comeProcedereCheckbox.nativeElement.checked;
        valutazione.dettaglio = this.dettaglioValue;

        valutazione.aspettoGrafico = false;
        valutazione.visualizzaResponsive = false;
        valutazione.proceduraCompilaInvio = false;
        valutazione.sezioneAiuto = false;
        break;
      case VALUTAZIONE.CATTIVO:
        valutazione.trovaModulo = this.trovaModuloCheckbox.nativeElement.checked;
        valutazione.compilaModulo = this.compilaModuloCheckbox.nativeElement.checked;
        valutazione.inviaIstanza = this.inviaIstanzaCheckbox.nativeElement.checked;
        valutazione.trovaIstanza = this.trovaIstanzaCheckbox.nativeElement.checked;
        valutazione.comeProcedere = this.comeProcedereCheckbox.nativeElement.checked;
        valutazione.dettaglio = this.dettaglioValue;

        valutazione.aspettoGrafico = false;
        valutazione.visualizzaResponsive = false;
        valutazione.proceduraCompilaInvio = false;
        valutazione.sezioneAiuto = false;
        break;
    }
    return valutazione;
  }

}
