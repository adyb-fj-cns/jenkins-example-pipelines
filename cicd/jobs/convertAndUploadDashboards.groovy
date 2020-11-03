pipelineJob('convertAndUploadDashboards') {
    displayName('Convert And Upload Grafana Dashboards (Library)')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/adyb-fj-cns/jenkins-example-pipelines.git')
                    }
                    branches('*/master')
                }
            }
            scriptPath('cicd/pipelines/convertAndUploadDashboards.groovy')
        }
    }
}