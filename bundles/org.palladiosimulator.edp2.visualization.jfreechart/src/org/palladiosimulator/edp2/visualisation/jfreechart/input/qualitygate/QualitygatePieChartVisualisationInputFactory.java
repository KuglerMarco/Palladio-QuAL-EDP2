package org.palladiosimulator.edp2.visualisation.jfreechart.input.qualitygate;

import org.eclipse.ui.IMemento;
import org.palladiosimulator.edp2.datastream.configurable.IPropertyConfigurable;
import org.palladiosimulator.edp2.visualization.jfreechart.input.JFreeChartVisualizationInputFactory;

public class QualitygatePieChartVisualisationInputFactory extends JFreeChartVisualizationInputFactory {

    public static final String FACTORY_ID = QualitygatePieChartVisualisationInputFactory.class.getCanonicalName();
    
    @Override
    protected IPropertyConfigurable createElementInternal(IMemento memento) {
        return new QualitygatePieChartVisualisationInput();
    }

}
