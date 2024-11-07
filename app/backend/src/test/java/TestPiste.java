import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PisteServicesImplTest {

    @Mock
    private IPisteRepository pisteRepository;

    @InjectMocks
    private PisteServicesImpl pisteService;

    private Piste piste;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Easy Trail");
        piste.setColor(Color.GREEN);
        piste.setLength(1000);
        piste.setSlope(10);
    }

    @Test
    void testRetrieveAllPistes() {
        List<Piste> pistes = new ArrayList<>();
        pistes.add(piste);

        when(pisteRepository.findAll()).thenReturn(pistes);

        List<Piste> result = pisteService.retrieveAllPistes();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(piste.getNamePiste(), result.get(0).getNamePiste());

        verify(pisteRepository, times(1)).findAll();
    }

    @Test
    void testAddPiste() {
        when(pisteRepository.save(piste)).thenReturn(piste);

        Piste result = pisteService.addPiste(piste);
        assertNotNull(result);
        assertEquals(piste.getNamePiste(), result.getNamePiste());

        verify(pisteRepository, times(1)).save(piste);
    }

    @Test
    void testRemovePiste() {
        Long pisteId = 1L;

        doNothing().when(pisteRepository).deleteById(pisteId);

        pisteService.removePiste(pisteId);

        verify(pisteRepository, times(1)).deleteById(pisteId);
    }

    @Test
    void testRetrievePiste() {
        Long pisteId = 1L;

        when(pisteRepository.findById(pisteId)).thenReturn(Optional.of(piste));

        Piste result = pisteService.retrievePiste(pisteId);
        assertNotNull(result);
        assertEquals(piste.getNumPiste(), result.getNumPiste());

        verify(pisteRepository, times(1)).findById(pisteId);
    }

    @Test
    void testRetrievePiste_NotFound() {
        Long pisteId = 1L;

        when(pisteRepository.findById(pisteId)).thenReturn(Optional.empty());

        Piste result = pisteService.retrievePiste(pisteId);
        assertNull(result);

        verify(pisteRepository, times(1)).findById(pisteId);
    }
}
