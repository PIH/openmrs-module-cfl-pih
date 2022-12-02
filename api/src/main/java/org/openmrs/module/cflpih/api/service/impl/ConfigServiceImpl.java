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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflpih.CflPIHConstants;
import org.openmrs.module.cflpih.api.bean.PatientFilterConfiguration;
import org.openmrs.module.cflpih.api.service.ConfigService;

import java.io.IOException;
import java.util.List;

public class ConfigServiceImpl implements ConfigService {

  @Override
  public List<PatientFilterConfiguration> getAdHocMessagePatientFilterConfigurations() {
    final String configurationJson =
        getGp(CflPIHConstants.AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_KEY);
    final ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(
          configurationJson, new TypeReference<List<PatientFilterConfiguration>>() {});
    } catch (IOException ioe) {
      throw new APIException(
          "Malformed content of parameter: "
              + CflPIHConstants.AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_KEY,
          ioe);
    }
  }

  private String getGp(String propertyName) {
    return Context.getAdministrationService().getGlobalProperty(propertyName);
  }
}
