/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ComunicazioniPageComponent} from './comunicazioni-page.component';

describe('ComunicazioniPageComponent', () => {
  let component: ComunicazioniPageComponent;
  let fixture: ComponentFixture<ComunicazioniPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComunicazioniPageComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComunicazioniPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
