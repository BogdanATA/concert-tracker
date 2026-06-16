package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.model.Promoter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoterRepository extends JpaRepository<Promoter, Long> {
}
