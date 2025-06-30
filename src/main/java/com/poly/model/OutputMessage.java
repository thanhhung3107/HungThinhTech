package com.poly.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage extends Message {
    private int userid;
    private String text;
    private Date time;  // Thời gian gửi tin nhắn

    // Constructors

}
