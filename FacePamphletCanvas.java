
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel lab = new GLabel(msg);
		lab.setFont(MESSAGE_FONT);
		add(lab, (getWidth() - lab.getWidth()) / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		if (profile == null) {
			showMessage("A profile with the name does not exist");
			return;
		}
		removeAll();
		GLabel name = new GLabel(profile.getName());

		displayName(profile, name);
		displayPhoto(profile, name);
		displayStatus(profile, name);
		displayFriends(profile, name);

	}

	/* This mini-method displays profile name on canvas. */
	private void displayName(FacePamphletProfile profile, GLabel nameLab) {
		nameLab.setColor(Color.RED);
		nameLab.setFont(PROFILE_NAME_FONT);
		add(nameLab, nameLab.getAscent() + LEFT_MARGIN, TOP_MARGIN);
	}

	/* This mini-method displays profile photo - image on canvas. */
	private void displayPhoto(FacePamphletProfile profile, GLabel nameLab) {
		if (profile.getImage() != null) {
			profile.getImage().setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
			add(profile.getImage(), LEFT_MARGIN, IMAGE_MARGIN + nameLab.getY());
		} else {
			drawEmptyRect(nameLab);
		}
	}

	/* This mini-method displays profile status on canvas. */
	private void displayStatus(FacePamphletProfile profile, GLabel nameLab) {
		GLabel statusLab = new GLabel("No current status");
		if (!profile.getStatus().equals("")) {
			statusLab.setLabel(profile.getName() + " is " + profile.getStatus());
		}
		statusLab.setFont(PROFILE_STATUS_FONT);
		add(statusLab, LEFT_MARGIN,
				statusLab.getAscent() + nameLab.getY() + IMAGE_HEIGHT + IMAGE_MARGIN + STATUS_MARGIN);
	}

	/* This mini-method displays profile friends on canvas. */
	private void displayFriends(FacePamphletProfile profile, GLabel nameLab) {
		GLabel lab = new GLabel("Friends");
		lab.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(lab, getWidth() / 2, IMAGE_MARGIN + nameLab.getY());

		Iterator<String> friendList = profile.getFriends();
		double distance = lab.getHeight();
		int friendNum = 1;
		while (friendList.hasNext()) {
			GLabel friends = new GLabel(friendList.next());
			friends.setFont(PROFILE_FRIEND_FONT);
			add(friends, getWidth() / 2,   distance * (friendNum++)+ friends.getY() + 2*BOTTOM_MESSAGE_MARGIN);
		}
	}

	/* This method draws empty rectangle for profile that do not have any image. */
	private void drawEmptyRect(GLabel nameLab) {
		GRect emptyImage = new GRect(LEFT_MARGIN, nameLab.getY() + IMAGE_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT);
		add(emptyImage);

		GLabel noImage = new GLabel("No Image");
		noImage.setFont(PROFILE_IMAGE_FONT);
		add(noImage, (IMAGE_WIDTH - noImage.getWidth()) / 2 + LEFT_MARGIN,
				IMAGE_MARGIN + (IMAGE_HEIGHT + noImage.getAscent()) / 2 + nameLab.getY());
	}

}
