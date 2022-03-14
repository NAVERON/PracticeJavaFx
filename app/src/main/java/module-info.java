
module practicefx {
	
	requires java.base;
	
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    
    requires transitive javafx.graphics;
    
    opens org.practicefx to javafx.fxml;
	
	exports org.practicefx;
}