package kr.co.yigil.report.report.domain.dto;

import kr.co.yigil.member.Member;
import kr.co.yigil.report.report.domain.Report;
import kr.co.yigil.report.type.domain.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportCommand {


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreateCommand{
        private Long reportTypeId;
        private String content;
        private Long travelId;
        private Long reporterId;

        public Report toEntity(ReportType reportType, Member member) {
            return new Report(reportType, content, travelId, member);
        }
    }
}
