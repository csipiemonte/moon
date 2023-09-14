/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmConfOtrsComponent } from './crm-conf-otrs.component';

describe('CrmConfOtrsComponent', () => {
  let component: CrmConfOtrsComponent;
  let fixture: ComponentFixture<CrmConfOtrsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrmConfOtrsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmConfOtrsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
