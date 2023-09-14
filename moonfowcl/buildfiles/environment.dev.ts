import {defaultEnvironment} from 'src/environments/default.environment';

export const environment = {
  ...defaultEnvironment,
  production: true,
  beServer: '',
  wsServer: '',
  devBackendServer: '',
  onAppExitURL: 'http://www.csi.it',
  identitaIrideParameter: '',
  simulatePortale: '',
  pageSize: 15,
  timeout: 50,
  pathAssets: '/moonfobl/assets',
  apiUrlIncludeContext: false,
  autoSaveInterval: 60000,
  urlPiemontePay: 'https://pay.sistemapiemonte.it/epayweb/home'
};

