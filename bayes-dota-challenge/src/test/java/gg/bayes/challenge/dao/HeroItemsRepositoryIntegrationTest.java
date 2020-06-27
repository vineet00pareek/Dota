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

import gg.bayes.challenge.pojo.HeroItemsEventEntity;
import gg.bayes.challenge.pojo.HeroMatchEventEntity;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HeroItemsRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    HeroItemsRepository heroItemsRepository;

    @Test
    public void whenFindByMatchIdThenReturnHeroEntity() {
        HeroMatchEventEntity heroEntity = new HeroMatchEventEntity();

        heroEntity.setHeroName("rubick");
        heroEntity.setMatchId(1L);
        heroEntity.setKills(27);

        HeroItemsEventEntity heroItemsEntity = new HeroItemsEventEntity();
        heroItemsEntity.setItem("recipe_wraith_band");
        heroItemsEntity.setHeroKills(heroEntity);
        heroItemsEntity.setTime(1009975L);

        List<HeroItemsEventEntity> heroItemsEntities = new ArrayList<HeroItemsEventEntity>();
        heroItemsEntities.add(heroItemsEntity);
        heroEntity.setHeroIteamCollection(heroItemsEntities);

        entityManager.persist(heroEntity);
        entityManager.flush();
        List<HeroItemsEventEntity> heroItemsEntityCollection = heroItemsRepository.fetchHeroItemsEvent("rubick", 1L);

        heroItemsEntityCollection.forEach(heroItems -> {
            assertThat(heroItems.getItem()).isEqualTo(heroItemsEntity.getItem());
            assertThat(heroItems.getTime()).isEqualTo(heroItemsEntity.getTime());
        });
    }

    @After
    public void cleanUp() {
        heroItemsRepository.deleteAll();
    }

}
