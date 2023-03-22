@Library(
    ['cop-pipeline-bootstrap', 'cop-pipeline-step']
)
loadPipelines()

pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile.snowsql'
            args '<docker.sock path>'
        }
    }
}

environment {
    SQ_ACCOUNT = 'abcd'
    SQ_AUTH = 'efg'
    SQ_ENV = 'dev' //this also can be automated
    SQ_ROLE = ''
    SQ_WAREHOUSE = ''
    CERBERUS_ENV = ''
    CERBERUS_NAME = 'snowflake-deployment-test'
}

options {
    timeout (time: 30, unit: 'MINUTES')
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '30', artifactNnumToKeepStr))
    ansiColor('xterm')
    disableConcurrentBuilds()
}