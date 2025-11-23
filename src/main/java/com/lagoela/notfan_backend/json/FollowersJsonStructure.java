package com.lagoela.notfan_backend.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowersJsonStructure {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class string_list_data {
        private String href;
        private String value;
        private Integer timestamp;
    }

    private List<string_list_data> string_list_data;
}
