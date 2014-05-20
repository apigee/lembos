### Prerequisites

Lembos is published to the [Sonatype Maven Repository][sonatype-maven-repo].  This repository is a managed system and it
has its own rules.  For more information:

* [How to Generate GPG Signatures With Maven][sonatype-gpg-maven]
* [Sonatype OSS Maven Repository Usage Guide][sonatype-oss-maven-guide]

Lembos also uses GitHub Releases to make downloading a release from GitHub very easy.  For more information:

* [GitHub Release Guide][github-release-guide]

### Publishing a Snapshot Release

Performing a snapshot release is very simple.  Just perform `mvn clean deploy`.  Since this process will use GPG to sign
the artifacts being published, you may need to pass the `-Dgpg.keyname` if your GPG has multiple keys and you'd like to
specify one to use.

### Publishing an Official Release

Performing an official release is a multi-step process, one for Sonatype and one for GitHub.

#### Maven

To perform the steps for Maven, run `mvn release:clean release:prepare release:perform -DperformRelease=true`, again
possibly needing to pass `-Dgpg.keyname`.

#### GitHub Releases

To perform the steps for GitHub Releases, create a new release by following these steps:

* Visit [Lembos Releases][lembos-release]
* Click the **Create a new release** button
* Specify the appropriate `Tag version`
* Specify a corresponding `Release title`
* Fill out the appropriate `Release notes`
* Attach the build artifact, the `lembos-{VERSION}.jar`
* Submit

### Cleanup

To clean up after this, run `mvn release:clean`.

[github-release-guide]: https://github.com/blog/1547-release-your-software
[lembos-release]: https://github.com/apigee/lembos/releases
[sonatype-gpg-maven]: https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven
[sonatype-maven-repo]: http://central.sonatype.org/
[sonatype-oss-maven-guide]: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
[sonatype-repo-browser]: https://oss.sonatype.org