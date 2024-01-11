package com.slatelog.slatelog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /*
    * Converter solution from Instant type to Long type
    * This is needed because MongoDB does not like dots (".") at all in its key.
    * However, currently not needed since we truncate the instant in the HashMap key to minutes
    @Bean
    public MongoCustomConversions customConversions(){
        return new MongoCustomConversions(
                Arrays.asList(
                        new InstatToLongConverter(),
                        new LongToInstantConverter()
                )
        );
    }

     */
}

/*
* Here we have our actual converter classes
*
@ReadingConverter
class LongToInstantConverter implements Converter<Long, Instant> {
    @Override
    public Instant convert(Long source) {
        return Instant.ofEpochMilli(source);
    }
}

@WritingConverter
class InstatToLongConverter implements Converter<Instant, Long> {
    @Override
    public Long convert(Instant source) {
        return source.toEpochMilli();
    }
}

*/







