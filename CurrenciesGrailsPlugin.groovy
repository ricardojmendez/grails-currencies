
class CurrenciesGrailsPlugin {
	def version = "1.4"
	def dependsOn = [:]

    // TODO Add accent once Grail's UTF-8 bug is fixed
    def author = "Ricardo J. Mendez"
    def authorEmail = "ricardo@arquetipos.co.cr"
    def title = "Currency and exchange rate plugin for grails"
    def description = '''\
Allows easy storage of currencies and exchange rates across them.  Provides
a Money helper class that is used for converting between currencies at the
exchange rate on a certain date (the current date is used by default).

It's recommended you use the money amounts embedded.
'''
	
	
	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}   
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)		
	}
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional)
	}	                                      
	def doWithDynamicMethods = { ctx ->
		// TODO Implement additions to web.xml (optional)
	}	
	def onChange = { event ->
		// TODO Implement code that is executed when this class plugin class is changed  
		// the event contains: event.application and event.applicationContext objects
	}                                                                                  
	def onApplicationChange = { event ->
		// TODO Implement code that is executed when any class in a GrailsApplication changes
		// the event contain: event.source, event.application and event.applicationContext objects
	}
}
