
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import acmx.export.javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initialising the interactors in the
	 * application, and taking care of any other initialisation that needs to be
	 * performed.
	 */
	public void init() {

		JButton addAcc = new JButton("Add");
		JButton deleteAcc = new JButton("Delete");
		JButton lookupAcc = new JButton("Lookup");
		JButton changeSta = new JButton("Change Status");
		JButton changePic = new JButton("Change Picture");
		JButton addFriend = new JButton("Add Friend");

		initTextFields();

		add(new JLabel("Name"), NORTH);
		add(nameField, NORTH);
		add(addAcc, NORTH);
		add(deleteAcc, NORTH);
		add(lookupAcc, NORTH);

		add(statusField, WEST);
		add(changeSta, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(pictureField, WEST);
		add(changePic, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		add(friendField, WEST);
		add(addFriend, WEST);

		addActionListeners();
		initClasses();
	}

	/* This method initialises other classes. */
	private void initClasses() {
		dataBaseClass = new FacePamphletDatabase();
		canvasClass = new FacePamphletCanvas();
		add(canvasClass, CENTER);
		currProfile = null;
	}

	/* Creates all needed buttons */
	private void initTextFields() {
		nameField = new JTextField(TEXT_FIELD_SIZE);
		friendField = new JTextField(TEXT_FIELD_SIZE);
		statusField = new JTextField(TEXT_FIELD_SIZE);
		pictureField = new JTextField(TEXT_FIELD_SIZE);

		friendField.setActionCommand("Add Friend");
		statusField.setActionCommand("Change Status");
		pictureField.setActionCommand("Change Picture");

		nameField.addActionListener(this);
		friendField.addActionListener(this);
		statusField.addActionListener(this);
		pictureField.addActionListener(this);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		boolean nameTxtFill = !nameField.getText().equals("");
		txtName = nameField.getText();

		if (nameTxtFill && action.equals("Add")) {
			canvasClass.removeAll();
			addProfile();
		} else if (nameTxtFill && action.equals("Delete")) {
			canvasClass.removeAll();
			deleteProfile();
		} else if (nameTxtFill && action.equals("Lookup")) {
			lookupProfile();
		}

		else if (!statusField.getText().equals("") && action.equals("Change Status")) {
			canvasClass.removeAll();
			changeStatus();
		} else if (!pictureField.getText().equals("")
				&& (action.equals("Change Picture") || e.getSource() == pictureField)) {
			canvasClass.removeAll();
			changePicture();
		} else if (nameTxtFill && action.equals("Add Friend")) {
			canvasClass.removeAll();
			addFriend();
		}
		clearTxt();

	}

	private void addFriend() {
		if (currProfile != null) {
			canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));
			if (dataBaseClass.getProfile(friendField.getText()) != null && !friendField.getText().equals(currProfile.getName())) {
				if ((dataBaseClass.getProfile(currProfile.getName())).addFriend(friendField.getText()) == true) {
					dataBaseClass.getProfile(friendField.getText()).addFriend(currProfile.getName());
					canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));
					canvasClass.showMessage(friendField.getText() + " is added as friend");
				} else {
					canvasClass.showMessage("already has " + friendField.getText() + " as a friend");
				}
			} else {
				canvasClass.showMessage(friendField.getText() + " does not exist");
			}
		} else {
			canvasClass.showMessage("Please select a profile to add friends");
		}
	}

	private void changePicture() {
		if (currProfile == null) {
			canvasClass.showMessage("Please select a profile to change picture");

		} else {
			canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));

			GImage img = null;
			try {
				img = new GImage(pictureField.getText());
			} catch (ErrorException ex) {
				// Code that is executed if the filename cannot be opened.
			}

			if (img != null) {
				dataBaseClass.getProfile(currProfile.getName()).setImage(img);
				canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));
				canvasClass.showMessage("picture updated");
			} else {
				canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));
				canvasClass.showMessage("Unable to open image file: " + pictureField.getText());
			}
		}
	}

	private void changeStatus() {
		if (currProfile == null) {
			canvasClass.showMessage("Please select profile to change status. ");
		} else {
			dataBaseClass.getProfile(currProfile.getName()).setStatus(statusField.getText());
			canvasClass.displayProfile(dataBaseClass.getProfile(currProfile.getName()));
			canvasClass.showMessage("Status update to: " + statusField.getText());
		}
	}

	private void lookupProfile() {
		if (!dataBaseClass.containsProfile(txtName)) {
			canvasClass.removeAll();
			canvasClass.showMessage("A profile with the name: " + txtName + " does not exist");
		} else {
			canvasClass.displayProfile(dataBaseClass.getProfile(txtName));
			canvasClass.showMessage("Displaying " + txtName);
		}
		currProfile = dataBaseClass.getProfile(txtName);
	}

	private void deleteProfile() {
		if (!dataBaseClass.containsProfile(txtName)) {
			canvasClass.removeAll();
			canvasClass.showMessage("Profile with given name doest not exist :/");
		} else {
			Iterator<String> friends = dataBaseClass.getProfile(txtName).getFriends();
			while (friends.hasNext()) {
				dataBaseClass.getProfile(friends.next()).removeFriend(txtName);
			}
			currProfile = null;
			dataBaseClass.deleteProfile(txtName);
			canvasClass.showMessage("Profile with username of " + txtName + " deleted");
		}
	}

	private void addProfile() {

		if (dataBaseClass.containsProfile(txtName)) {
			canvasClass.displayProfile(dataBaseClass.getProfile(txtName));
			canvasClass
					.showMessage("A profile with the name:  " + dataBaseClass.getProfile(txtName) + " already exists");
		} else {
			dataBaseClass.addProfile(new FacePamphletProfile(txtName));
			canvasClass.displayProfile(dataBaseClass.getProfile(txtName));
			canvasClass.showMessage("New profile is created.");
		}
		currProfile = dataBaseClass.getProfile(txtName);
	}
	
	private void clearTxt() {
		pictureField.setText("");
		nameField.setText("");
		statusField.setText("");
		//friendField.setText("");
	}
	private String txtName;
	private JTextField nameField, friendField, statusField, pictureField;
	private FacePamphletDatabase dataBaseClass;
	private FacePamphletProfile currProfile;
	private FacePamphletCanvas canvasClass;

}
