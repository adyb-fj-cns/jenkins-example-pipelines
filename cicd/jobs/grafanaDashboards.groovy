multibranchPipelineJob('grafanaDashboards') {
    displayName('Convert and upload Grafana Dashboards (Multibranch)')

    branchSources {
        branchSource {
            source {
                git {
                    id 'git-scm'
                    remote 'https://github.com/adyb-fj-cns/grafana-dashboards.git'
                    traits {
                        gitBranchDiscovery()
                        gitTagDiscovery()
                    }
                }
            }
        }
    }
    factory {
        pipelineBranchDefaultsProjectFactory {
            scriptId('Jenkinsfile')
        }
    }
    triggers {
        cron('H H * * *')
    }

}