plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	id("com.diffplug.spotless") version "6.20.0"
}

group = "com.slatelog"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.projectlombok:lombok:1.18.28")
	implementation("org.projectlombok:lombok:1.18.28")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Password Strength Assessment
	implementation("com.nulab-inc:zxcvbn:1.8.2")

	// Mail
	implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")

	// Controller Tests
	testImplementation("io.rest-assured:rest-assured:5.3.1")

	// Crypto Bouncy Castle
	implementation("org.bouncycastle:bcpkix-jdk15on:1.70")

	// Map Struct
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")

	// DateFaker
	implementation("net.datafaker:datafaker:2.2.3-SNAPSHOT")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		googleJavaFormat()
	}
}

// Always run spotlessApply before spotlessCheck
tasks.named("check") {
	dependsOn("spotlessJavaApply")
}