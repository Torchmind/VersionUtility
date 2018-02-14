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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Provides test cases {@link JavaVersion}.
 *
 * @author Johannes Donath
 */
@RunWith(MockitoJUnitRunner.class)
public class JavaVersionTest {

  /**
   * Tests {@link JavaVersion#compareTo(JavaVersion)}.
   */
  @Test
  public void testCompare() {
    JavaVersion version00 = JavaVersion.builder().major(1).minor(5).patch(0).updateNumber(0)
        .build();
    JavaVersion version01 = JavaVersion.builder().major(1).minor(5).patch(0).updateNumber(1)
        .build();
    JavaVersion version02 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(0)
        .build();
    JavaVersion version03 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(1)
        .build();

    {
      Assert.assertEquals(-1, version00.compareTo(version01));
      Assert.assertEquals(1, version01.compareTo(version00));
      Assert.assertTrue(version00.olderThan(version01));
      Assert.assertFalse(version00.newerThan(version01));
      Assert.assertTrue(version01.newerThan(version00));
      Assert.assertFalse(version01.olderThan(version00));
    }

    {
      Assert.assertEquals(-1, version01.compareTo(version02));
      Assert.assertEquals(1, version02.compareTo(version01));
      Assert.assertTrue(version01.olderThan(version02));
      Assert.assertFalse(version01.newerThan(version02));
      Assert.assertTrue(version02.newerThan(version01));
      Assert.assertFalse(version02.olderThan(version01));
    }

    {
      Assert.assertEquals(-1, version02.compareTo(version03));
      Assert.assertEquals(1, version03.compareTo(version02));
      Assert.assertTrue(version02.olderThan(version03));
      Assert.assertFalse(version02.newerThan(version03));
      Assert.assertTrue(version03.newerThan(version02));
      Assert.assertFalse(version03.olderThan(version02));
    }
  }

  /**
   * Tests {@link JavaVersion#current()}.
   */
  @Test
  public void testCurrent() {
    JavaVersion current = JavaVersion.current();

    Assert.assertTrue(current.newerThan(JavaVersion.JAVA_1_5));
    Assert.assertTrue(current.newerThan(JavaVersion.JAVA_1_6));
    Assert.assertTrue(current.newerThan(JavaVersion.JAVA_1_7));
    Assert.assertTrue((current.newerThan(JavaVersion.JAVA_1_8) || current
        .equals(JavaVersion.JAVA_1_8))); // Else you are a magician or shit broke ...
  }

  /**
   * Tests {@link JavaVersion#equals(JavaVersion)}.
   */
  @Test
  public void testEquals() {
    JavaVersion version00 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(0)
        .build();
    JavaVersion version01 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(0)
        .build();
    JavaVersion version02 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(1)
        .build();
    JavaVersion version03 = JavaVersion.builder().major(1).minor(6).patch(0).updateNumber(1)
        .build();
    JavaVersion version04 = JavaVersion.builder().major(1).minor(7).patch(0).updateNumber(0)
        .build();
    JavaVersion version05 = JavaVersion.builder().major(1).minor(7).patch(0).updateNumber(0)
        .build();
    JavaVersion version06 = JavaVersion.builder().major(1).minor(7).patch(0).updateNumber(1)
        .build();
    JavaVersion version07 = JavaVersion.builder().major(1).minor(7).patch(0).updateNumber(1)
        .build();

    {
      JavaVersion current = version00;

      Assert.assertEquals(current, version00);
      Assert.assertEquals(current, version01);

      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version01;

      Assert.assertEquals(current, version00);
      Assert.assertEquals(current, version01);

      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version02;

      Assert.assertEquals(current, version02);
      Assert.assertEquals(current, version03);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version03;

      Assert.assertEquals(current, version02);
      Assert.assertEquals(current, version03);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version04;

      Assert.assertEquals(current, version04);
      Assert.assertEquals(current, version05);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version05;

      Assert.assertEquals(current, version04);
      Assert.assertEquals(current, version05);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version06);
      Assert.assertNotEquals(current, version07);
    }

    {
      JavaVersion current = version06;

      Assert.assertEquals(current, version06);
      Assert.assertEquals(current, version07);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
    }

    {
      JavaVersion current = version07;

      Assert.assertEquals(current, version06);
      Assert.assertEquals(current, version07);

      Assert.assertNotEquals(current, version00);
      Assert.assertNotEquals(current, version01);
      Assert.assertNotEquals(current, version02);
      Assert.assertNotEquals(current, version03);
      Assert.assertNotEquals(current, version04);
      Assert.assertNotEquals(current, version05);
    }
  }
}
