plugins {
    `kotlin-dsl`
    id("com.jfrog.bintray") version "1.8.4"
}

apply(plugin = "com.jfrog.bintray")

repositories {
    jcenter()
    google()
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:3.5.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
}