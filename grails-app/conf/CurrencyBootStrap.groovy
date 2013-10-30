import cr.co.arquetipos.currencies.*

class CurrencyBootStrap {
    def init = {servletContext ->
        if (!ExchangeRate.list()) {
            def dollar = Currency.getInstance('USD')
            def yen = Currency.getInstance('JPY')
            def euro = Currency.getInstance('EUR')
            def gbp = Currency.getInstance('GBP')

            new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.46122, date:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.33159, date:new Date('2006/12/02')).save()
            new ExchangeRate(baseCurrency:yen, toCurrency:dollar, rate:0.008981, date:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:dollar, toCurrency:yen, rate:111.336, date:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:gbp, toCurrency:dollar, rate:2.02369, date:new Date('2007/12/02')).save()
        }
    }

    def destroy = {
    }
}