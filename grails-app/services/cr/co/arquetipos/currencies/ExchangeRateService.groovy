package cr.co.arquetipos.currencies

import grails.transaction.Transactional

@Transactional
class ExchangeRateService {

    BigDecimal getExchangeRate(Currency baseCurrency, Currency termCurrency, Date toDate = null) {
        if (baseCurrency == termCurrency) {
            return 1.0G
        };
        if (!toDate) {
            toDate = new Date()
        }
        def c = ExchangeRate.createCriteria()
        // TODO Implement exchange rate caching as a service
        def rate = c.get {
            or {
                and {
                    eq('baseCurrency', baseCurrency)
                    eq('toCurrency', termCurrency)
                }
                and {
                    eq('baseCurrency', termCurrency)
                    eq('toCurrency', baseCurrency)
                }
            }
            le('date', toDate)
            order('date', 'desc')
            maxResults(1)
        }
        BigDecimal multiplier
        if (!rate) {
            throw new IllegalArgumentException("No exchange rate found")
        } else {
            if (rate.baseCurrency == baseCurrency) {
                multiplier = rate.rate
            } else {
                multiplier = 1 / rate.rate
            }
        }
        return multiplier
    }
}
