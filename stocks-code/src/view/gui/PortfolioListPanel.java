package view.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
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

/**
 * The top left panel which shows the user a drop down to select existing PFs and get the PF info on
 * the top right panel.
 */
public class PortfolioListPanel extends JPanel {

  private JComboBox<String> portfolioOptions;
  /**
   * The Selected PFID.
   */
  public String selectedPfID;
  /**
   * The button to "Show Portfolio".
   */
  public JButton selectedButton;
  private JXDatePicker datePicker;
  /**
   * The Scroll status pane which holds the status panel.
   */
  public JScrollPane scrollStatusPane;

  private JPanel selectionPanel;
  /**
   * The Status panel which shows user the messages.
   */
  public JPanel statusPanel;
  /**
   * The Selected date on which to show the portfolio value.
   */
  public Date selectedDate;
  /**
   * The Selected date string is the date selected coming from JXDatePicker.
   */
  public String selectedDateString;
  private JLabel showSelectedDate;
  /**
   * The App status updates to track and control what happens if something fails.
   */
  public JTextArea appStatusUpdates;
  /**
   * The Simple date format used by JXDatePicker.
   */
  public final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Instantiates a new Portfolio list panel.
   */
  public PortfolioListPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    selectionPanel = new JPanel();
    selectionPanel.setLayout(new GridLayout(0, 1, 30, 30));
    add(selectionPanel);
    statusPanel = new JPanel();
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.PAGE_AXIS));
    appStatusUpdates = new JTextArea(7, 0);
    appStatusUpdates.setEditable(false);
    appStatusUpdates.setText(
        "Welcome! You can start by viewing existing Portfolios or creating a new one."
            + "\nCommission set to $1 per transaction.");
    Border border = BorderFactory.createTitledBorder("Application Status Updates");
    appStatusUpdates.setBorder(border);
    scrollStatusPane = new JScrollPane(appStatusUpdates);
    scrollStatusPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    statusPanel.add(scrollStatusPane);
    add(statusPanel);
  }

  /**
   * Sets portfolio list to the new incoming (updated) list of portfolios.
   *
   * @param portfolios the portfolios
   */
  public void setupPortfolioList(String[] portfolios) {
    JLabel text = new JLabel("Select One of The Portfolios");
    text.setFont(new Font("Arial", Font.BOLD, 15));
    selectionPanel.add(text, BorderLayout.LINE_START);
    showSelectedDate = new JLabel();
    portfolioOptions = new JComboBox<>(portfolios);
    selectedPfID = portfolioOptions.getItemAt(0);
    portfolioOptions.addActionListener(
        e -> selectedPfID = (String) portfolioOptions.getSelectedItem());
    selectedButton = new JButton("Show Portfolio Info");
    selectionPanel.add(portfolioOptions, BorderLayout.CENTER);
    setDatePicker();
    selectionPanel.add(showSelectedDate);
    selectionPanel.add(selectedButton, BorderLayout.LINE_END);
  }

  /**
   * Update portfolio list is used to update the JComboBox drop down list to view the new list.
   *
   * @param portfolios the portfolios
   */
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
    selectionPanel.add(datePicker, BorderLayout.SOUTH);
    updateSelectedDateStatus(datePicker.getDate());
    // sets selected date upon click
    datePicker.addActionListener(e -> {
      selectedDate = datePicker.getDate();
      updateSelectedDateStatus(selectedDate);
    });
  }

  private void updateSelectedDateStatus(Date selectedDate) {
    selectedDateString = simpleDateFormat.format(selectedDate);
    showSelectedDate.setText("Selected Date is: " + selectedDateString);
  }

  private void setCalendarLimits(Calendar calendar) {
    calendar.setTime(new Date());
    datePicker.getMonthView().setUpperBound(calendar.getTime());
  }

}
