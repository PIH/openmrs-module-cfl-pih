/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at http://license.openmrs.org
 *
 * <p>Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF
 * ANY KIND, either express or implied. See the License for the specific language governing rights
 * and limitations under the License.
 *
 * <p>Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.cflpih;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.Extension.MEDIA_TYPE;
import org.openmrs.module.cflpih.extension.html.AdminList;

import java.util.Map;

/** This test validates the AdminList extension class */
public class AdminListExtensionTest {

  /** Get the links for the extension class */
  @Ignore
  @Test
  public void testValidatesLinks() {
    AdminList ext = new AdminList();

    Map<String, String> links = ext.getLinks();

    Assert.assertNotNull("Some links should be returned", links);

    Assert.assertTrue("There should be a positive number of links", links.values().size() > 0);
  }

  /** Check the media type of this extension class */
  @Ignore
  @Test
  public void testMediaTypeIsHtml() {
    AdminList ext = new AdminList();

    Assert.assertTrue(
        "The media type of this extension should be html",
        ext.getMediaType().equals(MEDIA_TYPE.html));
  }
}