/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ExportIstanzeComponent } from './export-istanze.component';

describe('ExportIstanzeComponent', () => {
  let component: ExportIstanzeComponent;
  let fixture: ComponentFixture<ExportIstanzeComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ExportIstanzeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExportIstanzeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
