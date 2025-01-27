plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.su"
version = "0.0.1-SNAPSHOT"
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}



dependencies {

    ext {
        set("druidVersion", "1.2.24")
        set("mybatisPlusVersion", "3.5.9")
        set("mybatisVersion", "3.0.4")
        set("knife4jVersion", "4.5.0")
        set("rocketmqVersion", "2.3.1")
        set("minioVersion", "8.5.14")
        set("fastjsonVersion", "2.0.53")
        set("jjwtVersion", "0.12.6")
        set("testSecurityVersion", "6.4.1")
        set("hutoolVersion", "5.8.34")
        set("fabricGatewayVersion", "2.2.9")
        set("jacksonVersion", "2.18.2")
    }
    //实现依赖
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:${ext.get("mybatisPlusVersion")}")
    implementation("org.mybatis:mybatis-spring:${ext.get("mybatisVersion")}")
    implementation("com.alibaba:druid-spring-boot-3-starter:${ext.get("druidVersion")}")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:${ext.get("knife4jVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.apache.rocketmq:rocketmq-spring-boot-starter:${ext.get("rocketmqVersion")}")
    implementation("io.minio:minio:${ext.get("minioVersion")}")
    implementation("com.alibaba.fastjson2:fastjson2:${ext.get("fastjsonVersion")}")
    implementation("io.jsonwebtoken:jjwt-api:${ext.get("jjwtVersion")}")
    implementation("cn.hutool:hutool-all:${ext.get("hutoolVersion")}")
    implementation("org.hyperledger.fabric:fabric-gateway-java:${ext.get("fabricGatewayVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${ext.get("jacksonVersion")}")

    //编译时依赖
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    //测试依赖
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test:${ext.get("testSecurityVersion")}")

    //运行时依赖
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${ext.get("jjwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${ext.get("jjwtVersion")}")

    //测试运行时依赖
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}
subprojects{
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies{
        ext{
            set("fabricChaincodeVersion", "2.5.5")
        }
        implementation("org.hyperledger.fabric-chaincode-java:fabric-chaincode-shim:${ext.get("fabricChaincodeVersion")}")
    }
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
