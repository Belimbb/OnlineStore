package com.teamChallenge.entity.promoCode;

import com.teamChallenge.dto.request.PromoCodeRequestDto;
import com.teamChallenge.dto.response.PromoCodeResponseDto;
import com.teamChallenge.exception.LogEnum;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomAlreadyExistException;
import com.teamChallenge.exception.exceptions.generalExceptions.CustomNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService{
    private static final String OBJECT_NAME = "Order";

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    @Override
    public List<PromoCodeResponseDto> getAll() {
        List<PromoCodeEntity> all = promoCodeRepository.findAll();

        log.info("{}: All " + OBJECT_NAME + "s retrieved from db", LogEnum.SERVICE);
        return promoCodeMapper.toResponseDtoList(all);
    }

    @Override
    public PromoCodeResponseDto getById(String id) {
        log.info("{}: " + OBJECT_NAME + " (id: {}) retrieved from db", LogEnum.SERVICE, id);
        return promoCodeMapper.toResponseDto(findById(id));
    }

    @Override
    public PromoCodeResponseDto getByCode(String code) {
        log.info("{}: " + OBJECT_NAME + " (code: {}) retrieved from db", LogEnum.SERVICE, code);
        return promoCodeMapper.toResponseDto(findByCode(code));
    }

    @Override
    public PromoCodeResponseDto create(PromoCodeRequestDto requestDto) {
        String requestDtoCode = requestDto.code();

        if (promoCodeRepository.existsByCode(requestDtoCode)){
            throw new CustomAlreadyExistException(OBJECT_NAME, "Code", requestDtoCode);
        }

        PromoCodeEntity newPromoCode = new PromoCodeEntity(requestDtoCode, requestDto.discount(), requestDto.expirationDate());

        log.info("{}: " + OBJECT_NAME + " was created", LogEnum.SERVICE);
        return promoCodeMapper.toResponseDto(promoCodeRepository.save(newPromoCode));
    }

    @Override
    public PromoCodeResponseDto update(String id, PromoCodeRequestDto requestDto) {
        PromoCodeEntity entity = findById(id);
        String code = requestDto.code();

        codeValidation(code);

        entity.setCode(code);
        entity.setDiscount(requestDto.discount());
        entity.setExpirationDate(requestDto.expirationDate());

        log.info("{}: " + OBJECT_NAME + " (id: {}) updated)", LogEnum.SERVICE, entity.getId());
        return promoCodeMapper.toResponseDto(promoCodeRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        PromoCodeEntity entity = findById(id);
        promoCodeRepository.delete(entity);

        log.info("{}: " + OBJECT_NAME + " (id: {}) deleted", LogEnum.SERVICE, id);
    }

    private PromoCodeEntity findById(String id) {
        return promoCodeRepository.findById(id).orElseThrow(()-> new CustomNotFoundException(OBJECT_NAME, id));
    }

    private PromoCodeEntity findByCode(String code) {
        return promoCodeRepository.findByCode(code).orElseThrow(()-> new CustomNotFoundException(OBJECT_NAME, code));
    }

    private void codeValidation(String code){
        if (promoCodeRepository.existsByCode(code)){
            throw new CustomAlreadyExistException(OBJECT_NAME, "Code", code);
        }
    }
}
