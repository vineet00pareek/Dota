package gg.bayes.challenge.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * It's contain all Heroes who all are involved in match.
 * 
 * @author VineetPareek
 *
 */
@Data
@Entity
@Table(name = "HERO_MATCH_EVENT")
public class HeroMatchEventEntity {

    /**
     * This is primary key of Hero, configured to auto generation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long heroId;

    /**
     * It store matchId which identified by the specific game.
     */
    private Long matchId;

    /**
     * It's holds Hero who are involved in combat game.
     */
    private String heroName;

    /**
     * It's holds how many times hero had kills other heroes
     */
    private Integer kills;

    /**
     * It's holds total damage count by hitted by all heroes.
     */
    private Integer total_damages;

    /**
     * It's hold relation of {@link HeroMatchEventEntity} to
     * {@link HeroItemsEventEntity}}
     */
    @OneToMany(mappedBy = "heroKills", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeroItemsEventEntity> heroIteamCollection = new ArrayList<>();

    /**
     * It's hold relation of {@link HeroMatchEventEntity} to
     * {@link HeroSpellsEventEntity}}
     */
    @OneToMany(mappedBy = "heroKills", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeroSpellsEventEntity> heroSpellsCollection = new ArrayList<>();

    /**
     * It's hold relation of {@link HeroMatchEventEntity} to
     * {@link HeroDamageEventEntity}}
     */
    @OneToMany(mappedBy = "heroKills", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<HeroDamageEventEntity> heroDamageCollection = new ArrayList<>();
}
