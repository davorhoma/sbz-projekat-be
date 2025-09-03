package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.PostDTO;
import app.mappers.PostMapper;
import app.model.Like;
import app.model.Post;
import app.model.User;
import app.repositories.PostRepository;
import app.utils.JwtUtil;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	public PostDTO create(String text, List<String> hashtags, String token) throws Exception {
		try {
			User user = userService.findById(jwtUtil.extractUserId(token));
			
			Post newPost = new Post(text, hashtags);
			newPost.setUser(user);
			
			Post saved = postRepository.save(newPost);
	        return new PostDTO(saved, user.getId());			
		} catch (Exception e) {
			throw new Exception("Error creating new post");
		}
	}

	public List<PostDTO> getAll(UUID userId) throws Exception {
		try {
			return postRepository.findAll().stream()
					.map(post -> new PostDTO(post, userId))
	                .collect(Collectors.toList());
		} catch (Exception e) {
			throw new Exception("Error fetching posts");
		}
	}
	
	public List<PostDTO> getPostsForUser(UUID userId) {
        return postRepository.findAllByUserId(userId)
                .stream()
                .map(post -> new PostDTO(post, userId))
                .collect(Collectors.toList());
    }
	
	public Post get(UUID postId) throws Exception {
		return postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));			
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	public PostDTO updateLikeStatus(String postIdStr, String token) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		User user = userService.findById(userId);
		
		UUID postId = UUID.fromString(postIdStr);
		Post post = get(postId);

		Like existingLike = post.getLikes().stream()
	            .filter(like -> like.getUser().getId().equals(user.getId()))
	            .findFirst()
	            .orElse(null);

	    if (existingLike != null) {
	        post.getLikes().remove(existingLike);
	    } else {
	        Like newLike = new Like(user, post);
	        post.addLike(newLike);
	    }
		
		Post saved = save(post);
		
		return PostMapper.toDto(saved, userId);
	}

}
