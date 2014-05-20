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

To perform an official release, run `mvn release:clean release:prepare release:perform -DperformRelease=true`, again
possibly needing to pass `-Dgpg.keyname`.  If this succeeds, the artifact is in the Sonatype Staging repository and we
need to officially *release* it.  To do this, follow these instructions:

* Log into [Sonatype][sonatype-repo-browser]
* Click the `Staging Repositories` link on the left side of the page
* Browse to the `io-apigee-*` entry that is listed as `open` *(If there is more than one, find the appropriate one)*
* Check the box beside the release and click the `Close` button
* Fill in a description, typically something like "Lembos 1.0 release", and submit

This will kick off a Sonatype validation process and once it's complete, you will get an email.  When the email comes in
you should be able to refresh the page and the `Release` button will enable for the artifact when you check the box
beside the appropriate build.  Click that button and you should finish the Maven part of the release process.

At this point, Maven has been updated but we need to update the official GitHub Release.  To do this, follow these
instructions:

* Find the latest [Lembos Release][lembos-release] and click the `Edit` button beside it
* Fill in the proper `Release title` *(Just the version number will do)*
* Fill in the proper `Release notes`
* Add the `target/lembos-{VERSION}.jar` to the release
* Save

### Cleanup

**Note:** If you are performing an official release, you should not perform this step until you have updating the
GitHub Release as documented above.

To clean up after this, run `mvn release:clean`.

[github-release-guide]: https://github.com/blog/1547-release-your-software
[lembos-release]: https://github.com/apigee/lembos/releases
[sonatype-gpg-maven]: https://docs.sonatype.org/display/Repository/How+To+Generate+PGP+Signatures+With+Maven
[sonatype-maven-repo]: http://central.sonatype.org/
[sonatype-oss-maven-guide]: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
[sonatype-repo-browser]: https://oss.sonatype.org
