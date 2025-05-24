package com.poly.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage extends Message{
	private int userid;
	private String text;
	 private Date time;  // Thời gian gửi tin nhắn

	    // Constructors

}
