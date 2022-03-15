package org.practicefx;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javafx.scene.layout.HBox;

public class UserModel extends HBox implements Cloneable {

	private static final Logger LOGGER = Logger.getLogger(UserModel.class.getName());

    private Long userId; // 自增主键id
	private String name; // 用户名称  **必须赋值**
	private String password; // 用户设定的密码 md5运算  **必须赋值**
	private String registEmail; // 注册邮箱  **必须赋值** 
	private LocalDateTime createTime; // 创建时间
	private LocalDateTime updateTime; // 最近一次修改属性的时间 
	
	@Deprecated 
	public UserModel() {
		// 默认值处理  一般情况下不使用
		this.name = "NULL";
		this.password = "NULL";
		this.registEmail = "NULL";
	}
	
	public UserModel(String name, String password, String registEmail) {
		this.name = name;
		this.password = password;
		this.registEmail = registEmail;
	}
	
	public UserModel(Builder builder) {
		this.name = builder.name;
		this.password = builder.password;
		this.registEmail = builder.registEmail;
		
		// 其他的不要给值，由数据库自动生成
	}
	
	public static Builder createBuilder() {
		return new Builder();
	}
	
	public static class Builder {  // 内建 
		
		//private Long id; // 自增主键id
		private String name = "NULL"; // 用户名称  **必须赋值**
		private String password = ""; // 用户设定的密码 md5运算  **必须赋值**
		private String registEmail; // 注册邮箱  **必须赋值** 
		//private LocalDateTime createTime; // 创建时间
		//private LocalDateTime updateTime; // 最近一次修改属性的时间 
		public Builder() {}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		public Builder registEmail(String registEmail) {
			this.registEmail = registEmail;
			return this;
		}
		
		public UserModel build() {
			// 必须数字和格式需要检查 
			if(this.password == null) {
				throw new IllegalArgumentException("password of User must required !");
			}
			if(this.registEmail == null) {
				throw new IllegalArgumentException("registEmail of User must required !");
			}
			return new UserModel(this);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	// id 和 updatetime 不覆盖, 其余属性覆盖 
	public UserModel overrideAttributes(UserModel another) {
		if (another == null) {
			LOGGER.severe("another User is null !!!");
			return this;
		}
		this.name = another.name;
		this.password = another.password;
		this.registEmail = another.registEmail;
		this.createTime = another.createTime;
		
		return this;
	}
	
	

	/**
	 * pojo 通用 getter 和 setter 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegistEmail() {
		return registEmail;
	}

	public void setRegistEmail(String registEmail) {
		this.registEmail = registEmail;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "[" 
				+ "userId: " + userId + ", name: " + name 
				+ ", password: " + password + ", registEmail: " + registEmail 
				+ ", createTime: " + createTime + ", updateTime: " + updateTime 
				+ "]";
	}
	
}
