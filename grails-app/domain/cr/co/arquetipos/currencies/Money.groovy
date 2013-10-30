package cr.co.arquetipos.currencies

class Money implements Serializable {
    private static decimalFormat = new java.text.DecimalFormat("###,##0.00")
    BigDecimal amount = 0.0G
    Currency currency = Currency.getInstance('EUR')
    static constraints = {
        amount(scale: 10) // Oracle NUMBER carries precision up to 38 digits in the range of 1.0E-130 to 1.0E126
    }

    /**
     * Receives a string which contains the amount and currency code,
     * separated by a space ('250.0 EUR'), and creates a new money instance
     * with those values.
     */
    static Money getInstance(String value) {
        def list = value.split(' ')
        assert list.size() == 2
        BigDecimal newAmount = new BigDecimal(list[0])
        def newCurrency = Currency.getInstance(list[1])
        Money money = new Money(amount: newAmount, currency: newCurrency)
        return money
    }

    int hashCode() {
        return amount.hashCode() + currency.hashCode()
    }

    String toString() {
        String formatted = decimalFormat.format((BigDecimal) amount)
        return "${formatted} ${currency?.currencyCode}"
    }

    boolean equals(Object other) {
        if (!other) {
            return false
        };
        if (!(other instanceof Money)) {
            return false
        };
        if (currency != other.currency) {
            return false
        };
        if (amount != other.amount) {
            return false
        };
        return true;
    }

    Money clone() {
        return new Money(amount: this.amount, currency: currency)
    }

    Money plus(Money other) {
        assert other
        if (other.currency != this.currency) {
            other = other.convertTo(this.currency)
        }
        return new Money(amount: this.amount + other.amount, currency: currency)
    }

    Money minus(Money other) {
        return plus(other * -1)
    }

    Money plus(Number n) {
        if (!n) {
            n = 0
        }
        return new Money(amount: this.amount + n, currency: currency)
    }

    Money minus(Number n) {
        if (!n) {
            n = 0
        }
        return new Money(amount: this.amount - n, currency: currency)
    }

    Money multiply(Number n) {
        if (!n) {
            n = 0
        }
        return new Money(amount: this.amount * n, currency: currency)
    }

    Money div(Number n) {
        return new Money(amount: this.amount / n, currency: currency)
    }

    Money convertTo(Currency newCurrency, Date toDate = null) {
        if (newCurrency == currency) {
            return this
        };
        if (!toDate) {
            toDate = new Date()
        }
        def c = ExchangeRate.createCriteria()
        // TODO Implement exchange rate caching as a service
        def rate = c.get {
            or {
                and {
                    eq('baseCurrency', this.currency)
                    eq('toCurrency', newCurrency)
                }
                and {
                    eq('baseCurrency', newCurrency)
                    eq('toCurrency', this.currency)
                }
            }
            le('date', toDate)
            order('date', 'desc')
            maxResults(1)
        }
        def multiplier = 0
        if (!rate) {
            throw new IllegalArgumentException("No exchange rate found")
        } else {
            if (rate.baseCurrency == this.currency) {
                multiplier = rate.rate
            } else {
                multiplier = 1 / rate.rate
            }
        }
        return new Money(amount: amount * multiplier, currency: newCurrency)
    }
}
