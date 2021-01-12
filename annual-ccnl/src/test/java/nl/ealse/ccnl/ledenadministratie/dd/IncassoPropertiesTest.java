package nl.ealse.ccnl.ledenadministratie.dd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = IncassoPropertiesTest.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories("nl.ealse.ccnl.ledenadministratie.model.dao")
@EntityScan("nl.ealse.ccnl.ledenadministratie.model")
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=update"})
@ComponentScan("nl.ealse.ccnl.ledenadministratie.dd")
class IncassoPropertiesTest {

  @Autowired
  private IncassoProperties props;

  @Test
  void testProperty() {
    Assertions.assertEquals("NL65ZZZ403419230000", props.getIncassantId());
    Assertions.assertEquals("NL97INGB0004160835", props.getIbanNummer());
  }


}
