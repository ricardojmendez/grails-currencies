dataSource {
    pooled = true
    driverClassName = 'org.h2.Driver'
    dialect = org.hibernate.dialect.H2Dialect
    username = 'sa'
    password = ''
    dbCreate = 'update'
    url = 'jdbc:h2:mem:testDb'
}

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
}
