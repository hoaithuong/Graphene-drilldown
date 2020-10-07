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
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_AVG_AMOUNT;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_PRODUCT;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_DEPARTMENT;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_SALES_REP;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_BEST_CASE;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_OPPORTUNITY;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_STAGE_NAME;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.ATTR_FORECAST_CATEGORY;
import static com.gooddata.qa.graphene.utils.GoodSalesUtils.METRIC_TIMELINE_EOP;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class KPIDashboardsDrillDownTest extends AbstractDashboardTest {

    private final String SOURCE_INSIGHT_HAS_TWO_MEASURES = "Two Measures";
    private final String SOURCE_INSIGHT_HAS_PROTECTED_DATA = "Protected Data";
    private final String TARGET_INSIGHT_FIRST = "First Insight";
    private final String TARGET_INSIGHT_SECOND = "Second Insight";
    private final String DATE = "Date";
    private final String DASHBOARD_TEST_REMOVING = "Removing";
    private final String DASHBOARD_TEST_PROTECTED = "PROTECTED";
    private final String TABLE_ONLY_MEASURES_COLUMNS = "Table only measures and columns";
    private final String DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_MEASURES_COLUMNS = "Dashboard drill down with table only measures and columns";
    private final String TABLE_ONLY_ROWS = "Table only rows";
    private final String DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS = "Dashboard drill down with table only rows";
    private final String TABLE_ONLY_ROWS_MEASURES = "Table only rows and measures";
    private final String DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS_MEASURES = "Dashboard drill down with table only rows and measures";
    private final String TABLE_ONLY_ROWS_COLUMNS = "Table only rows and columns";
    private final String DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS_COLUMNS = "Dashboard drill down with table only rows and columns";
    private final String TABLE_HAS_MEASURES_ROWS_COLUMNS = "Table has measure rows and columns";
    private final String DASHBOARD_DRILL_DOWN_WITH_TABLE_HAS_MEASURES_ROWS_COLUMNS = "Dashboard drill down with table only measures rows and columns";


    private IndigoRestRequest indigoRestRequest;
    ProjectRestRequest projectRestRequest;
    AttributeRestRequest attributeRestRequest;

    @Override
    protected void customizeProject() throws Throwable {
        getMetricCreator().createSnapshotBOPMetric();
        getMetricCreator().createSnapshotEOPMetric();
        getMetricCreator().createTimelineEOPMetric();
        getMetricCreator().createTimelineBOPMetric();
        getMetricCreator().createAmountMetric();
        getMetricCreator().createAvgAmountMetric();
        getMetricCreator().createBestCaseMetric();
        indigoRestRequest = new IndigoRestRequest(new RestClient(getProfile(Profile.ADMIN)), testParams.getProjectId());
        projectRestRequest = new ProjectRestRequest(
            new RestClient(getProfile(ADMIN)), testParams.getProjectId());
        projectRestRequest.setFeatureFlagInProjectAndCheckResult(
            ProjectFeatureFlags.ENABLE_KPI_DASHBOARD_DRILL_TO_INSIGHT, true);
        projectRestRequest.setFeatureFlagInProjectAndCheckResult(
            ProjectFeatureFlags.ENABLE_KPI_DASHBOARD_DRILL_TO_DASHBOARD, true);
        projectRestRequest.setFeatureFlagInProjectAndCheckResult(
            ProjectFeatureFlags.ENABLE_KPI_DASHBOARD_DRILL_DOWN, true);

        attributeRestRequest = new AttributeRestRequest(new RestClient(getProfile(Profile.ADMIN)), testParams.getProjectId());
        attributeRestRequest.setDrillDown(ATTR_SALES_REP, getAttributeDisplayFormUri(ATTR_STAGE_NAME));

        createInsightWidget(new InsightMDConfiguration(TABLE_ONLY_MEASURES_COLUMNS, ReportType.TABLE)
            .setMeasureBucket(asList(
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AMOUNT)),
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AVG_AMOUNT))))
            .setCategoryBucket(asList(
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_SALES_REP),
                        CategoryBucket.Type.COLUMNS),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.COLUMNS))));

        createInsightWidget(new InsightMDConfiguration(TABLE_ONLY_ROWS, ReportType.TABLE)
            .setCategoryBucket(asList(
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_SALES_REP),
                        CategoryBucket.Type.ATTRIBUTE),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.ATTRIBUTE))));

        createInsightWidget(new InsightMDConfiguration(TABLE_ONLY_ROWS_MEASURES, ReportType.TABLE)
            .setMeasureBucket(asList(
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AMOUNT)),
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AVG_AMOUNT))))
            .setCategoryBucket(asList(
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_SALES_REP),
                        CategoryBucket.Type.ATTRIBUTE),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.ATTRIBUTE))));
        
        createInsightWidget(new InsightMDConfiguration(TABLE_ONLY_ROWS_COLUMNS, ReportType.TABLE)
            .setCategoryBucket(asList(
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_SALES_REP),
                        CategoryBucket.Type.ATTRIBUTE),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.COLUMNS),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_PRODUCT),
                        CategoryBucket.Type.COLUMNS))));

        createInsightWidget(new InsightMDConfiguration(TABLE_HAS_MEASURES_ROWS_COLUMNS, ReportType.TABLE)
            .setMeasureBucket(asList(
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AMOUNT)),
                    MeasureBucket.createSimpleMeasureBucket(getMetricByTitle(METRIC_AVG_AMOUNT))))
            .setCategoryBucket(asList(
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_SALES_REP),
                        CategoryBucket.Type.ATTRIBUTE),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_DEPARTMENT),
                        CategoryBucket.Type.COLUMNS),
                CategoryBucket.createCategoryBucket(getAttributeByTitle(ATTR_PRODUCT),
                        CategoryBucket.Type.COLUMNS))));

}

    @Override
    protected void addUsersWithOtherRolesToProject() throws JSONException, IOException {
        createAndAddUserToProject(UserRoles.EDITOR);
    }

    @Test(dependsOnGroups = "createProject")
    public void drillDownOnTableOnlyMeasuresAndColumns() {
        initIndigoDashboardsPage().addDashboard().addInsight(TABLE_ONLY_MEASURES_COLUMNS);
        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_MEASURES_COLUMNS).waitForWidgetsLoading().saveEditModeWithWidgets();
    }

    @Test(dependsOnGroups = "createProject")
    public void drillDownOnTableOnlyRowsWithOneDrillableAttribute() {
        initIndigoDashboardsPage().addDashboard().addInsight(TABLE_ONLY_ROWS);
        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS).waitForWidgetsLoading().saveEditModeWithWidgets();
    }

    @Test(dependsOnGroups = "createProject")
    public void drillDownOnTableOnlyRowsAndMeasuresWithOneAttribute() {
        initIndigoDashboardsPage().addDashboard().addInsight(TABLE_ONLY_ROWS_MEASURES);
        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS_MEASURES).waitForWidgetsLoading().saveEditModeWithWidgets();
    }

    @Test(dependsOnGroups = "createProject")
    public void drillDownOnTableOnlyRowsAndColumnsWithOneAttribute() {
        initIndigoDashboardsPage().addDashboard().addInsight(TABLE_ONLY_ROWS_COLUMNS);
        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_DRILL_DOWN_WITH_TABLE_ONLY_ROWS_COLUMNS).waitForWidgetsLoading().saveEditModeWithWidgets();
    }

    @Test(dependsOnGroups = "createProject")
    public void drillDownOnTableOnlyRowsMeasuresAndColumnsWithOneAttribute() {
        initIndigoDashboardsPage().addDashboard().addInsight(TABLE_HAS_MEASURES_ROWS_COLUMNS);
        indigoDashboardsPage.openExtendedDateFilterPanel().selectPeriod(DateRange.ALL_TIME).apply();
        indigoDashboardsPage.changeDashboardTitle(DASHBOARD_DRILL_DOWN_WITH_TABLE_HAS_MEASURES_ROWS_COLUMNS).waitForWidgetsLoading().saveEditModeWithWidgets();
    }

    private void deleteAttribute(String attribute) {
        initAttributePage().initAttribute(attribute)
            .deleteObject();
    }
}
