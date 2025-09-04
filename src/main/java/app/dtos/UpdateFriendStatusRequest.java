package app.dtos;

import java.util.UUID;

public class UpdateFriendStatusRequest {

	public UUID friendRequestId;
	public String status;
	
	public UpdateFriendStatusRequest() {}
	
	public UpdateFriendStatusRequest(UUID friendRequestId, String status) {
		super();
		this.friendRequestId = friendRequestId;
		this.status = status;
	}
	
}
