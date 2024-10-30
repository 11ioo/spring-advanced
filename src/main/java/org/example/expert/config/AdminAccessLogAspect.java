package org.example.expert.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminAccessLogAspect {

  private static final Logger logger = LoggerFactory.getLogger(AdminAccessLogAspect.class);

  // CommentAdminController의 deleteComment 메소드에 대한 접근 로그
  @After("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..))")
  public void logDeleteCommentAccess(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Long commentId = (Long) args[0];
    logger.info("Admin accessed deleteComment with commentId: {}", commentId);
  }

  // UserAdminController의 changeUserRole 메소드에 대한 접근 로그
  @After("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
  public void logChangeUserRoleAccess(JoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();
    Long userId = (Long) args[0];
    logger.info("Admin accessed changeUserRole with userId: {}", userId);
  }
}