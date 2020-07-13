podTemplate() {
    node(POD_LABEL) {
        stage('Terraform') {
            steps {
                build 'terraform-job'
            }           
        }  
        stage('Kubernetes') {
            steps {
                build 'kubernetes-job'
            } 
        } 
    }
}