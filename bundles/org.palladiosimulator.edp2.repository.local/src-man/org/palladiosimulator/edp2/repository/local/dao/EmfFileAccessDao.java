package org.palladiosimulator.edp2.repository.local.dao;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.palladiosimulator.edp2.dao.EmfResourceDao;
import org.palladiosimulator.edp2.dao.exception.DataNotAccessibleException;
import org.palladiosimulator.edp2.models.ExperimentData.ExperimentDataFactory;
import org.palladiosimulator.edp2.repository.local.dao.internal.visitors.EmfmodelExtensionSwitch;

/**
 * Help class to manage file access for DAOs to file containing EMF models.
 * 
 * @author groenda
 */
abstract class EmfFileAccessDao extends FileAccessDao implements EmfResourceDao {
    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(EmfFileAccessDao.class.getCanonicalName());
    /** Factory for the EDP2 EMFmodel. */
    protected final static ExperimentDataFactory ModelFactory = ExperimentDataFactory.eINSTANCE;
    /** ResourceSet for the EDP2 EMFModel. */
    protected ResourceSet resourceSet = null;
    /** Resource for the EDP2 EMFModel of this instance. */
    protected Resource resource;
    /** Data from the resource. */
    protected EObject emfRootElement;

    /**
     * Initialized a new EmfFileAccessDao instance. Remember to set the resource file before
     * accessing data of this instance.
     */
    public EmfFileAccessDao() {
        super();
        resource = null;
    }

    @Override
    public synchronized void close() throws DataNotAccessibleException {
        super.close();
        try {
            resource.save(Collections.EMPTY_MAP);
            setClosed();
            emfRootElement = null;
            resource.unload();
        } catch (IOException e) {
            String errorMsg = "Could not close file " + resourceFile.getAbsolutePath()
                    + " and save the contained EMF model.";
            LOGGER.log(Level.SEVERE, errorMsg);
            throw new DataNotAccessibleException(errorMsg, e);
        }
    }

    @Override
    public synchronized void delete() throws DataNotAccessibleException {
        super.delete();
        try {
            emfRootElement = null;
            if (resource != null) {
                resource.delete(null);
            }
            setDeleted(true);
        } catch (IOException e) {
            setDeleted(false);
            String errorMsg = "Could not delete file " + resourceFile.getAbsolutePath()
                    + " and its contained EMF model.";
            LOGGER.log(Level.WARNING, errorMsg);
            throw new DataNotAccessibleException(errorMsg, e);
        }
    }

    @Override
    public synchronized void open(DiagnosticChain diagnostics) {
        super.open(diagnostics);
        String filename = resourceFile.getAbsolutePath();
        URI uri = URI.createFileURI(filename);
        
        if (resource == null) {
            resource = resourceSet.createResource(uri);
        }
        if (new File(resource.getURI().toFileString()).isFile()) {
            // File already exists
            try {
                resource.load(null);
            } catch (IOException ie) {
                LOGGER.log(Level.WARNING,
                        "Could not load EMF model from resource at " + filename + ". " + ie.getMessage());
            }
        }
        if (resource.getContents().size() == 0) {
            // If there is no existing file and root element, add EMF root element
            resource.getContents().add(createEmfRootElement());
        }

        // check if contents is valid for EDP2
        if (resource.getContents().size() == 1) {
            String extension = new EmfmodelExtensionSwitch().doSwitch(resource.getContents().get(0));
            if (extension == null) {
                String msg = "The root element in the file is not valid to EDP2 specifications. " + "Filename = "
                        + filename;
                LOGGER.log(Level.WARNING, msg);
                emfRootElement = null;
                setClosed();
                diagnostics.add(new BasicDiagnostic(filename, Diagnostic.ERROR, msg, null));
            } else {
                if (resourceFile.getName().endsWith(extension)) {
                    emfRootElement = resource.getContents().get(0);
                    setOpen();
                } else {
                    String msg = "The root element in the file is not valid to EDP2 specifications. "
                            + " Expected root element = " + extension + ". Filename = " + filename;
                    LOGGER.log(Level.WARNING, msg);
                    emfRootElement = null;
                    setClosed();
                    diagnostics.add(new BasicDiagnostic(filename, Diagnostic.ERROR, msg, null));
                }
            }
        } else {
            String msg = "Only one root element is allowed per file in EDP2 specifications. " + "Filename = "
                    + filename;
            LOGGER.log(Level.WARNING, msg);
            emfRootElement = null;
            setClosed();
            diagnostics.add(new BasicDiagnostic(filename, Diagnostic.ERROR, msg, null));
        }
    }

    /**
     * Code for subclasses to create the specific type of EObjects. Needed for root element
     * initialization.
     * 
     * @return Instance.
     */
    abstract protected EObject createEmfRootElement();

    /**
     * Returns the EMF root element (of the file).
     * 
     * @return the EMF element.
     */
    protected EObject getEmfRootElement() {
        return emfRootElement;
    }

}
