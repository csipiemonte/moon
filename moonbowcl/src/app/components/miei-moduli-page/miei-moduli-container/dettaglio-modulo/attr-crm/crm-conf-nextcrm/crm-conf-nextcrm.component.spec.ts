/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrmConfNextcrmComponent } from './crm-conf-nextcrm.component';

describe('CrmConfNextcrmComponent', () => {
  let component: CrmConfNextcrmComponent;
  let fixture: ComponentFixture<CrmConfNextcrmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrmConfNextcrmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CrmConfNextcrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
