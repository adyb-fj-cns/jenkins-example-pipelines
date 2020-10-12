/* 
Replace url and scriptPath below with the correct paths
*/

pipelineJob('terraform-job') {
    displayName('Terraform job')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/adyb-fj-cns/jenkins-example-pipelines.git')
                    }
                    branches('*/main')
                }
            }
            scriptPath('cicd/pipelines/grafana.groovy')
        }
    }
}