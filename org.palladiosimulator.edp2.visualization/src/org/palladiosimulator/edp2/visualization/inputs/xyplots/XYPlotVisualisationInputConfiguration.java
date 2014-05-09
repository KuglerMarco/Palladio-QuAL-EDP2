package org.palladiosimulator.edp2.visualization.inputs.xyplots;

import java.util.HashSet;
import java.util.Set;

import org.palladiosimulator.edp2.visualization.editors.JFreeChartVisualisationConfiguration;

public class XYPlotVisualisationInputConfiguration extends JFreeChartVisualisationConfiguration {

    public static final String DOMAIN_AXIS_LABEL_KEY = "domainAxisLabel";
    public static final String RANGE_AXIS_LABEL_KEY = "rangeAxisLabel";
    public static final String SHOW_RANGE_AXIS_LABEL_KEY = "showRangeAxisLabel";
    public static final String SHOW_DOMAIN_AXIS_LABEL_KEY = "showDomainAxisLabel";

    /**
     * Label for the number axis (= horizontal axis)
     */
    private String domainAxisLabel;

    /**
     * Label for the range axis (= vertical axis)
     */
    private String rangeAxisLabel;

    /**
     * Show different labels?
     */
    private boolean showRangeAxisLabel = true;
    private boolean showDomainAxisLabel = true;

    /* (non-Javadoc)
     * @see org.palladiosimulator.edp2.visualization.editors.JFreeChartVisualisationConfiguration#getKeys()
     */
    @Override
    public Set<String> getKeys() {
        final Set<String> result = new HashSet<String>(super.getKeys());
        result.add(DOMAIN_AXIS_LABEL_KEY);
        result.add(RANGE_AXIS_LABEL_KEY);
        result.add(SHOW_RANGE_AXIS_LABEL_KEY);
        result.add(SHOW_DOMAIN_AXIS_LABEL_KEY);
        return result;
    }

    public String getDomainAxisLabel() {
        if (domainAxisLabel == null) {
            throw new IllegalStateException("Axis label queried but not set before.");
        }
        return domainAxisLabel;
    }

    public String getRangeAxisLabel() {
        if (rangeAxisLabel == null) {
            throw new IllegalStateException("Axis label queried but not set before.");
        }
        return rangeAxisLabel;
    }

    public boolean isShowRangeAxisLabel() {
        return showRangeAxisLabel;
    }

    public boolean isShowDomainAxisLabel() {
        return showDomainAxisLabel;
    }

    @Override
    public void propertyChanged(final String key, final Object oldValue, final Object newValue) {
        if (DOMAIN_AXIS_LABEL_KEY.equals(key)) {
            domainAxisLabel = (String) newValue;
        } else if (RANGE_AXIS_LABEL_KEY.equals(key)) {
            rangeAxisLabel = (String) newValue;
        } else if (SHOW_DOMAIN_AXIS_LABEL_KEY.equals(key)) {
            showDomainAxisLabel = "true".equals(newValue);
        } else if (SHOW_RANGE_AXIS_LABEL_KEY.equals(key)) {
            showRangeAxisLabel = "true".equals(newValue);
        }
    }
}
