package models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityRequest {

    @JsonProperty("addition")
    private Addition addition;

    @JsonProperty("important_numbers")
    private List<Integer> importantNumbers;

    @JsonProperty("title")
    private String title;

    @JsonProperty("verified")
    private Boolean verified;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Addition {

        @JsonProperty("additional_info")
        private String additionalInfo;

        @JsonProperty("additional_number")
        private Integer additionalNumber;
    }

    private EntityRequest(EntityRequest entityRequest) {
        this.addition = entityRequest.addition;
        this.importantNumbers = entityRequest.getImportantNumbers();
        this.title = entityRequest.getTitle();
        this.verified = entityRequest.getVerified();
    }

    public EntityRequest cloneEntity() {
        return new EntityRequest(this);
    }
}
