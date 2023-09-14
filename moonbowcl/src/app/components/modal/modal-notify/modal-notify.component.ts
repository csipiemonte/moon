/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal-notify',
  templateUrl: './modal-notify.component.html',
  styleUrls: ['./modal-notify.component.css']
})


export class ModalNotifyComponent {
  @Input()  msgContenuto;
  @Input() modal_titolo;
  @Input() modal_contenuto;

  constructor(public activeModal: NgbActiveModal) {
  }
}
