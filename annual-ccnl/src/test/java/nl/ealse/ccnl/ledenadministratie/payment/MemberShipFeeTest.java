package nl.ealse.ccnl.ledenadministratie.payment;

import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

class MemberShipFeeTest {
  
  SpringApplication springApplication;
  
  MemberShipFee memberShipFee;
  
  @Test
  void testMemberShipFee() {
    Assertions.assertEquals(27.5, memberShipFee.getIncasso());
    Assertions.assertEquals(30.0, memberShipFee.getOverboeken());
  }
  
  @BeforeEach
  void setup() {
    springApplication = new SpringApplicationBuilder().sources(MemberShipFee.class).build();
    ConfigurableApplicationContext context = springApplication.run();
    ConfigurableEnvironment environment = context.getEnvironment();
    environment.getPropertySources().addFirst(new TestResource());
    
    memberShipFee = context.getBean(MemberShipFee.class);
  }
  
  static class TestResource extends PropertySource<Properties> {

    public TestResource() {
      super("databaseProperties", new Properties());
      getSource().put("ccnl.contributie.incasso", "27,50");
      getSource().put("ccnl.contributie.overboeken", "30,00");
    }

    @Override
    public Object getProperty(String name) {
      return getSource().getProperty(name);
    }
    
  }
}
