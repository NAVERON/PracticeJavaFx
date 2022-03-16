package org.practicefx;

import java.util.Random;

import javafx.geometry.Point2D;

/**
 * 公共的静态方法 晶态常量 
 * @author eron
 *
 */
public class CommonConstant {

	// 接口公共前缀 urlPrefix 
	public static String API_PREFIX = "http://localhost:8080/api/v1/";
	
	public static Point2D randomGenerate2DPoint() {
		// 450  500 范围内 
		Random random = new Random();
		Double x = random.nextDouble() * 450;
		Double y = random.nextDouble() * 500;
		
		return new Point2D(x, y);
	}
}
