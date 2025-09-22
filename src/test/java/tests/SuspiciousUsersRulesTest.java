package tests;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.model.User;
import app.repositories.InMemoryUserRestrictionRepository;
import app.model.Report;
import app.model.Block;
import app.model.Post;
import app.services.UserRestrictionService;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class SuspiciousUsersRulesTest {

	private KieSession kieSession;
    private UserRestrictionService restrictionService;
    private User testUser;

    @BeforeEach
    void setup() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        kieSession = kc.newKieSession("SuspiciousUsersSession");
        
        InMemoryUserRestrictionRepository repo = new InMemoryUserRestrictionRepository();
        restrictionService = new UserRestrictionService(repo);
        
        kieSession.setGlobal("restrictionService", restrictionService);

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("test@example.com");

        kieSession.insert(testUser);
    }
    
    // --- Report tests ---
    @Test
    void testPostReportedExactly5Times24h_NoRestriction() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            Report r = new Report();
            r.setCreationTime(now.minusHours(1));
            Post p = new Post();
            p.setUser(testUser);
            r.setPost(p);
            kieSession.insert(r);
        }
        kieSession.fireAllRules();
        assertFalse(restrictionService.isUserPostRestricted(testUser),
            "User should NOT be restricted for exactly 5 reports in 24h.");
    }
    
    @Test
    void testPostReported6TimesIn24h() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 6; i++) {
            Report r = new Report();
            r.setCreationTime(now.minusHours(1));
            Post testPost = new Post();
            testPost.setUser(testUser);
            r.setPost(testPost);
            kieSession.insert(r);
        }

        kieSession.fireAllRules();

        assertTrue(restrictionService.isUserPostRestricted(testUser),
            "User should be restricted from posting for 1 day.");
    }
    
    
    @Test
    void testPostReported9TimesIn48h() {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 9; i++) {
            Report r = new Report();
            r.setCreationTime(now.minusHours(10));
            Post testPost = new Post();
            testPost.setUser(testUser);
            r.setPost(testPost);
            kieSession.insert(r);
        }

        kieSession.fireAllRules();

        assertTrue(restrictionService.isUserPostRestricted(testUser),
            "User should be restricted from posting for 2 days.");
    }
    
    
    // --- Block tests ---
    void testUserBlockedExactly4Times24h_NoRestriction() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 4; i++) {
            Block b = new Block();
            b.setBlocked(testUser);
            b.setBlockedAt(now.minusHours(2));
            kieSession.insert(b);
        }
        kieSession.fireAllRules();
        assertFalse(restrictionService.isUserPostRestricted(testUser),
            "User should NOT be restricted for exactly 4 blocks in 24h.");
    }
    
    @Test
    void testUserBlocked5TimesIn24h() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            Block b = new Block();
            b.setBlocked(testUser);
            b.setBlockedAt(now.minusHours(2));
            kieSession.insert(b);
        }

        kieSession.fireAllRules();

        assertTrue(restrictionService.isUserPostRestricted(testUser),
            "User should be restricted from posting for 1 day due to blocks.");
    }
    
    // --- Combination: block and report tests ---
    @Test
    void testBlocked3AndReported5Times_LoginRestriction() {
        LocalDateTime now = LocalDateTime.now();

        // 3 block-a u poslednja 2 dana
        for (int i = 0; i < 3; i++) {
            Block b = new Block();
            b.setBlocked(testUser);
            b.setBlockedAt(now.minusHours(30));
            kieSession.insert(b);
        }

        // 5 report-a u poslednjih 1 dan
        for (int i = 0; i < 5; i++) {
            Report r = new Report();
            r.setCreationTime(now.minusHours(5));
            Post testPost = new Post();
            testPost.setUser(testUser);
            r.setPost(testPost);
            kieSession.insert(r);
        }

        kieSession.fireAllRules();

        assertTrue(restrictionService.isUserLoginRestricted(testUser),
            "User should be login restricted for 2 days.");
    }

    // --- Permanent login restriction test ---
    @Test
    void testPermanentLoginRestriction() {
        LocalDateTime now = LocalDateTime.now();
        // 11 block-a u poslednja 2 dana
        for (int i = 0; i < 11; i++) {
            Block b = new Block();
            b.setBlocked(testUser);
            b.setBlockedAt(now.minusHours(20));
            kieSession.insert(b);
        }
        // 11 report-a u poslednja 2 dana
        for (int i = 0; i < 11; i++) {
            Report r = new Report();
            r.setCreationTime(now.minusHours(20));
            Post p = new Post();
            p.setUser(testUser);
            r.setPost(p);
            kieSession.insert(r);
        }
        kieSession.fireAllRules();
        assertTrue(restrictionService.isUserLoginRestricted(testUser),
            "User should be permanently login restricted.");
    }

    // --- Likes restriction ---
    @Test
    void testLikesRestriction2Days() {
        LocalDateTime now = LocalDateTime.now();
        // 16 block-a u poslednjih 7 dana
        for (int i = 0; i < 16; i++) {
            Block b = new Block();
            b.setBlocked(testUser);
            b.setBlockedAt(now.minusDays(3));
            kieSession.insert(b);
        }
        kieSession.fireAllRules();
        assertTrue(restrictionService.isUserLikeRestricted(testUser),
            "User should be restricted from liking posts for 2 days.");
    }

}
