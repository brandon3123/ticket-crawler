import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("javax.mail:mail:1.4.7")

    implementation("io.github.cdimascio:dotenv-java:2.2.0")

    implementation("com.google.api-client:google-api-client:1.32.1")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.32.1")

    implementation("com.google.apis:google-api-services-gmail:v1-rev110-1.25.0")

    // KTOR
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-netty:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
//    implementation("io.ktor:ktor-jackson:1.6.8")

//    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}