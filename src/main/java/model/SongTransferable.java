package model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongTransferable implements Transferable {
    private List songs;
    final private DataFlavor[] flavors;

    public SongTransferable(List pSongs) {
        this.songs= Collections.unmodifiableList(
                    new ArrayList(pSongs));
            this.flavors = new DataFlavor[]
                    { DataFlavor.javaFileListFlavor };

        }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return this.flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.javaFileListFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor))
            return this.songs;
        else
            return null;
    }
}
