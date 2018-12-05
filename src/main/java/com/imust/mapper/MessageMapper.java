package com.imust.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imust.entity.Message;
@Mapper
public interface MessageMapper {
	
	//添加信息
	@Insert("insert into message(content,createDate,user_id,user_name) values(#{content},SYSDATE(),#{user_id},#{user_name})")
    public void insertMessage(Message message);
	
	//删除信息
	@Delete("delete from message where id=#{id}")
	public void deleteMessage(int id);
	
	//查询信息
	@Select("select * from message where content like #{content}")
	List<Message> findByContent(@Param("content") String content);
	
	@Select("select * from message")
	List<Message> findAllMessage();
	@Select("select * from message where user_id=#{id}")
	List<Message> findMyMessage(@Param("id") int id);
	//修改信息
	@Update("update message set answer=#{answer},answerDate=SYSDATE(),admin_id=#{admin_id},admin_name=#{admin_name} where id=#{id}")
	public void updateMessage(Message message);
}
