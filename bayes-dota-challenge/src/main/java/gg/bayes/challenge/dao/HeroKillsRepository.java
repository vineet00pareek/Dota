package gg.bayes.challenge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.bayes.challenge.pojo.HeroMatchEventEntity;

/**
 * This interface used for fetching {@link HeroMatchEventEntity} object.
 * 
 * @author VineetPareek
 */
public interface HeroKillsRepository extends JpaRepository<HeroMatchEventEntity, Long> {

    /**
     * It's used to fetch all hero which is matching with {@value matchId}
     * 
     * @param matchId this is used for matching ID for fetching of all Heroes
     * @return {@link HeroMatchEventEntity}
     */
    List<HeroMatchEventEntity> findByMatchId(Long matchId);
}
