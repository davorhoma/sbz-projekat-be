package app.mappers;

import java.util.UUID;

import app.dtos.FriendRequestDTO;
import app.model.FriendRequest;
import app.model.User;

public class FriendRequestMapper {

	public static FriendRequestDTO toDto(FriendRequest friendRequest) {
		if (friendRequest == null) return null;
		
		FriendRequestDTO dto = new FriendRequestDTO(
				friendRequest.getId(), 
				UserMapper.toDto(friendRequest.getSender()),
				friendRequest.getStatus().toString());
		
		return dto;
	}
	
	public static FriendRequestDTO toDtoForCurrentUser(FriendRequest friendRequest, UUID currentUserId) {
	    if (friendRequest == null) return null;

	    User otherUser;
	    if (friendRequest.getSender().getId().equals(currentUserId)) {
	        otherUser = friendRequest.getRecipient();
	    } else {
	        otherUser = friendRequest.getSender();
	    }

	    FriendRequestDTO dto = new FriendRequestDTO(
	        friendRequest.getId(),
	        UserMapper.toDto(otherUser),
	        friendRequest.getStatus().toString()
	    );

	    return dto;
	}

}
