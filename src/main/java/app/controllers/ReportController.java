package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.PostDTO;
import app.dtos.ReportPostRequest;
import app.services.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping
	public ResponseEntity<PostDTO> reportPost(@RequestBody ReportPostRequest request, @RequestHeader("Authorization") String authHeader) {
		try {
			String token = authHeader.replace("Bearer ", "");
			return ResponseEntity.ok(reportService.reportPost(request.getPostId(), token));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}