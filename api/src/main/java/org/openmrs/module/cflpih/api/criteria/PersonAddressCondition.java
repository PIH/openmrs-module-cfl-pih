/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.cflpih.api.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.openmrs.PersonAddress;
import org.openmrs.module.cflpih.api.criterion.EntityFieldExpression;

import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * The PersonAddressCondition Class.
 *
 * <p>The Person Address Condition allows to make simple condition on the preferred address of a
 * Person.
 */
public class PersonAddressCondition extends BaseJoinHibernateCondition {

  private static final String PREFERRED_FIELD_SUFFIX = ".preferred";

  private final String addressPropertyName;

  private final Object value;

  public PersonAddressCondition(String addressPropertyName, String operator, Object value) {
    super(addressPropertyName, operator);
    this.addressPropertyName = addressPropertyName;
    this.value = value;
  }

  @Override
  public Void applyCondition(Criteria hibernateCriteria) {
    final DetachedCriteria conditionQuery =
        DetachedCriteria.forClass(PersonAddress.class, getAlias());
    conditionQuery.setProjection(Property.forName(PERSON_PROP));

    final Criterion joinOnClause =
        and(
            new EntityFieldExpression(getAlias() + "." + addressPropertyName, getOperator(), value),
            eq(getAlias() + VOIDED_FIELD_SUFFIX, Boolean.FALSE),
            eq(getAlias() + PREFERRED_FIELD_SUFFIX, Boolean.TRUE));
    conditionQuery.add(joinOnClause);

    hibernateCriteria.add(Property.forName(ID_PROP).in(conditionQuery));
    return null;
  }
}
