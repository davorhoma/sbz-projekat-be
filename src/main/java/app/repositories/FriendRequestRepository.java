package app.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.model.FriendRequest;
import app.model.RequestStatus;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID>{

	@Query("SELECT fr FROM FriendRequest fr WHERE "
			+ "(fr.sender.id = :senderId AND fr.recipient.id = :recipientId OR fr.sender.id = :recipientId AND fr.recipient.id = :senderId) AND "
			+ "(fr.status = 'PENDING' OR fr.status = 'ACCEPTED')")
	Optional<FriendRequest> findPendingOrAcceptedBySenderAndRecipient(@Param("senderId") UUID senderId, @Param("recipientId") UUID recipientId);
	
	List<FriendRequest> findAllByRecipient_IdAndStatus(UUID recipientId, RequestStatus status);
	
	@Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender.id = :userId OR fr.recipient.id = :userId) AND fr.status = 'ACCEPTED'")
	List<FriendRequest> findAllFriends(UUID userId);


}
