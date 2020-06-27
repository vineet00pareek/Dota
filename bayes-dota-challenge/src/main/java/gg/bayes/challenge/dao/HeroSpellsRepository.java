package gg.bayes.challenge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.pojo.HeroSpellsEventEntity;

/**
 * This interface used for fetching {@link HeroSpellsEventEntity} object.
 * 
 * @author VineetPareek
 */
public interface HeroSpellsRepository extends JpaRepository<HeroSpellsEventEntity, Long> {

    /**
     * This is used to fetch all {@link HeroSpellsEventEntity} with Join of the
     * table {@link HeroMatchEventEntity}.
     * 
     * @param heroName The Hero Name
     * @param matchId  this is used for matching ID for {@link HeroMatchEventEntity}
     *                 of match ID
     * @return {@link HeroSpellsEventEntity}
     */
    @Query("SELECT hs FROM HeroSpellsEventEntity hs, HeroMatchEventEntity hk WHERE hs.heroKills = hk AND hk.heroName=:heroName AND hk.matchId=:matchId")
    List<HeroSpellsEventEntity> fetchHeroSpellsEvent(@Param("heroName") String heroName,
            @Param("matchId") Long matchId);

}
