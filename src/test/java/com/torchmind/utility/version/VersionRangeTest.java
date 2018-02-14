/*
 * Copyright 2018 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torchmind.utility.version;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Provides test cases for {@link VersionRange}.
 *
 * @author Johannes Donath
 */
@RunWith(MockitoJUnitRunner.class)
public class VersionRangeTest {

  @Mock
  private TestVersion startBound;
  @Mock
  private TestVersion endBound;
  @Mock
  private TestVersion containedVersion;
  @Mock
  private TestVersion olderVersion;
  @Mock
  private TestVersion newerVersion;

  /**
   * Prepares the environment for upcoming test cases.
   */
  @Before
  public void setup() {
    Mockito.when(this.startBound.newerThan(this.startBound))
        .thenReturn(false);
    Mockito.when(this.startBound.olderThan(this.startBound))
        .thenReturn(false);

    Mockito.when(this.startBound.newerThan(this.olderVersion))
        .thenReturn(true);
    Mockito.when(this.startBound.olderThan(this.olderVersion))
        .thenReturn(false);

    Mockito.when(this.startBound.newerThan(this.containedVersion))
        .thenReturn(false);
    Mockito.when(this.startBound.olderThan(this.containedVersion))
        .thenReturn(true);

    Mockito.when(this.startBound.newerThan(this.newerVersion))
        .thenReturn(false);
    Mockito.when(this.startBound.olderThan(this.newerVersion))
        .thenReturn(true);

    Mockito.when(this.endBound.newerThan(this.endBound))
        .thenReturn(false);
    Mockito.when(this.endBound.olderThan(this.endBound))
        .thenReturn(false);

    Mockito.when(this.endBound.newerThan(this.olderVersion))
        .thenReturn(true);
    Mockito.when(this.endBound.olderThan(this.olderVersion))
        .thenReturn(false);

    Mockito.when(this.endBound.newerThan(this.containedVersion))
        .thenReturn(true);
    Mockito.when(this.endBound.olderThan(this.containedVersion))
        .thenReturn(false);

    Mockito.when(this.endBound.newerThan(this.newerVersion))
        .thenReturn(false);
    Mockito.when(this.endBound.olderThan(this.newerVersion))
        .thenReturn(true);
  }

  /**
   * Tests {@link VersionRange#matches(Version)}.
   */
  @Test
  public void testContains() {
    VersionRange<TestVersion> range00 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(true).endBound(this.endBound).endInclusive(true)
        .build();
    VersionRange<TestVersion> range01 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(false).endBound(this.endBound)
        .endInclusive(true).build();
    VersionRange<TestVersion> range02 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(true).endBound(this.endBound)
        .endInclusive(false).build();
    VersionRange<TestVersion> range03 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(false).endBound(this.endBound)
        .endInclusive(false).build();

    {
      Assert.assertTrue(range00.matches(this.startBound));

      Assert.assertFalse(range00.matches(this.olderVersion));
      Assert.assertTrue(range00.matches(this.containedVersion));
      Assert.assertFalse(range00.matches(this.newerVersion));

      Assert.assertTrue(range00.matches(this.endBound));
    }

    {
      Assert.assertFalse(range01.matches(this.startBound));

      Assert.assertFalse(range01.matches(this.olderVersion));
      Assert.assertTrue(range01.matches(this.containedVersion));
      Assert.assertFalse(range01.matches(this.newerVersion));

      Assert.assertTrue(range01.matches(this.endBound));
    }

    {
      Assert.assertTrue(range02.matches(this.startBound));

      Assert.assertFalse(range02.matches(this.olderVersion));
      Assert.assertTrue(range02.matches(this.containedVersion));
      Assert.assertFalse(range02.matches(this.newerVersion));

      Assert.assertFalse(range02.matches(this.endBound));
    }

    {
      Assert.assertFalse(range03.matches(this.startBound));

      Assert.assertFalse(range03.matches(this.olderVersion));
      Assert.assertTrue(range03.matches(this.containedVersion));
      Assert.assertFalse(range03.matches(this.newerVersion));

      Assert.assertFalse(range03.matches(this.endBound));
    }
  }

  /**
   * Tests {@link VersionRange#matching(java.util.Collection)}.
   */
  @Test
  public void testMatching() {
    VersionRange<TestVersion> range00 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(true).endBound(this.endBound).endInclusive(true)
        .build();
    VersionRange<TestVersion> range01 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(false).endBound(this.endBound)
        .endInclusive(true).build();
    VersionRange<TestVersion> range02 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(true).endBound(this.endBound)
        .endInclusive(false).build();
    VersionRange<TestVersion> range03 = VersionRange.<TestVersion>builder()
        .startBound(this.startBound).startInclusive(false).endBound(this.endBound)
        .endInclusive(false).build();

    Set<TestVersion> versionSet = new HashSet<>(Arrays
        .asList(this.startBound, this.olderVersion, this.containedVersion, this.newerVersion,
            this.endBound));

    {
      Set<TestVersion> set = range00.matching(versionSet);

      Assert.assertEquals(3, set.size());
      Assert.assertTrue(set.contains(this.startBound));
      Assert.assertFalse(set.contains(this.olderVersion));
      Assert.assertTrue(set.contains(this.containedVersion));
      Assert.assertFalse(set.contains(this.newerVersion));
      Assert.assertTrue(set.contains(this.endBound));
    }

    {
      Set<TestVersion> set = range01.matching(versionSet);

      Assert.assertEquals(2, set.size());
      Assert.assertFalse(set.contains(this.startBound));
      Assert.assertFalse(set.contains(this.olderVersion));
      Assert.assertTrue(set.contains(this.containedVersion));
      Assert.assertFalse(set.contains(this.newerVersion));
      Assert.assertTrue(set.contains(this.endBound));
    }

    {
      Set<TestVersion> set = range02.matching(versionSet);

      Assert.assertEquals(2, set.size());
      Assert.assertTrue(set.contains(this.startBound));
      Assert.assertFalse(set.contains(this.olderVersion));
      Assert.assertTrue(set.contains(this.containedVersion));
      Assert.assertFalse(set.contains(this.newerVersion));
      Assert.assertFalse(set.contains(this.endBound));
    }

    {
      Set<TestVersion> set = range03.matching(versionSet);

      Assert.assertEquals(1, set.size());
      Assert.assertFalse(set.contains(this.startBound));
      Assert.assertFalse(set.contains(this.olderVersion));
      Assert.assertTrue(set.contains(this.containedVersion));
      Assert.assertFalse(set.contains(this.newerVersion));
      Assert.assertFalse(set.contains(this.endBound));
    }
  }

  /**
   * Provides an interface for testing purposes.
   */
  public interface TestVersion extends Version<TestVersion> {

  }
}
