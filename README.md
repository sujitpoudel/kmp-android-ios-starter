# A Starter Project for Kotlin Multiplatform library
If you are looking to make a kotlin-multiplatform library for mobile (iOS and Android), this is a starter project for it. What does this setup?

1. Uses kotlin DSL gradle in `build.gradle.kts` to setup android and iOS project for Kotlin Multiplatform project.

2. Folder structure is pre-defined where:
    a. `src/androidMain/kotlin` is android platform specific source directory
    b. `src/androidTest/kotlin` is android platform specific test directory
    c. `src/commonMain/kotlin` is common sources
    d. `src/commonTest/kotlin` is common test sources
    e. `src/iosMain/kotlin` is iOS platform specific sources
    f. `src/iosTest/kotlin` is iOS platform specific test sources

3. (Optional) Sets up essential libraries:
    a. `Coroutines`
    b. `Kotlinx-serialization`
    c. `ktor` for networking infrastructure

## How to use it?
1. Clone the repo
2. Run `./scripts/setup.sh`

*Only for a machine that can build iOS apps.*

#### *This is released under the MIT License:*

> The MIT License (MIT)
> Copyright © 2020 Sujit Poudel
>
> Permission is hereby granted, free of charge, to any person obtaining a copy > of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
