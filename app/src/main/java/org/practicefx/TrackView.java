package org.practicefx;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javafx.geometry.Point2D;

/**
 * 轨迹点的属性 
 * @author eron
 *
 */
public class TrackView extends Point2D implements Cloneable {

	private static final Logger LOGGER = Logger.getLogger(TrackView.class.getName());
    
	// 不需要column注解的情况是 使用大小驼峰命名 如 a_b -> 对象 aB 属性 

    private Long trackId; // 自增主键  
	private Long shipId; // 船舶id @See Ship  必须赋值 
	private Float rotationAcceleration; // 转向角加速度 
	private Float sogSpeed; // 对地船舶速度 
	private Float cogCourse; // 对地航向 
	private Float speed; // 船速 speed - 流速 = sogSpeed 
	private Float course; // 船向 cource = cogCource 
	private Float rudder; // 船舶舵角 
	private Float longitude; // 经度 
	private Float latitude; // 纬度 
	private LocalDateTime createTime; // 轨迹点创建时间 
	
	@Deprecated 
	public TrackView(Double x, Double y) { 
		super(x, y);
		this.shipId = 0L;
	}
	public TrackView(Long shipId, Double x, Double y) { 
		super(x, y);
		this.shipId = shipId;
	}
	public TrackView(Builder builder) { 
		super(builder.x, builder.y);  // 坐标是  double类型的 
		
		this.shipId = builder.shipId;
		this.rotationAcceleration = builder.rotationAcceleration;
		this.sogSpeed = builder.sogSpeed;
		this.cogCourse = builder.cogCource;
		this.speed = builder.speed;
		this.course = builder.cource;
		this.rudder = builder.rudder;
		this.longitude = builder.longitude;
		this.latitude = builder.latitude;
	}
	
	public static Builder createBuilder() {
		return new Builder();
	}
	
	public static class Builder { 
		
		private Double x = 0D;
		private Double y = 0D;
		// 以后可以实现快速创建和参数验证 
		//private Long id; // 自增主键  
		private Long shipId; // 船舶id @See Ship  必须赋值 
		private Float rotationAcceleration = 0F; // 转向角加速度 
		private Float sogSpeed = 0F; // 对地船舶速度 
		private Float cogCource = 0F; // 对地航向 
		private Float speed = 0F; // 船速 speed - 流速 = sogSpeed 
		private Float cource = 0F; // 船向 cource = cogCource 
		private Float rudder = 0F; // 船舶舵角 
		private Float longitude = 0F; // 经度 
		private Float latitude = 0F; // 纬度 
		//private LocalDateTime createTime; // 轨迹点创建时间 
		
		public Builder() {}
		public Builder posX(Double x) {
			this.x = x;
			return this;
		}
		public Builder posY(Double y) {
			this.y = y;
			return this;
		}
		public Builder shipId(Long shipId) {
			this.shipId = shipId;
			return this;
		}
		public Builder rotationAcceleration(Float rotationAcceleration) {
			this.rotationAcceleration = rotationAcceleration;
			return this;
		}
		public Builder sogSpeed(Float sogSpeed) {
			this.sogSpeed = sogSpeed;
			return this;
		}
		public Builder cogCource(Float cogCource) {
			this.cogCource = cogCource;
			return this;
		}
		public Builder speed(Float speed) {
			this.speed = speed;
			return this;
		}
		public Builder cource(Float cource) {
			this.cource = cource;
			return this;
		}
		public Builder rudder(Float rudder) {
			this.rudder = rudder;
			return this;
		}
		public Builder longitude(Float longitude) {
			this.longitude = longitude;
			return this;
		}
		public Builder latitude(Float latitude) {
			this.latitude = latitude;
			return this;
		}
		
		public TrackView build() {
			if(this.shipId == null) {
				throw new IllegalArgumentException("shipId of ShipTrackPoint must required !");
			}
			return new TrackView(this);
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	// id 不覆盖 
	public void overrideAttributes(TrackView another) {
		if (another == null) {
			LOGGER.severe("another User is null !!!");
			return;
		}
		
		this.shipId = another.shipId;
		this.rotationAcceleration = another.rotationAcceleration;
		this.sogSpeed = another.sogSpeed;
		this.cogCourse = another.cogCourse;
		this.speed = another.speed;
		this.course = another.course;
		this.rudder = another.rudder;
		this.longitude = another.longitude;
		this.latitude = another.latitude;
		this.createTime = another.createTime;
		
	}

	/**
	 * pojo 通用 getter 和 setter 
	 * @return
	 */
	public Long getTrackId() {
		return trackId;
	}

	public void setTrackId(Long trackId) {
		this.trackId = trackId;
	}

	public Long getShipId() {
		return shipId;
	}

	public void setShipId(Long shipId) {
		this.shipId = shipId;
	}

	public Float getRotationAcceleration() {
		return rotationAcceleration;
	}

	public void setRotationAcceleration(Float rotationAcceleration) {
		this.rotationAcceleration = rotationAcceleration;
	}

	public Float getSogSpeed() {
		return sogSpeed;
	}

	public void setSogSpeed(Float sogSpeed) {
		this.sogSpeed = sogSpeed;
	}

	public Float getCogCource() {
		return cogCourse;
	}

	public void setCogCource(Float cogCource) {
		this.cogCourse = cogCource;
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	public Float getCource() {
		return course;
	}

	public void setCource(Float cource) {
		this.course = cource;
	}

	public Float getRudder() {
		return rudder;
	}

	public void setRudder(Float rudder) {
		this.rudder = rudder;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ShipTrackPoint [trackId=" + trackId + ", shipId=" + shipId + ", rotationAcceleration=" + rotationAcceleration
				+ ", sogSpeed=" + sogSpeed + ", cogCource=" + cogCourse + ", speed=" + speed + ", cource=" + course
				+ ", rudder=" + rudder + ", longitude=" + longitude + ", latitude=" + latitude + ", createTime="
				+ createTime + "]";
	}
	
	
}




