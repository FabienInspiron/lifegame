package jeux;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;


public class SimulatorView extends JPanel {
    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
	private FieldView fieldView;

	private Simulator hook;
    
	private Color red = Color.red;
	private JLabel lblPopulation;
	private JLabel lblStep;
	
    class simulationWorker extends SwingWorker<Void, Void>
	{
	    protected Void doInBackground() throws Exception
	    {
	    	hook.live();
			return null;
	    }
	}
    
	
    public SimulatorView(int height, int width, final Simulator hook) {
        this.hook = hook;
    	
        fieldView = new FieldView(height, width);
        setLayout(new BorderLayout());
        add(fieldView, BorderLayout.CENTER);
        
        JPanel panelTop = new JPanel();
        add(panelTop, BorderLayout.NORTH);
        
        lblPopulation = new JLabel("Population");
        lblStep = new JLabel("Step");
        
        GroupLayout gl_panel = new GroupLayout(panelTop);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
        			.addContainerGap(118, Short.MAX_VALUE)
        			.addComponent(lblPopulation)
        			.addGap(50)
        			.addComponent(lblStep)
        			.addGap(172))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(lblPopulation)
        				.addComponent(lblStep)))
        );
        panelTop.setLayout(gl_panel);
        
        JPanel panelDown = new JPanel();
        add(panelDown, BorderLayout.SOUTH);
        
        JButton btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new simulationWorker().execute();
				
			}
		});
        panelDown.add(btnRun);
        
        JButton btnStep = new JButton("1 Step");
        btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hook.liveOneStep();				
			}
		});
        
        panelDown.add(btnStep);
        
        validate();
        setVisible(true);
	}
    
    /**
     * Show the current status of the field.
     * 
     * @param step
     *            Which iteration step it is.
     * @param field
     *            The field whose status is to be displayed.
     * @param population 
     */
    public void showStatus(int step, Field field, int population) {
        if (!isVisible()) {
            setVisible(true);
        }

        lblStep.setText(STEP_PREFIX + step);
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                    if(field.getState(row, col)) fieldView.drawMark(col, row, red);
                    else fieldView.drawMark(col, row, Color.white);
            }
        }

        lblPopulation.setText(POPULATION_PREFIX + population);
        fieldView.repaint();
    }
    
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component may be
         * resized, compute the scaling factor again.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the internal
         * image to screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                Dimension currentSize = getSize();
                if (size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                } else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width,
                            currentSize.height, null);
                }
            }
        }
    }
}
