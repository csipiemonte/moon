/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NuovaIstanzaComponent } from './nuova-istanza.component';

describe('NuovaIstanzaComponent', () => {
  let component: NuovaIstanzaComponent;
  let fixture: ComponentFixture<NuovaIstanzaComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NuovaIstanzaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuovaIstanzaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
