/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export const uniqueByProperty =
  <T>(arr: Array<T>, objKey: keyof T) =>
    arr.reduce<Array<T>>((acc, curr) =>
      acc.some(a => a[objKey] === curr[objKey])
        ? acc
        : [...acc, curr], []);

export const uniqueByPropMaxValue =
  <T>(arr: Array<T>, objKey1: keyof T, objKey2: keyof T) =>
    arr.sort((a, b) => (b[objKey2] > a[objKey2]) ? 1 : -1).reduce<Array<T>>((acc, curr) =>
      acc.some(a => a[objKey1] === curr[objKey1])
        ? acc
        : [...acc, curr]
      , []);

export const logByKeyValue =
  <T>(arr: Array<T>, objKey: keyof T, objValue: keyof T) => (arr.forEach(e => console.log(e[objKey] + ' - ' + e[objValue])))

export const orderByProperty =
  <T>(arr: Array<T>, objKey: keyof T) => (
    arr.sort((a, b) => (a[objKey] > b[objKey]) ? 1 : -1)
  )

export const isJsonEqual = (obj1, obj2) => {
  
  let keys1 = Object.keys(obj1);
  let keys2 = Object.keys(obj2);

  //return true when the two json has same length and all the properties has same value key by key
  return keys1.length === keys2.length && Object.keys(obj1).every(key => obj1[key] == obj2[key]);
}
