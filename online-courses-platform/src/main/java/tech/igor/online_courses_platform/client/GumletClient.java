package tech.igor.online_courses_platform.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import tech.igor.online_courses_platform.client.dto.CreateAssetRequest;
import tech.igor.online_courses_platform.client.dto.CreateAssetResponse;
import tech.igor.online_courses_platform.client.dto.CreateCollectionDto;
import tech.igor.online_courses_platform.client.dto.CreateCollectionResponse;

@FeignClient(name = "GumletClient", url = "${gumlet.url}")
public interface GumletClient {

    @PostMapping(path = "/video/assets")
    ResponseEntity<CreateAssetResponse> createAsset(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CreateAssetRequest dto
    );

    @PostMapping(path = "/video/sources")
    ResponseEntity<CreateCollectionResponse> createCollection(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CreateCollectionDto dto
    );

}
