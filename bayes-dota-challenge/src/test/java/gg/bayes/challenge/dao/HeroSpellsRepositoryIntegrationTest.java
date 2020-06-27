package gg.bayes.challenge.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import gg.bayes.challenge.pojo.HeroMatchEventEntity;
import gg.bayes.challenge.pojo.HeroSpellsEventEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HeroSpellsRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    HeroSpellsRepository heroSpellsRepository;

    @Test
    public void whenFindByMatchIdThenReturnHeroEntity() {
        HeroMatchEventEntity heroEntity = new HeroMatchEventEntity();
        heroEntity.setHeroName("rubick");
        heroEntity.setMatchId(1L);
        heroEntity.setKills(27);

        HeroSpellsEventEntity heroSpellsEntity = new HeroSpellsEventEntity();
        heroSpellsEntity.setCaste(26);
        heroSpellsEntity.setSpellName("monkey_king_boundless_strike");
        heroSpellsEntity.setHeroKills(heroEntity);

        List<HeroSpellsEventEntity> heroSpellsEntities = new ArrayList<HeroSpellsEventEntity>();
        heroSpellsEntities.add(heroSpellsEntity);
        heroEntity.setHeroSpellsCollection(heroSpellsEntities);

        entityManager.persist(heroEntity);
        entityManager.flush();

        List<HeroSpellsEventEntity> heroSpellsEntityCollection = heroSpellsRepository.fetchHeroSpellsEvent("rubick", 1L);
        heroSpellsEntityCollection.forEach(herospells -> {
            assertThat(herospells.getCaste()).isEqualTo(heroSpellsEntity.getCaste());
            assertThat(herospells.getSpellName()).isEqualTo(heroSpellsEntity.getSpellName());
        });
    }

    @After
    public void cleanUp() {
        heroSpellsRepository.deleteAll();
    }

}
