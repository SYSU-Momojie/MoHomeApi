package com.cn.momojie.es;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:es-config.xml"},
		initializers = {ConfigDataApplicationContextInitializer.class})
@ActiveProfiles("es")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@TestPropertySource(properties = { "spring.config.location=classpath:application-es.yml" })
public abstract class BaseTest {
}
