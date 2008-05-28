package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AccountingAnswer;

public interface AccountingServerSessionActivity extends
		AccountingSessionActivity {

	void sendAccountAnswer(AccountingAnswer answer) throws IOException;

	
}
