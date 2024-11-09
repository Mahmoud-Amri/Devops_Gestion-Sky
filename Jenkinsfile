pipeline {
   agent any

    environment {
        GIT_REPO = 'https://github.com/Mahmoud-Amri/Devops_Gestion-Sky.git'
        BRANCH = 'feature/PisteRest'
        DOCKER_IMAGE = 'amine6207/amineayari'
        SONARQUBE_ENV = 'SonarQubeScanner'
        SONARQUBE_PROJECT_KEY ='Montoring-containerization-springBoot-angular_AY-Xciv7g-qPkHtLhErl'
        SONARQUBE_CREDS = credentials('9481aa8a-1116-40ea-aeaf-436f71baf215')
        NEXUS_URL = 'http://172.18.0.1:8081/repository/maven-releases/'
        NEXUS_CREDENTIALS_ID = 'nexus-credential'
        MAVEN_TOOL = 'mvn' // Maven tool name in Jenkins
        registryCredential = 'docker-cred'

    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Unit Test') {
            tools {
                maven "${MAVEN_TOOL}"
            }
            steps {
                dir('app/backend') {
                    sh 'mvn clean test'
                    
                }
            }
        }


        stage('Build Application') {
            tools {
                maven "${MAVEN_TOOL}"
            }
            steps {
                dir('app/backend') {
                    sh 'mvn -B -f pom.xml clean package -DskipTests'
                }
            }
        }
        
         stage('SonarQube Analysis') {
             environment {
                scannerHome = tool 'SonarQubeScanner' // SonarQube scanner tool name in Jenkins
            }
            
                   tools {
                maven "${MAVEN_TOOL}"
            }
                steps {
           
                dir('app/backend') {
                    withSonarQubeEnv(installationName :"${SONARQUBE_ENV}") {

                             sh """
                                mvn clean verify sonar:sonar \
                                -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY} \
                                -Dsonar.login=${SONARQUBE_CREDS_USR} \
                                -Dsonar.password=${SONARQUBE_CREDS_PSW}
                            """

                    }
                }
             }
          }
        

        stage('Build Docker Image') {
            steps {
                dir('app/backend') {
                    script {
               dockerImage = docker.build("${DOCKER_IMAGE}:${BUILD_NUMBER}")


                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    
                  docker.withRegistry( '', registryCredential ) {
dockerImage.push()
                }
                }
            }
        }

      stage('Upload to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: '172.18.0.1:8081',
                    groupId: 'tn.esprit',
                    version: '1.0.0',
                    repository: 'maven-releases',
                    credentialsId: NEXUS_CREDENTIALS_ID,
                    artifacts: [
                        [artifactId: 'DevOps_Project', classifier: '', file: 'app/backend/target/gestion-station-ski-1.0.jar', type: 'jar']
                    ]
                )
            }
        }
    }

    post {
        always {
              
                cleanWs()  // This is now within the 'node' context
            
        }
    }
}
