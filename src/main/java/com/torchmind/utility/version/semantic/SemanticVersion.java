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

import com.torchmind.utility.version.IVersion;
import com.torchmind.utility.version.UnstableVersionType;
import com.torchmind.utility.version.VersionRange;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Represents a semantic version.
 *
 * @author Johannes Donath
 */
@ThreadSafe
public class SemanticVersion implements IVersion<SemanticVersion> {
        private final int major;
        private final int minor;
        private final int patch;

        private final String extra;
        private final String metadata;
        private final UnstableVersionType unstableVersionType;

        private SemanticVersion (@Nonnegative int major, @Nonnegative int minor, @Nonnegative int patch, @Nullable String extra, @Nullable String metadata, @Nullable UnstableVersionType unstableVersionType) {
                this.major = major;
                this.minor = minor;
                this.patch = patch;

                this.extra = extra;
                this.metadata = metadata;
                this.unstableVersionType = unstableVersionType;
        }

        /**
         * Creates a new {@link com.torchmind.utility.version.semantic.SemanticVersion.Builder} instance.
         *
         * @return the builder.
         */
        @Nonnull
        public static Builder builder () {
                return (new Builder ());
        }

        /**
         * Creates a new {@link com.torchmind.utility.version.semantic.SemanticVersion.Builder} replicating the values
         * of {@code version}.
         *
         * @param version the version to replicate.
         * @return the builder.
         */
        @Nonnull
        public static Builder builder (@Nonnull SemanticVersion version) {
                // @formatter:off
                return builder ()
                        .major (version.major ())
                        .minor (version.minor ())
                        .patch (version.patch ())
                        .extra (version.extra ())
                        .metadata (version.metadata ());
                // @formatter:on
        }

        /**
         * Creates a new {@link com.torchmind.utility.version.semantic.SemanticVersion.Builder} based on values found
         * within a string.
         *
         * @param version the version string.
         * @return the builder.
         *
         * @throws java.lang.IllegalArgumentException when one or more bits are not within SemVer bounds.
         * @throws java.lang.NumberFormatException    when one or more numeric bits have invalid values.
         */
        @Nonnull
        public static Builder builder (@Nonnull String version) throws IllegalArgumentException, NumberFormatException {
                Builder builder = builder ();
                String extra = null,
                        metadata = null;

                // Due to their nature extra and metadata bits will be extracted first and removed from the version
                // string to simplify further element extraction.
                // Note that extra and metadata bits may not contain the special characters "-" and "+" as they might
                // confuse further parsing.
                {
                        int extraOffset = version.indexOf ('-');
                        int metadataOffset = version.indexOf ('+');

                        if (extraOffset != -1) {
                                extra = version.substring ((extraOffset + 1));
                        }

                        if (metadataOffset != -1) {
                                metadata = version.substring ((metadataOffset + 1));
                        }

                        if (extra != null && metadataOffset > extraOffset) {
                                extra = extra.substring (0, extra.indexOf ('+'));
                        } else if (metadata != null && extraOffset > metadataOffset) {
                                metadata = metadata.substring (0, metadata.indexOf ('-'));
                        }

                        version = version.substring (0, Math.min ((extraOffset != -1 ? extraOffset : version.length ()), (metadataOffset != -1 ? metadataOffset : version.length ())));

                        // @formatter:off
                        builder
                                .extra (extra)
                                .metadata (metadata);
                        // @formatter:on
                }

                // Next up the root version bits (major, minor, patch) are extracted from the remaining version string
                // using a functional interface is declared here to simplify extraction further.
                // Note that this method has not been extracted into a separate private method as it is only used within
                // this specific context and would thus only pollute the overall object scope.
                {
                        BiFunction<String, Consumer<Integer>, String> function = (i, c) -> {
                                if (i.isEmpty ()) {
                                        return i;
                                }

                                int offset = i.indexOf ('.');

                                if (offset == -1) {
                                        c.accept (Integer.parseUnsignedInt (i));
                                        return "";
                                }

                                c.accept (Integer.parseUnsignedInt (i.substring (0, offset)));
                                return i.substring ((offset + 1));
                        };

                        version = function.apply (version, builder::major);
                        version = function.apply (version, builder::minor);
                        function.apply (version, builder::patch);
                }

                return builder;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compareTo (@Nullable SemanticVersion version) {
                // in case null is passed we assume that this version is in fact newer to simplify libraries relying
                // on this implementation.
                // additionally we will simply run a quick instance equality check to shorten the code path in case
                // the very same object finds it's way here.
                if (version == null) {
                        return 1;
                }
                if (this.equals (version)) {
                        return 0;
                }

                // the root version bits are first compared here as their checks are the fastest and do not require
                // any special voodoo or black magic.
                if (this.major () > version.major ()) {
                        return 1;
                }
                if (this.major () < version.major ()) {
                        return -1;
                }

                if (this.minor () > version.minor ()) {
                        return 1;
                }
                if (this.minor () < version.minor ()) {
                        return -1;
                }

                if (this.patch () > version.patch ()) {
                        return 1;
                }
                if (this.patch () < version.patch ()) {
                        return -1;
                }

                // if one of the versions is unstable it is now assumed to be "lesser" (older) than this instance due
                // to the previous checks validating that the version is equal up to this point. This usually means that
                // a version carries an extra element and was thus marked as unstable.
                if (!this.unstable () && version.unstable ()) {
                        return 1;
                }
                if (this.unstable () && !version.unstable ()) {
                        return -1;
                }

                // In case both versions are marked as unstable, their version types are compared.
                // Note that 0.X versions are always marked unstable so a non-present unstable version type will always
                // refer to such a release thus making versions that carry a specific extra flag "lesser" (older).
                // For example: 0.0 vs. 0.0-alpha
                Optional<UnstableVersionType> thisVersionTypeWrapper = this.unstableVersionType ();
                Optional<UnstableVersionType> otherVersionTypeWrapper = version.unstableVersionType ();

                if (!thisVersionTypeWrapper.isPresent () && otherVersionTypeWrapper.isPresent ()) {
                        return 1;
                }
                if (thisVersionTypeWrapper.isPresent () && !otherVersionTypeWrapper.isPresent ()) {
                        return -1;
                }

                // If both values are actually present, version types are compared to sort out version differences
                // between snapshot, alpha, beta and release candidate versions.
                UnstableVersionType thisVersionType = thisVersionTypeWrapper.get ();
                UnstableVersionType otherVersionType = otherVersionTypeWrapper.get ();

                int typeComparison = thisVersionType.compareTo (otherVersionType);
                if (typeComparison != 0) {
                        return typeComparison;
                }

                // If the versions are equal up to this point, the revision bit is extracted and compared to determine
                // the end result. If both values are equal the version is assumed equal by SemVer standards.
                // For example: 1.0-alpha vs. 1.0-alpha.1
                return Math.max (-1, Math.min (1, (this.extraRevision () - version.extraRevision ())));
        }

        /**
         * Retrieves the extra version bit (if any).
         *
         * @return the bit.
         */
        @Nullable
        public String extra () {
                return this.extra;
        }

        /**
         * Creates a mutated copy which has it's extra bit set to {@code value}.
         *
         * @param value the bit.
         * @return the mutated version.
         */
        @Nonnull
        public SemanticVersion extra (@Nullable String value) {
                // @formatter:off
                return builder (this)
                        .extra (value)
                        .build ();
                // @formatter:on
        }

        /**
         * Retrieves the version revision (part of the extra bit) or zero if none is present.
         *
         * @return the bit.
         */
        @Nonnegative
        public int extraRevision () {
                String extra = this.extra ();

                if (extra == null) {
                        return 0;
                }

                int index = extra.indexOf ('.');

                if (index == -1) {
                        return 0;
                }

                try {
                        return Integer.parseUnsignedInt (extra.substring ((index + 1)));
                } catch (NumberFormatException ex) {
                        return 0;
                }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnegative
        public int major () {
                return this.major;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion major (@Nonnegative int value) {
                // @formatter:off
                return builder (this)
                        .major (value)
                        .build ();
                // @formatter:on
        }

        /**
         * Retrieves the metadata version bit (if any).
         *
         * @return the bit.
         */
        @Nullable
        public String metadata () {
                return this.metadata;
        }

        /**
         * Creates a mutated copy which has it's metadata bit set to {@code value}.
         *
         * @param value the bit.
         * @return the mutated version.
         */
        @Nonnull
        public SemanticVersion metadata (@Nullable String value) {
                // @formatter:off
                return builder (this)
                        .metadata (value)
                        .build ();
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnegative
        public int minor () {
                return this.minor;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion minor (@Nonnegative int value) {
                // @formatter:off
                return builder (this)
                        .minor (value)
                        .build ();
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean newerThan (@Nullable SemanticVersion version) {
                return (this.compareTo (version) == 1);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion newerThan (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                if (this.newerThan (version)) {
                        consumer.accept (this);
                }

                return this;
        }

        /**
         * Creates a new {@link com.torchmind.utility.version.semantic.SemanticVersion} based on values found within a
         * string.
         *
         * @param version the version string.
         * @return the version.
         *
         * @throws java.lang.IllegalArgumentException when one or more bits are not within SemVer bounds.
         * @throws java.lang.NumberFormatException    when one or more numeric bits have invalid values.
         */
        @Nonnull
        public static SemanticVersion of (@Nonnull String version) throws IllegalArgumentException, NumberFormatException {
                // @formatter:off
                return builder (version)
                        .build ();
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean olderThan (@Nullable SemanticVersion version) {
                return (this.compareTo (version) == -1);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion olderThan (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                if (this.olderThan (version)) {
                        consumer.accept (this);
                }

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnegative
        public int patch () {
                return this.patch;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion patch (@Nonnegative int value) {
                // @formatter:off
                return builder (this)
                        .patch (value)
                        .build ();
                // @formatter:on
        }

        /**
         * Creates a version range consisting of two {@link com.torchmind.utility.version.semantic.SemanticVersion} instances.
         *
         * @param version1 the starting bound.
         * @param version2 the ending bound.
         * @return the range.
         */
        @Nonnull
        public static VersionRange<SemanticVersion> range (@Nonnull SemanticVersion version1, @Nonnull SemanticVersion version2) {
                // @formatter:off
                return VersionRange.<SemanticVersion>builder ()
                                        .startBound (version1)
                                        .endBound (version2)
                                        .build ();
                // @formatter:on
        }

        /**
         * Creates a version range consisting of two {@link com.torchmind.utility.version.semantic.SemanticVersion} instances.
         *
         * @param version1 the starting bound.
         * @param version2 the ending bound.
         * @return the range.
         */
        @Nonnull
        public static VersionRange<SemanticVersion> range (@Nonnull String version1, @Nonnull String version2) {
                return range (of (version1), of (version2));
        }

        /**
         * Creates a version range consisting of two {@link com.torchmind.utility.version.semantic.SemanticVersion}
         * instances declared by a string utilizing the interval notation.
         *
         * @param range the range string.
         * @return the range.
         *
         * @throws java.lang.IllegalArgumentException when the range or one of the version strings is invalid.
         * @throws java.lang.NumberFormatException    when one or more bits are invalid.
         */
        @Nonnull
        public static VersionRange<SemanticVersion> range (@Nonnull String range) throws IllegalArgumentException, NumberFormatException {
                VersionRange.Builder<SemanticVersion> builder = VersionRange.<SemanticVersion>builder ();

                int offset = range.indexOf (',');
                if (offset == -1) {
                        throw new IllegalArgumentException ("Invalid version range: Missing separator");
                }

                String startBound = range.substring (0, offset);
                String endBound = range.substring ((offset + 1));

                if (startBound.startsWith ("[")) {
                        builder.startInclusive (true);
                } else if (!startBound.startsWith ("(")) {
                        throw new IllegalArgumentException ("Invalid version range: Missing starting bound type");
                }


                if (endBound.endsWith ("]")) {
                        builder.endInclusive (true);
                } else if (!endBound.endsWith (")")) {
                        throw new IllegalArgumentException ("Invalid version range: Missing ending bound type");
                }

                startBound = startBound.substring (1);
                endBound = endBound.substring (0, (endBound.length () - 1));

                // @formatter:off
                return builder
                        .startBound (of (startBound))
                        .endBound (of (endBound))
                        .build ();
                // @formatter:on
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean stable () {
                return (this.unstableVersionType == null && this.major () > 0);
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion stable (@Nonnull Consumer<SemanticVersion> consumer) {
                if (this.stable ()) {
                        consumer.accept (this);
                }
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean unstable () {
                return !this.stable ();
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion unstable (@Nonnull Consumer<SemanticVersion> consumer) {
                if (this.unstable ()) {
                        consumer.accept (this);
                }
                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public Optional<UnstableVersionType> unstableVersionType () {
                return Optional.ofNullable (this.unstableVersionType);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals (Object object) {
                if (this == object) {
                        return true;
                }
                if (object == null || !(object instanceof SemanticVersion)) {
                        return false;
                }

                return this.equals (((SemanticVersion) object));

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals (@Nullable SemanticVersion version) {
                // Do a quick null & instance check to shorten down the code path as far as possible and prevent lags
                // by frequent comparisons within applications.
                if (version == null) {
                        return false;
                }
                if (version == this) {
                        return true;
                }

                // Check for the root version bits first as they are the easiest to compare and shorten the code path
                // majorly when different.
                if (this.major () != version.major ()) {
                        return false;
                }
                if (this.minor () != version.minor ()) {
                        return false;
                }
                if (this.patch () != version.patch ()) {
                        return false;
                }

                if (this.unstable () != version.unstable ()) {
                        return false;
                }

                // Compare the instability types first to avoid need of extracting extra version bit which is quite
                // expensive at the moment.
                // TODO: The extra revision may be cached to reduce load on frequent comparisons.
                Optional<UnstableVersionType> thisVersionTypeWrapper = this.unstableVersionType ();
                Optional<UnstableVersionType> otherVersionTypeWrapper = version.unstableVersionType ();

                if (thisVersionTypeWrapper.isPresent () != otherVersionTypeWrapper.isPresent ()) {
                        return false;
                }
                if (!thisVersionTypeWrapper.isPresent ()) {
                        return true;
                }

                UnstableVersionType thisVersionType = thisVersionTypeWrapper.get ();
                UnstableVersionType otherVersionType = otherVersionTypeWrapper.get ();

                if (thisVersionType != otherVersionType) {
                        return false;
                }

                // In the worst case extraction of revision bits from the extra bit is required to determine absolute
                // equality. This is only needed for versions that are equal up to this point.
                // For example: 1.0-alpha vs. 1.0-alpha.1
                return (this.extraRevision () == version.extraRevision ());
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public SemanticVersion equals (@Nullable SemanticVersion version, @Nonnull Consumer<SemanticVersion> consumer) {
                if (this.equals (version)) {
                        consumer.accept (this);
                }

                return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode () {
                int result = major;
                result = 31 * result + minor;
                result = 31 * result + patch;
                result = 31 * result + (this.unstableVersionType != null ? this.unstableVersionType.ordinal () : 0);
                result = 31 * result + this.extraRevision ();
                return result;
        }

        /**
         * {@inheritDoc}
         */
        @Nonnull
        @Override
        public String toString () {
                StringBuilder builder = new StringBuilder ();

                // To unify versions and make them look sane to users of the software the minimum of present elements
                // within the string version are major and minor.
                builder.append (this.major ());
                builder.append ('.');
                builder.append (this.minor ());

                // In case further elements are present within the version they are appended in a unified order to the
                // end of the version number as per SemVer specification.
                if (this.patch () > 0) {
                        builder.append ('.');
                        builder.append (this.patch);
                }

                if (this.extra () != null) {
                        builder.append ('-');
                        builder.append (this.extra ());
                }

                if (this.metadata () != null) {
                        builder.append ('+');
                        builder.append (this.metadata ());
                }

                return builder.toString ();
        }

        /**
         * Provides a factory for {@link com.torchmind.utility.version.semantic.SemanticVersion} instances.
         */
        @NotThreadSafe
        public static class Builder {
                private int major = 0;
                private int minor = 0;
                private int patch = 0;

                private String extra = null;
                private String metadata = null;
                private UnstableVersionType unstableVersionType = null;

                protected Builder () {
                }

                /**
                 * Builds the {@link com.torchmind.utility.version.semantic.SemanticVersion} instance.
                 *
                 * @return the version.
                 */
                @Nonnull
                public SemanticVersion build () {
                        try {
                                return (new SemanticVersion (this.major (), this.minor (), this.patch (), this.extra (), this.metadata (), this.unstableVersionType));
                        } finally {
                                this.reset ();
                        }
                }

                /**
                 * Retrieves the extra version bit (if any).
                 *
                 * @return the bit.
                 */
                @Nullable
                public String extra () {
                        return this.extra;
                }

                /**
                 * Sets the extra version bit (if any).
                 *
                 * @param extra the bit.
                 * @return the builder.
                 *
                 * @throws java.lang.IllegalArgumentException when an invalid character is present.
                 */
                @Nonnull
                public Builder extra (@Nullable String extra) throws IllegalArgumentException {
                        // To shorten the code path the version type is determined early on during building and passed
                        // along to new version objects.
                        if (extra != null) {
                                // Generally extra and metadata elements may not contain the special characters "-" and "+" as
                                // they may break parsing later on. Thus all strings passed that contain one or more of these
                                // characters will be rejected.
                                if (extra.indexOf ('-') != -1) {
                                        throw new IllegalArgumentException ("Invalid special character in extra bit: -");
                                }
                                if (extra.indexOf ('+') != -1) {
                                        throw new IllegalArgumentException ("Invalid special character in extra bit: +");
                                }

                                String tmp = extra;

                                if (tmp.indexOf ('.') != -1) {
                                        tmp = tmp.substring (0, tmp.indexOf ('.'));
                                }

                                this.unstableVersionType = UnstableVersionType.byAlias (tmp).get ();
                        } else {
                                this.unstableVersionType = null;
                        }

                        this.extra = extra;
                        return this;
                }

                /**
                 * Retrieves the major version bit.
                 *
                 * @return the bit.
                 */
                @Nonnegative
                public int major () {
                        return this.major;
                }

                /**
                 * Sets the major version bit.
                 *
                 * @param major the bit.
                 * @return the builder.
                 */
                @Nonnull
                public Builder major (@Nonnegative int major) {
                        this.major = major;
                        return this;
                }

                /**
                 * Retrieves the version metadata (if any).
                 *
                 * @return the metadata.
                 */
                @Nullable
                public String metadata () {
                        return this.metadata;
                }

                /**
                 * Sets the version metadata (if any).
                 *
                 * @param metadata the metadata.
                 * @return the builder.
                 *
                 * @throws java.lang.IllegalArgumentException when an invalid character is present.
                 */
                @Nonnull
                public Builder metadata (@Nullable String metadata) throws IllegalArgumentException {
                        // Generally extra and metadata elements may not contain the special characters "-" and "+" as
                        // they may break parsing later on. Thus all strings passed that contain one or more of these
                        // characters will be rejected.
                        if (metadata != null) {
                                if (metadata.indexOf ('-') != -1) {
                                        throw new IllegalArgumentException ("Invalid special character in metadata bit: -");
                                }
                                if (metadata.indexOf ('+') != -1) {
                                        throw new IllegalArgumentException ("Invalid special character in metadata bit: +");
                                }
                        }

                        this.metadata = metadata;
                        return this;
                }

                /**
                 * Retrieves the minor version bit.
                 *
                 * @return the bit.
                 */
                @Nonnegative
                public int minor () {
                        return this.minor;
                }

                /**
                 * Sets the minor version bit.
                 *
                 * @param minor the bit.
                 * @return the builder.
                 */
                @Nonnull
                public Builder minor (@Nonnegative int minor) {
                        this.minor = minor;
                        return this;
                }

                /**
                 * Retrieves the patch version bit.
                 *
                 * @return the bit.
                 */
                @Nonnegative
                public int patch () {
                        return this.patch;
                }

                /**
                 * Sets the patch version bit.
                 *
                 * @param patch the bit.
                 * @return the builder.
                 */
                @Nonnull
                public Builder patch (@Nonnegative int patch) {
                        this.patch = patch;
                        return this;
                }

                /**
                 * Resets the builder.
                 *
                 * @return the builder.
                 */
                @Nonnull
                public Builder reset () {
                        // @formatter:off
                        return this
                                .major (0)
                                .minor (0)
                                .patch (0)
                                .extra (null)
                                .metadata (null);
                        // @formatter:on
                }
        }
}
