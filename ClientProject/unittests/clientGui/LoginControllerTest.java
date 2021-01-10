package clientGui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginControllerTest {
	static String errMsg = null;
	@Before
	static void setPopUp() {
	PopUp.myPop = new PopUp.IPopUp() {
		
		@Override
		public void showInformation(String title, String header, String body) {
		}
		
		@Override
		public void showError(String title, String header, String body) {
			errMsg = body;
		}
		
		@Override
		public boolean ask(String title, String header, String body) {
			return false;
		}
	};	
	}
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
