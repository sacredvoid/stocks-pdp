package view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXDatePicker;

public class PortfolioListPanel extends JPanel {
  private JComboBox<String> portfolioOptions;
  public String selected;
  public JButton selectedButton;
  private JXDatePicker datePicker;
  public JScrollPane scrollStatusPane;

  private JPanel selectionPanel;
  public JPanel statusPanel;
  public Date selectedDate;
  public String selectedDateString;
  private JLabel showSelectedDate;
  public JTextArea appStatusUpdates;
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  public PortfolioListPanel() {
    super();
    setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    selectionPanel = new JPanel();
    add(selectionPanel);
    statusPanel = new JPanel();
    statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.PAGE_AXIS));
    appStatusUpdates = new JTextArea(7,0);
    appStatusUpdates.setEditable(false);
    appStatusUpdates.setText("Welcome! You can start by viewing existing Portfolios or creating a new one.\nCommission set to $1 per transaction.");
    Border border = BorderFactory.createTitledBorder("Application Status Updates");
    appStatusUpdates.setBorder(border);
    scrollStatusPane = new JScrollPane(appStatusUpdates);
    scrollStatusPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    statusPanel.add(scrollStatusPane);
    add(Box.createRigidArea(new Dimension(0,300)));
    add(statusPanel);
  }

  public void setupPortfolioList(String[] portfolios) {
    if(portfolios.length == 0) {
      JLabel emptyList = new JLabel("No Existing Portfolios found, you can create one");
      add(emptyList);
    }
    else {
      selectionPanel.add(new JLabel("Select One of The Portfolios"),BorderLayout.LINE_START);
      showSelectedDate = new JLabel();
      portfolioOptions = new JComboBox<>(portfolios);
      selected = portfolioOptions.getItemAt(0);
      portfolioOptions.addActionListener(e -> selected = (String) portfolioOptions.getSelectedItem());
      selectedButton = new JButton("Show Portfolio Info");
      selectionPanel.add(portfolioOptions, BorderLayout.CENTER);
      setDatePicker();
      selectionPanel.add(selectedButton,BorderLayout.LINE_END);
      selectionPanel.add(showSelectedDate);
    }
  }

  public void updatePortfolioList(String[] portfolios) {
    portfolioOptions.setModel(new DefaultComboBoxModel<>(portfolios));
  }

  private void setDatePicker() {
    datePicker = new JXDatePicker();
    datePicker.getMonthView().setZoomable(true);
    datePicker.setDate(new Date());
    datePicker.setFormats(simpleDateFormat);
    datePicker.getMonthView().setZoomable(true);
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
    selectedDateString = simpleDateFormat.format(selectedDate);
    showSelectedDate.setText("Selected Date is: "+selectedDateString);
  }

  private void setCalendarLimits(Calendar calendar) {
    calendar.setTime(new Date());
    datePicker.getMonthView().setUpperBound(calendar.getTime());
  }

}
