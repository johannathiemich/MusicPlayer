package view;

import java.awt.*;

public class ColorTheme {
    /**
     * The colorTheme dark.
     */
    public final static ColorTheme dark = new ColorTheme(
            new Color[] {new Color(40,40,40), new Color(50,50,50)},
            new Color[] {Color.white, Color.lightGray, Color.gray},
            new Color(0, 89, 200)
          );

    /**
     * The colorTheme red.
     */
    public final static ColorTheme red = new ColorTheme(
            new Color[] {new Color(40,40,40), new Color(50,50,50)},
            new Color[] {Color.white, Color.lightGray, Color.gray},
            new Color(111, 20, 0)
    );

    public Color[] bgColor;
    public Color pointColor;
    public Color[] fgColor;

    /**
     * Constructs ColorTheme instance with Color values
     * @param bg the background colors, up to 2 colors
     * @param fg the foreground colors, up to 3 colors
     * @param point the point color
     */
    public ColorTheme(Color[] bg, Color[] fg, Color point){
        this.bgColor = bg;
        this.fgColor = fg;
        this.pointColor = point;
    }
}
