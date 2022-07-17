package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
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

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public MemberResponseDTO findSessionMember(Long memberId) {
        Member member = findMemberEntity(memberId);
        String imageUrl = findProfileImage(memberId);
        return new MemberResponseDTO(member, imageUrl);
    }

    @Transactional
    public MemberResponseDTO findMember(Long memberId) {
        Member member = findMemberEntity(memberId);
        String imageUrl = findProfileImage(memberId);
        return new MemberResponseDTO(member, imageUrl);
    }

    @Transactional
    public MemberResponseDTO modifySessionMember(Long memberId, String nickname, MultipartFile file) throws IOException {
        Member member = findMemberEntity(memberId);

        if(file!=null) {
            modifyMemberImage(memberId, file);
        }

        if(nickname!=null) {
            member.updateProfile(nickname);
        }

        String imageUrl = findProfileImage(memberId);
        return new MemberResponseDTO(member, imageUrl);
    }

    @Transactional
    public void modifyMemberImage(Long memberId, MultipartFile file) throws IOException {
        Member member = findMemberEntity(memberId);

        S3Presigner presigner = createPresigner();
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

        S3Presigner presigner = createPresigner();

        String url = ImageUploadService.getS3DownloadURL(presigner, this.bucket, fileName);
        presigner.close();
        return url;
    }

    @Transactional
    public String deleteSessionMember(Long memberId) {
        try {
            Member member = findMemberEntity(memberId);
            member.updateDeleteFlag();
            return "삭제완료";
        }catch(Exception e) {
            return "실패";
        }
    }

    public Member findMemberEntity(Long memberId) {
        return memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다 id= "+memberId));
    }

    public AwsBasicCredentials createCredentials() {
        return AwsBasicCredentials.create(this.accessKey,this.secretKey);
    }

    public S3Presigner createPresigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(createCredentials()))
                .build();
    }

    public static String makeFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-profile-" + date.format(new Date()) + contentType;
    }
}
