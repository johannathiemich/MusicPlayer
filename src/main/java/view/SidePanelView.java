package view;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * SidePanelView class is to show "Library" and "Playlist"
 * and a tree of the existing playlists.
 */
public class SidePanelView extends JPanel {
    //components for the side panel
    private JScrollPane scrollPane;
    private JTree libraryTree;
    private JTree playlistTree;
    private DefaultTreeModel libTreeModel;
    private DefaultTreeModel pllTreeModel;
    private DefaultMutableTreeNode libRootNode;
    private DefaultMutableTreeNode pllRootNode;

    private Dimension sidePanelSize = new Dimension(120,500);

    /**
     * Constructs a panel to show "Library" and "Playlist" with a tree.
     */
    public SidePanelView() {
        //creates a Library tree with one node
        libRootNode = new DefaultMutableTreeNode("Library");
        libTreeModel = new DefaultTreeModel(libRootNode);
        libraryTree = new JTree(libTreeModel);

        //create a Playlist tree with playlists
        pllRootNode = new DefaultMutableTreeNode("Playlist");
        DefaultMutableTreeNode pll1Node = new DefaultMutableTreeNode("Playlist 1");
        DefaultMutableTreeNode pll2Node = new DefaultMutableTreeNode("Playlist 2");
        DefaultMutableTreeNode pll3Node = new DefaultMutableTreeNode("Playlist 3");
        pllRootNode.add(pll1Node);
        pllRootNode.add(pll2Node);
        pllRootNode.add(pll3Node);
        pllTreeModel = new DefaultTreeModel(pllRootNode);
        playlistTree = new JTree(pllTreeModel);

        //call the list of playlist names from the db
        //create nodes under "Playlist"

        scrollPane = new JScrollPane(playlistTree);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

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

    public void setColorTheme(ColorTheme colorTheme) {
        //background color setup
        this.setBackground(colorTheme.bgColor[1]);
        scrollPane.setOpaque(false);
        playlistTree.setBackground(colorTheme.bgColor[1]);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        //playlistTree.setBorder(BorderFactory.createEmptyBorder());
        this.setBorder(BorderFactory.createLineBorder(colorTheme.bgColor[0]));
        //text color setup
        libraryTree.setForeground(colorTheme.fgColor[0]);
        playlistTree.setForeground(colorTheme.fgColor[0]);

    }
}