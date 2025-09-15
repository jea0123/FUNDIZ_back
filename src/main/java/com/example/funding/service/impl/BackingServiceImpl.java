package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import com.example.funding.dto.response.Backing.BackingRewardDto;
import com.example.funding.dto.response.address.AddressResponseDto;
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

    @Override
    public ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId) {
        User user = userMapper.getUserById(userId);
        Project project = projectMapper.findById(projectId);
        List<AddressResponseDto> addressList = addressMapper.getAddressList(userId);
        List<Reward> rewardList = rewardMapper.getRewardList(projectId);
        Creator creator =creatorMapper.findById(project.getCreatorId());

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원 페이지를 찾을 수 없습니다."));
        }
        if(project == null){
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
        return ResponseEntity.ok(ResponseDto.success(200, "후원 페이지 추가 성공", "추가!!"));
    }
}
