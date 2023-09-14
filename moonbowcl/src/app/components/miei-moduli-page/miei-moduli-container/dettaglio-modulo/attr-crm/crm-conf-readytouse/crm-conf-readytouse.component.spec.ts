/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmConfReadytouseComponent } from './crm-conf-readytouse.component';

describe('CrmConfReadytouseComponent', () => {
  let component: CrmConfReadytouseComponent;
  let fixture: ComponentFixture<CrmConfReadytouseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrmConfReadytouseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmConfReadytouseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
