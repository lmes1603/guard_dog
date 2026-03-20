package rycfod.com.the_tracking_guard_dog.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Address used inside {@link CreateLabelRequest} for both ship_to and ship_from.
 */
@Data
@NoArgsConstructor
public class AddressDto {

    @NotBlank
    private String name;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String cityLocality;

    @NotBlank
    private String stateProvince;

    @NotBlank
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2, message = "must be a 2-letter ISO country code")
    private String countryCode;

    /** "yes" or "no" — defaults to "yes" for dropshipping destinations. */
    private String addressResidentialIndicator = "yes";

    private String phone;
}
