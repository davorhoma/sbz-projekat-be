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

    private UserRestrictionRepository repository;
	
	@Autowired
    public UserRestrictionService(UserRestrictionRepository repository) {
        this.repository = repository;
    }

    public void addRestriction(User user, RestrictionType type, int days) {
    	LocalDateTime now = LocalDateTime.now();
    	
    	List<UserRestriction> active = repository.findActiveRestrictions(user, type, now);
        if (!active.isEmpty()) {
        	for (UserRestriction r : active) {
                if (r.getValidUntil() == null) {
                    System.out.println("Permanent restriction already exists for user " + user.getEmail() + " and type " + type);
                    return;
                }
            }
            // Ako se dodaje permanentna restrikcija, zameni postojeću privremenu
            if (days == -1) {
                for (UserRestriction r : active) {
                    repository.delete(r); // izbriši privremenu restrikciju
                }
            } else {
                // Eventualno produži postojeću privremenu restrikciju
                for (UserRestriction r : active) {
                    r.setValidUntil(r.getValidUntil().plusDays(days));
                    repository.save(r);
                }
                return;
            }
        }
        
        LocalDateTime until;
        if (days == -1) {
        	until = null;
        } else {
        	until = now.plusDays(days);
        }
        
        UserRestriction restriction = new UserRestriction(user, type, until);
        repository.save(restriction);
        
        if (until == null) {
        	System.out.println("User " + user.getEmail() + " " + type.toString() + " restricted permanently");        	
        } else {
        	System.out.println("User " + user.getEmail() + " " + type.toString() + " restricted until " + until.toString());        	
        }
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
