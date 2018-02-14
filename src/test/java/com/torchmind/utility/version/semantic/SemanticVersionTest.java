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
package com.torchmind.utility.version.semantic;

import com.torchmind.utility.version.VersionRange;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Provides test cases for {@link SemanticVersion}.
 *
 * @author Johannes Donath
 */
@RunWith(MockitoJUnitRunner.class)
public class SemanticVersionTest {

  /**
   * Provides an assertion helper for {@link #testParse()}.
   *
   * @param version The version to assert on.
   * @param major The expected major bit.
   * @param minor The expected minor bit.
   * @param patch The expected patch bit.
   * @param extra The expected extra bit (if any).
   * @param metadata The expected metadata bit (if any).
   */
  private void assertVersion(@NonNull SemanticVersion version,
      int major,
      int minor,
      int patch,
      @Nullable String extra,
      @Nullable String metadata) {
    Assert.assertEquals(major, version.major());
    Assert.assertEquals(minor, version.minor());
    Assert.assertEquals(patch, version.patch());

    if (extra == null) {
      Assert.assertNull(version.extra());
    } else {
      Assert.assertEquals(extra, version.extra());
    }

    if (metadata == null) {
      Assert.assertNull(version.metadata());
    } else {
      Assert.assertEquals(metadata, version.metadata());
    }
  }

  /**
   * Tests {@link SemanticVersion#newerThan(SemanticVersion)} and {@link
   * SemanticVersion#olderThan(SemanticVersion)}.
   */
  @Test
  public void testCompare() {
    SemanticVersion version00 = SemanticVersion.builder().major(0).build(); // 0.0.0
    SemanticVersion version01 = SemanticVersion.builder().major(1).extra("snapshot")
        .build(); // 1.0.0-snapshot
    SemanticVersion version02 = SemanticVersion.builder().major(1).extra("snapshot.1")
        .build(); // 1.0.0-snapshot.1
    SemanticVersion version03 = SemanticVersion.builder().major(1).extra("snapshot.2")
        .build(); // 1.0.0-snapshot.2
    SemanticVersion version04 = SemanticVersion.builder().major(1).extra("snapshot.3")
        .build(); // 1.0.0-snapshot.3
    SemanticVersion version05 = SemanticVersion.builder().major(1).extra("alpha")
        .build(); // 1.0.0-alpha
    SemanticVersion version06 = SemanticVersion.builder().major(1).extra("alpha.1")
        .build(); // 1.0.0-alpha.1
    SemanticVersion version07 = SemanticVersion.builder().major(1).extra("alpha.2")
        .build(); // 1.0.0-alpha.2
    SemanticVersion version08 = SemanticVersion.builder().major(1).extra("alpha.3")
        .build(); // 1.0.0-alpha.3
    SemanticVersion version09 = SemanticVersion.builder().major(1).extra("beta")
        .build(); // 1.0.0-beta
    SemanticVersion version10 = SemanticVersion.builder().major(1).extra("beta.1")
        .build(); // 1.0.0-beta.1
    SemanticVersion version11 = SemanticVersion.builder().major(1).extra("beta.2")
        .build(); // 1.0.0-beta.2
    SemanticVersion version12 = SemanticVersion.builder().major(1).extra("beta.3")
        .build(); // 1.0.0-beta.3
    SemanticVersion version13 = SemanticVersion.builder().major(1).extra("rc").build(); // 1.0.0-rc
    SemanticVersion version14 = SemanticVersion.builder().major(1).extra("rc.1")
        .build(); // 1.0.0-rc.1
    SemanticVersion version15 = SemanticVersion.builder().major(1).extra("rc.2")
        .build(); // 1.0.0-rc.2
    SemanticVersion version16 = SemanticVersion.builder().major(1).extra("rc.3")
        .build(); // 1.0.0-rc.3
    SemanticVersion version17 = SemanticVersion.builder().major(1).build(); // 1.0.0

    SemanticVersion version18 = SemanticVersion.builder().patch(0).build(); // 0.0.0
    SemanticVersion version19 = SemanticVersion.builder().patch(1).build(); // 0.0.1
    SemanticVersion version20 = SemanticVersion.builder().patch(2).build(); // 0.0.2
    SemanticVersion version21 = SemanticVersion.builder().patch(3).build(); // 0.0.3

    SemanticVersion version22 = SemanticVersion.builder().minor(0).build(); // 0.0.0
    SemanticVersion version23 = SemanticVersion.builder().minor(1).build(); // 0.1.0
    SemanticVersion version24 = SemanticVersion.builder().minor(2).build(); // 0.2.0
    SemanticVersion version25 = SemanticVersion.builder().minor(3).build(); // 0.3.0

    SemanticVersion version26 = SemanticVersion.builder().major(0).build(); // 0.0.0
    SemanticVersion version27 = SemanticVersion.builder().major(1).build(); // 1.0.0
    SemanticVersion version28 = SemanticVersion.builder().major(2).build(); // 2.0.0
    SemanticVersion version29 = SemanticVersion.builder().major(3).build(); // 3.0.0

    SemanticVersion version30 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .build(); // 0.0.0-alpha
    SemanticVersion version31 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .build(); // 0.0.0
    SemanticVersion version32 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .build(); // 0.0.1
    SemanticVersion version33 = SemanticVersion.builder().major(0).minor(1).patch(0).extra(null)
        .build(); // 0.1.0
    SemanticVersion version34 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .build(); // 1.0.0

    {
      Assert.assertTrue(version00.olderThan(version01));
      Assert.assertFalse(version00.newerThan(version01));
      Assert.assertEquals(-1, version00.compareTo(version01));
      Assert.assertTrue(version01.newerThan(version00));
      Assert.assertFalse(version01.olderThan(version00));
      Assert.assertEquals(1, version01.compareTo(version00));

      Assert.assertTrue(version01.olderThan(version02));
      Assert.assertFalse(version01.newerThan(version02));
      Assert.assertEquals(-1, version01.compareTo(version02));
      Assert.assertTrue(version02.newerThan(version01));
      Assert.assertFalse(version02.olderThan(version01));
      Assert.assertEquals(1, version02.compareTo(version01));

      Assert.assertTrue(version02.olderThan(version03));
      Assert.assertFalse(version02.newerThan(version03));
      Assert.assertEquals(-1, version02.compareTo(version03));
      Assert.assertTrue(version03.newerThan(version02));
      Assert.assertFalse(version03.olderThan(version02));
      Assert.assertEquals(1, version03.compareTo(version02));

      Assert.assertTrue(version03.olderThan(version04));
      Assert.assertFalse(version03.newerThan(version04));
      Assert.assertEquals(-1, version03.compareTo(version04));
      Assert.assertTrue(version04.newerThan(version03));
      Assert.assertFalse(version04.olderThan(version03));
      Assert.assertEquals(1, version04.compareTo(version03));

      Assert.assertTrue(version04.olderThan(version05));
      Assert.assertFalse(version04.newerThan(version05));
      Assert.assertEquals(-1, version04.compareTo(version05));
      Assert.assertTrue(version05.newerThan(version04));
      Assert.assertFalse(version05.olderThan(version04));
      Assert.assertEquals(1, version05.compareTo(version04));

      Assert.assertTrue(version05.olderThan(version06));
      Assert.assertFalse(version05.newerThan(version06));
      Assert.assertEquals(-1, version05.compareTo(version06));
      Assert.assertTrue(version06.newerThan(version05));
      Assert.assertFalse(version06.olderThan(version05));
      Assert.assertEquals(1, version06.compareTo(version05));

      Assert.assertTrue(version06.olderThan(version07));
      Assert.assertFalse(version06.newerThan(version07));
      Assert.assertEquals(-1, version06.compareTo(version07));
      Assert.assertTrue(version07.newerThan(version06));
      Assert.assertFalse(version07.olderThan(version06));
      Assert.assertEquals(1, version07.compareTo(version06));

      Assert.assertTrue(version07.olderThan(version08));
      Assert.assertFalse(version07.newerThan(version08));
      Assert.assertEquals(-1, version07.compareTo(version08));
      Assert.assertTrue(version08.newerThan(version07));
      Assert.assertFalse(version08.olderThan(version07));
      Assert.assertEquals(1, version08.compareTo(version07));

      Assert.assertTrue(version08.olderThan(version09));
      Assert.assertFalse(version08.newerThan(version09));
      Assert.assertEquals(-1, version08.compareTo(version09));
      Assert.assertTrue(version09.newerThan(version08));
      Assert.assertFalse(version09.olderThan(version08));
      Assert.assertEquals(1, version09.compareTo(version08));

      Assert.assertTrue(version09.olderThan(version10));
      Assert.assertFalse(version09.newerThan(version10));
      Assert.assertEquals(-1, version09.compareTo(version10));
      Assert.assertTrue(version10.newerThan(version09));
      Assert.assertFalse(version10.olderThan(version09));
      Assert.assertEquals(1, version10.compareTo(version09));

      Assert.assertTrue(version10.olderThan(version11));
      Assert.assertFalse(version10.newerThan(version11));
      Assert.assertEquals(-1, version10.compareTo(version11));
      Assert.assertTrue(version11.newerThan(version10));
      Assert.assertFalse(version11.olderThan(version10));
      Assert.assertEquals(1, version11.compareTo(version10));

      Assert.assertTrue(version11.olderThan(version12));
      Assert.assertFalse(version11.newerThan(version12));
      Assert.assertEquals(-1, version11.compareTo(version12));
      Assert.assertTrue(version12.newerThan(version11));
      Assert.assertFalse(version12.olderThan(version11));
      Assert.assertEquals(1, version12.compareTo(version11));

      Assert.assertTrue(version12.olderThan(version13));
      Assert.assertFalse(version12.newerThan(version13));
      Assert.assertEquals(-1, version12.compareTo(version13));
      Assert.assertTrue(version13.newerThan(version12));
      Assert.assertFalse(version13.olderThan(version12));
      Assert.assertEquals(1, version13.compareTo(version12));

      Assert.assertTrue(version13.olderThan(version14));
      Assert.assertFalse(version13.newerThan(version14));
      Assert.assertEquals(-1, version13.compareTo(version14));
      Assert.assertTrue(version14.newerThan(version13));
      Assert.assertFalse(version14.olderThan(version13));
      Assert.assertEquals(1, version14.compareTo(version13));

      Assert.assertTrue(version14.olderThan(version15));
      Assert.assertFalse(version14.newerThan(version15));
      Assert.assertEquals(-1, version14.compareTo(version15));
      Assert.assertTrue(version15.newerThan(version14));
      Assert.assertFalse(version15.olderThan(version14));
      Assert.assertEquals(1, version15.compareTo(version14));

      Assert.assertTrue(version15.olderThan(version16));
      Assert.assertFalse(version15.newerThan(version16));
      Assert.assertEquals(-1, version15.compareTo(version16));
      Assert.assertTrue(version16.newerThan(version15));
      Assert.assertFalse(version16.olderThan(version15));
      Assert.assertEquals(1, version16.compareTo(version15));

      Assert.assertTrue(version16.olderThan(version17));
      Assert.assertFalse(version16.newerThan(version17));
      Assert.assertEquals(-1, version16.compareTo(version17));
      Assert.assertTrue(version17.newerThan(version16));
      Assert.assertFalse(version17.olderThan(version16));
      Assert.assertEquals(1, version17.compareTo(version16));
    }

    {
      Assert.assertTrue(version18.olderThan(version19));
      Assert.assertFalse(version18.newerThan(version19));
      Assert.assertEquals(-1, version18.compareTo(version19));
      Assert.assertTrue(version19.newerThan(version18));
      Assert.assertFalse(version19.olderThan(version18));
      Assert.assertEquals(1, version19.compareTo(version18));

      Assert.assertTrue(version19.olderThan(version20));
      Assert.assertFalse(version19.newerThan(version20));
      Assert.assertEquals(-1, version19.compareTo(version20));
      Assert.assertTrue(version20.newerThan(version19));
      Assert.assertFalse(version20.olderThan(version19));
      Assert.assertEquals(1, version20.compareTo(version19));

      Assert.assertTrue(version20.olderThan(version21));
      Assert.assertFalse(version20.newerThan(version21));
      Assert.assertEquals(-1, version20.compareTo(version21));
      Assert.assertTrue(version21.newerThan(version20));
      Assert.assertFalse(version21.olderThan(version20));
      Assert.assertEquals(1, version21.compareTo(version20));
    }

    {
      Assert.assertTrue(version22.olderThan(version23));
      Assert.assertFalse(version22.newerThan(version23));
      Assert.assertEquals(-1, version22.compareTo(version23));
      Assert.assertTrue(version23.newerThan(version22));
      Assert.assertFalse(version23.olderThan(version22));
      Assert.assertEquals(1, version23.compareTo(version22));

      Assert.assertTrue(version23.olderThan(version24));
      Assert.assertFalse(version23.newerThan(version24));
      Assert.assertEquals(-1, version23.compareTo(version24));
      Assert.assertTrue(version24.newerThan(version23));
      Assert.assertFalse(version24.olderThan(version23));
      Assert.assertEquals(1, version24.compareTo(version23));

      Assert.assertTrue(version24.olderThan(version25));
      Assert.assertFalse(version24.newerThan(version25));
      Assert.assertEquals(-1, version24.compareTo(version25));
      Assert.assertTrue(version25.newerThan(version24));
      Assert.assertFalse(version25.olderThan(version24));
      Assert.assertEquals(1, version25.compareTo(version24));
    }

    {
      Assert.assertTrue(version26.olderThan(version27));
      Assert.assertFalse(version26.newerThan(version27));
      Assert.assertEquals(-1, version26.compareTo(version27));
      Assert.assertTrue(version27.newerThan(version26));
      Assert.assertFalse(version27.olderThan(version26));
      Assert.assertEquals(1, version27.compareTo(version26));

      Assert.assertTrue(version27.olderThan(version28));
      Assert.assertFalse(version27.newerThan(version28));
      Assert.assertEquals(-1, version27.compareTo(version28));
      Assert.assertTrue(version28.newerThan(version27));
      Assert.assertFalse(version28.olderThan(version27));
      Assert.assertEquals(1, version28.compareTo(version27));

      Assert.assertTrue(version28.olderThan(version29));
      Assert.assertFalse(version28.newerThan(version29));
      Assert.assertEquals(-1, version28.compareTo(version29));
      Assert.assertTrue(version29.newerThan(version28));
      Assert.assertFalse(version29.olderThan(version28));
      Assert.assertEquals(1, version29.compareTo(version28));
    }

    {
      Assert.assertTrue(version30.olderThan(version31));
      Assert.assertFalse(version30.newerThan(version31));
      Assert.assertEquals(-1, version30.compareTo(version31));
      Assert.assertTrue(version31.newerThan(version30));
      Assert.assertFalse(version31.olderThan(version30));
      Assert.assertEquals(1, version31.compareTo(version30));

      Assert.assertTrue(version31.olderThan(version32));
      Assert.assertFalse(version31.newerThan(version32));
      Assert.assertEquals(-1, version31.compareTo(version32));
      Assert.assertTrue(version32.newerThan(version31));
      Assert.assertFalse(version32.olderThan(version31));
      Assert.assertEquals(1, version32.compareTo(version31));

      Assert.assertTrue(version32.olderThan(version33));
      Assert.assertFalse(version32.newerThan(version33));
      Assert.assertEquals(-1, version32.compareTo(version33));
      Assert.assertTrue(version33.newerThan(version32));
      Assert.assertFalse(version33.olderThan(version32));
      Assert.assertEquals(1, version33.compareTo(version32));

      Assert.assertTrue(version33.olderThan(version34));
      Assert.assertFalse(version33.newerThan(version34));
      Assert.assertEquals(-1, version33.compareTo(version34));
      Assert.assertTrue(version34.newerThan(version33));
      Assert.assertFalse(version34.olderThan(version33));
      Assert.assertEquals(1, version34.compareTo(version33));
    }
  }

  /**
   * Tests {@link SemanticVersion#equals(SemanticVersion)}.
   */
  @Test
  public void testEquals() {
    SemanticVersion version00 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version01 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version02 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version03 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata(null).build();
    SemanticVersion version04 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata(null).build();
    SemanticVersion version05 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata("metadata").build();

    SemanticVersion version06 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata(null).build();
    SemanticVersion version07 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata(null).build();
    SemanticVersion version08 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata("metadata").build();

    SemanticVersion version09 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.2").metadata(null).build();
    SemanticVersion version10 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.2").metadata(null).build();
    SemanticVersion version11 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.2").metadata("metadata").build();

    SemanticVersion version12 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata(null).build();
    SemanticVersion version13 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata(null).build();
    SemanticVersion version14 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a")
        .metadata(null).build();
    SemanticVersion version15 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata("metadata").build();
    SemanticVersion version16 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a")
        .metadata("metadata").build();

    SemanticVersion version17 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata(null).build();
    SemanticVersion version18 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata(null).build();
    SemanticVersion version19 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.1")
        .metadata(null).build();
    SemanticVersion version20 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata("metadata").build();
    SemanticVersion version21 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.1")
        .metadata("metadata").build();

    SemanticVersion version22 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.2").metadata(null).build();
    SemanticVersion version23 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.2").metadata(null).build();
    SemanticVersion version24 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.2")
        .metadata(null).build();
    SemanticVersion version25 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.2").metadata("metadata").build();
    SemanticVersion version26 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.2")
        .metadata("metadata").build();

    SemanticVersion version27 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata(null).build();
    SemanticVersion version28 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata(null).build();
    SemanticVersion version29 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b")
        .metadata(null).build();
    SemanticVersion version30 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata("metadata").build();
    SemanticVersion version31 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b")
        .metadata("metadata").build();

    SemanticVersion version32 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata(null).build();
    SemanticVersion version33 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata(null).build();
    SemanticVersion version34 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.1")
        .metadata(null).build();
    SemanticVersion version35 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata("metadata").build();
    SemanticVersion version36 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.1")
        .metadata("metadata").build();

    SemanticVersion version37 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.2")
        .metadata(null).build();
    SemanticVersion version38 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.2")
        .metadata(null).build();
    SemanticVersion version39 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.2")
        .metadata(null).build();
    SemanticVersion version40 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.2")
        .metadata("metadata").build();
    SemanticVersion version41 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.2")
        .metadata("metadata").build();

    SemanticVersion version42 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata(null).build();
    SemanticVersion version43 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata(null).build();
    SemanticVersion version44 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata("metadata").build();

    SemanticVersion version45 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata(null).build();
    SemanticVersion version46 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata(null).build();
    SemanticVersion version47 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata("metadata").build();

    SemanticVersion version48 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.2")
        .metadata(null).build();
    SemanticVersion version49 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.2")
        .metadata(null).build();
    SemanticVersion version50 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.2")
        .metadata("metadata").build();

    SemanticVersion version51 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version52 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version53 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version54 = SemanticVersion.builder().major(0).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version55 = SemanticVersion.builder().major(0).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version56 = SemanticVersion.builder().major(0).minor(1).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version57 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version58 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version59 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version60 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version61 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version62 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version63 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version64 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version65 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version66 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version67 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version68 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version69 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version70 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version71 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata("metadata").build();

    {
      SemanticVersion current = version00;

      Assert.assertEquals(current, version01);
      Assert.assertEquals(current, version02);

      // Assert.assertNotEquals (current, version00);
      // Assert.assertNotEquals (current, version01);
      // Assert.assertNotEquals (current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version03;

      Assert.assertEquals(current, version04);
      Assert.assertEquals(current, version05);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      // Assert.assertNotEquals (current, version03);
      // Assert.assertNotEquals (current, version04);
      // Assert.assertNotEquals (current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version06;

      Assert.assertEquals(current, version07);
      Assert.assertEquals(current, version08);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      // Assert.assertNotEquals (current, version06);
      // Assert.assertNotEquals (current, version07);
      // Assert.assertNotEquals (current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version09;

      Assert.assertEquals(current, version10);
      Assert.assertEquals(current, version11);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      // Assert.assertNotEquals (current, version09);
      // Assert.assertNotEquals (current, version10);
      // Assert.assertNotEquals (current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version12;

      Assert.assertEquals(current, version13);
      Assert.assertEquals(current, version14);
      Assert.assertEquals(current, version15);
      Assert.assertEquals(current, version16);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      // Assert.assertNotEquals (current, version12);
      // Assert.assertNotEquals (current, version13);
      // Assert.assertNotEquals (current, version14);
      // Assert.assertNotEquals (current, version15);
      // Assert.assertNotEquals (current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version17;

      Assert.assertEquals(current, version18);
      Assert.assertEquals(current, version19);
      Assert.assertEquals(current, version20);
      Assert.assertEquals(current, version21);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      // Assert.assertNotEquals (current, version17);
      // Assert.assertNotEquals (current, version18);
      // Assert.assertNotEquals (current, version19);
      // Assert.assertNotEquals (current, version20);
      // Assert.assertNotEquals (current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version22;

      Assert.assertEquals(current, version23);
      Assert.assertEquals(current, version24);
      Assert.assertEquals(current, version25);
      Assert.assertEquals(current, version26);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      // Assert.assertNotEquals (current, version22);
      // Assert.assertNotEquals (current, version23);
      // Assert.assertNotEquals (current, version24);
      // Assert.assertNotEquals (current, version25);
      // Assert.assertNotEquals (current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version27;

      Assert.assertEquals(current, version28);
      Assert.assertEquals(current, version29);
      Assert.assertEquals(current, version30);
      Assert.assertEquals(current, version31);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      // Assert.assertNotEquals (current, version27);
      // Assert.assertNotEquals (current, version28);
      // Assert.assertNotEquals (current, version29);
      // Assert.assertNotEquals (current, version30);
      // Assert.assertNotEquals (current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version32;

      Assert.assertEquals(current, version33);
      Assert.assertEquals(current, version34);
      Assert.assertEquals(current, version35);
      Assert.assertEquals(current, version36);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      // Assert.assertNotEquals (current, version32);
      // Assert.assertNotEquals (current, version33);
      // Assert.assertNotEquals (current, version34);
      // Assert.assertNotEquals (current, version35);
      // Assert.assertNotEquals (current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version37;

      Assert.assertEquals(current, version38);
      Assert.assertEquals(current, version39);
      Assert.assertEquals(current, version40);
      Assert.assertEquals(current, version41);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      // Assert.assertNotEquals (current, version37);
      // Assert.assertNotEquals (current, version38);
      // Assert.assertNotEquals (current, version39);
      // Assert.assertNotEquals (current, version40);
      // Assert.assertNotEquals (current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version42;

      Assert.assertEquals(current, version43);
      Assert.assertEquals(current, version44);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      // Assert.assertNotEquals (current, version42);
      // Assert.assertNotEquals (current, version43);
      // Assert.assertNotEquals (current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version45;

      Assert.assertEquals(current, version46);
      Assert.assertEquals(current, version47);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      // Assert.assertNotEquals (current, version45);
      // Assert.assertNotEquals (current, version46);
      // Assert.assertNotEquals (current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version48;

      Assert.assertEquals(current, version49);
      Assert.assertEquals(current, version50);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      // Assert.assertNotEquals (current, version48);
      // Assert.assertNotEquals (current, version49);
      // Assert.assertNotEquals (current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version51;

      Assert.assertEquals(current, version52);
      Assert.assertEquals(current, version53);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      // Assert.assertNotEquals (current, version51);
      // Assert.assertNotEquals (current, version52);
      // Assert.assertNotEquals (current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version54;

      Assert.assertEquals(current, version55);
      Assert.assertEquals(current, version56);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      // Assert.assertNotEquals (current, version54);
      // Assert.assertNotEquals (current, version55);
      // Assert.assertNotEquals (current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version57;

      Assert.assertEquals(current, version58);
      Assert.assertEquals(current, version59);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      // Assert.assertNotEquals (current, version57);
      // Assert.assertNotEquals (current, version58);
      // Assert.assertNotEquals (current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version60;

      Assert.assertEquals(current, version61);
      Assert.assertEquals(current, version62);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      // Assert.assertNotEquals (current, version60);
      // Assert.assertNotEquals (current, version61);
      // Assert.assertNotEquals (current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version63;

      Assert.assertEquals(current, version64);
      Assert.assertEquals(current, version65);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      // Assert.assertNotEquals (current, version63);
      // Assert.assertNotEquals (current, version64);
      // Assert.assertNotEquals (current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version66;

      Assert.assertEquals(current, version67);
      Assert.assertEquals(current, version68);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      // Assert.assertNotEquals (current, version66);
      // Assert.assertNotEquals (current, version67);
      // Assert.assertNotEquals (current, version68);
      Assert.assertNotEquals(current, version69);
      Assert.assertNotEquals(current, version70);
      Assert.assertNotEquals(current, version71);
    }

    {
      SemanticVersion current = version69;

      Assert.assertEquals(current, version70);
      Assert.assertEquals(current, version71);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
      Assert.assertNotEquals(current, version08);
      Assert.assertNotEquals(current, version09);
      Assert.assertNotEquals(current, version10);
      Assert.assertNotEquals(current, version11);
      Assert.assertNotEquals(current, version12);
      Assert.assertNotEquals(current, version13);
      Assert.assertNotEquals(current, version14);
      Assert.assertNotEquals(current, version15);
      Assert.assertNotEquals(current, version16);
      Assert.assertNotEquals(current, version17);
      Assert.assertNotEquals(current, version18);
      Assert.assertNotEquals(current, version19);
      Assert.assertNotEquals(current, version20);
      Assert.assertNotEquals(current, version21);
      Assert.assertNotEquals(current, version22);
      Assert.assertNotEquals(current, version23);
      Assert.assertNotEquals(current, version24);
      Assert.assertNotEquals(current, version25);
      Assert.assertNotEquals(current, version26);
      Assert.assertNotEquals(current, version27);
      Assert.assertNotEquals(current, version28);
      Assert.assertNotEquals(current, version29);
      Assert.assertNotEquals(current, version30);
      Assert.assertNotEquals(current, version31);
      Assert.assertNotEquals(current, version32);
      Assert.assertNotEquals(current, version33);
      Assert.assertNotEquals(current, version34);
      Assert.assertNotEquals(current, version35);
      Assert.assertNotEquals(current, version36);
      Assert.assertNotEquals(current, version37);
      Assert.assertNotEquals(current, version38);
      Assert.assertNotEquals(current, version39);
      Assert.assertNotEquals(current, version40);
      Assert.assertNotEquals(current, version41);
      Assert.assertNotEquals(current, version42);
      Assert.assertNotEquals(current, version43);
      Assert.assertNotEquals(current, version44);
      Assert.assertNotEquals(current, version45);
      Assert.assertNotEquals(current, version46);
      Assert.assertNotEquals(current, version47);
      Assert.assertNotEquals(current, version48);
      Assert.assertNotEquals(current, version49);
      Assert.assertNotEquals(current, version50);
      Assert.assertNotEquals(current, version51);
      Assert.assertNotEquals(current, version52);
      Assert.assertNotEquals(current, version53);
      Assert.assertNotEquals(current, version54);
      Assert.assertNotEquals(current, version55);
      Assert.assertNotEquals(current, version56);
      Assert.assertNotEquals(current, version57);
      Assert.assertNotEquals(current, version58);
      Assert.assertNotEquals(current, version59);
      Assert.assertNotEquals(current, version60);
      Assert.assertNotEquals(current, version61);
      Assert.assertNotEquals(current, version62);
      Assert.assertNotEquals(current, version63);
      Assert.assertNotEquals(current, version64);
      Assert.assertNotEquals(current, version65);
      Assert.assertNotEquals(current, version66);
      Assert.assertNotEquals(current, version67);
      Assert.assertNotEquals(current, version68);
      // Assert.assertNotEquals (current, version69);
      // Assert.assertNotEquals (current, version70);
      // Assert.assertNotEquals (current, version71);
    }
  }

  /**
   * Tests {@link SemanticVersion#hashCode()}.
   */
  @Test
  public void testHashCode() {
    SemanticVersion version01 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata(null).build();
    SemanticVersion version02 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata(null).build();
    SemanticVersion version03 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot").metadata("metadata").build();

    SemanticVersion version04 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata(null).build();
    SemanticVersion version05 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata(null).build();
    SemanticVersion version06 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("snapshot.1").metadata("metadata").build();

    SemanticVersion version07 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata(null).build();
    SemanticVersion version08 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata(null).build();
    SemanticVersion version09 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha")
        .metadata("metadata").build();
    SemanticVersion version10 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a")
        .metadata(null).build();
    SemanticVersion version11 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a")
        .metadata(null).build();
    SemanticVersion version12 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a")
        .metadata("metadata").build();

    SemanticVersion version13 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata(null).build();
    SemanticVersion version14 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata(null).build();
    SemanticVersion version15 = SemanticVersion.builder().major(0).minor(0).patch(0)
        .extra("alpha.1").metadata("metadata").build();
    SemanticVersion version16 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.1")
        .metadata(null).build();
    SemanticVersion version17 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.1")
        .metadata(null).build();
    SemanticVersion version18 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("a.1")
        .metadata("metadata").build();

    SemanticVersion version19 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata(null).build();
    SemanticVersion version20 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata(null).build();
    SemanticVersion version21 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta")
        .metadata("metadata").build();
    SemanticVersion version22 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b")
        .metadata(null).build();
    SemanticVersion version23 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b")
        .metadata(null).build();
    SemanticVersion version24 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b")
        .metadata("metadata").build();

    SemanticVersion version25 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata(null).build();
    SemanticVersion version26 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata(null).build();
    SemanticVersion version27 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("beta.1")
        .metadata("metadata").build();
    SemanticVersion version28 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.1")
        .metadata(null).build();
    SemanticVersion version29 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.1")
        .metadata(null).build();
    SemanticVersion version30 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("b.1")
        .metadata("metadata").build();

    SemanticVersion version31 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata(null).build();
    SemanticVersion version32 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata(null).build();
    SemanticVersion version33 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc")
        .metadata("metadata").build();

    SemanticVersion version34 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata(null).build();
    SemanticVersion version35 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata(null).build();
    SemanticVersion version36 = SemanticVersion.builder().major(0).minor(0).patch(0).extra("rc.1")
        .metadata("metadata").build();

    SemanticVersion version37 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version38 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version39 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version40 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version41 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version42 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version43 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version44 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version45 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version46 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version47 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version48 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version49 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version50 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version51 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .metadata("metadata").build();

    SemanticVersion version52 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version53 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata(null).build();
    SemanticVersion version54 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version55 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version56 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata(null).build();
    SemanticVersion version57 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .metadata("metadata").build();

    {
      Assert.assertEquals(version01.hashCode(), version02.hashCode());
      Assert.assertEquals(version01.hashCode(), version03.hashCode());
    }

    {
      Assert.assertEquals(version04.hashCode(), version05.hashCode());
      Assert.assertEquals(version04.hashCode(), version06.hashCode());
    }

    {
      Assert.assertEquals(version07.hashCode(), version08.hashCode());
      Assert.assertEquals(version07.hashCode(), version09.hashCode());
      Assert.assertEquals(version07.hashCode(), version10.hashCode());
      Assert.assertEquals(version07.hashCode(), version11.hashCode());
      Assert.assertEquals(version07.hashCode(), version12.hashCode());
    }

    {
      Assert.assertEquals(version13.hashCode(), version14.hashCode());
      Assert.assertEquals(version13.hashCode(), version15.hashCode());
      Assert.assertEquals(version13.hashCode(), version16.hashCode());
      Assert.assertEquals(version13.hashCode(), version17.hashCode());
      Assert.assertEquals(version13.hashCode(), version18.hashCode());
    }

    {
      Assert.assertEquals(version19.hashCode(), version20.hashCode());
      Assert.assertEquals(version19.hashCode(), version21.hashCode());
      Assert.assertEquals(version19.hashCode(), version22.hashCode());
      Assert.assertEquals(version19.hashCode(), version23.hashCode());
      Assert.assertEquals(version19.hashCode(), version24.hashCode());
    }

    {
      Assert.assertEquals(version25.hashCode(), version26.hashCode());
      Assert.assertEquals(version25.hashCode(), version27.hashCode());
      Assert.assertEquals(version25.hashCode(), version28.hashCode());
      Assert.assertEquals(version25.hashCode(), version29.hashCode());
      Assert.assertEquals(version25.hashCode(), version30.hashCode());
    }

    {
      Assert.assertEquals(version31.hashCode(), version32.hashCode());
      Assert.assertEquals(version31.hashCode(), version33.hashCode());
    }

    {
      Assert.assertEquals(version34.hashCode(), version35.hashCode());
      Assert.assertEquals(version34.hashCode(), version36.hashCode());
    }

    {
      Assert.assertEquals(version37.hashCode(), version38.hashCode());
      Assert.assertEquals(version37.hashCode(), version39.hashCode());
    }

    {
      Assert.assertEquals(version40.hashCode(), version41.hashCode());
      Assert.assertEquals(version40.hashCode(), version42.hashCode());
    }

    {
      Assert.assertEquals(version43.hashCode(), version44.hashCode());
      Assert.assertEquals(version43.hashCode(), version45.hashCode());
    }

    {
      Assert.assertEquals(version46.hashCode(), version47.hashCode());
      Assert.assertEquals(version46.hashCode(), version48.hashCode());
    }

    {
      Assert.assertEquals(version49.hashCode(), version50.hashCode());
      Assert.assertEquals(version49.hashCode(), version51.hashCode());
    }

    {
      Assert.assertEquals(version52.hashCode(), version53.hashCode());
      Assert.assertEquals(version52.hashCode(), version54.hashCode());
    }

    {
      Assert.assertEquals(version55.hashCode(), version56.hashCode());
      Assert.assertEquals(version55.hashCode(), version57.hashCode());
    }
  }

  /**
   * Tests {@link SemanticVersion#extra(String)}, {@link SemanticVersion#major(int)}, {@link
   * SemanticVersion#metadata(String)}, {@link SemanticVersion#minor(int)} and {@link
   * SemanticVersion#patch(int)}.
   */
  @Test
  public void testMutate() {
    SemanticVersion version = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .metadata(null).build();

    {
      SemanticVersion mutatedVersion = version.major(1);

      Assert.assertEquals(0, version.major());
      Assert.assertEquals(0, version.minor());
      Assert.assertEquals(0, version.patch());
      Assert.assertNull(version.extra());
      Assert.assertNull(version.metadata());

      Assert.assertEquals(1, mutatedVersion.major());
      Assert.assertEquals(0, mutatedVersion.minor());
      Assert.assertEquals(0, mutatedVersion.patch());
      Assert.assertNull(mutatedVersion.extra());
      Assert.assertNull(mutatedVersion.metadata());
    }

    {
      SemanticVersion mutatedVersion = version.minor(1);

      Assert.assertEquals(0, version.major());
      Assert.assertEquals(0, version.minor());
      Assert.assertEquals(0, version.patch());
      Assert.assertNull(version.extra());
      Assert.assertNull(version.metadata());

      Assert.assertEquals(0, mutatedVersion.major());
      Assert.assertEquals(1, mutatedVersion.minor());
      Assert.assertEquals(0, mutatedVersion.patch());
      Assert.assertNull(mutatedVersion.extra());
      Assert.assertNull(mutatedVersion.metadata());
    }

    {
      SemanticVersion mutatedVersion = version.patch(1);

      Assert.assertEquals(0, version.major());
      Assert.assertEquals(0, version.minor());
      Assert.assertEquals(0, version.patch());
      Assert.assertNull(version.extra());
      Assert.assertNull(version.metadata());

      Assert.assertEquals(0, mutatedVersion.major());
      Assert.assertEquals(0, mutatedVersion.minor());
      Assert.assertEquals(1, mutatedVersion.patch());
      Assert.assertNull(mutatedVersion.extra());
      Assert.assertNull(mutatedVersion.metadata());
    }

    {
      SemanticVersion mutatedVersion = version.extra("alpha");

      Assert.assertEquals(0, version.major());
      Assert.assertEquals(0, version.minor());
      Assert.assertEquals(0, version.patch());
      Assert.assertNull(version.extra());
      Assert.assertNull(version.metadata());

      Assert.assertEquals(0, mutatedVersion.major());
      Assert.assertEquals(0, mutatedVersion.minor());
      Assert.assertEquals(0, mutatedVersion.patch());
      Assert.assertEquals("alpha", mutatedVersion.extra());
      Assert.assertNull(mutatedVersion.metadata());
    }

    {
      SemanticVersion mutatedVersion = version.metadata("metadata");

      Assert.assertEquals(0, version.major());
      Assert.assertEquals(0, version.minor());
      Assert.assertEquals(0, version.patch());
      Assert.assertNull(version.extra());
      Assert.assertNull(version.metadata());

      Assert.assertEquals(0, mutatedVersion.major());
      Assert.assertEquals(0, mutatedVersion.minor());
      Assert.assertEquals(0, mutatedVersion.patch());
      Assert.assertNull(mutatedVersion.extra());
      Assert.assertEquals("metadata", mutatedVersion.metadata());
    }
  }

  /**
   * Tests {@link SemanticVersion#of(String)}.
   */
  @Test
  public void testParse() {
    this.assertVersion(SemanticVersion.of("0.0"), 0, 0, 0, null, null);
    this.assertVersion(SemanticVersion.of("0.0+metadata"), 0, 0, 0, null, "metadata");
    this.assertVersion(SemanticVersion.of("0.0-snapshot"), 0, 0, 0, "snapshot", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-snapshot"), 0, 0, 0, "snapshot",
        "metadata");
    this.assertVersion(SemanticVersion.of("0.0-snapshot+metadata"), 0, 0, 0, "snapshot",
        "metadata");

    this.assertVersion(SemanticVersion.of("0.0-snapshot.1"), 0, 0, 0, "snapshot.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-snapshot.1"), 0, 0, 0, "snapshot.1",
        "metadata");
    this.assertVersion(SemanticVersion.of("0.0-snapshot.1+metadata"), 0, 0, 0, "snapshot.1",
        "metadata");

    this.assertVersion(SemanticVersion.of("0.0-snapshot.2"), 0, 0, 0, "snapshot.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-snapshot.2"), 0, 0, 0, "snapshot.2",
        "metadata");
    this.assertVersion(SemanticVersion.of("0.0-snapshot.2+metadata"), 0, 0, 0, "snapshot.2",
        "metadata");

    this.assertVersion(SemanticVersion.of("0.0-alpha"), 0, 0, 0, "alpha", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-alpha"), 0, 0, 0, "alpha", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-alpha+metadata"), 0, 0, 0, "alpha", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-a"), 0, 0, 0, "a", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-a"), 0, 0, 0, "a", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-a+metadata"), 0, 0, 0, "a", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-alpha.1"), 0, 0, 0, "alpha.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-alpha.1"), 0, 0, 0, "alpha.1", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-alpha.1+metadata"), 0, 0, 0, "alpha.1", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-a.1"), 0, 0, 0, "a.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-a.1"), 0, 0, 0, "a.1", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-a.1+metadata"), 0, 0, 0, "a.1", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-alpha.2"), 0, 0, 0, "alpha.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-alpha.2"), 0, 0, 0, "alpha.2", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-alpha.2+metadata"), 0, 0, 0, "alpha.2", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-a.2"), 0, 0, 0, "a.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-a.2"), 0, 0, 0, "a.2", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-a.2+metadata"), 0, 0, 0, "a.2", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-beta"), 0, 0, 0, "beta", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-beta"), 0, 0, 0, "beta", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-beta+metadata"), 0, 0, 0, "beta", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-b"), 0, 0, 0, "b", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-b"), 0, 0, 0, "b", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-b+metadata"), 0, 0, 0, "b", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-beta.1"), 0, 0, 0, "beta.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-beta.1"), 0, 0, 0, "beta.1", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-beta.1+metadata"), 0, 0, 0, "beta.1", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-b.1"), 0, 0, 0, "b.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-b.1"), 0, 0, 0, "b.1", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-b.1+metadata"), 0, 0, 0, "b.1", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-beta.2"), 0, 0, 0, "beta.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-beta.2"), 0, 0, 0, "beta.2", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-beta.2+metadata"), 0, 0, 0, "beta.2", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-b.2"), 0, 0, 0, "b.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-b.2"), 0, 0, 0, "b.2", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-b.2+metadata"), 0, 0, 0, "b.2", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-rc"), 0, 0, 0, "rc", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-rc"), 0, 0, 0, "rc", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-rc+metadata"), 0, 0, 0, "rc", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-rc.1"), 0, 0, 0, "rc.1", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-rc.1"), 0, 0, 0, "rc.1", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-rc.1+metadata"), 0, 0, 0, "rc.1", "metadata");

    this.assertVersion(SemanticVersion.of("0.0-rc.2"), 0, 0, 0, "rc.2", null);
    this.assertVersion(SemanticVersion.of("0.0+metadata-rc.2"), 0, 0, 0, "rc.2", "metadata");
    this.assertVersion(SemanticVersion.of("0.0-rc.2+metadata"), 0, 0, 0, "rc.2", "metadata");

    this.assertVersion(SemanticVersion.of("0.1"), 0, 1, 0, null, null);
    this.assertVersion(SemanticVersion.of("1.0"), 1, 0, 0, null, null);
    this.assertVersion(SemanticVersion.of("1.1"), 1, 1, 0, null, null);

    this.assertVersion(SemanticVersion.of("0.0.1"), 0, 0, 1, null, null);
    this.assertVersion(SemanticVersion.of("0.1.0"), 0, 1, 0, null, null);
    this.assertVersion(SemanticVersion.of("0.1.1"), 0, 1, 1, null, null);
    this.assertVersion(SemanticVersion.of("1.0.0"), 1, 0, 0, null, null);
    this.assertVersion(SemanticVersion.of("1.0.1"), 1, 0, 1, null, null);
    this.assertVersion(SemanticVersion.of("1.1.0"), 1, 1, 0, null, null);
    this.assertVersion(SemanticVersion.of("1.1.1"), 1, 1, 1, null, null);
  }

  /**
   * Tests {@link SemanticVersion#range(String)}.
   */
  @Test
  public void testParseRange() {
    VersionRange<SemanticVersion> range0 = SemanticVersion.range("(0.0,1.0)");
    VersionRange<SemanticVersion> range1 = SemanticVersion.range("[0.0,1.0)");
    VersionRange<SemanticVersion> range2 = SemanticVersion.range("(0.0,1.0]");
    VersionRange<SemanticVersion> range3 = SemanticVersion.range("[0.0,1.0]");

    {
      Assert.assertEquals(SemanticVersion.builder().major(0).minor(0).build(), range0.startBound());
      Assert.assertEquals(SemanticVersion.builder().major(1).minor(0).build(), range0.endBound());
      Assert.assertFalse(range0.startInclusive());
      Assert.assertFalse(range0.endInclusive());
    }

    {
      Assert.assertEquals(SemanticVersion.builder().major(0).minor(0).build(), range1.startBound());
      Assert.assertEquals(SemanticVersion.builder().major(1).minor(0).build(), range1.endBound());
      Assert.assertTrue(range1.startInclusive());
      Assert.assertFalse(range1.endInclusive());
    }

    {
      Assert.assertEquals(SemanticVersion.builder().major(0).minor(0).build(), range2.startBound());
      Assert.assertEquals(SemanticVersion.builder().major(1).minor(0).build(), range2.endBound());
      Assert.assertFalse(range2.startInclusive());
      Assert.assertTrue(range2.endInclusive());
    }

    {
      Assert.assertEquals(SemanticVersion.builder().major(0).minor(0).build(), range3.startBound());
      Assert.assertEquals(SemanticVersion.builder().major(1).minor(0).build(), range3.endBound());
      Assert.assertTrue(range3.startInclusive());
      Assert.assertTrue(range3.endInclusive());
    }
  }

  /**
   * Tests {@link SemanticVersion#stable()} and {@link SemanticVersion#unstable()}.
   */
  @Test
  public void testStability() {
    SemanticVersion version00 = SemanticVersion.builder().major(0).minor(0).patch(0).extra(null)
        .build();
    SemanticVersion version01 = SemanticVersion.builder().major(0).minor(0).patch(1).extra(null)
        .build();
    SemanticVersion version02 = SemanticVersion.builder().major(0).minor(1).patch(0).extra(null)
        .build();
    SemanticVersion version03 = SemanticVersion.builder().major(0).minor(1).patch(1).extra(null)
        .build();
    SemanticVersion version04 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .build();
    SemanticVersion version05 = SemanticVersion.builder().major(1).minor(0).patch(1).extra(null)
        .build();
    SemanticVersion version06 = SemanticVersion.builder().major(1).minor(1).patch(0).extra(null)
        .build();
    SemanticVersion version07 = SemanticVersion.builder().major(1).minor(1).patch(1).extra(null)
        .build();
    SemanticVersion version08 = SemanticVersion.builder().major(1).minor(0).patch(0).extra(null)
        .metadata("metadata").build();

    SemanticVersion version09 = SemanticVersion.builder().major(1).minor(0).patch(0)
        .extra("snapshot").build();
    SemanticVersion version10 = SemanticVersion.builder().major(1).minor(0).patch(0)
        .extra("snapshot.1").build();
    SemanticVersion version11 = SemanticVersion.builder().major(1).minor(0).patch(0)
        .extra("snapshot.2").build();
    SemanticVersion version12 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("alpha")
        .build();
    SemanticVersion version13 = SemanticVersion.builder().major(1).minor(0).patch(0)
        .extra("alpha.1").build();
    SemanticVersion version14 = SemanticVersion.builder().major(1).minor(0).patch(0)
        .extra("alpha.2").build();
    SemanticVersion version15 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("a")
        .build();
    SemanticVersion version16 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("a.1")
        .build();
    SemanticVersion version17 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("a.2")
        .build();
    SemanticVersion version18 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("beta")
        .build();
    SemanticVersion version19 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("beta.1")
        .build();
    SemanticVersion version20 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("beta.2")
        .build();
    SemanticVersion version21 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("b")
        .build();
    SemanticVersion version22 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("b.1")
        .build();
    SemanticVersion version23 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("b.2")
        .build();
    SemanticVersion version24 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("rc")
        .build();
    SemanticVersion version25 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("rc.1")
        .build();
    SemanticVersion version26 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("rc.2")
        .build();
    SemanticVersion version27 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("foo")
        .build();
    SemanticVersion version28 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("foo.1")
        .build();
    SemanticVersion version29 = SemanticVersion.builder().major(1).minor(0).patch(0).extra("foo.2")
        .build();

    {
      Assert.assertFalse(version00.stable());
      Assert.assertTrue(version00.unstable());

      Assert.assertFalse(version01.stable());
      Assert.assertTrue(version01.unstable());

      Assert.assertFalse(version02.stable());
      Assert.assertTrue(version02.unstable());

      Assert.assertFalse(version03.stable());
      Assert.assertTrue(version03.unstable());

      Assert.assertTrue(version04.stable());
      Assert.assertFalse(version04.unstable());

      Assert.assertTrue(version05.stable());
      Assert.assertFalse(version05.unstable());

      Assert.assertTrue(version06.stable());
      Assert.assertFalse(version06.unstable());

      Assert.assertTrue(version07.stable());
      Assert.assertFalse(version07.unstable());

      Assert.assertTrue(version08.stable());
      Assert.assertFalse(version08.unstable());
    }

    {
      Assert.assertFalse(version09.stable());
      Assert.assertTrue(version09.unstable());

      Assert.assertFalse(version10.stable());
      Assert.assertTrue(version10.unstable());

      Assert.assertFalse(version11.stable());
      Assert.assertTrue(version11.unstable());

      Assert.assertFalse(version12.stable());
      Assert.assertTrue(version12.unstable());

      Assert.assertFalse(version13.stable());
      Assert.assertTrue(version13.unstable());

      Assert.assertFalse(version14.stable());
      Assert.assertTrue(version14.unstable());

      Assert.assertFalse(version15.stable());
      Assert.assertTrue(version15.unstable());

      Assert.assertFalse(version16.stable());
      Assert.assertTrue(version16.unstable());

      Assert.assertFalse(version17.stable());
      Assert.assertTrue(version17.unstable());

      Assert.assertFalse(version18.stable());
      Assert.assertTrue(version18.unstable());

      Assert.assertFalse(version19.stable());
      Assert.assertTrue(version19.unstable());

      Assert.assertFalse(version20.stable());
      Assert.assertTrue(version20.unstable());

      Assert.assertFalse(version21.stable());
      Assert.assertTrue(version21.unstable());

      Assert.assertFalse(version22.stable());
      Assert.assertTrue(version22.unstable());

      Assert.assertFalse(version23.stable());
      Assert.assertTrue(version23.unstable());

      Assert.assertFalse(version24.stable());
      Assert.assertTrue(version24.unstable());

      Assert.assertFalse(version25.stable());
      Assert.assertTrue(version25.unstable());

      Assert.assertFalse(version26.stable());
      Assert.assertTrue(version26.unstable());

      Assert.assertFalse(version27.stable());
      Assert.assertTrue(version27.unstable());

      Assert.assertFalse(version28.stable());
      Assert.assertTrue(version28.unstable());

      Assert.assertFalse(version29.stable());
      Assert.assertTrue(version29.unstable());
    }
  }

  /**
   * Tests {@link SemanticVersion#toString()}.
   */
  @Test
  public void testToString() {
    Assert.assertEquals("0.0-alpha",
        SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("0.0-alpha+metadata",
        SemanticVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("0.0",
        SemanticVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("0.0+metadata",
        SemanticVersion.builder().major(0).minor(0).patch(0).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("0.0.1-alpha",
        SemanticVersion.builder().major(0).minor(0).patch(1).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("0.0.1-alpha+metadata",
        SemanticVersion.builder().major(0).minor(0).patch(1).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("0.0.1",
        SemanticVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("0.0.1+metadata",
        SemanticVersion.builder().major(0).minor(0).patch(1).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("0.1-alpha",
        SemanticVersion.builder().major(0).minor(1).patch(0).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("0.1-alpha+metadata",
        SemanticVersion.builder().major(0).minor(1).patch(0).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("0.1",
        SemanticVersion.builder().major(0).minor(1).patch(0).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("0.1+metadata",
        SemanticVersion.builder().major(0).minor(1).patch(0).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("0.1.1-alpha",
        SemanticVersion.builder().major(0).minor(1).patch(1).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("0.1.1-alpha+metadata",
        SemanticVersion.builder().major(0).minor(1).patch(1).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("0.1.1",
        SemanticVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("0.1.1+metadata",
        SemanticVersion.builder().major(0).minor(1).patch(1).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("1.0-alpha",
        SemanticVersion.builder().major(1).minor(0).patch(0).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("1.0-alpha+metadata",
        SemanticVersion.builder().major(1).minor(0).patch(0).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("1.0",
        SemanticVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("1.0+metadata",
        SemanticVersion.builder().major(1).minor(0).patch(0).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("1.0.1-alpha",
        SemanticVersion.builder().major(1).minor(0).patch(1).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("1.0.1-alpha+metadata",
        SemanticVersion.builder().major(1).minor(0).patch(1).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("1.0.1",
        SemanticVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("1.0.1+metadata",
        SemanticVersion.builder().major(1).minor(0).patch(1).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("1.1-alpha",
        SemanticVersion.builder().major(1).minor(1).patch(0).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("1.1-alpha+metadata",
        SemanticVersion.builder().major(1).minor(1).patch(0).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("1.1",
        SemanticVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("1.1+metadata",
        SemanticVersion.builder().major(1).minor(1).patch(0).extra(null).metadata("metadata")
            .build().toString());

    Assert.assertEquals("1.1.1-alpha",
        SemanticVersion.builder().major(1).minor(1).patch(1).extra("alpha").metadata(null).build()
            .toString());
    Assert.assertEquals("1.1.1-alpha+metadata",
        SemanticVersion.builder().major(1).minor(1).patch(1).extra("alpha").metadata("metadata")
            .build().toString());
    Assert.assertEquals("1.1.1",
        SemanticVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build()
            .toString());
    Assert.assertEquals("1.1.1+metadata",
        SemanticVersion.builder().major(1).minor(1).patch(1).extra(null).metadata("metadata")
            .build().toString());
  }

  /**
   * Provides test cases for {@link SemanticVersion.Builder}.
   */
  @RunWith(MockitoJUnitRunner.class)
  public static class BuilderTest {

    /**
     * Tests {@link SemanticVersion.Builder#build()}.
     */
    @Test
    public void testBuild() {
      SemanticVersion.Builder builder00 = SemanticVersion.builder();
      SemanticVersion.Builder builder01 = SemanticVersion.builder().metadata("metadata");
      SemanticVersion.Builder builder02 = SemanticVersion.builder().extra("extra");
      SemanticVersion.Builder builder03 = SemanticVersion.builder().extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder04 = SemanticVersion.builder().patch(1);
      SemanticVersion.Builder builder05 = SemanticVersion.builder().patch(1).metadata("metadata");
      SemanticVersion.Builder builder06 = SemanticVersion.builder().patch(1).extra("extra");
      SemanticVersion.Builder builder07 = SemanticVersion.builder().patch(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder08 = SemanticVersion.builder().minor(1);
      SemanticVersion.Builder builder09 = SemanticVersion.builder().minor(1).metadata("metadata");
      SemanticVersion.Builder builder10 = SemanticVersion.builder().minor(1).extra("extra");
      SemanticVersion.Builder builder11 = SemanticVersion.builder().minor(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder12 = SemanticVersion.builder().minor(1).patch(1);
      SemanticVersion.Builder builder13 = SemanticVersion.builder().minor(1).patch(1)
          .metadata("metadata");
      SemanticVersion.Builder builder14 = SemanticVersion.builder().minor(1).patch(1)
          .extra("extra");
      SemanticVersion.Builder builder15 = SemanticVersion.builder().minor(1).patch(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder16 = SemanticVersion.builder().major(1);
      SemanticVersion.Builder builder17 = SemanticVersion.builder().major(1).metadata("metadata");
      SemanticVersion.Builder builder18 = SemanticVersion.builder().major(1).extra("extra");
      SemanticVersion.Builder builder19 = SemanticVersion.builder().major(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder20 = SemanticVersion.builder().major(1).patch(1);
      SemanticVersion.Builder builder21 = SemanticVersion.builder().major(1).patch(1)
          .metadata("metadata");
      SemanticVersion.Builder builder22 = SemanticVersion.builder().major(1).patch(1)
          .extra("extra");
      SemanticVersion.Builder builder23 = SemanticVersion.builder().major(1).patch(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder24 = SemanticVersion.builder().major(1).minor(1);
      SemanticVersion.Builder builder25 = SemanticVersion.builder().major(1).minor(1)
          .metadata("metadata");
      SemanticVersion.Builder builder26 = SemanticVersion.builder().major(1).minor(1)
          .extra("extra");
      SemanticVersion.Builder builder27 = SemanticVersion.builder().major(1).minor(1).extra("extra")
          .metadata("metadata");
      SemanticVersion.Builder builder28 = SemanticVersion.builder().major(1).minor(1).patch(1);
      SemanticVersion.Builder builder29 = SemanticVersion.builder().major(1).minor(1).patch(1)
          .metadata("metadata");
      SemanticVersion.Builder builder30 = SemanticVersion.builder().major(1).minor(1).patch(1)
          .extra("extra");
      SemanticVersion.Builder builder31 = SemanticVersion.builder().major(1).minor(1).patch(1)
          .extra("extra").metadata("metadata");

      {
        Assert.assertEquals(0, builder00.major());
        Assert.assertEquals(0, builder00.minor());
        Assert.assertEquals(0, builder00.patch());
        Assert.assertNull(builder00.extra());
        Assert.assertNull(builder00.metadata());

        SemanticVersion version = builder00.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder01.major());
        Assert.assertEquals(0, builder01.minor());
        Assert.assertEquals(0, builder01.patch());
        Assert.assertNull(builder01.extra());
        Assert.assertEquals("metadata", builder01.metadata());

        SemanticVersion version = builder01.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder02.major());
        Assert.assertEquals(0, builder02.minor());
        Assert.assertEquals(0, builder02.patch());
        Assert.assertEquals("extra", builder02.extra());
        Assert.assertNull(builder02.metadata());

        SemanticVersion version = builder02.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder03.major());
        Assert.assertEquals(0, builder03.minor());
        Assert.assertEquals(0, builder03.patch());
        Assert.assertEquals("extra", builder03.extra());
        Assert.assertEquals("metadata", builder03.metadata());

        SemanticVersion version = builder03.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder04.major());
        Assert.assertEquals(0, builder04.minor());
        Assert.assertEquals(1, builder04.patch());
        Assert.assertNull(builder04.extra());
        Assert.assertNull(builder04.metadata());

        SemanticVersion version = builder04.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder05.major());
        Assert.assertEquals(0, builder05.minor());
        Assert.assertEquals(1, builder05.patch());
        Assert.assertNull(builder05.extra());
        Assert.assertEquals("metadata", builder05.metadata());

        SemanticVersion version = builder05.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder06.major());
        Assert.assertEquals(0, builder06.minor());
        Assert.assertEquals(1, builder06.patch());
        Assert.assertEquals("extra", builder06.extra());
        Assert.assertNull(builder06.metadata());

        SemanticVersion version = builder06.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder07.major());
        Assert.assertEquals(0, builder07.minor());
        Assert.assertEquals(1, builder07.patch());
        Assert.assertEquals("extra", builder07.extra());
        Assert.assertEquals("metadata", builder07.metadata());

        SemanticVersion version = builder07.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder08.major());
        Assert.assertEquals(1, builder08.minor());
        Assert.assertEquals(0, builder08.patch());
        Assert.assertNull(builder08.extra());
        Assert.assertNull(builder08.metadata());

        SemanticVersion version = builder08.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder09.major());
        Assert.assertEquals(1, builder09.minor());
        Assert.assertEquals(0, builder09.patch());
        Assert.assertNull(builder09.extra());
        Assert.assertEquals("metadata", builder09.metadata());

        SemanticVersion version = builder09.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder10.major());
        Assert.assertEquals(1, builder10.minor());
        Assert.assertEquals(0, builder10.patch());
        Assert.assertEquals("extra", builder10.extra());
        Assert.assertNull(builder10.metadata());

        SemanticVersion version = builder10.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder11.major());
        Assert.assertEquals(1, builder11.minor());
        Assert.assertEquals(0, builder11.patch());
        Assert.assertEquals("extra", builder11.extra());
        Assert.assertEquals("metadata", builder11.metadata());

        SemanticVersion version = builder11.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder12.major());
        Assert.assertEquals(1, builder12.minor());
        Assert.assertEquals(1, builder12.patch());
        Assert.assertNull(builder12.extra());
        Assert.assertNull(builder12.metadata());

        SemanticVersion version = builder12.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder13.major());
        Assert.assertEquals(1, builder13.minor());
        Assert.assertEquals(1, builder13.patch());
        Assert.assertNull(builder13.extra());
        Assert.assertEquals("metadata", builder13.metadata());

        SemanticVersion version = builder13.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(0, builder14.major());
        Assert.assertEquals(1, builder14.minor());
        Assert.assertEquals(1, builder14.patch());
        Assert.assertEquals("extra", builder14.extra());
        Assert.assertNull(builder14.metadata());

        SemanticVersion version = builder14.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(0, builder15.major());
        Assert.assertEquals(1, builder15.minor());
        Assert.assertEquals(1, builder15.patch());
        Assert.assertEquals("extra", builder15.extra());
        Assert.assertEquals("metadata", builder15.metadata());

        SemanticVersion version = builder15.build();

        Assert.assertEquals(0, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder16.major());
        Assert.assertEquals(0, builder16.minor());
        Assert.assertEquals(0, builder16.patch());
        Assert.assertNull(builder16.extra());
        Assert.assertNull(builder16.metadata());

        SemanticVersion version = builder16.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder17.major());
        Assert.assertEquals(0, builder17.minor());
        Assert.assertEquals(0, builder17.patch());
        Assert.assertNull(builder17.extra());
        Assert.assertEquals("metadata", builder17.metadata());

        SemanticVersion version = builder17.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder18.major());
        Assert.assertEquals(0, builder18.minor());
        Assert.assertEquals(0, builder18.patch());
        Assert.assertEquals("extra", builder18.extra());
        Assert.assertNull(builder18.metadata());

        SemanticVersion version = builder18.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder19.major());
        Assert.assertEquals(0, builder19.minor());
        Assert.assertEquals(0, builder19.patch());
        Assert.assertEquals("extra", builder19.extra());
        Assert.assertEquals("metadata", builder19.metadata());

        SemanticVersion version = builder19.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder20.major());
        Assert.assertEquals(0, builder20.minor());
        Assert.assertEquals(1, builder20.patch());
        Assert.assertNull(builder20.extra());
        Assert.assertNull(builder20.metadata());

        SemanticVersion version = builder20.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder21.major());
        Assert.assertEquals(0, builder21.minor());
        Assert.assertEquals(1, builder21.patch());
        Assert.assertNull(builder21.extra());
        Assert.assertEquals("metadata", builder21.metadata());

        SemanticVersion version = builder21.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder22.major());
        Assert.assertEquals(0, builder22.minor());
        Assert.assertEquals(1, builder22.patch());
        Assert.assertEquals("extra", builder22.extra());
        Assert.assertNull(builder22.metadata());

        SemanticVersion version = builder22.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder23.major());
        Assert.assertEquals(0, builder23.minor());
        Assert.assertEquals(1, builder23.patch());
        Assert.assertEquals("extra", builder23.extra());
        Assert.assertEquals("metadata", builder23.metadata());

        SemanticVersion version = builder23.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(0, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder24.major());
        Assert.assertEquals(1, builder24.minor());
        Assert.assertEquals(0, builder24.patch());
        Assert.assertNull(builder24.extra());
        Assert.assertNull(builder24.metadata());

        SemanticVersion version = builder24.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder25.major());
        Assert.assertEquals(1, builder25.minor());
        Assert.assertEquals(0, builder25.patch());
        Assert.assertNull(builder25.extra());
        Assert.assertEquals("metadata", builder25.metadata());

        SemanticVersion version = builder25.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder26.major());
        Assert.assertEquals(1, builder26.minor());
        Assert.assertEquals(0, builder26.patch());
        Assert.assertEquals("extra", builder26.extra());
        Assert.assertNull(builder26.metadata());

        SemanticVersion version = builder26.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder27.major());
        Assert.assertEquals(1, builder27.minor());
        Assert.assertEquals(0, builder27.patch());
        Assert.assertEquals("extra", builder27.extra());
        Assert.assertEquals("metadata", builder27.metadata());

        SemanticVersion version = builder27.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(0, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder28.major());
        Assert.assertEquals(1, builder28.minor());
        Assert.assertEquals(1, builder28.patch());
        Assert.assertNull(builder28.extra());
        Assert.assertNull(builder28.metadata());

        SemanticVersion version = builder28.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder29.major());
        Assert.assertEquals(1, builder29.minor());
        Assert.assertEquals(1, builder29.patch());
        Assert.assertNull(builder29.extra());
        Assert.assertEquals("metadata", builder29.metadata());

        SemanticVersion version = builder29.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertNull(version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }

      {
        Assert.assertEquals(1, builder30.major());
        Assert.assertEquals(1, builder30.minor());
        Assert.assertEquals(1, builder30.patch());
        Assert.assertEquals("extra", builder30.extra());
        Assert.assertNull(builder30.metadata());

        SemanticVersion version = builder30.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertNull(version.metadata());
      }

      {
        Assert.assertEquals(1, builder31.major());
        Assert.assertEquals(1, builder31.minor());
        Assert.assertEquals(1, builder31.patch());
        Assert.assertEquals("extra", builder31.extra());
        Assert.assertEquals("metadata", builder31.metadata());

        SemanticVersion version = builder31.build();

        Assert.assertEquals(1, version.major());
        Assert.assertEquals(1, version.minor());
        Assert.assertEquals(1, version.patch());
        Assert.assertEquals("extra", version.extra());
        Assert.assertEquals("metadata", version.metadata());
      }
    }

    /**
     * Tests {@link SemanticVersion#builder(SemanticVersion)}.
     */
    @Test
    public void testCopy() {
      SemanticVersion version = SemanticVersion.builder().major(1).minor(1).patch(1).extra("test")
          .metadata("test").build();
      SemanticVersion.Builder builder = SemanticVersion.builder(version);

      Assert.assertEquals(version.major(), builder.major());
      Assert.assertEquals(version.minor(), builder.minor());
      Assert.assertEquals(version.patch(), builder.patch());
      Assert.assertEquals(version.extra(), builder.extra());
      Assert.assertEquals(version.metadata(), builder.metadata());
    }

    /**
     * Tests {@link SemanticVersion.Builder#reset()}
     */
    @Test
    public void testReset() {
      SemanticVersion.Builder builder = SemanticVersion.builder().major(1).minor(1).patch(1)
          .extra("test").metadata("test");

      Assert.assertEquals(1, builder.major());
      Assert.assertEquals(1, builder.minor());
      Assert.assertEquals(1, builder.patch());
      Assert.assertEquals("test", builder.extra());
      Assert.assertEquals("test", builder.metadata());

      builder.reset();

      Assert.assertEquals(0, builder.major());
      Assert.assertEquals(0, builder.minor());
      Assert.assertEquals(0, builder.patch());
      Assert.assertNull(builder.extra());
      Assert.assertNull(builder.metadata());
    }
  }
}
