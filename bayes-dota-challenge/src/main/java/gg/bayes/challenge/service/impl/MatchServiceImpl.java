package gg.bayes.challenge.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.bayes.challenge.dao.HeroDamageRepository;
import gg.bayes.challenge.dao.HeroItemsRepository;
import gg.bayes.challenge.dao.HeroKillsRepository;
import gg.bayes.challenge.dao.HeroSpellsRepository;
import gg.bayes.challenge.exception.MatchServiceException;
import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroItemsEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.pojo.HeroSpellsEventEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.service.bf.EventReaderWriterFunction;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to fetch all hero and event associate with it and store
 * into database.
 * 
 * @author VineetPareek
 *
 */
@Slf4j
@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    /**
     * It is counting for match.
     */
    private static Long matchId = 1L;

    /**
     * Initialization for this Object
     */
    @Autowired
    public MatchServiceImpl() {
    }

    /**
     * This is used to connect database to fetch HeroEntity
     */
    @Autowired
    private HeroKillsRepository heroRepository;

    @Autowired
    private HeroSpellsRepository heroSpellsRepository;

    @Autowired
    private HeroItemsRepository heroItemsRepository;

    @Autowired
    private HeroDamageRepository heroDamageRepository;

    /**
     * This is service class for reading match file and manipulation events.
     */
    @Autowired
    private EventReaderWriterFunction eventService;

    /**
     * This method used for insert events in to Database.
     * 
     * @param payload this is content of events
     * @return {@link Long}
     * @throws MatchServiceException
     */
    @Override
    public Long ingestMatch(String payload) throws MatchServiceException {
        log.debug("MatchServiceImpl: Method call for checking individual events and insert match events into database");
        Set<HeroMatchEventEntity> heroEntities = eventService.readMatchLogs(payload, matchId);

        try {

            heroEntities = new HashSet<HeroMatchEventEntity>(heroRepository.saveAll(heroEntities));
            heroEntities.forEach(hero -> {
                matchId = hero.getMatchId();
                return;
            });
            log.info("successfully reading the data from match logs and store into database Match ID: " + matchId);
        } catch (Exception e) {
            throw new MatchServiceException(
                    "Getting exception for finding Heros in the match with match ID : " + matchId, e.getMessage(), e);
        }
        eventService.clean();
        return matchId;
    }

    /**
     * This method used to fetch all heroes and how many times he kills.
     * 
     * @param matchId It used for identified by the specific Match.
     * @return {@link HeroKills}
     * @throws MatchServiceException
     */
    @Override
    public List<HeroKills> getHeroKillsByMatchId(Long matchId) throws MatchServiceException {
        log.debug("Fetching Hero kills event from database using match ID");
        List<HeroKills> heroKills = null;
        try {
            List<HeroMatchEventEntity> listOfHero = heroRepository.findByMatchId(matchId);
            heroKills = listOfHero.stream().map(list -> new HeroKills(list.getHeroName(), list.getKills()))
                    .collect(Collectors.toList());
            log.info("Successfully fetched the hero kills event from database using match ID : "
                    + listOfHero);
        } catch (Exception e) {
            throw new MatchServiceException(
                    "Getting exception for finding Heros in the match with match ID : " + matchId, e.getMessage(), e);
        }
        return heroKills;
    }

    /**
     * This method used to fetch all Items and time stamp specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Items
     * @return {@link HeroItems}
     * @throws MatchServiceException
     */
    @Override
    public List<HeroItems> getItemsByMatchIdAndHeroName(Long matchId, String heroName) throws MatchServiceException {
        log.debug("Fetching Hero Items event from database using match ID and Hero Name");
        List<HeroItems> heroItems = null;
        try {
            List<HeroItemsEventEntity> itemsEntity = heroItemsRepository.fetchHeroItemsEvent(heroName, matchId);
            heroItems = itemsEntity.stream().map(item -> new HeroItems(item.getItem(), item.getTime()))
                    .collect(Collectors.toList());
            log.info("Successfully fetched the Items event from database using match ID : " + matchId
                    + " and Hero name : " + heroName + " is : " + heroItems);
        } catch (Exception e) {
            throw new MatchServiceException("Getting exception for finding Items event with match ID : " + matchId
                    + " and Hero Name : " + heroName, e.getMessage(), e);
        }
        return heroItems;
    }

    /**
     * This method used to fetch all Spells and casts specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Spells
     * @return {@link HeroSpells}
     */
    @Override
    public List<HeroSpells> getHeroSpellsByMatchIdAndHeroName(Long matchId, String heroName)
            throws MatchServiceException {
        log.debug("Fetching Hero Spells event from database using match ID and Hero Name");
        List<HeroSpells> heroSpells = null;
        try {
            List<HeroSpellsEventEntity> spellsEntity = heroSpellsRepository.fetchHeroSpellsEvent(heroName, matchId);
            heroSpells = spellsEntity.stream().map(spell -> new HeroSpells(spell.getSpellName(), spell.getCaste()))
                    .collect(Collectors.toList());
            log.info("Successfully fetched the Spells event from database using match ID : " + matchId
                    + " and Hero name : " + heroName + " is : " + heroSpells);
        } catch (Exception e) {
            throw new MatchServiceException("Getting exception for finding Spell event with match ID : " + matchId
                    + " and Hero Name : " + heroName, e.getMessage(), e);
        }
        return heroSpells;
    }

    /**
     * This method used to fetch all damageHero, DamageInstance and TotalDamage
     * specified by hero.
     * 
     * @param matchId  It used for identified by the specific Match.
     * @param heroName specified hero to fetch Damage
     * @return {@link HeroDamage}
     * @throws MatchServiceException
     */
    @Override
    public List<HeroDamage> getHeroDamageByMatchIdAndHeroName(Long matchId, String heroName)
            throws MatchServiceException {
        log.debug("Fetching Hero Damage event from database using match ID and Hero Name");
        List<HeroDamage> heroDamageList = null;
        try {
            List<HeroMatchEventEntity> listOfHero = heroRepository.findByMatchId(matchId);
            List<HeroDamageEventEntity> damageEntities = heroDamageRepository.fetchHeroDamageEvent(heroName, matchId);
            heroDamageList = damageEntities.stream().map(
                    damageEntity -> new HeroDamage(damageEntity.getTargetHero(), damageEntity.getDamage_instance()))
                    .collect(Collectors.toList());
            for (HeroMatchEventEntity heroEntity : listOfHero) {
                if (!Objects.equals(heroEntity.getHeroName(), heroName)) {
                    for (HeroDamage heroDamage : heroDamageList) {
                        if (heroDamage.getTarget().equals(heroEntity.getHeroName())) {
                            heroDamage.setTotalDamage(heroEntity.getTotal_damages());
                        }
                    }
                }
            }
            log.info("Successfully fetched the Damage event from database using match ID : " + matchId
                    + " and Hero name : " + heroName + " is : " + heroDamageList);
        } catch (Exception e) {
            throw new MatchServiceException("Getting exception for finding damage event with match ID : " + matchId
                    + " and Hero Name : " + heroName, e.getMessage(), e);
        }
        return heroDamageList;
    }
}
