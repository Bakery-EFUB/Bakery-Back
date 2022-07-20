package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Sheet;
import bakery.caker.domain.Recomment;
import bakery.caker.dto.SheetDTO;
import bakery.caker.dto.SheetResponseDTO;
import bakery.caker.dto.SheetsResponseDTO;
import bakery.caker.repository.CommentRepository;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.SheetRepository;
import bakery.caker.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class SheetService {
    private final SheetRepository sheetRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //새로운 order 저장
    @Transactional
    public void addOrder(Long memberId, SheetDTO order, MultipartFile file){
        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).ifPresent(
                member -> {
                    S3Presigner presigner = ImageUploadService.createPresigner();
                    String fileName = null;

                    if (!file.isEmpty()){
                        fileName = makeFileName(file);
                        URL url = ImageUploadService.getS3UploadURL(presigner, this.bucket, fileName);
                        try {
                            ImageUploadService.UploadImage(url, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        presigner.close();
                    }

                    sheetRepository.save(order.toEntity(member, fileName));
                });
    }

    //order pickupDate 업데이트
    @Transactional
    public void modifyOrder(Long memberId, Long orderId, SheetDTO order){
        sheetRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updatePickup(order.getPickupDate());
                        sheetRepository.save(o);
                    }
                }
        );
    }

    //order 사용 완료
    @Transactional
    public void removeOrder(Long memberId, Long orderId){
        sheetRepository.findById(orderId).ifPresent(
                o -> {
                    if(o.getMember().getMemberId().equals(memberId)){
                        o.updateFinishedFlag();
                        sheetRepository.save(o);
                    }
                }
        );
    }

    //처리되지 않은 모든 order 읽어오기
    public SheetsResponseDTO findOrders(){
        List<Sheet> sheets = sheetRepository.findAllByFinishedFlag(false);
        List<SheetResponseDTO> sheetResponse = returnSheetResponse(sheets);

        return new SheetsResponseDTO(sheetResponse);
    }

    //처리되지 않은 location 별 order 읽어오기
    public SheetsResponseDTO findLocOrders(String locationGu, String locationDong){
        List<Sheet> sheets = sheetRepository.findAllByLocationGuAndLocationDongAndFinishedFlag(locationGu, locationDong, false);
        List<SheetResponseDTO> sheetResponse = returnSheetResponse(sheets);

        return SheetsResponseDTO.builder()
                .sheetResponseDTOs(sheetResponse)
                .build();
    }

    //특정 order 가져오기, 없는 경우 null return
    public SheetResponseDTO findOrder(Long orderId){
        AtomicReference<SheetResponseDTO> sheet = new AtomicReference<>();
        sheetRepository.findById(orderId).ifPresent(
                order -> sheet.set(new SheetResponseDTO(order, findImage(order.getSheetId()))));
        return sheet.get();
    }

    //최근 6개 order 보여주기
    public SheetsResponseDTO findOrdersByCreatedAt(){
        List<Sheet> sheetList = sheetRepository.findTop6ByFinishedFlagOrderByCreatedAtDesc(false);
        List<SheetResponseDTO> sheetResponse = returnSheetResponse(sheetList);

        return SheetsResponseDTO.builder()
                .sheetResponseDTOs(sheetResponse)
                .build();
    }

    //내가 댓글 단 제안서
    public SheetsResponseDTO findOrdersByComment(Long memberId){
        List<Comment> comments = new ArrayList<>(Collections.emptyList());
        List<Recomment> recomments = new ArrayList<>(Collections.emptyList());
        Set<Sheet> sheets = new HashSet<>();

        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).ifPresent(
                member -> {
                    comments.addAll(commentRepository.findAllByWriter(member));
                    recomments.addAll(recommentRepository.findAllByWriter(member));
                }
        );

        for(Comment comment : comments){
            sheets.add(comment.getSheet());
        }
        for(Recomment recomment : recomments){
            sheets.add(recomment.getComment().getSheet());
        }

        List<SheetResponseDTO> sheetResponse = returnSheetResponse(List.copyOf(sheets));

        return SheetsResponseDTO.builder()
                .sheetResponseDTOs(sheetResponse)
                .build();
    }

    //내가 작성한 제안서 조회
    public SheetsResponseDTO findMyOrders(Long memberId){

        List<SheetResponseDTO> sheetResponse = new ArrayList<>();

        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).ifPresent(
                member -> {
                    List<Sheet> sheets = sheetRepository.findAllByMember(member);
                    sheetResponse.addAll(returnSheetResponse(sheets));
                });

        return SheetsResponseDTO.builder()
                .sheetResponseDTOs(sheetResponse)
                .build();
    }

    //리스트로 orderResponse 돌려주기
    private List<SheetResponseDTO> returnSheetResponse(List<Sheet> sheets){
        List<SheetResponseDTO> sheetResponse = new ArrayList<>();

        for(Sheet sheet:sheets){
            sheetResponse.add(new SheetResponseDTO(sheet, findImage(sheet.getSheetId())));
        }

        return sheetResponse;
    }

    //파일 이름 생성
    public static String makeFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return "caker-sheet-" + date.format(new Date()) + contentType;
    }

    //이미지 접근 url 받아오기
    @Transactional
    public String findImage(Long orderId) {
        AtomicReference<String> url = new AtomicReference<>();

        sheetRepository.findById(orderId).ifPresent(
                sheet -> {
                    String image = sheet.getImage();
                    if(image != null){
                        S3Presigner presigner = ImageUploadService.createPresigner();

                        url.set(ImageUploadService.getS3DownloadURL(presigner, this.bucket, image));
                        presigner.close();
                    }
                }
        );
        return url.get();
    }
}