package MiniTwitter;

import javax.swing.*;
import java.awt.*;

/**
 * Here we have the implementation of the GUI for an individual user's view;
 * Below the code allows the user to post tweets, see their news feed and followings, and follow other users.
 */
public class UserViewGUI {
    private JFrame frame;
    private JTextArea tweetInput;
    private JButton postTweetButton;
    private JList<String> followingList;
    private JList<String> newsFeedList;
    private DefaultListModel<String> followingListModel;
    private DefaultListModel<String> newsFeedListModel;
    private JLabel followerCountLabel; // Label to display the number of followers
    private JTextField followUserInput; // Input field for user ID to follow
    private JButton followUserButton; // Button to follow user

    public UserViewGUI(User user) {
        frame= new JFrame(user.getId() + "'s View");
        frame.setSize(600, 400);
        user.setUserViewGUI(this);

        // Initializing the layout and framework of the program 
        tweetInput=new JTextArea(2, 20);
        postTweetButton=new JButton("Post Tweet");
        followingListModel=new DefaultListModel<>();
        followingList=new JList<>(followingListModel);
        newsFeedListModel=new DefaultListModel<>();
        newsFeedList=new JList<>(newsFeedListModel);
        followerCountLabel= new JLabel("Followers: " + user.getFollowerCount()); 
        followUserInput=new JTextField(10);
        followUserButton= new JButton("Follow User");

        // Layout for tweet section
        JPanel tweetPanel= new JPanel(new BorderLayout());
        tweetPanel.add(new JScrollPane(tweetInput), BorderLayout.CENTER);
        tweetPanel.add(postTweetButton, BorderLayout.EAST);
        JPanel followPanel=new JPanel(new BorderLayout());
        followPanel.add(new JLabel("User ID to Follow:"), BorderLayout.WEST);
        followPanel.add(followUserInput, BorderLayout.CENTER);
        followPanel.add(followUserButton, BorderLayout.EAST);

        //Final layout
        JPanel mainPanel= new JPanel(new BorderLayout());
        JPanel eastPanel=new JPanel(new GridLayout(2, 1));
        eastPanel.add(followerCountLabel);
        eastPanel.add(new JScrollPane(followingList));
        mainPanel.add(tweetPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(newsFeedList), BorderLayout.CENTER);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        mainPanel.add(followPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        postTweetButton.addActionListener(e -> postTweet(user));
        followUserButton.addActionListener(e -> followUser(user));

        updateFollowingList(user);
        updateNewsFeed(user);

        frame.setVisible(true);
    }

    // Code updates newsfeed for followers
    private void postTweet(User user) {
        String tweet=tweetInput.getText().trim();
        if (tweet.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Tweet cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        user.postTweet(tweet);
        updateNewsFeed(user);
    }

    // Follows another user and updates the following list
    private void followUser(User user) {
        String userIdToFollow=followUserInput.getText().trim();
        if (userIdToFollow.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "User ID cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User userToFollow=null;
        for (User u : AdminControlPanel.getInstance().getUsers()) {
            if (u.getId().equals(userIdToFollow)) {
                userToFollow = u;
                break;
            }
        }

        if (userToFollow==null) {
            JOptionPane.showMessageDialog(frame, "User ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.follow(userToFollow);
        updateFollowingList(user);
        JOptionPane.showMessageDialog(frame, "You are now following " + userIdToFollow, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

   //Most of this code deals with real-time updating of followers & newsfeed 
    private void updateFollowingList(User user) {
        followingListModel.clear();
        for (String following : user.getFollowings()) {
            followingListModel.addElement(following);
        }
    }

    public void updateNewsFeed(User user) {
        newsFeedListModel.clear();
        for (String tweet : user.getNewsFeed()) {
            newsFeedListModel.addElement(tweet);
        }
    }

    public void updateFollowerCount(User user) {
        followerCountLabel.setText("Followers: " + user.getFollowerCount());
    }
}
