package com.rfsystems.subscription.infrastructure;

import com.rfsystems.subscription.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class
})
@ExtendWith(TimeZoneSetup.class)
@SpringBootTest(classes = {
        WebServerConfig.class,
        IntegrationTestConfiguration.class,
})
@Tag("integrationTest")
public @interface IntegrationTest {
}
