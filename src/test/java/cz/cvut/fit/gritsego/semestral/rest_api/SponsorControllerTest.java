package cz.cvut.fit.gritsego.semestral.rest_api;

import cz.cvut.fit.gritsego.semestral.dto.SponsorCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.SponsorDTO;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.exeptions.SponsorNotFoundException;
import cz.cvut.fit.gritsego.semestral.service.SponsorService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@AutoConfigureMockMvc
class SponsorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SponsorService sponsorService;

    @Test
    void readOne() throws Exception {
        SponsorDTO sponsor = new SponsorDTO(1,"Egor");
        BDDMockito.given(sponsorService.findByIdAsDTO(sponsor.getId())).willReturn(Optional.of(sponsor));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/sponsors/{id}", sponsor.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sponsor.getName())));
        Mockito.verify(sponsorService, Mockito.atLeastOnce()).findByIdAsDTO(sponsor.getId());
    }

    @Test
    void readAll() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        ArrayList<SponsorDTO> sponsors = new ArrayList<>();
        sponsors.add(new SponsorDTO(1,"Egor"));
        sponsors.add(new SponsorDTO(2,"Egor1"));
        Page<SponsorDTO> pageSponsors = new PageImpl<>(sponsors, pageable, sponsors.size());
        BDDMockito.given(sponsorService.readAll(pageable)).willReturn(pageSponsors);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/sponsors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[0].name", CoreMatchers.is(sponsors.get(0).getName())))


                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.[1].name", CoreMatchers.is(sponsors.get(1).getName())));

        Mockito.verify(sponsorService, Mockito.atLeastOnce()).readAll(pageable);
    }

    @Test
    void postNew() throws Exception {
        SponsorDTO sponsor = new SponsorDTO(1, "Egor");
        BDDMockito.given(sponsorService.create(any(SponsorCreateDTO.class))).willReturn(sponsor);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/sponsors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\"}")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(sponsor.getName())));

        BDDMockito.given(sponsorService.create(any(SponsorCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/sponsors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Egor\"}")
        ).andExpect(MockMvcResultMatchers.status().isConflict());
        Mockito.verify(sponsorService, Mockito.atLeast(2)).create(any(SponsorCreateDTO.class));

    }

    @Test
    void update() throws Exception {
        SponsorDTO updatedSponsor = new SponsorDTO(1,"Egor");
        BDDMockito.given(sponsorService.update(any(Integer.class), any(SponsorCreateDTO.class))).willReturn(updatedSponsor);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/sponsors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\"}")
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updatedSponsor.getName())));

        BDDMockito.given(sponsorService.update(any(Integer.class), any(SponsorCreateDTO.class))).willThrow(InstanceAlreadyExistsException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/sponsors/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\"}")
        ).andExpect(MockMvcResultMatchers.status().isConflict());

        BDDMockito.given(sponsorService.update(any(Integer.class), any(SponsorCreateDTO.class))).willThrow(SponsorNotFoundException.class);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/api/v1/sponsors/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"Egor\"}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(sponsorService, Mockito.atLeast(3)).update(any(Integer.class), any(SponsorCreateDTO.class));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/sponsors/{id}", 1)
        ).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(sponsorService, Mockito.atLeastOnce()).deleteById(1);
    }
}