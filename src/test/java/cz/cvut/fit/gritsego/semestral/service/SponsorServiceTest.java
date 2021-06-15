package cz.cvut.fit.gritsego.semestral.service;

import cz.cvut.fit.gritsego.semestral.dto.SponsorCreateDTO;
import cz.cvut.fit.gritsego.semestral.dto.SponsorDTO;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.entity.Sponsor;
import cz.cvut.fit.gritsego.semestral.repository.SponsorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
@SpringBootTest
public class SponsorServiceTest {
    @Autowired
    private SponsorService sponsorService;

    @MockBean
    private SponsorRepository sponsorRepositoryMock;

    @Test
    void create() throws Exception {
        Sponsor sponsorToReturn = new Sponsor("Logitech");
        ReflectionTestUtils.setField(sponsorToReturn, "id", 1);
        SponsorCreateDTO sponsorCreateDTO = new SponsorCreateDTO("Logitech");

        BDDMockito.given(sponsorRepositoryMock.save(any(Sponsor.class))).willReturn(sponsorToReturn);
        SponsorDTO returnedSponsorDTO = sponsorService.create(sponsorCreateDTO);

        SponsorDTO expectedSponsorDTO = new SponsorDTO(1 ,"Logitech");
        Assertions.assertEquals(expectedSponsorDTO.getName(), returnedSponsorDTO.getName());


        ArgumentCaptor<Sponsor> argumentCaptor = ArgumentCaptor.forClass(Sponsor.class);
        Mockito.verify(sponsorRepositoryMock, Mockito.atLeastOnce()).save(argumentCaptor.capture());

        Sponsor sponsorProvidedToSave = argumentCaptor.getValue();
        Assertions.assertEquals("Logitech", sponsorProvidedToSave.getName());

    }

    @Test
    void update() throws Exception {
        Sponsor sponsorToReturn = new Sponsor("Asus");
        ReflectionTestUtils.setField(sponsorToReturn, "id", 1);
        SponsorCreateDTO sponsorCreateDTO = new SponsorCreateDTO("Logitech");

        BDDMockito.given(sponsorRepositoryMock.findById(1)).willReturn(Optional.of(sponsorToReturn));

        SponsorDTO returnedSponsorDTO = sponsorService.update(1, sponsorCreateDTO);

        SponsorDTO expectedSponsorDTO = new SponsorDTO(1 ,"Logitech");
        Assertions.assertEquals(expectedSponsorDTO.getName(), returnedSponsorDTO.getName());
    }

    @Test
    void delete() throws Exception {
        Sponsor SponsorToReturn = new Sponsor("Egor");
        ReflectionTestUtils.setField(SponsorToReturn, "id", 1);

        BDDMockito.given(sponsorRepositoryMock.findById(1)).willReturn(Optional.of(SponsorToReturn));
        BDDMockito.given(sponsorRepositoryMock.findByName("Egor")).willReturn(Optional.of(SponsorToReturn));

        sponsorService.deleteById(SponsorToReturn.getId());
        BDDMockito.verify(sponsorRepositoryMock, Mockito.atLeastOnce()).deleteById(SponsorToReturn.getId());

        sponsorService.deleteByName(SponsorToReturn.getName());
        BDDMockito.verify(sponsorRepositoryMock, Mockito.atLeastOnce()).deleteByName(SponsorToReturn.getName());
    }


}
