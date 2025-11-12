module frogger {
    requires ucd.comp2011.engine;
    requires javafx.graphics;
    requires java.desktop;

    exports frogger.display to javafx.graphics;
}