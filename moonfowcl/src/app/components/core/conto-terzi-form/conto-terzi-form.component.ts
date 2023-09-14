/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

declare var $: any;

@Component({
  selector: 'app-conto-terzi-form',
  templateUrl: './conto-terzi-form.component.html',
  styleUrls: ['./conto-terzi-form.component.scss'],
})
export class ContoTerziFormComponent implements OnInit {
  @Input() form: string;

  contoTerziForm: FormGroup;

  patternPIvaCF: string = '^([0-9]{11})|([A-Za-z]{6}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{3}[A-Za-z]{1})$';
  patternCF: string = '^[A-Za-z]{6}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{3}[A-Za-z]{1}$';

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.createForm(this.form);
  }


  //validazione regexp su codice fiscale che gestisce le omocodie
  private createForm(form) {
    if (form === 'CONTO_TERZI_CF') {
      this.contoTerziForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required,Validators.pattern(this.patternCF),Validators.minLength(16),Validators.maxLength(16)]]
      });
    } else if(form === 'CONTO_TERZI_CF_PIVA'){
      this.contoTerziForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required, Validators.pattern(this.patternPIvaCF), Validators.minLength(11),Validators.maxLength(16)]]
      });
    } else if(form === 'CONTO_TERZI_CF_PIVA-IN_PROPRIO'){
      this.contoTerziForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required, Validators.pattern(/^[0-9]{11}$/), Validators.minLength(11),Validators.maxLength(11)]]
      });
    }
  }

  submitForm() {
    if (this.contoTerziForm.invalid) {
      return;
    } else {
      this.activeModal.close(this.contoTerziForm.value);
    }
  }

  onSubmit() {
    this.submitForm();
  }

  reset() {
    this.contoTerziForm.reset();
  }
}
