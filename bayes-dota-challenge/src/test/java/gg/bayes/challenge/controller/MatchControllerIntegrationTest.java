package gg.bayes.challenge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import gg.bayes.challenge.dao.HeroDamageRepository;
import gg.bayes.challenge.dao.HeroItemsRepository;
import gg.bayes.challenge.dao.HeroKillsRepository;
import gg.bayes.challenge.dao.HeroSpellsRepository;
import gg.bayes.challenge.rest.controller.MatchController;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.service.MatchService;

@RunWith(SpringRunner.class)
@WebMvcTest(MatchController.class)
public class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MatchService service;

    @MockBean
    private HeroKillsRepository heroRepository;

    @MockBean
    private HeroSpellsRepository heroSpellsRepository;

    @MockBean
    private HeroItemsRepository heroItemsRepository;

    @MockBean
    private HeroDamageRepository heroDamageRepository;

    private static final String PAYLOAD = "[00:10:41.998] npc_dota_hero_abyssal_underlord casts ability abyssal_underlord_firestorm (lvl 1) on dota_unknown";
    private Long matchID = 1L;

    @Test
    public void ingestMatch_thenReturnMatchID() throws Exception {
        given(service.ingestMatch(PAYLOAD)).willReturn(matchID);
        ResultActions resultActions = mvc
                .perform(post("/api/match").contentType(MediaType.TEXT_PLAIN_VALUE).content(PAYLOAD))
                .andExpect(status().isOk());
        assertThat(resultActions.andReturn().getResponse().getContentAsString()).isEqualTo("1");
    }

    @Test
    public void getMatch_thenReturnHeroKills() throws Exception {
        List<HeroKills> heroKills = new ArrayList<HeroKills>();
        HeroKills heroKill = new HeroKills("rubick", 27);
        heroKills.add(heroKill);
        given(service.getHeroKillsByMatchId(matchID)).willReturn(heroKills);
        mvc.perform(get("/api/match/1")).andExpect(status().isOk());

    }

    @Test
    public void getItems_thenReturnHeroItems() throws Exception {
        given(service.ingestMatch(PAYLOAD)).willReturn(matchID);
        mvc.perform(get("/api/match/1/rubick/items").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getSpells_thenReturnHeroSpells() throws Exception {
        given(service.ingestMatch(PAYLOAD)).willReturn(matchID);
        mvc.perform(get("/api/match/1/rubick/spells").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getDamage_thenReturnHeroDamage() throws Exception {

        given(service.ingestMatch(PAYLOAD)).willReturn(matchID);
        mvc.perform(get("/api/match/1/rubick/damage").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
