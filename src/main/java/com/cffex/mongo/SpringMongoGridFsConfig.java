package com.cffex.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Created by Ming on 2017/4/28.
 */
@Configuration
public class SpringMongoGridFsConfig extends AbstractMongoConfiguration {

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
    @Override
    protected String getDatabaseName() {
        return "yourdb";
    }
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }
}
