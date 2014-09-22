BASED ON: https://github.com/Triple-T/gradle-play-publisher
gradle-play-publisher

=== Difference ===
* Use packageId from flavor config in build.gradle instead of AndroidManifest.xml
* Updated ReadMe with the dependencies I had to add to make this plugin work

=== USAGE ===
* Building:
** ./gradlew build
** copy build/libs/gradle-play-publisher-0.0.2.jar TO projectRoot/libs/

* Config:
** in project root make build.gradle look something like this (your dependencies could differ):


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.google.guava:guava:15.0'
        classpath 'com.android.tools.build:gradle:0.12+'
        classpath 'com.google.api-client:google-api-client-jackson2:1.19.0'
        classpath 'com.google.apis:google-api-services-androidpublisher:v2-rev5-1.19.0'
        classpath fileTree(dir: 'libs', includes: ['gradle-play-publisher-0.0.2.jar'])
    }
}

** In application build.gradle add: apply plugin: 'play'
** In application build.gradle add scope: 
play {
  serviceAccountEmail = "<YOUR SERVICE ACCOUNT EMAIL>"
  pk12File = file('PATH TO FILE')
  track = 'beta'
}
** execute ./gradlew tasks and look for tasks that have publish in the name
