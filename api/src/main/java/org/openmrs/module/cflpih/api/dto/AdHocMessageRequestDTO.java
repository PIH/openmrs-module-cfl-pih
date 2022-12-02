/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.api.dto;

import org.springframework.util.AutoPopulatingList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdHocMessageRequestDTO {

  private static final String MESSAGE_TEMPLATE_DEFAULT = "Simple message here.";

  private Date deliveryDateTime;

  private String messageTemplate;

  private List<AdHocMessagePatientFilterDTO> filters;

  public AdHocMessageRequestDTO() {
    this.messageTemplate = MESSAGE_TEMPLATE_DEFAULT;
    this.filters = new AutoPopulatingList<>(AdHocMessagePatientFilterDTO.class);
  }

  public void applyFromOther(AdHocMessageRequestDTO otherDto) {
    setDeliveryDateTime(otherDto.getDeliveryDateTime());
    setMessageTemplate(otherDto.getMessageTemplate());

    for (int filterIdx = 0; filterIdx < getFilters().size(); ++filterIdx) {
      getFilters().get(filterIdx).copyValue(otherDto.getFilters().get(filterIdx));
    }
  }

  public Map<String, String> getProperties() {
    final Map<String, String> properties = new HashMap<>();
    properties.put("message", messageTemplate);
    return properties;
  }

  public Date getDeliveryDateTime() {
    return deliveryDateTime;
  }

  public void setDeliveryDateTime(Date deliveryDateTime) {
    this.deliveryDateTime = deliveryDateTime;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }

  public void setMessageTemplate(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  public List<AdHocMessagePatientFilterDTO> getFilters() {
    return filters;
  }

  public void setFilters(List<AdHocMessagePatientFilterDTO> filters) {
    this.filters = filters;
  }
}
