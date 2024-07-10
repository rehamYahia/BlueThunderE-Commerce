//buildscript {
//
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath ("com.android.tools.build:gradle:7.3.1")
//        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
//        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.50")
//    }
//}



plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.android.library") version "8.3.2" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21" apply false

}