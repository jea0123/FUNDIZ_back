package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.request.reward.RewardBackingRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingListDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingList_backingDetail;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingList_reward;
import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.exception.notfound.BackingNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import com.example.funding.service.BackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BackingServiceImpl implements BackingService {
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final BackingMapper backingMapper;
    private final AddressMapper addressMapper;
    private final RewardMapper rewardMapper;
    private final CreatorMapper creatorMapper;
    private final PaymentMapper paymentMapper;
    private final ShippingMapper shippingMapper;

    @Override
    public ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId) {
        User user = userMapper.getUserById(userId);
        if (user == null) throw new UserNotFoundException();

        Project project = projectMapper.findById(projectId);
        if (project == null) throw new ProjectNotFoundException();

        List<AddressResponseDto> addressList = addressMapper.getAddressList(userId);
        List<Reward> rewardList = rewardMapper.getRewardList(projectId);
        Creator creator = creatorMapper.findById(project.getCreatorId());
        List<BackingPagePaymentDto> backingPagePayment = paymentMapper.backingPagePayment(userId);

        List<BackingRewardDto> rewards = rewardList.stream()
                .map(r -> BackingRewardDto.builder()
                        .rewardId(r.getRewardId())
                        .rewardName(r.getRewardName())
                        .price(r.getPrice())
                        .quantity(1L)
                        .deliveryDate(r.getDeliveryDate())
                        .build()).toList();

        BackingResponseDto backingResponse = BackingResponseDto.builder()
                .userId(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .addressList(addressList)
                .creatorName(creator.getCreatorName())
                .profileImg(creator.getProfileImg())
                .title(project.getTitle())
                .thumbnail(project.getThumbnail())
                .backingPagePaymentList(backingPagePayment)
                .build();

        return ResponseEntity.ok(ResponseDto.success(200, "후원페이지 출력 성공", backingResponse));
    }


    //TODO : 후원 하기 (생성)
    @Override
    @Transactional
    public ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto) {
        Backing backing = requestDto.getBacking();
        backing.setUserId(userId);
        backing.setCreatedAt(LocalDate.now());
        backingMapper.addBacking(backing);

        Long backingId = backing.getBackingId();

        List<RewardBackingRequestDto> rewards = requestDto.getRewards();

        if(rewards != null && !rewards.isEmpty()){
            for(RewardBackingRequestDto reward : rewards){
                BackingDetail detail = new BackingDetail();
                detail.setBackingId(backingId);
                detail.setRewardId(reward.getRewardId());
                detail.setQuantity(reward.getQuantity());
                detail.setPrice(reward.getPrice());
                backingMapper.addBackingDetail(detail);
            }

        }
        if (rewards != null) {
            rewards.forEach(r -> System.out.println("  - rewardId: " + r.getRewardId() + ", qty: " + r.getQuantity()));
        }

//        Long backingId = backing.getBackingId();
//        System.out.println("backingId 확인" + backingId);
//        BackingDetail detail = requestDto.getBackingDetail();
//        detail.setBackingId(backingId);
//        backingMapper.addBackingDetail(detail);

        //orderId 임의 값 설정
        String orderId = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        Payment payment = requestDto.getPayment();
        payment.setOrderId(orderId);
        payment.setBackingId(backingId);
        paymentMapper.addPayment(payment);
        return ResponseEntity.ok(ResponseDto.success(200, "후원 추가 성공", /*"추가!!"*/ null));
    }

    /**
     * <p>로그인 사용자 후원 리스트</p>
     *
     * @param userId 사용자 Backing으로 통합
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-15
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(Long userId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();
        List<BackingDto> backingList = backingMapper.getBackingListUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "후원한 프로젝트 리스트 조회 성공", backingList));
    }

    /**
     * <p>로그인 사용자 후원 리스트 상세 Backing 통합</p>
     *
     * @param userId 사용자
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-05
     */

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(Long userId, Long projectId, Long rewardId, Long backingId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();
        if (projectMapper.findById(projectId) == null) throw new ProjectNotFoundException();

        BackingDto backingDetailDto = backingMapper.getBackingProjectAndUserId(userId, projectId, rewardId, backingId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "후원한 프로젝트 리스트 상세 조회성공", backingDetailDto));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> updateBacking(BackingRequestUpdateDto updateDto, Long backingId, Long userId) {
        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();
        if (backingMapper.findById(backingId) == null) throw new BackingNotFoundException();
        updateDto.setBackingId(backingId);
        updateDto.setUserId(backingId);

        if (updateDto.getNewAddress() == null && (updateDto.getBackingRewards() == null || updateDto.getBackingRewards().isEmpty())) {
            throw new IllegalArgumentException("수정할 값이 없습니다.");
        }

        backingMapper.updateBacking(updateDto);
        return ResponseEntity.ok(ResponseDto.success(200, "후원 수정 성공", null));
    }

    @Override
    public ResponseEntity<ResponseDto<List<MyPageBackingListDto>>> getMyPageBackingList(Long userId) {
        List<MyPageBackingListDto> backings= backingMapper.getBackingList(userId);
        List<MyPageBackingList_backingDetail> backingDetails = backingMapper.getMyPageDetailBackingList(userId);
        List<MyPageBackingList_reward> rewards=  rewardMapper.getMyPageDetailRewardList(userId);

        List<MyPageBackingListDto> list = backings.stream().toList();

        return null;
    }

}