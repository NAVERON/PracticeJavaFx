
package org.practicefx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LauncherFx extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 创建主面板 
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
		MainController controller = new MainController();
		loader.setController(controller);
		
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
//	public static class Starter {
//		public static void main(String[] args) {
//			LauncherFx.main(args);
//		}
//	}

}



