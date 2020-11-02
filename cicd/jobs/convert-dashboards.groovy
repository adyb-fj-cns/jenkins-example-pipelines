/* 
Replace url and scriptPath below with the correct paths
*/

pipelineJob('convert-dashboards') {
    displayName('Convert Grafana Dashboards')

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
            scriptPath('cicd/pipelines/convert-dashboards.groovy')
        }
    }
}