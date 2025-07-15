package com.bsdclinic.subscriber;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.BaseEntity_;
import com.bsdclinic.exception_handler.exception.BadRequestException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.user.User_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;
    private final MessageProvider messageProvider;

    @Override
    public DatatableResponse getSubscribersByFilter(SubscriberFilter subscriberFilter) {
        Specification<Subscriber> subscriberSpecification = SubscriberSpecifications.withFilter(subscriberFilter);
        Pageable pageable = PageRequest.of(
                subscriberFilter.getStart() / subscriberFilter.getLength(),
                subscriberFilter.getLength(),
                Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT)
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

    public void checkDuplicateSubscriberEmail (String email) {
        boolean isSubscriberEmailExisted = subscriberRepository.existsByEmail(email);
        if (isSubscriberEmailExisted) {
            Map<String, String> errors = Map.of("subscriberEmail", messageProvider.getMessage("validation.existed.email"));
            throw new BadRequestException(errors);
        }
    }

    @Override
    public SubscriberDto getSubscriberById(String id) {
        Subscriber subscriber = subscriberRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.subscriber"))
        );

        return appointmentMapper.toSubscriberDto(subscriber);
    }
}
