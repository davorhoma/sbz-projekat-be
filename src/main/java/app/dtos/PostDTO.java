package app.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import app.model.Post;

public class PostDTO {

	public UUID id;
	public String text;
	public List<String> hashtags;
	public int numberOfLikes;
	public int numberOfReports;
	public UUID userId;
	public String userFirstName;
    public String userLastName;
    public boolean likedByCurrentUser;
    public LocalDateTime createdAt;
	
	public PostDTO() {}
	
	public PostDTO(Post post, UUID currentUserId) {
        this.id = post.getId();
        this.text = post.getText();
        this.numberOfLikes = post.getLikes().size();
        this.hashtags = post.getHashtags();
        this.numberOfReports = post.getReports().size();
        this.userId = post.getUser().getId();
        this.userFirstName = post.getUser().getFirstName();
        this.userLastName = post.getUser().getLastName();
        this.likedByCurrentUser = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUserId));
        this.createdAt = post.getCreatedAt();
    }
	
	public PostDTO(UUID id, String text, List<String> hashtags, 
			int numberOfLikes, int numberOfReports, UUID userId, String userFirstName, String userLastName, 
			boolean likedByCurrentUser, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.text = text;
		this.hashtags = hashtags;
		this.numberOfLikes = numberOfLikes;
		this.numberOfReports = numberOfReports;
		this.userId = userId;
		this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.likedByCurrentUser = likedByCurrentUser;
        this.createdAt = createdAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public List<String> getHashtags() {
		return hashtags;
	}
	
	public UUID getId() {
		return id;
	}

	public boolean isCreatedInLast24Hours() {
	    return createdAt.isAfter(LocalDateTime.now().minusHours(24));
	}

	
}
