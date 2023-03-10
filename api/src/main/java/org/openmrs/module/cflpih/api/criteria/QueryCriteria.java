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
import org.openmrs.OpenmrsObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * The QueryCriteria Class.
 *
 * <p>The Query Criteria allows to build queries in a dynamic way.
 */
public class QueryCriteria<E extends OpenmrsObject> extends BaseCriteria {

  private static final long serialVersionUID = 6652974685352147059L;

  private final Class<E> resultClass;

  private transient List<HibernateCondition> hibernateConditions;

  private transient List<InMemoryCondition> inMemoryConditions;

  QueryCriteria(
      final Class<E> resultClass,
      final List<HibernateCondition> hibernateConditions,
      final List<InMemoryCondition> inMemoryConditions) {
    this.resultClass = resultClass;
    this.hibernateConditions = unmodifiableList(hibernateConditions);
    this.inMemoryConditions = unmodifiableList(inMemoryConditions);
  }

  public static <E extends OpenmrsObject> QueryCriteria<E> fromConditions(
      final Class<E> resultClass, final List<Condition> conditions) {
    final List<HibernateCondition> tmpHibernateConditions = new ArrayList<>();
    final List<InMemoryCondition> tmpInMemoryConditions = new ArrayList<>();

    for (Condition condition : conditions) {
      if (condition instanceof HibernateCondition) {
        tmpHibernateConditions.add((HibernateCondition) condition);
      } else if (condition instanceof InMemoryCondition) {
        tmpInMemoryConditions.add((InMemoryCondition) condition);
      } else {
        throw new IllegalStateException(
            "Unsupported Condition class: " + condition.getClass().getName());
      }
    }

    return new QueryCriteria<E>(resultClass, tmpHibernateConditions, tmpInMemoryConditions);
  }

  public static <E extends OpenmrsObject> QueryCriteria<E> fromConditions(
      final Class<E> resultClass, final Condition... conditions) {
    return fromConditions(resultClass, Arrays.asList(conditions));
  }

  @Override
  public void loadHibernateCriteria(Criteria hibernateCriteria) {
    // nothing to do here
  }

  @Override
  public void initHibernateCriteria(Criteria hibernateCriteria) {
    final String rootAlias = hibernateCriteria.getAlias();

    for (HibernateCondition hibernateCondition : hibernateConditions) {
      hibernateCondition.applyCondition(hibernateCriteria);
    }

    // The conditions above can add Joins, which means new aliases are added, it would result in:
    // select root, join1,
    // join2, we want here to transform the result to return only the root AND filter by
    // inMemoryConditions
    hibernateCriteria.setResultTransformer(
        new FilteringResultTransformer<E>(resultClass, rootAlias, inMemoryConditions));
  }
}
