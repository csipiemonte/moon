/**
 * date-fns-date-time-adapter.class
 */

 import { Inject, Injectable, InjectionToken, Optional } from '@angular/core';
 import { addDays, addMonths, addYears, differenceInCalendarDays, format, getDate, getDay, getDaysInMonth, getHours, getMinutes, getMonth, getSeconds, getTime, getYear, isDate, isEqual, isSameDay, isValid, Locale, parse, parseISO, setHours, setMinutes, setSeconds, toDate } from 'date-fns';
 import { enUS, it } from 'date-fns/locale';
 import { DateTimeAdapter, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';
 
 /**
  * Matches strings that have the form of a valid RFC 3339 string
  * (https://tools.ietf.org/html/rfc3339). Note that the string may not actually be a valid date
  * because the regex will match strings an with out of bounds month, date, etc.
  */
 const ISO_8601_REGEX = /^\d{4}-\d{2}-\d{2}(?:T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|(?:(?:\+|-)\d{2}:\d{2}))?)?$/;
 
 export interface DateFnsOptions {
   locale?: Locale;
   weekStartsOn?: 0 | 1 | 2 | 3 | 4 | 5 | 6;
   firstWeekContainsDate?: 1 | 2 | 3 | 4 | 5 | 6 | 7;
   useAdditionalWeekYearTokens?: boolean;
   useAdditionalDayOfYearTokens?: boolean;
 }

 /** Configurable options for {@see DateFnsDateAdapter}. */
 export interface OwlDateFnsDateTimeAdapterOptions extends DateFnsOptions {
   getInstance: Function;
 }
 
 export const getOwlDateFnsDateTimeAdapterOptionsInstance = function (): DateFnsOptions {
   var _this = Object.assign({}, this);
   delete _this.getInstance;
   return _this;
 };
 
 /** InjectionToken for date-fns date adapter to configure options. */
 export const OWL_DATEFNS_DATE_TIME_ADAPTER_OPTIONS = new InjectionToken<OwlDateFnsDateTimeAdapterOptions>(
   'OWL_DATEFNS_DATE_TIME_ADAPTER_OPTIONS', {
     providedIn: 'root',
     factory: OWL_DATEFNS_DATE_TIME_ADAPTER_OPTIONS_FACTORY
   }
 );
 
 /** @docs-private */
 export function OWL_DATEFNS_DATE_TIME_ADAPTER_OPTIONS_FACTORY(): OwlDateFnsDateTimeAdapterOptions {
   return {
     locale: it,
     getInstance: getOwlDateFnsDateTimeAdapterOptionsInstance
   };
 }
 
 /** Creates an array and fills it with values. */
 function range<T>(length: number, valueFunction: (index: number) => T): T[] {
   const valuesArray = Array(length);
   for (let i = 0; i < length; i++) {
     valuesArray[i] = valueFunction(i);
   }
   return valuesArray;
 }
 
 
 @Injectable()
 export class DateFnsDateTimeAdapter extends DateTimeAdapter<Date> {
 
   private _localeData: {
     longMonths: string[],
     shortMonths: string[],
     narrowMonths: string[],
     longDaysOfWeek: string[],
     shortDaysOfWeek: string[],
     narrowDaysOfWeek: string[],
     dates: string[],
   };
 
   constructor(@Optional() @Inject(OWL_DATE_TIME_LOCALE) private owlDateTimeLocale: string, 
       @Optional() @Inject(OWL_DATEFNS_DATE_TIME_ADAPTER_OPTIONS) private options: OwlDateFnsDateTimeAdapterOptions) {
     super();
     this.setLocale(owlDateTimeLocale, options);
   }
 
   public setLocale(locale: string, options?: OwlDateFnsDateTimeAdapterOptions) {
 
     super.setLocale(locale);
 
     if (!!options) {
       if (!this.options) {
         this.options = options;
       } else {
         for (const key in options) {
           this.options[key] = options[key];
         }
       }
     }
     this.options.getInstance = getOwlDateFnsDateTimeAdapterOptionsInstance;
 
     const dateFnsLocaleData = this.options.locale.localize;
     this._localeData = {
       longMonths: range(12, (i) => dateFnsLocaleData.month(i, { width: 'wide' })),
       shortMonths: range(12, (i) => dateFnsLocaleData.month(i, { width: 'abbreviated' })),
       narrowMonths: range(12, (i) => dateFnsLocaleData.month(i, { width: 'narrow' })),
       longDaysOfWeek: range(7, (i) => dateFnsLocaleData.day(i, { width: 'wide' })),
       shortDaysOfWeek: range(7, (i) => dateFnsLocaleData.day(i, { width: 'abbreviated' })),
       narrowDaysOfWeek: range(7, (i) => dateFnsLocaleData.day(i, { width: 'short' })),
       dates: range(31, (i) => String(this.getDate(this.createDate(2017, 0, i + 1)))),
     };
   }
 
   public getYear(date: Date): number {
     return getYear(this.clone(date));
   }
 
   public getMonth(date: Date): number {
     return getMonth(this.clone(date));
   }
 
   public getDay(date: Date): number {
     return getDay(this.clone(date));
   }
 
   public getDate(date: Date): number {
     return getDate(this.clone(date));
   }
 
   public getHours(date: Date): number {
     return getHours(this.clone(date));
   }
 
   public getMinutes(date: Date): number {
     return getMinutes(this.clone(date));
   }
 
   public getSeconds(date: Date): number {
     return getSeconds(this.clone(date));
   }
 
   public getTime(date: Date): number {
     return getTime(this.clone(date));
   }
 
   public getNumDaysInMonth(date: Date): number {
     return getDaysInMonth(this.clone(date));
   }
 
   public differenceInCalendarDays(dateLeft: Date, dateRight: Date): number {
     return differenceInCalendarDays(dateLeft, dateRight);
   }
 
   public getYearName(date: Date): string {
     return String(format(date, 'yyyy', this.options.getInstance()));
   }
 
   public getMonthNames(style: 'long' | 'short' | 'narrow'): string[] {
     switch (style) {
       case 'narrow': return this._localeData.narrowMonths;
       case 'short': return this._localeData.shortMonths;
       default: return this._localeData.longMonths;
     }
   }
 
   public getDayOfWeekNames(style: 'long' | 'short' | 'narrow'): string[] {
     switch (style) {
       case 'narrow': return this._localeData.narrowDaysOfWeek;
       case 'short': return this._localeData.shortDaysOfWeek;
       default: return this._localeData.longDaysOfWeek;
     }
   }
 
   public getDateNames(): string[] {
     return this._localeData.dates;
   }
 
   public toIso8601(date: Date): string {
     return this.clone(date).toISOString();
   }
 
   public isEqual(dateLeft: Date, dateRight: Date): boolean {
 
     if (dateLeft && dateRight) {
       return isEqual(this.clone(dateLeft), this.clone(dateRight));
     }
 
     return dateLeft === dateRight;
   }
 
   public isSameDay(dateLeft: Date, dateRight: Date): boolean {
 
     if (dateLeft && dateRight) {
       return isSameDay(this.clone(dateLeft), this.clone(dateRight));
     }
 
     return dateLeft === dateRight;
   }
 
   public isValid(date: Date): boolean {
     return isValid(this.clone(date));
   }
 
   public invalid(): Date {
     return new Date(NaN);
   }
 
   public isDateInstance(obj: any): boolean {
     return isDate(obj);
   }
 
   public addCalendarYears(date: Date, amount: number): Date {
     return addYears(this.clone(date), amount);
   }
 
   public addCalendarMonths(date: Date, amount: number): Date {
     return addMonths(this.clone(date), amount);
   }
 
   public addCalendarDays(date: Date, amount: number): Date {
     return addDays(this.clone(date), amount);
   }
 
   public setHours(date: Date, amount: number): Date {
     return setHours(this.clone(date), amount);
   }
 
   public setMinutes(date: Date, amount: number): Date {
     return setMinutes(this.clone(date), amount);
   }
 
   public setSeconds(date: Date, amount: number): Date {
     return setSeconds(this.clone(date), amount);
   }
 
   public createDate(year: number, month: number, date: number): Date;
   public createDate(year: number, month: number, date: number, hours: number = 0, minutes: number = 0, seconds: number = 0): Date {
     if (month < 0 || month > 11) {
       throw Error(`Invalid month index "${month}". Month index should be between 0 and 11.`);
     }
 
     if (date < 1) {
       throw Error(`Invalid date "${date}". Date should be greater than 0.`);
     }
 
     if (hours < 0 || hours > 23) {
       throw Error(`Invalid hours "${hours}". Hours should be between 0 and 23.`);
     }
 
     if (minutes < 0 || minutes > 59) {
       throw Error(`Invalid minutes "${minutes}". Minutes should between 0 and 59.`);
     }
 
     if (seconds < 0 || seconds > 59) {
       throw Error(`Invalid seconds "${seconds}". Seconds should be between 0 and 59.`);
     }
 
     const result = this.createDateWithOverflow(year, month, date, hours, minutes, seconds);
 
     // If the result isn't valid, the date must have been out of bounds for this month.
     if (!this.isValid(result)) {
       throw Error(`Invalid date "${date}" for month with index "${month}".`);
     }
 
     return result;
   }
 
   public clone(date: Date | number | string): Date {
     return typeof date === 'string' ? parseISO(date) : toDate(date);
   }
 
   public now(): Date {
     return new Date();
   }
 
   public format(date: Date, displayFormat: any): string {
     date = this.clone(date);
     if (!this.isValid(date)) {
       throw Error('DateFnsDateTimeAdapter: Cannot format invalid date.');
     }
     return format(date, displayFormat, this.options.getInstance());
   }
 
   public parse(value: any, parseFormat: any): Date | null {
     if (value && typeof value === 'string' && parseFormat) {
       if (typeof parseFormat === 'string') {
         parseFormat = parseFormat.split('||');
       }
       if (!Array.isArray(parseFormat)) {
         throw Error('DateFnsDateTimeAdapter: Invalid date parse format string set: ' + JSON.stringify(parseFormat));
       }
 
       for (let i = 0; i < parseFormat.length; i++) {
         const parsedDate = parse(value, parseFormat[i].trim(), new Date(), this.options.getInstance());
         if (this.isValid(parsedDate)) {
           return parsedDate;
         }
       }
     }
     return value ? this.clone(value) : null;
   }
 
   /**
    * Returns the given value if given a valid DateFns or null. Deserializes valid ISO 8601 strings
    * (https://www.ietf.org/rfc/rfc3339.txt) and valid Date objects into valid DateFnss and empty
    * string into null. Returns an invalid date for all other values.
    */
   deserialize(value: any): Date | null {
     let date;
     if (typeof value === 'string') {
       if (!value) {
         return null;
       }
       date = this.parse(value, null);
     } else {
       date = this.clone(value);
     }
     if (date && this.isValid(date)) {
       return date;
     }
     return super.deserialize(value);
   }
 
   /**
    * Creates a date but allows the month and date to overflow.
    * @param {number} year
    * @param {number} month
    * @param {number} date
    * @param {number} hours -- default 0
    * @param {number} minutes -- default 0
    * @param {number} seconds -- default 0
    * @returns The new date, or null if invalid.
    * */
   private createDateWithOverflow(year: number, month: number, date: number, hours: number = 0, minutes: number = 0, seconds: number = 0): Date {
     const result = new Date(year, month, date, hours, minutes, seconds);
 
     if (year >= 0 && year < 100) {
       result.setFullYear(this.getYear(result) - 1900);
     }
     return result;
   }
   
 }
 