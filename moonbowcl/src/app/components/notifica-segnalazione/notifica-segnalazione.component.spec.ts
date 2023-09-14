/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { NotificaSegnalazioneComponent } from './notifica-segnalazione.component';

describe('NotificaSegnalazioneComponent', () => {
  let component: NotificaSegnalazioneComponent;
  let fixture: ComponentFixture<NotificaSegnalazioneComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ NotificaSegnalazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificaSegnalazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
