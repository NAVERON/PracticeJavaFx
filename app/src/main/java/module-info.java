

module practicefx {

	
	requires java.base;
    requires transitive java.net.http;
    requires java.logging;

    requires org.apache.commons.lang3;
    requires com.google.common;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires transitive org.json;
	
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    
    requires transitive javafx.graphics;
    
    opens org.practicefx to javafx.fxml;
	
	exports org.practicefx;
}

