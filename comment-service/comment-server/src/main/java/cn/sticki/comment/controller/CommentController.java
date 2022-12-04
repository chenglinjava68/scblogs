package cn.sticki.comment.controller;

import cn.sticki.comment.pojo.Comment;
import cn.sticki.comment.pojo.CommentListVO;
import cn.sticki.comment.service.CommentService;
import cn.sticki.common.exception.BusinessException;
import cn.sticki.common.result.RestResult;
import cn.sticki.common.web.auth.AuthHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 评论相关接口
 *
 * @author 阿杆
 */
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

	@Resource
	private CommentService commentService;

	/**
	 * 新增评论
	 *
	 * @param comment 必须传入被评论的博客id和评论内容
	 */
	@PostMapping
	public RestResult<Object> createComment(@RequestBody Comment comment) {
		Integer id = AuthHelper.getCurrentUserIdOrExit();
		if (comment.getBlogId() == null || comment.getContent() == null) {
			throw new BusinessException();
		}
		comment.setUserId(id);
		commentService.create(comment);
		return new RestResult<>();
	}

	/**
	 * 删除评论
	 *
	 * @param id 评论id
	 */
	@DeleteMapping
	public RestResult<Object> deleteComment(@RequestParam Integer id) {
		Integer userId = AuthHelper.getCurrentUserIdOrExit();
		commentService.checkAndDelete(userId, id);
		return new RestResult<>();
	}

	/**
	 * 获取评论列表
	 *
	 * @param blogId 评论的博客id
	 */
	@GetMapping("/list")
	public RestResult<CommentListVO> getCommentList(@RequestParam Integer blogId, @RequestParam Integer page, @RequestParam(defaultValue = "3") Integer pageSize) {
		CommentListVO commentListVO = commentService.getList(blogId, page, pageSize);
		return new RestResult<>(commentListVO);
	}

}
