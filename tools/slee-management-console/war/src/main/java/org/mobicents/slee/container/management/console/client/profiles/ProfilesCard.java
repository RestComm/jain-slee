package org.mobicents.slee.container.management.console.client.profiles;

import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.Card;

public class ProfilesCard extends Card{
	private BrowseContainer browseContainer = new BrowseContainer();
	
	
	public ProfilesCard() {
		super();
	    initWidget(browseContainer);
	}

	@Override
	public void onShow() {
		browseContainer.empty();
	    ProfileTablesListPanel profileTablesListPanel = new ProfileTablesListPanel(browseContainer);
	    browseContainer.add("Tables", profileTablesListPanel);		
	}

	@Override
	public void onHide() {
				
	}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}

}
