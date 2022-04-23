pipeline{
    agent any

    options{
        timeout(time: 10, unit: 'MINUTES')
    }
    stages{
        stage('workspace Cleanup'){
            steps{
                echo "Cleaning up the workspace"
                cleanWs()
                script{
                    sh 'docker rm -f $(docker ps -aq)'
                    sh 'docker rmi -f $(docker images -aq)'
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
                    sh 'docker build -t demoapp .'
                }
            }
            post{
                failure{
                    sh "exit 1"
                    error "docker build failed, exiting now!"
                }
            }
        }
        stage('Deploy docker image'){
            steps{
                echo "Running the docker images"
                script{
                    sh 'docker run -d -p9090:8080 --name demoapp demoapp'
                    sh 'sleep 5'
                    sh 'docker ps -a' 
                }
            }
        }
    }
    post{
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