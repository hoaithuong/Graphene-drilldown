package com.gooddata.qa.utils.http;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ColorPaletteRequestData {
    private List<Pair<String, ColorPalette>> listColors = emptyList();

    /**
     * If only input one color.
     * This method Cast "Pair.of(guid, colorPalette)" to list colors.
     * Returns list colors and then getJson() method will reuse it.
     *
     * @param guid         For each colorPalette object there is a guid attribute corresponding.
     * @param colorPalette the object contains red , green and blue.
     */

    public ColorPaletteRequestData(String guid, ColorPalette colorPalette) {
        listColors = singletonList(Pair.of(guid, colorPalette));
    }

    /**
     * If input list colors
     * Returns list colors and then getJson() method will reuse it.
     *
     * @param colors list colorPalette object and guid attribute corresponding.
     */
    public ColorPaletteRequestData(List<Pair<String, ColorPalette>> colors) {
        listColors = colors;
    }

    /**
     * Init ColorPaletteRequestData with list guids and colorPalettes.
     * Returns new ColorPaletteRequestData with input param is a list colors.
     *
     * @param colors list colorPalette object and guid attribute corresponding.
     */

    public static ColorPaletteRequestData initColorPalette(List<Pair<String, ColorPalette>> colors) {
        return new ColorPaletteRequestData(colors);
    }

    /**
     * Init ColorPaletteRequestData with two params guid and colorPalette.
     * Returns new ColorPaletteRequestData with two input param is guid and colorPalette object .
     *
     * @param guid         For each colorPalette object there is a guid attribute corresponding.
     * @param colorPalette the object contains red , green and blue.
     */
    public static ColorPaletteRequestData initColorPalette(String guid, ColorPalette colorPalette) {
        return new ColorPaletteRequestData(guid, colorPalette);
    }

    /**
     * Returns jsonObject and push to RestClient.
     */
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject() {{
            put("styleSettings", new JSONObject() {{
                put("chartPalette", new JSONArray() {{
                    for (Pair<String, ColorPalette> color : listColors) {
                        put(new JSONObject() {{
                            put("guid", color.getLeft());
                            put("fill", new JSONObject() {{
                                put("r", color.getRight().getRed());
                                put("g", color.getRight().getGreen());
                                put("b", color.getRight().getBlue());
                            }});
                        }});
                    }
                }});
            }});
        }};
        return jsonObject;
    }

    public enum ColorPalette {
        RED(255, 0, 0),
        GREEN(0, 255, 0),
        BLUE(0, 0, 255),
        YELLOW(255, 255, 0),
        YELLOW_LIGHT(255, 255, 25),
        WHITE(255, 255, 255),
        CYAN(20, 178, 226),
        CYAN_DEVIANT(20, 177, 225),
        LIME_GREEN(0, 193, 141),
        CYAN_LIME_GREEN(1, 194, 142),
        LIGHT_RED(255, 153, 153),
        BRIGHT_RED(229, 77, 66),
        PURE_ORANGE(241, 134, 0),
        SOFT_RED(245, 184, 179),
        LIGHT_PURPLE(196, 110, 188),
        VIVID_ORANGE(25, 218, 166),
        PURPLE(171, 85, 163),
        LIGHT_GRAYISH_PURPLE(221, 187, 218),
        DARK_CYAN(0, 110, 145),
        MODERATE_RED(181, 60, 51);

        private int red;
        private int green;
        private int blue;

        ColorPalette(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        @Override
        public String toString() {
            return String.format("rgb(%d,%d,%d)", red, green, blue);
        }

        public String toReportFormatString() {
            return String.format("rgb(%d, %d, %d)", red, green, blue);
        }

        public String toCssFormatString() {
            return String.format("%d-%d-%d", red, green, blue);
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

        /**
         * This method convert RGB color to Hex color
         * Return string hex color
         * For example : rgb(255,0,0) -> #FF0000
         */

        public String getHexColor() {
            return String.format("#%02X%02X%02X", red, green, blue);
        }
    }
}
