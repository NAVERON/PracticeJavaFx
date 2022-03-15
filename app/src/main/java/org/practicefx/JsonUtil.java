package org.practicefx;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {
	
	private static final Logger LOGGER = Logger.getLogger(JsonUtil.class.getName());
    
    /**
     * org.json  library tools 
     * @param originJson
     * @param key
     * @return
     */
    public static String getJsonObject(String originJson, String key) {
    	JSONObject jsonObject = new JSONObject(originJson);
    	
    	return jsonObject.getString(key);
    }
    public static JSONArray getJsonArray(String originJson, String key) {
    	JSONObject jsonObject = new JSONObject(originJson);
    	return jsonObject.getJSONArray(key);
    }
    
    public static List<UserModel> parseJsonArrayToUsers(JSONArray usersJsonArray){
    	List<UserModel> users = new ArrayList<>();
    	
    	for(int i = 0; i < usersJsonArray.length(); i++) {
    		JSONObject jsonOne = usersJsonArray.getJSONObject(i);
    		
    		Long userId = jsonOne.optLong("id");
    		String name = jsonOne.optString("name");
    		String password = jsonOne.optString("password");
    		String registEmail = jsonOne.optString("registEmail");
    		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(jsonOne.optString("createTime"));
    		LocalDateTime updateTime = DateTimeUtils.parseStringToLocalDateTime(jsonOne.optString("updateTime"));
    		
    		UserModel user = UserModel.createBuilder().name(name).password(password).registEmail(registEmail).build();
    		user.setUserId(userId);
    		user.setCreateTime(createTime);
    		user.setUpdateTime(updateTime);
    		
    		users.add(user);
    	}
    	
    	return users;
    }
    public static UserModel parseJsonObectToUser(JSONObject userJson) {
    	
    	Long userId = userJson.optLong("id");
		String name = userJson.optString("name");
		String password = userJson.optString("password");
		String registEmail = userJson.optString("registEmail");
		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(userJson.optString("createTime"));
		LocalDateTime updateTime = DateTimeUtils.parseStringToLocalDateTime(userJson.optString("updateTime"));
		
		UserModel user = UserModel.createBuilder().name(name).password(password).registEmail(registEmail).build();
		user.setUserId(userId);
		user.setCreateTime(createTime);
		user.setUpdateTime(updateTime);
		
		return user;
    }
    
    public static List<ShipModel> parseJsonArrayToShips(JSONArray shipsJsonArray){
    	List<ShipModel> ships = new ArrayList<>();
    	
    	for(int i = 0; i < shipsJsonArray.length(); i++) {
    		JSONObject jsonOne = shipsJsonArray.getJSONObject(i);
    		
    		Long shipId = jsonOne.optLong("id");
    		Long userId = jsonOne.optLong("userId");
    		String name = jsonOne.optString("name");
    		String mmsi = jsonOne.optString("mmsi");
    		String imoNumber = jsonOne.optString("imoNumber");
    		String callNumber = jsonOne.optString("callNumber");
    		Integer type = jsonOne.optInt("type");
    		Integer electronicType = jsonOne.optInt("electronicType");
    		Float draft = jsonOne.optFloat("draft");
    		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(jsonOne.optString("createTime"));
    		LocalDateTime updateTime = DateTimeUtils.parseStringToLocalDateTime(jsonOne.optString("updateTime"));
    		
    		ShipModel ship = ShipModel.createBuilder().userId(userId).name(name).mmsi(mmsi).imoNumber(imoNumber)
    				.callNumber(callNumber).type(type).electronicType(electronicType).build();
    		ship.setShipId(shipId);
    		ship.setCreateTime(createTime);
    		ship.setUpdateTime(updateTime);
    		
    		ships.add(ship);
    	}
    	
    	return ships;
    }
    public static ShipModel parseJsonObectToShip(JSONObject shipJson) {
    	
    	Long shipId = shipJson.optLong("id");
		Long userId = shipJson.optLong("userId");
		String name = shipJson.optString("name");
		String mmsi = shipJson.optString("mmsi");
		String imoNumber = shipJson.optString("imoNumber");
		String callNumber = shipJson.optString("callNumber");
		Integer type = shipJson.optInt("type");
		Integer electronicType = shipJson.optInt("electronicType");
		Float draft = shipJson.optFloat("draft");
		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(shipJson.optString("createTime"));
		LocalDateTime updateTime = DateTimeUtils.parseStringToLocalDateTime(shipJson.optString("updateTime"));
		
		ShipModel ship = ShipModel.createBuilder().userId(userId).name(name).mmsi(mmsi).imoNumber(imoNumber)
				.callNumber(callNumber).type(type).electronicType(electronicType).build();
		ship.setShipId(shipId);
		ship.setCreateTime(createTime);
		ship.setUpdateTime(updateTime);
		
		return ship;
    }
    
    public static List<TrackView> parseJsonArrayToTracks(JSONArray tracksJsonArray){
    	List<TrackView> tracks = new ArrayList<>();
    	    	
    	for(int i = 0; i < tracksJsonArray.length(); i++) {
    		JSONObject jsonOne = tracksJsonArray.getJSONObject(i);
    		    		
    		Long trackId = jsonOne.optLong("id");
    		Long shipId = jsonOne.optLong("shipId");
    		Float rotation = jsonOne.optFloat("rotationAcceleration");
    		Float sogSpeed = jsonOne.optFloat("sogSpeed");
    		Float cogCourse = jsonOne.optFloat("cogCourse");
    		Float speed = jsonOne.optFloat("speed");
    		Float course = jsonOne.optFloat("course");
    		Float rudder = jsonOne.optFloat("rudder");
    		Float longitude = jsonOne.optFloat("longitude");
    		Float latitude = jsonOne.optFloat("latitude");
    		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(jsonOne.optString("createTime"));

    		TrackView track = TrackView.createBuilder().shipId(shipId).rotationAcceleration(rotation)
    				.sogSpeed(sogSpeed).cogCource(cogCourse).speed(speed).cource(course)
    				.rudder(rudder).longitude(longitude).latitude(latitude).build();
    		track.setTrackId(trackId);
    		track.setCreateTime(createTime);
    		    		
    		tracks.add(track);
    	}
    	
    	return tracks;
    }
    public static TrackView parseJsonObectToTrack(JSONObject trackJson) {
    	
		Long trackId = trackJson.optLong("id");
		Long shipId = trackJson.optLong("shipId");
		Float rotation = trackJson.optFloat("rotationAcceleration");
		Float sogSpeed = trackJson.optFloat("sogSpeed");
		Float cogCourse = trackJson.optFloat("cogCourse");
		Float speed = trackJson.optFloat("speed");
		Float course = trackJson.optFloat("course");
		Float rudder = trackJson.optFloat("rudder");
		Float longitude = trackJson.optFloat("longitude");
		Float latitude = trackJson.optFloat("latitude");
		LocalDateTime createTime = DateTimeUtils.parseStringToLocalDateTime(trackJson.optString("createTime"));

		TrackView track = TrackView.createBuilder().shipId(shipId).rotationAcceleration(rotation)
				.sogSpeed(sogSpeed).cogCource(cogCourse).speed(speed).cource(course)
				.rudder(rudder).longitude(longitude).latitude(latitude).build();
		track.setShipId(shipId);
		track.setCreateTime(createTime);
		
		return track;
    }
    
}














