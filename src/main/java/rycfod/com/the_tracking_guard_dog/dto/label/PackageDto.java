package rycfod.com.the_tracking_guard_dog.dto.label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Minimal package descriptor for label creation requests. */
@Data
@NoArgsConstructor
public class PackageDto {

    @Positive
    private double weightValue;

    /** "ounce", "pound", "gram", or "kilogram". */
    @NotBlank
    private String weightUnit;

    @Positive
    private double length;

    @Positive
    private double width;

    @Positive
    private double height;

    /** "inch" or "centimeter". */
    @NotBlank
    private String dimensionUnit;
}
