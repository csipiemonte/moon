/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.dto;

import java.util.Objects;

public class EmailRequest   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  
	private boolean pec;
	private String to = null;
	private String cc = null;
	private String bcc = null;
	private String subject = null;
	private String text = null;
	private String html = null;
	private EmailAttachment attachment = null;
	private ContextRequest context = null;

	public boolean isPec() {
		return pec;
	}
	public void setPec(boolean pec) {
		this.pec = pec;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String contentHtml) {
		this.html = contentHtml;
	}
    public EmailAttachment getAttachment() {
		return attachment;
	}
	public void setAttachment(EmailAttachment attachment) {
		this.attachment = attachment;
	}
	public ContextRequest getContext() {
		return context;
	}
	public void setContext(ContextRequest context) {
		this.context = context;
	}
	
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailRequest emailReq = (EmailRequest) o;
    return
        Objects.equals(pec, emailReq.pec) &&
    	Objects.equals(to, emailReq.to) &&
    	Objects.equals(cc, emailReq.cc) &&
    	Objects.equals(bcc, emailReq.bcc) &&
    	Objects.equals(subject, emailReq.subject) &&
        Objects.equals(text, emailReq.text) &&
        Objects.equals(html, emailReq.html) &&
        Objects.equals(attachment, emailReq.attachment) &&
        Objects.equals(context, emailReq.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pec, to, cc, bcc, subject, text, html, attachment, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmailRequest {\n");
    sb.append("    pec: ").append(toIndentedString(pec)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    cc: ").append(toIndentedString(cc)).append("\n");
    sb.append("    bcc: ").append(toIndentedString(bcc)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

