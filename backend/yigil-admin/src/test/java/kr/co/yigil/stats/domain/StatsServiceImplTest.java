package kr.co.yigil.stats.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import kr.co.yigil.member.Member;
import kr.co.yigil.favor.domain.DailyFavorCount;
import kr.co.yigil.favor.domain.DailyTotalFavorCount;
import kr.co.yigil.member.Member;
import kr.co.yigil.member.SocialLoginType;
import kr.co.yigil.region.domain.DailyRegion;
import kr.co.yigil.travel.TravelType;
import kr.co.yigil.travel.domain.Travel;
import kr.co.yigil.travel.domain.Travel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @InjectMocks
    private StatsServiceImpl statsService;

    @Mock
    private StatsReader statsReader;

    @Mock TravelReader travelReader;

    @DisplayName("getRegionStats 메서드가 StatsReader를 잘 호출하는지")
    @Test
    void getRegionStatsTest() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        List<DailyRegion> expectedRegionStats = Collections.singletonList(new DailyRegion());
        when(statsReader.getRegionStats(any(LocalDate.class), any(LocalDate.class))).thenReturn(expectedRegionStats);

        List<DailyRegion> actualRegionStats = statsService.getRegionStats(startDate, endDate);

        assertEquals(expectedRegionStats, actualRegionStats);
    }

    @DisplayName("일별 좋아요 수 조회시 DailyFavorCount의 페이지 정보를 반환한다.")
    @Test
    void getDailyFavors() {
        PageRequest pageable = PageRequest.of(0, 10);

        DailyTotalFavorCount dailyFavorCount = new DailyTotalFavorCount(5L, LocalDate.now());
        Page<DailyTotalFavorCount> dailyFavorCountPage = new PageImpl<>(List.of(dailyFavorCount));

        when(statsReader.getDailyTotalFavorCounts(any())).thenReturn(dailyFavorCountPage);

        var result = statsService.getDailyFavors(pageable);

        assertThat(result).isInstanceOf(StaticInfo.DailyTotalFavorCountInfo.class);
    }

    @Test
    void getTopDailyFavors() {

        Member mockMember = new Member(1L, "email", "socialLoginId", "nickname", "profileImageUrl", SocialLoginType.GOOGLE);
        Travel mockTravel = new Travel(1L, mockMember, "title", "description", 4.5, false);
        DailyFavorCount dailyFavorCount = new DailyFavorCount(5L, mockTravel, LocalDate.now(), TravelType.SPOT);
        List<DailyFavorCount> dailyFavorCountList = List.of(dailyFavorCount);

        when(statsReader.getTopDailyFavorCount(any(), any(), any(), any())).thenReturn(dailyFavorCountList);

        var result = statsService.getTopDailyFavors(LocalDate.now(), LocalDate.now(), TravelType.SPOT, null);

        assertThat(result).isInstanceOf(StaticInfo.DailyTravelsFavorCountInfo.class);
    }

    @DisplayName("getRecentRegionStats 메서드가 TravelReader를 잘 호출하는지")
    @Test
    void getRecentRegionStatsTest() {
        long travelCount = 1;
        Travel mockTravel = mock(Travel.class);
        Member mockMember= mock(Member.class);
        when(mockTravel.getMember()).thenReturn(mockMember);
        List<Travel> recentTravel = Collections.singletonList(mockTravel);
        when(travelReader.getTodayTravelCnt()).thenReturn(travelCount);
        when(travelReader.getRecentTravel()).thenReturn(recentTravel);

        StatsInfo.Recent actualRecentRegionStats = statsService.getRecentRegionStats();

        assertEquals(travelCount, actualRecentRegionStats.getTravelCnt());
        assertEquals(recentTravel.size(), actualRecentRegionStats.getTravels().size());
    }
}