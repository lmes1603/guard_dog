package rycfod.com.the_tracking_guard_dog.dto.shipengine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Address object used in both ship_to and ship_from within a ShipEngine shipment.
 */
@Data
@Builder
public class SeAddress {

    @JsonProperty("name")
    private String name;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("address_line1")
    private String addressLine1;

    @JsonProperty("address_line2")
    private String addressLine2;

    @JsonProperty("city_locality")
    private String cityLocality;

    @JsonProperty("state_province")
    private String stateProvince;

    @JsonProperty("postal_code")
    private String postalCode;

    /** ISO 3166-1 alpha-2, e.g. "US", "MX". */
    @JsonProperty("country_code")
    private String countryCode;

    /** "yes" or "no" — important for residential surcharges. */
    @JsonProperty("address_residential_indicator")
    private String addressResidentialIndicator;

    @JsonProperty("phone")
    private String phone;
}
