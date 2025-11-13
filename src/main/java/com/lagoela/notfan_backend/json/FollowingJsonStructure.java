package com.lagoela.notfan_backend.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowingJsonStructure {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class string_list_data {
        private String href;
        private Integer timestamp;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class relationships_following {
        private String title;
        private string_list_data string_list_data;
    }

    private relationships_following relationships_following;
}
