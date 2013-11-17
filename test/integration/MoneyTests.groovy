import cr.co.arquetipos.currencies.ExchangeRate
import cr.co.arquetipos.currencies.Money

class MoneyTests extends GroovyTestCase {
    cr.co.arquetipos.currencies.ExchangeRateService exchangeRateService
    Currency dollar = Currency.getInstance('USD')
    Currency yen = Currency.getInstance('JPY')
    Currency euro = Currency.getInstance('EUR')
    Currency gbp = Currency.getInstance('GBP')

    void setUp() {
        new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.46122G, date: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: yen, toCurrency: dollar, rate: 0.008981G, date: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: dollar, toCurrency: yen, rate: 111.336G, date: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: gbp, toCurrency: dollar, rate: 2.02369G, date: new Date('2007/12/2')).save()
        new ExchangeRate(baseCurrency: euro, toCurrency: dollar, rate: 1.33159G, date: new Date('2006/12/2')).save()
    }

    void testInits() {
        Money dollars = new Money(amount: 9.50G, currency: dollar)
        assert dollars
        assertEquals dollars.currency.currencyCode, 'USD'
        assertEquals dollars.currency.symbol, '\$'

        dollars.save() // Usually will just be embedded

        def list = Money.list()
        assertEquals list.size(), 1

        def newRecord = Money.get(1)
        assertEquals newRecord.amount, 9.5G

        def blank = new Money()
        assertEquals blank.amount, 0.0G
        assertEquals blank.currency.currencyCode, 'EUR'
    }

    void testRates() {
        Money euros = new Money(amount: 10.0G, currency: euro)
        BigDecimal rate = exchangeRateService.getExchangeRate(euro, dollar)
        Money toDollars = euros.convertTo(dollar, rate)
        assertEquals toDollars.amount, 14.6122G
        assertEquals toDollars, new Money(amount: 14.6122G, currency: dollar)

        BigDecimal rate2 = exchangeRateService.getExchangeRate(euro, dollar, new Date('2007/01/01'))

        toDollars = euros.convertTo(dollar, rate2)
        assertEquals toDollars.amount, 13.31590G

        Money dollars = new Money(amount: 1.0G, currency: dollar)

        BigDecimal rate3 = exchangeRateService.getExchangeRate(dollar, gbp)
        Money toGBP = dollars.convertTo(gbp, rate3)
        assertEquals toGBP.amount, 0.4941468308G
    }

    void testSum() {
        def dollars = new Money(amount: 1.0G, currency: dollar)
        def pounds = new Money(amount: 1.0G, currency: gbp)
        def sum = dollars + pounds
        assert sum
        assertEquals sum.amount, 3.02369G
        assertEquals sum.currency.currencyCode, 'USD'

        sum = pounds + dollars
        assert sum
        assertEquals sum.amount, 1.4941468308G
        assertEquals sum.currency.currencyCode, 'GBP'

        def euros = new Money(currency: euro)
        euros += new Money(amount: 10.0G, currency: dollar) + new Money(amount: 1.0G, currency: gbp)
        assert euros
        //println euros
        assertEquals euros.currency.currencyCode, 'EUR'
        assertEquals euros.amount, 8.228528216382943G

        pounds = new Money(amount: 1.0G, currency: gbp)
        pounds += 2
        assertEquals pounds.amount, 3.0G
        assertEquals pounds.currency.currencyCode, 'GBP'
    }

    void testMinus() {
        def dollars = new Money(amount: 1.0G, currency: dollar)
        def pounds = new Money(amount: 1.0G, currency: gbp)
        def sum = dollars + pounds
        assert sum
        assertEquals sum.amount, 3.02369G
        assertEquals sum.currency.currencyCode, 'USD'

        sum = pounds + dollars
        assert sum
        assertEquals sum.amount, 1.4941468308G
        assertEquals sum.currency.currencyCode, 'GBP'

        def euros = new Money(currency: euro)
        euros += new Money(amount: 10.0G, currency: dollar) + new Money(amount: 1.0G, currency: gbp)
        assert euros
        //println euros
        assertEquals euros.currency.currencyCode, 'EUR'
        assertEquals euros.amount, 8.228528216382943G

        pounds = new Money(amount: 1.0G, currency: gbp)
        pounds += 2
        assertEquals pounds.amount, 3.0G
        assertEquals pounds.currency.currencyCode, 'GBP'

        pounds -= 1.5
        assertEquals pounds.amount, 1.5G

        def tenPounds = new Money(amount: 10.0G, currency: gbp)
        tenPounds -= pounds
        assertEquals tenPounds.amount, 8.5G
        assertEquals tenPounds.currency.currencyCode, 'GBP'
    }

    void testMultiply() {
        def dollars = new Money(amount: 2.0G, currency: dollar)
        def mult = dollars * 2.5

        assertEquals mult.amount, 5.0G
        assertEquals mult.currency.currencyCode, 'USD'

        dollars *= 2.5
        assert dollars
        assertEquals dollars.amount, 5.0G

        mult = new Money(amount: 15.0G, currency: euro) * 21
        assertEquals mult.amount, 315.0G
    }

    void testDiv() {
        def dollars = new Money(amount: 10.0G, currency: dollar)
        def div = dollars / 2

        assertEquals div.amount, 5.0G
        assertEquals div.currency.currencyCode, 'USD'

        dollars /= 4
        assert dollars
        assertEquals dollars.amount, 2.5G

        div = new Money(amount: 7.5G, currency: euro) / 3
        assertEquals div.amount, 2.5G
    }

    void testFailures() {
        Money euros = new Money(amount: 10.0G, currency: euro)
        BigDecimal rate = exchangeRateService.getExchangeRate(euro, dollar, new Date('2005/01/01'))
        shouldFail(Exception) {
            Money toDollars = euros.convertTo(dollar, rate)
        }

        Money toYens

        BigDecimal rate2 = exchangeRateService.getExchangeRate(euro, yen)
        shouldFail(IllegalArgumentException) {
            toYens = euros.convertTo(yen, rate2)
        }
        BigDecimal rate3 = exchangeRateService.getExchangeRate(euro, yen, new Date('2007/12/05'))
        shouldFail(IllegalArgumentException) {
            toYens = euros.convertTo(yen, rate3)
        }
    }

    void testGetInstance() {
        def money = Money.getInstance('250.0 EUR')
        assertEquals money.amount, 250.0G
        assertEquals money.currency.currencyCode, 'EUR'

        money = Money.getInstance('15.50 USD')
        assertEquals money.amount, 15.5G
        assertEquals money.currency.currencyCode, 'USD'

        shouldFail(AssertionError) {
            money = Money.getInstance('250.5')
        }

        shouldFail(AssertionError) {
            money = Money.getInstance('EUR')
        }
    }
}