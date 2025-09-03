package app.dtos;

import java.util.List;
import java.util.UUID;

import app.model.Post;

public class PostDTO {

	public UUID id;
	public String text;
	public List<String> hashtags;
	public int numberOfLikes;
	public int numberOfReports;
	public String userFirstName;
    public String userLastName;
    public boolean likedByCurrentUser;
	
	public PostDTO() {}
	
	public PostDTO(Post post, UUID currentUserId) {
        this.id = post.getId();
        this.text = post.getText();
        this.numberOfLikes = post.getLikes().size();
        this.hashtags = post.getHashtags();
        this.numberOfReports = post.getReports().size();
        this.userFirstName = post.getUser().getFirstName();
        this.userLastName = post.getUser().getLastName();
        this.likedByCurrentUser = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUserId));
    }
	
	public PostDTO(UUID id, String text, List<String> hashtags, int numberOfLikes, int numberOfReports, String userFirstName, String userLastName, boolean likedByCurrentUser) {
		super();
		this.id = id;
		this.text = text;
		this.hashtags = hashtags;
		this.numberOfLikes = numberOfLikes;
		this.numberOfReports = numberOfReports;
		this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.likedByCurrentUser = likedByCurrentUser;
	}
}
