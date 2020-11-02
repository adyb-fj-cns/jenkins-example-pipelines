podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  name: pod
spec:
  containers:
  - name: converter
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
    

    stage('Convert Jsonnet'){
      git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-dashboards'

      //dir ('json') {
      //  git branch: 'main', url: 'https://github.com/adyb-fj-cns/grafana-json-dashboards'
      //  credentials('adyb-fj-cns-github-token')
      //}
      
      container('converter') {
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
      }
    }

    stage('Upload JSON tarball'){
      container('uploader') {
        sh '''
            set +x; \
            echo "Creating tarball"
            tar -czvf dashboards.tar.gz dashboards-jsonnet/
           '''

        sh '''
            set +x; \
            echo "Pushing tarball to staging"
            tar -ztvf dashboards.tar.gz
           '''
      }
    }
  }
}
