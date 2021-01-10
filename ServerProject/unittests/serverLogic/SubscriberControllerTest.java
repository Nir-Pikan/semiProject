package serverLogic;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import db.DbController;

class SubscriberControllerTest {

	@Mock
	ResultSet sub;
	
	@Mock
	ResultSet credit;
	
	@Mock
	DbController db;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testHandleRequest() {
		fail("Not yet implemented"); // TODO
	}

}
