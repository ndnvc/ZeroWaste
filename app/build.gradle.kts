plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.uasproject_zerowaste"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.uasproject_zerowaste"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // ... bagian buildTypes dan compileOptions biarkan tetap sama
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.activity.ktx)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // TAMBAHKAN DUA BARIS INI (Memanggil library Realm secara langsung)
    implementation("io.realm:realm-android-library:10.19.0")
    annotationProcessor("io.realm:realm-annotations:10.19.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}