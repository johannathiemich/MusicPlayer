/**
 * This class was largely copied from here:
 * https://github.com/aterai/java-swing-tips/blob/master/DragRowsAnotherTable/src/java/example/MainPanel.java
 */

package model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import java.util.*;
import java.util.List;

//TODO: copy songs, not cut them from original library
//TODO: add songs to database (playlist table)
public class TableRowTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor;
    private int[] indices;
    private int addIndex = -1; //Location where items were added
    private int addCount; //Number of items added.
    private JComponent source;

    public TableRowTransferHandler() {
        super();
        localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
    }
    @Override protected Transferable createTransferable(JComponent c) {
        source = c;
        JTable table = (JTable) c;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        indices = table.getSelectedRows();
        List<Object> list = new ArrayList<>();
        indices = table.getSelectedRows();
        for (int i: indices) {
            list.add(model.getDataVector().get(i));
        }
        Object[] transferData = list.toArray();
        for (int i : indices) {
            list.add(model.getDataVector().elementAt(i));
        }
        final Object[] transferedObjects = list.toArray();
        return new Transferable() {
            @Override public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] {localObjectFlavor};
            }
            @Override public boolean isDataFlavorSupported(DataFlavor flavor) {
                return Objects.equals(localObjectFlavor, flavor);
            }
            @Override public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return transferData;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        };
    }
    @Override public boolean canImport(TransferSupport info) {
        JTable table = (JTable) info.getComponent();
        boolean isDroppable = info.isDrop()
                && info.isDataFlavorSupported(localObjectFlavor);
        //XXX bug?
        table.setCursor(isDroppable ? DragSource.DefaultCopyDrop
                : DragSource.DefaultCopyNoDrop);
        return isDroppable;
    }
    @Override public int getSourceActions(JComponent c) {
        return TransferHandler.COPY; //TransferHandler.COPY_OR_MOVE;
    }
    @Override public boolean importData(TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }
        TransferHandler.DropLocation tdl = info.getDropLocation();
        if (!(tdl instanceof JTable.DropLocation)) {
            return false;
        }
        JTable.DropLocation dl = (JTable.DropLocation) tdl;
        JTable target = (JTable) info.getComponent();
        DefaultTableModel model = (DefaultTableModel) target.getModel();
        int index = dl.getRow();
        //boolean insert = dl.isInsert();
        int max = model.getRowCount();
        if (index < 0 || index > max) {
            index = max;
        }
        addIndex = index;
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        ///target.clearSelection();
        try {
            Object[] values =
                    (Object[]) info.getTransferable().getTransferData(localObjectFlavor);
            if (Objects.equals(source, target)) {
                addCount = values.length;
            }
            for (int i = 0; i < values.length; i++) {
                int idx = index++;
                model.insertRow(idx, (Vector) values[i]);
                target.getSelectionModel().addSelectionInterval(idx, idx);
                //TODO add rows to database here?
            }
            target.clearSelection();
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    @Override protected void exportDone(
            JComponent c, Transferable data, int action) {
        cleanup(c, action == COPY);
    }
    private void cleanup(JComponent c, boolean remove) {
        if (remove && indices != null) {
            c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            DefaultTableModel model = (DefaultTableModel) ((JTable) c).getModel();
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] >= addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
           // for (int i = indices.length - 1; i >= 0; i--) {
                //model.removeRow(indices[i]);
           // }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;
    }
}