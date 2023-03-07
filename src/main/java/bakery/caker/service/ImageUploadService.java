package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    public static URL getS3UploadURL(S3Presigner presigner, String bucketName, String fileName) {
        try {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(checkContentType(fileName))
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) //링크가 유효한 시간
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            URL myURL = presignedRequest.url();
            System.out.println("Presigned URL to upload a file to: " +myURL.toString());
            System.out.println("Which HTTP method needs to be used when uploading a file: " +
                    presignedRequest.httpRequest().method());

            return myURL;

        } catch (S3Exception e) {
            throw new CustomException(ErrorCode.EXCEPTION, e.getMessage());
        }
    }

    public static void UploadImage(URL url, MultipartFile file) {
        try{
            String contentType = checkContentType(file.getOriginalFilename());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestMethod("PUT");
            connection.getOutputStream().write(file.getBytes());
            connection.getResponseCode();
            System.out.println("HTTP response code is " + connection.getResponseCode());
        }catch(IOException e) {
            throw new CustomException(ErrorCode.IO_EXCEPTION, e.getMessage());
        }
    }

    public static String getS3DownloadURL(S3Presigner presigner, String bucketName, String keyName) {

        try {
            GetObjectRequest getObjectRequest =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(keyName)
                            .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest =
                    presigner.presignGetObject(getObjectPresignRequest);

            //여기서 접근 url나옴
            String url = presignedGetObjectRequest.url().toString();
            System.out.println("Presigned URL: " + url);
            return url;
        } catch (S3Exception e) {
            throw new CustomException(ErrorCode.EXCEPTION, e.getMessage());
        }
    }

    public static AwsBasicCredentials createCredentials(String access, String secret) {
        return AwsBasicCredentials.create(access, secret);
    }

    public static S3Presigner createPresigner(String access, String secret) {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(createCredentials(access, secret)))
                .build();
    }

    public static String checkContentType(String fileName) {
        int extension = fileName.indexOf(".");
        String contentType = fileName.substring(extension);

        if(ObjectUtils.isEmpty(contentType)){
            return null;
        } else {
            if(contentType.contains("jpeg")) {
                return "image/jpeg";
            }
            else if(contentType.contains("jpg")) {
                return "image/jpg";
            }
            else if(contentType.contains("png")) {
                return "image/png";
            }
            else{
                return null;
            }
        }
    }

}
