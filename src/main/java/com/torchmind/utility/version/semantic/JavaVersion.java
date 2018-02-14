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

import com.torchmind.utility.version.UnstableVersionType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.Objects;

/**
 * Provides a wrapper around {@link SemanticVersion} for working with Java versions.
 *
 * @author Johannes Donath
 */
public final class JavaVersion extends SemanticVersion {

  public static final JavaVersion JAVA_1_5 = new JavaVersion(1, 5, 0, null, null, null, 0);
  public static final JavaVersion JAVA_1_6 = new JavaVersion(1, 6, 0, null, null, null, 0);
  public static final JavaVersion JAVA_1_7 = new JavaVersion(1, 7, 0, null, null, null, 0);
  public static final JavaVersion JAVA_1_8 = new JavaVersion(1, 8, 0, null, null, null, 0);
  public static final JavaVersion JAVA_9 = new JavaVersion(9, 0, 0, null, null, null, 0);

  private final int updateNumber;

  protected JavaVersion(int major, int minor, int patch,
      @Nullable String extra,
      @Nullable String metadata,
      @Nullable UnstableVersionType unstableVersionType,
      int updateNumber) {
    super(major, minor, patch, extra, metadata, unstableVersionType);
    this.updateNumber = updateNumber;
  }

  /**
   * Retrieves a new {@link Builder} instance.
   *
   * @return the builder.
   */
  @NonNull
  public static Builder builder() {
    return (new Builder());
  }

  /**
   * Retrieves a new {@link Builder} representing the values of {@code version}.
   *
   * @param version the version to copy from.
   * @return the builder.
   */
  @NonNull
  public static Builder builder(@NonNull JavaVersion version) {
    return builder()
        .major(version.major())
        .minor(version.minor())
        .patch(version.patch())
        .updateNumber(version.updateNumber())
        .extra(version.extra())
        .metadata(version.metadata());
  }

  /**
   * Parses a version string and creates a {@link Builder} that represents the version.
   *
   * @param version the version.
   * @return the builder.
   * @throws IllegalArgumentException when the passed version has an invalid format.
   * @throws NumberFormatException when one or more elements contain invalid numbers.
   */
  @NonNull
  public static Builder builder(@NonNull String version)
      throws IllegalArgumentException {
    return builder().parse(version);
  }

  /**
   * Retrieves the current Java version.
   *
   * @return the version.
   */
  @NonNull
  public static JavaVersion current() {
    return currentBuilder().build();
  }

  /**
   * Parses the current Java version and creates a {@link Builder} that represents the version.
   *
   * @return the builder.
   */
  @NonNull
  public static Builder currentBuilder() {
    return builder(System.getProperty("java.version"));
  }

  /**
   * Parses a Java version string.
   *
   * @param version the string.
   * @return the version.
   * @throws IllegalArgumentException when the passed version has an invalid format.
   * @throws NumberFormatException when one or more elements contain invalid numbers.
   */
  @NonNull
  public static JavaVersion of(@NonNull String version)
      throws IllegalArgumentException {
    return builder(version).build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(@NonNull SemanticVersion version) {
    if (version instanceof JavaVersion) {
      return this.compareTo(((JavaVersion) version));
    }

    return super.compareTo(version);
  }

  /**
   * Compares two versions.
   *
   * @param version the other version.
   * @return -1, 0 or 1 if the version is older than, equal or newer than the passed version.
   */
  public int compareTo(@Nullable JavaVersion version) {
    int semanticComparison = super.compareTo(version);

    if (semanticComparison != 0) {
      return semanticComparison;
    }

    return Math.max(-1, Math.min(1, (this.updateNumber() - version.updateNumber())));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(@Nullable SemanticVersion version) {
    if (version instanceof JavaVersion) {
      return this.equals(((JavaVersion) version));
    }

    return super.equals(version);
  }

  /**
   * Checks whether this version and {@code version} are equal.
   *
   * @param version the version to compare to.
   * @return {@code true} if equal, {@code false} otherwise.
   */
  public boolean equals(@Nullable JavaVersion version) {
    if (version == null) {
      return false;
    }
    if (version == this) {
      return true;
    }

    return this.updateNumber() == version.updateNumber() && super.equals(version);

  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public JavaVersion extra(@Nullable String value) {
    return builder(this).extra(value).build();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public JavaVersion major(int value) {
    return builder(this).major(value).build();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public JavaVersion metadata(@Nullable String value) {
    return builder(this).metadata(value).build();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public JavaVersion minor(int value) {
    return builder(this).minor(value).build();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public JavaVersion patch(int value) {
    return builder(this).patch(value).build();
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public String toString() {
    if (this.updateNumber() != 0) {
      return super.toString() + "_" + this.updateNumber();
    }

    return super.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    JavaVersion that = (JavaVersion) o;
    return this.updateNumber == that.updateNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.updateNumber);
  }

  /**
   * Retrieves the update number.
   *
   * @return the number.
   */
  public int updateNumber() {
    return this.updateNumber;
  }

  /**
   * Provides a builder for Java versions.
   */
  public static class Builder extends SemanticVersion.Builder {

    private int updateNumber;

    protected Builder() {
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public JavaVersion build() {
      return (new JavaVersion(this.major(), this.minor(), this.patch(), this.extra(),
          this.metadata(), this.unstableVersionType(), this.updateNumber()));
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder extra(@Nullable String extra) throws IllegalArgumentException {
      super.extra(extra);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder major(int major) {
      super.major(major);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder metadata(@Nullable String metadata) throws IllegalArgumentException {
      super.metadata(metadata);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder minor(int minor) {
      super.minor(minor);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    protected Builder parse(@NonNull String version) {
      int updateIndex = version.indexOf('_');

      if (updateIndex != -1) {

        int extraIndex = version.indexOf('-');
        int metadataIndex = version.indexOf('+');

        int endIndex;

        if (extraIndex != -1 && metadataIndex != -1) {
          endIndex = Math.min(extraIndex, metadataIndex);
        } else if (extraIndex == -1 && metadataIndex == -1) {
          endIndex = version.length();
        } else {
          endIndex = Math.max(extraIndex, metadataIndex);
        }

        try {
          this.updateNumber(
              Integer.parseUnsignedInt(version.substring((updateIndex + 1), endIndex)));
        } catch (NumberFormatException ex) {
          this.updateNumber(0);
        }

        String tmp = version.substring(0, updateIndex);
        if (endIndex != version.length()) {
          tmp += version.substring(endIndex);
        }

        version = tmp;
      }

      super.parse(version);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder patch(int patch) {
      super.patch(patch);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Builder reset() {
      super.reset();
      return this;
    }

    /**
     * Sets the update number.
     *
     * @param updateNumber the number.
     * @return the builder.
     */
    @NonNull
    public Builder updateNumber(int updateNumber) {
      this.updateNumber = updateNumber;
      return this;
    }

    /**
     * Retrieves the update number.
     *
     * @return the number.
     */
    public int updateNumber() {
      return this.updateNumber;
    }
  }
}
