package com.amdose.base.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * @author Alaa Jawhar
 */
@Data
public class TestRequest {
    @NotNull
    @NotBlank
    private String s;

    @Size(min = 5, max = 50)
    @Pattern(regexp = "^[0-9a-zA-Z \\\\#@._\\/-]*$")
    @Email(message = "Email should be valid")
    private String email;

    @JsonIgnore
    private Internal internal = new Internal();

    @Data
    public class Internal {
        private String test;
    }
}
