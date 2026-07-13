buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Classpath untuk memuat plugin Realm Java
        classpath(libs.realm.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    id("com.android.library") version "9.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
