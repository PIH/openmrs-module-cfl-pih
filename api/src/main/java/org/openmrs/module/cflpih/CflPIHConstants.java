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

public class CflPIHConstants {
  public static final String TELEPHONE_NUMBER_PERSON_ATTRIBUTE_TYPE_NAME = "cfl.person.attributeType.telephoneNumber.name";

  public static final String PERSON_IDENTIFIER_ATTRIBUTE_KEY = "cfl.person.attribute.identifier";

  public static final String PERSON_IDENTIFIER_ATTRIBUTE_DEFAULT_VALUE =
      "ffb6b2bc-ac7b-4807-8afd-f9464cb14003";

  public static final String PERSON_IDENTIFIER_ATTRIBUTE_DESCRIPTION =
      "Used to specify the UUID of person attribute "
          + "type which will be used to store the additional person identifier value.";

  public static final String PERSON_LOCATION_ATTRIBUTE_KEY = "cfl.person.attribute.location";

  public static final String PERSON_LOCATION_ATTRIBUTE_DEFAULT_VALUE = "LocationAttribute";

  public static final String PERSON_LOCATION_ATTRIBUTE_DESCRIPTION =
      "Specifies attribute name used to store " + "information about person localization";

  public static final String AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_KEY =
      "cfl.adHocMessage.ui.configuration";

  public static final String AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_DESCRIPTION =
      "The Global Property with JSON configuration of the Patient filters available to be used to specify message "
          + "recipients.";

  public static final String AD_HOC_MESSAGE_PATIENT_FILTERS_CONFIGURATION_GP_DEFAULT_VALUE =
      "[\n"
          + "  {\n"
          + "    \"label\": \"cfl.adHocMessage.patientAddress.country\",\n"
          + "    \"inputType\": \"STRING\",\n"
          + "    \"converter\": \"PatientFilterAddressFieldStringConverter\",\n"
          + "    \"config\": \n"
          + "    {\n"
          + "      \"fieldPath\": \"country\",\n"
          + "      \"operator\": \"=\"\n"
          + "    }\n"
          + "  },\n"
          + "  {\n"
          + "    \"label\": \"cfl.adHocMessage.patientAddress.stateProvince\",\n"
          + "    \"inputType\": \"STRING\",\n"
          + "    \"converter\": \"PatientFilterAddressFieldStringConverter\",\n"
          + "    \"config\": \n"
          + "    {\n"
          + "      \"fieldPath\": \"stateProvince\",\n"
          + "      \"operator\": \"=\"\n"
          + "    }\n"
          + "  },\n"
          + "  {\n"
          + "    \"label\": \"cfl.adHocMessage.patient.gender\",\n"
          + "    \"inputType\": \"MULTI_SELECT_STRING\",\n"
          + "    \"converter\": \"PatientFilterStringListConverter\",\n"
          + "    \"config\": \n"
          + "    {\n"
          + "      \"fieldPath\": \"gender\",\n"
          + "      \"options\": [ \"M\", \"F\", \"O\" ]\n"
          + "    }\n"
          + "  },\n"
          + "  {\n"
          + "    \"label\": \"cfl.adHocMessage.patient.age\",\n"
          + "    \"inputType\": \"AGE_RANGE\",\n"
          + "    \"converter\": \"PatientFilterAgeToDateConverter\",\n"
          + "    \"config\": \n"
          + "    {\n"
          + "      \"fieldPath\": \"birthdate\"\n"
          + "    }\n"
          + "  }\n"
          + "]\n";

  private CflPIHConstants() {}
}
