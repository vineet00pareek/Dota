package gg.bayes.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import gg.bayes.challenge.dao.HeroDamageRepository;
import gg.bayes.challenge.dao.HeroItemsRepository;
import gg.bayes.challenge.dao.HeroKillsRepository;
import gg.bayes.challenge.dao.HeroSpellsRepository;
import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroItemsEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.pojo.HeroSpellsEventEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.impl.MatchServiceImpl;
import gg.bayes.challenge.utils.ReaderUtils;

@RunWith(SpringRunner.class)
public class MatchServiceImplIntegrationTest {

    @TestConfiguration
    static class MatchServiceImplTestContextConfiguration {

        @Bean
        public MatchService matchService() {
            return new MatchServiceImpl();
        }
    }

    @Autowired
    private MatchService matchService;

    @MockBean
    private HeroKillsRepository heroRepository;

    @MockBean
    private HeroItemsRepository heroItemsRepository;

    @MockBean
    private HeroDamageRepository heroDamageRepository;

    @MockBean
    private HeroSpellsRepository heroSpellsRepository;

    @MockBean
    private ReaderUtils readerUtils;

    private Long matchID = 1L;

    private static final String HERO_NAME = "rubick";
    private static final String EVENTS = "[00:10:41.998] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 1) on dota_unknown";

    @Before
    public void setUp() {

        List<HeroMatchEventEntity> heroEntities = getHeroEntity();
        Set<HeroMatchEventEntity> heroEntitiesUnique = new HashSet<>();
        heroEntities.forEach(heroEntry -> heroEntitiesUnique.add(heroEntry));
        Mockito.when(heroRepository.saveAll(heroEntitiesUnique)).thenReturn(heroEntities);
        Mockito.when(heroRepository.findByMatchId(matchID)).thenReturn(heroEntities);
        Mockito.when(readerUtils.readPayload(EVENTS, matchID)).thenReturn(heroEntitiesUnique);
    }

    @Test
    public void ingestMatchTest() {
        Long matchId = matchService.ingestMatch(EVENTS);
        assertThat(matchId).isEqualTo(matchID);
    }

    @Test
    public void getHeroKillsByMatchIdTest() {
        List<HeroKills> heroKills = matchService.getHeroKillsByMatchId(matchID);
        for (HeroKills hero : heroKills) {
            assertThat(hero.getHero()).isEqualTo(HERO_NAME);
            assertThat(hero.getKills()).isEqualTo(27);
        }
    }

    @Test
    public void getItemsByMatchIdAndHeroNameTest() {
        List<HeroItems> heroItems = matchService.getItemsByMatchIdAndHeroName(matchID, HERO_NAME);
        for (HeroItems heroItem : heroItems) {
            assertThat(heroItem.getItem()).isEqualTo("tango");
            assertThat(heroItem.getTimestamp()).isEqualTo(919996L);
        }
    }

    @Test
    public void getHeroSpellsByMatchIdAndHeroNameTest() {
        List<HeroSpells> heroSpells = matchService.getHeroSpellsByMatchIdAndHeroName(matchID, HERO_NAME);
        for (HeroSpells heroSpell : heroSpells) {
            assertThat(heroSpell.getSpell()).isEqualTo("grimstroke_ink_creature");
            assertThat(heroSpell.getCasts()).isEqualTo(15);
        }
    }

    @Test
    public void getHeroDamageByMatchIdAndHeroNameTest() {
        List<HeroDamage> heroDamages = matchService.getHeroDamageByMatchIdAndHeroName(matchID, HERO_NAME);
        for (HeroDamage heroDamage : heroDamages) {
            assertThat(heroDamage.getTarget()).isEqualTo("monkey_king");
            assertThat(heroDamage.getDamageInstances()).isEqualTo(3141);
        }
    }

    private List<HeroMatchEventEntity> getHeroEntity() {
        List<HeroMatchEventEntity> heroEntities = new ArrayList<HeroMatchEventEntity>();
        List<HeroItemsEventEntity> iteamCollection = new ArrayList<>();
        List<HeroSpellsEventEntity> spellCollection = new ArrayList<>();
        List<HeroDamageEventEntity> damageCollection = new ArrayList<>();

        HeroMatchEventEntity heroEntity = new HeroMatchEventEntity();
        heroEntity.setMatchId(matchID);
        heroEntity.setHeroName(HERO_NAME);
        heroEntity.setKills(27);
        heroEntity.setTotal_damages(9669);

        HeroDamageEventEntity damageEntity = new HeroDamageEventEntity();
        damageEntity.setTargetHero("monkey_king");
        damageEntity.setDamage_instance(3141);
        damageCollection.add(damageEntity);

        HeroSpellsEventEntity spellsEntity = new HeroSpellsEventEntity();
        spellsEntity.setCaste(15);
        spellsEntity.setSpellName("grimstroke_ink_creature");
        spellCollection.add(spellsEntity);

        HeroItemsEventEntity itemsEntity = new HeroItemsEventEntity();
        itemsEntity.setItem("tango");
        itemsEntity.setTime(919996L);
        iteamCollection.add(itemsEntity);

        heroEntity.setHeroDamageCollection(damageCollection);
        heroEntity.setHeroIteamCollection(iteamCollection);
        heroEntity.setHeroSpellsCollection(spellCollection);
        heroEntities.add(heroEntity);

        return heroEntities;
    }
}
