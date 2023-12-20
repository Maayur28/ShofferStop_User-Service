package com.userservice.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pagination {
    private int page;
    private int pageSize;
}
