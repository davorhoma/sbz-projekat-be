package app.services;

import java.time.LocalDateTime;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import app.dtos.FeedPost;

@Service
public class DroolsService {

	private KieContainer kieContainer;

    public DroolsService() {
        KieServices ks = KieServices.Factory.get();
        kieContainer = ks.getKieClasspathContainer();
    }

    public void applyRulesForNewUser(List<FeedPost> feedPosts) {
        KieSession kieSession = kieContainer.newKieSession("NoFriendsSession");
        for (FeedPost fp : feedPosts) {
            kieSession.insert(fp);
        }
        
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        kieSession.insert(threshold);
        
        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
    public void applyRulesForUserWithFriends(List<FeedPost> feedPosts) {
        KieSession kieSession = kieContainer.newKieSession("WithFriendsSession");
        for (FeedPost fp : feedPosts) {
            kieSession.insert(fp);
        }
        
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        kieSession.insert(threshold);
        
        kieSession.fireAllRules();
        kieSession.dispose();
    }
}
