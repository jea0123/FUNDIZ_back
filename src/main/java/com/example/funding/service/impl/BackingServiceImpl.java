package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import com.example.funding.service.BackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId) {
        User user = userMapper.getUserById(userId);
        Project project = projectMapper.findById(projectId);
        List<AddressResponseDto> addressList = addressMapper.getAddressList(userId);
        List<Reward> rewardList = rewardMapper.getRewardList(projectId);
        Creator creator =creatorMapper.findById(project.getCreatorId());
        //List<BackingPagePaymentDto>

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원 페이지를 찾을 수 없습니다."));
        }

        List<BackingRewardDto> rewards = rewardList.stream()
                .map(r->BackingRewardDto.builder()
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
                .build();

        return ResponseEntity.ok(ResponseDto.success(200,"후원페이지 출력 성공", backingResponse));
    }


    @Override
    public ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto) {
        BackingRequestDto backingRequest= BackingRequestDto.builder()
                .userId(userId)
                .backingRewardList(requestDto.getBackingRewardList())
                .addrId(requestDto.getAddrId())
                .newAddress(requestDto.getNewAddress())
                .build();
        return ResponseEntity.ok(ResponseDto.success(200, "후원 추가 성공", /*"추가!!"*/ backingRequest.toString()));
    }

    /**
     * <p>로그인 사용자 후원 리스트</p>
     * @param userId 사용자 Backing으로 통합
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-09-15
     * @author by: 이윤기
     */
    @Override
    public ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(Long userId) {

        User user = userMapper.getUserById(userId);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원한 프로젝트 목록을 찾을 수 없습니다."));
        }
        List<BackingDto> backingList = backingMapper.getBackingListUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "후원한 프로젝트 리스트 조회 성공",backingList));
    }

    /**
     * <p>로그인 사용자 후원 리스트 상세 Backing 통합</p>
     * @param userId 사용자
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-09-05
     * @author by: 이윤기
     */

    @Override
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(Long userId, Long projectId, Long rewardId){
        User user = userMapper.getUserById(userId);
        Project project = projectMapper.findById(projectId);


        if(user == null || project == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원한 해당 프로젝트를 찾을 수 없습니다."));
        }

        BackingDto backingDetailDto = backingMapper.getBackingProjectAndUserId(userId, projectId, rewardId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "후원한 프로젝트 리스트 상세 조회성공", backingDetailDto));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> updateBacking(BackingRequestUpdateDto updateDto, Long backingId, Long userId) {
        updateDto.setBackingId(backingId);
        updateDto.setUserId(backingId);

        if(updateDto.getNewAddress() ==null && (updateDto.getBackingRewards() == null || updateDto.getBackingRewards().isEmpty())){
            throw new IllegalArgumentException("수정할 값이 없습니다.");
        }

        int result = backingMapper.updateBacking(updateDto);
        if (result == 0) {
            throw  new IllegalArgumentException("후원 수정 실패");
        }
        return ResponseEntity.ok(ResponseDto.success(200, "후원 수정 성공","이런들어떠하리"));

    }

    @Override
    public ResponseEntity<ResponseDto<String>> deleteBacking(Long backingId, Long userId) {
        int delete =backingMapper.deleteBacking(backingId, userId);

        return ResponseEntity.ok((ResponseDto.success(200, "후원내용 삭제",null)));
    }
}