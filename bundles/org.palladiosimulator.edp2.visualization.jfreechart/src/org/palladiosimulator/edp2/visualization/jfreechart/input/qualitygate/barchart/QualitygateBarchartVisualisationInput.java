package org.palladiosimulator.edp2.visualization.jfreechart.input.qualitygate.barchart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

public class QualitygateBarchartVisualisationInput extends JFreeChartVisualizationInput {

    public QualitygateBarchartVisualisationInput() {
        this(null);
    }
    
    
    public QualitygateBarchartVisualisationInput(final AbstractDataSource source) {
        super();
    }
    
    @Override
    public void saveState(final IMemento memento) {
        QualitygateBarchartVisualisationInputFactory.saveState(memento, this);
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
                    .equals("InvolvedIssues")) {
            return false;
        }

        return subMetricDescriptions[1].getScale()
            .compareTo(Scale.ORDINAL) <= 0;
        
        
    }


    @Override
    public String getFactoryId() {
        return QualitygateBarchartVisualisationInputFactory.FACTORY_ID; 
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
        final Map<String, Integer> bins = new LinkedHashMap<String, Integer>();
        final IDataSource datasource = getInputs().get(0).getDataSource();
        final IDataStream<TupleMeasurement> datastream = datasource.getDataStream();
        
        
        
        for(final TupleMeasurement tuple : datastream) {
            
            final String state =  (String) ((Identifier) tuple.asArray()[1].getValue()).getLiteral();
            
            if (!bins.containsKey(state)) {
                bins.put(state, 1);
            }
            bins.put(state, bins.get(state) + 1);

        }
        
        
        List<Entry<String, Integer>> list = new ArrayList<>(bins.entrySet());
        list.sort(Entry.comparingByValue(Comparator.reverseOrder()));
        bins.clear();
        
        for(Entry<String, Integer> entry : list) {
            bins.put(entry.getKey(), entry.getValue());
        }
        
        for (final String o : bins.keySet()) {
            if(o.equals("Overall count of violation")) {
                dataset.setValue(bins.get(o), o, o);
            }
        }
        
        
        for (final String o : bins.keySet()) {
            if(!o.equals("Overall count of violation")) {
                dataset.setValue(bins.get(o), o, o);
            }
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


    @Override
    protected Set<String> getPropertyKeysTriggeringUpdate() {
        return Collections.emptySet();
    }
    
    
}
