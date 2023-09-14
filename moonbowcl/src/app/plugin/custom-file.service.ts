/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Formio } from 'formiojs';
import { CustomUrlProvider } from './CustomUrlProvider';
import { Injectable } from '@angular/core';


@Injectable({
    providedIn: 'root'
})
export class CustomFileService {


    constructor( private urlProvider: CustomUrlProvider) {}

    myFormio: any = Formio;

    async uploadFile(storage, fileToUpload, uniqueFileName, fileDir, progressCallback, url, options, fileKey) {
        const requestArgs = {
            provider: storage,
            method: 'upload',
            file: fileToUpload,
            fileName: uniqueFileName,
            dir: fileDir
        };
        fileKey = fileKey || 'file';
        const request = Formio.pluginWait('preRequest', requestArgs).then(() => {
            return Formio.pluginGet('fileRequest', requestArgs).then((result) => {

                if (storage && isNil(result)) {
                    if (storage === 'url') {
                        const provider = this.urlProvider;
                        return provider.uploadFile(fileToUpload, uniqueFileName, fileDir, progressCallback, url, options, fileKey);
                    } else {
                        const Provider = this.myFormio.Providers.getProvider('storage', storage);
                        if (Provider) {
                            const provider = Provider(Formio);
                            return provider.uploadFile(fileToUpload, uniqueFileName, fileDir, progressCallback, url, options, fileKey);
                        } else {
                            throw new Error('Storage provider not found');
                        }
                    }
                }

                return result || {
                    url: ''
                };
            });
        });
        return Formio.pluginAlter('wrapFileRequestPromise', request, requestArgs);
    }


    deleteFile(fileInfo) {
        console.log(fileInfo);
        if(fileInfo.storage =='url'){
            this.myFormio.makeRequest('','', fileInfo.url, 'delete',fileInfo);
        }
        
    }


    async downloadFile(fileInfo, options) {
        console.log('FileInfo: ', fileInfo);
        console.log('Options: ', options);
        const requestArgs = {
            method: 'download',
            file: fileInfo
        };
        const request = Formio.pluginWait('preRequest', requestArgs).then(() => {
            return Formio.pluginGet('fileRequest', requestArgs).then((result) => {
                if (fileInfo.storage && isNil(result)) {
                    if (fileInfo.storage === 'url') {
                        const provider = this.urlProvider;
                        return provider.downloadFile(fileInfo, options);
                    } else {
                        const prov = this.myFormio.Providers;
                        const Provider = this.myFormio.Providers.getProvider('storage', fileInfo.storage);
                        if (Provider) {
                            const provider = Provider(Formio);
                            return provider.downloadFile(fileInfo, options);
                        } else {
                            throw new Error('Storage provider not found');
                        }
                    }
                }

                return result || {
                    url: ''
                };
            });
        });
        return Formio.pluginAlter('wrapFileRequestPromise', request, requestArgs);
    }

}

function isNil(val) {
    return val === null || val === undefined;
}
