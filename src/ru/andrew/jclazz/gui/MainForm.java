package ru.andrew.jclazz.gui;

import ru.andrew.jclazz.core.Clazz;
import ru.andrew.jclazz.core.ClazzException;
import ru.andrew.jclazz.decompiler.FileInputStreamBuilder;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;

public class MainForm extends JFrame {

    private Clazz clazz;
    private File lastOpenedDirectory = null;

    private JMenuItem decompileMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu fileMenu;
    private JSeparator fileSeparator;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane3;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JMenuItem openMenuItem;
    private JMenu operationsMenu;
    private JSplitPane splitPanel;
    private JTextPane textPane;
    private JTree tree;

    public MainForm() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        initComponents();

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent tse) {
                ClazzTreeNode node = (ClazzTreeNode) tse.getPath().getLastPathComponent();
                textPane.setText(node.getDescription());
            }
        });
        tree.setCellRenderer(new ClazzTreeNodeCellRenderer());
        tree.setModel(new DefaultTreeModel(null));
        setContentPane(mainPanel);
    }

    protected void openClass() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class") || pathname.isDirectory();
            }

            public String getDescription() {
                return "Java class files";
            }
        });
        if (lastOpenedDirectory != null) {
            chooser.setCurrentDirectory(lastOpenedDirectory);
        }

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(this, "No class selected", "Exiting...", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            lastOpenedDirectory = chooser.getSelectedFile().getParentFile();
            this.clazz = new Clazz(chooser.getSelectedFile().getAbsolutePath(), new FileInputStreamBuilder());
        } catch (ClazzException ce) {
            JOptionPane.showMessageDialog(this, ce.toString(), "Clazz Exception", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, ioe.toString(), "Input/Output Exception", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle(clazz.getThisClassInfo().getFullyQualifiedName());
        decompileMenuItem.setEnabled(true);
        tree.setModel(new ClazzTreeUI(this.clazz).getTreeModel());
        tree.setSelectionRow(0);
        pack();
    }

    protected void openDecompileWindow() {
        DecompileForm decompileForm = new DecompileForm();
        try {
            decompileForm.setClazz(this.clazz);
        } catch (IllegalArgumentException iae) {
            return;
        }
        decompileForm.setModal(true);
        decompileForm.setSize(1000, 500);
        decompileForm.setVisible(true);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new JPanel();
        splitPanel = new JSplitPane();
        jScrollPane1 = new JScrollPane();
        tree = new JTree();
        jScrollPane3 = new JScrollPane();
        textPane = new JTextPane();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        openMenuItem = new JMenuItem();
        fileSeparator = new JSeparator();
        exitMenuItem = new JMenuItem();
        operationsMenu = new JMenu();
        decompileMenuItem = new JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jclazz");
        setName("mainFrame"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        mainPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        splitPanel.setDividerLocation(250);

        jScrollPane1.setViewportView(tree);

        splitPanel.setLeftComponent(jScrollPane1);

        textPane.setContentType("text/html");
        textPane.setEditable(false);
        jScrollPane3.setViewportView(textPane);

        splitPanel.setRightComponent(jScrollPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(splitPanel, gridBagConstraints);

        getContentPane().add(mainPanel);

        fileMenu.setText("File");

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openClass();
            }
        });
        fileMenu.add(openMenuItem);
        fileMenu.add(fileSeparator);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        operationsMenu.setText("Operations");

        decompileMenuItem.setText("Decompile");
        decompileMenuItem.setEnabled(false);
        decompileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDecompileWindow();
            }
        });
        operationsMenu.add(decompileMenuItem);

        menuBar.add(operationsMenu);

        setJMenuBar(menuBar);

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainForm mf = new MainForm();
                mf.setVisible(true);
            }
        });
    }
}
