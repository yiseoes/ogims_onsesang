// src/main/java/com/semi/service/comment/impl/CommentServiceImpl.java
package com.semi.service.comment.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.semi.domain.Comment;
import com.semi.service.comment.CommentService;
import com.semi.service.comment.dao.CommentDao;

public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao = new CommentDao();

    @Override
    public long addComment(Comment comment) throws Exception {
        return commentDao.addComment(comment);
    }

    @Override
    public int updateComment(long commentId, String content) throws Exception {
        return commentDao.updateComment(commentId, content);
    }

    @Override
    public int deleteComment(long commentId) throws Exception {
        return commentDao.deleteComment(commentId);
    }
    
    @Override
    public Map<String, Object> getComments(long volunteerId, int page, int pageSize) throws Exception {
        int count = commentDao.countByVolunteer(volunteerId);
        List<Comment> list = Collections.emptyList();
        if (count > 0) {
            list = commentDao.selectPageByVolunteer(volunteerId, page, pageSize);
        }
        int totalPage = (count + pageSize - 1) / pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("totalPage", totalPage);
        return map;
    }

    @Override
    public List<Comment> getCommentList(long volunteerId, int page, int pageSize) throws Exception {
        return commentDao.selectPageByVolunteer(volunteerId, page, pageSize);
    }

    @Override
    public int getCommentCount(long volunteerId) throws Exception {
        return commentDao.countByVolunteer(volunteerId);
    }
}
