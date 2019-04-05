package view;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * SidePanelView class is to show "Library" and "Playlist"
 * and a tree of the existing playlists.
 */
public class SidePanelView extends JPanel {
    private Dimension sidePanelSize = new Dimension(120,500);

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
        libraryTreePanel.setLayout(new BorderLayout());
        libraryTreePanel.add(libraryTree, BorderLayout.CENTER);

        //create a "Playlist" tree with playlist nodes
        pllRootNode = new DefaultMutableTreeNode("Playlist");
        pllTreeModel = new DefaultTreeModel(pllRootNode);
        playlistTree = new JTree(pllTreeModel);
        playlistTree.setName("playlistTree");
        //place playlist tree in panel
        playlistTreePanel = new JScrollPane(playlistTree);

        //create nodes under "Playlist"
        String[] testPlaylist = {"Favorite","Rock","Party","Jazz"};
        updatePlaylistTree(testPlaylist);


        //tree cell renderer setup for trees
        renderer = new DefaultTreeCellRenderer();
        //remove default icons
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        //apply cell renderer to trees
        libraryTree.setCellRenderer(renderer);
        playlistTree.setCellRenderer(renderer);


        //put tree panels in place
        this.setLayout(new BorderLayout());
        this.add(libraryTreePanel, BorderLayout.NORTH);
        this.add(playlistTreePanel, BorderLayout.CENTER);

        this.setPreferredSize(sidePanelSize);
    }

    public JTree getLibraryTree() { return libraryTree; }
    public JTree getPlaylistTree() { return playlistTree; }

    /**
     * Updates the tree view under "Playlist"
     * @param playlist the array of playlist names
     */
    public void updatePlaylistTree(String[] playlist){
        //update the tree view
        for (int i=0; i<playlist.length; i++) {
            pllRootNode.add(new DefaultMutableTreeNode(playlist[i]));
        }
        pllTreeModel.reload();
    }

    /**
     * Adds a mouse listener to trees of library/playlist.
     * @param adapter the adapter to be added to the trees.
     */
    public void addMouseListener(MouseAdapter adapter) {
        libraryTree.addMouseListener(adapter);
        playlistTree.addMouseListener(adapter);
    }

    /**
     * Set color theme on the side panel with trees.
     * @param colorTheme the theme to apply to this side panel view.
     */
    public void setColorTheme(final ColorTheme colorTheme) {
        //this panel color setup
        this.setBackground(colorTheme.bgColor[1]);
        this.setBorder(BorderFactory.createLineBorder(colorTheme.bgColor[0]));
        //tree panel color setup
        libraryTreePanel.setOpaque(false);
        libraryTree.setBackground(colorTheme.bgColor[1]);
        libraryTreePanel.setBorder(BorderFactory.createEmptyBorder());
        playlistTreePanel.setOpaque(false);
        playlistTree.setBackground(colorTheme.bgColor[1]);
        playlistTreePanel.setBorder(BorderFactory.createEmptyBorder());

        //tree cell color setup
        renderer.setBackgroundNonSelectionColor(null);
        renderer.setTextNonSelectionColor(colorTheme.fgColor[0]);
        renderer.setBackgroundSelectionColor(colorTheme.pointColor[0]);
        renderer.setBorderSelectionColor(colorTheme.pointColor[0]);
        renderer.setTextSelectionColor(colorTheme.pointColor[1]);
        renderer.setBackground(null);
    }

}