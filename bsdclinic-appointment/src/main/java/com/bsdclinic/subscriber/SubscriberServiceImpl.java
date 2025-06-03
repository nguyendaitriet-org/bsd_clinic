package com.bsdclinic.subscriber;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.response.UserResponse;
import com.bsdclinic.response.DatatableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public DatatableResponse getSubscribersByFilter(SubscriberFilter subscriberFilter) {
        Specification<Subscriber> subscriberSpecification = SubscriberSpecifications.withFilter(subscriberFilter);
        Pageable pageable = PageRequest.of(
                subscriberFilter.getStart() / subscriberFilter.getLength(),
                subscriberFilter.getLength()
        );
        Page<Subscriber> subscribers = subscriberRepository.findAll(subscriberSpecification, pageable);
        List<SubscriberDto> appointmentDtos = subscribers.stream().map(appointmentMapper::toSubscriberDto).toList();

        DatatableResponse<SubscriberDto> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(appointmentDtos);
        datatableResponse.setDraw(subscriberFilter.getDraw());
        Long totalRecord = subscribers.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }
}
