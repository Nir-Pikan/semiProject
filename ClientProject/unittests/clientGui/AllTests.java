package clientGui;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses( {EntryReportControllerTest.class,LoginControllerTest.class} )
class AllTests {


}
