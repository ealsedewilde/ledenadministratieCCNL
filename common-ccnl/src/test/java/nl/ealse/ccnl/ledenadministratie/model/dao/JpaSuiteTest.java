package nl.ealse.ccnl.ledenadministratie.model.dao;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// Mixing suites and separate tests doesn't work
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@SelectPackages({"nl.ealse.ccnl.ledenadministratie.util", "nl.ealse.ccnl.ledenadministratie.model",
    "nl.ealse.ccnl.ledenadministratie.model.dao"})
class JpaSuiteTest {

}
