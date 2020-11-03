podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  name: pod
spec:
  containers:
  - name: converter
    image: adybfjcns/grafonnet
    volumeMounts:
    - mountPath: '/shared'
      name: shared
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
  - name: uploader
    image: adybfjcns/grafonnet
    volumeMounts:
    - mountPath: '/shared'
      name: shared
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
  volumes:
  - name: shared
    emptyDir: {}
""") {
  node(POD_LABEL) {
    git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-dashboards'

    def lib = library (
      identifier: 'my-shared-library@main'
    )

    convertDashboards {
      stageName = 'Convert Jsonnet (Library)'
      containerName = 'converter'
      sourceDir = 'dashboards-jsonnet'  
    }

    uploadTarball {
      stageName = 'Package tarball (Library)'
      containerName = 'converter'
      sourceDir = 'dashboards-jsonnet'  
    }

    uploadDashboards {
        stageName = 'Upload to Grafana (Library)'
        containerName = 'uploader'
        credentialsId = 'grafana'
        sourceDir = 'dashboards-jsonnet'
        grafanaUrl = 'grafana:3000'
    }
  }
}
