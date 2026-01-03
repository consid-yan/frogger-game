module frogger {
    requires ucd.comp2011.engine;
    requires javafx.graphics;
    requires java.desktop;
    requires org.jetbrains.annotations;

    exports frogger.display to javafx.graphics;
    exports frogger.model;
    exports frogger.model.entities;
}