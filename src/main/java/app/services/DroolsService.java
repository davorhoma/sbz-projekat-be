package app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import app.dtos.FeedPost;
import app.dtos.UserDTO;
import app.model.Like;
import app.model.Post;
import app.model.User;

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
    
    public void applyRulesForUserWithFriends(List<FeedPost> feedPosts,
    		User currentUser,
    		List<User> allUsers, 
    		List<Post> allPosts,
    		List<Like> allLikes) {
    	
        KieSession kieSession = kieContainer.newKieSession("WithFriendsSession");
        
        feedPosts.forEach(kieSession::insert);
        kieSession.setGlobal("loggedUser", currentUser);
        kieSession.setGlobal("allLikes", allLikes);

        allUsers.forEach(kieSession::insert);
        allPosts.forEach(kieSession::insert);
        allLikes.forEach(kieSession::insert);
        
        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
    public static double pearsonSimilarity(User a, User b, List<Like> allLikes) {
    	System.out.println("Pearson similarity");
        // 1. Nađi zajedničke postove
        List<Post> commonPosts = allLikes.stream()
            .filter(l -> l.getUser().getId().equals(a.getId()) || l.getUser().getId().equals(b.getId()))
            .map(Like::getPost)
            .distinct()
            .collect(Collectors.toList());

        // 2. Za svaki post, vrednost je 1 ako korisnik lajkovao, 0 ako nije
        List<Integer> aRatings = new ArrayList<>();
        List<Integer> bRatings = new ArrayList<>();

        for (Post p : commonPosts) {
            aRatings.add(allLikes.stream().anyMatch(l -> l.getUser().getId().equals(a.getId()) && l.getPost().getId().equals(p.getId())) ? 1 : 0);
            bRatings.add(allLikes.stream().anyMatch(l -> l.getUser().getId().equals(b.getId()) && l.getPost().getId().equals(p.getId())) ? 1 : 0);
        }

        System.out.println("aRatings.size(): " + aRatings.size());
        System.out.println("bRatings.size(): " + bRatings.size());
        // 3. Izračunaj prosečne ocene
        double meanA = aRatings.stream().mapToInt(Integer::intValue).average().orElse(0);
        double meanB = bRatings.stream().mapToInt(Integer::intValue).average().orElse(0);

        if (meanA == 1) meanA = 0;
        if (meanB == 1) meanB = 0;
        meanA = 0;
        meanB = 0;
        
        System.out.println("meanA: " + meanA + "meanB: " + meanB);
        // 4. Pearsonova formula
        double numerator = 0;
        double denomA = 0;
        double denomB = 0;

        for (int i = 0; i < commonPosts.size(); i++) {
            double devA = aRatings.get(i) - meanA;
            double devB = bRatings.get(i) - meanB;
            numerator += devA * devB;
            denomA += devA * devA;
            denomB += devB * devB;
        }

        System.out.println("denomA: " + denomA + ", denomB: " + denomB);
        double denominator = Math.sqrt(denomA) * Math.sqrt(denomB);
        
        System.out.println("Numerator" + numerator + "Denominator" + denominator);
        if (denominator == 0) return 0;
        System.out.println("Numerator/Denominator" + numerator/denominator);
        return numerator / denominator;
    }
}
