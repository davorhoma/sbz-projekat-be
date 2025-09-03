package app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Report;

public interface ReportRepository extends JpaRepository<Report, UUID>{

}
