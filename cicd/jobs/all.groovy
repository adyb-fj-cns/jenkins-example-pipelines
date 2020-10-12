pipelineJob('all-job') {
    displayName('All job')

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
            scriptPath('cicd/pipelines/all.groovy')
        }
    }
}