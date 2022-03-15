package org.practicefx;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	
	private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
	
	@FXML
	private BorderPane root;  // 界面外框 
	@FXML
	private VBox userVBox;  // 用户list
	@FXML
	private VBox shipVBox;  // 用户所属的ship list
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
	private Group usersGroup = new Group();
	private Group shipsGroup = new Group();
	private Group tracksGroup = new Group();
	
	// url common 
	private static String apiPrefix = "http://localhost:8080/api/v1/";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 请求获取所有用户数据   初始化组件 
		// 1 请求所有用户 创建用户Hbox 并添加到 userVBox 
		
	}
	
	public void test() {

		HttpResponse<String> usersResponse = HttpClientUtils.httpGet(apiPrefix + "users");
		String usersResponseBody = usersResponse.body();
		LOGGER.info("用户json : " + usersResponseBody);
		
		// json 取出data 
		String test = JsonUtil.getJsonObject(usersResponseBody, "message");
		LOGGER.info("取出的data : " + test);
		
		List<UserModel> users = JsonUtil.parseJsonArrayToUsers(JsonUtil.getJsonArray(usersResponseBody, "data"));
		
		LOGGER.info("解析的用户对象 : " + users.toString());
		
		
		// =======================================  ships 
		HttpResponse<String> shipsResponse = HttpClientUtils.httpGet(apiPrefix + "ships");
		String shipsResponseBody = shipsResponse.body();
		List<ShipModel> ships = JsonUtil.parseJsonArrayToShips(JsonUtil.getJsonArray(shipsResponseBody, "data"));
		LOGGER.info("转换的ships : " + ships.toString());
		
		// =============================== tracks 
		HttpResponse<String> tracksResponse = HttpClientUtils.httpGet(apiPrefix + "shiptracks/16");
		String tracksResponseBody = tracksResponse.body();
		LOGGER.info("track body : " + tracksResponseBody);
		List<TrackView> tracks = JsonUtil.parseJsonArrayToTracks(JsonUtil.getJsonArray(tracksResponseBody, "data"));
		LOGGER.info("转换的tracks : " + tracks.toString());
		
	}
	
	@FXML
	public void trackRun(MouseEvent event) {
		LOGGER.info("runButton click - ");
		test();
	}
	@FXML
	public void trackStop(MouseEvent event) {
		LOGGER.info("stopButton click - ");
	}
	@FXML
	public void trackSave(MouseEvent event) {
		LOGGER.info("saveTrackButton click - ");
	}
	@FXML
	public void trackClear(MouseEvent event) {
		LOGGER.info("clear Button click - ");
	}
	
	@FXML
	public void canvasClickStart(MouseEvent event) {
		LOGGER.info("event type - " + event.getEventType());
		Double startX = event.getX();
		Double startY = event.getY();
		
		LOGGER.info("mouse Clicked Start - " + startX + ", " + startY);

	}
	@FXML 
	public void canvasDraggedMoving(MouseEvent event) {
		LOGGER.info("mouse event type : " + event.getEventType());
		Double movingX = event.getX();
		Double movingY = event.getY();
		
		LOGGER.info("mouse Dragging event, moving - " + movingX + ", " + movingY);
	}
	@FXML 
	public void canvasReleaseStop(MouseEvent event) {
		LOGGER.info("mouse event type : {}" + event.getEventType());
		Double releaseX = event.getX();
		Double releaseY = event.getY();
		LOGGER.info("mouse canvas released - " + releaseX + ", " + releaseY);
	}

}











