package com.gooddata.qa.graphene.indigo.dashboards;

import com.gooddata.qa.graphene.entity.visualization.CategoryBucket;
import com.gooddata.qa.graphene.entity.visualization.InsightMDConfiguration;
import com.gooddata.qa.graphene.entity.visualization.MeasureBucket;
import com.gooddata.qa.graphene.enums.DateRange;
import com.gooddata.qa.graphene.enums.indigo.ReportType;
import com.gooddata.qa.graphene.enums.project.ProjectFeatureFlags;
import com.gooddata.qa.graphene.enums.user.UserRoles;
import com.gooddata.qa.graphene.fragments.indigo.analyze.pages.AnalysisPage;
import com.gooddata.qa.graphene.fragments.indigo.analyze.pages.internals.ConfigurationPanelBucket;
import com.gooddata.qa.graphene.fragments.indigo.analyze.reports.ChartReport;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.ConfigurationPanel;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.ConfigurationPanel.DrillMeasureDropDown.DrillConfigPanel;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.DrillModalDialog;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.Insight;
import com.gooddata.qa.graphene.indigo.dashboards.common.AbstractDashboardTest;
import com.gooddata.qa.graphene.utils.ElementUtils;
import com.gooddata.qa.utils.http.ColorPaletteRequestData;
import com.gooddata.qa.utils.http.RestClient;
import com.gooddata.qa.utils.http.attribute.AttributeRestRequest;
import com.gooddata.qa.utils.http.indigo.IndigoRestRequest;
import com.gooddata.qa.utils.http.project.ProjectRestRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.gooddata.qa.graphene.AbstractTest.Profile.ADMIN;
import static com.gooddata.qa.graphene.utils.ElementUtils.isElementPresent;
import static com.gooddata.qa.graphene.utils.ElementUtils.isElementVisible;
import static com.gooddata.qa.graphene.utils.WaitUtils.waitForElementNotVisible;
import static com.gooddata.qa.graphene.utils.ElementUtils.BY_WARNING_MESSAGE_BAR;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_AMOUNT;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_DEPARTMENT;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_BEST_CASE;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_STAGE_NAME;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_FORECAST_CATEGORY;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_TIMELINE_EOP;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class KPIDashboardsDrillToInsightTest extends AbstractDashboardTest {

    private final String SOURCE_INSIGHT_HAS_TWO_MEASURES = "Two Measures";
    private final String SOURCE_INSIGHT_HAS_PROTECTED_DATA = "Protected Data";
    private final String TARGET_INSIGHT_FIRST = "First Insight";
    private final String TARGET_INSIGHT_SECOND = "Second Insight";
    private final String DATE = "Date";
    private final String DASHBOARD_TEST_REMOVING = "Removing";
    private final String DASHBOARD_TEST_PROTECTED = "PROTECTED";

    private IndigoRestRequest indigoRestRequest;
    ProjectRestRequest projectRestRequest;

    @Override
    protected void customizeProject() throws Throwable {
        getMetricCreator().createSnapshotBOPMetric();
        getMetricCreator().createSnapshotEOPMetric();
        getMetricCreator().createTimelineEOPMetric();
        getMetricCreator().createTimelineBOPMetric();
        getMetricCreator().createAmountMetric();
        getMetricCreator().createBestCaseMetric();
        indigoRestRequest = new IndigoRestRequest(new RestClient(getProfile(Profile.ADMIN)), testParams.getProjectId());
        projectRestRequest = new ProjectRestRequest(
            new RestClient(getProfile(ADMIN)), testParams.getProjectId());
        projectRestRequest.setFeatureFlagInProjectAndCheckResult(
            ProjectFeatureFlags.ENABLE_KPI_DASHBOARD_DRILL_TO_INSIGHT, true);
        projectRestRequest.setFeatureFlagInProjectAndCheckResult(
            ProjectFeatureFlags.ENABLE_KPI_DASHBOARD_DRILL_TO_DASHBOARD, false);
    }

    @Override
    protected void addUsersWithOtherRolesToProject() throws JSONException, IOException {
        createAndAddUserToProject(UserRoles.EDITOR);
    }

    @Test(dependsOnGroups = "createProject")
    public void prepareInsights() {
        AnalysisPage analysisPage = initAnalysePage().changeReportType(ReportType.COLUMN_CHART)
            .addMetric(METRIC_AMOUNT).addMetric(METRIC_BEST_CASE).addDate().addAttribute(ATTR_DEPARTMENT);

        analysisPage.openFilterBarPicker().checkItem(ATTR_DEPARTMENT).checkItem(DATE).apply();
        analysisPage.setFilterIsValues(ATTR_DEPARTMENT, "Direct Sales");
        ConfigurationPanelBucket.ItemConfiguration itemConfiguration = analysisPage.openConfigurationPanelBucket()
            .openColorConfiguration();

        itemConfiguration.openColorsPaletteDialog(ColorPaletteRequestData.ColorPalette.CYAN.toCssFormatString())
            .getColorsPaletteDialog().openCustomColorPalette().getCustomColorsPaletteDialog()
            .setColorCustomPicker(ColorPaletteRequestData.ColorPalette.YELLOW.getHexColor()).apply();
        itemConfiguration.openColorsPaletteDialog(ColorPaletteRequestData.ColorPalette.LIME_GREEN.toCssFormatString())
            .getColorsPaletteDialog().openCustomColorPalette().getCustomColorsPaletteDialog()
            .setColorCustomPicker(ColorPaletteRequestData.ColorPalette.RED.getHexColor()).apply();

        analysisPage.saveInsight(SOURCE_INSIGHT_HAS_TWO_MEASURES).waitForReportComputing();

        String attributeUri = indigoRestRequest.getAttributeByTitle(ATTR_FORECAST_CATEGORY).getUri();
        AttributeRestRequest attributeRestRequest = new AttributeRestRequest(
            new RestClient(getProfile(Profile.ADMIN)), testParams.getProjectId());
        attributeRestRequest.setAttributeProtected(attributeUri);

        initAnalysePage().changeReportType(ReportType.COLUMN_CHART).addMetric(METRIC_AMOUNT)
            .addAttribute(ATTR_FORECAST_CATEGORY).saveInsight(SOURCE_INSIGHT_HAS_PROTECTED_DATA).waitForReportComputing();

        initAnalysePage().changeReportType(ReportType.COLUMN_CHART).addMetric(METRIC_BEST_CASE).addDate().addAttribute(ATTR_DEPARTMENT).addFilter(ATTR_STAGE_NAME).
            openFilterBarPicker().checkItem(DATE).checkItem(ATTR_DEPARTMENT).checkItem(ATTR_STAGE_NAME).apply();

        analysisPage.setFilterIsValues(ATTR_DEPARTMENT, "Direct Sales");
        analysisPage.setFilterIsValues(ATTR_STAGE_NAME,"Interest", "Closed Lost");
        analysisPage.saveInsight(TARGET_INSIGHT_FIRST).waitForReportComputing();

        indigoRestRequest.createInsight(
            new InsightMDConfiguration(TARGET_INSIGHT_SECOND, ReportType.TABLE)
                .setMeasureBucket(singletonList(
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_TIMELINE_EOP))))
                .setCategoryBucket(asList(
                    CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.ATTRIBUTE))));
    }

    @Test(dependsOnMethods = "prepareInsights")
    public void drillToInsightOnKPIDashboards() {
        initIndigoDashboardsPage().addDashboard()
            .addInsight(SOURCE_INSIGHT_HAS_TWO_MEASURES).addInsightNext(TARGET_INSIGHT_FIRST)
            .selectWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_TWO_MEASURES);
        ConfigurationPanel configurationPanel =  indigoDashboardsPage.waitForWidgetsLoading().getConfigurationPanel();

        configurationPanel.drillIntoInsight(METRIC_AMOUNT, SOURCE_INSIGHT_HAS_TWO_MEASURES);
        configurationPanel.drillIntoInsight(METRIC_BEST_CASE, TARGET_INSIGHT_FIRST);

        indigoDashboardsPage.selectWidgetByHeadline(Insight.class, TARGET_INSIGHT_FIRST);
        configurationPanel.drillIntoInsight(METRIC_BEST_CASE, TARGET_INSIGHT_FIRST);

        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_TEST_REMOVING).waitForWidgetsLoading()
            .saveEditModeWithWidgets();

        ChartReport chartReport = indigoDashboardsPage
            .getWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_TWO_MEASURES).getChartReport();
            DrillModalDialog drillModalDialog = chartReport.openDrillingPicker(Pair.of(0, 1)).drillToInsight();
            indigoDashboardsPage.waitForDrillModalDialogLoading();
        ChartReport drillChartReport = DrillModalDialog.getInstance(browser).getChartReport();

        // This assert is to cover the bug ONE-4587 - Infinite rendering loop of visualisation in KD
        assertEquals(drillChartReport.checkColorColumn(0, 0), ColorPaletteRequestData.ColorPalette.YELLOW.toString());
        assertEquals(drillChartReport.getXaxisLabels(), asList("Direct Sales", "2011"));
        assertEquals(drillChartReport.getDataLabels(), asList("$40,105,983.96", "14,069,855"));
        assertEquals(drillModalDialog.getTitleInsight(), SOURCE_INSIGHT_HAS_TWO_MEASURES);
        drillModalDialog.close();

        chartReport.openDrillingPicker(Pair.of(1, 1)).drillToInsight();
        indigoDashboardsPage.waitForDrillModalDialogLoading();

        // This assert is to cover the bug ONE-4587 - Infinite rendering loop of visualisation in KD
        assertEquals(drillChartReport.checkColorColumn(0, 0), ColorPaletteRequestData.ColorPalette.YELLOW_LIGHT.toString());
        assertEquals(drillChartReport.getYaxisTitle(), METRIC_BEST_CASE);
        assertEquals(drillChartReport.getXaxisLabels(), asList("Direct Sales", "2011"));
        assertEquals(drillChartReport.getDataLabels(), asList("13,273,818"));
        assertEquals(drillModalDialog.getTitleInsight(), TARGET_INSIGHT_FIRST);
        drillModalDialog.close();

        initAnalysePage().openInsight(SOURCE_INSIGHT_HAS_TWO_MEASURES).waitForReportComputing()
            .removeMetric(METRIC_BEST_CASE).saveInsight().waitForReportComputing();
        initIndigoDashboardsPage().selectKpiDashboard(DASHBOARD_TEST_REMOVING);

        indigoDashboardsPage.switchToEditMode();
        assertEquals(ElementUtils.getElementTexts(BY_WARNING_MESSAGE_BAR, browser),
            asList("Some interactions were removedShow more"));

        indigoDashboardsPage.selectWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_TWO_MEASURES).clickDeleteButton();
        try {
            waitForElementNotVisible(BY_WARNING_MESSAGE_BAR);
        } catch (TimeoutException e) {
        }
        assertFalse(isElementPresent(BY_WARNING_MESSAGE_BAR, browser), "The warning message should be hidden");

        indigoDashboardsPage.saveEditModeWithWidgets();

        chartReport.openDrillingPicker(Pair.of(0, 1)).drillToInsight();
        indigoDashboardsPage.waitForDrillModalDialogLoading();
        assertEquals(drillChartReport.getYaxisTitle(), METRIC_BEST_CASE);
        assertEquals(drillChartReport.getXaxisLabels(), asList("Direct Sales", "2011"));
        assertEquals(drillChartReport.getDataLabels(), asList("13,273,818"));
        assertEquals(drillModalDialog.getTitleInsight(), TARGET_INSIGHT_FIRST);
        drillModalDialog.close();

        indigoDashboardsPage.addDashboard().addInsight(SOURCE_INSIGHT_HAS_PROTECTED_DATA)
            .changeDashboardTitle(DASHBOARD_TEST_PROTECTED);
        indigoDashboardsPage.selectWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_PROTECTED_DATA);
        configurationPanel.drillIntoInsight(METRIC_AMOUNT, SOURCE_INSIGHT_HAS_PROTECTED_DATA);
        indigoDashboardsPage.saveEditModeWithWidgets();
    }

    @Test(dependsOnMethods = {"drillToInsightOnKPIDashboards"})
    public void drillToInsightWithProtectedAttribute() throws IOException{
        try {
            addUsersWithOtherRolesToProject();
            logoutAndLoginAs(true, UserRoles.EDITOR);
            initIndigoDashboardsPage().selectKpiDashboard(DASHBOARD_TEST_PROTECTED)
                .openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
            indigoDashboardsPage.waitForWidgetsLoading();
            indigoDashboardsPage.switchToEditMode().selectWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_PROTECTED_DATA);
            assertFalse(isElementVisible(DrillConfigPanel.ROOT, browser), "Cannot see the drilling configuration");

            indigoDashboardsPage.selectWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_PROTECTED_DATA)
                .setHeadline(SOURCE_INSIGHT_HAS_PROTECTED_DATA + " Editor");
            indigoDashboardsPage.saveEditModeWithWidgets();

            logoutAndLoginAs(true, UserRoles.ADMIN);
            initIndigoDashboardsPage().selectKpiDashboard(DASHBOARD_TEST_PROTECTED).openExtendedDateFilterPanel()
                .selectPeriod(DateRange.ALL_TIME).apply();

            ChartReport chartReport = indigoDashboardsPage
                .getWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_PROTECTED_DATA + " Editor").getChartReport();

            chartReport.clickOnElement(Pair.of(0, 0));
            indigoDashboardsPage.waitForDrillModalDialogLoading();

            DrillModalDialog drillModalDialog = DrillModalDialog.getInstance(browser);
            ChartReport drillChartReport = drillModalDialog.getChartReport();

            assertEquals(drillChartReport.getYaxisTitle(), METRIC_AMOUNT);
            assertEquals(drillChartReport.getXaxisLabels(), asList("Exclude"));
            assertEquals(drillChartReport.getDataLabels(), asList("$48,932,639.59"));
            assertEquals(drillModalDialog.getTitleInsight(), SOURCE_INSIGHT_HAS_PROTECTED_DATA);

            deleteAttribute(ATTR_FORECAST_CATEGORY);

            initIndigoDashboardsPage().selectKpiDashboard(DASHBOARD_TEST_PROTECTED).openExtendedDateFilterPanel()
                .selectPeriod(DateRange.ALL_TIME).apply();

            chartReport = indigoDashboardsPage
                .getWidgetByHeadline(Insight.class, SOURCE_INSIGHT_HAS_PROTECTED_DATA + " Editor").getChartReport();

            chartReport.clickOnElement(Pair.of(0, 0));
            indigoDashboardsPage.waitForDrillModalDialogLoading();

            drillModalDialog = DrillModalDialog.getInstance(browser);
            drillChartReport = drillModalDialog.getChartReport();

            assertEquals(drillChartReport.getYaxisTitle(), METRIC_AMOUNT);
            assertEquals(drillChartReport.getXaxisLabels(), asList("Exclude"));
            assertEquals(drillChartReport.getDataLabels(), asList("$48,932,639.59"));
            assertEquals(drillModalDialog.getTitleInsight(), SOURCE_INSIGHT_HAS_PROTECTED_DATA);
        } finally {
            logoutAndLoginAs(true, UserRoles.ADMIN);
        }
    }

    private void deleteAttribute(String attribute) {
        initAttributePage().initAttribute(attribute)
            .deleteObject();
    }
}
