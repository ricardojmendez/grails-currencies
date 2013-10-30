import cr.co.arquetipos.currencies.*

class BootStrap {
    def init = {servletContext ->
        if (!ExchangeRate.list()) {
            def dollar = Currency.getInstance('USD')
            def yen = Currency.getInstance('JPY')
            def euro = Currency.getInstance('EUR')
            def gbp = Currency.getInstance('GBP')

            new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.46122, dateCreated:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:euro, toCurrency:dollar, rate:1.33159, dateCreated:new Date('2006/12/02')).save()
            new ExchangeRate(baseCurrency:yen, toCurrency:dollar, rate:0.008981, dateCreated:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:dollar, toCurrency:yen, rate:111.336, dateCreated:new Date('2007/12/02')).save()
            new ExchangeRate(baseCurrency:gbp, toCurrency:dollar, rate:2.02369, dateCreated:new Date('2007/12/02')).save()
        }
    }

    def destroy = {
    }
}