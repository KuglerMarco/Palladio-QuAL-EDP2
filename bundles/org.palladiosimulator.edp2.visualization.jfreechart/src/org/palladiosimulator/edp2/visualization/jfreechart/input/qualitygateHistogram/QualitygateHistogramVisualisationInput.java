package org.palladiosimulator.edp2.visualization.jfreechart.input.qualitygateHistogram;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableDouble;
import org.eclipse.ui.IMemento;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.palladiosimulator.edp2.datastream.AbstractDataSource;
import org.palladiosimulator.edp2.datastream.IDataSource;
import org.palladiosimulator.edp2.datastream.IDataStream;
import org.palladiosimulator.edp2.datastream.configurable.PropertyConfigurable;
import org.palladiosimulator.edp2.util.MetricDescriptionUtility;
import org.palladiosimulator.edp2.visualization.jfreechart.input.JFreeChartVisualizationInput;
import org.palladiosimulator.measurementframework.TupleMeasurement;
import org.palladiosimulator.metricspec.BaseMetricDescription;
import org.palladiosimulator.metricspec.Identifier;
import org.palladiosimulator.metricspec.Scale;
import org.palladiosimulator.metricspec.constants.MetricDescriptionConstants;

public class QualitygateHistogramVisualisationInput extends JFreeChartVisualizationInput {

    public QualitygateHistogramVisualisationInput() {
        this(null);
    }
    
    
    public QualitygateHistogramVisualisationInput(final AbstractDataSource source) {
        super();
    }
    
    @Override
    public void saveState(final IMemento memento) {
        QualitygateHistogramVisualisationFactory.saveState(memento, this);
    }
    
    
    @Override
    public boolean canAccept(final IDataSource source) {
        final BaseMetricDescription[] subMetricDescriptions = MetricDescriptionUtility
            .toBaseMetricDescriptions(source.getMetricDesciption());
        if (subMetricDescriptions.length != 2) {
            return false; // two-dimensional data needed
        }

        if (!subMetricDescriptions[0].getId()
            .equals(MetricDescriptionConstants.POINT_IN_TIME_METRIC.getId())) {
            return false;
        }
        if (!subMetricDescriptions[1].getName()
            .equals("Severity")
                && !subMetricDescriptions[1].getName()
                    .equals("QualitygateViolation")) {
            return false;
        }

        return subMetricDescriptions[1].getScale()
            .compareTo(Scale.ORDINAL) <= 0;
        
        
    }


    @Override
    public String getFactoryId() {
        return QualitygateHistogramVisualisationFactory.FACTORY_ID; 
    }



    @Override
    protected Set<String> getPropertyKeysTriggeringUpdate() {
        return Collections.emptySet();
    }
    
    
    @Override
    protected Plot generatePlot(final PropertyConfigurable config, final AbstractDataset dataset) {
        
        final CategoryPlot plotResult = new CategoryPlot();
        final BarRenderer renderer = new BarRenderer();
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        
        final CategoryAxis domainAxis = new CategoryAxis("Issues");
        final NumberAxis rangeAxis = new NumberAxis("Frequency");
        
        plotResult.setDataset((CategoryDataset) dataset);
        
        plotResult.setRenderer(renderer);
        plotResult.setRangeAxis(rangeAxis);
        plotResult.setDomainAxis(domainAxis);
        
        return plotResult;
        
        
    }
    
    @Override
    protected AbstractDataset generateDataset() {
        
        
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final Map<String, MutableDouble> bins = new HashMap<String, MutableDouble>();
        final IDataSource datasource = getInputs().get(0).getDataSource();
        final IDataStream<TupleMeasurement> datastream = datasource.getDataStream();
        
        for(final TupleMeasurement tuple : datastream) {
            
            final String state =  (String) ((Identifier) tuple.asArray()[1].getValue()).getLiteral();
            
            if (!bins.containsKey(state)) {
                bins.put(state, new MutableDouble(0.0d));
            }
            bins.get(state)
                .add(1.0);

        }
        
        for (final String o : bins.keySet()) {
            dataset.setValue(bins.get(o)
                .doubleValue(), o, "test");
        }
        
        return dataset;
        
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.palladiosimulator.edp2.visualization.jfreechart.input.JFreeChartVisualizationInput#
     * getName()
     */
    @Override
    public String getName() {
        return "Propagation Results";
    }
    
}
