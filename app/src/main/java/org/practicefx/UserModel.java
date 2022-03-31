package org.practicefx;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * 请求接口获取的用户对象 封装对象 
 * @author eron
 *
 */
public class UserModel extends HBox implements Cloneable {

	private static final Logger LOGGER = Logger.getLogger(UserModel.class.getName());

    private Long userId; // 自增主键id
	private String name; // 用户名称  **必须赋值**
	private String password; // 用户设定的密码 md5运算  **必须赋值**
	private String registEmail; // 注册邮箱  **必须赋值** 
	private LocalDateTime createTime; // 创建时间
	private LocalDateTime updateTime; // 最近一次修改属性的时间 
	
	private MainController mainController;
	
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
		
	}
	
	/**
	 * 传入船舶list的vbox 控制添加和
	 * @param shipVBox 
	 */
	public void customeComponent(MainController mainController) {
		this.mainController = mainController;
		
		this.setOnMouseClicked( e -> {
			this.mainController.getTimer().stop();
			// 清空绘制轨迹的面板   手动清除/在绘制图形时会自动清除图形轨迹 
			this.mainController.clearShipTracks();
			this.mainController.setUserId(this.userId);
			this.mainController.setShipId(-1L);
			
			// 请求 当前用户的船舶对象 
			CompletableFuture<HttpResponse<String>> shipsResponse = HttpClientUtils.asyncHttpGet(CommonConstant.API_PREFIX + "ships/" + this.userId);
			String shipsResponseBody = "";
			try {
				shipsResponseBody = shipsResponse.get(10, TimeUnit.SECONDS).body();
			} catch (InterruptedException | ExecutionException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<ShipModel> ships = JsonUtil.parseJsonArrayToShips(JsonUtil.getJsonArray(shipsResponseBody, "data"));
			
			LOGGER.info("from user < " + this.userId + " > \n" + " all ships : " + ships.toString());
			
			ships.forEach(ship -> {
				ship.customeComponent(this.mainController);
			});
			
			// 先清空之前的数据, 再重新显示 
			this.mainController.shipVBox.getChildren().clear();
			this.mainController.shipVBox.getChildren().addAll(ships);
			
		});
		
		this.styleProperty().bind(
				Bindings
                .when(hoverProperty())
                .then(new SimpleStringProperty("-fx-background-color: #43CD80;"))
                .otherwise(new SimpleStringProperty("-fx-background-color: #F4F4F4;"))
        );
				
		// 设置按钮 
		Label nameLabel = new Label(this.name);
		nameLabel.setGraphic(new FontIcon("fa-apple"));
		nameLabel.setPrefWidth(100);
		nameLabel.setPrefHeight(50);
		
		Label idLabel = new Label(String.valueOf(this.userId));
		idLabel.setPrefWidth(100);
		idLabel.setPrefHeight(50);
		
		this.getChildren().add(nameLabel);
		this.getChildren().add(idLabel);
		
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
