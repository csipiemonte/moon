/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, AfterViewInit, ViewChild } from '@angular/core';
import {FormBuilderComponent, FormioAppConfig} from 'angular-formio';
import Formio from 'formiojs/Formio';



@Component({
  selector: 'app-formio-builder',
  templateUrl: './formio-builder.component.html',
  styleUrls: ['./formio-builder.component.scss']
})
export class FormioBuilderComponent implements AfterViewInit {
  public form: object = {components: []};
  public options: any = {
    builder: {
      basic: false,
      advanced: false,
      data: false,
      custom: {
        title: 'Custom Components',
        default: true,
        weight: 10,
        components: {
          firstName: {
            title: 'First Name',
            key: 'firstName',
            icon: 'fa fa-terminal',
            schema: {
              label: 'First Name',
              type: 'textfield',
              key: 'firstName',
              input: true
            }
          },
          lastName: {
            title: 'Last Name',
            key: 'lastName',
            icon: 'fa fa-terminal',
            schema: {
              label: 'Last Name',
              type: 'textfield',
              key: 'lastName',
              input: true
            }
          },
          email: {
            title: 'Email',
            key: 'email',
            icon: 'fa fa-at',
            schema: {
              label: 'Email',
              type: 'email',
              key: 'email',
              input: true
            }
          }
        }
      }
    }
  };

  public builder: any = null;
  @ViewChild(FormBuilderComponent) formBuilder: FormBuilderComponent;

  constructor(public config: FormioAppConfig) { }

  ngAfterViewInit() {
    this.formBuilder.ready.then((formio) => {
      this.builder = formio;
    });
  }

  saveForm() {
    const title: string = document.getElementById('title').textContent;
    const form = this.builder.schema;
    form.title = title;
    form.path = title.replace(/[^A-Za-z]*/g, '').toLowerCase();
    form.name = title.replace(/[^A-Za-z]*/g, '').toLowerCase();
    (new Formio(this.config.appUrl)).saveForm(form).then(function(savedForm) {
      console.log(savedForm);
    });
  }

}
