package kr.co.yigil.report.report.infrastructure;

import kr.co.yigil.report.report.domain.Report;
import kr.co.yigil.report.report.domain.ReportStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportStoreImpl implements ReportStore {
    private final ReportRepository reportRepository;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public void delete(Report report) {
        reportRepository.delete(report);
    }
}
