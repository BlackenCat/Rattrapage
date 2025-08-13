package com.rattrapage.repository;

import com.rattrapage.model.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, Long> {
    boolean existsByTitle(String title);
}
