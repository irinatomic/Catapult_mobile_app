// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    kotlin("plugin.serialization") version "1.9.22"
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
    id("com.google.dagger.hilt.android") version "2.51" apply false
}