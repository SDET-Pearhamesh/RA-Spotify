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
                    
                    # Create docs directory structure for GitHub Pages
                    mkdir -p docs/latest
                    mkdir -p docs/archive/build-${BUILD_NUMBER}
                    
                    # Copy latest report to both locations
                    cp -r ../allure-report/* docs/latest/
                    cp -r ../allure-report/* docs/archive/build-${BUILD_NUMBER}/
                    
                    # Create simple index.html for navigation
                    cat > docs/index.html << EOF
<!DOCTYPE html>
<html>
<head>
    <title>Spotify REST API Test Reports</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; }
        h1 { color: #1db954; text-align: center; }
        .info { background: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0; }
        .link { display: inline-block; margin: 10px 5px; padding: 12px 20px; background: #1db954; color: white; text-decoration: none; border-radius: 4px; }
        .link:hover { background: #1ed760; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎵 Spotify REST API Test Reports</h1>
        <div class="info">
            <strong>Latest Build:</strong> #${BUILD_NUMBER} | <strong>Generated:</strong> ${BUILD_TIMESTAMP}
        </div>
        <div style="text-align: center;">
            <a href="latest/index.html" class="link">📊 Latest Report</a>
            <a href="archive/build-${BUILD_NUMBER}/index.html" class="link">🏗️ Build #${BUILD_NUMBER}</a>
        </div>
        <p style="text-align: center; margin-top: 30px; color: #666;">
            <strong>URL Pattern:</strong> archive/build-{BUILD_NUMBER}/index.html
        </p>
        <div style="margin-top: 30px;">
            <h3>Available Reports:</h3>
            <ul>
EOF
                    
                    # List all available build reports
                    for build_dir in docs/archive/build-*; do
                        if [ -d "$build_dir" ]; then
                            build_num=$(basename "$build_dir" | sed 's/build-//')
                            echo "                <li><a href=\"archive/build-$build_num/index.html\">Build #$build_num</a></li>" >> docs/index.html
                        fi
                    done
                    
                    cat >> docs/index.html << EOF
            </ul>
        </div>
    </div>
</body>
</html>
EOF
                    
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
            script {
                def buildStatus = currentBuild.result ?: 'SUCCESS'
                def reportUrl = "${env.GITHUB_PAGES_URL}/latest/"
                def archiveUrl = "${env.GITHUB_PAGES_URL}/archive/build-${BUILD_NUMBER}/"
                
                echo "📧 Sending email notification..."
                
                // Send email with all details
                emailext (
                    subject: "🎵 Spotify API Test Results - Build #${BUILD_NUMBER} - ${buildStatus}",
                    body: """
                    <html>
                    <body style="font-family: Arial, sans-serif;">
                        <div style="max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px;">
                            <h2 style="color: ${buildStatus == 'SUCCESS' ? '#1db954' : '#e22134'}; text-align: center;">
                                ${buildStatus == 'SUCCESS' ? '✅' : '❌'} Spotify API Tests - ${buildStatus}
                            </h2>
                            
                            <div style="background: #f8f9fa; padding: 20px; border-radius: 5px; margin: 20px 0;">
                                <h3>Build Details</h3>
                                <p><strong>Job:</strong> ${JOB_NAME}</p>
                                <p><strong>Build:</strong> #${BUILD_NUMBER}</p>
                                <p><strong>Status:</strong> ${buildStatus}</p>
                                <p><strong>Duration:</strong> ${currentBuild.durationString}</p>
                                <p><strong>Timestamp:</strong> ${BUILD_TIMESTAMP}</p>
                                <p><strong>Build URL:</strong> <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                            </div>
                            
                            <div style="background: #e8f4f8; padding: 20px; border-radius: 5px; margin: 20px 0; text-align: center;">
                                <h3>📊 Test Reports</h3>
                                <a href="${reportUrl}" style="display: inline-block; margin: 10px; padding: 12px 20px; background: #1db954; color: white; text-decoration: none; border-radius: 4px;">
                                    🔗 Latest Report
                                </a>
                                <a href="${archiveUrl}" style="display: inline-block; margin: 10px; padding: 12px 20px; background: #007cba; color: white; text-decoration: none; border-radius: 4px;">
                                    🏗️ Build #${BUILD_NUMBER} Report
                                </a>
                            </div>
                            
                            <div style="background: #fff3cd; padding: 15px; border-radius: 5px; margin: 20px 0;">
                                <p><strong>Quick Access:</strong></p>
                                <p>Latest: <code>${reportUrl}</code></p>
                                <p>This Build: <code>${archiveUrl}</code></p>
                            </div>
                            
                            <p style="text-align: center; margin-top: 30px; color: #666; font-size: 12px;">
                                <em>Automated CI/CD Pipeline | Check Jenkins console for detailed logs</em>
                            </p>
                        </div>
                    </body>
                    </html>
                    """,
                    mimeType: 'text/html',
                    to: 'prathamesh.d.ingale@gmail.com',
                    attachLog: true
                )
            }
        }
        
        success {
            echo "✅ Pipeline completed successfully!"
        }
        
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}
