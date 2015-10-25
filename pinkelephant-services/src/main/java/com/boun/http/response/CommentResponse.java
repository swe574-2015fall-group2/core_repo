package com.boun.http.response;

import com.boun.data.cassandra.model.Comment;
import lombok.Data;

import java.util.List;


@Data
public class CommentResponse {
    List<Comment> comments;
    boolean isBlocked;
    String message;

}
