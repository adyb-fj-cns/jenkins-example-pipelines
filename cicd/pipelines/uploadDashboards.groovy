podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  name: pod
spec:
  containers:
  - name: extractor
    image: adybfjcns/grafonnet
    volumeMounts:
    - mountPath: '/shared'
      name: shared
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
  - name: uploader
    image: adybfjcns/grafonnet
    volumeMounts:
    - mountPath: '/shared'
      name: shared
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
  volumes:
  - name: shared
    emptyDir: {}
""") {
  node(POD_LABEL) {
    git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-dashboards'

    stage('Extract JSON') {
      container('extractor') {
        sh '''
           set +x;\
           echo "Extracting dashboards"
           '''
      }
    }

    stage('Upload to Grafana'){
      container('uploader') {
        withCredentials([usernamePassword(
          credentialsId: 'grafana', 
          usernameVariable: 'GRAFANA_USERNAME', 
          passwordVariable: 'GRAFANA_PASSWORD')]) {

          sh '''
            set +x; \
            SCRIPT_PATH="dashboards-jsonnet"; \
            for file in $SCRIPT_PATH/*.jsonnet; \
            do \
                input=$(basename -- $file); \
                output=$SCRIPT_PATH/${input%.*}.json; \
                echo "Converting $file into $output"; \
                jsonnet $file > $output; \
                rm $file; \
            done
          '''
          
          sh '''
            set +x;\
            SCRIPT_PATH="dashboards-jsonnet"; \
            for file in $SCRIPT_PATH/*.json; \
            do \
              DASHBOARD=$(jq . $file); \
              curl -X POST \
                -H 'Content-Type: application/json' \
                -d "{\\\"dashboard\\\": $DASHBOARD, \\\"overwrite\\\": true}" \
                "http://$GRAFANA_USERNAME:$GRAFANA_PASSWORD@grafana:3000/api/dashboards/db";
            done
            '''
            
        }
      }
    }
  
  }
}
