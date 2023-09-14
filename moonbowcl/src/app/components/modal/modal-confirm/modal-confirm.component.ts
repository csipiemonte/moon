/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-modal-confirm',
  templateUrl: './modal-confirm.component.html'
})


export class ModalConfirmComponent {

  @Input() modal_titolo;
  @Input() modal_contenuto;

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit() {
  }

}
