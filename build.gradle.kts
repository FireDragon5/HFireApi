import java.net.URI

plugins {
    id("java")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    withSourcesJar()
    withJavadocJar()
}

group = "me.firedragon5"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.40"


val downloadHytaleJar = tasks.register("downloadHytaleJar") {
    description = "Downloads the Hytale Server Jar if it does not exist in the libs directory."
    val libsDir = file("libs")
    val jarFile = file("libs/hytale-server.jar")

    doLast {
        if (!jarFile.exists()) {
            libsDir.mkdirs()
            logger.lifecycle("Downloading Hytale Server Jar...")
            val downloadUrl = URI("https://github.com/FireDragon5/H-FireApi/releases/download/v0.0.1/hytale-server.jar").toURL()
            downloadUrl.openStream().use { input ->
                jarFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}

tasks.compileJava {
    dependsOn(downloadHytaleJar)
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly(files("libs/hytale-server.jar"))

    // Lombok
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from("src/main/resources")

    if (!System.getenv().containsKey("JITPACK")) {
        destinationDirectory.set(file("C:\\Users\\antho\\AppData\\Roaming\\Hytale\\UserData\\Mods"))
    }
}