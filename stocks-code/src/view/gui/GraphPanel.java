package view.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXDatePicker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class GraphPanel extends JPanel {

  public JButton showGraph;
  public JXDatePicker startDate;
  public JXDatePicker endDate;
  public String startDateString;
  public String endDateString;
  public ChartPanel chartPanel = null;
  public JPanel buttonPanel;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  public GraphPanel() {
    setPreferredSize(getPreferredSize());
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    buttonPanel = new JPanel();
    startDate = new JXDatePicker(new Date());
    endDate = new JXDatePicker(new Date());
    startDate.setFormats(simpleDateFormat);
    endDate.setFormats(simpleDateFormat);
    startDateString = simpleDateFormat.format(startDate.getDate());
    startDate.addActionListener(e -> startDateString = simpleDateFormat.format(startDate.getDate()));
    endDateString = simpleDateFormat.format(endDate.getDate());
    endDate.addActionListener(e -> endDateString = simpleDateFormat.format(endDate.getDate()));
    showGraph = new JButton("Show Graph");
    add(buttonPanel);
    buttonPanel.add(startDate);
    buttonPanel.add(endDate);
    buttonPanel.add(showGraph);
  }

  public void addGraph(JFreeChart chart) {
    if(chart == null) {
     return;
    }
    if(chartPanel != null) {
      chartPanel.setChart(chart);
    }
    else {
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
