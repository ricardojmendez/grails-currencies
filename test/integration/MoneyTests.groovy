import cr.co.arquetipos.currencies.ExchangeRate
import cr.co.arquetipos.currencies.Money

class MoneyTests extends GroovyTestCase {
    Currency dollar = Currency.getInstance('USD')
    Currency yen = Currency.getInstance('JPY')
    Currency euro = Currency.getInstance('EUR')
    Currency gbp = Currency.getInstance('GBP')

    void setUp() {
        new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.46122, validFrom: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: yen, toCurrency: dollar, rate: 0.008981, validFrom: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: dollar, toCurrency: yen, rate: 111.336, validFrom: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: gbp, toCurrency: dollar, rate: 2.02369, validFrom: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.33159, validFrom: new Date('2006/12/2')).save()
    }

    void testInits() {
        Money dollars = new Money(amount: 9.50, currency: dollar)
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
        Money euros = new Money(amount: 10, currency: euro)

        Money toDollars = euros.convertTo(dollar)
        assertEquals toDollars.amount, 14.6122
        assertEquals toDollars, new Money(amount: 14.6122, currency: dollar)

        toDollars = euros.convertTo(dollar, new Date('2007/01/01'))
        assertEquals toDollars.amount, 13.31590

        Money dollars = new Money(amount: 1, currency: dollar)
        Money toGBP = dollars.convertTo(gbp)
        assertEquals toGBP.amount, 0.4941468308
    }

    void testSum() {
        def dollars = new Money(amount: 1, currency: dollar)
        def pounds = new Money(amount: 1, currency: gbp)
        def sum = dollars + pounds
        assert sum
        assertEquals sum.amount, 3.02369G
        assertEquals sum.currency.currencyCode, 'USD'

        sum = pounds + dollars
        assert sum
        assertEquals sum.amount, 1.4941468308G
        assertEquals sum.currency.currencyCode, 'GBP'

        def euros = new Money(currency: euro)
        euros += new Money(amount: 10, currency: dollar) + new Money(amount: 1, currency: gbp)
        assert euros
        //println euros
        assertEquals euros.currency.currencyCode, 'EUR'
        assertEquals euros.amount, 8.228528216382943G

        pounds = new Money(amount: 1, currency: gbp)
        pounds += 2
        assertEquals pounds.amount, 3
        assertEquals pounds.currency.currencyCode, 'GBP'
    }

    void testMinus() {
        def dollars = new Money(amount: 1, currency: dollar)
        def pounds = new Money(amount: 1, currency: gbp)
        def sum = dollars + pounds
        assert sum
        assertEquals sum.amount, 3.02369G
        assertEquals sum.currency.currencyCode, 'USD'

        sum = pounds + dollars
        assert sum
        assertEquals sum.amount, 1.4941468308G
        assertEquals sum.currency.currencyCode, 'GBP'

        def euros = new Money(currency: euro)
        euros += new Money(amount: 10, currency: dollar) + new Money(amount: 1, currency: gbp)
        assert euros
        //println euros
        assertEquals euros.currency.currencyCode, 'EUR'
        assertEquals euros.amount, 8.228528216382943G

        pounds = new Money(amount: 1, currency: gbp)
        pounds += 2
        assertEquals pounds.amount, 3
        assertEquals pounds.currency.currencyCode, 'GBP'

        pounds -= 1.5
        assertEquals pounds.amount, 1.5

        def tenPounds = new Money(amount: 10, currency: gbp)
        tenPounds -= pounds
        assertEquals tenPounds.amount, 8.5
        assertEquals tenPounds.currency.currencyCode, 'GBP'
    }

    void testMultiply() {
        def dollars = new Money(amount: 2, currency: dollar)
        def mult = dollars * 2.5

        assertEquals mult.amount, 5f
        assertEquals mult.currency.currencyCode, 'USD'

        dollars *= 2.5
        assert dollars
        assertEquals dollars.amount, 5f

        mult = new Money(amount: 15, currency: euro) * 21
        assertEquals mult.amount, 315f
    }

    void testDiv() {
        def dollars = new Money(amount: 10, currency: dollar)
        def div = dollars / 2

        assertEquals div.amount, 5f
        assertEquals div.currency.currencyCode, 'USD'

        dollars /= 4
        assert dollars
        assertEquals dollars.amount, 2.5f

        div = new Money(amount: 7.5, currency: euro) / 3
        assertEquals div.amount, 2.5f
    }

    void testFailures() {
        Money euros = new Money(amount: 10, currency: euro)

        shouldFail(Exception) {
            Money toDollars = euros.convertTo(dollar, new Date('2005/01/01'))
        }

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