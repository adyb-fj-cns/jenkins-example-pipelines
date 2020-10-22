podTemplate(yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: k8s
    image: dtzar/helm-kubectl:3.3.4
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
        container('k8s') {
            withCredentials([kubeconfigContent(credentialsId: 'kubeconfig', variable: 'KUBECONFIG_FILE')]) {
                sh """
                    set +x
                    echo "$KUBECONFIG_FILE" >> kubeconfig
                    set -x
                    kubectl get pods --kubeconfig kubeconfig
                    kubectl get nodes --kubeconfig kubeconfig
                    helm list --kubeconfig kubeconfig 
                """
            }
        }
    }
}