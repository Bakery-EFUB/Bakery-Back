package bakery.caker.service;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
import bakery.caker.repository.MemberRepository;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bakery.caker.dto.MemberResponseDTO.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public MemberProfileResponseDTO findSessionMember(Long memberId) {
        Member member = findMemberEntity(memberId);
        String imageUrl;
        if(member.getImage().contains("k.kakaocdn.net")) {
            imageUrl = member.getImage();
        }
        else {
            imageUrl = findProfileImage(memberId);
        }
        return new MemberProfileResponseDTO(member, imageUrl);
    }

    @Transactional
    public MemberResponseDTO findMember(Long memberId) {
        Member member = findMemberEntity(memberId);
        String imageUrl;
        if(member.getImage().contains("k.kakaocdn.net")) {
            imageUrl = member.getImage();
        }
        else {
            imageUrl = findProfileImage(memberId);
        }
        return new MemberResponseDTO(member, imageUrl);
    }

    @Transactional
    public MemberResponseDTO modifySessionMember(Long memberId, String nickname, MultipartFile file) {
        Member member = findMemberEntity(memberId);

        if(file!=null) modifyMemberImage(memberId, file);
        if(nickname!=null) member.updateProfile(nickname);

        String imageUrl = findProfileImage(memberId);
        return new MemberResponseDTO(member, imageUrl);
    }

    @Transactional
    public void modifyMemberImage(Long memberId, MultipartFile file) {
        Member member = findMemberEntity(memberId);

        S3Presigner presigner = ImageUploadService.createPresigner();
        String fileName = makeFileName(file);

        URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);

        ImageUploadService.UploadImage(url, file);
        presigner.close();

        member.updateImage(fileName);
    }

    @Transactional
    public String findProfileImage(Long memberId) {
        Member member = findMemberEntity(memberId);
        String fileName = member.getImage();

        S3Presigner presigner = ImageUploadService.createPresigner();

        String url = ImageUploadService.getS3DownloadURL(presigner, this.bucket, fileName);
        presigner.close();
        return url;
    }

    @Transactional
    public String deleteSessionMember(Long memberId) {
        Member member = findMemberEntity(memberId);
        member.updateDeleteFlag();
        return "?????? ??????";
    }

    @Transactional
    public SessionUserDTO modifyRole(Long memberId) {
        Member member = findMemberEntity(memberId);
        member.updateAuthority(Authority.TRAINEE);
        return new SessionUserDTO(member);
    }

    public Member findMemberEntity(Long memberId) {
        return memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, null));
    }

    public String makeFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-profile-" + date.format(new Date()) + contentType;
    }
}
