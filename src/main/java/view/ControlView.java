package view;

import model.Song;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ControlView class is a panel with player controls.
 * This contains play/stop/prev/next buttons and a volume slider.
 */
public class ControlView extends JPanel {
    private Dimension buttonSize = new Dimension(60,48);
    private Font buttonFont = new Font("Helvetica",Font.PLAIN,20);

    //components for basic control view
    private JPanel buttonPanel;
    private JPanel stopPanel;
    private JPanel sliderPanel;
    private JButton playBtn;
    private JButton nextBtn;
    private JButton prevBtn;
    private JButton stopBtn;
    private JSlider volumeSlider;

    //components for songInfoPanel
    private JPanel songInfoPanel;
    private JLabel songTitleLbl;
    private JLabel songDetailLbl;
    private JLabel songTimePlayingLbl;
    private JLabel songTimeRemainingLbl;
    private JProgressBar songProgressBar;

    public ControlView(){
        buttonPanel = new JPanel();
        sliderPanel = new JPanel();
        stopPanel = new JPanel();

        //set layout of the panels
        this.setLayout(new BorderLayout(0,0));
        stopPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        sliderPanel.setLayout(new BorderLayout());

        //Buttons setup
        stopBtn = new JButton("◼");
        prevBtn = new JButton("⦉⦉");
        playBtn = new JButton("▶");
        nextBtn = new JButton("⦊⦊");
        stopBtn.setPreferredSize(buttonSize);
        prevBtn.setPreferredSize(buttonSize);
        playBtn.setPreferredSize(buttonSize);
        nextBtn.setPreferredSize(buttonSize);
        stopBtn.setFont(buttonFont);
        prevBtn.setFont(buttonFont);
        playBtn.setFont(buttonFont);
        nextBtn.setFont(buttonFont);
        stopBtn.setFocusable(false);
        prevBtn.setFocusable(false);
        playBtn.setFocusable(false);
        nextBtn.setFocusable(false);

        //setting name(key) of button components
        stopBtn.setName("stop");
        playBtn.setName("play");
        prevBtn.setName("prev");
        nextBtn.setName("next");

        //Slider setup
        volumeSlider = new JSlider();
        volumeSlider.setFocusable(false);

        //Add components in place
        stopPanel.add(stopBtn);
        buttonPanel.add(prevBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(nextBtn);
        sliderPanel.add(volumeSlider);
        this.add(stopPanel, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(sliderPanel, BorderLayout.EAST);

        createSongInfoPanel();
        showSongInfoPanel(false);
    }

    /**
     * getters for buttons and the volume slider
     */
    public JButton getPlayBtn(){ return playBtn; }
    public JButton getStopBtn(){ return stopBtn; }
    public JButton getNextBtn(){ return nextBtn; }
    public JButton getPrevBtn(){ return prevBtn; }
    public JSlider getVolumeSlider(){ return volumeSlider; }

    /**
     * Create all components for song info panel
     * which contains song title, artist, progressbar, duration.
     */
    private void createSongInfoPanel() {
        songInfoPanel = new JPanel();
        songInfoPanel.setLayout(new BorderLayout(0,0));

        //song info
        songTitleLbl = new JLabel("Title");
        songDetailLbl = new JLabel("Artist");
        songTimePlayingLbl = new JLabel("0:00");
        songTimeRemainingLbl = new JLabel("3:33");

        //ui setup
        songTitleLbl.setPreferredSize(new Dimension(this.getWidth(),24));
        songTitleLbl.setVerticalAlignment(SwingConstants.BOTTOM);
        songTitleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songDetailLbl.setHorizontalAlignment(SwingConstants.CENTER);
        songTimePlayingLbl.setHorizontalAlignment(SwingConstants.LEFT);
        songTimeRemainingLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        //progress bar
        songProgressBar = new JProgressBar(0,0,190);
        songProgressBar.setValue(60);

        //add all components to song info panel
        songInfoPanel.add(Box.createHorizontalStrut(10));   //invisible space
        songInfoPanel.add(songTitleLbl, BorderLayout.NORTH);
        songInfoPanel.add(songDetailLbl, BorderLayout.CENTER);
        songInfoPanel.add(songTimePlayingLbl, BorderLayout.WEST);
        songInfoPanel.add(songTimeRemainingLbl, BorderLayout.EAST);
        songInfoPanel.add(songProgressBar, BorderLayout.SOUTH);

        //add songInfoPanel to this view
        this.add(songInfoPanel, BorderLayout.NORTH);
    }

    /**
     * set visibility of song info panel
     * @param show true to show, false to hide
     */
    public void showSongInfoPanel(boolean show){
        songInfoPanel.setVisible(show);
        this.repaint();
    }

    /**
     * Update the view of song info panel
     * with title, artist, duration of currently playing song.
     * @param song currently playing song
     */
    public void updateCurrentPlayingView(Song song){
        if(songInfoPanel != null) {
            songTitleLbl.setText(song.getTitle());
            songDetailLbl.setText(song.getArtist());
            songTimePlayingLbl.setText("0:00");
            songTimeRemainingLbl.setText(song.getDurationMinSec());
            songProgressBar.setMinimum(0);
            songProgressBar.setMaximum(song.getDuration());
        }
    }

    /**
     * Set color theme on the control view panel.
     */
    public void setColorTheme(ColorTheme colorTheme) {
        Color[] bgColor = colorTheme.bgColor;
        Color[] fgColor = colorTheme.fgColor;
        Color[] pointColor = colorTheme.pointColor;

        this.setBackground(bgColor[1]);
        stopPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        sliderPanel.setOpaque(false);

        //button transparent background
        stopBtn.setOpaque(false);
        prevBtn.setOpaque(false);
        playBtn.setOpaque(false);
        nextBtn.setOpaque(false);
        stopBtn.setBorder(BorderFactory.createEmptyBorder());
        prevBtn.setBorder(BorderFactory.createEmptyBorder());
        playBtn.setBorder(BorderFactory.createEmptyBorder());
        nextBtn.setBorder(BorderFactory.createEmptyBorder());
        //button text color
        stopBtn.setForeground(fgColor[2]);
        prevBtn.setForeground(fgColor[2]);
        playBtn.setForeground(fgColor[2]);
        nextBtn.setForeground(fgColor[2]);

        //song info panel
        songInfoPanel.setBackground(bgColor[1]);
        songTitleLbl.setForeground(fgColor[1]);
        songDetailLbl.setForeground(fgColor[2]);
        songTimePlayingLbl.setForeground(fgColor[2]);
        songTimeRemainingLbl.setForeground(fgColor[2]);
        songTitleLbl.setFont(MusicPlayerGUI.FONT);
        //progressBar color setting.. not simple to change..
        //songProgressBar.setForeground(pointColor[0]);

    }

    /**
     * Only for development use.
     * Show borders of all components in the ui to check the layout.
     * @param show true to show, false not to show borders
     */
    private void showLayoutBorders(boolean show){
        LineBorder[] border = {new LineBorder(Color.red), new LineBorder(Color.green), new LineBorder(Color.blue)};
        this.setBorder(border[0]);
        stopPanel.setBorder(border[1]);
        stopBtn.setBorder(border[2]);
        buttonPanel.setBorder(border[1]);
        prevBtn.setBorder(border[2]);
        playBtn.setBorder(border[2]);
        nextBtn.setBorder(border[2]);
        sliderPanel.setBorder(border[1]);
        volumeSlider.setBorder(border[2]);
        if(songInfoPanel != null){
            songInfoPanel.setBorder(border[0]);
            songTitleLbl.setBorder(border[1]);
            songDetailLbl.setBorder(border[1]);
            songTimePlayingLbl.setBorder(border[1]);
            songTimeRemainingLbl.setBorder(border[1]);
            songProgressBar.setBorder(border[1]);
        }
    }
}
