/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RigaIstanzePervenuteComponent } from './riga-istanze-pervenute.component';

describe('RigaIstanzePervenuteComponent', () => {
  let component: RigaIstanzePervenuteComponent;
  let fixture: ComponentFixture<RigaIstanzePervenuteComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RigaIstanzePervenuteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RigaIstanzePervenuteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
