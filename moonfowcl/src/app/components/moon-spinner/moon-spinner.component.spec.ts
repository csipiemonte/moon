/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MoonSpinnerComponent} from './moon-spinner.component';

describe('MoonSpinnerComponent', () => {
  let component: MoonSpinnerComponent;
  let fixture: ComponentFixture<MoonSpinnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MoonSpinnerComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MoonSpinnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
