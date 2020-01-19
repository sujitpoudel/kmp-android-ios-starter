# A Starter Project for Kotlin Multiplatform library
If you are looking to make a kotlin-multiplatform library for mobile (iOS and Android), this is a starter project for it. What does this setup?
1. Uses kotlin DSL gradle in build.gradle.kts to setup android and iOS project for Kotlin Multiplatform project.
2. Folder structure is pre-defined where:
    a. src/androidMain/kotlin is android platform specific source directory
    b. src/androidTest/kotlin is android platform specific test directory
    c. src/commonMain/kotlin is common sources
    d. src/commonTest/kotlin is common test sources
    e. src/iosMain/kotlin is iOS platform specific sources
    f. src/iosTest/kotlin is iOS platform specific test sources
3. (Optional) Sets up essential libraries:
    a. Coroutines
    b. Kotlinx-serialization
    c. ktor for networking infrastructure
