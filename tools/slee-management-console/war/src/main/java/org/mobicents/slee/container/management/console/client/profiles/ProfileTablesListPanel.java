package org.mobicents.slee.container.management.console.client.profiles;

import org.mobicents.slee.container.management.console.client.Logger;
import org.mobicents.slee.container.management.console.client.ServerCallback;
import org.mobicents.slee.container.management.console.client.ServerConnection;
import org.mobicents.slee.container.management.console.client.common.BrowseContainer;
import org.mobicents.slee.container.management.console.client.common.ControlContainer;
import org.mobicents.slee.container.management.console.client.common.ListPanel;
import org.mobicents.slee.container.management.console.client.common.Title;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class ProfileTablesListPanel extends Composite {

	private BrowseContainer browseContainer;

	private ProfileServiceAsync service = ServerConnection.profileServiceAsync;

	private ControlContainer rootPanel = new ControlContainer();

	private ListPanel profileTablesListPanel = new ListPanel();

	private String[] tables;
	
	private static final int COLUMN_TABLE_NAME = 0;
	private static final int COLUMN_ACTION = 1;
	private static final int COLUMN_EMPTY = 2;

	private static final int ROW_PROFILE_SPECIFICATIONS_LIST = 0;
	private static final int ROW_TABLE_COUNT = 1;
	private static final int ROW_PROFILE_LIST_PANNEL = 2;
	private static final int ROW_PROFILE_TABLE_CREATION_TITLE = 3;
	private static final int ROW_PROFILE_TABLE_CREATION_PANEL = 4;

	private Label tableCount;

	private final TextBox tableNameTextBox = new TextBox();
	private final ListBox TypeListBox = new ListBox();

	public ProfileTablesListPanel(BrowseContainer browseContainer) {
		super();

		this.browseContainer = browseContainer;
		initWidget(rootPanel);

		rootPanel.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		addProfileSpecificationsListPannel();
		addRefreshPanel();
		addProfileListPanel();
		addProfileTableCreationPanel();
	}

	
	private void addProfileListPanel() {
		profileTablesListPanel.setHeader(COLUMN_TABLE_NAME, "Table Name");
		profileTablesListPanel.setHeader(COLUMN_ACTION, "Action");
		profileTablesListPanel.setHeader(COLUMN_EMPTY, "");
		profileTablesListPanel.setColumnWidth(COLUMN_TABLE_NAME, "100%");
		rootPanel.setWidget(ROW_PROFILE_LIST_PANNEL, 0, profileTablesListPanel);
	}

	private void addRefreshPanel() {
		Hyperlink refreshLink = new Hyperlink("refresh", "refresh");
		refreshLink.setStyleName("common-pointer-curser");
		refreshLink.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent arg0) {
				if(TypeListBox.getSelectedIndex() != 0){
					getTables(TypeListBox.getValue(TypeListBox.getSelectedIndex()));		
				}
			}
		});
		ControlContainer counterTypeTitlePanel = new ControlContainer();
		tableCount = new Label("");
		counterTypeTitlePanel.setWidget(0, 0, tableCount);
		counterTypeTitlePanel.setWidget(0, 1, new Image("images/refresh.gif"));
		counterTypeTitlePanel.setWidget(0, 2, refreshLink);
		counterTypeTitlePanel.getCellFormatter().setWidth(0, 0, "100%");
		rootPanel.setWidget(ROW_TABLE_COUNT, 0, counterTypeTitlePanel);
	}

	private void addProfileSpecificationsListPannel() {

		ControlContainer typeListPannel = new ControlContainer();
		typeListPannel.setWidth("");
		typeListPannel.setWidget(0, 0, new Label("Profile Specifications:"));
		typeListPannel.setWidget(0, 1, TypeListBox);
		TypeListBox.addItem("");
		TypeListBox.setWidth("100%");
		TypeListBox.setHeight("200%");
		TypeListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent arg0) {
				int selectedIndex = TypeListBox.getSelectedIndex();
				if(selectedIndex != 0 ){
					String profileSpecId = TypeListBox.getValue(selectedIndex);
					getTables(profileSpecId);
				}
			}
		});
		TypeListBox.setVisibleItemCount(1);
		rootPanel.setWidget(ROW_PROFILE_SPECIFICATIONS_LIST, 0, typeListPannel);

		ServerCallback callback = new ServerCallback(this) {
			@Override
			public void onSuccess(Object result) {
				String[] profileSpecificationIDs = (String[]) result;
				for (String type : profileSpecificationIDs) {
					TypeListBox.addItem(convertProfileSpecFormat(type),type);
				}
				TypeListBox.setSelectedIndex(0);
			}

			@Override
			public void onFailure(Throwable caught) {
				TypeListBox.clear();
				super.onFailure(caught);
			}

		};

		service.getProfileSpecifications(callback);
		
		
		
	}
	private void addProfileTableCreationPanel() {
		ControlContainer createProfileTablePanel = new ControlContainer();
		createProfileTablePanel.setWidth("");
		Button createProfileTableButton = new Button("Create");

		// createEntityPanel.setWidget(0, 0, new
		// Image("images/resources.createentity.gif"));
		createProfileTablePanel.setWidget(0, 0, new Label("Table Name:"));
		createProfileTablePanel.setWidget(0, 1, tableNameTextBox);

		createProfileTablePanel.setWidget(1, 0, createProfileTableButton);
		createProfileTableButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				int index = TypeListBox.getSelectedIndex();
				onCreateProfileTableClicked(tableNameTextBox.getText(), TypeListBox.getValue(index).toString());
			}
		});

		tableNameTextBox.addKeyDownHandler(new KeyDownHandler() {

			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					int index = TypeListBox.getSelectedIndex();
					onCreateProfileTableClicked(tableNameTextBox.getText(), TypeListBox.getValue(index).toString());
				}
			}
		});
		rootPanel.setWidget(ROW_PROFILE_TABLE_CREATION_TITLE, 0,
				new Title("Create Profile Table", Title.TITLE_LEVEL_1));
		rootPanel.setWidget(ROW_PROFILE_TABLE_CREATION_PANEL, 0, createProfileTablePanel);
	}
	private void getTables(String profileSpecId) {
		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				tables = (String[]) result;
				refreshTable();
				tableNameTextBox.setText("");
			}

			@Override
			public void onFailure(Throwable caught) {
				tables = null;
				refreshTable();
				super.onFailure(caught);
			}

		};
		service.getProfileTables(profileSpecId, callback);
	}
	private void refreshTable() {
		if (tables == null || tables.length == 0) {
			profileTablesListPanel.emptyTable();
			tableCount.setText("There are no profile tables");
			browseContainer.setTitle(this, "There are no profile tables");
			return;
		}

		tableCount.setText("Total: " + tables.length);

		browseContainer.setTitle(this, "Tables");

		profileTablesListPanel.emptyTable();
		for (int i = 0; i < tables.length; i++) {

			final String tableName = tables[i];

			Hyperlink profileTableLink = new Hyperlink(tableName, tableName);
			profileTableLink.setStyleName("common-pointer-curser");
			profileTableLink.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent arg0) {
					onProfileTableClicked(tableName);
				}
			});

			profileTablesListPanel.setCell(i, COLUMN_TABLE_NAME, profileTableLink);

			Hyperlink removeLink = new Hyperlink("remove", "remove table " + tableName);
			removeLink.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					onRemove(tableName);
				}
			});
			
			Hyperlink renameLink = new Hyperlink("rename", "rename table " + tableName);
			renameLink.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent arg0) {
					onRename(tableName);
				}
			});

			profileTablesListPanel.setCell(i, COLUMN_ACTION, removeLink);
			profileTablesListPanel.setCell(i, COLUMN_EMPTY, renameLink);
			
			
		}
	}

	private void onCreateProfileTableClicked(final String name, final String type) {
		if ((name != null) && (type != null)) {
			ServerCallback callback = new ServerCallback(this) {
				public void onSuccess(Object result) {
					Logger.info("Profile Table: " + name + " created");
					if(TypeListBox.getSelectedIndex() != 0){
						getTables(TypeListBox.getValue(TypeListBox.getSelectedIndex()));		
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					if(TypeListBox.getSelectedIndex() != 0){
						getTables(TypeListBox.getValue(TypeListBox.getSelectedIndex()));		
					}
					super.onFailure(caught);
				}
			};
			service.createProfileTable(type, name, callback);
		}

	}

	private void onProfileTableClicked(String tableName) {
//		browseContainer.empty();
		ProfilesListPanel profileTablesListPanel = new ProfilesListPanel(browseContainer, tableName);
		browseContainer.add(tableName, profileTablesListPanel);
	}

	private void onRemove(final String tableName) {
		ServerCallback callback = new ServerCallback(this) {
			public void onSuccess(Object result) {
				Logger.info(tableName + " removed");
				if(TypeListBox.getSelectedIndex() != 0){
					getTables(TypeListBox.getValue(TypeListBox.getSelectedIndex()));		
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				if(TypeListBox.getSelectedIndex() != 0){
					getTables(TypeListBox.getValue(TypeListBox.getSelectedIndex()));		
				}
				super.onFailure(caught);
			}
		};
		service.removeProfileTable(tableName, callback);
	}
	private void onRename(final String tableName) {
		//TODO
	}
	private String convertProfileSpecFormat(String profileSpec){
		String componentString = profileSpec;
		String componentType = componentString.substring(0, componentString.indexOf('['));
		componentString = componentString.substring(componentType.length() + 1, componentString.length() - 1);
		String[] componentStringParts = componentString.split(",");
		String componentName = componentStringParts[0].substring("name=".length());
		String componentVendor = componentStringParts[1].substring("vendor=".length());
		String componentVersion = componentStringParts[2].substring("version=".length());
		return componentName+" / "+componentVendor+" / "+componentVersion;
	}
}
