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

import org.hibernate.transform.ResultTransformer;
import org.openmrs.OpenmrsObject;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.INDEX_NOT_FOUND;
import static org.apache.commons.lang3.ArrayUtils.indexOf;

/**
 * The FilteringResultTransformer Class.
 *
 * <p>The Filtering ResultTransformer applies {@link InMemoryCondition}s to the criteria result list
 * and reduces the list to elements which fulfills ALL of the conditions. The original order of
 * result items is retained.
 */
public class FilteringResultTransformer<E extends OpenmrsObject> implements ResultTransformer {

  private static final long serialVersionUID = 1711131935629296502L;

  private final Class<E> resultClass;

  private final String rootAlias;

  private final List<InMemoryCondition> conditions;

  public FilteringResultTransformer(
      final Class<E> resultClass,
      final String rootAlias,
      final List<InMemoryCondition> conditions) {
    this.resultClass = resultClass;
    this.rootAlias = rootAlias;
    this.conditions = conditions;
  }

  @Override
  public Object transformTuple(Object[] tuple, String[] aliases) {
    final int rootAliasIdx = indexOf(aliases, rootAlias);

    final Object transformed;

    if (rootAliasIdx == INDEX_NOT_FOUND) {
      transformed = tuple[0];
    } else {
      transformed = tuple[rootAliasIdx];
    }

    return transformed;
  }

  @Override
  public List transformList(final List items) {
    final List<E> result = new ArrayList<>(items.size());

    for (Object item : items) {
      final E entityItem = resultClass.cast(item);

      if (applyAllConditions(entityItem)) {
        result.add(entityItem);
      }
    }

    return result;
  }

  private boolean applyAllConditions(final E item) {
    for (InMemoryCondition condition : conditions) {
      if (!condition.applyCondition(item)) {
        return false;
      }
    }

    return true;
  }
}
