package gg.bayes.challenge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gg.bayes.challenge.pojo.HeroItemsEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;

/**
 * This interface used for fetching {@link HeroItemsEventEntity} object.
 * 
 * @author Vineet Pareek
 */
public interface HeroItemsRepository extends JpaRepository<HeroItemsEventEntity, Long> {

    /**
     * This is used to fetch all {@link HeroItemsEventEntity} with Join of the table
     * {@link HeroMatchEventEntity}.
     * 
     * @param heroName The Hero Name
     * @param matchId  this is used for matching ID for {@link HeroMatchEventEntity}
     *                 of match ID
     * @return {@link HeroItemsEventEntity}
     */
    @Query("SELECT hs FROM HeroItemsEventEntity hs, HeroMatchEventEntity hk WHERE hs.heroKills = hk AND hk.heroName=:heroName AND hk.matchId=:matchId")
    List<HeroItemsEventEntity> fetchHeroItemsEvent(@Param("heroName") String heroName, @Param("matchId") Long matchId);

}
