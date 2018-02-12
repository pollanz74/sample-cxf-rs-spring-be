package org.pollanz.samples.api.core.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pollanz.samples.api.core.pojo.enumeration.Status;

import java.util.ArrayList;
import java.util.List;

@Data
public class PetPojo {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "")
    private String category;

    @ApiModelProperty(example = "doggie", required = true, value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String tag;

    @ApiModelProperty(value = "pet status in the store")
    private Status status;

}

