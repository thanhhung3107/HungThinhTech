package com.poly.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	    private Integer senderId;
	    private Integer receiverId;
	    private String text;
	    private Date timestamp;
}
