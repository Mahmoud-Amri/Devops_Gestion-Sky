
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
                git branch: 'gestion_Subscription', url: 'https://github.com/Mahmoud-Amri/Devops_Gestion-Sky.git'
            }
        }

        stage('Compile Stage') {
            steps {
                // Compiler le projet
                sh 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                // Exécuter les tests unitaires
                sh 'mvn test'
            }
            // Optionnel : vous pouvez ajouter un post-action pour gérer les résultats de test
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // Utilisez cette ligne si vous générez des rapports de test JUnit
                }
            }
        }
    }
}