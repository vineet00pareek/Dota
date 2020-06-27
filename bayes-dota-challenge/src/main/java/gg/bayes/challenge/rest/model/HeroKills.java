package gg.bayes.challenge.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HeroKills {
    private String hero;
    private Integer kills;
}
