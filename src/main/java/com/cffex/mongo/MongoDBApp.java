package com.cffex.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Ming on 2017/4/25.
 */
public class MongoDBApp {
    static final Logger logger = LoggerFactory.getLogger(MongoDBApp.class);

    public static void main( String[] args ) {
        logger.info("Bootstrapping MongoDemo application");

        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        PersonRepository personRepository = context.getBean(PersonRepository.class);

        // cleanup person collection before insertion
        personRepository.dropPersonCollection();

        //create person collection
        personRepository.createPersonCollection();

        for(int i=0; i<20; i++) {
            personRepository.insertPersonWithNameJohnAndRandomAge();
        }

        personRepository.logAllPersons();
        logger.info("Avarage age of a person is: {}", personRepository.getAvarageAgeOfPerson());

        logger.info("Finished MongoDemo application");
    }
}
