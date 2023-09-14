import {defaultEnvironment} from 'src/environments/default.environment';

export const environment = {
  ...defaultEnvironment,
  production: false,
  beServer: '',
  wsServer: 'ws://....',
  devBackendServer: 'http://.....',
  onAppExitURL: 'http://......',
  identitaIrideParameter: '...',
  simulatePortale: '*',
  pageSize: 15,
  timeout: 50,
  pathAssets: './assets',
  apiUrlIncludeContext: false,
  autoSaveInterval: 60000
};

