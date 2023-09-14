/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { AttrWfAzioniComponent } from './attr-wf-azioni.component';

describe('AttrWfAzioniComponent', () => {
  let component: AttrWfAzioniComponent;
  let fixture: ComponentFixture<AttrWfAzioniComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AttrWfAzioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AttrWfAzioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
