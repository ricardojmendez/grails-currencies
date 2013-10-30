package cr.co.arquetipos.currencies
class ExchangeRate implements Serializable { 
    Currency baseCurrency
    Currency toCurrency
    BigDecimal rate
    Date dateCreated = new Date()
    
    static constraints = {
        dateCreated(unique:['baseCurrency', 'toCurrency'])
    }

    String toString() {
        "$dateCreated $baseCurrency to $toCurrency @ $rate"
    }
}