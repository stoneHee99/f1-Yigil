package kr.co.yigil.travel.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.co.yigil.global.exception.BadRequestException;
import kr.co.yigil.travel.Travel;
import kr.co.yigil.travel.repository.TravelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {
    @InjectMocks
    private TravelService travelService;
    @Mock
    private TravelRepository travelRepository;

    @Test
    void GivenValidTravelId_WhenFindTravelById_ThenReturnTravel() {
        Travel travel = new Travel(1L);
        when(travelRepository.findById(anyLong())).thenReturn(Optional.of(travel));
        assertThat(travelService.findTravelById(anyLong())).isInstanceOf(Travel.class);
    }

    @Test
    void GivenInvalidTravelId_WhenFindTravelById_ThenThrowException() {
        when(travelRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> travelService.findTravelById(anyLong()));
    }
}