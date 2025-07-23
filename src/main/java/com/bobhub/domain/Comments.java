package com.bobhub.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comments {
    private long id;
    private long partyId;
    private long writerId;
    private String comments;
    Date createdAt;
}
