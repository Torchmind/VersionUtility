[![License](https://img.shields.io/github/license/Torchmind/VersionUtility.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.txt)
[![Latest tag](https://img.shields.io/github/tag/Torchmind/VersionUtility.svg?style=flat-square)](https://github.com/Torchmind/VersionUtility/tags)
[![GitHub tag](https://img.shields.io/github/release/Torchmind/VersionUtility.svg?style=flat-square)](https://github.com/Torchmind/VersionUtility/releases)

Version Utility
===============

Table of Contents
-----------------
* [About](#about)
* [Contacts](#contacts)
* [Issues](#issues)
* [Building](#building)
* [Contributing](#contributing)

About
-----

Provides POJO representations for version numbers and ranges.

Contacts
--------

* [GitHub](https://github.com/Torchmind/VersionUtility)

Using
-----

When running maven you may simply add a new dependency along with our repository to your ```pom.xml```:

```xml
<repository>
  <id>sonatype</id>
  <name>Sonatype Open Source Repository</name>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>

<dependencies>
  <dependency>
    <groupId>com.torchmind.utility</groupId>
    <artifactId>version</artifactId>
    <version>2.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

Parsing [SemVer](http://semver.org) based versions:
```java
SemanticVersion version = SemanticVersion.of("1.0-alpha");
SemanticVersion version = SemanticVersion.of("0.1.0");
```

Comparing versions:
```java
IVersion version1 = ...;
IVersion version2 = ...;

if (version2.isNewerThan(version1)) {
  // Update application or something
}
```

Creating version ranges
```java
VersionRange<SemanticVersion> range = SemanticVersion.range("(1.0,2.0]");

// OR

SemanticVersion version1 = ...;
SemanticVersion version2 = ...;
VersionRange<SemanticVersion> range = SemanticVersion.range(version1, version2);
```

Checking range matches:
```java
VersionRange range = ...;
IVersion version = ...;

if (range.matches(version)) {
  // Dependency satisfied or something
}

// OR

VersionRange range = ...;
Set<IVersion> versions = ...;

Set<IVersion> matchingVersions = range.matching(versions);
```

Note: Developers may develop their own version parsers by implementing the ```com.torchmind.utility.version.IVersion```
interface. For an example please refer to ```com.torchmind.utility.version.semantic.SemanticVersion```.

Issues
------

You encountered problems with the library or have a suggestion? Create an issue!

1. Make sure your issue has not been fixed in a newer version (check the list of [closed issues](https://github.com/Torchmind/VersionUtility/issues?q=is%3Aissue+is%3Aclosed)
1. Create [a new issue](https://github.com/Torchmind/VersionUtility/issues/new) from the [issues page](https://github.com/Torchmind/VersionUtility/issues)
1. Enter your issue's title (something that summarizes your issue) and create a detailed description containing:
   - What is the expected result?
   - What problem occurs?
   - How to reproduce the problem?
   - Crash Log (Please use a [Pastebin](http://www.pastebin.com) service)
1. Click "Submit" and wait for further instructions

Building
--------

1. Clone this repository via ```git clone https://github.com/Torchmind/VersionUtility.git``` or download a [zip](https://github.com/Torchmind/VersionUtility/archive/master.zip)
1. Build the modification by running ```mvn clean install```
1. The resulting jars can be found in ```api/target```, ```core/target``` and ```mapper/target```

Contributing
------------

Before you add any major changes to the library you may want to discuss them with us (see [Contact](#contact)) as
we may choose to reject your changes for various reasons. All contributions are applied via [Pull-Requests](https://help.github.com/articles/creating-a-pull-request).
Patches will not be accepted. Also be aware that all of your contributions are made available under the terms of the
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt). Please read the [Contribution Guidelines](CONTRIBUTING.md)
for more information.
