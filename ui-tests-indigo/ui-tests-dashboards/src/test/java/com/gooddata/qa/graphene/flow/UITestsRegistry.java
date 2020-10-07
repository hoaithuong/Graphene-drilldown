package com.gooddata.qa.graphene.flow;

import com.gooddata.qa.graphene.indigo.dashboards.CommonDateFilteringTest;
import com.gooddata.qa.graphene.indigo.dashboards.DashboardHeadersTest;
import com.gooddata.qa.graphene.indigo.dashboards.DataSetTest;
import com.gooddata.qa.graphene.indigo.dashboards.DateDatasetRecommendationTest;
import com.gooddata.qa.graphene.indigo.dashboards.DateFilterConfigurationTest;
import com.gooddata.qa.graphene.indigo.dashboards.DateFilterOnCategoryBucketTest;
import com.gooddata.qa.graphene.indigo.dashboards.DateFilteringOnInsightTest;
import com.gooddata.qa.graphene.indigo.dashboards.DeleteAttributeFilterTest;
import com.gooddata.qa.graphene.indigo.dashboards.DragWidgetsTest;
import com.gooddata.qa.graphene.indigo.dashboards.ExtendedDateFilterTest;
import com.gooddata.qa.graphene.indigo.dashboards.FilteringWidgetsTest;
import com.gooddata.qa.graphene.indigo.dashboards.EmbeddingDashboardPostMessageTest;
import com.gooddata.qa.graphene.indigo.dashboards.EmbeddingSingleDashboardTest;
import com.gooddata.qa.graphene.indigo.dashboards.ExportDataToXLSXAndCSVDashboardsTest;
import com.gooddata.qa.graphene.indigo.dashboards.ExportDataToXLSXAndCSVDashboardsMediumTest;
import com.gooddata.qa.graphene.indigo.dashboards.ExportDataToXLSXAndCSVDashboardsAdvancedTest;
import com.gooddata.qa.graphene.indigo.dashboards.HeaderTest;
import com.gooddata.qa.graphene.indigo.dashboards.InsightOnDashboardTest;
import com.gooddata.qa.graphene.indigo.dashboards.InvalidDateDataSetTest;
import com.gooddata.qa.graphene.indigo.dashboards.KpiCompareToPreviousPeriodTest;
import com.gooddata.qa.graphene.indigo.dashboards.KpiDashboardCreationTest;
import com.gooddata.qa.graphene.indigo.dashboards.KpiDashboardsParamsTest;
import com.gooddata.qa.graphene.indigo.dashboards.ManipulateWidgetsTest;
import com.gooddata.qa.graphene.indigo.dashboards.MetricsDropdownTest;
import com.gooddata.qa.graphene.indigo.dashboards.MultipleAttributeFilterManipulationTest;
import com.gooddata.qa.graphene.indigo.dashboards.MultipleAttributeFilteringTest;
import com.gooddata.qa.graphene.indigo.dashboards.NonProductionDatasetTest;
import com.gooddata.qa.graphene.indigo.dashboards.PartialExportDashboardsTest;
import com.gooddata.qa.graphene.indigo.dashboards.ReorderInsightTest;
import com.gooddata.qa.graphene.indigo.dashboards.RoutingTest;
import com.gooddata.qa.graphene.indigo.dashboards.VisualizationsTest;
import com.gooddata.qa.graphene.indigo.dashboards.AttributeFilterManipulationTest;
import com.gooddata.qa.graphene.indigo.dashboards.AttributeFilterMiscTest;
import com.gooddata.qa.graphene.indigo.dashboards.KpiPermissionsTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.ContributionAndComparisionTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingBasicInsightTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingInEditMode;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingMultipleWidgetsTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingWidgetDrillToDashboardTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingWidgetDrillToPreventDefaultDashboardTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingWidgetEditModeTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingWidgetWithPoPTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.EventingWidgetWithoutDrillToDashBoardTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.VisualizationDrillableWidgetTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.VisualizationInvalidDrillConfigTest;
import com.gooddata.qa.graphene.indigo.dashboards.eventing.VisualizationMeasureAttributeTest;
import com.gooddata.qa.graphene.indigo.dashboards.KpiApplyColorPaletteTest;
import com.gooddata.qa.graphene.indigo.dashboards.ReportPageApplyColorPaletteTest;
import com.gooddata.qa.graphene.indigo.dashboards.KPIDashboardCRUDAndMovingTest;
import com.gooddata.qa.graphene.indigo.dashboards.ResizeWidgetsAndRowsTest;
import com.gooddata.qa.graphene.indigo.dashboards.ExportKPIDashboardsToPDF;
import com.gooddata.qa.graphene.indigo.dashboards.KPIDashboardsDrillToInsightTest;
import com.gooddata.qa.graphene.indigo.dashboards.KPIDashboardsDrillDownTest;
import com.gooddata.qa.graphene.indigo.dashboards.postMessage.ControlFilterEmbeddedKDFromExternalApplication;
import com.gooddata.qa.utils.flow.TestsRegistry;

import java.util.HashMap;
import java.util.Map;

public class UITestsRegistry {

    public static void main(String[] args) throws Throwable {
        Map<String, Object> suites = new HashMap<>();

        suites.put("sanity", new Object[] {
            ManipulateWidgetsTest.class,
            EventingBasicInsightTest.class,
            VisualizationDrillableWidgetTest.class,
            EventingWidgetDrillToDashboardTest.class,
            KpiApplyColorPaletteTest.class,
            "testng-desktop-EditMode.xml",
            "testng-desktop-imap-KpiAlertEvaluate.xml",
            "testng-desktop-SplashScreen.xml"
        });

        suites.put("pull-request", new Object[] {
            ManipulateWidgetsTest.class,
            MetricsDropdownTest.class,
            VisualizationsTest.class,
            InsightOnDashboardTest.class,
            DateFilteringOnInsightTest.class,
            EmbeddingDashboardPostMessageTest.class,
            MultipleAttributeFilterManipulationTest.class,
            "testng-desktop-KpiDrillTo.xml",
            "testng-desktop-imap-KpiAlert.xml",
            "testng-desktop-KpiDashboardWithTotalsResult.xml",
            "testng-mobile-KpiDrillTo.xml",
            "testng-mobile-ResponsiveNavigation.xml",
            "testng-mobile-DropDownNavigation.xml",
            "testng-mobile-KpiDashboardWithTotalsResult.xml"
        });

        suites.put("all", new Object[] {
            DataSetTest.class,
            HeaderTest.class,
            ManipulateWidgetsTest.class,
            DragWidgetsTest.class,
            PartialExportDashboardsTest.class,
            MetricsDropdownTest.class,
            VisualizationsTest.class,
            ReorderInsightTest.class,
            DashboardHeadersTest.class,
            NonProductionDatasetTest.class,
            InsightOnDashboardTest.class,
            RoutingTest.class,
            DateFilteringOnInsightTest.class,
            CommonDateFilteringTest.class,
            DateDatasetRecommendationTest.class,
            FilteringWidgetsTest.class,
            EmbeddingSingleDashboardTest.class,
            EmbeddingDashboardPostMessageTest.class,
            InvalidDateDataSetTest.class,
            DateFilterOnCategoryBucketTest.class,
            AttributeFilterManipulationTest.class,
            AttributeFilterMiscTest.class,
            DeleteAttributeFilterTest.class,
            KpiPermissionsTest.class,
            KpiDashboardCreationTest.class,
            KpiDashboardsParamsTest.class,
            MultipleAttributeFilteringTest.class,
            MultipleAttributeFilterManipulationTest.class,
            EventingBasicInsightTest.class,
            VisualizationMeasureAttributeTest.class,
            ContributionAndComparisionTest.class,
            EventingInEditMode.class,
            VisualizationDrillableWidgetTest.class,
            VisualizationInvalidDrillConfigTest.class,
            EventingWidgetWithPoPTest.class,
            EventingWidgetWithoutDrillToDashBoardTest.class,
            EventingWidgetEditModeTest.class,
            EventingWidgetDrillToPreventDefaultDashboardTest.class,
            EventingWidgetDrillToDashboardTest.class,
            EventingMultipleWidgetsTest.class,
            KpiCompareToPreviousPeriodTest.class,
            ReportPageApplyColorPaletteTest.class,
            KpiApplyColorPaletteTest.class,
            ExportDataToXLSXAndCSVDashboardsTest.class,
            ExportDataToXLSXAndCSVDashboardsMediumTest.class,
            ExportDataToXLSXAndCSVDashboardsAdvancedTest.class,
            KPIDashboardCRUDAndMovingTest.class,
            ResizeWidgetsAndRowsTest.class,
            ExportKPIDashboardsToPDF.class,
            KPIDashboardsDrillToInsightTest.class,
            KPIDashboardsDrillDownTest.class,
            ControlFilterEmbeddedKDFromExternalApplication.class,
            "testng-desktop-AttributeFiltering.xml",
            "testng-desktop-DateFiltering.xml",
            "testng-desktop-EditMode.xml",
            "testng-desktop-imap-KpiAlertEvaluate.xml",
            "testng-desktop-KpiDrillTo.xml",
            "testng-desktop-KpiPop.xml",
            "testng-desktop-SplashScreen.xml",
            "testng-desktop-MetricFormatting.xml",
            "testng-desktop-ResponsiveNavigation.xml",
            "testng-desktop-KpiPopChangeValueExceedLimit.xml",
            "testng-desktop-MetricsAccessibility.xml",
            "testng-desktop-ProjectSwitch.xml",
            "testng-desktop-imap-KpiAlertNullValue.xml",
            "testng-desktop-imap-KpiValueFormatInAlertEmail.xml",
            "testng-desktop-imap-KpiAlertSpecialCaseTest.xml",
            "testng-desktop-EmptyErrorKpiValue.xml",
            "testng-desktop-imap-KpiAlert.xml",
            "testng-desktop-KpiDashboards.xml",
            "testng-desktop-KpiDashboardWithTotalsResult.xml",
            "testng-mobile-AttributeFiltering.xml",
            "testng-mobile-DateFiltering.xml",
            "testng-mobile-EditMode.xml",
            "testng-mobile-KpiDrillTo.xml",
            "testng-mobile-KpiPop.xml",
            "testng-mobile-MetricFormatting.xml",
            "testng-mobile-ResponsiveNavigation.xml",
            "testng-mobile-SplashScreen.xml",
            "testng-mobile-KpiPopChangeValueExceedLimit.xml",
            "testng-mobile-ProjectSwitch.xml",
            "testng-mobile-KpiDashboards.xml",
            "testng-mobile-DropDownNavigation.xml",
            "testng-mobile-KpiDashboardWithTotalsResult.xml",
            "testng-mobile-EventingBasicInsight.xml",
            "testng-mobile-EventingMultipleWidgets.xml"
        });

        suites.put("crud", new Object[] {
            ManipulateWidgetsTest.class,
            DragWidgetsTest.class,
            MetricsDropdownTest.class,
            VisualizationsTest.class,
            ReorderInsightTest.class,
            "testng-desktop-EditMode.xml",
            "testng-desktop-SplashScreen.xml",
            "testng-desktop-KpiDashboards.xml",
            "testng-mobile-EditMode.xml",
            "testng-mobile-SplashScreen.xml",
            "testng-mobile-KpiDashboards.xml"
        });

        suites.put("alerts", new Object[] {
            "testng-desktop-imap-KpiAlertEvaluate.xml",
            "testng-desktop-imap-KpiValueFormatInAlertEmail.xml",
            "testng-desktop-imap-KpiAlertSpecialCaseTest.xml",
            "testng-desktop-imap-KpiAlert.xml",
            "testng-desktop-imap-KpiAlertNullValue.xml"
        });

        suites.put("filters", new Object[] {
            DateFilteringOnInsightTest.class,
            CommonDateFilteringTest.class,
            FilteringWidgetsTest.class,
            DateFilterOnCategoryBucketTest.class,
            AttributeFilterManipulationTest.class,
            AttributeFilterMiscTest.class,
            DeleteAttributeFilterTest.class,
            MultipleAttributeFilteringTest.class,
            MultipleAttributeFilterManipulationTest.class,
            KpiCompareToPreviousPeriodTest.class,
            ExtendedDateFilterTest.class,
            DateFilterConfigurationTest.class,
            "testng-desktop-AttributeFiltering.xml",
            "testng-desktop-DateFiltering.xml",
            "testng-mobile-AttributeFiltering.xml",
            "testng-mobile-DateFiltering.xml"
        });

        suites.put("drilling", new Object[] {
            "testng-desktop-KpiDrillTo.xml",
            "testng-mobile-KpiDrillTo.xml"
        });

        suites.put("total-results", new Object[] {
            "testng-desktop-KpiDashboardWithTotalsResult.xml",
            "testng-mobile-KpiDashboardWithTotalsResult.xml"
        });

        suites.put("drill-eventing", new Object[] {
            ContributionAndComparisionTest.class,
            EventingBasicInsightTest.class,
            EventingInEditMode.class,
            EventingMultipleWidgetsTest.class,
            EventingWidgetDrillToDashboardTest.class,
            EventingWidgetDrillToPreventDefaultDashboardTest.class,
            EventingWidgetEditModeTest.class,
            EventingWidgetWithoutDrillToDashBoardTest.class,
            EventingWidgetWithPoPTest.class,
            VisualizationDrillableWidgetTest.class,
            VisualizationInvalidDrillConfigTest.class,
            VisualizationMeasureAttributeTest.class
        });

        TestsRegistry.getInstance()
            .register(args, suites)
            .toTextFile();
    }
}
