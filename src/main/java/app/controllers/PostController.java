package app.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.PostCreateDTO;
import app.dtos.PostDTO;
import app.services.PostService;
import app.utils.JwtUtil;

@RestController
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping
	public ResponseEntity<?> createPost(@RequestBody PostCreateDTO request,
			@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			PostDTO createdPost = postService.create(request.text, request.hashtags, token);
			
			return ResponseEntity.ok(createdPost);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public ResponseEntity<List<PostDTO>> getAllPosts(@RequestHeader("Authorization") String authHeader) {
		try {
			UUID userId = jwtUtil.extractUserId(authHeader.replace("Bearer ", ""));
	        List<PostDTO> fetchedPosts = postService.getAll(userId);
	        return ResponseEntity.ok(fetchedPosts);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/user-posts")
	public ResponseEntity<List<PostDTO>> getUserPosts(@RequestHeader("Authorization") String authHeader) {
		try {
	        UUID userId = jwtUtil.extractUserId(authHeader.replace("Bearer ", ""));
	        List<PostDTO> userPosts = postService.getPostsForUser(userId);
	        return ResponseEntity.ok(userPosts);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().build();
	    }
	}
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/like")
	public ResponseEntity<PostDTO> updateLikeStatus(@RequestBody String postId, @RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			PostDTO post = postService.updateLikeStatus(postId, token);
			return ResponseEntity.ok(post);
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/feed")
	public ResponseEntity<List<PostDTO>> getFeedPosts(@RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			List<PostDTO> feedPosts = postService.getFeedPosts(token);
			
			return ResponseEntity.ok(feedPosts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
