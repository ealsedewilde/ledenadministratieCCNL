package nl.ealse.ccnl.ledenadministratie.test;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

// Mixing suites and separate tests doesn't work
@Suite
@SelectPackages({"nl.ealse.ccnl.ledenadministratie.model.dao"})
class JpaSuiteTest {

}
