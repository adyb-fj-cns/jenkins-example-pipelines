podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  name: pod
spec:
  containers:
  - name: grafonnet
    image: adybfjcns/grafonnet
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "32Mi"
        cpu: "100m"
      limits:
        memory: "64Mi"
        cpu: "200m"  
""") {
  node(POD_LABEL) {
    

    stage('convert'){
      git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-dashboards'
      container('grafonnet') {
        sh '''
            SCRIPT_PATH="dashboards-jsonnet"; \
            for file in $SCRIPT_PATH/*.jsonnet; \
            do \
                input=$(basename -- $file); \
                output=$SCRIPT_PATH/${input%.*}.json; \
                echo "Converting $file into $output"; \
                jsonnet $file > $output; \
            done
          '''
      }
    }

    stage('commit'){
      git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-json-dashboards'
      container('grafonnet') {
        sh '''
            SCRIPT_PATH="dashboards-jsonnet"; \
            for file in $SCRIPT_PATH/*.jsonnet; \
            do \
                echo "Committing $file"; \
            done
          '''
      }
    }
  }
}
