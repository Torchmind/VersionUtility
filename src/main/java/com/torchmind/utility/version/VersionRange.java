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
package com.torchmind.utility.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents a range between two versions.
 *
 * @param <T> the version type.
 * @author Johannes Donath
 */
@ThreadSafe
public final class VersionRange<T extends IVersion<T>> {
        private final T startBound;
        private final T endBound;

        private final boolean startInclusive;
        private final boolean endInclusive;

        private VersionRange (@Nonnull T startBound, boolean startInclusive, @Nonnull T endBound, boolean endInclusive) {
                this.startBound = startBound;
                this.startInclusive = startInclusive;

                this.endBound = endBound;
                this.endInclusive = endInclusive;
        }

        /**
         * Retrieves a new {@link com.torchmind.utility.version.VersionRange.Builder}.
         *
         * @param <T> the version type.
         * @return the builder.
         */
        @Nonnull
        public static <T extends IVersion<T>> Builder<T> builder () {
                return (new Builder<> ());
        }

        /**
         * Retrieves a new {@link com.torchmind.utility.version.VersionRange.Builder} that replicates the bounds of
         * {@code range}.
         *
         * @param range the range.
         * @param <T>   the range version type.
         * @return the builder.
         */
        @Nonnull
        public static <T extends IVersion<T>> Builder<T> builder (@Nonnull VersionRange<T> range) {
                // @formatter:off
                return VersionRange.<T>builder ()
                                .startBound (range.startBound ())
                                .startInclusive (range.startInclusive ())
                                .endBound (range.endBound ())
                                .endInclusive (range.endInclusive ());
                // @formatter:on
        }

        /**
         * Retrieves the ending bound.
         *
         * @return the ending version.
         */
        @Nonnull
        public T endBound () {
                return this.endBound;
        }

        /**
         * Creates a mutated copy of this range with {@code endBound} as it's ending bound.
         *
         * @param endBound the ending version.
         * @return the mutated copy.
         */
        @Nonnull
        public VersionRange<T> endBound (@Nonnull T endBound) {
                return builder (this).endBound (endBound).build ();
        }

        /**
         * Checks whether the ending version is part of the range.
         *
         * @return {@code true} if part of the range, {@code false} otherwise.
         */
        public boolean endInclusive () {
                return this.endInclusive;
        }

        /**
         * If {@code value} is {@code true}, creates a mutated copy of this range which includes the ending version,
         * otherwise creates a copy that excludes it.
         *
         * @param value the value.
         * @return the mutated copy.
         */
        @Nonnull
        public VersionRange<T> endInclusive (boolean value) {
                return builder (this).endInclusive (value).build ();
        }

        /**
         * Checks whether {@code version} is part of this range.
         *
         * @param version the version.
         * @return {@code true} if part of this set, {@code false} otherwise.
         */
        public boolean matches (@Nullable T version) {
                if (this.startBound.newerThan (version)) {
                        return false;
                }
                if (this.endBound.olderThan (version)) {
                        return false;
                }
                if (!this.startInclusive && this.startBound.equals (version)) {
                        return false;
                }
                if (!this.endInclusive && this.endBound.equals (version)) {
                        return false;
                }
                return true;
        }

        /**
         * Checks whether {@code version} is part of this range and executes {@code consumer} if so.
         *
         * @param version  the version.
         * @param consumer the consumer.
         * @return the instance.
         */
        @Nonnull
        public VersionRange<T> matches (@Nullable T version, @Nonnull Consumer<T> consumer) {
                if (this.matches (version)) {
                        consumer.accept (version);
                }
                return this;
        }

        /**
         * Retrieves a set containing all elements that match this range.
         *
         * @param versions the original set.
         * @return the matching set.
         */
        @Nonnull
        public Set<T> matching (@Nonnull Set<T> versions) {
                // @formatter:off
                return versions.stream ()
                               .filter (this::matches)
                                        .collect (Collectors.toSet ());
                // @formatter:on
        }

        /**
         * Retrieves the starting bound.
         *
         * @return the starting version.
         */
        @Nonnull
        public T startBound () {
                return this.startBound;
        }

        /**
         * Creates a mutated copy of this range with {@code startBound} as it's starting bound.
         *
         * @param startBound the starting version.
         * @return the mutated copy.
         */
        @Nonnull
        public VersionRange<T> startBound (@Nonnull T startBound) {
                return builder (this).startBound (startBound).build ();
        }

        /**
         * Checks whether the starting version is part of the range.
         *
         * @return {@code true} if part of the range, {@code false} otherwise.
         */
        public boolean startInclusive () {
                return this.startInclusive;
        }

        /**
         * If {@code value} is {@code true}, creates a mutated copy of this range which includes the starting version,
         * otherwise creates a copy that excludes it.
         *
         * @param value the value.
         * @return the mutated copy.
         */
        @Nonnull
        public VersionRange<T> startInclusive (boolean value) {
                return builder (this).startInclusive (value).build ();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings ("unchecked")
        public boolean equals (Object object) {
                if (this == object) {
                        return true;
                }
                if (object == null || getClass () != object.getClass ()) {
                        return false;
                }

                if (!this.startBound.getClass ().equals (((VersionRange<?>) object).startBound.getClass ())) {
                        return false;
                }
                VersionRange<T> that = (VersionRange<T>) object;

                if (startInclusive != that.startInclusive) {
                        return false;
                }

                if (endInclusive != that.endInclusive) {
                        return false;
                }

                if (!startBound.equals (that.startBound)) {
                        return false;
                }

                return endBound.equals (that.endBound);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode () {
                int result = startBound.hashCode ();
                result = 31 * result + endBound.hashCode ();
                result = 31 * result + (startInclusive ? 1 : 0);
                result = 31 * result + (endInclusive ? 1 : 0);
                return result;
        }

        /**
         * Provides a factory for {@link com.torchmind.utility.version.VersionRange} instances.
         *
         * @param <T> the version type.
         */
        @NotThreadSafe
        public static final class Builder<T extends IVersion<T>> {
                private T startBound;
                private boolean startInclusive;

                private T endBound;
                private boolean endInclusive;

                private Builder () {
                }

                /**
                 * Builds an instance of {@link com.torchmind.utility.version.VersionRange} and resets the builder.
                 *
                 * @return the range.
                 *
                 * @throws java.lang.IllegalStateException when {@code startBound} or {@code endBound} are null.
                 */
                @Nonnull
                public VersionRange<T> build () throws IllegalStateException {
                        if (this.startBound == null) {
                                throw new IllegalStateException ("Missing starting bound");
                        }
                        if (this.endBound == null) {
                                throw new IllegalStateException ("Missing ending bound");
                        }

                        try {
                                return (new VersionRange<> (this.startBound, this.startInclusive, this.endBound, this.endInclusive));
                        } finally {
                                this.reset ();
                        }
                }

                /**
                 * Retrieves the ending bound.
                 *
                 * @return the bound.
                 */
                @Nullable
                public T endBound () {
                        return this.endBound;
                }

                /**
                 * Sets the ending bound.
                 *
                 * @param endBound the bound.
                 * @return the builder.
                 */
                @Nonnull
                public Builder<T> endBound (@Nullable T endBound) {
                        this.endBound = endBound;
                        return this;
                }

                /**
                 * Checks whether the ending bound is part of the range.
                 *
                 * @return {@code true} if part of range, {@code false} otherwise.
                 */
                public boolean endInclusive () {
                        return this.endInclusive;
                }

                /**
                 * Sets whether the ending bound is part of the range.
                 *
                 * @param endInclusive if {@code true}, the bound is part of the range, if {@code false} it is excluded.
                 * @return the builder.
                 */
                @Nonnull
                public Builder<T> endInclusive (boolean endInclusive) {
                        this.endInclusive = endInclusive;
                        return this;
                }

                /**
                 * Resets the builder instance.
                 *
                 * @return the builder.
                 */
                @Nonnull
                public Builder<T> reset () {
                        // @formatter:off
                        return this
                                .startBound (null)
                                .startInclusive (true)

                                .endBound (null)
                                .endInclusive (false);
                        // @formatter:on
                }

                /**
                 * Retrieves the starting bound.
                 *
                 * @return the starting bound.
                 */
                @Nullable
                public T startBound () {
                        return this.startBound;
                }

                /**
                 * Sets the starting bound.
                 *
                 * @param startBound the bound.
                 * @return the builder.
                 */
                @Nonnull
                public Builder<T> startBound (@Nullable T startBound) {
                        this.startBound = startBound;
                        return this;
                }

                /**
                 * Checks whether the starting bound is part of the range.
                 *
                 * @return {@code true} if part of range, {@code false} otherwise.
                 */
                public boolean startInclusive () {
                        return this.startInclusive;
                }

                /**
                 * Specifies whether the starting bound is part of the range.
                 *
                 * @param startInclusive if {@code true} the bound is part of the range, if {@code false} it is excluded.
                 * @return the builder.
                 */
                public Builder<T> startInclusive (boolean startInclusive) {
                        this.startInclusive = startInclusive;
                        return this;
                }
        }
}