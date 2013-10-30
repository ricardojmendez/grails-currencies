package cr.co.arquetipos.currencies
class ExchangeRate implements Serializable {
    Currency baseCurrency
    Currency toCurrency
    BigDecimal rate
    Date date = new Date()

    static constraints = {
        date(unique: ['baseCurrency', 'toCurrency'])
        rate(scale: 10)
    }

    String toString() {
        "$date $baseCurrency to $toCurrency @ $rate"
    }
}