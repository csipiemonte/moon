/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal-confirm',
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.scss']
})


export class ModalConfirmComponent {
  @Input() msgContenuto;
  @Input() modal_titolo;
  @Input() modal_contenuto;
  @Input() exit = false;
  @Input() exitMessage = `Esci`;
  @Input() exitCallback : Function;

  constructor(public activeModal: NgbActiveModal) {
  }

  doExit(){
    if(this.exit){
      this.exitCallback();
      this.activeModal.dismiss();
    }
  }
}
