/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

/**
 * Handle optional inputs for channel configuration.
 */
$j(document).ready(function () {
  document.getElementById("send-btn").onclick = function (event) {
    const doSend = confirm($sendConfirmationMessage);
    if (!doSend) {
      event.preventDefault();
    }
  };

  /**
   * Initializes Patient Overview table.
   */
  $j("#patientOverview").dataTable({
    "bJQueryUI": true,
    "sPaginationType": "full_numbers",
    "bPaginate": true,
    "bAutoWidth": false,
    "bLengthChange": false,
    "bFilter": false,
    "iDisplayLength": 25,
    "aoColumns": [
      {"sClass": "identifierColumn", "bSortable": false},
      {"sClass": "givenNameColumn", "bSortable": false},
      {"sClass": "middleNameColumn", "bSortable": false},
      {"sClass": "familyNameColumn", "bSortable": false},
      {"sClass": "ageColumn", "bSortable": false},
      {"sClass": "genderColumn", "bSortable": false}
    ],
    "aaData": $patients,
    "sDom": '<"fg-toolbar ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
  });
});

