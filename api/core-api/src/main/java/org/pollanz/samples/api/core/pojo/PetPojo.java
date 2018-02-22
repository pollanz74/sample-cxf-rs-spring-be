package org.pollanz.samples.api.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pollanz.samples.api.core.pojo.enumeration.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PetPojo {

    @ApiModelProperty(value = "")
    private Long id;

    @Size(max = 255)
    @ApiModelProperty(value = "")
    private String category;

    @NotNull
    @Size(min = 5, max = 255)
    @ApiModelProperty(example = "doggie", required = true, value = "")
    private String name;

    @Size(max = 255)
    @ApiModelProperty(value = "")
    private String tag;

    @NotNull
    @ApiModelProperty(value = "pet status in the store")
    private Status status;

}

