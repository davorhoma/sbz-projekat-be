package app.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.model.Report;

public interface ReportRepository extends JpaRepository<Report, UUID>{

	@Query("SELECT r FROM Report r JOIN FETCH r.post p JOIN FETCH p.user")
	List<Report> findAllWithPostAndUser();

}
