/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.dto.moonfobl;

import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * Risposta paginata
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class ResponsePaginated<T> {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private List<T> items;
  private Integer page;
  private Integer pageSize;
  private Integer totalElements;
  private Integer totalPages;


  /**
   * elenco degli elementi ricercati
   **/
  
  @ApiModelProperty(value = "elenco degli elementi ricercati")

  // nome originario nello yaml: items 
  public List<T> getItems() {
    return items;
  }
  public void setItems(List<T> items) {
    this.items = items;
  }


  /**
   * numero della page corrispondente all'elenco ritornata (0..n) *** zero-based ***
   **/
  
  @ApiModelProperty(value = "numero della page corrispondente all'elenco ritornata (0..n) *** zero-based ***")

  // nome originario nello yaml: page 
  public Integer getPage() {
    return page;
  }
  public void setPage(Integer page) {
    this.page = page;
  }


  /**
   * numero di elementi per pagina
   **/
  
  @ApiModelProperty(value = "numero di elementi per pagina")

  // nome originario nello yaml: pageSize 
  public Integer getPageSize() {
    return pageSize;
  }
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }


  /**
   * numero total di elementi
   **/
  
  @ApiModelProperty(value = "numero total di elementi")

  // nome originario nello yaml: totalElements 
  public Integer getTotalElements() {
    return totalElements;
  }
  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }


  /**
   * numero totale di pagine
   **/
  
  @ApiModelProperty(value = "numero totale di pagine")

  // nome originario nello yaml: totalPages 
  public Integer getTotalPages() {
    return totalPages;
  }
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

 
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponsePaginated s = (ResponsePaginated) o;
    return Objects.equals(items, s.items) &&
		Objects.equals(page, s.page) &&
		Objects.equals(pageSize, s.pageSize) &&
		Objects.equals(totalElements, s.totalElements) &&
		Objects.equals(totalPages, s.totalPages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, page, pageSize, totalElements, totalPages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponsePaginated {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(items)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(page)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(items)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

