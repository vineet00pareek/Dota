package gg.bayes.challenge.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import gg.bayes.challenge.dao.HeroKillsRepository;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HeroKillsRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    HeroKillsRepository heroRepository;

    @Test
    public void whenFindByMatchIdThenReturnHeroEntity() {
        HeroMatchEventEntity heroEntity = new HeroMatchEventEntity();

        heroEntity.setHeroName("rubick");
        heroEntity.setMatchId(1L);
        heroEntity.setKills(27);
        heroEntity.setTotal_damages(9669);
        entityManager.persist(heroEntity);
        entityManager.flush();

        List<HeroMatchEventEntity> heroKillsEntityCollection = heroRepository.findByMatchId(1L);
        heroKillsEntityCollection.forEach(hero -> {
            assertThat(hero.getHeroName()).isEqualTo(heroEntity.getHeroName());
            assertThat(hero.getKills()).isEqualTo(heroEntity.getKills());
            assertThat(hero.getTotal_damages()).isEqualTo(heroEntity.getTotal_damages());
        });
    }

    @After
    public void cleanUp() {
        heroRepository.deleteAll();
    }
}
