log4j = {
    // Enable Hibernate SQL logging with param values
    trace 'org.hibernate.type'
    debug 'org.hibernate.SQL'
    root {
        all 'stdout'
    }
}