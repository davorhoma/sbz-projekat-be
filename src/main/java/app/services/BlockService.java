package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dtos.BlockResponse;
import app.model.Block;
import app.model.User;
import app.repositories.BlockRepository;
import app.utils.JwtUtil;

@Service
public class BlockService {

	@Autowired
	private BlockRepository blockRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DroolsSuspiciousUserService droolsSuspiciousUserService;
	
	public BlockResponse blockUser(String token, String blockedUserIdStr) throws Exception {
	    UUID userId = jwtUtil.extractUserId(token);
	    UUID blockedUserId = UUID.fromString(blockedUserIdStr);

	    if (!userService.areUsersFriends(userId, blockedUserId)) {
	        return new BlockResponse(false, "Users are not friends");
	    }

	    if (blockRepository.findByBlocker_IdAndBlocked_Id(userId, blockedUserId).isPresent()) {
	        return new BlockResponse(false, "User is already blocked");
	    }

	    User blocker = userService.findById(userId);
	    User blocked = userService.findById(blockedUserId);
	    Block newBlock = new Block(blocker, blocked);
	    blockRepository.save(newBlock);

	    droolsSuspiciousUserService.applyRules();
	    
	    return new BlockResponse(true, "User blocked successfully");
	}

	public List<UUID> getBlockedUsers(String token) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
		
		return blockRepository.findBlockedUserIdsByBlockerId(userId);
	}

	public BlockResponse unblockUser(String token, String blockedUserIdStr) throws Exception {
		UUID userId = jwtUtil.extractUserId(token);
	    UUID blockedUserId = UUID.fromString(blockedUserIdStr);
	    
	    Optional<Block> block = blockRepository.findByBlocker_IdAndBlocked_Id(userId, blockedUserId);
	    if (!block.isPresent()) {
	        return new BlockResponse(false, "User is not blocked");
	    }
	    
	    blockRepository.delete(block.get());
	    
	    return new BlockResponse(true, "User unblocked successfully");
	}
	
	public List<Block> findAll() {
		return blockRepository.findAll();
	}
}
