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
 * It's contain Damage event entity, hitting by a specified hero.
 * 
 * @author VineetPareek
 *
 */
@Data
@Entity
@Table(name = "HERO_DAMAGE_EVENT")
public class HeroDamageEventEntity {

    /**
     * This is primary key of Damage, configured to auto generation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long damageId;

    /**
     * It's holds the target hero name that damaged by specified hero.
     */
    private String targetHero;

    /**
     * It's holds the number of times it damaged by specified hero.
     */
    private Integer damageInstance;
    
    /**
     * It's holds the total damage by specified hero.
     */
    private Integer totalDamage;

    /**
     * It's hold relation of {@link HeroMatchEventEntity} to
     * {@link HeroDamageEventEntity}}
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hero_Id", nullable = false)
    private HeroMatchEventEntity heroKills;
}
