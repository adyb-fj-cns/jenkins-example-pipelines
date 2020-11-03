//Based on https://gitlab.gnome.org/World/gimp-ci/jenkins-dsl/-/blob/master/jobs/gimp_multibranch_pipelines.groovy
import jenkins.model.Jenkins

def generatePipelineJob(String jobName, Map settings) {
    multibranchPipelineJob(jobName) {
        displayName(settings['displayName'])
        branchSources {
            branchSource {
                source {
                    git {
                        id 'git-scm'
                        remote settings['remote']
                        traits {
                            gitBranchDiscovery()
                            gitTagDiscovery()
                        }
                    }
                }
            }
        }
        triggers {
            cron(settings['cron'])
        }
    }
}


Map multibranch_jobs = [
    grafanaDashboards: [
        name: 'grafanaDashboards',
        displayName: 'Convert and upload Grafana Dashboards (Multibranch)',
        remote: 'https://github.com/adyb-fj-cns/grafana-dashboards.git', 
        cron: 'H H * * *'
    ]
]

multibranch_jobs.each { String name, Map settings ->
    generatePipelineJob name, settings
}