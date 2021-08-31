package org.palladiosimulator.edp2.visualization.jfreechart.input.qualitygateHistogram;

import org.eclipse.ui.IMemento;
import org.palladiosimulator.edp2.datastream.configurable.IPropertyConfigurable;
import org.palladiosimulator.edp2.visualization.jfreechart.input.JFreeChartVisualizationInputFactory;

public class QualitygateHistogramVisualisationFactory extends JFreeChartVisualizationInputFactory {

    public static final String FACTORY_ID = QualitygateHistogramVisualisationFactory.class.getCanonicalName();
    
    @Override
    protected IPropertyConfigurable createElementInternal(IMemento memento) {
        return new QualitygateHistogramVisualisationInput();
    }

}
