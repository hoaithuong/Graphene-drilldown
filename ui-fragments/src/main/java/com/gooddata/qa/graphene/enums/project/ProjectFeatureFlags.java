package com.gooddata.qa.graphene.enums.project;

public enum ProjectFeatureFlags {

    HIDE_DASHBOARD_SCHEDULE("hideDashboardSchedule"),
    DASHBOARD_SCHEDULE_RECIPIENTS("dashboardScheduleRecipients"),
    DISPLAY_USER_MANAGEMENT("displayUserManagement"),
    NPS_STATUS("npsStatus"),
    ANALYTICAL_DESIGNER("analyticalDesigner"),
    ENABLE_CSV_UPLOADER("enableCsvUploader"),
    ENABLE_ANALYTICAL_DASHBOARDS("enableAnalyticalDashboards"),
    DISABLE_SAVED_FILTERS("disableSavedFilters"),
    ENABLE_CHANGE_LANGUAGE("enableChangeLanguage"),
    ENABLE_SECTION_HEADERS("enableSectionHeaders"),
    HIDE_KPI_ALERT_LINK("hideKPIAlertLinks"),
    FISCAL_CALENDAR_ENABLED("fiscalCalendarEnabled"),
    DASHBOARD_ACCESS_CONTROL("dashboardAccessControlEnabled"),
    USE_AVAILABLE_ENABLED("useAvailableEnabled"),
    ENABLE_ETL_COMPONENT("enableEtlComponent"),
    CONTROL_EXECUTION_CONTEXT_ENABLED("controlExecutionContextEnabled"),
    EXPORT_TO_XLSX_ENABLED("exportToXLSXEnabled"),
    CELL_MERGED_BY_DEFAULT("cellMergedByDefault"),
    ACTIVE_FILTERS_BY_DEFAULT("activeFiltersByDefault"),
    REPORT_HEADER_PAGING_ENABLED("reportHeaderPagingEnabled"),
    ENABLE_ANALYTICAL_DESIGNER_EXPORT("enableAnalyticalDesignerExport"),
    ENABLE_ACTIVE_FILTER_CONTEXT("enableActiveFilterContext"),
    ENABLE_METRIC_DATE_FILTER("enableMetricDateFilter"),
    ENABLE_CUSTOM_COLOR_PICKER("enableCustomColorPicker"),
    IS_REDIRECTED_FOR_ONE_PROJECT("isRedirectedForOneProject"),
    CASCADING_FILTERS_BOOSTING_ENABLE("cascadingFiltersBoostingEnabled"),
    ENABLE_KPI_DASHBOARD_EXTENDED_DATE_FILTERS("enableKPIDashboardExtendedDateFilters"),
    ENABLE_WEEK_FILTERS("enableWeekFilters"),
    ENABLE_KPI_DASHBOARD_EXPORT_PDF("enableKPIDashboardExportPDF"),
    AD_CATALOG_GROUPING("ADCatalogGrouping"),
    DISABLE_ZEBRA_EFFECT("disableZebraEffect"),
    TABLE_HEADER_FONT_SIZE("tableHeaderFontSize"),
    TABLE_BODY_FONT_SIZE("tableBodyFontSize"),
    ENABLE_NEW_AD_FILTER_BAR("enableNewADFilterBar"),
    ENABLE_MEASURE_VALUE_FILTERS("enableMeasureValueFilters"),
    ENABLE_KPI_DASHBOARD_SCHEDULE("enableKPIDashboardSchedule"),
    ENABLE_KPI_DASHBOARD_SCHEDULE_RECIPIENTS("enableKPIDashboardScheduleRecipients"),
    PRESERVE_AREA_CHART_DATA_ORDER("preserveAreaChartDataOrder"),
    ENABLE_ACCURATE_PIE_CHART("accuratePieChartEnabled"),
    ENABLE_KPI_DASHBOARD_DRILL_TO_INSIGHT("enableKPIDashboardDrillToInsight"),
    ENABLE_KPI_DASHBOARD_DRILL_TO_DASHBOARD("enableKPIDashboardDrillToDashboard"),
    ENABLE_KPI_DASHBOARD_DRILL_DOWN("enableKPIDashboardImplicitDrillDown"),
    ENABLE_TABLE_COLUMN_AUTO_RESIZING("enableTableColumnsAutoResizing"),
    AD_MEASURE_VALUE_FILTER_NULL_AS_ZERO_OPTION("ADMeasureValueFilterNullAsZeroOption"),
    ENABLE_KPI_DASHBOARD_SAVE_AS_NEW("enableKPIDashboardSaveAsNew"),
    XAE_VERSION("xae_version"),
    ENABLE_TABLE_COLUMNS_GROW_TO_FIT("enableTableColumnsGrowToFit");

    private final String featureFlag;

    ProjectFeatureFlags(String featureFlag) {
        this.featureFlag = featureFlag;
    }

    public String getFlagName() {
        return this.featureFlag;
    }
}
