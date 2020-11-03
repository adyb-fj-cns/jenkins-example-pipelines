multibranchPipelineJob('grafanaDashboards') {
    displayName('Convert and upload Grafana Dashboards (Multibranch)')

    branchSources {
        branchSource {
            source {
                git {
                    id 'git-scm'
                    remote {
                        url('https://github.com/adyb-fj-cns/grafana-dashboards.git')
                    }
                }
            }
        }
    }

    triggers {
        cron('H H * * *')
    }
}