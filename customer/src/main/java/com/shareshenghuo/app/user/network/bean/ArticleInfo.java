package com.shareshenghuo.app.user.network.bean;

import java.io.Serializable;

public class ArticleInfo implements Serializable {
	public int id;
	public int user_id;
	public int city_id;
	public String nick_name;
	public String user_photo;
	public String mobile;
	public String title;
	public String content;
	public String photo;		//多张 逗号隔开
	public int status;
	public int browse_count;	//浏览量
	public int user_like_count;	//点赞量
	public int is_like;			//用户是否点赞
	public int comment_count;	//评论数
	public int ranking;			//浏览量排名
	public long create_time;
	public int life_circle_count;	//userid不为空的时候  表示用户发表的文章总数
}
