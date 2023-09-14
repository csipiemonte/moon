import { defaultEnvironment } from 'src/environments/default.environment';
export const environment = {
  ...defaultEnvironment,
  production: true,
  beServer: '',
  devBackendServer: '',
  shibbolethSSOLogoutURL: '.....',
  onAppExitURL: '......',
  codiceEnte: '001272',
  identitaIrideParameter: '',
  simulatePortale: '',
  pageSize: 15,
  timeout: 50,
  pathAssets: '/moonbobl/assets',
  // pageSizeExport: 500,
  apiUrlIncludeContext: false,
  pagingSizeExport: [250, 500, 750, 1000, 1250, 1500, 1750, 2000],
  moduliDettaglioTabEnable: {
    notificatore: true,
    protocollo: true,
    wfCosmo: true,
    wfAzioni: true,
    estraiDich: true,
    crm: true,
    epay: true
  }
};
