package app.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.PostDTO;
import app.mappers.PostMapper;
import app.model.Post;
import app.model.Report;
import app.model.User;
import app.utils.JwtUtil;

@Service
public class ReportService {

	@Autowired
    private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	public PostDTO reportPost(String postIdStr, String token) throws Exception {		
		UUID postId = UUID.fromString(postIdStr);

        UUID userId = jwtUtil.extractUserId(token);
        User user = userService.findById(userId);

        Post post = postService.get(postId);

        boolean alreadyReported = post.getReports().stream()
                .anyMatch(report -> report.getUserId().equals(user.getId()));

        if (alreadyReported) {
            throw new IllegalStateException("User has already reported this post.");
        }
        
        boolean isPostCreatedByUser = post.getUser().getId().equals(user.getId());
        if (isPostCreatedByUser) {
        	throw new IllegalStateException("You cannot report your own post");
        }
            
        Report newReport = new Report(user.getId(), post);
        
        post.addReport(newReport);
        Post saved = postService.save(post);
        
        return PostMapper.toDto(saved);			
	}
}
