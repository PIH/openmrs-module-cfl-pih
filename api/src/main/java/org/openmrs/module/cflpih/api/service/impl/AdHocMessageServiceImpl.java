/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.api.service.impl;

import org.openmrs.Patient;
import org.openmrs.module.cflpih.api.bean.AdHocMessageSummary;
import org.openmrs.module.cflpih.api.bean.AdHocSMSDataListBuilder;
import org.openmrs.module.cflpih.api.service.AdHocMessageService;
import org.openmrs.module.sms.api.data.AdHocSMSData;
import org.openmrs.module.sms.api.service.ConfigService;
import org.openmrs.module.sms.api.service.ScheduleAdHocSMSesService;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdHocMessageServiceImpl implements AdHocMessageService {
  private ScheduleAdHocSMSesService scheduleAdHocSMSesService;
  private ConfigService smsConfigService;

  @Override
  public AdHocMessageSummary scheduleAdHocMessage(
      Date deliveryDateTime,
      Set<String> channelTypes,
      Map<String, String> messageProperties,
      Collection<Patient> patients) {

    for (String channelType : channelTypes) {
      if (!"SMS".equalsIgnoreCase(channelType)) {
        throw new IllegalStateException(
            "Unsupported channel type for AdHos message: " + channelType);
      }

      scheduleSMS(deliveryDateTime, messageProperties, patients);
    }

    return new AdHocMessageSummary(patients.size());
  }

  public void setScheduleAdHocSMSesService(ScheduleAdHocSMSesService scheduleAdHocSMSesService) {
    this.scheduleAdHocSMSesService = scheduleAdHocSMSesService;
  }

  public void setSmsConfigService(ConfigService smsConfigService) {
    this.smsConfigService = smsConfigService;
  }

  private void scheduleSMS(
      Date deliveryDateTime, Map<String, String> messageProperties, Collection<Patient> patients) {
    final List<AdHocSMSData> smsDatas =
        new AdHocSMSDataListBuilder(smsConfigService)
            .setDeliveryDate(deliveryDateTime)
            .setMessageProperties(messageProperties)
            .setPatients(patients)
            .build();

    scheduleAdHocSMSesService.scheduleAdHocSMSes(smsDatas);
  }
}
