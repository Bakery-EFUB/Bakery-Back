package bakery.caker.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("cloud.aws")
public class S3Properties {
    private final BucketProperties s3;
    private final CredentialProperties credentials;


    @ConstructorBinding
    @Getter
    public static class BucketProperties {
        private final String bucket;

        public BucketProperties(String bucket) {
            this.bucket = bucket;
        }
    }

    @ConstructorBinding
    @Getter
    public static class CredentialProperties{
        private final String accessKey;
        private final String secretKey;

        public CredentialProperties(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }
    }
}
