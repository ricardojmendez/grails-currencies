grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
//        compile 'org.joda:joda-money:0.9'
//        compile('javax.money:jsr354-api:0.7')
    }

    plugins {
        compile ':hibernate:3.6.10.2'
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
