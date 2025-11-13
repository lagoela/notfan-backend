package com.lagoela.notfan_backend.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowingNotFollowingModel {

    private String userNickname;
    private String profileLink;
}
