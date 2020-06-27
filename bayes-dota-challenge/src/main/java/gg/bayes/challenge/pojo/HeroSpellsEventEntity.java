package gg.bayes.challenge.pojo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * It's contain Spells casts by specified hero.
 * 
 * @author VineetPareek
 *
 */
@Data
@Entity
@Table(name = "HERO_SPELLS_EVENT")
public class HeroSpellsEventEntity {

    /**
     * This is primary key of Items, configured to auto generation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long spellId;

    /**
     * It's holds the number of times they cast said spell by specified hero.
     */
    private Integer caste;

    /**
     * 
     * It's holds to Spells Name caste by specified hero.
     */
    private String spellName;

    /**
     * It's hold relation of {@link HeroMatchEventEntity} to
     * {@link HeroSpellsEventEntity}}
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hero_Id", nullable = false)
    private HeroMatchEventEntity heroKills;
}
