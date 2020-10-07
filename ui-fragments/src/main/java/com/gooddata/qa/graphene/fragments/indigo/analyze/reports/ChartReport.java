package com.gooddata.qa.graphene.fragments.indigo.analyze.reports;

import com.gooddata.qa.graphene.fragments.AbstractFragment;
import com.gooddata.qa.graphene.fragments.indigo.analyze.pickerDropDown.Drilling;
import com.gooddata.qa.graphene.utils.ElementUtils;
import com.gooddata.qa.graphene.utils.WaitUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gooddata.qa.graphene.utils.ElementUtils.*;
import static com.gooddata.qa.graphene.utils.WaitUtils.waitForCollectionIsNotEmpty;
import static com.gooddata.qa.graphene.utils.WaitUtils.waitForElementVisible;
import static com.gooddata.qa.utils.CssUtils.isShortenedTitleDesignByCss;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

public class ChartReport extends AbstractFragment {

    public static final String LEGEND_SERIES = ".viz-legend .series";
    public static final String LEGEND_INDICATOR = LEGEND_SERIES + " .series-axis-indicator";
    public static final String LEGEND_ITEM = LEGEND_SERIES + " .series-item";
    public static final String LEGEND_ITEM_NAME = LEGEND_ITEM + " .series-name";
    public static final String LEGEND_ITEM_ICON = LEGEND_ITEM + " .series-icon";
    private static final String LEGEND_COLOR_ATTRIBUTE = "style";
    private static final String HEATMAP_LEGEND = ".viz-legend .box";
    private static final String TOOLTIP_ITEM = ".gd-viz-tooltip-item";
    private static final String TOOLTIP_TITLE = ".gd-viz-tooltip-title";
    private static final String TOOLTIP_VALUE = ".gd-viz-tooltip-value";

    @FindBy(css = ".highcharts-series *")
    private List<WebElement> trackers;

    @FindBy(css = ".highcharts-markers *")
    private List<WebElement> markers;

    @FindBy(css = ".highcharts-series rect")
    private List<WebElement> rectTrackers;

    @FindBy(css = LEGEND_ITEM_ICON)
    private List<WebElement> legendIcons;

    @FindBy(css = LEGEND_ITEM_NAME)
    private List<WebElement> legendNames;

    @FindBy(css = LEGEND_INDICATOR)
    private List<WebElement> legendIndicators;

    @FindBy(css = HEATMAP_LEGEND)
    private List<WebElement> heatmapLegend;

    @FindBy(css = ".highcharts-data-labels tspan")
    private List<WebElement> dataLabels;

    @FindBy(css = ".highcharts-axis-labels text[text-anchor = 'middle']")
    private List<WebElement> axisLabels;

    @FindBy(css = ".highcharts-xaxis-labels text[text-anchor = 'middle'], .highcharts-xaxis-labels text[text-anchor = 'end']")
    private List<WebElement> xAxisLabels;

    @FindBy(css = ".highcharts-yaxis-labels text[text-anchor = 'middle'], .highcharts-yaxis-labels text[text-anchor = 'end']")
    private List<WebElement> yAxisLabels;

    @FindBy(css = ".highcharts-stack-labels text tspan.highcharts-text-outline")
    private List<WebElement> totalsStackedColumn;

    private static final By BY_X_AXIS_TITLE = cssSelector(".highcharts-xaxis .highcharts-axis-title");
    private static final By BY_Y_AXIS_TITLE = cssSelector(".highcharts-yaxis .highcharts-axis-title");
    private static final By BY_LEGEND = className("viz-static-legend-wrap");
    private static final By BY_PRIMARY_Y_AXIS_TITLE = cssSelector(".highcharts-yaxis.s-highcharts-primary-yaxis");
    private static final By BY_SECONDARY_Y_AXIS_TITLE = cssSelector(".highcharts-yaxis.s-highcharts-secondary-yaxis");
    private static final By BY_PRIMARY_Y_AXIS = cssSelector(".highcharts-yaxis-labels.s-highcharts-primary-yaxis");
    private static final By BY_SECONDARY_Y_AXIS = cssSelector(".highcharts-yaxis-labels.s-highcharts-secondary-yaxis");
    private static final By BY_HIGHCHARTS_TOOLTIP = cssSelector(".highcharts-tooltip-container div.highcharts-tooltip");

    public static ChartReport getInstance(SearchContext context) {
        return Graphene.createPageFragment(ChartReport.class,
                waitForElementVisible(className("viz-line-family-chart-wrap"), context));
    }

    public boolean isColumnHighlighted(Pair<Integer, Integer> position) {
        WebElement element = getTracker(position.getLeft(), position.getRight());
        final String fillBeforeHover = element.getAttribute("fill");
        getActions().moveToElement(element).moveByOffset(1, 1).perform();
        final String fillAfterHover = element.getAttribute("fill");
        return !Objects.equals(fillAfterHover, fillBeforeHover);
    }

    public Drilling openDrillingPicker(Pair<Integer, Integer> position) {
        clickOnElement(position);
        return Drilling.getInstance(browser);
    }

    public void clickOnElement(Pair<Integer, Integer> position) {
        WebElement element = getTracker(position.getLeft(), position.getRight());
        // Because geckodriver follows W3C and moves the mouse pointer from the centre of the screen,
        // Move the mouse pointer to the top-left corner of the fragment before moving to the specific Element
        ElementUtils.moveToElementActions(getRoot(), 0, 0).moveToElement(element)
                .moveByOffset(1, 1).click().perform();
    }

    public void clickOnDataLabel(Pair<Integer, Integer> position) {
        List<WebElement> list = waitForCollectionIsNotEmpty(getRoot().findElements(
                cssSelector(String.format(".highcharts-data-labels.highcharts-series-%s g", position.getLeft()))));
        WebElement element = list.get(position.getRight());
        getActions().moveToElement(element).moveByOffset(1, 1).click().perform();
    }

    //Some type charts don't exist legend will return Zero
    public int getLegendIndex(String legendName) {
        List<WebElement> elements = getRoot()
                .findElements(className("series-item"));
        if(elements.isEmpty()) {
            return 0;
        }
        List<String> texts = elements.stream().map(element -> element.getText()).collect(Collectors.toList());
        return texts.indexOf(legendName);
    }

    //Some type charts don't exist axis will return empty
    public String getYaxisTitle() {
        List<WebElement> yAxisTitle = getRoot().findElements(BY_Y_AXIS_TITLE);
        if (yAxisTitle.isEmpty()) {
            return "";
        }
        return yAxisTitle.get(0).getText();
    }

    public String getPrimaryYaxisTitle() {
        List<WebElement> yPrimaryAxisTitle = getRoot().findElements(BY_PRIMARY_Y_AXIS_TITLE);
        if (yPrimaryAxisTitle.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return yPrimaryAxisTitle.get(0).getText();
    }

    public String getSecondaryYaxisTitle() {
        List<WebElement> ySencondaryAxisTitle = getRoot().findElements(BY_SECONDARY_Y_AXIS_TITLE);
        if (ySencondaryAxisTitle.isEmpty()) {
            return StringUtils.EMPTY;
        }
        return ySencondaryAxisTitle.get(0).getText();
    }

    public Boolean isPrimaryYaxisVisible() {
        return isElementVisible(BY_PRIMARY_Y_AXIS, getRoot());
    }

    public Boolean isSecondaryYaxisVisible() {
        return isElementVisible(BY_SECONDARY_Y_AXIS, getRoot());
    }

    public List<List<String>> getValuePrimaryYaxis() {
        return getRoot().findElements(BY_PRIMARY_Y_AXIS).stream()
                .map(e -> e.findElements(By.tagName("text")))
                .map(es -> es.stream().map(WebElement::getText).collect(toList()))
                .collect(toList());
    }

    public List<List<String>> getValueSecondaryYaxis() {
        return getRoot().findElements(BY_SECONDARY_Y_AXIS).stream()
                .map(e -> e.findElements(By.tagName("text")))
                .map(es -> es.stream().map(WebElement::getText).collect(toList()))
                .collect(toList());
    }

    //Some type charts don't exist axis will return empty
    public String getXaxisTitle() {
        List<WebElement> xAxisTitle = getRoot().findElements(BY_X_AXIS_TITLE);
        if (xAxisTitle.isEmpty()) {
            return "";
        }
        return xAxisTitle.get(0).getText();
    }

    public int getTrackersCount() {
        if (isLineChart()) {
            return waitForCollectionIsNotEmpty(trackers).size();
        }

        if (isPieChart()) {
            return (int) waitForCollectionIsNotEmpty(trackers).stream()
                    .filter(e -> isElementVisible(e))
                    .count();
        }

        if (isTreeMapChart()) {
            return (int) waitForCollectionIsNotEmpty(rectTrackers).stream()
                    .map(e -> isElementVisible(e))
                    .count();
        }

        return (int) waitForCollectionIsNotEmpty(trackers).stream()
            .map(e -> e.getAttribute("height"))
            .map(Integer::parseInt)
            .filter(i -> i > 0)
            .count();
    }

    public int getMarkersCount() {
        return waitForCollectionIsNotEmpty(markers).size();
    }

    public List<List<String>> getTooltipTextOnTrackerByIndex(int groupIndex, int index) {
        displayTooltipOnTrackerByIndex(groupIndex, index);
        return getTooltipText();
    }

    public boolean isShortenTooltipTextOnTrackerByIndex(int groupNumber, int index, int width) {
        displayTooltipOnTrackerByIndex(groupNumber, index);
        WebElement tooltip = browser.findElement(BY_HIGHCHARTS_TOOLTIP);
        return isShortenedTitleDesignByCss(waitForElementVisible(tooltip.findElement(cssSelector(TOOLTIP_TITLE))), width);
    }

    public boolean isLegendVisible() {
        return isElementVisible(BY_LEGEND, browser);
    }

    public boolean areLegendsHorizontal() {
        return isElementVisible(cssSelector(".viz-legend.position-top"), browser);
    }

    public boolean areLegendsVertical() {
        return isElementVisible(cssSelector(".viz-legend.position-right"), browser);
    }

    public boolean isShortenNameInLegend(String measureName, int width) {
        WebElement measure = legendNames.stream().filter(measures -> measures.getText().equals(measureName)).findFirst().get();
        return isShortenedTitleDesignByCss(measure, width);
    }

    public List<String> getLegends() {
        if (legendNames.isEmpty()) {
            return Collections.emptyList();
        }

        return waitForCollectionIsNotEmpty(legendNames).stream()
            .map(e -> e.getText())
            .collect(toList());
    }

    public void clickLegend(String nameLegend) {
        clickLegend(nameLegend, 0);
    }

    //using index to specific Legend which has the same name
    public void clickLegend(String nameLegend, int index) {
        waitForCollectionIsNotEmpty(legendNames)
            .stream()
            .filter(e -> nameLegend.equals(e.getText()))
            .collect(toList())
            .get(index).click();
    }

    public List<String> getLegendIndicators() {
        return waitForCollectionIsNotEmpty(legendIndicators).stream()
                .map(e -> e.getText())
                .collect(toList());
    }

    public Boolean isLegendIndicatorPresent() {
        return isElementPresent(cssSelector(LEGEND_INDICATOR), getRoot());
    }

    public int getTrackerLegends() {
        return waitForCollectionIsNotEmpty(heatmapLegend).stream()
                .map(ElementUtils::isElementVisible)
                .collect(toList()).size();
    }

    public List<String> getLegendColors() {
        return waitForCollectionIsNotEmpty(legendIcons).stream()
            .map(e -> e.getAttribute(LEGEND_COLOR_ATTRIBUTE))
            .map(e -> e.replaceAll(".*background-color: ([^;]*);.*", "$1").replace(" ", ""))
            .collect(toList());
    }

    public List<String> getDataLabels() {
        if (dataLabels.isEmpty()) {
            return Collections.emptyList();
        }
        return getLabels(dataLabels);
    }

    public List<String> getAxisLabels() {
        // Axis labels will be empty in case report has no attribute.
        if (axisLabels.isEmpty())
            return Collections.emptyList();

        return getLabels(axisLabels);
    }

    public List<String> getXaxisLabels() {
        // Axis labels will be empty in case report has no attribute.
        if (xAxisLabels.isEmpty()) {
            return Collections.emptyList();
        }
        return getLabels(xAxisLabels);
    }

    public List<String> getYaxisLabels() {
        // Axis labels will be empty in case report has no attribute.
        if (yAxisLabels.isEmpty()) {
            return Collections.emptyList();
        }
        return getLabels(yAxisLabels);
    }

    public String getChartType() {
        return Stream.of(getRoot().getAttribute("class").split("\\s+"))
                .filter(e -> e.contains("s-visualization-"))
                .map(e -> e.replace("s-visualization-", ""))
                .findFirst()
                .get();
    }

    public List<String> getTotalsStackedColumn() {
        if (totalsStackedColumn.isEmpty()) {
            return emptyList();
        }
        return getElementTexts(totalsStackedColumn);
    }

    public String checkColorColumn(int xAxis, int yAxis) {
        WebElement webElement = getTracker(xAxis, yAxis);
        return webElement.getAttribute("fill");
    }

    public String getColor(int xAxis, int yAxis) {
        WebElement webElement = getTracker(xAxis, yAxis);
        return webElement.getAttribute("stroke");
    }

    public String getHeightColumn(int xAxis, int yAxis) {
        WebElement webElement = getTracker(xAxis, yAxis);
        return webElement.getAttribute("height");
    }

    public String getXLocationColumn(int xAxis, int yAxis) {
        WebElement webElement = getTracker(xAxis, yAxis);
        return webElement.getAttribute("x");
    }

    private boolean isLineChart() {
        return getRoot().getAttribute("class").contains("visualization-line");
    }

    private boolean isPieChart() {
        return getRoot().getAttribute("class").contains("visualization-pie");
    }

    private boolean isTreeMapChart() {
        return getRoot().getAttribute("class").contains("visualization-treemap");
    }

    private List<String> getLabels(Collection<WebElement> labels) {
        // all labels need to be visible before getting text
        // without this it's specially unstable on embedded AD
        labels.stream().forEach(WaitUtils::waitForElementVisible);
        return getElementTexts(labels);
    }

    private void displayTooltipOnTrackerByIndex(int groupIndex, int index) {
        WebElement tracker = getTracker(groupIndex, index);
        checkIndex(index);
        getActions().moveToElement(tracker).moveByOffset(1, 1).click().perform();
        waitForElementVisible(BY_HIGHCHARTS_TOOLTIP, browser);
    }

    public boolean isTooltipContainTwoRowsAndThreeDots(int groupIndex, int index){
        displayTooltipOnTrackerByIndex(groupIndex, index);
        return isTooltipContainThreeDots() && isTooltipContainTwoRows();
    }

    public boolean isTooltipContainThreeDots(int groupIndex, int index){
        displayTooltipOnTrackerByIndex(groupIndex, index);
        return  getTooltipInstance().findElement(className("gd-viz-tooltip-title")).getCssValue("text-overflow").contains("ellipsis");
    }

    private boolean isTooltipContainThreeDots(){
        return  getTooltipInstance().findElement(className("clamp-two-line")).getCssValue("-webkit-box-orient").contains("vertical");
    }

    private boolean isTooltipContainTwoRows(){
        return  getTooltipInstance().findElement(className("clamp-two-line")).getCssValue("-webkit-line-clamp").contains("2");
    }

    public WebElement getTooltipInstance(){
        return Tooltip.getInstance(browser).getRoot();
    }

    private List<List<String>> getTooltipText() {
        WebElement tooltip = browser.findElement(BY_HIGHCHARTS_TOOLTIP);
        return waitForCollectionIsNotEmpty(tooltip.findElements(cssSelector(TOOLTIP_ITEM))).stream()
                .map(item -> asList(item.findElement(cssSelector(TOOLTIP_TITLE)).getText(),
                        item.findElement(cssSelector(TOOLTIP_VALUE)).getText()))
                .collect(Collectors.toList());
    }

    private void checkIndex(int index) {
        waitForCollectionIsNotEmpty(trackers);
        if (index < 0 || index >= trackers.size()) {
            throw new IndexOutOfBoundsException();
        }
    }

    public String getTrackerType(int groupIndex, int index) {
        return getTracker(groupIndex, index).getTagName();
    }

    private WebElement getTracker(int groupIndex, int index) {
        List<WebElement> list = waitForCollectionIsNotEmpty(getRoot()
                .findElements(cssSelector(String.format(".highcharts-series-%s.highcharts-tracker rect," +
                        ".highcharts-series-%s.highcharts-tracker path," +
                        ".highcharts-series-%s.highcharts-tracker circle", groupIndex, groupIndex, groupIndex))));
        return list.get(index);
    }

    public static class Tooltip extends AbstractFragment {
        public static final String MAIN_CLASS = "hc-tooltip";
        public static Tooltip getInstance(SearchContext context) {
            return Graphene.createPageFragment(Tooltip.class,
                    waitForElementVisible(className(MAIN_CLASS), context));
        }
    }
}
