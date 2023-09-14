/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-modal-action',
  templateUrl: './modal-action.component.html',
  styleUrls: ['./modal-action.component.scss']
})
export class ModalActionComponent implements OnInit {

  @Input() modal_titolo;
  @Input() modal_contenuto;
  @Input() modal_testo_conferma = 'Conferma';
  @Input() modal_testo_annulla = 'Annulla';

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

}
