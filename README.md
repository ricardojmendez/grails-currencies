Grails Currencies Plugin
========================

Multi-currency plugin for Grails.
Implements [Money Pattern](http://martinfowler.com/eaaCatalog/money.html) and contains exchange rates.

Warning! This plugin currently is badly designed and should not be used in production!

Alternatives
============
* [JSR 354 Money and Currency API and JavaMoney Library](https://java.net/projects/javamoney/pages/Home)
* [Joda-Time](http://www.joda.org/joda-money/)
* [JScience](http://jscience.org/)
 
Description
====================
The currencies plugin provides a Money domain class, which is currency-aware, along with an ExchangeRate domain class which can be used to store historical exchange rates.&nbsp; It's useful on applications where you're expected to support multiple currencies, along with a history of exchange rates.

Examples
========
To install, execute:

grails install-plugin currencies

Usage is extremely simple, and exemplified on the included integration tests. To create a list of exchange rates:

    def dollar = Currency.getInstance('USD')
    def yen = Currency.getInstance('JPY')
    def euro = Currency.getInstance('EUR')
    def gbp = Currency.getInstance('GBP')
    new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.46122G, date: new Date('2007/12/02')).save()
    new ExchangeRate(baseCurrency: yen, toCurrency: dollar, rate: 0.008981G, date: new Date('2007/12/02')).save()
    new ExchangeRate(baseCurrency: dollar, toCurrency: yen, rate: 111.336G, date: new Date('2007/12/02')).save()
    new ExchangeRate(baseCurrency: gbp, toCurrency: dollar, rate: 2.02369G, date: new Date('2007/12/02')).save()
    new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.33159G, date: new Date('2006/12/02')).save()

You then create Money items in this way:

    Money dollars = new Money(amount: 9.50G, currency: dollar)
    assert dollars
    assertEquals dollars.currency.currencyCode, 'USD'
    assertEquals dollars.currency.symbol, '$'

You can also obtain an instance of a Money class from a string containing the amount and currency code:

    def money = Money.getInstance('250.0 EUR')
    assertEquals money.amount, 250G
    assertEquals money.currency.currencyCode, 'EUR'
    money = Money.getInstance('15.50 USD')
    assertEquals money.amount, 15.5G
    assertEquals money.currency.currencyCode, 'USD'


To convert from a currency to another, you call the domain class' convertTo function:

    def dollar = Currency.getInstance('USD')
    def euro = Currency.getInstance('EUR')
    assert dollar
    assert euro
    Money euros = new Money(amount: 10.0G, currency: euro)
    Money toDollars = euros.convertTo(dollar)
    assertEquals toDollars.amount, 14.6122G
    assertEquals toDollars, new Money(amount: 14.6122G, currency: dollar)
    toDollars = euros.convertTo(dollar, new Date('2007/01/01'))
    assertEquals toDollars.amount, 13.315901G

Finally, to add money amounts, simply use the overriden Plus operator.
The Money class will look for the most recent exchange rate for both currencies, and convert it to the currency of the first Money object.

    def dollars = new Money(amount: 1.0G, currency: dollar)
    def pounds = new Money(amount: 1.0G, currency: gbp)
    def sum = dollars + pounds
    assert sum
    assertEquals sum.amount, 3.02369G
    assertEquals sum.currency.currencyCode, 'USD'

Attempting to convert between two amounts at a point for which there isn't a valid exchange rate will raise an exception:

    def dollar = Currency.getInstance('USD')
    def euro = Currency.getInstance('EUR')
    Money euros = new Money(amount:10, currency:euro)
    shouldFail(Exception) {
        Money toDollars = euros.convertTo(dollar, newDate('2005/01/01'))
    }

Recommended usage
=================
We use the Money class embedded on objects, for instance:

    class CustomerTransaction {
        Date date = new Date()
        Money amount
        static embedded = ['money']
    }


Next steps
==========
* Caching exchange rates
* Controllers and views for exchange rates

