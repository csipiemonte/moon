/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogMydocsComponent } from './log-mydocs.component';

describe('LogMydocsComponent', () => {
  let component: LogMydocsComponent;
  let fixture: ComponentFixture<LogMydocsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LogMydocsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LogMydocsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
