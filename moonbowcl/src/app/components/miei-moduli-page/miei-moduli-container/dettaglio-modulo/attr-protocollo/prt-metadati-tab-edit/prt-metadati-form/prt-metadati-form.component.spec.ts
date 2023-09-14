/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrtMetadatiFormComponent } from './prt-metadati-form.component';

describe('PrtMetadatiFormComponent', () => {
  let component: PrtMetadatiFormComponent;
  let fixture: ComponentFixture<PrtMetadatiFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrtMetadatiFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrtMetadatiFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
