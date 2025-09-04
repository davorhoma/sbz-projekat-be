package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.FriendRequestDTO;
import app.dtos.UpdateFriendStatusRequest;
import app.dtos.UserDTO;
import app.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/search")
	public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam("searchTerm") String searchTerm) {
		try {
			return ResponseEntity.ok(userService.search(searchTerm));
			
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/send-friend-request/{recipientId}")
	public ResponseEntity<UserDTO> sendFriendRequest(@PathVariable String recipientId, @RequestHeader("Authorization") String authHeader) {
		try {
			userService.createFriendRequest(authHeader.replace("Bearer ", ""), recipientId);
			return ResponseEntity.ok(null);
				
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/friend-requests")
	public ResponseEntity<List<FriendRequestDTO>> getUserFriendRequests(@RequestHeader("Authorization") String authHeader) {
		try {
			List<FriendRequestDTO> userFriendRequests = userService.getUserFriendRequests(authHeader.replace("Bearer ", ""));
			if (userFriendRequests.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity.ok(userFriendRequests);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@PutMapping("/update-friend-request-status")
	public ResponseEntity<Boolean> updateFriendRequestStatus(@RequestBody UpdateFriendStatusRequest request) {
		try {
			return ResponseEntity.ok().body(userService.updateFriendRequestStatus(request.friendRequestId, request.status));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/friends")
	public ResponseEntity<List<FriendRequestDTO>> findUserFriends(@RequestHeader("Authorization") String authHeader) {
		try {
			List<FriendRequestDTO> friends = userService.findFriends(authHeader.replace("Bearer ", ""));
			if (friends.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			
			return ResponseEntity.ok(friends);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
