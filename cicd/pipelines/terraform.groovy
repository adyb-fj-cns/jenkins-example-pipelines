podTemplate(yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: terraform
    image: hashicorp/terraform:light
    tty: true
""") {
    node(POD_LABEL) {
        git 'https://github.com/jenkinsci/docker-jnlp-slave.git'
        stage('init'){
          container('terraform') {
              sh """
                init 
                """
          }
        }
        stage('plan'){
          container('terraform') {
              sh """
                plan 
                """
          }
        }
    }
}