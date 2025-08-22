package tech.igor.online_courses_platform.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateAssetResponse(
        @JsonProperty("asset_id") String assetId,
        Output output
) {
    public record Output(
            @JsonProperty("playback_url") String playbackUrl
    ) {}
}

