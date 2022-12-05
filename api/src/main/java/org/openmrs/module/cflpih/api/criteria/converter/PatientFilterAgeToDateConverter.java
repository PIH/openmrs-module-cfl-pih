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
import org.openmrs.module.cflpih.util.ConversionUtils;
import org.openmrs.module.sms.api.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PatientFilterAgeToDateConverter extends AbstractFieldValueConverter {

  private final TimeZone defaultUserTimezone;

  public PatientFilterAgeToDateConverter() {
    this.defaultUserTimezone = DateUtil.getLocalTimeZone();
  }

  @Override
  protected List<Condition<?, ?>> createConditions(AdHocMessagePatientFilterDTO dto) {
    final Number fromAge = ConversionUtils.toNumber(dto.getValue());
    final Number toAge = ConversionUtils.toNumber(dto.getSecondValue());

    final List<Condition<?, ?>> results = new ArrayList<>();
    if (toAge != null) {
      results.add(
          new EntityFieldCondition(getFieldPath(), ">=", convertAgeToDate(toAge.intValue() + 1)));
    }
    if (fromAge != null) {
      results.add(
          new EntityFieldCondition(getFieldPath(), "<=", convertAgeToDate(fromAge.intValue())));
    }
    return results;
  }

  private Date convertAgeToDate(final int age) {
    final Calendar calendar = Calendar.getInstance(defaultUserTimezone);
    calendar.add(Calendar.YEAR, -age);
    return calendar.getTime();
  }
}
