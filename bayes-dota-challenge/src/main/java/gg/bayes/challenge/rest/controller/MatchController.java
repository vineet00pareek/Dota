package gg.bayes.challenge.rest.controller;

import gg.bayes.challenge.exception.MatchServiceException;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    private ResponseUtils responseUtils;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<?> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        log.debug("Method entered in ingestMatch() for reading match event...");
        Long matchId;
        try {
            matchId = matchService.ingestMatch(payload);
            return responseUtils.buildOk(matchId);
        } catch (MatchServiceException e) {
            log.error("Something went wrong in reading the match event", e);
            return responseUtils.build(e);
        }
    }

    @GetMapping("{matchId}")
    public ResponseEntity<?> getMatch(@PathVariable("matchId") Long matchId) {
        log.debug("Method entered in getMatch() for fetching match details from match ID");
        try {
            List<HeroKills> heroKills = matchService.getHeroKillsByMatchId(matchId);
            log.info("Successfully received the match detail from Match ID :" + matchId + " Match detail: "
                    + heroKills);
            return responseUtils.buildOk(heroKills);
        } catch (MatchServiceException e) {
            log.error("Getting error while fetching match details from Match ID", e);
            return responseUtils.build(e);
        }
    }

    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<?> getItems(@PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.debug("Method entered in getItems() for fetching Items event from Match ID and Hero name");
        try {
            List<HeroItems> heroItems = matchService.getItemsByMatchIdAndHeroName(matchId, heroName);
            log.info("Successfully received the Item events from Match ID :" + matchId + " Item event : "
                    + heroItems);
            return responseUtils.buildOk(heroItems);
        } catch (MatchServiceException e) {
            log.error("Getting error while fetching Items event from Match ID", e);
            return responseUtils.build(e);
        }
    }

    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<?> getSpells(@PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.debug("Method entered in getSpells() for fetching Spell event from Match ID and Hero name");
        try {
            List<HeroSpells> heroSpells = matchService.getHeroSpellsByMatchIdAndHeroName(matchId, heroName);
            log.info("Successfully received the Spell events from Match ID  :" + matchId + " with Hero name: "
                    + heroName + " Item event : " + heroSpells);
            return responseUtils.buildOk(heroSpells);
        } catch (MatchServiceException e) {
            log.error("Getting error while fetching Spell event from Match ID", e);
            return responseUtils.build(e);
        }

    }

    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<?> getDamage(@PathVariable("matchId") Long matchId,
            @PathVariable("heroName") String heroName) {
        log.debug("Method entered in getDamage() for fetching Damage event from Match ID and Hero name");
        try {
            List<HeroDamage> heroDamages = matchService.getHeroDamageByMatchIdAndHeroName(matchId, heroName);
            log.info("Successfully received the Spell Damage from Match ID  :" + matchId + " with Hero : " + heroName
                    + " Item event : " + heroDamages);
            return responseUtils.buildOk(heroDamages);
        } catch (MatchServiceException e) {
            log.error("Getting error while fetching Damage event from Match ID", e);
            return responseUtils.build(e);
        }
    }
}
