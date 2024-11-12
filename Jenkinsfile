pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                // Cloner le dépôt Git
                git branch: 'gestion_course', url: 'https://github.com/Mahmoud-Amri/Devops_Gestion-Sky.git'
            }
        }

 stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=Devops_Gestion-Sky \
                        -Dsonar.host.url=http://10.0.2.15:9000/ \
                        -Dsonar.login=sqa_92e56bd0910a7d915393ea19b1aae53609f6ed5c
                    """
                }
            }
            post {
                success {
                    echo 'SonarQube analysis completed successfully.'
                }
                failure {
                    echo 'SonarQube analysis failed.'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: 'e0e7a97a-ca79-4963-ba43-d6130af4a3b3', variable: 'mavensettings')]) {
                    echo 'Deploying to Nexus...'    
                    sh 'mvn -s $mavensettings deploy -DskipTests=true'
                }
            }
        }

        stage('BUILD IMAGE') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t fatmaouergh/gestion-course .'
            }
        }

        stage('PUSH IMAGE') {
            steps {
                echo 'Pushing Docker image...'
                sh '''
                    echo "Adminadmin0." | docker login -u fatmaouerghi --password-stdin
                    docker push fatmaouerghi/gestion-course
                '''
            }
        }

        stage('DOCKER COMPOSE') {
            steps {
                echo 'Starting Backend + DB with Docker Compose...'
                sh 'docker-compose up -d'
            }
        }
    }
}
