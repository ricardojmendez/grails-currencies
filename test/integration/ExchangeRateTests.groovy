import cr.co.arquetipos.currencies.*

class ExchangeRateTests extends GroovyTestCase {

    void setUp() {
        def dollar = Currency.getInstance('USD')
        def yen = Currency.getInstance('JPY')
        def euro = Currency.getInstance('EUR')
        def gbp = Currency.getInstance('GBP')

        new ExchangeRate(baseCurrency:euro, toCurrency:'USD', rate:1.46122, date:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:yen, toCurrency:dollar, rate:0.008981, date:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:'USD', toCurrency:yen, rate:111.336, date:new Date('2007/12/02')).save()
        new ExchangeRate(baseCurrency:gbp, toCurrency:dollar, rate:2.02369, date:new Date('2007/12/02')).save()
    }
    
    
    void testCurrencies() {
        def dollar = Currency.getInstance('USD')
        def yen = Currency.getInstance('JPY')
        def euro = Currency.getInstance('EUR')
        def gbp = Currency.getInstance('GBP')
                                            
        def rate = ExchangeRate.findByBaseCurrencyAndToCurrency(euro, dollar)
        assert rate
        assertEquals rate.rate, 1.46122
        assertEquals rate.date, new Date('2007/12/02')
    }
}