package view;

import model.Song;
import model.SongLibrary;
import model.SongTransferHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;

/**
 * SongListView class is to show either the library or a playlist.
 * This contains a table with column headers for songs.
 * Table related listeners are attached.
 */
public class SongListView extends JPanel {
    //components for table
    private JScrollPane tableScrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private String[] columnHeader;
    private int tableRowHeight = 24;

    /**
     * Constructs a panel to show a list of songs
     * with an empty table view.
     */
    public SongListView(){
        // Table setup
        columnHeader = new String[]{"Path", "Title", "Artist", "Album", "Year", "Comment", "Genre"};
        table = new JTable(){
            @Override   //block table contents editing
            public boolean isCellEditable(int row, int column) { return false; }
        };

        //initialize table for dynamic row addition
        initializeTable();

        //table behavior setups
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setDragEnabled(true);
        table.getTableHeader().setReorderingAllowed(false);

        //ui setups
        table.setFillsViewportHeight(true);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFont(MusicPlayerGUI.FONT);
        table.getTableHeader().setFont(MusicPlayerGUI.FONT);
        table.setRowHeight(tableRowHeight);



        //table.setShowGrid(false);

//        //change the look of the header
//        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setBorder(BorderFactory.createEmptyBorder());
//        table.getTableHeader().setDefaultRenderer(renderer);

        //put table in place
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.add(tableScrollPane, BorderLayout.CENTER);


        table.setDropTarget(new DropTarget() {
            private static final long serialVersionUID = -6418118605479053389L;

            @SuppressWarnings("unchecked")
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List droppedFiles = (List) evt
                            .getTransferable().getTransferData(
                                    DataFlavor.javaFileListFlavor);
                    if(droppedFiles.getItemCount() > 1){
                        JOptionPane.showMessageDialog(table, "Sorry...can't handle more than one files together.");
                    }
                    else{
                        Song droppedFile = droppedFiles.getItem(0);
                        if(droppedFile.getPath() != null){
                            char[] contentBytes = readFile(droppedFile);
                            if(contentBytes == null){
                                JOptionPane.showMessageDialog(content, "Sorry...file size is too long.");
                            }
                            else if(contentBytes.length == 0){
                                JOptionPane.showMessageDialog(content, "Sorry...file is empty.");
                            }
                            else{
                                content.setText(new String(contentBytes));
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(content, "Sorry...not a text file.");
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(splitPane);
        pack();
    }

    private char[] readFile(File inputFile){
        BufferedReader inputReader = null;
        char[] content = null;
        long availableHeap = Runtime.getRuntime().freeMemory();
        long fileSize = inputFile.length();
        try {
            if(fileSize <= availableHeap){
                content = new char[(int)inputFile.length()];
                inputReader = new BufferedReader(new FileReader(inputFile));

                inputReader.read(content);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }

    private File[] getAllDrives(){

        return File.listRoots();
    }

    private ArrayList getAllDirectories(File file){

        ArrayList directories = new ArrayList();
        File[] allSub = file.listFiles();
        if(allSub != null){
            for(File sub: allSub){
                if(sub.isDirectory()){
                    directories.add(sub);
                }
            }
        }
        return directories;
    }

    private ArrayList getAllTXTs(File directory){
        ArrayList pdfs = new ArrayList();
        File[] allSub = directory.listFiles();
        if(allSub != null){
            for(File sub: allSub){
                if(sub.isFile() && sub.getName().endsWith(".txt")){
                    pdfs.add(sub);
                }
            }
        }
        return pdfs;
    }

    private DefaultMutableTreeNode getTreeStructure(){
        File[] roots = getAllDrives();
        DefaultMutableTreeNode allDrives = new DefaultMutableTreeNode("All Drives");
        for(File root: roots){
            DefaultMutableTreeNode drive = new DefaultMutableTreeNode(root);
            ArrayList folderNodes = getAllDirectories(root);

            for(File folderNode : folderNodes){
                DefaultMutableTreeNode childDrive =new DefaultMutableTreeNode(folderNode.getName());
                ArrayList txts = getAllTXTs(folderNode);
                for(File txt : txts){
                    childDrive.add(new DefaultMutableTreeNode(txt));
                }
                drive.add(childDrive);
            }
            allDrives.add(drive);
        }
        return allDrives;
    }

}



        });


    }

    /**
     * Constructs a panel to show a list of songs
     * with a table view from a list of songs.
     * @param songList list of Songs to be reflected in table view,
     *                 which can be from the library or a playlist.
     */
    public SongListView (ArrayList<Song> songList){
        this();
        updateTableView(songList);
    }

    /**
     * Initializes the table model to dynamically add rows later.
     */
    private void initializeTable() {
        //for dynamic row addition
        tableModel = new DefaultTableModel(columnHeader,0);
        table.setModel(tableModel);
    }

    /**
     * Updates the table view.
     * @param songList list of Songs to be reflected in table view,
     *                 which can be from the library or a playlist.
     */
    public void updateTableView(ArrayList<Song> songList) {
        initializeTable();
        for (Song song : songList) {
            tableModel.addRow(song.toArray());
        }
        tableModel.fireTableDataChanged();
    }

    /**
     * Returns the table of this SongListView
     * @return JTable containing songs
     */
    public JTable getSongTable(){
        return table;
    }

    /**
     * Set color theme on the table view panel.
     */
    public void setColorTheme(ColorTheme colorTheme){
        Color[] bgColor = colorTheme.bgColor;
        Color[] fgColor = colorTheme.fgColor;
        Color[] pointColor = colorTheme.pointColor;

        //table
        table.setBackground(bgColor[0]);
        table.setForeground(fgColor[1]);
        table.getTableHeader().setBackground(bgColor[1]);
        table.getTableHeader().setForeground(fgColor[2]);

        //horizontal line of the table
        table.setGridColor(bgColor[2]);

        //table row selection
        table.setSelectionBackground(pointColor[0]);
        table.setSelectionForeground(pointColor[1]);
    }

    public void setTransferHandlerLibrary(SongLibrary library) {
        table.setTransferHandler(new SongTransferHandler(library));
    }
}
