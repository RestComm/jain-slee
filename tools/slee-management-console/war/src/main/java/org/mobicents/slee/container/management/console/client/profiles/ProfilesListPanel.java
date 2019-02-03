package org.mobicents.slee.container.management.console.client.profiles;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class ProfilesListPanel extends Composite implements ProfileAttributesListener {

	private BrowseContainer browseContainer;

	private ProfileServiceAsync service = ServerConnection.profileServiceAsync;

	private ControlContainer rootPanel = new ControlContainer();

	private ListBox profilesListBox;

	ProfileAttributesPanel profilePropertiesPanel;

	private PropertiesInfo profileProperties;


	private static final int ROW_PROFILES_REFRESH = 0;
	private static final int ROW_PROFILE_LIST_PANNEL = 1;
	private static final int ROW_PROFILE_MANAGEMENT_PANNEL = 2;
	private static final int ROW_SEPERATOR = 3;
	private static final int ROW_PROFILE_NAME_PANNEL = 4;
	private static final int ROW_PROFILE_DATA_PANNEL = 5;

	private Label profileCount;

	private final TextBox profileNameTextBox = new TextBox();

	private final Button createProfileButton = new Button("Create");
	private final Button removeProfileButton = new Button("Remove");
	private final Button restoreProfileButton = new Button("Restore");

	private String tableName;
	private ControlContainer profileManagementPanel;
	private ControlContainer profileNamePannel;

	private boolean creation = false;


	public ProfilesListPanel(BrowseContainer browseContainer, String tableName) {
		super();

		this.tableName = tableName;
		this.browseContainer = browseContainer;
		initWidget(rootPanel);

		Hyperlink refreshLink = new Hyperlink("refresh", "refresh");
		refreshLink.setStyleName("common-pointer-curser");
		refreshLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				addProfileList();
				try {

					if (rootPanel.isCellPresent(ROW_PROFILE_DATA_PANNEL, 0)) {
						rootPanel.clearCell(ROW_PROFILE_DATA_PANNEL, 0);
					}
					if (rootPanel.isCellPresent(ROW_PROFILE_NAME_PANNEL, 0)) {
						rootPanel.clearCell(ROW_PROFILE_NAME_PANNEL, 0);
					}
					profilesListBox.setEnabled(true);
					createProfileButton.setEnabled(true);
					removeProfileButton.setEnabled(true);
					restoreProfileButton.setEnabled(true);
					creation = false;
				} catch (Exception e) {
					Logger.error(e.getMessage());
				}
			}
		});

		ControlContainer counterTypeTitlePanel = new ControlContainer();
		profileCount = new Label("Profiles(0)");
		counterTypeTitlePanel.setWidget(0, 0, profileCount);
		counterTypeTitlePanel.setWidget(0, 1, new Image("images/refresh.gif"));
		counterTypeTitlePanel.setWidget(0, 2, refreshLink);
		counterTypeTitlePanel.getCellFormatter().setWidth(0, 0, "100%");
		rootPanel.setWidget(ROW_PROFILES_REFRESH, 0, counterTypeTitlePanel);

		rootPanel.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		addProfileList();
		addProfileManagementPanel();
		addSeperatorPanel(ROW_SEPERATOR);
		addProfileNamePanel();

	}

	private void addSeperatorPanel(int row) {
		ControlContainer seperatorPannel = new ControlContainer();
		seperatorPannel.setHeight("15px");
		rootPanel.setWidget(row, 0, seperatorPannel);
	}

	private void addProfileNamePanel() {
		profileNamePannel = new ControlContainer();
		profileNamePannel.setText(0, 0, "Profile Name");
		profileNamePannel.setWidget(1, 0, profileNameTextBox);
	}

	private void showProfile() {
		int selectedIndex = profilesListBox.getSelectedIndex();

		String profileName = profilesListBox.getValue(selectedIndex);
		getProfileAttributes(profileName);
	}

	private void addProfileList() {

		profilesListBox = new ListBox();
		profilesListBox.setFocus(true);
		ControlContainer profileListpannel = new ControlContainer();
		profileListpannel.setWidget(0, 0, profilesListBox);

		profilesListBox.setWidth("100%");
		profilesListBox.setVisibleItemCount(5);
		profilesListBox.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent var1) {
				showProfile();
			}
		});

		profilesListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent arg0) {
				showProfile();
			}
		});

		rootPanel.setWidget(ROW_PROFILE_LIST_PANNEL, 0, profileListpannel);

		ServerCallback callback = new ServerCallback(this) {
			@Override
			public void onSuccess(Object result) {
				String[] profiles = (String[]) result;
				for (String profile : profiles) {
					profilesListBox.addItem(profile);
				}
				profileCount.setText("Profiles(" + profiles.length + ")");
			}

			@Override
			public void onFailure(Throwable caught) {
				profilesListBox.clear();
				super.onFailure(caught);
			}

		};

		service.getProfiles(tableName, callback);
	}

	private void getProfileAttributes(final String profileName) {
		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				profileProperties = (PropertiesInfo) result;
				loadProfileProperties();
			}

			@Override
			public void onFailure(Throwable caught) {
				profileProperties = null;
				addProfileList();
				super.onFailure(caught);
			}
		};
		service.getProfileAttributes(tableName, profileName, callback);
	}

	private void addProfileManagementPanel() {

		profileManagementPanel = new ControlContainer();
		profileManagementPanel.setWidth("");

		createProfileButton.setStyleName("common-pointer-curser");
		removeProfileButton.setStyleName("common-pointer-curser");
		restoreProfileButton.setStyleName("common-pointer-curser");
		profileManagementPanel.setWidget(0, 1, createProfileButton);
		profileManagementPanel.setWidget(0, 2, removeProfileButton);
		profileManagementPanel.setWidget(0, 3, restoreProfileButton);

		createProfileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				onCreateProfileBtnClicked();
			}
		});

		removeProfileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				onRemoveProfileBtnClicked();
			}
		});
		restoreProfileButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent arg0) {
				onRestoreProfileBtnClicked();
			}

		});
		rootPanel.setWidget(ROW_PROFILE_MANAGEMENT_PANNEL, 0, profileManagementPanel);
	}

	private void onRestoreProfileBtnClicked() {
		final String profileName = profilesListBox.getItemText(profilesListBox.getSelectedIndex());
		ServerCallback callback = new ServerCallback(this) {
			@Override
			public void onSuccess(Object result) {
				showProfile();
			}

			@Override
			public void onFailure(Throwable caught) {
				Logger.error("failed to restore profile as it remains unchanged.");
			}

		};

		service.restoreProfile(tableName, profileName, callback);
	}

	private void onCreateProfileBtnClicked() {
		this.creation = true;
		rootPanel.setWidget(ROW_PROFILE_NAME_PANNEL, 0, profileNamePannel);
		profilesListBox.setEnabled(false);
		createProfileButton.setEnabled(false);
		removeProfileButton.setEnabled(false);
		restoreProfileButton.setEnabled(false);
		addCreateProfilePanel();
	}

	private void onRemoveProfileBtnClicked() {
		if (profilesListBox.getSelectedIndex() >= 0) {
			final String profileName = profilesListBox.getItemText(profilesListBox.getSelectedIndex());
			ServerCallback callback = new ServerCallback(this) {
				public void onSuccess(Object result) {
					Logger.info(profileName + " removed");
					addProfileList();
					try {
						if (rootPanel.isCellPresent(ROW_PROFILE_DATA_PANNEL, 0)) {
							rootPanel.clearCell(ROW_PROFILE_DATA_PANNEL, 0);
						}
						if (rootPanel.isCellPresent(ROW_PROFILE_NAME_PANNEL, 0)) {
							rootPanel.clearCell(ROW_PROFILE_NAME_PANNEL, 0);
						}

					} catch (Exception e) {
						Logger.error(e.getMessage());
					}

				}

				@Override
				public void onFailure(Throwable caught) {

					addProfileList();
					super.onFailure(caught);
				}
			};
			service.removeProfile(tableName, profileName, callback);
		}

	}

	private void addCreateProfilePanel() {
		getProfileAttributes(null);
	}

	private void loadProfileProperties() {
		try {
			profilePropertiesPanel = new ProfileAttributesPanel(profileProperties, this);
			if (creation) {
				profilePropertiesPanel.showCreationPanel();
			} else {
				profilePropertiesPanel.showEditPanel();
			}
			rootPanel.setWidget(ROW_PROFILE_DATA_PANNEL, 0, profilePropertiesPanel);
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}

	public void onSaveProperties(PropertiesInfo propertiesInfo) {
		try {
			this.profileProperties = propertiesInfo;
			setAttributes();
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}

	private void setAttributes() {

		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				if (creation) {
					creation = false;
					createProfileButton.setEnabled(true);
					removeProfileButton.setEnabled(true);
					restoreProfileButton.setEnabled(true);

					if (rootPanel.isCellPresent(ROW_PROFILE_NAME_PANNEL, 0)) {
						rootPanel.clearCell(ROW_PROFILE_NAME_PANNEL, 0);
					}
					Logger.info(profileNameTextBox.getText() + " is created Successfuly...");
					addProfileList();
					profileNameTextBox.setText("");

				} else {

					Logger.info(profilesListBox.getItemText(profilesListBox.getSelectedIndex())
							+ " data is set Successfuly...");
				}

				if (rootPanel.isCellPresent(ROW_PROFILE_DATA_PANNEL, 0)) {
					rootPanel.clearCell(ROW_PROFILE_DATA_PANNEL, 0);
				}

				showProfile();

			}

			@Override
			public void onFailure(Throwable caught) {

				if (creation) {
					Logger.info("faild to create profile : " + profileNameTextBox.getText() + " \n");
				} else {
					Logger.info("faild to set " + profilesListBox.getItemText(profilesListBox.getSelectedIndex())
							+ " data\n");
				}

				profilePropertiesPanel.onEditError();
				super.onFailure(caught);
			}

		};
		if (creation) {
			if (profileNameTextBox.getText().isEmpty() || profileNameTextBox.getText() == null) {
				Logger.error("empty Profile Name ");
				profilePropertiesPanel.onEditError();
			} else {
				service.createProfile(tableName, profileNameTextBox.getText(), profileProperties, callback);
			}

		} else {
			service.setProfileAttributes(tableName, profilesListBox.getItemText(profilesListBox.getSelectedIndex()),
					profileProperties, callback);
		}

	}

	public void onCancelProperties(PropertiesInfo propertiesInfo) {
		if (rootPanel.isCellPresent(ROW_PROFILE_DATA_PANNEL, 0)) {
			rootPanel.clearCell(ROW_PROFILE_DATA_PANNEL, 0);
		}
		if (rootPanel.isCellPresent(ROW_PROFILE_NAME_PANNEL, 0)) {
			rootPanel.clearCell(ROW_PROFILE_NAME_PANNEL, 0);
		}
		profilesListBox.setEnabled(true);
		createProfileButton.setEnabled(true);
		removeProfileButton.setEnabled(true);
		restoreProfileButton.setEnabled(true);
		creation = false;
		showProfile();

	}

}
