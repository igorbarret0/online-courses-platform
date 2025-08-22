package tech.igor.online_courses_platform.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.igor.online_courses_platform.client.dto.CreateAssetRequest;
import tech.igor.online_courses_platform.client.dto.CreateAssetResponse;
import tech.igor.online_courses_platform.client.dto.CreateCollectionDto;
import tech.igor.online_courses_platform.client.dto.CreateCollectionResponse;

@Service
public class GumletService {

    private final GumletClient gumletClient;

    @Value("${gumlet.api.key}")
    private String gumletApiKey;

//    @Value("${gumlet.collection.id}")
//    private String collectionId;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.accessKeyId}")
    String accessKey;

    @Value("${aws.secretKey}")
    String secretKey;

    @Value("${cloud.aws.region.static}")
    String region;

    public GumletService(GumletClient gumletClient) {
        this.gumletClient = gumletClient;
    }

    public CreateAssetResponse uploadVideo(String videoUrl, String collectionId) {

        CreateAssetRequest dto = new CreateAssetRequest(
                videoUrl, collectionId, "MP4"
        );

        var response = gumletClient.createAsset(
                "Bearer " + gumletApiKey,
                dto
        );

        if (response.getStatusCode().is2xxSuccessful()) {
           return response.getBody();
        }

        return null;
    }

    public CreateCollectionResponse createCollection(
            String collectionName
    ) {

        var createCollection = new CreateCollectionDto(
                collectionName,
                "direct-upload",
                true,
                bucketName,
                accessKey,
                secretKey,
                region
        );

        var response =
                gumletClient.createCollection("Bearer " + gumletApiKey, createCollection);


        return new CreateCollectionResponse(response.getBody().id());

    }



}
