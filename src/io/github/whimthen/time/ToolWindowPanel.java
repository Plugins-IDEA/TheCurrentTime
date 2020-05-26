package io.github.whimthen.time;

import com.intellij.codeInspection.ui.ListTable;
import com.intellij.codeInspection.ui.ListWrappingTableModel;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH;
import static com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED;
import static java.awt.GridBagConstraints.BOTH;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class ToolWindowPanel extends SimpleToolWindowPanel {

    private final Project project;
    private final JPanel formatterPanel;
    private final JPanel patternsPanel;
    private JLabel currentMills;
    private JLabel currentDateTime;
    private JPanel contentPanel;
    private ListTable listTable;

    public static ToolWindowPanel getInstance(@NotNull Project project) {
        return new ToolWindowPanel(project);
    }

    public ToolWindowPanel(@NotNull Project project) {
        super(true, true);
        this.project = project;
        this.formatterPanel = new JPanel();
        this.formatterPanel.setLayout(new BoxLayout(this.formatterPanel, BoxLayout.Y_AXIS));
        this.formatterPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        this.patternsPanel = new JPanel(new GridLayoutManager(1, 2, JBUI.emptyInsets(), 0, 0));

        this.initContentPanel();
        setContent(this.contentPanel);
        this.timer();
    }

    private void timer() {
        Timer timer = new Timer("TheCurrentTimer", true);
        long millis = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    ToolWindowPanel.this.currentMills.setText(TimeUtils.getCurrentTime(TimeUtils.MILLISECONDS));
                    ToolWindowPanel.this.currentDateTime.setText(TimeUtils.getCurrent());
                } catch (Exception err) {
                    // not deal
                }
            }
        }, 1000 - (millis - millis / 1000 * 1000), 1000);
    }

    private void initContentPanel() {
        this.contentPanel = UI.PanelFactory.grid().splitColumns()
                .add(UI.PanelFactory.panel(this.formatterPanel))
                .add(UI.PanelFactory.panel(this.patternsPanel))
                .createPanel();
        ((GridBagLayout) this.contentPanel.getLayout()).getConstraints(this.contentPanel).fill = BOTH;
        GridBagConstraints patternsConstraints = new GridBagConstraints(1, 1, 1,
                10, 1, 1, 21, BOTH, JBUI.emptyInsets(), 0, 0);
        ((GridBagLayout) this.contentPanel.getLayout()).setConstraints(this.patternsPanel, patternsConstraints);
        this.initFormatterPanel();
        this.initPatternsPanel();
    }

    private void initPatternsPanel() {
        JPanel patternsToolbar = this.initPatternsToolbar();
        GridConstraints constraints = new GridConstraints();
        constraints.setColumn(0);
        constraints.setFill(FILL_BOTH);

        this.listTable = new ListTable(getTableModel());

        JBScrollPane scrollPane = new JBScrollPane(listTable, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(JBUI.Borders.emptyLeft(5));
        this.patternsPanel.add(scrollPane, constraints);

        constraints = new GridConstraints();
        constraints.setColumn(1);
        constraints.setFill(FILL_BOTH);
        constraints.setHSizePolicy(SIZEPOLICY_FIXED);
        this.patternsPanel.add(patternsToolbar, constraints);
        this.patternsPanel.setBorder(JBUI.Borders.customLine(JBUI.CurrentTheme.ToolWindow.borderColor(), 1, 0, 0, 0));
    }

    @NotNull
    private ListWrappingTableModel getTableModel() {
        ListWrappingTableModel patternsTableModel = new ListWrappingTableModel(TimePatternService.getInstance().getPatterns(), "Pattern Settings");
        patternsTableModel.addTableModelListener(event -> {
            if (event.getType() == TableModelEvent.UPDATE) {
                int editingRow = this.listTable.getEditingRow();
                System.out.println(editingRow);
                if (editingRow != -1) {
                    Object value = this.listTable.getModel().getValueAt(editingRow, 0);
                    if (Objects.nonNull(value) && StringUtils.isNotBlank(value.toString())) {
                        TimePatternService.getInstance().save(value.toString());
                    } else {
                        this.listTable.getModel().removeRow(editingRow);
                    }
                }
            }
        });
        return patternsTableModel;
    }

    private JPanel initPatternsToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(Actions.getAddAction());
        actionGroup.add(Actions.getDeleteAction());
        actionGroup.add(Actions.getEditAction());

        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLWINDOW_TITLE, actionGroup, false);
        actionToolbar.setTargetComponent(toolbar);
        toolbar.add(actionToolbar.getComponent());
        toolbar.setBorder(JBUI.Borders.customLine(JBUI.CurrentTheme.ToolWindow.borderColor(), 0, 1, 0, 0));
        return toolbar;
    }

    private void initFormatterPanel() {
        JBInsets titleInsets = JBUI.insets(0, 5, 0, 0);
        this.currentMills = new JLabel(TimeUtils.getCurrentTime(TimeUtils.MILLISECONDS));
        this.currentDateTime = new JLabel(TimeUtils.getCurrent());
        JPanel currentTimePanel = UI.PanelFactory.grid().splitColumns()
                .add(UI.PanelFactory.panel(this.currentMills).resizeX(false).resizeY(false).withLabel("Milliseconds:"))
                .add(UI.PanelFactory.panel(this.currentDateTime).resizeX(false).resizeY(false).withLabel("LocalDateTime:"))
                .createPanel();
        currentTimePanel.setBorder(IdeBorderFactory.createTitledBorder("Current Time", true, titleInsets));
        this.formatterPanel.add(currentTimePanel);

        JBTextField fromMillis = new JBTextField();
        JBTextField toDate = new JBTextField();
        toDate.setEditable(false);
        String[] fromTypes = {TimeUtils.MILLISECONDS, TimeUtils.SECONDS};
        JComboBox<String> fromType = new ComboBox<>(fromTypes);
        JButton stampToDateBtn = new JButton("Timestamp to date");
        stampToDateBtn.addActionListener(event -> {
            try {
                String fromMillisText = fromMillis.getText();
                if (StringUtils.isBlank(fromMillisText)) {
                    fromMillisText = TimeUtils.getCurrentTime(TimeUtils.MILLISECONDS);
                }
                String fromTypeText = fromTypes[fromType.getSelectedIndex()];
                toDate.setText(TimeUtils.timestampToDate(fromMillisText, fromTypeText));
                fromMillis.setText(fromMillisText);
            } catch (Exception err) {
                Messages.showErrorDialog(this.project, err.getMessage(), "Timestamp To Date Error");
            }
        });
        JPanel millisToDatePanel = UI.PanelFactory.grid().splitColumns()
                .add(UI.PanelFactory.panel(fromType).withLabel("ConvertType:"))
                .add(UI.PanelFactory.panel(fromMillis).withLabel("Timestamp:").withComment("<p>The <em>Timestamp</em> default value is current milliseconds</p>"))
                .add(UI.PanelFactory.panel(toDate).withLabel("DateTime:"))
                .add(UI.PanelFactory.panel(UI.PanelFactory.panel(stampToDateBtn).resizeX(false).createPanel()))
                .createPanel();
        millisToDatePanel.setBorder(IdeBorderFactory.createTitledBorder("Timestamp To DateTime", true, titleInsets));
        this.formatterPanel.add(millisToDatePanel);

        final String fromDateToTimestampType = TimeUtils.YYYY_MM_DD_HH_MM_SS;
        JBTextField fromDate = new JBTextField();
        JBTextField toMillis = new JBTextField();
        toMillis.setEditable(false);
        fromDate.getEmptyText().setText(fromDateToTimestampType);
        JButton dateToStampBtn = new JButton("Date to millis");
        dateToStampBtn.addActionListener(event -> {
            try {
                String fromDateText = fromDate.getText();
//                int selectedRow = this.listTable.getSelectedRow();
//                if (selectedRow != -1) {
//                    fromDateToTimestampType = this.listTable.getValueAt(selectedRow, 0).toString();
//                }
                if (StringUtils.isBlank(fromDateText)) {
                    fromDateText = TimeUtils.getCurrentTime(fromDateToTimestampType);
                }
                toMillis.setText(TimeUtils.dateToTimestamp(fromDateText, fromDateToTimestampType));
                fromDate.setText(fromDateText);
            } catch (Exception err) {
                Messages.showErrorDialog(this.project, err.getMessage(), "Date To Milliseconds Error");
            }
        });
//        String dateTimeComment = "<p>The <em>DateTime</em> default value is current Date<br/>One of the following patterns needs to be selected, the default is <em>yyyy-MM-dd HH:mm:ss</em></p>";
        String dateTimeComment = "<p>The <em>DateTime</em> default value is current Date</p>";
        JPanel dateToMillisPanel = UI.PanelFactory.grid().splitColumns()
                .add(UI.PanelFactory.panel(fromDate).withLabel("DateTime:").withComment(dateTimeComment))
                .add(UI.PanelFactory.panel(toMillis).withLabel("Timestamp:"))
                .add(UI.PanelFactory.panel(UI.PanelFactory.panel(dateToStampBtn).resizeX(false).createPanel()))
                .createPanel();
        dateToMillisPanel.setBorder(IdeBorderFactory.createTitledBorder("DateTime To Milliseconds", true, titleInsets));
        this.formatterPanel.add(dateToMillisPanel);
    }

    public ListTable getListTable() {
        return this.listTable;
    }

}
