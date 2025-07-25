# 🎵 Spotify API Test Automation

![Java](https://img.shields.io/badge/Java-11-orange?style=flat-square&logo=java)
![REST Assured](https://img.shields.io/badge/REST%20Assured-5.5.5-green?style=flat-square)
![TestNG](https://img.shields.io/badge/TestNG-7.11.0-blue?style=flat-square)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-blue?style=flat-square&logo=jenkins)
![Allure](https://img.shields.io/badge/Allure-Reports-yellow?style=flat-square)
![GitHub Pages](https://img.shields.io/badge/GitHub%20Pages-Hosting-purple?style=flat-square&logo=github)

A comprehensive REST Assured test automation framework for Spotify Web API, featuring automated playlist management, token handling, and CI/CD integration with Jenkins and GitHub Pages reporting.

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔐 Authentication | Complete OAuth2 token management |
| 🎧 API Coverage | Full CRUD operations for playlists |
| 📊 Reporting | Allure reports with GitHub Pages hosting |
| 🚀 CI/CD | Jenkins pipeline with scheduled execution |
| 🏗️ Design Patterns | Singleton pattern implementation |
| 🎲 Dynamic Data | Java Faker for test data generation |
| 🧹 Clean Code | Lombok annotations for reduced boilerplate |

## 🚀 Getting Started

### Prerequisites
- Java 11+
- Maven 3.6+
- Git

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/spotify-api-automation.git
   cd spotify-api-automation
   ```

2. **Review Spotify API Documentation**
   
   📚 All Spotify APIs are thoroughly documented including authentication, token generation, and management. [**Click here**](https://developer.spotify.com/documentation/web-api) for complete API reference.

3. **Configure credentials**
   ```bash
   # Update src/main/resources/config.properties with your Spotify credentials
   client_id=your_client_id
   client_secret=your_client_secret
   refersh token etc...
   ```

4. **Run tests**
   ```bash
   mvn clean test
   ```

5. **Generate reports**
   ```bash
   allure serve allure-results
   ```

## 🏗️ Project Architecture

```
📦 Project Root/
 ├── 📂 .idea/
 ├── 📂 allure-results/
 ├── 📂 src/
 │   ├── 📂 main/java/com/spotify/oauth2/
 │   │   ├── 🔌 api/
 │   │   │   ├── Routes.java
 │   │   │   ├── SpecBuilder.java
 │   │   │   ├── StatusCode.java
 │   │   │   └── TokenManager.java
 │   │   ├── 📋 pojo/
 │   │   └── 🛠️ utils/
 │   │       ├── ConfigLoader.java
 │   │       ├── JavaFakerUtils.java
 │   │       ├── PlaylistAPI.java
 │   │       └── PropertyUtil.java
 │   └── 📂 test/java/com/spotify/oauth2/
 │       └── 🧪 tests/
 │           ├── BaseTest.java
 │           └── PlaylistTests.java
 ├── 📂 resources/
 │   └── config.properties
 ├── 📄 .gitignore
 ├── 📄 Jenkinsfile
 ├── 📄 README.md
 ├── 📄 pom.xml
 └── 📄 testng.xml

```

## 🔧 Tech Stack

| Technology | Purpose | Version |
|------------|---------|---------|
| ☕ **Java** | Programming Language | 11 |
| 🌐 **REST Assured** | API Testing Framework | 5.5.5 |
| 🧪 **TestNG** | Testing Framework | 7.11.0 |
| 📊 **Allure** | Test Reporting | 2.29.1 |
| 🎭 **Java Faker** | Test Data Generation | 1.0.2 |
| 🏷️ **Lombok** | Code Generation | 1.18.38 |
| 🔧 **Jackson** | JSON Processing | 2.17.1 |
| ⚙️ **Maven** | Build Tool | 3.11.0 |

## 💡 Key Implementation Details

### 🏭 POJO Generation
POJO classes generated using [**jsonschema2pojo.org**](https://www.jsonschema2pojo.org/) - simply paste JSON schema to convert to Java classes.

### 🧹 Code Quality
- **Lombok Integration**: Eliminates getters, setters, and builder methods with annotations
- **No Hard-coded Values**: Except where required, using Java Faker for dynamic data
- **Clean Architecture**: Singleton design pattern for configuration management

### 🚀 CI/CD Pipeline
Jenkins automation pipeline featuring:
- ⏰ Daily scheduled execution (10 AM)
- 🧪 Automated test execution and reporting
- 📄 GitHub Pages deployment for reports
- 📧 Email notifications with report links

**Report Repository**: [**RA-Spotify-Reports**](https://github.com/SDET-Pearhamesh/RA-Spotify-Reports)

## 📊 Reporting

| Report Type | Location | Description |
|-------------|----------|-------------|
| 🏠 **Local** | `allure-report/` directory | Generated locally after test execution |
| 🌐 **GitHub Pages** | Automated deployment | Via Jenkins pipeline |
| 📚 **Archive** | Historical reports | Maintained for all builds |

## 🚀 Execution Commands

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=PlaylistTests

# Generate Allure report
allure serve allure-results

# Clean and generate fresh report
allure generate allure-results --clean -o allure-report
```
