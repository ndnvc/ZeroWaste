buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.realm.gradle.plugin)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
<<<<<<< Updated upstream:build.gradle.kts
}
=======
    alias(libs.plugins.kotlin.compose) apply false
}
>>>>>>> Stashed changes:ZerowasteAPK/build.gradle.kts
