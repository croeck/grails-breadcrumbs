grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
    }

    plugins {
        build(':release:2.2.1') {
            export = false
        }

        runtime ":jquery:1.8.3"
    }
}
