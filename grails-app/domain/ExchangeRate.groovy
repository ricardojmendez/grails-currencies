class ExchangeRate implements Serializable { 
    Currency baseCurrency
    Currency toCurrency
    float rate
    Date date = new Date()
    
    static constraints = {
        date(unique:['baseCurrency', 'toCurrency'])
    }

    String toString() {
        "$date $baseCurrency to $toCurrency @ $rate"
    }
}