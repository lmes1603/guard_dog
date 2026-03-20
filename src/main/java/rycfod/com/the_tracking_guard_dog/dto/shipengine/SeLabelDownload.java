package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/** Download URLs for the generated label, as returned by ShipEngine. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeLabelDownload {

    /** Generic download URL. */
    @JsonProperty("href")
    private String href;

    @JsonProperty("pdf")
    private String pdf;

    @JsonProperty("zpl")
    private String zpl;

    @JsonProperty("png")
    private String png;
}
