pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    def scmVars = checkout([
                        $class: 'GitSCM',
                        branches: [[name: '**']],
                        extensions: [
                            [$class: 'LocalBranch', localBranch: '**']
                        ],
                        userRemoteConfigs: [[
                            url: 'https://github.com/molodoymaxim/BookLibrary.git'
                        ]]
                    ])
                    env.BRANCH_NAME = scmVars.GIT_BRANCH.replace('origin/', '')
                    echo "Detected branch: ${env.BRANCH_NAME}"
                }
            }
        }

        stage('Build Code') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            when {
                expression { env.BRANCH_NAME.startsWith("feature/") }
            }
            steps {
                bat 'mvn test'
                junit testResults: '**/surefire-reports/*.xml'
            }
        }

        stage('Static Analysis') {
            when {
                expression { env.BRANCH_NAME == "develop" }
            }
            steps {
                bat 'mvn checkstyle:check'
            }
        }

        stage('Code Coverage Report') {
            steps {
                bat 'mvn jacoco:report'
                jacoco execPattern: '**/target/jacoco.exec'
                jacoco classPattern: '**/target/classes'
                jacoco sourcePattern: '**/src/main/java'
            }
        }

        stage('Install Artifacts') {
            steps {
                bat 'mvn install -DskipTests'
            }
        }

        stage('Check Coverage Requirements') {
            steps {
                bat 'mvn jacoco:check'
            }
        }

        stage('Deploy Artifact') {
            steps {
                script {
                    def targetDir = "C:\\artifacts\\${env.BRANCH_NAME}"
                    bat """
                        if not exist "${targetDir}" (
                            mkdir "${targetDir}"
                        )
                        copy /Y "app\\target\\*.jar" "${targetDir}\\"
                    """
                }
                archiveArtifacts artifacts: 'app\\target\\*.jar', fingerprint: true
            }
        }
    }
}
