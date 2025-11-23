package com.lagoela.notfan_backend.json;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowingModel {

    private String userNickname;
    private String profileLink;
}
