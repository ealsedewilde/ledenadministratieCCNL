package nl.ealse.ccnl.ledenadministratie.model.dao;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@EnableJpaRepositories("nl.ealse.ccnl.ledenadministratie.model.dao")
@EntityScan("nl.ealse.ccnl.ledenadministratie.model")
@ComponentScan("nl.ealse.ccnl.ledenadministratie.util")
@ContextConfiguration(classes = JpaSuiteTest.class)
public abstract class JpaTestBase {

}
