package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.BlockResponse;
import app.services.BlockService;

@RestController
@RequestMapping("/blocks")
public class BlockController {

	@Autowired
	private BlockService blockService;
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/{userId}")
	public ResponseEntity<BlockResponse> blockUser(@PathVariable String userId,
	                                               @RequestHeader("Authorization") String authHeader) {
	    try {
	        String token = authHeader.replace("Bearer ", "");
	        BlockResponse response = blockService.blockUser(token, userId);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(new BlockResponse(false, "Server error"));
	    }
	}
}
