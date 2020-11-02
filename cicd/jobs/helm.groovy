pipelineJob('kubernetes-job') {
    displayName('Kubernetes job')

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
            scriptPath('cicd/pipelines/helm.groovy')
        }
    }
}