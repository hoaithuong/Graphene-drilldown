package com.gooddata.qa.graphene.fragments.indigo.analyze.pickerDropDown;

import com.gooddata.qa.graphene.fragments.AbstractFragment;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.DrillModalDialog;
import com.gooddata.qa.graphene.fragments.indigo.dashboards.IndigoDashboardsPage;
import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.gooddata.qa.graphene.utils.WaitUtils.*;
import static org.openqa.selenium.By.className;

public class Drilling extends AbstractFragment {

    @FindBy(className = "drill-down")
    private WebElement drillDownItem;

    @FindBy(className = "drill-to-dashboard")
    private WebElement drillToDashboard;

    @FindBy(className = "drill-to-insight")
    private WebElement drillToInsight;

    @FindBy(className = "icon-hyperlink-disabled")
    private WebElement drillToURL;

    public static final String ROOT_CLASS = "gd-drill-modal-picker-dropdown";

    public static Drilling getInstance(SearchContext searchContext) {
        return Graphene.createPageFragment(Drilling.class,
                waitForElementVisible(className(ROOT_CLASS), searchContext));
    }

    public IndigoDashboardsPage drillToDashboard() {
        waitForElementVisible(drillToDashboard).click();
        waitForFragmentNotVisible(this);
        return IndigoDashboardsPage.getInstance(browser);
    }

    public DrillModalDialog drillToInsight() {
        waitForElementVisible(drillToInsight).click();
        waitForFragmentNotVisible(this);
        return DrillModalDialog.getInstance(browser);
    }

    public void drillToURL() {
        waitForElementVisible(drillToURL).click();
        waitForFragmentNotVisible(this);
    }
}
