/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto.api;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="PostAzione da eseguire in seguito alla ricezione di un cambio di stato")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyEapServerCodegen")@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "postAzioneType", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = PostAzioneSendMail.class, name = "SEND_EMAIL"),
})

public class PostAzione   {
  

  /**
   * Gets or Sets postAzioneType
   */
  public enum PostAzioneTypeEnum {
    SEND_EMAIL("SEND_EMAIL");
    private String value;

    PostAzioneTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }


  private PostAzioneTypeEnum postAzioneType;

  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("postAzioneType")
  @NotNull
  public PostAzioneTypeEnum getPostAzioneType() {
    return postAzioneType;
  }
  public void setPostAzioneType(PostAzioneTypeEnum postAzioneType) {
    this.postAzioneType = postAzioneType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostAzione postAzione = (PostAzione) o;
    return Objects.equals(postAzioneType, postAzione.postAzioneType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(postAzioneType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostAzione {\n");
    
    sb.append("    postAzioneType: ").append(toIndentedString(postAzioneType)).append("\n");
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

