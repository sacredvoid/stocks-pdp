package view.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * The GraphPanel which is rendered on the bottom right of the JFrame. This panel renders the
 * performance graph and contains datepickers and buttons for respective inputs.
 */
public class GraphPanel extends JPanel {

  /**
   * The Show graph button which renders the graph.
   */
  public JButton showGraph;
  /**
   * The Start date JXDatePicker component.
   */
  public JXDatePicker startDate;
  /**
   * The End date JXDatePicker component.
   */
  public JXDatePicker endDate;
  /**
   * The Start date string converted from Date() object.
   */
  public String startDateString;
  /**
   * The End date string converted from Date() object.
   */
  public String endDateString;
  /**
   * The Chart panel used to render the JFreechart.
   */
  public ChartPanel chartPanel = null;
  /**
   * The Button panel which stores the buttons.
   */
  public JPanel buttonPanel;
  /**
   * The Simple date format used to format JXDatePicker dates.
   */
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Instantiates a new Graph panel.
   */
  public GraphPanel() {
    setPreferredSize(getPreferredSize());
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    buttonPanel = new JPanel();
    startDate = new JXDatePicker(new Date());
    endDate = new JXDatePicker(new Date());
    startDate.setFormats(simpleDateFormat);
    startDate.getMonthView().setUpperBound(new Date());
    endDate.setFormats(simpleDateFormat);
    endDate.getMonthView().setUpperBound(new Date());
    startDateString = simpleDateFormat.format(startDate.getDate());
    startDate.addActionListener(
        e -> startDateString = simpleDateFormat.format(startDate.getDate()));
    endDateString = simpleDateFormat.format(endDate.getDate());
    endDate.addActionListener(e -> endDateString = simpleDateFormat.format(endDate.getDate()));
    showGraph = new JButton("Show Graph");
    add(buttonPanel);
    buttonPanel.add(new JLabel("View Performance between (start date)"));
    buttonPanel.add(startDate);
    buttonPanel.add(new JLabel("and (end date)"));
    buttonPanel.add(endDate);
    buttonPanel.add(showGraph);
  }

  /**
   * Add a JFreeChart to the given chart panel.
   *
   * @param chart the chart
   */
  public void addGraph(JFreeChart chart) {
    if (chart == null) {
      return;
    }
    if (chartPanel != null) {
      chartPanel.setChart(chart);
    } else {
      chartPanel = new ChartPanel(chart);
      chartPanel.setFillZoomRectangle(true);
      chartPanel.setMouseWheelEnabled(true);
      chartPanel.setMaximumSize(getParent().getSize());
      add(chartPanel);
      revalidate();
      repaint();
    }
  }
}