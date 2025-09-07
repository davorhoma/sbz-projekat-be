package app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.RestrictionType;
import app.model.User;
import app.model.UserRestriction;
import app.repositories.UserRestrictionRepository;

@Service
public class UserRestrictionService {

	@Autowired
    private UserRestrictionRepository repository;

    public void addRestriction(User user, RestrictionType type, int days) {
    	LocalDateTime now = LocalDateTime.now();
    	
    	List<UserRestriction> active = repository.findActiveRestrictions(user, type, now);
        if (!active.isEmpty()) {
            System.out.println("Restriction already exists for user " + user.getEmail() + " and type " + type);
            
//            UserRestriction existing = active.get(0);
//            if (existing.getType().equals(RestrictionType.LOGIN)
//            		&& existing.getValidUntil() == null) {
//            	return;
//            }
//            
//            existing.setValidUntil(existing.getValidUntil().plusDays(days));
//            repository.save(existing);
            return;
        }
        
        LocalDateTime until;
        if (days == -1) {
        	until = null;
        } else {
        	until = now.plusDays(days);
        }
        
        UserRestriction restriction = new UserRestriction(user, type, until);
        repository.save(restriction);
        
        System.out.println("User " + user.getEmail() + " " + type.toString() + " restricted " + until.toString());
    }
    
    public boolean isUserLoginRestricted(User user) {
    	LocalDateTime now = LocalDateTime.now();
        List<UserRestriction> active = repository.findActiveRestrictions(user, RestrictionType.LOGIN, now);
        return !active.isEmpty();
    }
    
    public boolean isUserPostRestricted(User user) {
    	LocalDateTime now = LocalDateTime.now();
        List<UserRestriction> active = repository.findActiveRestrictions(user, RestrictionType.POST, now);
        return !active.isEmpty();
    }
    
    public boolean isUserLikeRestricted(User user) {
    	LocalDateTime now = LocalDateTime.now();
        List<UserRestriction> active = repository.findActiveRestrictions(user, RestrictionType.LIKE, now);
        return !active.isEmpty();
    }
}
