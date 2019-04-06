package view;


import model.PlaylistLibrary;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * SidePanelView class is to show "Library" and "Playlist"
 * and a tree of the existing playlists.
 */
public class SidePanelView extends JPanel {
    private Dimension sidePanelSize = new Dimension(120,500);
    private int treeRowHeight = 23;

    //components for library tree
    private JPanel libraryTreePanel;
    private JTree libraryTree;
    private DefaultTreeModel libTreeModel;
    private DefaultMutableTreeNode libRootNode;
    //components for playlist tree
    private JScrollPane playlistTreePanel;
    private JTree playlistTree;
    private DefaultTreeModel pllTreeModel;
    private DefaultMutableTreeNode pllRootNode;

    private DefaultTreeCellRenderer renderer;

    //popup menu items for playlist
    private JPopupMenu playlistPopupMenu;
    private JMenuItem openNewWindowMenuItem;
    private JMenuItem deletePlaylistMenuItem;
    private PlaylistLibrary playlistLibrary;

    /**
     * Constructs a panel to show "Library" and "Playlist" with a tree.
     */
    public SidePanelView() {
        //creates a "Library" tree with only one node (the root node)
        libRootNode = new DefaultMutableTreeNode("Library");
        libRootNode.setAllowsChildren(false);
        libTreeModel = new DefaultTreeModel(libRootNode);
        libraryTree = new JTree(libTreeModel);
        libraryTree.setName("libraryTree");
        //place library tree in panel
        libraryTreePanel = new JPanel();
        libraryTreePanel.setPreferredSize(new Dimension(this.getWidth(),treeRowHeight+4));
        libraryTreePanel.setLayout(new BorderLayout());
        libraryTreePanel.add(libraryTree, BorderLayout.CENTER);

        //create a "Playlist" tree with playlist nodes
        pllRootNode = new DefaultMutableTreeNode("Playlist");
        pllTreeModel = new DefaultTreeModel(pllRootNode);
        playlistTree = new JTree(pllTreeModel);
        playlistTree.setName("playlistTree");
        //place playlist tree in panel
        playlistTreePanel = new JScrollPane(playlistTree);
        playlistTreePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //create nodes under "Playlist"
        playlistLibrary = new PlaylistLibrary();
        String[] testPlaylist = {"Favorite","Rock","Party","Jazz"};
        updatePlaylistTree(playlistLibrary.getAllPlaylistNamesArray());

        //tree UI setups
        //cell renderer for trees
        renderer = new DefaultTreeCellRenderer();
        //change font
        renderer.setFont(MusicPlayerGUI.FONT);
        //remove default icons
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        //set size of the tree cell
        renderer.setPreferredSize(new Dimension(sidePanelSize.width, treeRowHeight));
        //apply cell renderer to trees
        libraryTree.setCellRenderer(renderer);
        playlistTree.setCellRenderer(renderer);
        //tree row height setup
        libraryTree.setRowHeight(treeRowHeight);
        playlistTree.setRowHeight(treeRowHeight);

        //put tree panels in place
        this.setLayout(new BorderLayout());
        this.add(libraryTreePanel, BorderLayout.NORTH);
        this.add(playlistTreePanel, BorderLayout.CENTER);

        this.setPreferredSize(sidePanelSize);

        libraryTree.setFocusable(false);
        playlistTree.setFocusable(false);

        //playlist popup menu
        playlistPopupMenu = new JPopupMenu();
        openNewWindowMenuItem = new JMenuItem("Open in New Window");
        deletePlaylistMenuItem = new JMenuItem("Delete Playlist");
        openNewWindowMenuItem.setName("playlist-openNewWindow");
        deletePlaylistMenuItem.setName("playlist-delete");
        playlistPopupMenu.add(openNewWindowMenuItem);
        playlistPopupMenu.add(deletePlaylistMenuItem);
    }

    /**
     * Gets the library tree with only one node
     * @return the library tree
     */
    public JTree getLibraryTree() { return libraryTree; }

    /**
     * Gets the playlist tree
     * @return the playlist tree
     */
    public JTree getPlaylistTree() { return playlistTree; }

    /**
     * Updates the tree view under "Playlist"
     * @param playlistName the array of playlist names
     */
    public void updatePlaylistTree(String[] playlistName){
        //update the tree view
        for (int i=0; i<playlistName.length; i++) {
            pllRootNode.add(new DefaultMutableTreeNode(playlistName[i]));
        }
        pllTreeModel.reload();
    }

    /**
     * Gets a popup menu for a playlist with
     * "Open in New Window" and "Delete Playlist"
     * @return playlist popup menu
     */
    public JPopupMenu getPlaylistPopupMenu(){
        return playlistPopupMenu;
    }

    /**
     * Attaches a mouse listener to trees of library/playlist.
     * @param adapter the MouseAdapter with mouse actions on trees.
     */
    public void addMouseListener(MouseAdapter adapter) {
        libraryTree.addMouseListener(adapter);
        playlistTree.addMouseListener(adapter);
    }

    /**
     * Attaches a listener to all popup menu items for playlist
     * @param listener ActionListener with playlist popup menu actions
     */
    public void addMenuListener(ActionListener listener) {
        //popup menu items for playlist
        openNewWindowMenuItem.addActionListener(listener);
        deletePlaylistMenuItem.addActionListener(listener);
    }

    /**
     * Sets color theme on the side panel with trees.
     * @param colorTheme the theme to apply to this side panel view.
     */
    public void setColorTheme(final ColorTheme colorTheme) {
        //this panel color setup
        this.setBackground(colorTheme.bgColor[2]);
        this.setBorder(BorderFactory.createLineBorder(colorTheme.bgColor[0]));

        //tree panel background color setup
        libraryTreePanel.setOpaque(false);
        playlistTreePanel.setOpaque(false);
        libraryTree.setBackground(colorTheme.bgColor[2]);
        playlistTree.setBackground(colorTheme.bgColor[2]);
        libraryTreePanel.setBorder(BorderFactory.createEmptyBorder());
        playlistTreePanel.setBorder(BorderFactory.createEmptyBorder());

        //tree cell color setup
        renderer.setBackgroundNonSelectionColor(null);
        renderer.setTextNonSelectionColor(colorTheme.fgColor[1]);
        renderer.setBackgroundSelectionColor(colorTheme.pointColor[0]);
        renderer.setBorderSelectionColor(colorTheme.pointColor[0]);
        renderer.setTextSelectionColor(colorTheme.pointColor[1]);
        renderer.setBackground(null);
    }

}