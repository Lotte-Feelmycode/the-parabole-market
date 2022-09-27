package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.SellerRegisterDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    @Transactional
    public Seller registerSeller(@NotNull Long userId, @NotNull SellerRegisterDto sDto) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "사용자Id 로 조회 가능한 사용자가 존재하지 않습니다"));

        Seller seller = new Seller(sDto.getStoreName(), sDto.getSellerRegistrationNo());
        if (sellerRepository.findByRegistrationNo(seller.getRegistrationNo()) != null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "판매자 사업자 번호가 올바르지 않습니다.");
        }
        seller.setUser(user);
        return sellerRepository.save(seller);
    }

}
