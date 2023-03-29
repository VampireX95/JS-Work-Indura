pipeline {
    options {
      timeout(time: 1, unit: 'HOURS') 
  }
  agent { //Using docker agent sqitch - saves us the trouble of installing ODBC drivers and SnoqSQL setup
    docker {
      image 'hashmapinc/sqitch:jenkins'
      args "-u root -v /var/run/docker.sock:/var/run/docker.sock --entrypoint=''"
    }
  }
  
  stages {
    stage('Moving .snowsql to workspace and replacing snowsql in /bin') {
        steps {
            sh '''
            rm /bin/snowsql 
            mv /var/snowsql /bin/
            mv /var/.snowsql ./
            ''' 
        }
    }
    stage('Deploy changes') {
      steps {
        withCredentials(bindings: [usernamePassword(credentialsId: 'snowflake_creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh '''
              sqitch deploy "db:snowflake://$USERNAME:$PASSWORD@hashmap.snowflakecomputing.com/flipr?Driver=Snowflake;warehouse=sqitch_wh"
              '''           
        }
      }
    }
  }
}

//1. Install Docker Desktop
//2. Get Jenkins Image | docker pull jenkins/jenkins:latest | (docker pull jenkins/jenkins:windowsservercore-2019)
//3. 