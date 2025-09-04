package app.dtos;

import java.util.UUID;

public class FriendRequestDTO {

	public UUID id;
	public UserDTO otherUser;
	public String status;
	
	public FriendRequestDTO() {}
	
	public FriendRequestDTO(UUID id, UserDTO otherUser, String status) {
		super();
		this.id = id;
		this.otherUser = otherUser;
		this.status = status;
	}
	
}
