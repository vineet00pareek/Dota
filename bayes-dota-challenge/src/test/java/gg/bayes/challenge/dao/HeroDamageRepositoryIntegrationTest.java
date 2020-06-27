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

import gg.bayes.challenge.pojo.HeroDamageEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HeroDamageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    HeroDamageRepository heroDamageRepository;

    @Test
    public void whenFindByMatchIdThenReturnHeroEntity() {
        HeroMatchEventEntity heroEntity = new HeroMatchEventEntity();

        heroEntity.setHeroName("rubick");
        heroEntity.setMatchId(1L);
        heroEntity.setKills(27);
        heroEntity.setTotal_damages(9669);
        HeroDamageEventEntity heroDamageEntity = new HeroDamageEventEntity();
        heroDamageEntity.setDamage_instance(3807);
        heroDamageEntity.setTargetHero("mars");
        heroDamageEntity.setHeroKills(heroEntity);
        List<HeroDamageEventEntity> heroDamageEntities = new ArrayList<HeroDamageEventEntity>();
        heroDamageEntities.add(heroDamageEntity);
        heroEntity.setHeroDamageCollection(heroDamageEntities);

        entityManager.persist(heroEntity);
        entityManager.flush();

        List<HeroDamageEventEntity> heroDamageEntityCollection = heroDamageRepository.join("rubick", 1L);
        heroDamageEntityCollection.forEach(heroDamage -> {
            assertThat(heroDamage.getTargetHero()).isEqualTo(heroDamageEntity.getTargetHero());
            assertThat(heroDamage.getDamage_instance()).isEqualTo(heroDamageEntity.getDamage_instance());
        });
    }

    @After
    public void cleanUp() {
        heroDamageRepository.deleteAll();
    }

}
