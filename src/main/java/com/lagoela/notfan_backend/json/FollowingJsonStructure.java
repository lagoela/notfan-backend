package com.lagoela.notfan_backend.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowingJsonStructure {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class string_list_data {
        private String href;
        private Integer timestamp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class relationships_following {
        private String title;
        private List<string_list_data> string_list_data;
    }

    private List<relationships_following> relationships_following;
}
