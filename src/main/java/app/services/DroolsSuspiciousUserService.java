package app.services;

import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import app.model.Block;
import app.model.Report;
import app.model.User;

@Service
public class DroolsSuspiciousUserService {

	private KieContainer kieContainer;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlockService blockService;
	
	@Autowired
	private UserRestrictionService restrictionService;
	
	public DroolsSuspiciousUserService() {
		KieServices ks = KieServices.Factory.get();
		kieContainer = ks.getKieClasspathContainer();
	}
	
	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	public void applyRules() {
		System.out.println("applying rules");
		KieSession kieSession = kieContainer.newKieSession("SuspiciousUsersSession");
		
		kieSession.setGlobal("restrictionService", restrictionService);
		
		List<Report> allReports = reportService.findAllWithPostAndUser();
		allReports.forEach(kieSession::insert);
		
		List<User> allUsers = userService.getAll();
		allUsers.forEach(kieSession::insert);
		
		List<Block> allBlocks = blockService.findAll();
		allBlocks.forEach(kieSession::insert);
		
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}
