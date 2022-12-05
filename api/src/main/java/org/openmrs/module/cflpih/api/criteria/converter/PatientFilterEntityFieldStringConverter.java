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
import org.openmrs.module.cflpih.api.criteria.EntityFieldCondition;
import org.openmrs.module.cflpih.api.dto.AdHocMessagePatientFilterDTO;

import java.util.Collections;
import java.util.List;

public class PatientFilterEntityFieldStringConverter
    extends AbstractFieldValueStaticOperatorConverter {

  @Override
  protected List<Condition<?, ?>> createConditions(AdHocMessagePatientFilterDTO dto) {
    return Collections.singletonList(
        new EntityFieldCondition(getFieldPath(), getOperator(), dto.getValue()));
  }
}
