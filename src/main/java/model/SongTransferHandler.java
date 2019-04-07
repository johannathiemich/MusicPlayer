package model;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class SongTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 4278824893466339483L;
    private SongLibrary library;

    public SongTransferHandler(SongLibrary library) {
        this.library = library;
    }

    public int getSourceActions(JComponent Source){

        return COPY_OR_MOVE;
    }

    protected Transferable createTransferable(JComponent source){

        JTable table = (JTable)source;
        int[] rows = table.getSelectedRows();
        ArrayList songList = new ArrayList();

        for (int i = 0; i < rows.length; i++) {
           songList.add(library.get(rows[i]));
        }
        return new SongTransferable(songList);
    }

}

