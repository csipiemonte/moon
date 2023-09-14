/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import { faSync, faEdit} from "@fortawesome/free-solid-svg-icons";
import { VersioneStato } from 'src/app/model/dto/versione-stato';
import { SharedService } from 'src/app/services/shared.service';
import {ObserveService} from '../../../services/observe.service';
@Component({
  selector: 'app-tree-grid-op',
  templateUrl: './tree-grid-op.component.html',
  styleUrls: ['./tree-grid-op.component.scss']
})
export class TreeGridOpComponent implements OnInit {
  @Input() cell_value: any;
  @Input() column: any;
  @Input() row_data: VersioneStato;

  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;

  faSync = faSync;
  faEdit = faEdit;

  constructor(private _observeService: ObserveService,
            private sharedService: SharedService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
    console.log('Costruttore custom component');
  }

  ngOnInit(): void {
    // tslint:disable-next-line:max-line-length
    console.log('ngOnInit() idVersioneModulo=' + this.row_data.idVersioneModulo + '  versioneModulo=' + this.row_data.versioneModulo + ' codice=' + this.row_data.codice);
    // tslint:disable-next-line:max-line-length
    if (this.isAdmin || this.isOpAdmin ||
      ((this.row_data.codice === 'INIT' || this.row_data.codice === 'TST' || this.row_data.codice === 'MOD')
        && this.sharedService.UserLogged.hasRuoloOperatorADV())) {
      // console.log('ngOnInit() dataFineValidita=' + this.row_data.dataFineValidita);
      if (!this.row_data.dataFineValidita) {
          this.isModificabile = true;
      }
    }
  }

  cambiaStato() {
    this._observeService.sendClickCambiaStatoOnTreeGridOperation(this.row_data);
  }

  editFormIo() {
    this._observeService.sendClickEditOnTreeGridOperation(this.row_data);
  }
}
