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
        EMAIL_TO = 'prathamesh.d.ingale@gmail.com'
        EMAIL_FROM = 'jenkins@sdet-pearhamesh.com'
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
        
        stage('Debug Email Configuration') {
            steps {
                script {
                    echo "🔍 Debugging email configuration..."
                    
                    // Check if Email Extension plugin is available
                    try {
                        def emailExtAvailable = Jenkins.instance.pluginManager.getPlugin('email-ext')
                        echo "📧 Email Extension plugin status: ${emailExtAvailable ? 'INSTALLED' : 'NOT INSTALLED'}"
                    } catch (Exception e) {
                        echo "❌ Error checking Email Extension plugin: ${e.getMessage()}"
                    }
                    
                    // Print environment variables
                    echo "📧 Email Environment Variables:"
                    echo "EMAIL_TO: ${env.EMAIL_TO}"
                    echo "EMAIL_FROM: ${env.EMAIL_FROM}"
                    echo "BUILD_URL: ${env.BUILD_URL}"
                    echo "GITHUB_PAGES_URL: ${env.GITHUB_PAGES_URL}"
                    
                    // Check Jenkins system configuration
                    def jenkinsLocationConfig = Jenkins.instance.getExtensionList('jenkins.model.JenkinsLocationConfiguration')[0]
                    echo "📧 Jenkins URL: ${jenkinsLocationConfig.url}"
                    echo "📧 Admin Email: ${jenkinsLocationConfig.adminAddress}"
                }
            }
        }
    }
    
    post {
        always {
            script {
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def reportUrl = "${env.GITHUB_PAGES_URL}/latest/"
                def archiveUrl = "${env.GITHUB_PAGES_URL}/archive/build-${BUILD_NUMBER}/"
                def buildDuration = currentBuild.durationString ?: 'Unknown'
                
                echo "📧 Preparing email notification..."
                echo "Build Status: ${buildStatus}"
                echo "Latest Report URL: ${reportUrl}"
                echo "Archive Report URL: ${archiveUrl}"
                echo "Build Duration: ${buildDuration}"
                
                def emailSubject = "🎵 Spotify API Test Results - Build #${BUILD_NUMBER} - ${buildStatus}"
                def emailBody = """
<h2>🎵 Spotify API Test Results</h2>

<h3>Build Information:</h3>
<ul>
    <li><strong>Status:</strong> ${buildStatus}</li>
    <li><strong>Job:</strong> ${JOB_NAME}</li>
    <li><strong>Build:</strong> #${BUILD_NUMBER}</li>
    <li><strong>Duration:</strong> ${buildDuration}</li>
    <li><strong>Timestamp:</strong> ${BUILD_TIMESTAMP}</li>
</ul>

<h3>📊 Test Reports:</h3>
<ul>
    <li><a href="${reportUrl}">Latest Report</a></li>
    <li><a href="${archiveUrl}">Build #${BUILD_NUMBER} Archive</a></li>
</ul>

<h3>🔗 Links:</h3>
<ul>
    <li><a href="${BUILD_URL}">Jenkins Build URL</a></li>
    <li><a href="${BUILD_URL}console">Build Console Output</a></li>
</ul>

<p><em>Generated by Jenkins on ${new Date()}</em></p>
                """
                
                def emailBodyPlain = """
🎵 Spotify API Test Results - Build #${BUILD_NUMBER}

Build Information:
- Status: ${buildStatus}
- Job: ${JOB_NAME}
- Build: #${BUILD_NUMBER}
- Duration: ${buildDuration}
- Timestamp: ${BUILD_TIMESTAMP}

📊 Test Reports:
- Latest Report: ${reportUrl}
- Build #${BUILD_NUMBER} Archive: ${archiveUrl}

🔗 Links:
- Jenkins Build: ${BUILD_URL}
- Console Output: ${BUILD_URL}console

Generated by Jenkins on ${new Date()}
                """
                
                // Method 1: Try EmailExt with more comprehensive configuration
                try {
                    echo "📧 Attempting EmailExt with comprehensive configuration..."
                    emailext (
                        subject: emailSubject,
                        body: emailBody,
                        mimeType: 'text/html',
                        to: env.EMAIL_TO,
                        from: env.EMAIL_FROM,
                        replyTo: env.EMAIL_TO,
                        recipientProviders: [
                            developers(),
                            requestor(),
                            culprits()
                        ],
                        attachLog: true,
                        compressLog: true,
                        attachmentsPattern: 'target/surefire-reports/*.xml'
                    )
                    echo "✅ EmailExt sent successfully!"
                    return // Exit if successful
                } catch (Exception e) {
                    echo "❌ EmailExt failed: ${e.getMessage()}"
                    echo "❌ EmailExt stack trace: ${e.getStackTrace()}"
                }
                
                // Method 2: Try EmailExt with minimal configuration
                try {
                    echo "📧 Attempting EmailExt with minimal configuration..."
                    emailext (
                        subject: emailSubject,
                        body: emailBodyPlain,
                        to: env.EMAIL_TO
                    )
                    echo "✅ EmailExt (minimal) sent successfully!"
                    return // Exit if successful
                } catch (Exception e) {
                    echo "❌ EmailExt (minimal) failed: ${e.getMessage()}"
                }
                
                // Method 3: Try basic mail step
                try {
                    echo "📧 Attempting basic mail step..."
                    mail (
                        subject: emailSubject,
                        body: emailBodyPlain,
                        to: env.EMAIL_TO,
                        from: env.EMAIL_FROM
                    )
                    echo "✅ Basic mail sent successfully!"
                    return // Exit if successful
                } catch (Exception e) {
                    echo "❌ Basic mail failed: ${e.getMessage()}"
                    echo "❌ Basic mail stack trace: ${e.getStackTrace()}"
                }
                
                // Method 4: Try basic mail with minimal configuration
                try {
                    echo "📧 Attempting basic mail with minimal configuration..."
                    mail (
                        subject: emailSubject,
                        body: emailBodyPlain,
                        to: env.EMAIL_TO
                    )
                    echo "✅ Basic mail (minimal) sent successfully!"
                } catch (Exception e) {
                    echo "❌ All email methods failed!"
                    echo "❌ Final error: ${e.getMessage()}"
                    echo "❌ Final stack trace: ${e.getStackTrace()}"
                    
                    // Log system information for debugging
                    echo "🔍 System Debug Information:"
                    echo "Java Version: ${System.getProperty('java.version')}"
                    echo "Jenkins Version: ${Jenkins.VERSION}"
                    echo "Available Environment Variables:"
                    env.each { key, value ->
                        if (key.contains('MAIL') || key.contains('EMAIL') || key.contains('SMTP')) {
                            echo "${key}: ${value}"
                        }
                    }
                }
            }
        }
        
        success {
            echo "✅ Pipeline completed successfully!"
            echo "📊 Reports available at: ${env.GITHUB_PAGES_URL}/latest/"
        }
        
        failure {
            echo "❌ Pipeline failed!"
            echo "📊 Check console output and reports for details"
        }
        
        unstable {
            echo "⚠️ Pipeline completed with unstable results!"
            echo "📊 Some tests may have failed - check reports"
        }
        
        cleanup {
            echo "🧹 Cleaning up workspace..."
            // Clean up downloaded files
            sh 'rm -f allure-*.tgz || true'
            sh 'rm -rf allure-2.* || true'
            sh 'rm -rf reports-repo || true'
        }
    }
}
