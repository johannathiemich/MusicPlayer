package controller;

import javax.swing.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;

public class DragGesture implements
        DragGestureListener, Transferable {

    private JTable mainTable;

    public DragGesture(JTable table) {
        this.mainTable = table;
        initUI();
    }

    private void initUI() {

        DragSource ds = new DragSource();

        ds.createDefaultDragGestureRecognizer(this.mainTable,
                DnDConstants.ACTION_COPY, this);

    }

    public void dragGestureRecognized(DragGestureEvent event) {

        Cursor cursor = Cursor.getDefaultCursor();

        if (event.getDragAction() == DnDConstants.ACTION_COPY) {

            cursor = DragSource.DefaultCopyDrop;
        }

        event.startDrag(cursor, this);
    }

    public Object getTransferData(DataFlavor flavor) {

        return null;
    }

    public DataFlavor[] getTransferDataFlavors() {

        return new DataFlavor[0];
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {

        return false;
    }

    private void createLayout(JComponent... arg) {

    }
}