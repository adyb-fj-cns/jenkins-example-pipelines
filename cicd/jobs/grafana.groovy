/* 
Replace url and scriptPath below with the correct paths
*/

pipelineJob('grafana-job') {
    displayName('Grafana job')

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
            scriptPath('cicd/pipelines/grafana.groovy')
        }
    }
}