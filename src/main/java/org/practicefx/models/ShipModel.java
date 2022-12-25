package org.practicefx.models;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.practicefx.CommonConstant;
import org.practicefx.MainController;
import org.practicefx.utils.HttpClientUtils;
import org.practicefx.utils.JsonUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * 远程获取 船舶对象属性  以后可以增加更多的 组件美化显示 
 * @author eron
 *
 */
public class ShipModel extends HBox implements Cloneable {

	private static final Logger LOGGER = Logger.getLogger(ShipModel.class.getName());
	
    private Long shipId; // 自动生成的id 
	private Long userId; // 用户ID @See User  **必须赋值** 
	private String name; // 船舶名称   **必须赋值** 
	private String mmsi; // mmsi Maritime Mobile Service Identify 水上移动通信业务标识码
	private String imoNumber; // IMO ship identification number  
	private String callNumber; // CALL SIGN,是国际海事组织IMO指定给每条船舶唯一的识别信号，CALL SIGN主要的作用就是在船舶海上联络、码头靠泊、信息报告的时候使用 
	private Integer type; // 船舶类型 
	private Integer electronicType; // 船舶电子设备类型 GPS AIS 等电子设备, router项目有编码 
	private Float draft; // 船舶吃水 m/dt 
	private LocalDateTime createTime; // 船舶创建时间 
	private LocalDateTime updateTime; // 船舶属性修改时间 
	
	private MainController mainController;
	
	@Deprecated
	public ShipModel() {  // 禁用
		this.userId = 0L; 
		this.name = "NULL";
		
	}
	
	public ShipModel(Long userId, String name) { 
		this.userId = userId;
		this.name = name;
		
	}
	
	public ShipModel(Builder builder) {
		this.userId = builder.userId;
		this.name = builder.name;
		this.mmsi = builder.mmsi;
		this.imoNumber = builder.imoNumber;
		this.callNumber = builder.callNumber;
		this.type = builder.type;
		this.electronicType = builder.electronicType;
		this.draft = builder.draft;
		
	}
	
	public void customeComponent(MainController mainController) {
		this.mainController = mainController;
		
		this.setOnMouseClicked(e -> {
			this.mainController.getTimer().stop();
			this.mainController.setShipId(this.shipId);
			
			CompletableFuture<HttpResponse<String>> tracksResponse = HttpClientUtils.asyncHttpGet(CommonConstant.API_PREFIX + "shiptracks/" + this.shipId);
			String tracksResponseBody = "";
			try {
				tracksResponseBody = tracksResponse.get(10, TimeUnit.SECONDS).body();
			} catch (InterruptedException | ExecutionException | TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			LOGGER.info("track body : " + tracksResponseBody);
			List<TrackView> tracks = JsonUtil.parseJsonArrayToTracks(JsonUtil.getJsonArray(tracksResponseBody, "data"));
			LOGGER.info("转换的tracks : " + tracks.toString());
			
			// 获取到轨迹点之后 mainontroller 调用Pane 绘制轨迹点 
			this.mainController.drawShipTracks(tracks);
		});
		
		this.styleProperty().bind(
				Bindings
                .when(hoverProperty())
                .then(new SimpleStringProperty("-fx-background-color: #CE5C00;"))
                .otherwise(new SimpleStringProperty("-fx-background-color: #F4F4F4;"))
        );
		
		// 设置按钮 
		Label nameLabel = new Label(this.name);
		nameLabel.setPrefWidth(200);
		nameLabel.setPrefHeight(50);
		
		Label idLabel = new Label(String.valueOf(this.shipId));
		idLabel.setPrefWidth(100);
		idLabel.setPrefHeight(50);
		
		this.getChildren().add(nameLabel);
		this.getChildren().add(idLabel);
	}
	
	/**
	 * 使用 Ship.createBuilder().build();
	 * @return Builder obj 
	 */
	public static Builder createBuilder() { 
		return new Builder();
	}
	
	public static class Builder {
		//private Long id; // 自动生成的id 
		private Long userId; // 用户ID @See User  **必须赋值** 
		private String name = "NULL"; // 船舶名称   **必须赋值** 
		private String mmsi = "NULL"; // mmsi Maritime Mobile Service Identify 水上移动通信业务标识码
		private String imoNumber = "NULL"; // IMO ship identification number  
		private String callNumber = "NULL"; // CALL SIGN,是国际海事组织IMO指定给每条船舶唯一的识别信号，CALL SIGN主要的作用就是在船舶海上联络、码头靠泊、信息报告的时候使用 
		private Integer type = 0; // 船舶类型 
		private Integer electronicType = 0; // 船舶电子设备类型 GPS AIS 等电子设备, router项目有编码 
		private Float draft = 0F; // 船舶吃水 m/dt 
		//private LocalDateTime createTime; // 船舶创建时间 
		//private LocalDateTime updateTime; // 船舶属性修改时间 
		public Builder() {}
		public Builder userId(Long userId) {
			this.userId = userId;
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder mmsi(String mmsi) {
			this.mmsi = mmsi;
			return this;
		}
		public Builder imoNumber(String imoNumber) {
			this.imoNumber = imoNumber;
			return this;
		}
		public Builder callNumber(String callNumber) {
			this.callNumber = callNumber;
			return this;
		}
		public Builder type(Integer type) {
			this.type = type;
			return this;
		}
		public Builder electronicType(Integer electronicType) {
			this.electronicType = electronicType;
			return this;
		}
		public Builder draft(Float draft) {
			this.draft = draft;
			return this;
		}
		
		public ShipModel build() {
			// 检查参数合法化 
			if (this.userId == null) {
				throw new IllegalArgumentException("userId of Ship is Required !");
			}
			return new ShipModel(this);
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	// id 和 updatetime不覆盖 
	public void overrideAttributes(ShipModel another) {
		if (another == null) {
			LOGGER.severe("another User is null !!!");
			return;
		}
		
		this.userId = another.userId;
		this.name = another.name;
		this.mmsi = another.mmsi;
		this.imoNumber = another.imoNumber;
		this.callNumber = another.callNumber;
		this.type = another.type;
		this.electronicType = another.electronicType;
		this.draft = another.draft;
		this.createTime = another.createTime;
	}

	/**
	 * pojo 通用 getter 和 setter 
	 * @return 
	 */
	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

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

	public String getMmsi() {
		return mmsi;
	}

	public void setMmsi(String mmsi) {
		this.mmsi = mmsi;
	}

	public String getImoNumber() {
		return imoNumber;
	}

	public void setImoNumber(String imoNumber) {
		this.imoNumber = imoNumber;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getElectronicType() {
		return electronicType;
	}

	public void setElectronicType(Integer electronicType) {
		this.electronicType = electronicType;
	}

	public Float getDraft() {
		return draft;
	}

	public void setDraft(Float draft) {
		this.draft = draft;
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
		return "Ship [shipId=" + shipId + ", userId=" + userId + ", name=" + name + ", mmsi=" + mmsi + ", imoNumber="
				+ imoNumber + ", callNumber=" + callNumber + ", type=" + type + ", electronicType=" + electronicType
				+ ", draft=" + draft + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}









