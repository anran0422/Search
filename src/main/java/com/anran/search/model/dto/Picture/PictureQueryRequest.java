package com.anran.search.model.dto.Picture;


import com.anran.search.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String searchText;
}
