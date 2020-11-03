podTemplate(
  containers: [
    containerTemplate(name: 'grafonnet', image: 'adybfjcns/grafonnet', ttyEnabled: true, command: 'cat'),
  ]) {
  node(POD_LABEL) {
    git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-dashboards'

    //def lib = library (
    //    identifier: 'my-shared-library@main'
    //)
    library identifier: 'dynamic-shared-library@main', retriever: modernSCM(
      [$class: 'GitSCMSource',
      remote: 'https://github.com/adyb-fj-cns/jenkins-shared-library']
    )
    
    convertDashboards {
        stageName = 'Convert Jsonnet'
        containerName = 'grafonnet'
        sourceDir = 'dashboards-jsonnet'
    }

    uploadDashboards {
        stageName = 'Upload to Dev Grafana'
        containerName = 'grafonnet'
        credentialsId = 'grafana'
        sourceDir = 'dashboards-jsonnet'
        grafanaUrl = 'grafana:3000'
    }   
  }
}