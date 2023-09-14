import {defaultEnvironment} from './default.environment';

export const environment = {
  ...defaultEnvironment,
  production: false,
  beServer: '',
  wsServer: 'ws://....',
  devBackendServer: 'http://......',
  onAppExitURL: 'http://...',
  codiceEnte: '001272',
  identitaIrideParameter: '...',
  contestoBackendService: '/moonfobl/restfacade/be/',
  simulatePortale: 'moon.csi.it', // MultiEnte
  pageSize: 15,
  timeout: 50,
  pathAssets: './assets',
  autoSaveInterval: 30000,
  apiUrlIncludeContext: false,
  urlPiemontePay: 'https://.....',
};

