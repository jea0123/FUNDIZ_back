package com.example.funding.validator;

import com.example.funding.exception.notfound.*;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.example.funding.common.Preconditions.requireFound;

@Component
@RequiredArgsConstructor
public class Loaders {
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final RewardMapper rewardMapper;
    private final AddressMapper addressMapper;
    private final BackingMapper backingMapper;
    private final CreatorMapper creatorMapper;
    private final CommunityMapper communityMapper;
    private final InquiryMapper inquiryMapper;
    private final NoticeMapper noticeMapper;
    private final NotificationMapper notificationMapper;
    private final QnaMapper qnaMapper;
    private final AdminMapper adminMapper;

    /**
     * 사용자 로더
     * @param id 사용자 아이디
     * @return 사용자 객체
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우 발생
     */
    public User user(Long id) {
        return requireFound(Optional.ofNullable(userMapper.getUserById(id)), UserNotFoundException::new);
    }

    /**
     * 프로젝트 로더
     * @param id 프로젝트 아이디
     * @return 프로젝트 객체
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 경우 발생
     */
    public Project project(Long id) {
        return requireFound(Optional.ofNullable(projectMapper.findById(id)), ProjectNotFoundException::new);
    }

    /**
     * 리워드 로더
     * @param id 리워드 아이디
     * @return 리워드 객체
     * @throws RewardNotFoundException 리워드가 존재하지 않을 경우 발생
     */
    public Reward reward(Long id) {
        return requireFound(Optional.ofNullable(rewardMapper.findById(id)), RewardNotFoundException::new);
    }

    /**
     * 주소 로더
     * @param id 주소 아이디
     * @return 주소 객체
     * @throws AddressNotFoundException 주소가 존재하지 않을 경우 발생
     */
    public Address address(Long id) {
        return requireFound(Optional.ofNullable(addressMapper.getAddr(id)), AddressNotFoundException::new);
    }

    /**
     * 펀딩 로더
     * @param id 펀딩 아이디
     * @return 펀딩 객체
     * @throws BackingNotFoundException 펀딩이 존재하지 않을 경우 발생
     */
    public Backing backing(Long id) {
        return requireFound(Optional.ofNullable(backingMapper.findById(id)), BackingNotFoundException::new);
    }

    /**
     * 크리에이터 로더
     * @param id 크리에이터 아이디
     * @return 크리에이터 객체
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 경우 발생
     */
    public Creator creator(Long id) {
        return requireFound(Optional.ofNullable(creatorMapper.findById(id)), CreatorNotFoundException::new);
    }

    /**
     * 커뮤니티 로더
     * @param id 커뮤니티 아이디
     * @return 커뮤니티 객체
     * @throws CommunityNotFoundException 커뮤니티가 존재하지 않을 경우 발생
     */
    public Community community(Long id) {
        return requireFound(Optional.ofNullable(communityMapper.getCommunityById(id)), CommunityNotFoundException::new);
    }

    /**
     * 문의 로더
     * @param id 문의 아이디
     * @return 문의 객체
     * @throws InquiryNotFoundException 문의가 존재하지 않을 경우 발생
     */
    public Inquiry inquiry(Long id) {
        return requireFound(Optional.ofNullable(inquiryMapper.findById(id)), InquiryNotFoundException::new);
    }

    /**
     * 공지사항 로더
     * @param id 공지사항 아이디
     * @return 공지사항 객체
     * @throws NoticeNotFoundException 공지사항이 존재하지 않을 경우 발생
     */
    public Notice notice(Long id) {
        return requireFound(Optional.ofNullable(noticeMapper.noticeDetail(id)), NoticeNotFoundException::new);
    }

    /**
     * 알림 로더
     * @param id 알림 아이디
     * @return 알림 객체
     * @throws NotificationNotFoundException 알림이 존재하지 않을 경우 발생
     */
    public Notification notification(Long id) {
        return requireFound(Optional.ofNullable(notificationMapper.getNotificationById(id)), NotificationNotFoundException::new);
    }

    /**
     * QnA 로더
     * @param id QnA 아이디
     * @return QnA 객체
     * @throws QnANotFoundException QnA가 존재하지 않을 경우 발생
     */
    public Qna qna(Long id) {
        return requireFound(Optional.ofNullable(qnaMapper.findById(id)), QnANotFoundException::new);
    }

    /**
     * 관리자 로더
     * @param id 관리자 아이디
     * @return 관리자 객체
     * @throws AdminNotFoundException 관리자가 존재하지 않을 경우 발생
     */
    public Admin admin(String id) {
        return requireFound(Optional.ofNullable(adminMapper.getAdminByAdminId(id)), AdminNotFoundException::new);
    }
}
