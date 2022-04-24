def call(int tag, String creds){
    sh '''
        echo "Pushing docker image registry"
        docker tag demoapp:${tag} dockerhamse/demoapp:${tag}
        docker login -u dockerhamse ${creds}
        docker push dockerhamse/demoapp:${tag}
    '''
}