/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { RigaIstanzaDaCompletareComponent } from './riga-istanza-da-completare.component';

describe('RigaIstanzaDaCompletareComponent', () => {
  let component: RigaIstanzaDaCompletareComponent;
  let fixture: ComponentFixture<RigaIstanzaDaCompletareComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ RigaIstanzaDaCompletareComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RigaIstanzaDaCompletareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
