/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RigaIstanzeLavorazioneComponent } from './riga-istanze-lavorazione.component';

describe('RigaIstanzeLavorazioneComponent', () => {
  let component: RigaIstanzeLavorazioneComponent;
  let fixture: ComponentFixture<RigaIstanzeLavorazioneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RigaIstanzeLavorazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RigaIstanzeLavorazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
