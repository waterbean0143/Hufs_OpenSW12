buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/'}
        maven{
            url "https://maven.google.com"
        }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.3'
        classpath 'com.google.gms:google-services:4.3.3'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
        maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/'}
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
