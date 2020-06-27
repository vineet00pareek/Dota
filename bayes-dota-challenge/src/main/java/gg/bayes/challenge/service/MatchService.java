package gg.bayes.challenge.service;

import java.util.List;

import gg.bayes.challenge.exception.MatchServiceException;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

public interface MatchService {

    /**
     * This method used for insert events in to Database.
     * 
     * @param payload this is content of events
     * @return {@link Long}
     * @throws MatchServiceException
     */
    Long ingestMatch(String payload) throws MatchServiceException;

    /**
     * This method used to fetch all heroes and how many times he kills.
     * 
     * @param matchId It used for identified by the specific Match.
     * @return {@link HeroKills}
     * @throws MatchServiceException
     */
    List<HeroKills> getHeroKillsByMatchId(Long matchId) throws MatchServiceException;

    /**
     * This method used to fetch all Items and timestamp specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Items
     * @return {@link HeroItems}
     * @throws MatchServiceException
     */
    List<HeroItems> getItemsByMatchIdAndHeroName(Long matchId, String heroName) throws MatchServiceException;

    /**
     * This method used to fetch all Spells and casts specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Spells
     * @return {@link HeroSpells}
     * @throws MatchServiceException
     */
    List<HeroSpells> getHeroSpellsByMatchIdAndHeroName(Long matchId, String heroName) throws MatchServiceException;

    /**
     * This method used to fetch all damageHero, DamageInstance and TotalDamage
     * specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Damage
     * @return {@link HeroDamage}
     * @throws MatchServiceException
     */
    List<HeroDamage> getHeroDamageByMatchIdAndHeroName(Long matchId, String heroName) throws MatchServiceException;
}
