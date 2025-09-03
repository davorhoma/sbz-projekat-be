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
	        return new PostDTO(saved);			
		} catch (Exception e) {
			throw new Exception("Error creating new post");
		}
	}

	public List<PostDTO> getAll() throws Exception {
		try {
			return postRepository.findAll().stream()
                    .map(PostMapper::toDto)
                    .collect(Collectors.toList());
		} catch (Exception e) {
			throw new Exception("Error fetching posts");
		}
	}
	
	public List<PostDTO> getPostsForUser(UUID userId) {
        return postRepository.findAllByUserId(userId)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }
	
	public Post get(UUID postId) throws Exception {
		return postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));			
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	public PostDTO likePost(String postIdStr, String token) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		User user = userService.findById(userId);
		
		UUID postId = UUID.fromString(postIdStr);
		Post post = get(postId);

		boolean alreadyLiked = post.getLikes().stream()
                .anyMatch(like -> like.getUserId().equals(user.getId()));

		if (alreadyLiked) {
            throw new IllegalStateException("User has already liked this post.");
        }
		
		Like newLike = new Like(userId, postId);
		post.addLike(newLike);
		Post saved = save(post);
		
		return PostMapper.toDto(saved);
	}

}
