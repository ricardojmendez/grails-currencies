package cr.co.arquetipos.currencies
class ExchangeRate implements Serializable {
    Currency baseCurrency
    Currency toCurrency
    BigDecimal rate
    Date validFrom = new Date()

    static constraints = {
        validFrom(unique: ['baseCurrency', 'toCurrency'])
    }

    static mapping = {
//        autoTimestamp(false)
    }

    String toString() {
        "$validFrom $baseCurrency to $toCurrency @ $rate"
    }
}