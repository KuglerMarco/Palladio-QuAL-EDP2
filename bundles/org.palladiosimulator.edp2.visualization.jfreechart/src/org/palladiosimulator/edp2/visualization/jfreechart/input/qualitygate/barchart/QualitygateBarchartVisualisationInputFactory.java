package org.palladiosimulator.edp2.visualization.jfreechart.input.qualitygate.barchart;

import org.eclipse.ui.IMemento;
import org.palladiosimulator.edp2.datastream.configurable.IPropertyConfigurable;
import org.palladiosimulator.edp2.visualization.jfreechart.input.JFreeChartVisualizationInputFactory;

public class QualitygateBarchartVisualisationInputFactory extends JFreeChartVisualizationInputFactory {

    public static final String FACTORY_ID = QualitygateBarchartVisualisationInputFactory.class.getCanonicalName();
    
    @Override
    protected IPropertyConfigurable createElementInternal(IMemento memento) {
        return new QualitygateBarchartVisualisationInput();
    }

}
