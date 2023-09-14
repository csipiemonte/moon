/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.dto;

import java.util.Objects;
import java.util.UUID;

public class Message   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
  private UUID uuid = null;
  private AnyType payload = null;
  private String expireAt = null;


  // nome originario nello yaml: uuid 
  public UUID getUuid() {
    return uuid;
  }
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

   // nome originario nello yaml: payload 
  public AnyType getPayload() {
    return payload;
  }
  public void setPayload(AnyType payload) {
    this.payload = payload;
  }

  /**
   * date of expire message in iso 8601 format, default value is 5 days after submit date message. (i.e 2019-05-02T14:00:00)
   **/
  


  // nome originario nello yaml: expire_at 
  public String getExpireAt() {
    return expireAt;
  }
  public void setExpireAt(String expireAt) {
    this.expireAt = expireAt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;
    return Objects.equals(uuid, message.uuid) &&
        Objects.equals(payload, message.payload) &&
        Objects.equals(expireAt, message.expireAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, payload, expireAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Message {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    expireAt: ").append(toIndentedString(expireAt)).append("\n");
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

