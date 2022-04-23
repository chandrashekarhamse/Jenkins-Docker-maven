pipeline{
    agent any

    options{
        timeout(time: 10, unit: 'MINUTES')
    }
    stages{
        stage('Cleanup'){
            steps{
                echo "Cleaning up older docker images"
                script{
                    try{
                        sh 'docker rm -f $(docker ps -aq)'
                        sh 'docker rmi -f $(docker images -aq)'
                    }
                    catch(e){
                        echo "No image or containers to delete, continuing the execution"
                    }
                }
            }
        }
        stage("Building artifacts"){
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
                    sh 'docker build -t demoapp:$BUILD_NUMBER .'
                }
            }
            post{
                failure{
                    sh "exit 1"
                    error "docker build failed, exiting now!"
                }
            }
        }
        stage('Pushing image to registry'){
            steps{
                echo "Pushing docker image registry"
                sh 'docker tag demoapp:$BUILD_NUMBER dockerhamse/demoapp:$BUILD_NUMBER'
                sh 'docker push dockerhamse/demoapp:$BUILD_NUMBER'
            }
        }
        stage('Deploy docker image'){
            steps{
                echo "Running the docker images"
                script{
                    sh 'docker run -d -p9090:8080 --name demoapp dockerhamse/demoapp:$BUILD_NUMBER'
                    sh 'sleep 5'
                    sh 'docker ps -a' 
                }
            }
        }
    }
    post{
        always{
            echo "Cleaning up the workspace"
            cleanWs()
        }
        success{
            slackSend channel: '#jenkinsci',
                      color: 'good',
                      message: "Job ${currentBuild.fullDisplayName} build successful"
        }
        failure{
            slackSend channel: '#jenkinsci',
                      color: 'danger',
                      message: "Job ${currentBuild.fullDisplayName} build failed"
        }
    }
}