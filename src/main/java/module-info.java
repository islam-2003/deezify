module be.deezify {
    requires com.google.gson;
    requires javafx.fxml;
    requires jaudiotagger;
    requires static lombok;
    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.management;

    opens be.deezify to javafx.fxml;
    opens be.deezify.views to javafx.fxml;
    opens be.deezify.models to com.google.gson;
    opens be.deezify.json to com.google.gson;
    exports be.deezify;
    opens be.deezify.views.tags to javafx.fxml;
    opens be.deezify.views.global to javafx.fxml;
}