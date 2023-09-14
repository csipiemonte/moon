/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faSave, faHome } from '@fortawesome/free-solid-svg-icons';
import { NgxSpinnerService } from 'ngx-spinner';
import { Categoria } from 'src/app/model/dto/categoria';
import { FormIOModulo } from 'src/app/model/dto/form-iomodulo';
import { Modulo } from 'src/app/model/dto/modulo';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { StatoModulo } from '../../../../model/dto/statoModulo';
import { Processo } from '../../../../model/dto/processo';
import { ServiziError } from 'src/app/common/messaggi';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalNotifyComponent } from 'src/app/components/modal/modal-notify/modal-notify.component';
import { ModalBasicComponent } from 'src/app/components/modal/modal-basic/modal-basic.component';
import { ModalAlertComponent } from 'src/app/components/modal/modal-alert/modal-alert.component';
import {Subject} from 'rxjs';
import {ModuliCacheService} from '../../../../services/moduli-cache.service';



@Component({
  selector: 'app-miei-moduli-crea',
  templateUrl: './crea-modulo.component.html',
  styleUrls: ['./crea-modulo.component.scss']
})
export class CreaModuloComponent implements OnInit {

  moduloFormIO: FormIOModulo = {} as FormIOModulo;
  resetFormioEditor$ = new Subject<void>();
  moduloMoon: Modulo = new Modulo();
  msg = '';
  private idModulo: number = null;

  isAdmin = false;

  formTypes = [
    { tipo: 'form', descrizione: 'FORM' },
    { tipo: 'wizard', descrizione: 'WIZARD' }];
  categorie: Categoria[];

  @Input() elencoProcessi: Processo[];
  @Output() backEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output('alertService') alert = new EventEmitter();

  constructor(
    private router: Router,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private library: FaIconLibrary,
    private sharedService: SharedService,
    private modalService: NgbModal,
    private mouduliCacheService: ModuliCacheService
  ) {
    library.addIcons(faSave, faHome);

    // funzione riservata a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit() {
    this.spinnerService.show();
    this.caricaElencoCategorie();

    this.moduloMoon = new Modulo();
    this.moduloMoon.processo = this.elencoProcessi[0];
    this.spinnerService.hide();
  }

  onSubmit(ev: any) {
    console.log(ev);
    this.salva();
  }

  async caricaElencoCategorie() {
    this.spinnerService.show();
    try {
      this.categorie = await this.moonboblService.getElencoCategorie().toPromise() as Categoria[];
      this.spinnerService.hide();
    } catch (e) {
      this.spinnerService.hide();
      switch (e.status) {
        case 404:
          // alert('Servizio non trovato');
          this.alert.emit({ text: ServiziError.SERVICE_NOT_FOUND, type: 'error', autoclose: false });
          break;
        case 403:
          // alert('Utente non abilitato al servizio');
          this.alert.emit({ text: ServiziError.USER_NOT_QUALIFIED, type: 'error', autoclose: false });
          break;
        default:
          // alert('Errore servizio: ' + e.message);
          this.alert.emit({ text: e.message, type: 'error', autoclose: false });
      }
    }
  }

  salva() {
    this.spinnerService.show();

    this.alert.emit({clear: true});
    
    console.log('NEW');
    // const moduloFormIO: FormIOModulo = {} as FormIOModulo;
    this.moduloFormIO.name = this.moduloMoon.codiceModulo;
    this.moduloMoon.codiceModulo = this.moduloMoon.codiceModulo.toUpperCase();

    // this.moduloFormIO.type = (this.moduloTipoStuttura === 'WIZ') ? 'wizard':'form';
    // this.moduloFormIO.type = 'form';
    this.moduloFormIO.path = 'P_' + this.moduloMoon.codiceModulo;
    this.moduloFormIO.title = this.moduloMoon.oggettoModulo;
    this.moduloFormIO.machineName = 'MN_' + this.moduloMoon.codiceModulo;
    this.moduloMoon.struttura = JSON.stringify(this.moduloFormIO);
    // Imposto valore default per stato
    const statoModulo = new StatoModulo();
    statoModulo.codice = 'INIT';
    statoModulo.descrizione = 'IN COSTRUZIONE';
    this.moduloMoon.stato = statoModulo;
    // Imposto tipoStruttura
    this.moduloMoon.tipoStruttura = this.moduloTipoStuttura;

    this.moonboblService.inserisciModulo(this.moduloMoon).subscribe(response => {
      const risposta = response as Modulo;
      this.moduloMoon.idModulo = risposta.idModulo;
      this.moduloMoon.idVersioneModulo = risposta.idVersioneModulo;

      this.msg = 'Modulo inserito con id: ' + this.moduloMoon.idModulo;

      this.sharedService.mieiModuli.push(this.moduloMoon);
      // invalido la cache
      this.mouduliCacheService.clearCache();
      
      this.alert.emit({ text: this.msg, type: 'success', autoclose: false });

      this.backEvent.emit(this.msg);

      // FIXME - gestione navigazione tabs
      //      this.router.navigate(['mieiModuli']);
      // Tornare su VIEW.ELENCO azzerando filro e vedendo il nuovo modulo come prima della list (per potere fare clik su modifica struttura)

      this.spinnerService.hide();
    },
      (e) => {
        var msg = '';
        console.log(e.message);
        // alert(e.message);
       
        if (e.error.code === 'MOONBOBL-30101') {
          msg = 'Codice Modulo gia\' presente!';
          this.alert.emit({ text: msg, type: 'error', autoclose: false });
        }
        else{
          this.alert.emit({ text: e.message, type: 'error', autoclose: false });
        }

        this.spinnerService.hide();

        // const mdRef = this.modalService.open(ModalAlertComponent);
        // mdRef.componentInstance.modal_titolo = 'Errore';
        // mdRef.componentInstance.modal_contenuto = msg;
        //alert(e.message);
      });
  }

  changeDisplay() {

    //  this.moduloMoon.tipoStruttura = this.moduloTipoStuttura;
    //  this.resetFormioEditor$.next();
  }

  get moduloTipoStuttura(): string {
    return this.moduloFormIO.display === 'wizard' ? 'WIZ' : 'FRM';
  }

  //  getElencoModuli(): void {
  //    this.moonboblService.getElencoModuli().then(
  //      moduli => {
  //        this.sharedService.mieiModuli = moduli;
  //      }
  //    );
  //  }

  back() {
    this.spinnerService.show();
    this.alert.emit({clear: true});
    this.backEvent.emit('back');
  }
}


