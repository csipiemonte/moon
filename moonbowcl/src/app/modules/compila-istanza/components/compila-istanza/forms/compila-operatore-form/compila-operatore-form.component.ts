/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Identita } from 'src/app/common/identita';

@Component({
  selector: 'app-compila-operatore-form',
  templateUrl: './compila-operatore-form.component.html',
  styleUrls: ['./compila-operatore-form.component.scss']
})
export class CompilaOperatoreFormComponent implements OnInit {

  @Input() form: string;

  Identita = Identita;
  compilaOperatoreForm: FormGroup = new FormGroup({});

  patternPIvaCF: string = '^([0-9]{11})|([A-Za-z]{6}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{3}[A-Za-z]{1})$';
  patternCF: string = '^[A-Za-z]{6}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{2}[A-Za-z]{1}[0-9LMNPQRSTUV]{3}[A-Za-z]{1}$';

  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.createForm(this.form);
  }

  private createForm(form) {

    if (form === Identita.CF) {
      this.compilaOperatoreForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required, Validators.pattern(this.patternCF), Validators.minLength(16), Validators.maxLength(16)]]
      });
    }
    else if (form ===  Identita.CF_NOME_COGNOME) {
      this.compilaOperatoreForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required, Validators.pattern(this.patternPIvaCF), Validators.minLength(11), Validators.maxLength(16)]],
        //nome:["", [Validators.required]],
        //cognome: ["", [Validators.required]]
        nome:[""],
        cognome: [""]
      });
    }
    else if (form === Identita.CF_PIVA) {
      this.compilaOperatoreForm = this.formBuilder.group({
        cf_pIva: ["", [Validators.required, Validators.pattern(this.patternPIvaCF), Validators.minLength(11), Validators.maxLength(16)]]
      });
    }
  }

  submitForm() {
    if (this.compilaOperatoreForm.invalid) {
      return;
    }
    else {
      this.activeModal.close(this.compilaOperatoreForm.value);
    }
  }

  onSubmit() {
    this.submitForm();
  }

  reset() {
    this.compilaOperatoreForm.reset();
  }

}
