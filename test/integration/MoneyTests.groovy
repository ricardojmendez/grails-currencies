import cr.co.arquetipos.currencies.*

class MoneyTests extends GroovyTestCase {

    void setUp() {
        def dollar = Currency.getInstance('USD')
        def yen = Currency.getInstance('JPY')
        def euro = Currency.getInstance('EUR')
        def gbp = Currency.getInstance('GBP')

        new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.46122, dateCreated:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:yen, toCurrency:dollar, rate:0.008981, dateCreated:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:dollar, toCurrency:yen, rate:111.336, dateCreated:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:gbp, toCurrency:dollar, rate:2.02369, dateCreated:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.33159, dateCreated:new Date('2006/12/02')).save()
    }

    void testInits() {
        Money dollars = new Money(amount:9.50, currency:'USD')
        assert dollars
        assertEquals dollars.currency.currencyCode, 'USD'
        assertEquals dollars.currency.symbol, '\$'

        dollars.save() // Usually will just be embedded

        def list = Money.list()
        assertEquals list.size(), 1

        def newRecord = Money.get(1)
        assertEquals newRecord.amount, 9.5f

        def blank = new Money()
        assertEquals blank.amount, 0f
        assertEquals blank.currency.currencyCode, 'EUR'
    }

    void testRates() {
        def dollar = Currency.getInstance('USD')
        def euro = Currency.getInstance('EUR')

        assert dollar
        assert euro

        Money euros = new Money(amount:10, currency:euro)

        Money toDollars = euros.convertTo(dollar)
        assertEquals toDollars.amount, 14.6122
        assertEquals toDollars, new Money(amount:14.6122, currency:dollar)

        toDollars = euros.convertTo(dollar, new Date('2007/01/01'))
        assertEquals toDollars.amount, 13.31590

        def gbp = Currency.getInstance('GBP')
        assert gbp

        Money dollars = new Money(amount:1, currency:dollar)
        Money toGBP = dollars.convertTo(gbp)
        assertEquals toGBP.amount, 0.4941468308
    }

    void testSum() {
        def dollars = new Money(amount:1, currency:'USD')
        def pounds = new Money(amount:1, currency:'GBP')
        def sum = dollars + pounds
        assert sum
        assertEquals sum.amount, 3.02369
        assertEquals sum.currency.currencyCode, 'USD'
        
        sum = pounds + dollars
        assert sum
        assertEquals sum.amount, 1.4941468308
        assertEquals sum.currency.currencyCode, 'GBP'

        def euros = new Money(currency:'EUR')
        euros += new Money(amount:10, currency:'USD') + new Money(amount:1, currency:'GBP')
        assert euros
        //println euros
        assertEquals euros.currency.currencyCode, 'EUR'
        assertEquals euros.amount, 8.228528216382943

		pounds = new Money(amount:1, currency:'GBP')
		pounds += 2
		assertEquals pounds.amount, 3
		assertEquals pounds.currency.currencyCode, 'GBP'
	}

	void testMinus() {
		def dollars = new Money(amount:1, currency:'USD')
		def pounds = new Money(amount:1, currency:'GBP')
		def sum = dollars + pounds
		assert sum
		assertEquals sum.amount, 3.02369
		assertEquals sum.currency.currencyCode, 'USD'

		sum = pounds + dollars
		assert sum
		assertEquals sum.amount, 1.4941468308
		assertEquals sum.currency.currencyCode, 'GBP'

		def euros = new Money(currency:'EUR')
		euros += new Money(amount:10, currency:'USD') + new Money(amount:1, currency:'GBP')
		assert euros
		//println euros
		assertEquals euros.currency.currencyCode, 'EUR'
		assertEquals euros.amount, 8.228528216382943

		pounds = new Money(amount:1, currency:'GBP')
		pounds += 2
		assertEquals pounds.amount, 3
		assertEquals pounds.currency.currencyCode, 'GBP'

		pounds -= 1.5
		assertEquals pounds.amount, 1.5

		def tenPounds = new Money(amount:10, currency:'GBP')
		tenPounds -= pounds
		assertEquals tenPounds.amount, 8.5
		assertEquals tenPounds.currency.currencyCode, 'GBP'

	}

	void testMultiply() {
        def dollars = new Money(amount:2, currency:'USD')
        def mult =  dollars * 2.5

        assertEquals mult.amount, 5f
        assertEquals mult.currency.currencyCode, 'USD'

        dollars *= 2.5
        assert dollars
        assertEquals dollars.amount, 5f

        mult = new Money(amount:15, currency:'EUR') * 21
        assertEquals mult.amount, 315f
        
    }

	void testDiv() {
        def dollars = new Money(amount:10, currency:'USD')
        def div =  dollars / 2

        assertEquals div.amount, 5f
        assertEquals div.currency.currencyCode, 'USD'

		dollars /= 4
        assert dollars
        assertEquals dollars.amount, 2.5f

        div = new Money(amount:7.5, currency:'EUR') / 3
        assertEquals div.amount, 2.5f
        
    }


    void testFailures() {
        def dollar = Currency.getInstance('USD')
        def euro = Currency.getInstance('EUR')
        Money euros = new Money(amount:10, currency:euro)

        shouldFail(Exception) {
            Money toDollars = euros.convertTo(dollar, newDate('2005/01/01'))
        }

        def yen = Currency.getInstance('JPY')
        Money toYens

        shouldFail(IllegalArgumentException) {
            toYens = euros.convertTo(yen)
        }
        shouldFail(IllegalArgumentException) {
            toYens = euros.convertTo(yen, new Date('2007/12/05'))
        }
    }

    void testGetInstance() {
        def money = Money.getInstance('250.0 EUR')
        assertEquals money.amount, 250f
        assertEquals money.currency.currencyCode, 'EUR'

        money = Money.getInstance('15.50 USD')
        assertEquals money.amount, 15.5f
        assertEquals money.currency.currencyCode, 'USD'

        shouldFail(AssertionError) {
            money = Money.getInstance('250.5')
        }

        shouldFail(AssertionError) {
            money = Money.getInstance('EUR')
        }
    }
}