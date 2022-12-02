/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.web.controller;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflpih.api.bean.AdHocMessageSummary;
import org.openmrs.module.cflpih.api.bean.PatientFilterConfiguration;
import org.openmrs.module.cflpih.api.criteria.Condition;
import org.openmrs.module.cflpih.api.criteria.QueryCriteria;
import org.openmrs.module.cflpih.api.criteria.converter.PatientFilterConverter;
import org.openmrs.module.cflpih.api.dao.PatientAdvancedDao;
import org.openmrs.module.cflpih.api.dto.AdHocMessagePatientFilterDTO;
import org.openmrs.module.cflpih.api.dto.AdHocMessageRequestDTO;
import org.openmrs.module.cflpih.api.dto.PersonDTO;
import org.openmrs.module.cflpih.api.dto.PersonDTOBuilder;
import org.openmrs.module.cflpih.api.service.AdHocMessageService;
import org.openmrs.module.cflpih.api.service.ConfigService;
import org.openmrs.module.cflpih.web.model.AdHocMessageControllerModel;
import org.openmrs.propertyeditor.LocationEditor;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/** The AdHocMessageController Class. */
@Controller("cflpih.AdHocMessageController")
@RequestMapping(value = "module/cflpih/adHocMessage.form")
public class AdHocMessageController {

  private static final String VIEW = "/module/cflpih/adHocMessage";

  private static final String MODEL_NAME = "model";

  private static final String FILTER_ACTION = "filter";

  private static final String SEND_ACTION = "send";

  private static final int PATIENT_OVERVIEW_MAX_SIZE = 500;

  @Autowired private AdHocMessageService adHocMessageService;

  @Autowired private PatientAdvancedDao patientAdvancedDao;

  @Autowired private ConfigService configService;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    binder.registerCustomEditor(
        java.util.Date.class, new CustomDateEditor(Context.getDateTimeFormat(), true));
    binder.registerCustomEditor(Location.class, new LocationEditor());
  }

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView onGet() {
    final AdHocMessageControllerModel emptyModel = new AdHocMessageControllerModel();
    emptyModel.setMessageRequest(getEmptyMessageRequest());
    return new ModelAndView(VIEW, MODEL_NAME, emptyModel);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ModelAndView onPost(HttpServletRequest servletRequest, AdHocMessageControllerModel model) {
    if (servletRequest.getParameter(FILTER_ACTION) != null) {
      return onFilterAction(model);
    } else if (servletRequest.getParameter(SEND_ACTION) != null) {
      return onSendAction(servletRequest, model);
    } else {
      return new ModelAndView(VIEW);
    }
  }

  private ModelAndView onFilterAction(AdHocMessageControllerModel model) {
    final List<Condition> allPatientConditions = getAllPatientConditions(model.getMessageRequest());
    final List<Patient> recipients =
        patientAdvancedDao.getPatients(
            0,
            PATIENT_OVERVIEW_MAX_SIZE,
            QueryCriteria.fromConditions(Patient.class, allPatientConditions));

    final List<PersonDTO> recipientDTOs = new ArrayList<>();
    for (Patient recipient : recipients) {
      recipientDTOs.add(new PersonDTOBuilder().withPerson(recipient).build());
    }

    final AdHocMessageRequestDTO emptyMessageRequest = getEmptyMessageRequest();
    emptyMessageRequest.applyFromOther(model.getMessageRequest());
    return new ModelAndView(
        VIEW, MODEL_NAME, new AdHocMessageControllerModel(emptyMessageRequest, recipientDTOs));
  }

  private ModelAndView onSendAction(
      HttpServletRequest servletRequest, AdHocMessageControllerModel model) {
    final List<Condition> allPatientConditions = getAllPatientConditions(model.getMessageRequest());
    final List<Patient> recipients =
        patientAdvancedDao.getPatients(
            0, -1, QueryCriteria.fromConditions(Patient.class, allPatientConditions));

    final AdHocMessageSummary summary =
        adHocMessageService.scheduleAdHocMessage(
            model.getMessageRequest().getDeliveryDateTime(),
            Collections.singleton("SMS"),
            model.getMessageRequest().getProperties(),
            recipients);

    servletRequest
        .getSession()
        .setAttribute(WebConstants.OPENMRS_MSG_ATTR, "cfl.adHocMessage.send.success.info");
    servletRequest
        .getSession()
        .setAttribute(
            WebConstants.OPENMRS_MSG_ARGS, singletonList(summary.getTotalNumberOfPatients()));
    return onGet();
  }

  private List<Condition> getAllPatientConditions(AdHocMessageRequestDTO model) {
    final List<PatientFilterConfiguration> configurations =
        configService.getAdHocMessagePatientFilterConfigurations();

    final List<Condition> allPatientConditions = new ArrayList<>();

    for (int filterIdx = 0; filterIdx < configurations.size(); filterIdx++) {
      final PatientFilterConfiguration configuration = configurations.get(filterIdx);
      final AdHocMessagePatientFilterDTO dto = model.getFilters().get(filterIdx);

      final PatientFilterConverter converter = configuration.getFilterConverter();
      converter.initFilter(configuration.getConfig());

      allPatientConditions.addAll(converter.convert(dto));
    }

    return allPatientConditions;
  }

  private AdHocMessageRequestDTO getEmptyMessageRequest() {
    final List<AdHocMessagePatientFilterDTO> configuredFilters = new ArrayList<>();

    for (PatientFilterConfiguration configuration :
        configService.getAdHocMessagePatientFilterConfigurations()) {
      configuredFilters.add(
          new AdHocMessagePatientFilterDTO(
              configuration.getLabel(),
              configuration.getInputType().toString(),
              configuration.getConfig()));
    }

    final AdHocMessageRequestDTO dto = new AdHocMessageRequestDTO();
    dto.setFilters(configuredFilters);
    return dto;
  }
}
