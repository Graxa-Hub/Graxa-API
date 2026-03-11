package com.Graxa_API.Graxa_API.Repository;

import com.Graxa_API.Graxa_API.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {}

