pipeline{
    agent any
    stages{
        stage("Build"){
            steps{
                echo "Building the artifacts"
                script {
                    sh "mvn clean install"
                }
            }
            post{
                failure{
                    sh "exit 1"
                    error "Build failed, exiting now!"
                }
            }
        }
        stage('Building docker image'){
            steps{
                echo "Building docker images using artifacts from build stage"
                script {
                    sh 'docker build -t demapp .'
                }
            }
            post{
                failure{
                    sh "exit 1"
                    error "docker build failed, exiting now!"
                }
            }
        }
    }
    post{
        always{
            echo "========always========"
        }
        success{
            echo "========pipeline executed successfully ========"
        }
        failure{
            echo "========pipeline execution failed========"
        }
    }
}