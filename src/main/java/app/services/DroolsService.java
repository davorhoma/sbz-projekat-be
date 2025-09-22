package app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    public void applyRulesForUserWithFriends(List<FeedPost> feedPosts, List<Like> userLikes) {
        KieSession kieSession = kieContainer.newKieSession("WithFriendsSession");
        for (FeedPost fp : feedPosts) {
            kieSession.insert(fp);
        }
        
        userLikes.forEach(kieSession::insert);
        
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        kieSession.insert(threshold);
        
        kieSession.fireAllRules();
        kieSession.dispose();
    }
    
    public List<FeedPost> applyRulesForNewUser(List<FeedPost> feedPosts,
    		User currentUser,
    		List<User> allUsers, 
    		List<Post> allPosts,
    		List<Like> allLikes) {

        KieSession kieSession = kieContainer.newKieSession("NoFriendsSession");
        
        feedPosts.forEach(kieSession::insert);
        kieSession.setGlobal("loggedUser", currentUser);
        kieSession.setGlobal("allLikes", allLikes);
        kieSession.setGlobal("allPosts", allPosts);

        allUsers.forEach(kieSession::insert);
        allPosts.forEach(kieSession::insert);
        allLikes.forEach(kieSession::insert);
        
        feedPosts.forEach(fp -> fp.setScore(0));
        
        kieSession.fireAllRules();
        kieSession.dispose();
        
        feedPosts.forEach(fp -> fp.setVisible(false));
        feedPosts.sort(
        	    Comparator.comparingInt(FeedPost::getScore).reversed()
        	              .thenComparing(fp -> fp.getPost().createdAt,
        	                             Comparator.nullsLast(Comparator.reverseOrder()))
        	);
        
        feedPosts.stream()
	        .limit(20)
	        .forEach(fp -> fp.setVisible(true));        
        
        long visibleCount = feedPosts.stream()
        	    .filter(FeedPost::isVisible)
        	    .count();

    	System.out.println("Broj vidljivih postova: " + visibleCount);
    	System.out.print("Scores: ");
    	feedPosts.forEach(fp -> System.out.print(fp.getScore() + ", "));
    	
    	return feedPosts;
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
    
    public static boolean postsAreSimilar(Post postA, Post postB, List<Like> allLikes, double threshold) {
        Set<UUID> usersA = allLikes.stream()
                .filter(l -> l.getPost().equals(postA))
                .map(l -> l.getUser().getId())
                .collect(Collectors.toSet());

        Set<UUID> usersB = allLikes.stream()
                .filter(l -> l.getPost().equals(postB))
                .map(l -> l.getUser().getId())
                .collect(Collectors.toSet());

        if (usersA.isEmpty() || usersB.isEmpty()) return false;

        Set<UUID> intersection = new HashSet<>(usersA);
        intersection.retainAll(usersB);

        double overlap = (double) intersection.size() / (double) usersA.size();

        return overlap >= threshold;
    }
    
    public static boolean userLikedHashtagRecently(User user, List<String> hashtags, List<Like> allLikes, int minCount, int days) {
        LocalDateTime time = LocalDateTime.now().minusDays(days);
        
        long count = allLikes.stream()
            .filter(l -> l.getUser().getId() == user.getId() && l.getCreatedAt().isAfter(time))
            .map(l -> l.getPost())
            .filter(p -> p.getHashtags().stream().anyMatch(hashtags::contains))
            .count();

        System.out.println("Count " + count);
        return count >= minCount;
    }
    
    public static boolean userPostsContainHashtag(User user, List<String> hashtags, List<Post> allPosts, double threshold, int days) {
        LocalDateTime time = LocalDateTime.now().minusDays(days);

        List<Post> userPosts = allPosts.stream()
            .filter(p -> p.getUser().getId() == user.getId() && p.getCreatedAt().isAfter(time))
            .collect(Collectors.toList());

        if (userPosts.isEmpty()) return false;

        long count = userPosts.stream()
            .filter(p -> p.getHashtags().stream().anyMatch(hashtags::contains))
            .count();

        double ratio = (double) count / (double) userPosts.size();
        return ratio >= threshold;
    }

}
