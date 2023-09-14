/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Modulo } from '../../../../model/dto/modulo';
import { faSave, faHome, faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from '../../../../services/moonbobl.service';
import { StatoModulo } from '../../../../model/dto/statoModulo';
import { MoonboError } from '../../../../model/common/moonbo-error';
import { ErrorNotificationService } from '../../../../services/error-notification.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { timer } from 'rxjs';
import { SharedService } from 'src/app/services/shared.service';
import { AlertService } from 'src/app/modules/alert';
import { CambiaStatoModuloOptions } from './cambia-stato-options';
import { MsgModulo, MsgStato } from 'src/app/common/messaggi';

enum TIPO_DATE {
  CURRENT = 'CURRENT',
  DATE_FUT = 'DATE_FUT'
}

@Component({
  selector: 'app-miei-moduli-cambia-stato',
  templateUrl: './cambia-stato-modulo.component.html',
  styleUrls: ['./cambia-stato-modulo.component.css']
})

export class CambiaStatoModuloComponent implements OnInit {

  @Input() moduloInLavorazione: Modulo;
  @Input() options: CambiaStatoModuloOptions;
  @Output() backEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output('alertService') alert = new EventEmitter();

  opt: CambiaStatoModuloOptions;

  minutesLater = 2;
  elencoStatiAmmissibili: StatoModulo[];
  elencoStorico: StatoModulo[];
  statoModuloSelezionato: string;
  faSave = faSave;
  faHome = faHome;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;

  isAdmin = false;
  dateTime: Date;
  minDateTime: Date;
  lastDateTime: Date;
  showData = false;
  statoRequired = false;
  dateRequired = false;

  timer = timer(1000, 1000);
  subscription: any;

  subscribeTimer: number;
  timeLeft = 60;

  tipoDataMin: string = TIPO_DATE.CURRENT;
  TIPO_DATE = TIPO_DATE;

  public isMenuCollapsed = true;

  formTypes = [
    { tipo: 'FRM', descrizione: 'FORM' },
    { tipo: 'WIZ', descrizione: 'WIZARD' }];

  alertOptions = {
    id: 'alert-cambia-stato',
    autoClose: false,
    keepAfterRouteChange: false
  };

  modeError = false;
  constructor(
    private moonboservice: MoonboblService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    private spinnerService: NgxSpinnerService,
    protected alertService: AlertService
  ) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('CambiaStatoModuloComponent::ngOnInit() IN options=' + JSON.stringify(this.options));
    const optionsDefault = new CambiaStatoModuloOptions();
    if (this.options) {
      this.opt = Object.assign(optionsDefault, this.options);
    } else {
      this.opt = optionsDefault;
    }
    log('CambiaStatoModuloComponent::ngOnInit() opt=' + JSON.stringify(this.opt));

    // start timer
    this.observableTimer();

    // set min date to now
    this.minDateTime = new Date(Date.now());
    this.minDateTime.setMinutes(this.minDateTime.getMinutes() + this.minutesLater);

    this.moonboservice.initCambioStato(this.moduloInLavorazione).subscribe(
      (response: any) => {
        this.elencoStorico = response.cronologia;
        if (response.dataMinCambioStato) {
          this.lastDateTime = new Date(response.dataMinCambioStato);
        } else {
          this.lastDateTime = null;
        }

        if (this.lastDateTime) {
          if (this.lastDateTime < this.minDateTime) {
            // this.tipoDataMin = TIPO_DATE.CURRENT;  default
            this.lastDateTime = this.minDateTime;
          } else {
            // lastDate > now => set mindate to lastdate
            this.minDateTime = this.lastDateTime;
            this.tipoDataMin = TIPO_DATE.DATE_FUT;
          }
        }
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
       
        this.spinnerService.hide();
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
        this.modeError = true;
      });


    this.moonboservice.getElencoStatiModulo(this.moduloInLavorazione.stato.codice).subscribe(
      (elencoStati: StatoModulo[]) => {
        this.elencoStatiAmmissibili = elencoStati;
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.spinnerService.hide();
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
        this.modeError = true;
      });
  }

  back() {
    this.subscription.unsubscribe();
    if (this.modeError) {
      // reset error
      this.errNotificationError.notification.next(null);
    }
    this.backEvent.emit('back');
  }

  salva() {
    this.subscription.unsubscribe();
    // validazione dateTime selezionato al salvataggio
    // if (!this.dateTime) {
    //   this.dateRequired = true;
    // }
    // else {
    //   this.dateRequired = false;
    // }

    // validazione modulo selezionato al salvataggio
    if (!this.statoModuloSelezionato) {
      // validazione stato non selezionato
      this.statoRequired = true;
    } else {
      // validazione stato selezionato
      this.statoRequired = false;

      if (this.showData === true) {
        // caso data obbligatoria
        if (this.lastDateTime && this.dateTime) {
        // this.dateTime.setMinutes(this.dateTime.getMinutes() + this.minutesLater);
          if (this.dateTime  < this.minDateTime) {
            this.dateTime  = this.minDateTime;
          }
          const dateTimeFormatted = this.getDateTimeFormatted(this.dateTime);
          this.cambiaStatoModuloAtDate(this.moduloInLavorazione, this.statoModuloSelezionato, dateTimeFormatted);
        } else {
          // caso data non obbligatoria
          this.cambiaStatoModuloNoDate(this.moduloInLavorazione, this.statoModuloSelezionato);
        }
      } else {
        this.cambiaStatoModuloNoDate(this.moduloInLavorazione, this.statoModuloSelezionato);
      }
    }
  }

  selezionaStato(stato) {
    this.statoRequired = false;
    if (stato === 'SOSP') {
      this.showData = true;
    } else {
      this.showData = false;
    }
    // TEST
    // unsubscribe timer
    // this.subscription.unsubscribe();
  }

  getDateTimeFormatted(dateTime: Date) {
    const month: number = dateTime.getMonth() + 1;
    const date = '' + dateTime.getFullYear() + '-'
      + month + '-' + dateTime.getDate() + 'T' + dateTime.getHours() + ':' + dateTime.getMinutes() + ':00';

    log('formatted date  ' + date);
    return date;
  }


  cambiaStatoModuloNoDate(moduloInLavorazione: Modulo, codiceNuovoStato: string) {   

    // this.subscription.unsubscribe();
    this.moonboservice.cambiaStatoModulo(moduloInLavorazione, codiceNuovoStato).subscribe(
      (resp) => {
        log('CambiaStatoModuloComponent::cambiaStatoModuloNoDate() moonboservice.cambiaStatoModulo resp=' + JSON.stringify(resp));

        // alert('Modulo salvato correttamente');
        // this.backEvent.emit('back');
        this.spinnerService.show();

        // this.subscription.unsubscribe();
        this.alert.emit({ text: MsgStato.SUCCESS_PASSAGGIO_DI_STATO, type: 'success', autoclose:true});

        this.backEvent.emit('save-' + moduloInLavorazione.idModulo);
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.spinnerService.hide();
        // this.alertService.error('Impossibile effettuare il cambio di stato.<br>' + err.errorMsg, this.alertOptions);
        // this.subscription.unsubscribe();
        this.alert.emit({ text: MsgStato.ERRORE_PASSAGGIO_DI_STATO+ ". " +err.errorMsg, type: 'error', autoclose:false});
      });
  }


  cambiaStatoModuloAtDate(moduloInLavorazione: Modulo, codiceNuovoStato: string, dateTimeFormatted: string) {
    this.moonboservice.cambiaStatoModuloInDataOra(moduloInLavorazione, codiceNuovoStato, dateTimeFormatted).subscribe(
      (resp) => {
        log(JSON.stringify(resp));
        // alert('Modulo salvato correttamente');
        // this.backEvent.emit('back');
        this.spinnerService.show();

        this.alert.emit({ text: MsgStato.SUCCESS_PASSAGGIO_DI_STATO, type: 'success', autoclose:true});
        
        this.backEvent.emit('save');
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.spinnerService.hide();

        this.alert.emit({ text: MsgStato.ERRORE_PASSAGGIO_DI_STATO+ ". " +err.errorMsg, type: 'error', autoclose:false});

        this.modeError = true;
      });
  }

  ngOnDestroy() {
    log('ngOnDestroy - unsubscribe');
    this.subscription.unsubscribe();
  }

  observableTimer() {
    this.subscription = this.timer.subscribe(val => {
      this.subscribeTimer = this.timeLeft - val;
      log('timer = ' + this.subscribeTimer);
      if  (this.subscribeTimer === 0) {
          log('trascorso un minuto: invalidazione del cambio stato');
          this.subscription.unsubscribe();
          this.spinnerService.show();
          this.ngOnInit();
          this.spinnerService.hide();
      }
    });
  }
}

function log(a: any) {
  console.log(a);
}
