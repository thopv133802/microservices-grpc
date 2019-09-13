import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
	id("org.springframework.boot") version "2.1.8.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.2.71"
	kotlin("plugin.spring") version "1.2.71"

	id("idea")
	id("com.google.protobuf") version "0.8.10"
}

group = "com.thopham.projects.research"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "Greenwich.SR3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//Grpc
	implementation("io.grpc:grpc-netty-shaded:1.22.1")
	implementation("io.grpc:grpc-protobuf:1.22.1")
	implementation("io.grpc:grpc-stub:1.22.1")
	implementation("com.google.protobuf:protobuf-java:3.9.0")
	//json web token
	implementation("io.jsonwebtoken:jjwt:0.9.0")
	//Api document Swagger 2.0
	implementation("io.springfox:springfox-swagger2:2.9.2")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")
	//...
	implementation( "javax.xml.bind:jaxb-api:2.3.0")
	implementation( "com.sun.xml.bind:jaxb-core:2.3.0")
	implementation( "com.sun.xml.bind:jaxb-impl:2.3.0")
	implementation ("javax.activation:activation:1.1.1")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}



protobuf {
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.22.1"
		}
	}
	protoc {
		artifact = "com.google.protobuf:protoc:3.9.0"
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				id("grpc")
			}
		}
	}
}
sourceSets {
	main {
		proto {
			srcDir("/home/jlaot/IdeaProjects/research/proto-service/protos")
		}
	}
}