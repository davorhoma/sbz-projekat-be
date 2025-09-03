package app.mappers;

import app.dtos.PostDTO;
import app.model.Post;

public class PostMapper {
	public static PostDTO toDto(Post post) {
		if (post == null) return null;
		
		PostDTO dto = new PostDTO(
				post.getId(),
				post.getText(),
				post.getHashtags(),
				post.getLikes().size(),
				post.getReports().size(),
				post.getUser().getFirstName(),
                post.getUser().getLastName());
				
		return dto;
	}
}
