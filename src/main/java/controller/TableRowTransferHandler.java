
/**
 * This class was largely copied from here:
 * https://github.com/aterai/java-swing-tips/blob/master/DragRowsAnotherTable/src/java/example/MainPanel.java
 */

package controller;

import model.PlaylistLibrary;
import model.Song;
import model.SongLibrary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.IOException;
import java.util.*;
import java.util.List;

//TODO: regular drag and drop stopped working conflicting drop targets
public class TableRowTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor;
    private int[] indices;
    private int addIndex = -1; //Location where items were added
    private int addCount; //Number of items added.
    private JComponent source;
    private SongLibrary songLibrary;
    private PlaylistLibrary playlistLibrary;

    public TableRowTransferHandler() {
        //TODO replace this constructor
        super();
        localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
    }

    public TableRowTransferHandler(SongLibrary songL, PlaylistLibrary playlistL) {
        super();
        this.songLibrary = songL;
        this.playlistLibrary = playlistL;
        localObjectFlavor = new DataFlavor(Object[].class, "Array of items");
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        source = c;
        JTable table = (JTable) c;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        indices = table.getSelectedRows();
        List<Object> list = new ArrayList<>();
        indices = table.getSelectedRows();
        for (int i : indices) {
            list.add(model.getDataVector().get(i));
        }
        Object[] transferData = list.toArray();
        for (int i : indices) {
            list.add(model.getDataVector().elementAt(i));
        }
        final Object[] transferedObjects = list.toArray();
        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{localObjectFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return Objects.equals(localObjectFlavor, flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return transferData;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        };
    }

    @Override
    public boolean canImport(TransferSupport info) {
        JTable table = (JTable) info.getComponent();
        boolean isDroppable = info.isDrop()
                && info.isDataFlavorSupported(localObjectFlavor);
        //XXX bug?
        //table.setCursor(isDroppable ? DragSource.DefaultCopyDrop
        //        : DragSource.DefaultCopyNoDrop);
        return isDroppable;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY; //TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferSupport info) {
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
                Vector convValues = (Vector) values[i];
                if (!tableContainsSong(model, (String) convValues.get(0))) {
                    String songPath = (String) convValues.get(0);
                    model.insertRow(idx, (Vector) values[i]);
                    JFrame frame = (JFrame) SwingUtilities.getRoot(target);
                    String frameName = frame.getTitle();
                    System.out.println("frame name: " + frameName);
                    //frame to add into is a playlist window
                    if (frameName.split(":")[0].equalsIgnoreCase("playlist")) {
                        System.out.println("Adding song to playlist");
                        String playlistName = frameName.split(":")[1].trim();
                        playlistLibrary.getPlaylistByName(playlistName).addSong(new Song(songPath));
                    } else {
                        songLibrary.addSong(new Song(songPath));
                        System.out.println("Adding song to library");
                    }
                    target.getSelectionModel().addSelectionInterval(idx, idx);

                    //TODO not quite working yet
                }
            }
            target.clearSelection();
            return true;
        } catch (UnsupportedFlavorException | IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void exportDone(
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

    private boolean tableContainsSong(TableModel model, String songName) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(songName)) {
                return true;
            }
        }
        return false;
    }

}