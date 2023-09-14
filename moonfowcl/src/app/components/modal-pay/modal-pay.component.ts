/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal-pay',
  templateUrl: './modal-pay.component.html',
  styleUrls: ['./modal-pay.component.scss']
})
export class ModalPayComponent implements OnInit {

  @Input() modal_titolo;
  @Input() modal_contenuto;
  @Input() modal_switch_label;

  checkSelected : boolean = false;

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
    //console.log(`disableConfirm = ${!this.checkSelected}`)
  }

}
