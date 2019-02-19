import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion = "5.3.2"
val ktorVersion = "1.1.2"

plugins {
    kotlin("jvm") version "1.3.21"
    application
}

repositories {
    jcenter()
    maven {
        url = URI.create("https://kotlin.bintray.com/kotlinx")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.1.1")

    compile("io.ktor:ktor-server-core:$ktorVersion")
    compile("io.ktor:ktor-server-cio:$ktorVersion")
    compile("io.ktor:ktor-client-cio:$ktorVersion")

    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.slf4j:slf4j-api:1.7.21")

    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testCompile("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

application {
    mainClassName = "parallel2019.AppKt"
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}