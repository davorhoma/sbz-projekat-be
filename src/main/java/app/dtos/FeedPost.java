package app.dtos;

import java.time.LocalDateTime;

public class FeedPost {
    private PostDTO post;
    private UserDTO currentUser;
    private boolean isFriend;
    private boolean isPopular;
    private boolean hasPopularHashtag;
    private boolean userLikedSimilarHashtag;
    private boolean userCreatedHashtag;
    private LocalDateTime now = LocalDateTime.now();
    
    private boolean visible = false;
    
    public FeedPost() {}
    
	public FeedPost(PostDTO post, UserDTO currentUser, boolean isFriend, boolean isPopular, boolean hasPopularHashtag,
			boolean userLikedSimilarHashtag, boolean userCreatedHashtag, LocalDateTime now) {
		super();
		this.post = post;
		this.currentUser = currentUser;
		this.isFriend = isFriend;
		this.isPopular = isPopular;
		this.hasPopularHashtag = hasPopularHashtag;
		this.userLikedSimilarHashtag = userLikedSimilarHashtag;
		this.userCreatedHashtag = userCreatedHashtag;
		this.now = now;
	}
	
	public PostDTO getPost() {
		return post;
	}
	
	public void setPost(PostDTO post) {
		this.post = post;
	}
	
	public UserDTO getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(UserDTO currentUser) {
		this.currentUser = currentUser;
	}
	
	public boolean isFriend() {
		return isFriend;
	}
	
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	
	public boolean isPopular() {
		return isPopular;
	}
	
	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}
	
	public boolean isHasPopularHashtag() {
		return hasPopularHashtag;
	}
	
	public void setHasPopularHashtag(boolean hasPopularHashtag) {
		this.hasPopularHashtag = hasPopularHashtag;
	}
	
	public boolean isUserLikedSimilarHashtag() {
		return userLikedSimilarHashtag;
	}
	
	public void setUserLikedSimilarHashtag(boolean userLikedSimilarHashtag) {
		this.userLikedSimilarHashtag = userLikedSimilarHashtag;
	}
	
	public boolean isUserCreatedHashtag() {
		return userCreatedHashtag;
	}
	
	public void setUserCreatedHashtag(boolean userCreatedHashtag) {
		this.userCreatedHashtag = userCreatedHashtag;
	}
	
	public LocalDateTime getNow() {
		return now;
	}
	
	public void setNow(LocalDateTime now) {
		this.now = now;
	}
	
	public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    
}