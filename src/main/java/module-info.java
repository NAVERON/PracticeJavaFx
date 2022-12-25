
module practicefx {

    requires java.base;
    requires java.net.http;
    requires java.logging;

    requires org.apache.commons.lang3;
    requires com.google.common;
    // requires com.fasterxml.jackson.core;
    // requires com.fasterxml.jackson.databind;
    // requires com.fasterxml.jackson.annotation;
    requires org.json;
    requires org.slf4j;
    requires com.google.gson;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.media;
    requires javafx.web;
    requires javafx.graphics;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome;
    requires kafka.clients;

    opens org.practicefx to javafx.fxml;
    // opens org.practicefx.models to com.google.gson;
    opens org.practicefx.models to com.google.gson, org.json;

    exports org.practicefx;

}
