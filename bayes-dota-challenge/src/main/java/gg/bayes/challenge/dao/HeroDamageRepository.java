package gg.bayes.challenge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;

/**
 * This interface used for fetching {@link HeroDamageEventEntity} object.
 * 
 * @author VineetPareek
 */
public interface HeroDamageRepository extends JpaRepository<HeroDamageEventEntity, Long> {

    /**
     * This is used to fetch all {@link HeroDamageEventEntity} with Join of the
     * table {@link HeroMatchEventEntity}.
     * 
     * @param heroName The Hero Name
     * @param matchId  this is used for matching ID for {@link HeroMatchEventEntity}
     *                 of match ID
     * @return {@link HeroDamageEventEntity}
     */
    @Query("SELECT hs FROM HeroDamageEventEntity hs, HeroMatchEventEntity hk WHERE hs.heroKills = hk AND hk.heroName=:heroName AND hk.matchId=:matchId")
    List<HeroDamageEventEntity> fetchHeroDamageEvent(@Param("heroName") String heroName,
            @Param("matchId") Long matchId);

}
