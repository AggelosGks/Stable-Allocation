package DisplayPanels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import Algorithms.Instance;
import DataStructures.BipartiteGraph;
import DataStructures.JobNode;
import DataStructures.MachineNode;

public class GraphMap extends JPanel {
	public GraphMap() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int start_x = 0;
	final static int start_y = 0;
	final static int range_y = 20;
	final static int range_x = 30;
	final static int hotel_radius = 25;
	final static int poi_radius = 15;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(new java.awt.Color(255, 255, 204));
		BipartiteGraph graph = Instance.getGraph();
		int n = graph.machines.size();
		int j = graph.jobs.size();

		Graphics2D tdg = (Graphics2D) g;
		for (MachineNode node : graph.machines) {
			Ellipse2D.Double shape = new Ellipse2D.Double(start_x + range_x * node.getX_cord(),
					start_y + range_y * node.getY_cord(), hotel_radius, hotel_radius);
			tdg.setColor(Color.GREEN);
			tdg.fill(shape);

		}
		for (JobNode node : graph.jobs) {
			Ellipse2D.Double shape = new Ellipse2D.Double(start_x + range_x * node.getX_cord(),
					start_y + range_y * node.getY_cord(), poi_radius, poi_radius);
			tdg.setColor(Color.GREEN);
			tdg.fill(shape);

		}

	}
}
