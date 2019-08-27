/**
 * Copyright (C) 2015-2019 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.profile.cef;

import org.junit.ClassRule;
import org.junit.Test;

import com.helger.phase4.model.pmode.IPModeIDProvider;
import com.helger.photon.app.mock.PhotonAppWebTestRule;

/**
 * Test class for class {@link CEFPMode}.
 * 
 * @author Philip Helger
 */
public final class CEFPModeTest
{
  @ClassRule
  public static final PhotonAppWebTestRule s_aRule = new PhotonAppWebTestRule ();

  @Test
  public void testCEFPMode ()
  {
    CEFPMode.createCEFPMode ("TestInitiator",
                             "TestResponder",
                             "https://test.example.org",
                             IPModeIDProvider.DEFAULT_DYNAMIC,
                             false);
  }

  @Test
  public void testCEFPModeTwoWay ()
  {
    CEFPMode.createCEFPModeTwoWay ("TestInitiator",
                                   "TestResponder",
                                   "https://test.example.org",
                                   IPModeIDProvider.DEFAULT_DYNAMIC,
                                   false);
  }
}
