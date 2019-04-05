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
        libTreeModel = new DefaultTreeModel(libRootNode);
        libraryTree = new JTree(libTreeModel);
        libraryTreePanel = new JPanel();
        libraryTreePanel.add(libraryTree);

        //create a "Playlist" tree with playlist nodes
        pllRootNode = new DefaultMutableTreeNode("Playlist");
        pllTreeModel = new DefaultTreeModel(pllRootNode);
        playlistTree = new JTree(pllTreeModel);
        playlistTreePanel = new JScrollPane(playlistTree);

        //call the list of playlist names from the db
        //create nodes under "Playlist"
        DefaultMutableTreeNode pll1Node = new DefaultMutableTreeNode("Playlist 1");
        DefaultMutableTreeNode pll2Node = new DefaultMutableTreeNode("Playlist 2");
        DefaultMutableTreeNode pll3Node = new DefaultMutableTreeNode("Playlist 3");
        pllRootNode.add(pll1Node);
        pllRootNode.add(pll2Node);
        pllRootNode.add(pll3Node);
        //reload tree model to display updated tree nodes
        pllTreeModel.reload();


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

    public void addPlaylist(String name) {

    }

    public void removePlaylist(String name) {
        //..
    }

    public void expandPlaylistTree() {

    }

    public void collapsePlaylistTree() {

    }

    public void addMouseListenerForPlaylist(MouseAdapter adapter) {
        //..
    }

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
        renderer.setTextSelectionColor(colorTheme.pointColor[1]);
        renderer.setBackground(null);
    }

}