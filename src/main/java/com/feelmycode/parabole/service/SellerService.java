package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.SellerDto;
import com.feelmycode.parabole.dto.SellerRegisterDto;
import com.feelmycode.parabole.global.error.exception.NoDataException;
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
@Transactional(readOnly = true)
public class SellerService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public Seller registerSeller(@NotNull Long userId, @NotNull SellerRegisterDto sDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException());

        Seller seller = new Seller(sDto.getStoreName(), sDto.getSellerRegistrationNo());
        if (sellerRepository.findByRegistrationNo(seller.getRegistrationNo()) != null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "판매자 사업자 번호가 중복됩니다. 다른 값을 입력하세요.");
        }
        seller.setUser(user);
        return sellerRepository.save(seller);
    }

    public Seller getSellerByUserId(@NotNull Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoDataException()).getSeller();
    }

    public Seller getSellerByStoreName(@NotNull String storeName) {
        Seller seller = sellerRepository.findByStoreName(storeName);
        if (seller == null) {
            throw new ParaboleException(HttpStatus.NOT_FOUND, "해당 스토어를 가지는 판매자가 존재하지 않습니다.");
        }
        return seller;
    }

    public SellerDto getSellerBySellerId(@NotNull Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(()
            -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 판매자 Id로 조회되는 판매자가 존재하지 않습니다."));
        return new SellerDto(seller);
    }

}
