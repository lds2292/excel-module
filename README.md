./gradlew clean build

Java
```
repositories {
    mavenCentral()
    maven {
        url "http://nexus.smartfoodnet.com:8081/repository/sfn-maven/"
    }
}

dependencies {
    implementation group:'sfn', name: 'sfn-excel-module', version: '1.0.53'
}
```

Kotlin
```
repositories {
    mavenCentral()
    maven(url = "http://nexus.smartfoodnet.com:8081/repository/sfn-maven/") {
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation("sfn", "sfn-excel-module", "1.0.53")
}
```