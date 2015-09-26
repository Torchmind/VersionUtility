/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torchmind.utility.version.semantic;

import com.torchmind.utility.version.UnstableVersionType;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.util.function.Consumer;

/**
 * Provides a wrapper around {@link com.torchmind.utility.version.semantic.SemanticVersion} for working with Java
 * versions.
 *
 * @author Johannes Donath
 */
@ThreadSafe
public final class JavaVersion extends SemanticVersion {
        public static final JavaVersion JAVA_1_5 = new JavaVersion (1, 5, 0, null, null, null, 0);
        public static final JavaVersion JAVA_1_6 = new JavaVersion (1, 6, 0, null, null, null, 0);
        public static final JavaVersion JAVA_1_7 = new JavaVersion (1, 7, 0, null, null, null, 0);
        public static final JavaVersion JAVA_1_8 = new JavaVersion (1, 8, 0, null, null, null, 0);

        private final int updateNumber;

        protected JavaVersion (@Nonnegative int major, @Nonnegative int minor, @Nonnegative int patch, @Nullable String extra, @Nullable String metadata, @Nullable UnstableVersionType unstableVersionType, @Nonnegative int updateNumber) {
                super (major, minor, patch, extra, metadata, unstableVersionType);
                this.updateNumber = updateNumber;
        }

        /**
         * Retrieves a new {@link com.torchmind.utility.version.semantic.JavaVersion.Builder} instance.
         *
         * @return the builder.
         */
        @Nonnull
        public static Builder builder () {
                return (new Builder ());
        }

        /**
         * Retrieves a new {@link com.torchmind.utility.version.semantic.JavaVersion.Builder} representing the values
         * of {@code version}.
         *
         * @param version the version to copy from.
         * @return the builder.
         */
        @Nonnull
        public static Builder builder (@Nonnull JavaVersion version) {
                // @formatter:off
                return builder ()
                        .major (version.major ())
                        .minor (version.minor ())
                        .patch (version.patch ())
                        .updateNumber (version.updateNumber ())
                        .extra (version.extra ())
                        .metadata (version.metadata ());
                // @formatter:on
        }

        /**
         * Parses a version string and creates a {@link com.torchmind.utility.version.semantic.JavaVersion.Builder}
         * that represents the version.
         *
         * @param version the version.
         * @return the builder.
         *
         * @throws java.lang.IllegalArgumentException when the passed version has an invalid format.
         * @throws java.lang.NumberFormatException    when one or more elements contain invalid numbers.
         */
        @Nonnull
        public static Builder builder (@Nonnull String version) throws IllegalArgumentException, NumberFormatException {
                return builder ().parse (version);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo (@Nullable SemanticVersion version) {
                if (version instanceof JavaVersion) {
                        return this.compareTo (((JavaVersion) version));
                }

                return super.compareTo (version);
        }

        /**
         * Compares two versions.
         *
         * @param version the other version.
         * @return -1, 0 or 1 if the version is older than, equal or newer than the passed version.
         */
        public int compareTo (@Nullable JavaVersion version) {
                int semanticComparison = super.compareTo (version);
                if (semanticComparison != 0) {
                        return semanticComparison;
                }

                return Math.max (-1, Math.min (1, (this.updateNumber () - version.updateNumber ())));
        }

        /**
         * Retrieves the current Java version.
         *
         * @return the version.
         */
        @Nonnull
        public static JavaVersion current () {
                return currentBuilder ().build ();
        }

        /**
         * Parses the current Java version and creates a {@link com.torchmind.utility.version.semantic.JavaVersion.Builder}
         * that represents the version.
         *
         * @return the builder.
         */
        @Nonnull
        public static Builder currentBuilder () {
                return builder (System.getProperty ("java.version"));
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion extra (@Nullable String value) {
                return builder (this).extra (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion major (@Nonnegative int value) {
                return builder (this).major (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion metadata (@Nullable String value) {
                return builder (this).metadata (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion minor (@Nonnegative int value) {
                return builder (this).minor (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion newerThan (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                super.newerThan (version, consumer);
                return this;
        }

        /**
         * Parses a Java version string.
         *
         * @param version the string.
         * @return the version.
         *
         * @throws java.lang.IllegalArgumentException when the passed version has an invalid format.
         * @throws java.lang.NumberFormatException    when one or more elements contain invalid numbers.
         */
        @Nonnull
        public static JavaVersion of (@Nonnull String version) throws IllegalArgumentException, NumberFormatException {
                return builder (version).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion olderThan (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                super.olderThan (version, consumer);
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion patch (@Nonnegative int value) {
                return builder (this).patch (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion stable (@Nonnull Consumer<SemanticVersion> consumer) {
                super.stable (consumer);
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion unstable (@Nonnull Consumer<SemanticVersion> consumer) {
                super.unstable (consumer);
                return this;
        }

        /**
         * Retrieves the update number.
         *
         * @return the number.
         */
        @Nonnegative
        public int updateNumber () {
                return this.updateNumber;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals (@Nullable SemanticVersion version) {
                if (version instanceof JavaVersion) {
                        return this.equals (((JavaVersion) version));
                }

                return super.equals (version);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public JavaVersion equals (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                super.equals (version, consumer);
                return this;
        }

        /**
         * Checks whether this version and {@code version} are equal.
         *
         * @param version the version to compare to.
         * @return {@code true} if equal, {@code false} otherwise.
         */
        public boolean equals (@Nullable JavaVersion version) {
                if (version == null) {
                        return false;
                }
                if (version == this) {
                        return true;
                }

                if (this.updateNumber () != version.updateNumber ()) {
                        return false;
                }

                return super.equals (version);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String toString () {
                if (this.updateNumber () != 0) {
                        return super.toString () + "_" + this.updateNumber ();
                }

                return super.toString ();
        }

        /**
         * Provides a builder for Java versions.
         */
        @NotThreadSafe
        public static class Builder extends SemanticVersion.Builder {
                private int updateNumber;

                protected Builder () {
                        super ();
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public JavaVersion build () {
                        return (new JavaVersion (this.major (), this.minor (), this.patch (), this.extra (), this.metadata (), this.unstableVersionType (), this.updateNumber ()));
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder extra (@Nullable String extra) throws IllegalArgumentException {
                        super.extra (extra);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder major (@Nonnegative int major) {
                        super.major (major);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder metadata (@Nullable String metadata) throws IllegalArgumentException {
                        super.metadata (metadata);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder minor (@Nonnegative int minor) {
                        super.minor (minor);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                protected Builder parse (@Nonnull String version) {
                        int updateIndex = version.indexOf ('_');

                        if (updateIndex != -1) {

                                int extraIndex = version.indexOf ('-');
                                int metadataIndex = version.indexOf ('+');

                                int endIndex;

                                if (extraIndex != -1 && metadataIndex != -1) {
                                        endIndex = Math.min (extraIndex, metadataIndex);
                                } else if (extraIndex == -1 && metadataIndex == -1) {
                                        endIndex = version.length ();
                                } else {
                                        endIndex = Math.max (extraIndex, metadataIndex);
                                }

                                try {
                                        this.updateNumber (Integer.parseUnsignedInt (version.substring ((updateIndex + 1), endIndex)));
                                } catch (NumberFormatException ex) {
                                        this.updateNumber (0);
                                }

                                String tmp = version.substring (0, updateIndex);
                                if (endIndex != version.length ()) {
                                        tmp += version.substring (endIndex);
                                }

                                version = tmp;
                        }

                        super.parse (version);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder patch (@Nonnegative int patch) {
                        super.patch (patch);
                        return this;
                }

                /**
                 * {@inheritDoc}
                 */
                @Nonnull
                @Override
                public Builder reset () {
                        super.reset ();
                        return this;
                }

                /**
                 * Sets the update number.
                 *
                 * @param updateNumber the number.
                 * @return the builder.
                 */
                @Nonnull
                public Builder updateNumber (@Nonnegative int updateNumber) {
                        this.updateNumber = updateNumber;
                        return this;
                }

                /**
                 * Retrieves the update number.
                 *
                 * @return the number.
                 */
                @Nonnegative
                public int updateNumber () {
                        return this.updateNumber;
                }
        }
}
