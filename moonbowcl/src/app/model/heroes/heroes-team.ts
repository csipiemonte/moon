/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */


/**
 * DTO relativo al ComplexType HeroesTeam.
 * @generated 
 */

import {Hero} from '../heroes/hero';
import {UserInfo} from '../common/user-info';



export class HeroesTeam  {

	name : string = null;

	master : Hero = null;

	members : Array<Hero> = null;

	modifiedBy : UserInfo = null;
}
