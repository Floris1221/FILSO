package com.ale.filso.trash;

import com.ale.filso.models.User.Role;
import com.ale.filso.models.User.User;
import com.ale.filso.models.User.UserRepo;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {


    public void loadData(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        //return args -> { };

//
//            Logger logger = LoggerFactory.getLogger(getClass());
//            if (samplePersonRepo.count() != 0L) {
//                logger.info("Using existing database");
//                return;
//            }
//            int seed = 123;
//
//            logger.info("Generating demo data");
//
//            logger.info("... generating Sample Person entities...");
//            ExampleDataGenerator<SamplePerson> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
//                    SamplePerson.class, LocalDateTime.of(2022, 1, 24, 0, 0, 0));
//            //          samplePersonRepositoryGenerator.setData(SamplePerson::setId, DataType.ID );
//            samplePersonRepositoryGenerator.setData(SamplePerson::setFirstName, DataType.FIRST_NAME);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setLastName, DataType.LAST_NAME);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setEmail, DataType.EMAIL);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setPhone, DataType.PHONE_NUMBER);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setDateOfBirth, DataType.DATE_OF_BIRTH);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
//            samplePersonRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
//            samplePersonRepo.saveAll(samplePersonRepositoryGenerator.create(10, seed));
//
//
            //logger.info("... generating 2 User entities...");
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Lenon");
            user.setLogin("user");
            user.setActive(true);
            user.setEmail("s.rogiewicz@ndi.com.pl");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);

            User admin = new User();
            admin.setFirstName("Emma");
            admin.setLastName("Thompson");
            admin.setLogin("admin");
            admin.setActive(true);
            admin.setEmail("s.rogiewicz@ndi.com.pl");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
            userRepo.save(admin);
//
//            DictionaryGroup dg1 = new DictionaryGroup();
//            dg1.setCode("SEX");
//            dg1.setName("Płeć");
//            dg1.setActive(false);
//            dg1.setDescription("słownik płci");
//            dictGroupRepo.save(dg1);
//
//            Dictionary dict1 = new Dictionary();
//            dict1.setShortName("M");
//            dict1.setName("Mężczyzna");
//            dict1.setActive(false);
//            dict1.setDescription("wartość słownika SEX");
//            dict1.setDictionaryGroup(dg1);//   dictGroupRepo.findById(1).get());
//            dictRepo.save(dict1);
//
//            Dictionary dict2 = new Dictionary();
//            dict2.setShortName("K");
//            dict2.setName("Kobieta");
//            dict2.setActive(true);
//            dict2.setDescription("wartość słownika SEX");
//            dict2.setDictionaryGroup(dg1); //dictGroupRepo.findById(1).get());
//            dictRepo.save(dict2);
//
//            DictionaryGroup dg2 = new DictionaryGroup();
//            dg2.setCode("LEGAL_FORM");
//            dg2.setName("Forma prawna");
//            dg2.setActive(true);
//            dg2.setDescription("słownik formy prawne");
//            dictGroupRepo.save(dg2);
//
//            Dictionary dict3 = new Dictionary();
//            dict3.setShortName("JDK");
//            dict3.setName("jednoosobowa działalność gospodarcza");
//            dict3.setActive(false);
//            dict3.setDescription("wartość słownika forma prawna");
//            dict3.setDictionaryGroup(dg2);
//            dictRepo.save(dict3);
//
//            Dictionary dict4 = new Dictionary();
//            dict4.setShortName("sp. z o.o");
//            dict4.setName("spółka z o.o.");
//            dict4.setActive(true);
//            dict4.setDescription("wartość słownika forma prawna");
//            dict4.setDictionaryGroup(dg2);
//            dictRepo.save(dict4);
//
//
//            ExampleDataGenerator<Dictionary> sampleDictRepositoryGenerator = new ExampleDataGenerator<>(
//                    Dictionary.class, LocalDateTime.of(2022, 1, 24, 0, 0, 0));
//            //          samplePersonRepositoryGenerator.setData(SamplePerson::setId, DataType.ID );
//            sampleDictRepositoryGenerator.setData(Dictionary::setName, DataType.FIRST_NAME);
//            sampleDictRepositoryGenerator.setData(Dictionary::setShortName, DataType.LAST_NAME);
//            sampleDictRepositoryGenerator.setData(Dictionary::setDescription, DataType.EMAIL);
//            sampleDictRepositoryGenerator.setData(Dictionary::setActive, DataType.BOOLEAN_50_50);
//
//
//            List<Dictionary> dict = sampleDictRepositoryGenerator.create(30, seed);
//            for(Dictionary dicter : dict) {
//                dicter.setDictionaryGroup(dg1);
//            }
//
//  //          sampleDictRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
//    //        sampleDictRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
//            dictRepo.saveAll(dict);
//
//            logger.info("Generated demo data");
//        };

    }
}