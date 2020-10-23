podTemplate(yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: k8s
    image: adybfjcns/helm-kubectl-envsubst
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "32Mi"
        cpu: "100m"
      limits:
        memory: "64Mi"
        cpu: "200m" 
""") {
    node(POD_LABEL) {
        
        git branch: 'main', url: 'https://github.com/adyb-fj-cns/demo-helm-values.git'
         
        container('k8s') {
            withCredentials([kubeconfigContent(credentialsId: 'do-fra1-finx-dev', variable: 'KUBECONFIG_FILE')]) {
                sh """
                    set +x
                    echo "$KUBECONFIG_FILE" >> kubeconfig
                    set -x
                    helm repo add finx https://chartmuseum.157.230.76.69.nip.io --username adybuxton --password password
                    
                    export REPLICA_COUNT=1
                    export TAG=latest
                    envsubst < "values.tpl" > "jenkins-values.yaml"
                    
                    helm upgrade demo finx/demo -f jenkins-values.yaml --kubeconfig kubeconfig
                """
            }
        }
    }
}
