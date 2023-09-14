/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import NativePromise from 'native-promise-only';
import { Injectable } from '@angular/core';
import { Formio } from 'formiojs';
import { HttpXsrfTokenExtractor } from '@angular/common/http';
import { StorageManager } from 'src/app/common/utils/storage-manager';
import { STORAGE_KEYS } from 'src/app/common/costanti';


const TOKEN_KEY = 'X-XSRF-TOKEN';
const FAKE_IDENTITY_KEY = 'Shib-Iride-IdentitaDigitale';

@Injectable({
    providedIn: 'root'
})
export class CustomUrlProvider {

    formio: any;
    constructor(private tokenExtractor: HttpXsrfTokenExtractor) {
        this.formio = Formio;
    }


    private async xhrRequest(url, name, query, data, options?, onprogress?) {
        return await new NativePromise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            const json = (typeof data === 'string');
            const fd = new FormData();
            if (typeof onprogress === 'function') {
                xhr.upload.onprogress = onprogress;
            }

            if (!json) {
                // tslint:disable-next-line:forin
                for (const key in data) {
                    fd.append(key, data[key]);
                }
            }

            xhr.onload = () => {
                if (xhr.status >= 200 && xhr.status < 300) {
                    // Need to test if xhr.response is decoded or not.
                    let respData: any = {};
                    try {
                        respData = (typeof xhr.response === 'string') ? JSON.parse(xhr.response) : {};
                        respData = (respData && respData.data) ? respData.data : respData;
                    } catch (err) {
                        respData = {};
                    }

                    // Get the url of the file.
                    let respUrl = respData.hasOwnProperty('url') ? respData.url : `${xhr.responseURL}/${name}`;

                    // If they provide relative url, then prepend the url.
                    if (respUrl && respUrl[0] === '/') {
                        respUrl = `${url}${respUrl}`;
                    }

                    //resolve({ url: respUrl, data: respData});

                    resolve({ url: respUrl, data: respData , response: xhr.response});
                } else {
                    reject(xhr.response || 'Unable to upload file');
                }
            };

            xhr.onerror = () => reject(xhr);
            xhr.onabort = () => reject(xhr);

            let requestUrl = url + (url.indexOf('?') > -1 ? '&' : '?');
            // tslint:disable-next-line:forin
            for (const key in query) {
                requestUrl += `${key}=${query[key]}&`;
            }
            if (requestUrl[requestUrl.length - 1] === '&') {
                requestUrl = requestUrl.substr(0, requestUrl.length - 1);
            }

            // xhr.open('POST', requestUrl);
            // if (json) {
            //     xhr.setRequestHeader('Content-Type', 'application/json');
            // }
            if (json) {
                xhr.open('GET', requestUrl);
                xhr.setRequestHeader('Content-Type', 'application/json');
                xhr.responseType = 'blob';
            }
            else{
                xhr.open('POST', requestUrl);
            }

            // const token = this.tokenExtractor.getToken() as string;

            const token = StorageManager.get(STORAGE_KEYS.MOON_JWT_TOKEN);
               
            if (token) {
                xhr.setRequestHeader('Moon-Identita-JWT', token);
            }
            // if (!environment.production) {
            //     xhr.setRequestHeader(FAKE_IDENTITY_KEY, environment.identitaIrideParameter);
            // }

            // Overrides previous request props
            if (options) {
                const parsedOptions = typeof options === 'string' ? JSON.parse(options) : options;
                // tslint:disable-next-line:forin
                for (const prop in parsedOptions) {
                    xhr[prop] = parsedOptions[prop];
                }
            }
            xhr.send(json ? data : fd);
        });
    }


    uploadFile(file, name, dir, progressCallback, url, options, fileKey) {
        const uploadRequest = (form?) => {
            return this.xhrRequest(url, name, {
                baseUrl: encodeURIComponent(this.formio.baseUrl),
                project: form ? form.project : '',
                form: form ? form._id : ''
            }, {
                [fileKey]: file,
                name,
                dir
            }, options, progressCallback).then(response => {
                // Store the project and form url along with the metadata.
                response.data = response.data || {};
                response.data.baseUrl = this.formio.projectUrl;
                response.data.project = form ? form.project : '';
                response.data.form = form ? form._id : '';
                return {
                    storage: 'url',
                    name,
                    url: response.url,
                    size: file.size,
                    type: file.type,
                    data: response.data
                };
            });
        };
        if (file.private && this.formio.formId) {
            return this.formio.loadForm().then((form) => uploadRequest(form));
        } else {
            return uploadRequest();
        }
    }


    deleteFile(fileInfo) {
        return new NativePromise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            xhr.setRequestHeader('pippo', 'pluto');
            xhr.open('DELETE', fileInfo.url, true);
            xhr.onload = () => {
                if (xhr.status >= 200 && xhr.status < 300) {
                    resolve('File deleted');
                } else {
                    reject(xhr.response || 'Unable to delete file');
                }
            };
            xhr.send(null);
        });
    }


    downloadFile(file, optionsa) {
        if (!file.private) {
            if (this.formio.submissionId && file.data) {
                file.data.submission = this.formio.submissionId;
            }
            //return this.xhrRequest(file.url, file.name, {}, JSON.stringify(file)).then(response => response.data);
            return this.xhrRequest(file.url, file.name, {}, JSON.stringify(file)).then(data =>
            {
                let a = document.createElement('a');
                a.href = window.URL.createObjectURL(data.response);
                // Give filename you wish to download
                a.download = file.originalName;
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
            });
        }

        // Return the original as there is nothing to do.
        return NativePromise.resolve(file);
    }

}
