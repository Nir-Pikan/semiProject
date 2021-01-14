package serverLogic;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses( {EntryControllerTest.class,SubscriberControllerTest.class,WorkerControllerTest.class} )
class AllTests {


}
