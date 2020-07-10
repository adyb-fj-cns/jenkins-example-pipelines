podTemplate(yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: docker
    image: docker:18.05-dind
    tty: true
    securityContext:
      privileged: true
    volumeMounts:
      - name: dind-storage
        mountPath: /var/lib/docker
  volumes:
  - name: dind-storage
    emptyDir: {}
""") {
    node(POD_LABEL) {
        git 'https://github.com/jenkinsci/docker-jnlp-slave.git'
        container('docker') {
            sh """
               docker version 
               """
        }
    }
}