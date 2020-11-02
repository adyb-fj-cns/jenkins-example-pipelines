podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  name: terraform-pod
spec:
  containers:
  - name: terraform
    image: hashicorp/terraform:light
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
""") {
    node(POD_LABEL) {
      git 'https://github.com/adyb-fj-cns/jenkins-terraform-example.git'

      stage('init'){
        container('terraform') {
          sh """
            terraform init \
              -backend-config="address=consul-ui" \
              -input=false
            """
        }
      }

      stage('plan'){
        container('terraform') {
          sh """
            terraform plan \
              -var-file=jenkins.tfvars \
              -out=tfplan \
              -input=false
            """
        }
      }

      stage('apply'){
        container('terraform') {
          sh """
            terraform apply \
              -input=false \
              tfplan

            cat message.txt

            """
        }
      }  
    }
}