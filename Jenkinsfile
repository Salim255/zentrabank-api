pipeline {

    // Run on any available Jenkins agent (node)
    // In production, you might restrict this to a Docker agent or labeled node
    agent any

    // Define tools installed in Jenkins (Global Tool Configuration)
    tools {
        jdk "JDK21"        // Use Java 21 for compilation (required by your Spring Boot app)
        maven "MAVEN3.9"   // Use Maven 3.9 to build and manage dependencies
    }

    environment {
        DOCKER_IMAGE_BACK = "crawan/zentrabank-api"   // Backend image name
    }
    stages {

        // -------------------------
        // BUILD BACKEND
        // -------------------------
        stage ("BUILD") {
            steps {
                // Clean previous builds and compile the project
                // -DskipTests → skip tests here for faster build
                sh 'mvn clean install -DskipTests' // Build Spring Boot app
            }
        }

        // -------------------------
        // 2. Unit Testing
        // -------------------------
        stage ("TEST") {
            steps {
                // Run unit tests separately
                // Keeps build fast and isolates test failures
                //sh 'mvn test' // Run unit tests
                 echo 'Running SonarQube analysis...'
            }
        }

        // -------------------------
        // 3. Integration Testing
        // -------------------------
        stage ("INTEGRATION TEST") {
            steps {
                // Run integration tests (usually heavier tests)
                // -DskipUnitTests → avoid re-running unit tests
                // sh 'mvn verify -DskipUnitTests'
                 echo 'Running SonarQube analysis...'
            }
        }

        // -------------------------
        // 4. Static Code Analysis (Checkstyle)
        // -------------------------
        stage("CODE ANALYSIS WITH CHECKSTYLE") {
            steps {
                // Analyze code style and enforce coding standards
                sh 'mvn checkstyle:checkstyle'
            }
            post {
                success {
                    // Only runs if stage succeeds
                    echo 'Checkstyle analysis completed successfully'
                }
                failure {
                    echo 'Checkstyle found violations'
                }
            }
        }

        // -------------------------
        // 5. SonarQube Analysis (placeholder)
        // -------------------------
        stage("CODE ANALYSIS WITH SONARQUBE") {
            steps {
                // In real setup, this sends code to SonarQube server
                echo 'Running SonarQube analysis...'

                // Example (when configured):
                // sh 'mvn sonar:sonar'
            }
        }

        // -------------------------
        // BUILD DOCKER IMAGE
        // -------------------------
        stage("BUILD DOCKER IMAGE") {
            steps {
                script {
                    // Build backend Docker image
                    sh 'docker build -t $DOCKER_IMAGE_BACK:latest .'
                }
            }
        }

        // -------------------------
        // PUSH IMAGES
        // -------------------------
        stage("PUSH DOCKER IMAGES") {
            steps {
                script {
                    // Push backend image
                    sh 'docker push $DOCKER_IMAGE_BACK:latest'
                }
            }
        }

        // -------------------------
        // LOGIN TO DOCKER HUB
        // -------------------------
        stage("DOCKER LOGIN") {
            steps {
                script {
                    // Use Jenkins credentials securely
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {

                        // Login to Docker Hub
                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    }
                }
            }
        }

        // -------------------------
        // 7. Deploy using Docker Compose
        // -------------------------
        stage("ROLLOUT APP") {
            steps {
                script {
                    // Start new containers in detached mode
                    // sh 'docker-compose up -d'
                    withCredentials([file(credentialsId: 'ZENTRA_API_SECRETS_FILE', variable: 'SECRETS_FILE')]) {
                        sh '''
                            echo "SECRETS_FILE='$SECRETS_FILE'"
                            ls -l "$SECRETS_FILE"

                            # Copy the secret file into the workspace
                            cat "$SECRETS_FILE" > secrets.env

                            echo "After copy:"
                            ls -l secrets.env
                            cat secrets.env
                            # Run docker-compose (it will load secrets.env)
                            docker-compose up -d

                            # Debug inside container
                            echo "---- Inside container ----"

                        '''
                    }
                }
            }
        }
    }

    // -------------------------
    // Global post actions
    // -------------------------
    post {

        success {
            // Runs if pipeline succeeds
            echo 'Pipeline completed successfully 🚀'
        }

        failure {
            // Runs if pipeline fails
            echo 'Pipeline failed ❌'
        }

        always {
            // Always runs (cleanup, logs, etc.)
            echo 'Cleaning up...'
            cleanWs()
        }
    }
}