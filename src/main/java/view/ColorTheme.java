package view;

import java.awt.*;

public class ColorTheme {
    /**
     * The colorTheme dark.
     */
    public final static ColorTheme dark = new ColorTheme(
            new Color[] {new Color(40,40,40), new Color(50,50,50)},
            new Color[] {Color.white, Color.lightGray, Color.gray},
            new Color[] {new Color(0, 89, 200), Color.white}
          );

    /**
     * The colorTheme white.
     */
    public final static ColorTheme white = new ColorTheme(
            new Color[] {new Color(245, 245, 245), Color.white},
            new Color[] {Color.black, Color.darkGray, Color.gray},
            new Color[] {new Color(0, 89, 200), Color.white}
    );

    /**
     * The colorTheme red.
     */
    public final static ColorTheme red = new ColorTheme(
            new Color[] {new Color(40,40,40), new Color(50,50,50)},
            new Color[] {Color.white, Color.lightGray, Color.gray},
            new Color[] {new Color(111, 20, 0), Color.white}
    );

    public Color[] bgColor;
    public Color[] fgColor;
    public Color[] pointColor;

    /**
     * Constructs ColorTheme instance with Color values
     * @param bg the background colors, up to 2 colors
     * @param fg the foreground colors, up to 3 colors
     * @param point the point colors, up to 2 colors
     */
    public ColorTheme(Color[] bg, Color[] fg, Color[] point){
        this.bgColor = bg;
        this.fgColor = fg;
        this.pointColor = point;
    }
}
