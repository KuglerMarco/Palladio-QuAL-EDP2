/**
 * 
 */
package org.palladiosimulator.edp2.dao;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.palladiosimulator.edp2.dao.exception.DataNotAccessibleException;

/**
 * Generic interface for accessing any DAO in EDP2. See sub-interfaces for more specific information
 * on meta ({@link MetaDao}) or measurement data ({@link MeasurementsDao}).
 * 
 * @author groenda
 */
public interface Edp2Dao {
    /**
     * Opens the connection to the data store and makes it's data accessible.
     */
    public void open(DiagnosticChain diagnosticChain);
    
    /**
     * @throws DataNotAccessibleException
     * 
     * @deprecated use {@link #open(DiagnosticChain)} instead.
     */
    @Deprecated
    default void open() throws DataNotAccessibleException {
        var diag = new BasicDiagnostic(); 
        open(diag);
        if (diag.getSeverity() > Diagnostic.WARNING) {
            throw new DataNotAccessibleException(diag.toString(), null);
        }
    }

    /**
     * Checks if the data store which is accessed by this DAO is available and can be opened using
     * {@link open}.
     * 
     * @return <code>true</code> if the data store is available, false otherwise.
     */
    public boolean canOpen(DiagnosticChain diagnosticChain);
    
    /**
     * Checks if the data store which is accessed by this DAO is available and can be opened using
     * {@link open}.
     * 
     * Delegates to {@link #canOpen(DiagnosticChain)}.
     * 
     * @return <code>true</code> if the data store is available, false otherwise.
     */
    default boolean canOpen() {
        return canOpen(new BasicDiagnostic());
    }

    /**
     * Closes the connection to the data store. If necessary, data is persisted before closing.
     * 
     * @throws DataNotAccessibleException
     *             Errors when accessing data.
     */
    public void close() throws DataNotAccessibleException;

    /**
     * Checks if the connection of this DAO to the data store can be closed.
     * 
     * @return <code>true</code> if it can be closed, false otherwise.
     */
    public boolean canClose();

    /**
     * Checks if the data managed by this DAO can be permanently deleted.
     * 
     * @return <code>true</code> if {@link delete()} can be called, false otherwise.
     */
    public boolean canDelete();

    /**
     * Deletes all of the stored data.
     * 
     * @throws DataNotAccessibleException
     *             Errors when accessing data.
     */
    public void delete() throws DataNotAccessibleException;

    /**
     * Returns if data is accessible or not.
     * 
     * @return <code>true</code> if the connection is open and data is accessible.
     */
    public boolean isOpen();

    /**
     * Returns if the referenced data has been deleted.
     * 
     * @return <code>true</code> if the data has been delete.
     */
    public boolean isDeleted();

    /**
     * Write all non-persisted data to disk.
     */
    public void flush();

}
