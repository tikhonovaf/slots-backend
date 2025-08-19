//package ru.ttk.slotsbe.backend.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.ttk.slotsbe.backend.repository.SlotTemplateRepository;
//
//@Service
//@RequiredArgsConstructor
//public class SlotTemplateService {
//
//    private final SlotTemplateRepository slotTemplateRepository;
//
//    @Transactional
//    public void deleteSlotTemplatesByStoreId(Long storeId) {
//        if (storeId == null || storeId <= 0) {
//            throw new IllegalArgumentException("storeId must be a positive number");
//        }
//        slotTemplateRepository.deleteAllByStoreId(storeId);
//    }
//}
