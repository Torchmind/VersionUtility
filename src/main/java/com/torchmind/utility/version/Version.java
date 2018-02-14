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

import java.util.Optional;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Represents a version.
 *
 * @param <T> the implementing type.
 * @author Johannes Donath
 */
@ThreadSafe
public interface Version<T extends Version> extends Comparable<T> {

  /**
   * Retrieves the major version bit.
   *
   * @return the bit.
   */
  @Nonnegative
  int major();

  /**
   * Creates a copy of this version with it's major bit set to {@code value}.
   *
   * @param value the bit.
   * @return the instance.
   */
  @Nonnull
  T major(@Nonnegative int value);

  /**
   * Retrieves the minor version bit.
   *
   * @return the bit.
   */
  @Nonnegative
  int minor();

  /**
   * Creates a copy of this version with it's minor bit set to {@code value}.
   *
   * @param value the bit.
   * @return the instance.
   */
  @Nonnull
  T minor(@Nonnegative int value);

  /**
   * Checks whether this version is newer than {@code version}.
   *
   * @param version the version to compare to.
   * @return {@code true} if newer than {@code version}, {@code false} otherwise.
   */
  boolean newerThan(@Nullable T version);

  /**
   * Checks whether this version is older than {@code version}.
   *
   * @param version the version.
   * @return {@code true} if older than {@code version}, {@code false} otherwise.
   */
  boolean olderThan(@Nullable T version);

  /**
   * Retrieves the patch version bit.
   *
   * @return the bit.
   */
  @Nonnegative
  int patch();

  /**
   * Creates a copy of this version with it's patch bit set to {@code value}.
   *
   * @param value the bit.
   * @return the instance.
   */
  @Nonnull
  T patch(@Nonnegative int value);

  /**
   * Checks whether this version is stable.
   *
   * @return {@code true} if stable, {@code false} otherwise.
   */
  boolean stable();

  /**
   * Checks whether this version is unstable.
   *
   * @return {@code true} if unstable, {@code false} otherwise.
   */
  boolean unstable();

  /**
   * Retrieves the instability type (if any).
   *
   * @return the instability.
   */
  @Nonnull
  Optional<UnstableVersionType> unstableVersionType();

  /**
   * Retrieves a string representation of the version.
   *
   * @return the string.
   */
  @Nonnull
  String toString();
}
