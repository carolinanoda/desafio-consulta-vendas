package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.ReportDTO;
import com.devsuperior.dsmeta.dto.SummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<ReportDTO> findSalesReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate[] dates = resolveDates(minDate, maxDate);

        return repository.salesReport(dates[0], dates[1], name, pageable);

    }

	public List<SummaryDTO> findSellersSummary(String minDate, String maxDate) {
		LocalDate[] dates = resolveDates(minDate, maxDate);

		return repository.sellersSummary(dates[0], dates[1]);
	}

	private LocalDate[] resolveDates(String minDate, String maxDate) {

		LocalDate endDate = (maxDate == null || maxDate.isBlank())
				? LocalDate.now()
				: LocalDate.parse(maxDate);

		LocalDate startDate = (minDate == null || minDate.isBlank())
				? endDate.minusYears(1)
				: LocalDate.parse(minDate);

		return new LocalDate[]{startDate, endDate};
	}


}
