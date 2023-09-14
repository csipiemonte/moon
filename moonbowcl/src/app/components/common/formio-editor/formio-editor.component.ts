/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {
  Component,
  EventEmitter,
  Injectable,
  Input,
  OnInit,
  Output,
} from "@angular/core";
import { Modulo } from "../../../model/dto/modulo";
import { faHome, faSave, faPlus } from "@fortawesome/free-solid-svg-icons";
import { ModalActionComponent } from "../../modal/modal-action/modal-action.component";
import { MoonboblService } from "../../../services/moonbobl.service";
import { NgxSpinnerService } from "ngx-spinner";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Subject } from "rxjs";
import { FormIOModulo } from "../../../model/dto/form-iomodulo";
import { FormioEditorOptions } from "./formio-editor-options";
import { FormioAppConfig } from "@formio/angular";
import { AppConfig } from "../../conf/formio-config";
import { AlertService } from "src/app/modules/alert";
import { MsgModulo } from "src/app/common/messaggi";
import { CustomComponent } from "src/app/model/dto/custom-component";
import { CustomFileService } from "src/app/plugin/custom-file.service";

/*
Componente che consente di modificare un modulo formio
Riceve in input entità Modulo di Moon da cui preleva id e versione
per recuperare il json del modulo
Output: backEvent : premuto taso back
 */

@Component({
  selector: "app-formio-editor",
  templateUrl: "./formio-editor.component.html",
  styleUrls: ["./formio-editor.component.scss"],
  providers: [{ provide: FormioAppConfig, useValue: AppConfig }],
})
export class FormioEditorComponent implements OnInit {

  FileService: CustomFileService;
  
  constructor(
    private moonservice: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private modalService: NgbModal,
    private customFileService: CustomFileService
  ) {
    this.FileService = customFileService;
  }

  @Input() modulo: Modulo;
  @Input() isUserAdmin: boolean;
  @Input() options: FormioEditorOptions;

  @Output() backEvent = new EventEmitter();
  @Output("alertService") alert = new EventEmitter();

  formioOptions: any = {};

  opt: FormioEditorOptions;

  // modulo che viene modificato
  moduloDaEditare: Modulo = new Modulo();
  // json passato a formio e corrisponde alla struttura del modulo
  moduloFormIO: any = {};
  resetFormioEditor$ = new Subject<void>();
  isModuloModificabile: boolean;
  faHome = faHome;
  faSave = faSave;
  faPlus = faPlus;

  ngOnInit(): void {
    this.formioOptions = {
      builder: {
        hideDisplaySelect: true,
        output: {
          change: "onChange",
        },
        input: {
          options: {
            builder: {
              custom: {
                title: "Custom Fields",
                weight: 10,
                components: {},
              },
            },
          },
        },
      },
      renderer: {
        input: {
          submission: { data: {} },
          options: {
            renderOptions: {
              breadcrumbSettings: { clickable: true },
              language: "it",
              fileService: this.FileService,
            },
          },
        },
      },
    };

    this.moonservice.getFormioCustomComponents().subscribe((components) => {
      console.log("components = " + components);
      for (const component of components) {
        this.formioOptions.builder.input.options.builder.custom.components[
          component.idComponent
        ] = JSON.parse(component.jsonComponent);
      }
    });

    log("FormioEdito::ngOnInit() IN options=" + JSON.stringify(this.options));
    const optionsDefault = new FormioEditorOptions();
    if (this.options) {
      this.opt = Object.assign(optionsDefault, this.options);
    } else {
      this.opt = optionsDefault;
    }
    log("FormioEdito::ngOnInit() opt=" + JSON.stringify(this.opt));

    this.spinnerService.show();
    // recuperare il codice modulo
    this.moonservice
      .getModuloWithFields(
        this.modulo.idModulo,
        this.modulo.idVersioneModulo,
        "struttura"
      )
      .subscribe(
        (response) => {
          this.spinnerService.hide();
          this.moduloDaEditare = response as Modulo;
          this.moduloFormIO = new FormIOModulo();
          this.moduloFormIO = JSON.parse(response["struttura"]);

          console.log(
            "caricamento struttura miei moduli " +
              JSON.stringify(this.moduloFormIO)
          );
          this.resetFormioEditor$.next();
          // this.currentView = VIEWS.MODIFICA;
          if (
            this.isUserAdmin ||
            (response.stato && response.stato.codice === "INIT")
          ) {
            this.isModuloModificabile = true;
          }
        },
        (error) => {
          this.spinnerService.hide();
        }
      );
  }

  back() {
    if (this.isModuloModificabile) {
      const modalRef = this.modalService.open(ModalActionComponent);
      modalRef.componentInstance.modal_titolo = "MoonBackoffice";
      modalRef.componentInstance.modal_contenuto =
        "Vuoi salvare le eventuali modifiche effettuate ?";
      modalRef.componentInstance.modal_testo_conferma = "Salva ed esci";
      modalRef.componentInstance.modal_testo_annulla = "Esci senza salvare";
      modalRef.result.then(
        (result) => {
          console.log("Result:" + result);
          // this.closeResult = `Closed with: ${result}`;
          if (result === "OK") {
            console.log("salvo il modulo");
            this.salva();
          }
          this.backEvent.emit();
        },
        (reason) => {
          console.log("Reason: " + reason);
          if (reason === "KO") {
            this.backEvent.emit();
          }
          //this.backEvent.emit();
        }
      );
    } else {
      this.backEvent.emit();
    }
  }

  salva() {
    this.spinnerService.show();
    this.moduloDaEditare.struttura = JSON.stringify(this.moduloFormIO);
    this.moonservice.updateStrutturaModulo(this.moduloDaEditare).subscribe(
      (response) => {
        this.spinnerService.hide();
        // alert('Modulo Salvato correttamente');
        this.alert.emit({
          text: MsgModulo.SUCCESS_MODULO,
          type: "success",
          autoclose: true,
        });
        // this.backEvent.emit();
      },
      (error) => {
        this.spinnerService.hide();
        // alert('Errore Salvataggio Modulo ' + JSON.stringify(error));
        this.alert.emit({
          text: MsgModulo.ERROR_MODULO + ` ` + JSON.stringify(error),
          type: "error",
          autoclose: false,
        });
        // this.backEvent.emit();
      }
    );
  }

  reset() {
    this.resetFormioEditor$.next();
  }

  // fixme potrebbe non servire
  onChange(changed: any) {
    console.log(changed);
  }
}

function log(a: any) {
  console.log(a);
}
