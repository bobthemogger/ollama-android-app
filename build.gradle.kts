plugins {
    alias("com.android.application") version "8.1.0" apply false
    alias("com.android.library") version "8.1.0" apply false
    alias("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

task clean(type) {
    delete buildDir
}