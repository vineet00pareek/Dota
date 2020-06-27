package gg.bayes.challenge.service.bf;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gg.bayes.challenge.exception.MatchServiceException;
import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.utils.Event;
import gg.bayes.challenge.utils.MatchConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * This class used to Read and write hero details and events from match logs
 * 
 * @author VineetPareek
 *
 */
@Slf4j
@Component
public class EventReaderWriterFunction {

    /**
     * The EventBusinessFunction
     */
    @Autowired
    private EventBusinessFunction eventBusinessService;

    /**
     * It is hold for list of unique heroes with his total kills count.
     */
    private Set<HeroMatchEventEntity> heroMatchEventEntities = new HashSet<HeroMatchEventEntity>();

    /**
     * It's hold for list of unique heroes
     */
    private Set<String> heroes = new HashSet<>();

    /**
     * It's holds key as hero and value is total killed by count.
     */
    private Map<String, Integer> heroKillsMap = new HashMap<>();

    /**
     * It's holds key as hero spells and value is total number of he spells for
     * casts.
     */
    private Map<String, Integer> spellMap = new HashMap<>();

    /**
     * It's holds key as hero and value is list of damage of heroes he damaged.
     */
    private Map<String, List<HeroDamageEventEntity>> heroDamageEntitiesMap = new HashMap<>();

    /**
     * This method used for reading the match log file and captured the events and
     * storing in a structure way in HeroEntity Object.
     * 
     * @param matchLog this is match event combat logs.
     * @param matchId  this is match Id for match.
     * @return {@link HeroMatchEventEntity}
     * @throws MatchServiceException
     */
    public Set<HeroMatchEventEntity> readMatchLogs(String matchLog, Long matchId) throws MatchServiceException {
        log.debug("Start reading match log file");
        eventReading(matchLog, matchId);

        heroMatchEventEntities.stream().forEach(heroEntity -> {
            heroEntity.setKills(heroKillsMap.get(heroEntity.getHeroName()));
            heroEntity = eventBusinessService.addEntityForSpell(heroEntity, spellMap);
            heroEntity = eventBusinessService.addEntityForDamage(heroEntity, heroDamageEntitiesMap);
        });
        log.info("successfully added Unique hero in set");
        return heroMatchEventEntities;
    }

    /**
     * This method is used to read all content and apply events business logic to do
     * in structure data.
     * 
     * @param matchLog this is match event combat logs.
     * @param matchId  this is match Id for match.
     * @throws MatchServiceException
     */
    private void eventReading(String matchLog, Long matchId) throws MatchServiceException {
        log.debug("Start processeing on match log and store into HeroEntity and events");
        try (BufferedReader reader = new BufferedReader(new StringReader(matchLog))) {
            String eventlogs = reader.readLine();
            while (eventlogs != null) {
                eventlogs = reader.readLine();
                if (StringUtils.isNoneEmpty(eventlogs) && !eventlogs.contains(Event.USES.getEvent())
                        && !eventlogs.contains(MatchConstant.GAME)) {
                    addHeros(eventlogs, matchId);
                    writeEvents(eventlogs);
                }
            }
        } catch (Exception exc) {
            log.error("Getting exception while reading Events from match logs", exc);
            throw new MatchServiceException("Getting exception while reading Events from match logs", exc.getMessage(),
                    exc);
        }
    }

    public void clean() {
        log.info("Instance variable cleaning process start...");
        spellMap.clear();
        heroDamageEntitiesMap.clear();
        heroKillsMap.clear();
        heroes.clear();
        heroMatchEventEntities.clear();
    }

    /**
     * This method is check event with start hero name as {@link NPC_DOTA_HERO} and
     * add it in {@link Set} interface for counting unique hero in this Match, and
     * also manipulate {@link HeroMatchEventEntity}.
     * 
     * @param line    this is casts event line
     * @param matchId it is match Id of Match
     */
    public void addHeros(String eventlogs, long matchId) {
        log.debug("start fetching Heros from log and store into sets");
        StringTokenizer tokens = new StringTokenizer(eventlogs);
        while (tokens.hasMoreElements()) {
            String token = tokens.nextToken();
            if (token.startsWith(MatchConstant.NPC_DOTA_HERO)) {
                String hero = token.substring(MatchConstant.NPC_DOTA_HERO.length());
                if (!hero.contains(MatchConstant.SYMBOL_S)) {
                    if (heroes.add(hero)) {
                        createNewHero(matchId, hero);
                        break;
                    }
                }
            }
        }
    }

    /**
     * THis method is used to set Hero into Hero list.
     * 
     * @param matchId
     * @param hero
     */
    private void createNewHero(long matchId, String hero) {
        log.debug("Extract method for Hero entity");
        HeroMatchEventEntity heroKillsEntity = new HeroMatchEventEntity();
        heroKillsEntity.setHeroName(hero);
        heroKillsEntity.setMatchId(matchId);
        heroMatchEventEntities.add(heroKillsEntity);

    }

    /**
     * This method is used for checking events and store all into collection to
     * control this Match.
     * 
     * @param event It is one event
     */
    public void writeEvents(String eventlogs) {
        log.debug("start fetching individual match Events from log and store");
        if (eventlogs.contains(Event.CASTS.getEvent())) {
            eventBusinessService.addSpellForHeroMap(eventlogs, heroMatchEventEntities, spellMap);
        } else if (eventlogs.contains(Event.BUYS.getEvent())) {
            eventBusinessService.addItemsForHero(eventlogs, heroMatchEventEntities);
        } else if (eventlogs.contains(Event.HITS.getEvent())) {
            eventBusinessService.addDamgeByHitsHero(eventlogs, heroDamageEntitiesMap);
        } else if (eventlogs.contains(Event.KILLED.getEvent())) {
            countHeroKills(eventlogs);
        }
    }

    /**
     * This method used to count hero kill in match based on match logs
     * 
     * @param eventlogs
     */
    private void countHeroKills(String eventlogs) {
        String[] killedEvent = eventlogs.split(Event.KILLED.getEvent());
        String killedByHero = killedEvent[1];
        for (String heroName : heroes) {
            if (killedByHero.contains(MatchConstant.NPC_DOTA_HERO + heroName)) {
                if (heroKillsMap.containsKey(heroName)) {
                    heroKillsMap.put(heroName, heroKillsMap.get(heroName) + 1);
                } else {
                    heroKillsMap.put(heroName, 1);
                }
                break;
            }
        }
    }
}
