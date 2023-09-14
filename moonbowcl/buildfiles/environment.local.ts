import { defaultEnvironment } from './default.environment';
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.


export const environment = {
  ...defaultEnvironment,
  production: false,
  beServer: '',
  devBackendServer: '',
  shibbolethSSOLogoutURL: '....',
  onAppExitURL: '.....',
  contestoBackendService: '/moonbobl/restfacade/be/',  
  identitaIrideParameter: '',
  simulatePortale: 'xxx.yyy',
  pagingSizeExport: [125, 200],
  pageSize: 15,
  timeout: 50,
  pathAssets: '../assets',
  apiUrlIncludeContext: false,
  moduliDettaglioTabEnable: {
    notificatore: true,
    protocollo: true,
    wfCosmo: true,
    wfAzioni: true,
    estraiDich: true,
    crm: true,
    epay: true
  },
  crmSystems: ['R2U', 'NEXTCRM']
};



/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */

