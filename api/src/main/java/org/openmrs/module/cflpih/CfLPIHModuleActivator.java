/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.cflpih.util.GlobalPropertyUtils;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class CfLPIHModuleActivator extends BaseModuleActivator {

  private static final Log LOGGER = LogFactory.getLog(CfLPIHModuleActivator.class);

  @Override
  public void started() {
    createGlobalProperties();
    LOGGER.info("CfL for PIH Started.");
  }

  @Override
  public void stopped() {
    LOGGER.info("CfL for PIH stopped.");
  }

  private void createGlobalProperties() {
    GlobalPropertyUtils.createGlobalSettingIfNotExists(
        CflPIHConstants.PERSON_IDENTIFIER_ATTRIBUTE_KEY,
        CflPIHConstants.PERSON_IDENTIFIER_ATTRIBUTE_DEFAULT_VALUE,
        CflPIHConstants.PERSON_IDENTIFIER_ATTRIBUTE_DESCRIPTION);
    GlobalPropertyUtils.createGlobalSettingIfNotExists(
        CflPIHConstants.PERSON_LOCATION_ATTRIBUTE_KEY,
        CflPIHConstants.PERSON_LOCATION_ATTRIBUTE_DEFAULT_VALUE,
        CflPIHConstants.PERSON_LOCATION_ATTRIBUTE_DESCRIPTION);
    GlobalPropertyUtils.createGlobalSettingIfNotExists(
        CflPIHConstants.AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_KEY,
        CflPIHConstants.AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_DEFAULT_VALUE,
        CflPIHConstants.AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_DESCRIPTION);
  }
}
