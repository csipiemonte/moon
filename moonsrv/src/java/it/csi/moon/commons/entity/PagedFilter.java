/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.entity;

/**
 * PagedFilter DTO usato per le ricerche paginate
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public abstract class PagedFilter {

	private static final long serialVersionUID = 1L;
	
	private Integer offset = 0;
	private Integer limit = 20;
	private Long total = null;
	private boolean usePagination = false;

	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		if (offset!=null && offset>=0) {
			this.offset = offset;
		}
	}

	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		if (limit!=null && limit>=1) {
			this.limit = limit;
		}
	}

	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}

	public boolean isUsePagination() {
		return usePagination;
	}
	public void setUsePagination(boolean usePagination) {
		this.usePagination = usePagination;
	}

}
