/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.util;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.cflpih.api.bean.AdHocMessageSummary;
import org.openmrs.module.cflpih.api.service.AdHocMessageService;
import org.openmrs.module.htmlformentry.CustomFormSubmissionAction;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;



public class SendMessageOnHtmlFormSubmissionAction implements CustomFormSubmissionAction {

    @Override
    public void applyAction(FormEntrySession session) {

        Patient p=session.getPatient();
        List<Patient> recipients=new ArrayList<Patient>();
        recipients.add(p);

        final Map<String, String> properties = new HashMap<>();
        properties.put("message", "Dear Patient "+p.getFamilyName()+", Your Appointment is schedule on ....");

        //SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy hh:mm");
        System.out.println(new Date());
        System.out.println(Collections.singleton("SMS"));
        System.out.println("Message: "+properties.get("message").toString());
        System.out.println("Patient: "+recipients.get(0).getFamilyName());

        AdHocMessageService adHocMessageService = Context.getService(AdHocMessageService.class);
        final AdHocMessageSummary summary =
                adHocMessageService.scheduleAdHocMessage(
                        new Date(),
                        Collections.singleton("SMS"),
                        properties,
                        recipients);
    }
}