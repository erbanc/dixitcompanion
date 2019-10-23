package fr.erban.dxitcompanion.game.color;

public enum ColorEnum {
    RED("#DC3730"),
    BLUE("#30ADDC"),
    GREEN("#41B625"),
    YELLOW("#F6E91E"),
    BROWN("#88610D"),
    PURPLE("#C468CA"),
    PINK("#FFC9F4"),
    ORANGE("#EC861F"),
    WHITE("#FFFFFF"),
    CYAN("#1CDED2");

    String hexa;

    private ColorEnum(String hexa) {
        this.hexa = hexa;
    }
}
