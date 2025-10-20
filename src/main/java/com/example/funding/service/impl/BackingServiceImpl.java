package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.request.reward.RewardBackingRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import com.example.funding.dto.response.backing.userList_detail.*;
import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.enums.NotificationType;
import com.example.funding.exception.notfound.BackingNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import com.example.funding.service.BackingService;
import com.example.funding.validator.Loaders;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BackingServiceImpl implements BackingService {
    private final ProjectMapper projectMapper;
    private final BackingMapper backingMapper;
    private final AddressMapper addressMapper;
    private final RewardMapper rewardMapper;
    private final CreatorMapper creatorMapper;
    private final PaymentMapper paymentMapper;
    private final ShippingMapper shippingMapper;
    private final Loaders loaders;

    private final NotificationPublisher notificationPublisher;

    @Override
    public ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId) {
        User user = loaders.user(userId);
        Project project = loaders.project(projectId);

        List<AddressResponseDto> addressList = addressMapper.getAddressList(userId);
        List<Reward> rewardList = rewardMapper.getRewardListPublic(projectId);
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
                .currAmount(project.getCurrAmount())
                .goalAmount(project.getGoalAmount())
                .backingPagePaymentList(backingPagePayment)
                .build();

        return ResponseEntity.ok(ResponseDto.success(200, "후원페이지 출력 성공", backingResponse));
    }


    //TODO : 후원 하기 (생성)
    @Override
    @Transactional
    public ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto) {
        loaders.user(userId);
        requestDto.getRewards().forEach(reward -> {
            Reward existingReward = loaders.reward(reward.getRewardId());
            if (existingReward.getRemain() < reward.getQuantity()) {
                throw new IllegalArgumentException("리워드의 남은 수량이 부족합니다. 리워드 ID: " + reward.getRewardId());
            }
        });
        Backing backing = requestDto.getBacking();
        Payment payment = requestDto.getPayment();
        Address address = requestDto.getAddress();
        List<RewardBackingRequestDto> rewards = requestDto.getRewards();

        //backing
        backing.setUserId(userId);
        backing.setCreatedAt(LocalDateTime.now());
        backingMapper.addBacking(backing);

        Long backingId = backing.getBackingId();

        Long projectId = null;
        if (rewards != null && !rewards.isEmpty()) {
            //프로젝트 id 찾기 (첫번째 리워드 id 를 검색해서 역으로 찾는 로직)
            projectId = rewardMapper.findProjectIdByRewardId(rewards.getFirst().getRewardId());
        }

        if (projectId != null && backing.getAmount() != null) {
            //프로젝트 id 받아서 currAmount 에 더함
            projectMapper.increaseProjectCurrAmount(projectId, backing.getAmount());
            //프로젝트의 상태변경 ( open 에서 currAmount가 goalAmount 를넘겼을때)
            //projectMapper.updateProjectStatusGoal(projectId);
        }

        // backingDetail
        if (rewards != null && !rewards.isEmpty()) {
            for (RewardBackingRequestDto reward : rewards) {
                BackingDetail detail = new BackingDetail();
                detail.setBackingId(backingId);
                detail.setRewardId(reward.getRewardId());
                detail.setQuantity(reward.getQuantity());
                detail.setPrice(reward.getPrice());
                backingMapper.addBackingDetail(detail);
                // 남은개수 빼는 로직
                rewardMapper.decreaseRewardRemain(detail.getRewardId(), reward.getQuantity());
            }
            //출력용
            rewards.forEach(r -> System.out.println("  - rewardId: " + r.getRewardId() + ", qty: " + r.getQuantity()));
        }

        //payment
        Payment findPayment = paymentMapper.findUserMethod(userId, payment.getMethod(), payment.getCardCompany());
        System.out.println(" findPayment 확인용 - findPayment: " + findPayment);
        if (findPayment != null) {
            findPayment.setBackingId(backingId);
            findPayment.setAmount(backing.getAmount());
            paymentMapper.updateBackingPayment(findPayment);
        } else {
            String orderId = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            payment.setOrderId(orderId);
            payment.setBackingId(backingId);
            paymentMapper.addPayment(payment);
        }

        //TODO: address새로 생성시 로직추가 ,address 선택시 해당 address 만 연결
        // 신규 주소 직접 입력한 경우
        Long addrId = null;
        if (address != null && address.getAddrId() != null) {
            // 기존 주소 선택한 경우 
            addrId = address.getAddrId();
        } else {
            // 주소 직접 입력
            //addressMapper.addTempAddr(address);
        }

        // insert (shipping직접 생성)
        if (address != null && address.getAddrId() != null) {
            Shipping shipping = new Shipping();
            shipping.setBackingId(backingId);
            shipping.setAddrId(addrId);
            shippingMapper.addShipping(shipping);
        }

        notificationPublisher.publish(userId, NotificationType.BACKING_SUCCESS, "후원하신 프로젝트", backingId);
        return ResponseEntity.ok(ResponseDto.success(200, "후원 추가 성공", null));
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<String>> cancelBacking(Long userId, Long backingId) {
        Backing backing = loaders.backing(backingId);

        List<BackingDetail> rewardDetails = rewardMapper.findRewardByBackingId(backingId);
        if (rewardDetails == null || rewardDetails.isEmpty()) {
            throw new BackingNotFoundException();
        }
        //프로젝트 id 찾기
        Long projectId = rewardMapper.findProjectIdByRewardId(rewardDetails.getFirst().getRewardId());
        if (projectId == null) throw new ProjectNotFoundException();

        // 프로젝트 총 후원금 - 개인의 총 후원금
        projectMapper.decreaseProjectCurrAmount(projectId, backing.getAmount());

        // 후원 취소 -> 남은개수를 개수만큼 돌리기
        for (BackingDetail detail : rewardDetails) {
            rewardMapper.increaseRewardRemain(detail.getBackingId(), detail.getQuantity());
        }

        // 프로젝트의 상태를 확인 후 바꾸기 success -> open
        //projectMapper.updateProjectStatusBelowGoal(projectId);

        // 개인 후원 상태를 확인 후 바꾸기 completed -> canceled
        backingMapper.updateBackingStatus(backing.getBackingId());

        // 후원자가 취소했을 때 shippingStatus 바꾸기 어떤상태던 상관없이 CANCELED 로
        shippingMapper.updateBackingToShippingStatus(backing.getBackingId());

        return ResponseEntity.ok(ResponseDto.success(200, "후원 취소 성공", null));
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
        loaders.user(userId);
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
        loaders.user(userId);
        loaders.project(projectId);
        loaders.reward(rewardId);
        loaders.backing(backingId);

        BackingDto backingDetailDto = backingMapper.getBackingProjectAndUserId(userId, projectId, rewardId, backingId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "후원한 프로젝트 리스트 상세 조회성공", backingDetailDto));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> updateBacking(BackingRequestUpdateDto updateDto, @NotBlank Long backingId, @NotBlank Long userId) {
        loaders.user(userId);
        loaders.backing(backingId);
        updateDto.setBackingId(backingId);
        updateDto.setUserId(backingId);

        if (updateDto.getNewAddress() == null && (updateDto.getBackingRewards() == null || updateDto.getBackingRewards().isEmpty())) {
            throw new IllegalArgumentException("수정할 값이 없습니다.");
        }

        backingMapper.updateBacking(updateDto);
        return ResponseEntity.ok(ResponseDto.success(200, "후원 수정 성공", null));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<MyPageBackingListDto>>> getMyPageBackingList(Long userId) {
        loaders.user(userId);
        List<MyPageBackingSaveDto> backingLists = backingMapper.getBackingLists(userId);

        Map<Long, MyPageBackingListDto> grouped = new LinkedHashMap<>();

        for (MyPageBackingSaveDto list : backingLists) {
            MyPageBackingListDto myPageBackingList = grouped.computeIfAbsent(list.getBackingId(), id -> {
                MyPageBackingListDto newList = new MyPageBackingListDto();
                newList.setProjectId(list.getProjectId());
                newList.setTitle(list.getTitle());
                newList.setGoalAmount(list.getGoalAmount());
                newList.setCurrAmount(list.getCurrAmount());
                newList.setEndDate(list.getEndDate());
                newList.setThumbnail(list.getThumbnail());

                newList.setUserId(list.getUserId());
                newList.setBackingId(list.getBackingId());
                newList.setAmount(list.getAmount());
                newList.setCreatedAt(list.getCreatedAt());
                newList.setBackingStatus(list.getBackingStatus());
                newList.setShippingStatus(list.getShippingStatus());
                newList.setCreatorName(list.getCreatorName());
                return newList;
            });

            MyPageBacking_RewardDto reward = new MyPageBacking_RewardDto();
            reward.setRewardId(list.getRewardId());
            reward.setProjectId(list.getProjectId());
            reward.setRewardName(list.getRewardName());
            reward.setPrice(list.getPrice());
            reward.setDeliveryDate(list.getDeliveryDate());
            reward.setQuantity(list.getQuantity());
            myPageBackingList.getMpBackingList().add(reward);
        }

        List<MyPageBackingListDto> result = new ArrayList<>(grouped.values());
        return ResponseEntity.ok(ResponseDto.success(200, "후원내역 조회성공", result));
    }

    @Override
    public ResponseEntity<ResponseDto<List<MyPageBackingDetailDto>>> getMyPageBackingDetail(Long userId) {
        loaders.user(userId);
        List<MyPageBackingDetailSaveDto> backingDetils = backingMapper.getBackingDetails(userId);

        Map<Long, MyPageBackingDetailDto> grouped = new LinkedHashMap<>();

        for (MyPageBackingDetailSaveDto list : backingDetils) {
            MyPageBackingDetailDto backingDetail = grouped.computeIfAbsent(list.getBackingId(), id -> {
                MyPageBackingDetailDto newDetail = new MyPageBackingDetailDto();
                newDetail.setBackingId(list.getBackingId());
                newDetail.setUserId(list.getUserId());
                newDetail.setAmount(list.getAmount());
                newDetail.setCreatedAt(list.getCreatedAt());
                newDetail.setBackingStatus(list.getBackingStatus());
                newDetail.setMethod(list.getMethod());
                newDetail.setCardCompany(list.getCardCompany());
                newDetail.setShippingStatus(list.getShippingStatus());
                newDetail.setTrackingNum(list.getTrackingNum());
                newDetail.setShippedAt(list.getShippedAt());
                newDetail.setDeliveredAt(list.getDeliveredAt());
                newDetail.setTitle(list.getTitle());
                newDetail.setThumbnail(list.getThumbnail());
                newDetail.setAddrName(list.getAddrName());
                newDetail.setRecipient(list.getRecipient());
                newDetail.setPostalCode(list.getPostalCode());
                newDetail.setRoadAddr(list.getRoadAddr());
                newDetail.setDetailAddr(list.getDetailAddr());
                newDetail.setRecipientPhone(list.getRecipientPhone());
                newDetail.setCreatorName(list.getCreatorName());
                return newDetail;
            });

            MyPageBacking_RewardDto rewards = new MyPageBacking_RewardDto();
            rewards.setRewardId(list.getRewardId());
            rewards.setProjectId(list.getProjectId());
            rewards.setRewardName(list.getRewardName());
            rewards.setPrice(list.getPrice());
            rewards.setDeliveryDate(list.getDeliveryDate());
            rewards.setQuantity(list.getQuantity());
            backingDetail.getRewards().add(rewards);
        }

        List<MyPageBackingDetailDto> result = new ArrayList<>(grouped.values());

        return ResponseEntity.ok(ResponseDto.success(200, "후원 내역 상세 조회 성공", result));
    }
}