/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.api.criteria.converter;

import org.openmrs.module.cflpih.api.criteria.Condition;
import org.openmrs.module.cflpih.api.dto.AdHocMessagePatientFilterDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

public abstract class AbstractFieldValueStaticOperatorConverter extends AbstractFieldValueConverter
    implements PatientFilterConverterWithOperator {

  private String operator;

  @Override
  public void initFilter(Map<String, Object> properties) {
    super.initFilter(properties);
    operator = (String) properties.get(OPERATOR_PROP);

    if (operator == null) {
      throw new IllegalArgumentException(
          "The following properties must not be empty: "
              + Arrays.asList(FIELD_PATH_PROP, OPERATOR_PROP));
    }
  }

  @Override
  public String getOperator() {
    return operator;
  }

  protected abstract List<Condition<?, ?>> createConditions(AdHocMessagePatientFilterDTO dto);

  @Override
  protected boolean isConfigurationInvalid() {
    return super.isConfigurationInvalid() || isBlank(operator);
  }
}
