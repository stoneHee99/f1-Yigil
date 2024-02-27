package kr.co.yigil.member.application;

import kr.co.yigil.file.FileUploader;
import kr.co.yigil.member.domain.MemberCommand;
import kr.co.yigil.member.domain.MemberCommand.VisibilityChangeRequest;
import kr.co.yigil.member.domain.MemberInfo;
import kr.co.yigil.member.domain.MemberInfo.FollowingResponse;
import kr.co.yigil.member.domain.MemberInfo.VisibilityChangeResponse;
import kr.co.yigil.member.domain.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final FileUploader fileUploader;

    public MemberInfo.Main getMemberInfo(final Long memberId) {
        return memberService.retrieveMemberInfo(memberId);
    }

    public MemberInfo.MemberUpdateResponse updateMemberInfo(final Long memberId,
        MemberCommand.MemberUpdateRequest request) {

        boolean fileChanged = memberService.updateMemberInfo(memberId, request);
        if(fileChanged) fileUploader.upload(request.getProfileImageFile());
        return new MemberInfo.MemberUpdateResponse("회원 정보 업데이트 성공");
    }

    public MemberInfo.MemberDeleteResponse withdraw(final Long memberId) {
        memberService.withdrawal(memberId);
        return new MemberInfo.MemberDeleteResponse("회원 탈퇴 성공");
    }

    public MemberInfo.FollowerResponse getFollowerList(final Long memberId, Pageable pageable) {
        return memberService.getFollowerList(memberId, pageable);
    }

    public FollowingResponse getFollowingList(final Long memberId, Pageable pageable) {
        return memberService.getFollowingList(memberId, pageable);
    }

    public VisibilityChangeResponse setTravelsVisibility(Long memberId,
        VisibilityChangeRequest memberCommand) {
        return memberService.setTravelsVisibility(memberId, memberCommand);
    }
}