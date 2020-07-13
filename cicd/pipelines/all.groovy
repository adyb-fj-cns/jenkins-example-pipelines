podTemplate() {
    node(POD_LABEL) {
        stage('Terraform') {
            build 'terraform-job'        
        }  
        stage('Kubernetes') {
            build 'kubernetes-job'
        } 
    }
}