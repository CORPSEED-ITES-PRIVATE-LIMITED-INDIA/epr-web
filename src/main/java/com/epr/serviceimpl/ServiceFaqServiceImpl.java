package com.epr.serviceimpl;


import com.epr.dto.servicefaq.ServiceFaqRequestDto;
import com.epr.dto.servicefaq.ServiceFaqResponseDto;
import com.epr.entity.ServiceFaq;
import com.epr.entity.Services;
import com.epr.entity.User;
import com.epr.repository.ServiceFaqRepository;
import com.epr.repository.ServiceRepository;
import com.epr.repository.UserRepository;
import com.epr.service.ServiceFaqService;
import com.epr.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceFaqServiceImpl implements ServiceFaqService {

    private static final Logger log = LoggerFactory.getLogger(ServiceFaqServiceImpl.class);

    private final ServiceFaqRepository faqRepository;
    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;
    private final DateTimeUtil dateTimeUtil;

    private User validateAndGetActiveUser(Long userId) {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("User ID is required");
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));
    }

    @Override
    public List<ServiceFaqResponseDto> findByServiceId(Long serviceId) {
        if (serviceId == null || serviceId <= 0) throw new IllegalArgumentException("Valid service ID required");
        return faqRepository.findByServiceIdAndDeleteStatusOrderByDisplayOrderAsc(serviceId, 2)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceFaqResponseDto findById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid FAQ ID");
        ServiceFaq faq = faqRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found"));
        return toResponseDto(faq);
    }

    @Override
    public ServiceFaqResponseDto createFaq(ServiceFaqRequestDto dto, Long userId) {
        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        Services service = serviceRepository.findActiveById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        if (faqRepository.existsByQuestionIgnoreCaseAndServiceId(dto.getQuestion().trim(), dto.getServiceId())) {
            throw new IllegalArgumentException("This question already exists for this service");
        }

        ServiceFaq faq = new ServiceFaq();
        mapRequestToEntity(dto, faq);
        faq.setService(service);
        faq.setUuid(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
        faq.setPostDate(dateTimeUtil.getCurrentUtcTime().toString());
        faq.setAddedByUUID(user.getUuid());
        faq.setDeleteStatus(2);

        ServiceFaq saved = faqRepository.save(faq);
        log.info("FAQ created for service {}: {}", dto.getServiceId(), dto.getQuestion());
        return toResponseDto(saved);
    }

    @Override
    public ServiceFaqResponseDto updateFaq(Long id, ServiceFaqRequestDto dto, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid FAQ ID");

        validateDto(dto);
        User user = validateAndGetActiveUser(userId);

        ServiceFaq existing = faqRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found"));

        Services service = serviceRepository.findActiveById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        String newQuestion = dto.getQuestion().trim();
        if (!existing.getQuestion().equalsIgnoreCase(newQuestion) &&
                faqRepository.existsByQuestionIgnoreCaseAndServiceIdAndIdNot(newQuestion, dto.getServiceId(), id)) {
            throw new IllegalArgumentException("This question already exists for this service");
        }

        mapRequestToEntity(dto, existing);
        existing.setService(service);
        existing.setModifyDate(dateTimeUtil.getCurrentUtcTime().toString());
        existing.setModifyByUUID(user.getUuid());

        ServiceFaq updated = faqRepository.save(existing);
        return toResponseDto(updated);
    }

    @Override
    public void softDeleteFaq(Long id, Long userId) {
        if (id == null || id <= 0) throw new IllegalArgumentException("Invalid FAQ ID");
        validateAndGetActiveUser(userId);

        ServiceFaq faq = faqRepository.findByIdAndDeleteStatus(id, 2)
                .orElseThrow(() -> new IllegalArgumentException("FAQ not found"));

        faq.setDeleteStatus(1);
        faq.setModifyDate(dateTimeUtil.getCurrentUtcTime().toString());
        faqRepository.save(faq);
        log.info("FAQ soft deleted: {}", id);
    }

    // Helpers
    private void validateDto(ServiceFaqRequestDto dto) {
        if (dto == null) throw new IllegalArgumentException("FAQ data is required");
        if (dto.getQuestion() == null || dto.getQuestion().trim().isEmpty())
            throw new IllegalArgumentException("Question is required");
        if (dto.getAnswer() == null || dto.getAnswer().trim().isEmpty())
            throw new IllegalArgumentException("Answer is required");
        if (dto.getServiceId() == null || dto.getServiceId() <= 0)
            throw new IllegalArgumentException("Valid service is required");
    }

    private void mapRequestToEntity(ServiceFaqRequestDto dto, ServiceFaq entity) {
        entity.setQuestion(dto.getQuestion().trim());
        entity.setAnswer(dto.getAnswer());
        entity.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        entity.setDisplayStatus(dto.getDisplayStatus() != null && dto.getDisplayStatus() >= 1 && dto.getDisplayStatus() <= 2
                ? dto.getDisplayStatus() : 1);
    }

    private ServiceFaqResponseDto toResponseDto(ServiceFaq f) {
        ServiceFaqResponseDto dto = new ServiceFaqResponseDto();
        dto.setId(f.getId());
        dto.setUuid(f.getUuid());
        dto.setQuestion(f.getQuestion());
        dto.setAnswer(f.getAnswer());
        dto.setDisplayOrder(f.getDisplayOrder());
        dto.setDisplayStatus(f.getDisplayStatus());
        dto.setServiceId(f.getService().getId());
        dto.setServiceTitle(f.getService().getTitle());
        dto.setDeleteStatus(f.getDeleteStatus());
        return dto;
    }
}