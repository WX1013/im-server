pipeline {
    agent any

    environment{
        repoHost = 'registry-dev.hzlinks.net'
        repoNs = '00-0000-dlhsdx/zbp'
        repoUser = 'zbpadmin'
        repoPasswd = 'Zbp@310012'
        version = 'v1.0.0'
    }

    // 存放所有任务的合集
    stages {
        // 拉取代码
        stage('拉取代码') {
            steps {
                checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: 'wx-svn', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'svn://svn.hzlinks.net/SVN_ZBP/trunk/03.项目区/007.大连海事大学/02_Code/021_一表通系统/im-server']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
            }
        }
        // 构建代码
        stage('构建代码') {
            steps {
                sh '/var/jenkins_home/maven_3.6.1/bin/mvn clean package -DskipTests'
            }
        }
        // 制作自定义镜像并发布
        stage('制作自定义镜像并发布') {
            steps {
                sh '''cp target/im-server-1.0.0.jar test-deploy/
                cd test-deploy
                docker build -t ${JOB_NAME}:${version} ./'''
                
                sh '''docker login -u ${repoUser} -p ${repoPasswd} ${repoHost}
                docker tag ${JOB_NAME}:${version} ${repoHost}/${repoNs}/${JOB_NAME}:${version}
                docker image prune -f
                docker push ${repoHost}/${repoNs}/${JOB_NAME}:${version}'''
            }
        }
        // 上传pipeline.yml文件
        stage('上传pipeline.yml文件') {
            steps {
                sshPublisher(publishers: [sshPublisherDesc(configName: 'k8s-master-01', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/home/deploy/im-server', remoteDirectorySDF: false, removePrefix: 'test-deploy', sourceFiles: 'test-deploy/im-server.yml')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
        // 远程执行kubectl命令
        stage('远程执行kubectl命令') {
            steps {
                sh 'ssh root@192.168.18.11 kubectl apply -f /home/deploy/im-server/im-server.yml'
            }
        }
    }
}