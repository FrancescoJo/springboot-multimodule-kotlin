/*
 * springboot-multimodule-kotlin skeleton.
 * Under no licences and warranty.
 */
package com.github.fj.lib.text

import de.skuzzle.semantic.Version
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Thin wrapper for [de.skuzzle.semantic.Version], for better extensibility.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Oct - 2018
 */
class SemanticVersion private constructor(private val version: Version?) : Comparable<SemanticVersion> {
    // region Property accessor methods
    /**
     * Gets this version's major number. -1 if performed on [EMPTY] instance.
     *
     * @return The major version.
     */
    fun getMajor(): Int = version?.major ?: -1

    /**
     * Gets this version's minor number. -1 if performed on [EMPTY] instance.
     *
     * @return The minor version.
     */
    fun getMinor(): Int = version?.minor ?: -1

    /**
     * Gets this version's path number. -1 if performed on [EMPTY] instance.
     * @return The patch number.
     */
    fun getPatch(): Int = version?.patch ?: -1

    /**
     * Gets the pre release identifier of this version. If this version has no such
     * identifier, an empty string is returned.
     *
     * Note: This method will always reconstruct a new String by joining the single
     * identifier parts.
     *
     * @return This version's pre release identifier or an empty String if this version
     * has no such identifier.
     */
    fun getPreRelease(): String = version?.preRelease ?: ""

    /**
     * Gets this version's build meta data. If this version has no build meta data, the
     * returned string is empty.
     *
     * Note: This method will always reconstruct a new String by joining the single
     * identifier parts.
     *
     * @return The build meta data or an empty String if this version has no build meta
     * data.
     */
    fun getBuildMetaData(): String = version?.buildMetaData ?: ""
    // endregion

    // region Property manipulation methods
    /**
     * Given this Version, returns the next major Version. That is, the major part is
     * incremented by 1 and the remaining parts are set to 0. This also drops the
     * pre-release and build-meta-data.
     *
     * @return The incremented version.
     */
    fun nextMajor(): SemanticVersion = if (version == null) {
        EMPTY
    } else {
        with(version) {
            return@with SemanticVersion(Version.create(nextMajor().major, minor, patch)
                    .withPreRelease(preRelease)
                    .withBuildMetaData(buildMetaData)
            )
        }
    }

    /**
     * Given this version, returns the next minor version. That is, the major part remains
     * the same, the minor version is incremented and all other parts are reset/dropped.
     *
     * @return The incremented version.
     */
    fun nextMinor(): SemanticVersion = if (version == null) {
        EMPTY
    } else {
        with(version) {
            return@with SemanticVersion(Version.create(major, nextMinor().minor, patch)
                    .withPreRelease(preRelease)
                    .withBuildMetaData(buildMetaData)
            )
        }
    }

    /**
     * Given this version, returns the next patch version. That is, the major and minor
     * parts remain the same, the patch version is incremented and all other parts are
     * reset/dropped.
     *
     * @return The incremented version.
     */
    fun nextPatch(): SemanticVersion = if (version == null) {
        EMPTY
    } else {
        with(version) {
            return@with SemanticVersion(Version.create(major, minor, nextPatch().patch)
                    .withPreRelease(preRelease)
                    .withBuildMetaData(buildMetaData)
            )
        }
    }

    /**
     * Derives a new Version instance from this one by only incrementing the pre-release
     * identifier. The build-meta-data will be dropped, all other fields remain the same.
     *
     * The incrementation of the pre-release identifier behaves as follows:
     *
     *  * In case the identifier is currently empty, it becomes "1" in the result.
     *  * If the identifier's last part is numeric, that last part will be incremented in
     * the result.
     *  * If the last part is not numeric, the identifier is interpreted as
     * `identifier.0` which becomes `identifier.1` after increment.
     *
     * Examples:
     *
     * <table summary="Pre-release identifier incrementation behavior">
     * <tr>
     * <th>Version</th>
     * <th>After increment</th>
     * </tr>
     * <tr>
     * <td>1.2.3</td>
     * <td>1.2.3-1</td>
     * </tr>
     * <tr>
     * <td>1.2.3+build.meta.data</td>
     * <td>1.2.3-1</td>
     * </tr>
     * <tr>
     * <td>1.2.3-foo</td>
     * <td>1.2.3-foo.1</td>
     * </tr>
     * <tr>
     * <td>1.2.3-foo.1</td>
     * <td>1.2.3-foo.2</td>
     * </tr>
     * </table>
     *
     * @return The incremented Version.
     * @since 1.2.0
     */
    fun nextPreRelease(): SemanticVersion = if (version == null) {
        EMPTY
    } else {
        with(version) {
            return@with SemanticVersion(Version.create(major, minor, patch)
                    .withPreRelease(nextPreRelease().preRelease)
                    .withBuildMetaData(buildMetaData)
            )
        }
    }

    /**
     * Derives a new Version instance from this one by only incrementing the
     * build-meta-data identifier. All other fields remain the same.
     *
     * The incrementation of the build-meta-data identifier behaves as follows:
     *
     *  * In case the identifier is currently empty, it becomes "1" in the result.
     *  * If the identifier's last part is numeric, that last part will be incremented in
     * the result. **Leading 0's will be removed**.
     *  * If the last part is not numeric, the identifier is interpreted as
     * `identifier.0` which becomes `identifier.1` after increment.
     *
     * Examples:
     *
     * <table summary="Build meta data incrementation behavior">
     * <tr>
     * <th>Version</th>
     * <th>After increment</th>
     * </tr>
     * <tr>
     * <td>1.2.3</td>
     * <td>1.2.3+1</td>
     * </tr>
     * <tr>
     * <td>1.2.3-pre.release</td>
     * <td>1.2.3-pre.release+1</td>
     * </tr>
     * <tr>
     * <td>1.2.3+foo</td>
     * <td>1.2.3+foo.1</td>
     * </tr>
     * <tr>
     * <td>1.2.3+foo.1</td>
     * <td>1.2.3+foo.2</td>
     * </tr>
     * </table>
     *
     * @return The incremented Version.
     * @since 1.2.0
     */
    fun nextBuildMetaData(): SemanticVersion = if (version == null) {
        EMPTY
    } else {
        with(version) {
            return@with SemanticVersion(Version.create(major, minor, patch)
                    .withPreRelease(preRelease)
                    .withBuildMetaData(nextBuildMetaData().buildMetaData)
            )
        }
    }
    // endregion

    // region Comparison methods
    /**
     * Determines whether major version of this object is valid.
     *
     * @return `true` iff this version's major part is > -1.
     */
    fun isValidMajor(): Boolean = version?.major ?: Integer.MIN_VALUE > -1

    /**
     * Determines whether minor version of this object is valid.
     *
     * @return `true` iff this version's minor part is > -1.
     */
    fun isValidMinor(): Boolean = version?.minor ?: Integer.MIN_VALUE > -1

    /**
     * Determines whether patch version of this object is valid.
     *
     * @return `true` iff this version's patch part is > -1.
     */
    fun isValidPatch(): Boolean = version?.patch ?: Integer.MIN_VALUE > -1

    /**
     * Determines whether this version is still under initial development.
     *
     * @return `true` iff this version's major part is zero.
     */
    fun isInitialDevelopment(): Boolean = version?.major == 0

    /**
     * Determines whether this is a pre release version.
     *
     * @return `true` iff [.getPreRelease] is not empty.
     */
    fun isPreRelease(): Boolean = version?.isPreRelease ?: false

    /**
     * Determines whether this version has a build meta data field.
     *
     * @return `true` iff [.getBuildMetaData] is not empty.
     */
    fun hasBuildMetaData(): Boolean = version?.hasBuildMetaData() ?: false

    override fun compareTo(other: SemanticVersion): Int = this.version?.compareTo(other.version)
            ?: -1
    // endregion

    // region Object identity methods
    override fun equals(other: Any?): Boolean = if (other !is SemanticVersion) {
        false
    } else {
        version?.equals(other.version) ?: false
    }

    override fun hashCode(): Int = version?.hashCode() ?: -1
    // endregion

    /**
     * Creates a String representation of this version by joining its parts together as by
     * the semantic version specification.
     *
     * @return The version as a String.
     */
    override fun toString(): String = version?.toString() ?: ""

    companion object {
        val EMPTY = SemanticVersion(null)

        /**
         * Semantic Version Specification to which this class complies.
         *
         * @since 0.2.0
         */
        val COMPLIANCE = SemanticVersion(Version.create(2, 0, 0))

        /**
         * Comparator for natural version ordering. See [.compare] for
         * more information.
         *
         * @since 0.2.0
         */
        val NATURAL_ORDER: Comparator<SemanticVersion> = Comparator { o1, o2 -> compare(o1, o2) }

        /**
         * Comparator for ordering versions with additionally considering the build meta data
         * field when comparing versions.
         *
         * Note: this comparator imposes orderings that are inconsistent with equals.
         *
         * @since 0.3.0
         */
        val WITH_BUILD_META_DATA_ORDER: Comparator<SemanticVersion> = Comparator { o1, o2 -> compareWithBuildMetaData(o1, o2) }

        fun parse(versionString: String?): SemanticVersion = if (versionString.isNullOrEmpty()) {
            EMPTY
        } else {
            SemanticVersion(Version.parseVersion(versionString))
        }

        /**
         * Returns the greater of the two given versions by comparing them using their natural
         * ordering. If both versions are equal, then the first argument is returned.
         *
         * @param v1 The first version.
         * @param v2 The second version.
         * @return The greater version.
         * @throws IllegalArgumentException If either argument is `null`.
         * @since 0.4.0
         */
        fun max(v1: SemanticVersion?, v2: SemanticVersion?): SemanticVersion {
            if (v1 == null || v2 == null) {
                throw IllegalArgumentException("Either v1 or v2 is/are null.")
            }

            return SemanticVersion(Version.max(v1.version, v2.version))
        }

        /**
         * Returns the lower of the two given versions by comparing them using their natural
         * ordering. If both versions are equal, then the first argument is returned.
         *
         * @param v1 The first version.
         * @param v2 The second version.
         * @return The lower version.
         * @throws IllegalArgumentException If either argument is `null`.
         * @since 0.4.0
         */
        fun min(v1: SemanticVersion?, v2: SemanticVersion?): SemanticVersion {
            if (v1 == null || v2 == null) {
                throw IllegalArgumentException("Either v1 or v2 is/are null.")
            }

            return SemanticVersion(Version.min(v1.version, v2.version))
        }

        /**
         * Tries to parse the given String as a semantic version and returns whether the
         * String is properly formatted according to the semantic version specification.
         *
         * Note: this method does not throw an exception upon `null` input, but
         * returns `false` instead.
         *
         * @param version The String to check.
         * @return Whether the given String is formatted as a semantic version.
         * @since 0.5.0
         */
        fun isValidVersion(version: String?): Boolean = Version.isValidVersion(version)

        /**
         * Returns whether the given String is a valid pre-release identifier. That is, this
         * method returns `true` if, and only if the `preRelease` parameter
         * is either the empty string or properly formatted as a pre-release identifier
         * according to the semantic version specification.
         *
         * Note: this method does not throw an exception upon `null` input, but
         * returns `false` instead.
         *
         * @param preRelease The String to check.
         * @return Whether the given String is a valid pre-release identifier.
         * @since 0.5.0
         */
        fun isValidPreRelease(preRelease: String?): Boolean = Version.isValidPreRelease(preRelease)

        /**
         * Returns whether the given String is a valid build meta data identifier. That is,
         * this method returns `true` if, and only if the `buildMetaData`
         * parameter is either the empty string or properly formatted as a build meta data
         * identifier according to the semantic version specification.
         *
         * Note: this method does not throw an exception upon `null` input, but
         * returns `false` instead.
         *
         * @param buildMetaData The String to check.
         * @return Whether the given String is a valid build meta data identifier.
         * @since 0.5.0
         */
        fun isValidBuildMetaData(buildMetaData: String?): Boolean = Version.isValidBuildMetaData(buildMetaData)

        /**
         * Compares two versions, following the *semantic version* specification. Here
         * is a quote from [semantic version 2.0.0 specification](http://semver.org/):
         *
         * * Precedence refers to how versions are compared to each other when ordered.
         * Precedence MUST be calculated by separating the version into major, minor, patch
         * and pre-release identifiers in that order (Build metadata does not figure into
         * precedence). Precedence is determined by the first difference when comparing each
         * of these identifiers from left to right as follows: Major, minor, and patch
         * versions are always compared numerically. Example: 1.0.0 &lt; 2.0.0 &lt; 2.1.0 &lt;
         * 2.1.1. When major, minor, and patch are equal, a pre-release version has lower
         * precedence than a normal version. Example: 1.0.0-alpha &lt; 1.0.0. Precedence for
         * two pre-release versions with the same major, minor, and patch version MUST be
         * determined by comparing each dot separated identifier from left to right until a
         * difference is found as follows: identifiers consisting of only digits are compared
         * numerically and identifiers with letters or hyphens are compared lexically in ASCII
         * sort order. Numeric identifiers always have lower precedence than non-numeric
         * identifiers. A larger set of pre-release fields has a higher precedence than a
         * smaller set, if all of the preceding identifiers are equal. Example: 1.0.0-alpha
         * &lt; 1.0.0-alpha.1 &lt; 1.0.0-alpha.beta &lt; 1.0.0-beta &lt; 1.0.0-beta.2 &lt;
         * 1.0.0-beta.11 &lt; 1.0.0-rc.1 &lt; 1.0.0. *
         *
         * This method fulfills the general contract for Java's [Comparators][Comparator]
         * and [Comparables][Comparable].
         *
         * @param v1 The first version for comparison.
         * @param v2 The second version for comparison.
         * @return A value below 0 iff `v1 &lt; v2`, a value above 0 iff
         * `v1 &gt; v2</tt> and 0 iff <tt>v1 = v2`.
         * @throws NullPointerException If either parameter is null.
         * @since 0.2.0
         */
        fun compare(v1: SemanticVersion?, v2: SemanticVersion?): Int = SemanticVersion.compare(v1, v2)

        /**
         * Compares two Versions with additionally considering the build meta data field if
         * all other parts are equal. Note: This is *not* part of the semantic version
         * specification.
         *
         * Comparison of the build meta data parts happens exactly as for pre release
         * identifiers. Considering of build meta data first kicks in if both versions are
         * equal when using their natural order.
         *
         * This method fulfills the general contract for Java's [Comparators][Comparator]
         * and [Comparables][Comparable].
         *
         * @param v1 The first version for comparison.
         * @param v2 The second version for comparison.
         * @return A value below 0 iff `v1 &lt; v2`, a value above 0 iff
         * `v1 &gt; v2</tt> and 0 iff <tt>v1 = v2`.
         * @throws NullPointerException If either parameter is null.
         * @since 0.3.0
         */
        fun compareWithBuildMetaData(v1: SemanticVersion?, v2: SemanticVersion?): Int {
            if (v1 == null || v2 == null) {
                throw IllegalArgumentException("Either v1 or v2 is/are null.")
            }

            return Version.compareWithBuildMetaData(v1.version, v2.version)
        }
    }
}
