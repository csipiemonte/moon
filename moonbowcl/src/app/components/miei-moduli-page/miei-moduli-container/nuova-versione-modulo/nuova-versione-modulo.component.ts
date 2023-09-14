/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Modulo} from '../../../../model/dto/modulo';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VersioneModuloIf} from '../../../../model/dto/versione-modulo-if';
import {MoonboblService} from '../../../../services/moonbobl.service';
import {NgxSpinnerService} from 'ngx-spinner';
import {ErrorNotificationService} from '../../../../services/error-notification.service';
import {InitNuovaVersioneModuloIf} from '../../../../model/dto/init-nuova-versione-modulo-if';
import {MoonboError} from '../../../../model/common/moonbo-error';
import { faSave, faHome } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app/services/shared.service';
import { MsgCercaIstanza, MsgVersioni } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-nuova-versione',
  templateUrl: './nuova-versione-modulo.component.html',
  styleUrls: ['./nuova-versione-modulo.component.scss']
})

export class NuovaVersioneModuloComponent implements OnInit {

  @Input() modulo: Modulo;
  @Output() backEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output('alertService') alert = new EventEmitter();

  frmNuovaVersione: FormGroup;
  elencoVersioni: VersioneModuloIf[];
  datiInitnuovoaVersione: InitNuovaVersioneModuloIf;

  isAdmin = false;
  faSave = faSave;
  faHome = faHome;
  modeError = false;

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private sharedService: SharedService,
    private spinnerService: NgxSpinnerService,
    private errNotificationError: ErrorNotificationService) {

    // let string patternVersion = "^[0-9]*";
    const patternVersion = '^[0-9]{1,2}$';
    this.frmNuovaVersione = fb.group({
      versionePartenza: ['', Validators.required],
      maior: ['', [Validators.required,
                  Validators.minLength(1),
                  Validators.maxLength(2),
                  Validators.pattern('[0-9]{1,2}')]
      ],
      minor: ['', [Validators.required,
        Validators.minLength(1),
        Validators.maxLength(2),
        Validators.pattern('[0-9]{1,2}')]
      ],
      patch: ['', [Validators.required,
        Validators.minLength(1),
        Validators.maxLength(3),
        Validators.pattern('[0-9]{1,3}')]]
    });

    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    // carico elenco Versioni
    this.spinnerService.show();
    this.moonboblService.initNuovaVersioneModulo(this.modulo).subscribe(
        (init: InitNuovaVersioneModuloIf) => {
          this.datiInitnuovoaVersione = init;
          this.elencoVersioni = this.datiInitnuovoaVersione.versioni;
          if (this.elencoVersioni.length === 1 ) {
            this.frmNuovaVersione.controls.versionePartenza.setValue(this.elencoVersioni[0].idVersioneModulo);
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


  }

  onSubmit() {
    this.moonboblService.creaNuovaVersione(this.modulo,
                                            this.versionePartenza.value,
                                            this.maior.value + '.' +  this.minor.value + '.' + this.patch.value
        ).subscribe(
        () => {
            // alert ('Nuova versione creata correttamente');
         
            // this.backEvent.emit('back');
            this.spinnerService.show();
            this.backEvent.emit('save');
            this.alert.emit({ text: MsgVersioni.SUCCESS_NUOVA_VERSIONE, type: 'success', autoclose:true});
        } ,
        (err: MoonboError) => {
          // informazioni sulla chiamata
          // this.errNotificationError.notification.next(err);

          // alert(err.errorMsg);
          this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
          this.modeError = true;
        }
    );

  }

  back() {
    if (this.modeError) {
      // reset error
      this.errNotificationError.notification.next(null);
    }

    this.spinnerService.show();
    this.backEvent.emit('back');
  }


  // getter

  get versionePartenza() {
    return this.frmNuovaVersione.controls['versionePartenza'];
  }


  get maior() {
    return this.frmNuovaVersione.controls['maior'];
  }

  get minor() {
    return this.frmNuovaVersione.controls['minor'];
  }

  get patch() {
    return this.frmNuovaVersione.controls['patch'];
  }

   checkNewVersion(): boolean {
    try {
          const maiorUltimaVersione = this.datiInitnuovoaVersione?.ultimaMaggioreVersione.toString().padStart(2, '0');
          const minorUltimaVersione = this.datiInitnuovoaVersione?.ultimaMinoreVersione.toString().padStart(2, '0');
          const patchUltimaversione = this.datiInitnuovoaVersione?.ultimaPatchVersione.toString().padStart(3, '0');
          const numberUltimaVersione = parseInt(maiorUltimaVersione + minorUltimaVersione + patchUltimaversione, 10);

          const maiorNuovaVersione = this.maior.value.padStart(2, '0');
          const minorNuovaVersione = this.minor.value.padStart(2, '0');
          const patchNuovaVersione = this.patch.value.padStart(3, '0');
          const numberNuovaVersione = parseInt(maiorNuovaVersione + minorNuovaVersione + patchNuovaVersione, 10);
          console.log(numberUltimaVersione);
          console.log(numberNuovaVersione);
          console.log(numberNuovaVersione > numberUltimaVersione);
          return numberNuovaVersione > numberUltimaVersione;
    } catch (e) {
      console.log (e);
    }


  }



}
