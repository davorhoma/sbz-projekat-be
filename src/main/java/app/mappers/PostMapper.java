package app.mappers;

import java.util.UUID;

import app.dtos.PostDTO;
import app.model.Post;

public class PostMapper {
	public static PostDTO toDto(Post post, UUID userId) {
		if (post == null) return null;
		
		PostDTO dto = new PostDTO(
				post.getId(),
				post.getText(),
				post.getHashtags(),
				post.getLikes().size(),
				post.getReports().size(),
				post.getUser().getFirstName(),
                post.getUser().getLastName(),
                post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(userId)));
				
		return dto;
	}
}
