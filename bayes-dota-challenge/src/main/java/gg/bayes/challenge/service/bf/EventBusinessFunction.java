package gg.bayes.challenge.service.bf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroItemsEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.pojo.HeroSpellsEventEntity;
import gg.bayes.challenge.utils.Event;
import gg.bayes.challenge.utils.MatchConstant;
import gg.bayes.challenge.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * This class used to write all business logic to handle and manipulate all
 * match events and Heros details
 * 
 * @author VineetPareek
 *
 */
@Slf4j
@Component
public class EventBusinessFunction {

    @Autowired
    EventReaderWriterFunction eventServiceImpl;

    /**
     * It is calculating {@link Event.CASTS} logics to manipulate spells hero.
     * 
     * @param eventlogs this is casts event logs
     * @return
     */
    public Map<String, Integer> addSpellForHeroMap(String eventlogs, Set<HeroMatchEventEntity> heroKillsEntities,
            Map<String, Integer> spellMap) {
        log.debug("Start Spell event fetching from match logs");
        for (HeroMatchEventEntity heroEntity : heroKillsEntities) {
            if (eventlogs.contains(MatchConstant.NPC_DOTA_HERO + heroEntity.getHeroName())) {
                StringTokenizer tokenizer = new StringTokenizer(eventlogs);
                while (tokenizer.hasMoreElements()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith(heroEntity.getHeroName() + MatchConstant.HYPHEN)) {
                        if (spellMap.containsKey(token)) {
                            spellMap.put(token, spellMap.get(token) + 1);
                        } else {
                            spellMap.put(token, 1);
                        }
                        log.info("Hero Name : " + heroEntity.getHeroName() + "with Spell Event" + spellMap.get(token));
                        break;
                    }
                }
            }
        }
        return spellMap;
    }

    /**
     * It is calculating for {@link Event.ITEMS} logics to manipulate items for
     * hero.
     * 
     * @param eventlogs this is casts event logs
     */
    public Set<HeroMatchEventEntity> addItemsForHero(String eventlogs, Set<HeroMatchEventEntity> heroKillsEntities) {
        log.debug("Start Items event fetching from match logs");
        for (HeroMatchEventEntity heroEntity : heroKillsEntities) {
            if (eventlogs.contains(MatchConstant.NPC_DOTA_HERO + heroEntity.getHeroName())) {
                HeroItemsEventEntity itemsEntity = new HeroItemsEventEntity();
                StringTokenizer tokenizer = new StringTokenizer(eventlogs);
                while (tokenizer.hasMoreElements()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith(MatchConstant.BRACKETS)) {
                        itemsEntity.setTime(TimeUtil.getTimeStamp(token));
                    } else if (token.startsWith(MatchConstant.ITEMS)) {
                        String item = token.substring(MatchConstant.ITEMS.length());
                        itemsEntity.setItem(item);
                    }
                }
                heroEntity.getHeroIteamCollection().add(itemsEntity);
                itemsEntity.setHeroKills(heroEntity);
                log.info("Hero Name : " + heroEntity.getHeroName() + "with Item Event");
                break;
            }
        }

        return heroKillsEntities;
    }

    /**
     * This method used for calculating damage of heros {@link Event.HITS} by
     * opponents
     * 
     * @param eventlogs this is damage event logs
     * @return
     */
    public Map<String, List<HeroDamageEventEntity>> addDamgeByHitsHero(String eventlogs,
            Map<String, List<HeroDamageEventEntity>> heroDamageEntitiesMap) {
        log.debug("Start Damage event fetching from match logs");
        String[] splits = eventlogs.split(MatchConstant.REG_EXP);
        String strikeBy = splits[1].substring(MatchConstant.NPC_DOTA_HERO.length());
        String strikeAgainst = splits[3].substring(MatchConstant.NPC_DOTA_HERO.length());
        Integer totalDamage = Integer.valueOf(splits[7]);
        if (heroDamageEntitiesMap.containsKey(strikeBy)) {
            List<HeroDamageEventEntity> damageEntities = heroDamageEntitiesMap.get(strikeBy);
            if (!exitsHittedByHero(damageEntities, strikeAgainst, totalDamage)) {
                HeroDamageEventEntity damageEntity = new HeroDamageEventEntity();
                damageEntity.setTargetHero(strikeAgainst);
                damageEntity.setDamage_instance(totalDamage);
                damageEntities.add(damageEntity);
            }
            heroDamageEntitiesMap.put(strikeBy, damageEntities);
        } else {
            List<HeroDamageEventEntity> damageEntities = new ArrayList<>();
            HeroDamageEventEntity damageEntity = new HeroDamageEventEntity();
            damageEntity.setTargetHero(strikeAgainst);
            damageEntity.setDamage_instance(totalDamage);
            damageEntities.add(damageEntity);
            heroDamageEntitiesMap.put(strikeBy, damageEntities);
        }

        return heroDamageEntitiesMap;
    }

    /**
     * It is checking condition is Hitted By hero exits or not if exits then return
     * true or return false
     * 
     * @param damageEntities list of damage entity
     * @param strikeAgainst  this hitted hero
     * @param damageCount    the total damaged count
     * @return {@link Boolean}
     */
    private boolean exitsHittedByHero(List<HeroDamageEventEntity> damageEntities, String strikeAgainst,
            Integer damageCount) {
        for (HeroDamageEventEntity damageEntity : damageEntities) {
            if (damageEntity.getTargetHero().equals(strikeAgainst)) {
                Integer damageInstance = damageEntity.getDamage_instance() + damageCount;
                damageEntity.setDamage_instance(damageInstance);
                return true;
            }
        }
        return false;
    }

    /**
     * It's manipulating {@link HeroSpellsEventEntity}
     * 
     * @param heroEntity {@link HeroMatchEventEntity}
     * @return
     */
    public HeroMatchEventEntity addEntityForSpell(HeroMatchEventEntity heroEntity, Map<String, Integer> spellMap) {
        List<HeroSpellsEventEntity> spellCollection = new ArrayList<>();
        spellMap.keySet().stream().filter(key -> key.startsWith(heroEntity.getHeroName())).forEach(key -> {
            HeroSpellsEventEntity spellsEntity = new HeroSpellsEventEntity();
            spellsEntity.setSpellName(key);
            spellsEntity.setCaste(spellMap.get(key));
            spellsEntity.setHeroKills(heroEntity);
            spellCollection.add(spellsEntity);
        });
        heroEntity.setHeroSpellsCollection(spellCollection);
        return heroEntity;
    }

    /**
     * This method used for manage relationship between {@link HeroMatchEventEntity}
     * to {@link HeroDamageEventEntity}
     * 
     * @param heroEntity holds heroEntity
     * @return
     */
    public HeroMatchEventEntity addEntityForDamage(HeroMatchEventEntity heroEntity,
            Map<String, List<HeroDamageEventEntity>> heroDamageEntitiesMap) {
        List<HeroDamageEventEntity> damageEntities = heroDamageEntitiesMap.get(heroEntity.getHeroName());
        heroEntity.setHeroDamageCollection(damageEntities);
        damageEntities.stream().forEach(damageEntity -> {
            damageEntity.setHeroKills(heroEntity);
        });
        return heroEntity;
    }

    /**
     * This method used for calculated total damage count of hero.
     * 
     * @param heroName holds Hero name to count his total damaged.
     * @return {@link Integer}
     */
    public int totalDamageCountforHero(String heroName,
            Map<String, List<HeroDamageEventEntity>> heroDamageEntitiesMap) {
        Map<String, List<HeroDamageEventEntity>> damageEntitiesMap = new HashMap<>(heroDamageEntitiesMap);
        damageEntitiesMap.remove(heroName);
        int totalDamageCount = 0;
        for (String hero : damageEntitiesMap.keySet()) {
            List<HeroDamageEventEntity> damageEntities = damageEntitiesMap.get(hero);
            for (Iterator<HeroDamageEventEntity> iterator = damageEntities.iterator(); iterator.hasNext();) {
                HeroDamageEventEntity damageEntity = (HeroDamageEventEntity) iterator.next();
                if (heroName.equals(damageEntity.getTargetHero())) {
                    totalDamageCount = totalDamageCount + damageEntity.getDamage_instance();
                }
            }
        }
        return totalDamageCount;

    }
}
