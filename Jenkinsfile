pipeline {
    agent any

    tools{
        jdk 'JDK-11'
        maven 'Maven-3.8.6'
    }
    
    // Schedule to run everyday at 10 AM
    triggers {
        cron('0 10 * * *')
    }
    
    // Keep only latest 10 builds
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }
    
    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
        GITHUB_CREDENTIALS = credentials('github-credentials-id') // Replace with your credentials ID
        GITHUB_REPO_URL = 'https://github.com/SDET-Pearhamesh/RA-Spotify-Reports.git'
        GITHUB_PAGES_URL = 'https://sdet-pearhamesh.github.io/RA-Spotify-Reports'
        BUILD_TIMESTAMP = new Date().format('yyyy-MM-dd_HH-mm-ss')
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                script {
                    echo "🔄 Checking out source code..."
                }
                checkout scm
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    echo "🧪 Running REST Assured tests..."
                }
                sh 'mvn clean test -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    // Archive test results - Use junit with correct parameter name
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }
        
        stage('Generate Allure Report') {
            steps {
                script {
                    echo "📊 Generating Allure reports..."
                }
                sh '''
                    # Install Allure if not present
                    if ! command -v allure &> /dev/null; then
                        echo "Installing Allure..."
                        # Use curl instead of wget for macOS compatibility
                        curl -L -o allure-2.24.0.tgz https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.tgz
                        tar -xzf allure-2.24.0.tgz
                        # Verify installation
                        ./allure-2.24.0/bin/allure --version
                    fi
                    
                    # Check if allure-results directory exists
                    if [ ! -d "allure-results" ]; then
                        echo "Warning: allure-results directory not found. Creating empty directory..."
                        mkdir -p allure-results
                        echo "No test results available" > allure-results/empty.txt
                    fi
                    
                    # Generate report using the installed allure or system allure
                    if [ -d "allure-2.24.0" ]; then
                        echo "Using downloaded Allure..."
                        ./allure-2.24.0/bin/allure generate allure-results --clean -o allure-report
                    else
                        echo "Using system Allure..."
                        allure generate allure-results --clean -o allure-report
                    fi
                '''
                
                // Publish Allure report in Jenkins
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
                ])
            }
        }
        
        stage('Host Reports on GitHub Pages') {
            steps {
                script {
                    echo "🚀 Hosting Allure reports on GitHub Pages..."
                }
                sh '''
                    # Clean up any existing reports-repo directory
                    rm -rf reports-repo
                    
                    # Clone the reports repository
                    git clone ${GITHUB_REPO_URL} reports-repo
                    cd reports-repo
                    
                    # Configure git
                    git config user.name "Jenkins"
                    git config user.email "jenkins@sdet-pearhamesh.com"
                    
                    # Create docs directory structure for GitHub Pages (without dashboard)
                    mkdir -p docs/latest
                    mkdir -p docs/archive/build-${BUILD_NUMBER}
                    
                    # Copy latest report to both locations
                    cp -r ../allure-report/* docs/latest/
                    cp -r ../allure-report/* docs/archive/build-${BUILD_NUMBER}/
                    
                    # Clean up old archives (keep only last 30)
                    if [ -d "docs/archive" ]; then
                        ls -1 docs/archive/ | grep "build-" | sort -V | head -n -30 | xargs -I {} rm -rf docs/archive/{}
                    fi
                    
                    # Add all files and commit
                    git add .
                    git commit -m "Update reports - Build #${BUILD_NUMBER} [$(date '+%Y-%m-%d %H:%M:%S')]" || echo "No changes to commit"
                    
                    # Push to main branch
                    git push origin main
                '''
            }
        }
    }
    
    post {
        always {
            script { // <--- Added script block here
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def reportUrl = "${env.GITHUB_PAGES_URL}/latest/"
                def archiveUrl = "${env.GITHUB_PAGES_URL}/archive/build-${BUILD_NUMBER}/"
                
                echo "📧 Sending email notification..."
                echo "Build Status: ${buildStatus}"
                echo "Latest Report URL: ${reportUrl}"
                echo "Archive Report URL: ${archiveUrl}"
                
                // Send email with all details using basic mail step first
                try {
                    emailext (
                        subject: "🎵 Spotify API Test Results - Build #${BUILD_NUMBER} - ${currentBuild.result ?: 'SUCCESS'}",
                        body: """Build Status: ${currentBuild.result ?: 'SUCCESS'}
Job: ${JOB_NAME}
Build: #${BUILD_NUMBER}
Duration: ${currentBuild.durationString}
Timestamp: ${BUILD_TIMESTAMP}

📊 Test Reports:
Latest Report: ${GITHUB_PAGES_URL}/latest/
Build #${BUILD_NUMBER} Report: ${GITHUB_PAGES_URL}/archive/build-${BUILD_NUMBER}/

Build URL: ${BUILD_URL}
                        """,
                        to: 'prathamesh.d.ingale@gmail.com',
                        from: 'jenkins@sdet-pearhamesh.com',
                        replyTo: 'prathamesh.d.ingale@gmail.com'
                    )
                    echo "📧 EmailExt sent successfully"
                } catch (Exception e) {
                    echo "❌ EmailExt failed: ${e.getMessage()}"
                    
                    // Fallback to basic mail
                    try {
                        mail (
                            subject: "🎵 Spotify API Test Results - Build #${BUILD_NUMBER} - ${currentBuild.result ?: 'SUCCESS'}",
                            body: """Build Status: ${currentBuild.result ?: 'SUCCESS'}
Job: ${JOB_NAME}  
Build: #${BUILD_NUMBER}
Duration: ${currentBuild.durationString}

📊 Latest Report: ${GITHUB_PAGES_URL}/latest/
🏗️ Build Report: ${GITHUB_PAGES_URL}/archive/build-${BUILD_NUMBER}/

Build URL: ${BUILD_URL}
                            """,
                            to: 'prathamesh.d.ingale@gmail.com'
                        )
                        echo "📧 Basic mail sent successfully"
                    } catch (Exception e2) {
                        echo "❌ Basic mail also failed: ${e2.getMessage()}"
                    }
                }
            } // <--- Closed script block here
        }
        
        success {
            echo "✅ Pipeline completed successfully!"
            echo "📧 SUCCESS: Email should have been sent to prathamesh.d.ingale@gmail.com"
        }
        
        failure {
            echo "❌ Pipeline failed!"
            echo "📧 FAILURE: Email should have been sent to prathamesh.d.ingale@gmail.com"
        }
    }
}
