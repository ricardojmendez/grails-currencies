class CurrenciesGrailsPlugin {
    String version = '2.0-SNAPSHOT'
    String dependsOn = [:]
    String grailsVersion = '2.3.1 > *'
    String author = 'Ricardo J. Méndez'
    String authorEmail = 'ricardo@arquetipos.co.cr'
    String title = 'Currency and exchange rate plugin for Grails'
    String description = '''\
Allows easy storage of currencies and exchange rates across them.  Provides
a Money helper class that is used for converting between currencies at the
exchange rate on a certain date (the current date is used by default).

It's recommended you use the money amounts embedded.
'''
    String documentation = 'https://github.com/stokito/grails-currencies'
    String license = 'APACHE'
    Map issueManagement = [system: 'JIRA', url: 'https://github.com/stokito/grails-currencies/issues']
    Map scm = [url: 'https://github.com/stokito/grails-currencies']
}
