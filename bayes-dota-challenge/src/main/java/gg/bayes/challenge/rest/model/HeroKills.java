package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroKills {
    private String hero;
    private Integer kills;

    public HeroKills(String hero, Integer kills) {
        super();
        this.hero = hero;
        this.kills = kills;
    }
}
