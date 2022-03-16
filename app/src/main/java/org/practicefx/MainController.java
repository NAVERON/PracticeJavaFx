package org.practicefx;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 顶层控制器 实现主要的事件触发动作 
 * @author eron
 *
 */
public class MainController implements Initializable {
	
	private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
	
	@FXML
	private BorderPane root;  // 界面外框 
	@FXML
	public VBox userVBox;  // 用户list
	@FXML
	public VBox shipVBox;  // 用户所属的ship list
	@FXML 
	private HBox controlsHBox;  // 按钮摆放位置 
	@FXML 
	private Pane shipTracksCanvas;  // 绘制船舶图形的位置 
	
	@FXML 
	private Button runButton;  // 运行船舶产生轨迹 
	@FXML 
	private Button stopButton;  // 停止船舶运动 
	@FXML 
	private Button saveTracksButton;  // 保存船舶轨迹到服务端
	@FXML 
	private Button clearTracksButton;  // 清除当前产生的新轨迹点 
	
	// 自定义临时变量  图形绘制准备 
	//private Group usersGroup = new Group();
	//private Group shipsGroup = new Group();
	private Group tracksGroup = new Group();
	private Group newTracksGroup = new Group();
	
	// 设置当前进入的用户和船舶id  控制轨迹点记忆按钮的动作 
	private Long userId = -1L;
	private Long shipId = -1L;
	
	// 随机点生成定时器
	private AnimationTimer timer;
	
	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// 请求获取所有用户数据   初始化组件 
		// 1 请求所有用户 创建用户Hbox 并添加到 userVBox 
		initialUserInformation();
		
		this.shipTracksCanvas.getChildren().add(this.tracksGroup);
		this.shipTracksCanvas.getChildren().add(this.newTracksGroup);
		
		timer = new AnimationTimer() {
			private long lastUpdate = 0L;
			@Override
			public void handle(long now) {  // The timestamp of the current frame given in nanoseconds
				if (now - lastUpdate >= 1000_000_000) {
					draw2DPoint();
                    lastUpdate = now ;
                }
				
			}
		};
	}
	
	public void initialUserInformation() {
		// 请求所有用户 并加入组件 
		HttpResponse<String> usersResponse = HttpClientUtils.httpGet(CommonConstant.API_PREFIX + "users");
		String usersResponseBody = usersResponse.body();
		LOGGER.info("用户json : " + usersResponseBody);
		// 从json中解析用户对象 
		List<UserModel> users = JsonUtil.parseJsonArrayToUsers(JsonUtil.getJsonArray(usersResponseBody, "data"));
		
		users.forEach(user -> {
			user.customeComponent(this);
		});
		
		this.userVBox.getChildren().addAll(users);
	}
	public void draw2DPoint() {
		Point2D newPoint = CommonConstant.randomGenerate2DPoint();
		this.newTracksGroup.getChildren().add(new Circle(newPoint.getX(), newPoint.getY(), 5, Color.DARKGREEN));
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserId() {
		return this.userId;
	}
	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}
	public Long getShipId() {
		return this.shipId;
	}
	public AnimationTimer getTimer() {
		return this.timer;
	}

	@FXML
	public void trackRun(MouseEvent event) {
		LOGGER.info("runButton click - ");

		timer.start();
	}
	@FXML
	public void trackStop(MouseEvent event) {
		LOGGER.info("stopButton click - ");
		
		timer.stop();
	}
	@FXML
	public void trackSave(MouseEvent event) {
		LOGGER.info("saveTrackButton click - ");
		
		LOGGER.info("假装存储了船舶轨迹, 因为船舶轨迹需要航速航向等信息, 当前只作为测试, 联系到相关的内容即可 ");
		
		LOGGER.info("当前轨迹点 : \n" + this.newTracksGroup.getChildren().toString());
	}
	@FXML
	public void trackClear(MouseEvent event) {
		LOGGER.info("clear Button click - ");
		
		this.newTracksGroup.getChildren().clear();
	}
	
	
	// 绘制轨迹点的检查 
	public Boolean trackGenerateEnvCheck() {
		if( this.userId < 0 || this.shipId < 0 ) {
			LOGGER.warning("not ste user infomation and ship information !!! ");
			return false;
		}
		
		return true;
	}
	
	@FXML
	public void canvasClickStart(MouseEvent event) {
		if( !trackGenerateEnvCheck() ) {
			return;
		}
		
		LOGGER.info("event type - " + event.getEventType());
		Double startX = event.getX();
		Double startY = event.getY();
		
		LOGGER.info("mouse Clicked Start - " + startX + ", " + startY);

	}
	@FXML 
	public void canvasDraggedMoving(MouseEvent event) {
		if( !trackGenerateEnvCheck() ) {
			return;
		}
		
		LOGGER.info("mouse event type : " + event.getEventType());
		Double movingX = event.getX();
		Double movingY = event.getY();
		
		LOGGER.info("mouse Dragging event, moving - " + movingX + ", " + movingY);
	}
	@FXML 
	public void canvasReleaseStop(MouseEvent event) {
		if( !trackGenerateEnvCheck() ) {
			return;
		}
		
		LOGGER.info("mouse event type : " + event.getEventType());
		Double releaseX = event.getX();
		Double releaseY = event.getY();
		LOGGER.info("mouse canvas released - " + releaseX + ", " + releaseY);
	}

	
	public void drawShipTracks(List<TrackView> tracks) {
		if( !trackGenerateEnvCheck() ) {
			return;
		}
		this.clearShipTracks();
		
		tracks.forEach(track -> {
			tracksGroup.getChildren().add(new Circle(track.getX(), track.getY(), 5, Color.BLUE));
		});
		
	}
	public void clearShipTracks() {
		this.tracksGroup.getChildren().clear();
		this.newTracksGroup.getChildren().clear();
	}
}











