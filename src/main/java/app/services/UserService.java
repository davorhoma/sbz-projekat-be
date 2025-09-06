package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.FriendRequestDTO;
import app.dtos.UserDTO;
import app.mappers.FriendRequestMapper;
import app.mappers.UserMapper;
import app.model.FriendRequest;
import app.model.RequestStatus;
import app.model.User;
import app.repositories.FriendRequestRepository;
import app.repositories.UserRepository;
import app.utils.JwtUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public User findById(UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new Exception("User does not exist");
		}
		
		return user.get();
	}

	public List<UserDTO> search(String searchTerm) {
        List<User> users;
        if (searchTerm == null || searchTerm.isEmpty()) {
            return null;
        } else {
            users = userRepository.search(searchTerm);
        }

        return users.stream()
                    .map(user -> UserMapper.toDto(user))
                    .collect(Collectors.toList());
    }
	
	public boolean createFriendRequest(String token, String recipientIdStr) throws Exception {
		try {
			UUID userId = jwtUtil.extractUserId(token);
			UUID receiverId = UUID.fromString(recipientIdStr);
			
			if (userId.equals(receiverId)) {
				throw new Exception("You cannot send a friend request to yourself");
			}
			
			findById(userId);
			FriendRequest newFriendRequest = new FriendRequest(findById(userId), findById(receiverId));
			
			Optional<FriendRequest> optFriendRequest = friendRequestRepository.findPendingOrAcceptedBySenderAndRecipient(userId, receiverId);
			if (optFriendRequest.isPresent()) {
				throw new Exception("Friend request already sent");
			}
			
			friendRequestRepository.save(newFriendRequest);
			return true;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public List<FriendRequestDTO> getUserFriendRequests(String token) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		
		List<FriendRequestDTO> pendingUserFriendRequests = friendRequestRepository.findAllByRecipient_IdAndStatus(userId, RequestStatus.PENDING)
				.stream()
				.map(fr -> FriendRequestMapper.toDto(fr))
				.collect(Collectors.toList());
		
		return pendingUserFriendRequests;
	}

	public boolean updateFriendRequestStatus(UUID requestId, String status) {
		return friendRequestRepository.findById(requestId)
		        .map(request -> {
		            if (request.getStatus() != RequestStatus.PENDING) {
		                return false;
		            }
		            try {
		                RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
		                request.setStatus(newStatus);
		                friendRequestRepository.save(request);
		                return true;
		            } catch (IllegalArgumentException e) {
		                // invalid status string
		                return false;
		            }
		        })
		        .orElse(false);
	}
	
	public List<FriendRequestDTO> findFriends(String token) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		
		List<FriendRequestDTO> acceptedUserFriendRequests = friendRequestRepository.findAllFriends(userId)
				.stream()
				.map(fr -> FriendRequestMapper.toDtoForCurrentUser(fr, userId))
				.collect(Collectors.toList());
		
		return acceptedUserFriendRequests;
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}

	public boolean areUsersFriends(UUID userId, UUID blockedUserId) {
		return friendRequestRepository.areUsersFriends(userId, blockedUserId);
	}
}
