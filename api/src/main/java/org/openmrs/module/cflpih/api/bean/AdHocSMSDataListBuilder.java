/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.api.bean;

import org.openmrs.Patient;
import org.openmrs.module.cflpih.util.PersonUtil;
import org.openmrs.module.sms.api.data.AdHocSMSData;
import org.openmrs.module.sms.api.service.ConfigService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdHocSMSDataListBuilder {
  private static final String MESSAGE_TEXT_PROPERTY = "message";

  private final SimpleDateFormat smsAdHocContactTimeFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm");

  private String config;
  private Collection<Patient> patients = Collections.emptyList();
  private String contactTime;
  private String smsText;
  private Map<String, Object> parameters = Collections.emptyMap();

  public AdHocSMSDataListBuilder(ConfigService smsConfigService) {
    this.config = smsConfigService.getDefaultConfig().getName();
  }

  public AdHocSMSDataListBuilder setDeliveryDate(Date deliveryDate) {
    this.contactTime = smsAdHocContactTimeFormat.format(deliveryDate);
    return this;
  }

  public AdHocSMSDataListBuilder setMessageProperties(Map<String, String> messageProperties) {
    this.parameters =
        messageProperties.entrySet().stream()
            .filter(this::popMessageText)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return this;
  }

  public AdHocSMSDataListBuilder setPatients(Collection<Patient> patients) {
    this.patients = patients;
    return this;
  }

  public List<AdHocSMSData> build() {
    return patients.stream().map(this::newAdHocSMSData).collect(Collectors.toList());
  }

  private boolean popMessageText(Map.Entry<String, String> messagePropertyEntry) {
    if (messagePropertyEntry.getKey().equalsIgnoreCase(MESSAGE_TEXT_PROPERTY)) {
      this.smsText = messagePropertyEntry.getValue();
      return false;
    }

    return true;
  }

  private AdHocSMSData newAdHocSMSData(Patient patient) {
    final AdHocSMSData smsData = new AdHocSMSData();
    smsData.setConfig(config);
    smsData.setContactTime(contactTime);
    smsData.setParameters(parameters);
    smsData.setPhone(PersonUtil.getPhoneNumber(patient));
    smsData.setSmsText(smsText);
    return smsData;
  }
}
