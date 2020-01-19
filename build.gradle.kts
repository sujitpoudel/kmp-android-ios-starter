import extensions.*
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        maven("https://dl.bintray.com/jetbrains/kotlin-native-dependencies")
        maven("https://dl.bintray.com/jfrog/jfrog-jars")
        maven("https://kotlin.bintray.com/kotlinx")
        google()
        jcenter()
    }

    dependencies {
        classpath("com.amazonaws:aws-java-sdk-core:${Config.version.aws_sdk_core}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Config.version.kotlin}")
        classpath("com.android.tools.build:gradle:${Config.version.gradleTool}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Config.version.kotlin}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:${Config.version.mavenGradle}")
    }
}

plugins {
    id(Config.buildPlugins.androidLib)
    id(Config.buildPlugins.multiplatform)
    id(Config.buildPlugins.serialization) version Config.version.kotlin
    `maven-publish`
}

allprojects {
    repositories {
        mavenLocal()
        maven("https://jitpack.io")
        maven {
            url = uri("http://repo1.maven.org/maven2")
        }
        google()
        jcenter()
        mavenCentral()
        setupForProject()
    }
}

group = Config.libraryProps.group
version = Config.libraryProps.version


apply(plugin = Config.buildPlugins.mavenPublish)
apply(plugin = Config.buildPlugins.androidLib)
apply(plugin = Config.buildPlugins.androidx)
apply(plugin = Config.buildPlugins.serialization)

android {
    setDefaults()
}

kotlin {
    androidWithPublication(project)
    metadataPublication(project)
    val ios64 = iosArm64("ios64")
    val iosSim = iosX64("iosSim")
    val iosMain by sourceSets.creating {
        dependencies {
            implementation(Config.ios.dependencies.stdlib)
            implementation(Config.ios.dependencies.serialization)
            implementation(Config.ios.dependencies.coroutines)
            implementation(Config.ios.dependencies.ktor)
            implementation(Config.ios.dependencies.ktor_auth)
            implementation(kotlin("reflect"))
        }
    }

    val iosTest by sourceSets.creating {
        dependencies {
            implementation(Config.ios.dependencies.stdlib)
            implementation(Config.android.dependencies.coroutines_test)
        }
    }

    configure(listOf(ios64, iosSim)) {
        binaries.framework {
            baseName = Config.ios.frameworkName
            freeCompilerArgs = listOf("-Xobjc-generics")
        }
        sourceSets {
            @Suppress("UNUSED_VARIABLE")
            val ios64Main by getting {
                dependsOn(iosMain)
            }
            @Suppress("UNUSED_VARIABLE")
            val iosSimMain by getting {
                dependsOn(iosMain)
            }
            @Suppress("UNUSED_VARIABLE")
            val iosSimTest by getting {
                dependsOn(iosTest)
            }
            @Suppress("UNUSED_VARIABLE")
            val ios64Test by getting {
                dependsOn(iosTest)
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(Config.common.dependencies.stdlib)
                implementation(Config.common.dependencies.serialization)
                implementation(Config.ios.dependencies.ktor)
                implementation(Config.ios.dependencies.ktor_auth)
                implementation(Config.common.dependencies.coroutines)
                implementation(kotlin("reflect"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin(Config.common.dependencies.test))
                implementation(kotlin(Config.common.dependencies.testAnnotations))
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val androidMain by getting {
            dependencies {
                implementation(Config.android.dependencies.stdlib)
                implementation(Config.android.dependencies.serialization)
                implementation(Config.ios.dependencies.ktor)
                implementation(Config.ios.dependencies.ktor_auth)
                implementation(Config.android.dependencies.coroutines)
                implementation(Config.android.dependencies.coroutines_core)
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val androidTest by getting {
            dependencies {
                implementation(Config.android.dependencies.test)
                implementation(Config.android.dependencies.testJunit)
                implementation(Config.android.dependencies.testKotlinJunit)
                implementation(Config.android.dependencies.robolectricTest)
                implementation(Config.android.dependencies.coroutines_test)
                implementation(Config.android.dependencies.coroutines_core)
            }
        }
    }

    tasks.create(Config.ios.gradleTaskNameDebug, FatFrameworkTask::class) {
        baseName = Config.ios.frameworkName

        destinationDir = buildDir.resolve("fat-framework/debug")
        group = "ios"
        from(
            ios64.binaries.getFramework("DEBUG"),
            iosSim.binaries.getFramework("DEBUG")
        )
        doLast {
            makeGradleW(destinationDir)
        }
    }
}


tasks.withType<KotlinCompile>().all {
    kotlinOptions.freeCompilerArgs += listOf("-Xuse-experimental=kotlin.Experimental")
}

fun makeGradleW(destinationDir: File) {
    val file = File(destinationDir, "gradlew")
    file.writeText(text = "#!/bin/bash\nexport JAVA_HOME=${System.getProperty("java.home")}\ncd ${rootProject.rootDir}\n./gradlew \$@\n")
    file.setExecutable(true)
}

afterEvaluate {
    publishing {
        setupAllPublications(project)
    }
}
