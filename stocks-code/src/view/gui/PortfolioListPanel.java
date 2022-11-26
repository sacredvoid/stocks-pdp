package view.gui;

import controller.GraphicalUIFeatures;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXDatePicker;

public class PortfolioListPanel extends JPanel {
  private JComboBox<String> portfolioOptions;
  public String selected;
  public JButton selectedButton;
  private JXDatePicker datePicker;

  private JPanel selectionPanel;
  private JPanel statusPanel;
  private Date selectedDate;
  private JLabel showSelectedDate;
  private JTextArea appStatusUpdates;
  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public PortfolioListPanel() {
    super();
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    selectionPanel = new JPanel();
    add(selectionPanel);
    statusPanel = new JPanel();
    appStatusUpdates = new JTextArea();
    appStatusUpdates.setEditable(false);
    appStatusUpdates.setText("Status Updates go here");
    Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
    appStatusUpdates.setBorder(border);
    statusPanel.add(appStatusUpdates);
    add(statusPanel);
  }

  public void setupPortfolioList(String[] portfolios, GraphicalUIFeatures features) {
    if(portfolios.length == 0) {
      JLabel emptyList = new JLabel("No Existing Portfolios found, you can create one");
      add(emptyList);
    }
    else {
      selectionPanel.add(new JLabel("Select One of The Portfolios"),BorderLayout.LINE_START);
      showSelectedDate = new JLabel();
      portfolioOptions = new JComboBox<>(portfolios);
      portfolioOptions.addActionListener(e -> selected = (String) portfolioOptions.getSelectedItem());
      selectedButton = new JButton("Show Portfolio Info");
      selectionPanel.add(portfolioOptions, BorderLayout.CENTER);
      setDatePicker();
      selectionPanel.add(selectedButton,BorderLayout.LINE_END);
      selectionPanel.add(showSelectedDate);
    }
  }

  private void setDatePicker() {
    datePicker = new JXDatePicker();
    datePicker.getMonthView().setZoomable(true);
    datePicker.setDate(new Date());
    datePicker.setFormats(simpleDateFormat);
    setCalendarLimits(datePicker.getMonthView().getCalendar());
    selectionPanel.add(datePicker,BorderLayout.SOUTH);
    updateSelectedDateStatus(datePicker.getDate());
    // sets selected date upon click
    datePicker.addActionListener(e -> {
      selectedDate = datePicker.getDate();
      updateSelectedDateStatus(selectedDate);
    });
  }

  private void updateSelectedDateStatus(Date selectedDate) {
    showSelectedDate.setText("Selected Date is: "+simpleDateFormat.format(selectedDate));
  }

  private void setCalendarLimits(Calendar calendar) {
    calendar.setTime(new Date());
    datePicker.getMonthView().setUpperBound(calendar.getTime());
  }

}
